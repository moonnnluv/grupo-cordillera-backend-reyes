package com.grupcordillera.mskpi.factory.impl;

import org.springframework.stereotype.Component;

import com.grupcordillera.mskpi.factory.KpiCalculator;

@Component
public class InventoryKpiCalculator implements KpiCalculator {
    @Override public String getTipo() { return "INVENTARIO"; }
    @Override public double calcular(double valorBase) {
        return Math.round((valorBase / 30.0) * 100.0) / 100.0;
    }
    @Override public String getUnidad() { return "unidades/día"; }
}