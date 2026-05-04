package com.grupcordillera.mskpi.factory.impl;

import org.springframework.stereotype.Component;

import com.grupcordillera.mskpi.factory.KpiCalculator;

@Component
public class SalesKpiCalculator implements KpiCalculator {
    @Override public String getTipo() { return "VENTAS"; }
    @Override public double calcular(double valorBase) {
        return Math.round(valorBase * 0.35 * 100.0) / 100.0;
    }
    @Override public String getUnidad() { return "%"; }
}