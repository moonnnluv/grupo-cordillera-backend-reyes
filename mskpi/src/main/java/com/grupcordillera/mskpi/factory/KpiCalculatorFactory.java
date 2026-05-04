package com.grupcordillera.mskpi.factory;

import java.util.List;
import java.util.Map;
import java.util.function.Function;
import java.util.stream.Collectors;

import org.springframework.stereotype.Component;

@Component
public class KpiCalculatorFactory {

    private final Map<String, KpiCalculator> calculatorMap;

    // Spring inyecta automáticamente todas las implementaciones de KpiCalculator
    public KpiCalculatorFactory(List<KpiCalculator> calculators) {
        this.calculatorMap = calculators.stream()
                .collect(Collectors.toMap(KpiCalculator::getTipo, Function.identity()));
    }

    public KpiCalculator getCalculator(String tipo) {
        KpiCalculator calculator = calculatorMap.get(tipo.toUpperCase());
        if (calculator == null) {
            throw new IllegalArgumentException("Tipo de KPI no soportado: " + tipo);
        }
        return calculator;
    }
}