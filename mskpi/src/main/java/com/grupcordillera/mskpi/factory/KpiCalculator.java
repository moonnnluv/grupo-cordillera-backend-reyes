package com.grupcordillera.mskpi.factory;

public interface KpiCalculator {
    String getTipo();
    double calcular(double valorBase);
    String getUnidad();
}