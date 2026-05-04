package com.grupcordillera.mskpi.factory.impl;

import org.springframework.stereotype.Component;

import com.grupcordillera.mskpi.factory.KpiCalculator;

@Component
public class FinanceKpiCalculator implements KpiCalculator {
    @Override public String getTipo() { return "RENTABILIDAD"; }
    @Override public double calcular(double valorBase) {
        return Math.round(valorBase * 1.15 * 100.0) / 100.0;
    }
    @Override public String getUnidad() { return "CLP"; }
}