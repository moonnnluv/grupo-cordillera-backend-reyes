package com.grupcordillera.msbff.dto;

import java.util.List;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

// Este DTO agrega datos de múltiples microservicios en una sola respuesta
// Eso es el rol del BFF: transformar y consolidar para el frontend
@Data
@NoArgsConstructor
@AllArgsConstructor
public class DashboardResponse {
    private List<Object> datos;
    private List<Object> kpis;
    private List<Object> reportes;
    private String estado;
}