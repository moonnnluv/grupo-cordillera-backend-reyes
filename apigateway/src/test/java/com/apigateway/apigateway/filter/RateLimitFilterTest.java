package com.apigateway.apigateway.filter;

import jakarta.servlet.FilterChain;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.mock.web.MockHttpServletRequest;
import org.springframework.mock.web.MockHttpServletResponse;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.*;

class RateLimitFilterTest {

    private RateLimitFilter filter;

    @BeforeEach
    void setUp() {
        filter = new RateLimitFilter();
    }

    @Test
    void solicitudDentroDelLimite_pasaAlSiguienteFiltro() throws Exception {
        MockHttpServletRequest req = new MockHttpServletRequest();
        req.setRemoteAddr("10.0.0.1");
        MockHttpServletResponse res = new MockHttpServletResponse();
        FilterChain chain = mock(FilterChain.class);

        filter.doFilterInternal(req, res, chain);

        verify(chain, times(1)).doFilter(req, res);
        assertNotEquals(429, res.getStatus());
    }

    @Test
    void solicitudVeintiuno_superaLimite_retorna429() throws Exception {
        MockHttpServletRequest req = new MockHttpServletRequest();
        req.setRemoteAddr("10.0.0.2");

        // Consume the 20 allowed tokens
        for (int i = 0; i < 20; i++) {
            filter.doFilterInternal(req, new MockHttpServletResponse(), mock(FilterChain.class));
        }

        MockHttpServletResponse res = new MockHttpServletResponse();
        FilterChain chain = mock(FilterChain.class);
        filter.doFilterInternal(req, res, chain);

        assertEquals(429, res.getStatus());
        verify(chain, never()).doFilter(any(), any());
    }

    @Test
    void diferentesIPs_tienenBucketsIndependientes() throws Exception {
        MockHttpServletRequest req1 = new MockHttpServletRequest();
        req1.setRemoteAddr("192.168.1.1");
        MockHttpServletRequest req2 = new MockHttpServletRequest();
        req2.setRemoteAddr("192.168.1.2");

        // Exhaust IP1
        for (int i = 0; i < 20; i++) {
            filter.doFilterInternal(req1, new MockHttpServletResponse(), mock(FilterChain.class));
        }
        MockHttpServletResponse res1 = new MockHttpServletResponse();
        filter.doFilterInternal(req1, res1, mock(FilterChain.class));
        assertEquals(429, res1.getStatus());

        // IP2 must still have its own full bucket
        MockHttpServletResponse res2 = new MockHttpServletResponse();
        FilterChain chain2 = mock(FilterChain.class);
        filter.doFilterInternal(req2, res2, chain2);
        assertNotEquals(429, res2.getStatus());
        verify(chain2).doFilter(req2, res2);
    }

    @Test
    void respuesta429_incluyeJsonConMensajeDeError() throws Exception {
        MockHttpServletRequest req = new MockHttpServletRequest();
        req.setRemoteAddr("10.0.0.3");

        for (int i = 0; i < 20; i++) {
            filter.doFilterInternal(req, new MockHttpServletResponse(), mock(FilterChain.class));
        }

        MockHttpServletResponse res = new MockHttpServletResponse();
        filter.doFilterInternal(req, res, mock(FilterChain.class));

        assertEquals(429, res.getStatus());
        assertEquals("application/json", res.getContentType());
        String body = res.getContentAsString();
        assertTrue(body.contains("Too Many Requests"));
        assertTrue(body.contains("error"));
    }
}
