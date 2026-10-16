package com.cipa.nomina.domain;

import java.math.BigDecimal;

/**
 * <p><b>Responsabilidad:</b> representar al empleado con salario fijo y contrato temporal.</p>
 * <p><b>Funcionamiento:</b> exige una duracion contractual positiva y usa el salario mensual
 * como bruto, sin agregar bonos ni beneficios adicionales propios de este tipo.</p>
 */
public final class EmpleadoTemporal extends Empleado {
    private final BigDecimal salarioMensual;
    private final int duracionContratoMeses;

    public EmpleadoTemporal(String identificacion, String nombre, BigDecimal salarioMensual,
                            int duracionContratoMeses) {
        super(identificacion, nombre);
        // Paso 1: validar el salario; paso 2: exigir un contrato de al menos un mes.
        this.salarioMensual = requireNonNegative(salarioMensual, "El salario no puede ser negativo");
        if (duracionContratoMeses <= 0) {
            throw new IllegalArgumentException("La duracion del contrato debe ser mayor que cero");
        }
        this.duracionContratoMeses = duracionContratoMeses;
    }

    @Override
    public BigDecimal calcularSalarioBruto() {
        // No se agregan conceptos: el salario bruto temporal es el salario fijo mensual.
        return salarioMensual;
    }

    /** Indica la duracion pactada del contrato, expresada en meses. */
    public int getDuracionContratoMeses() {
        return duracionContratoMeses;
    }
}
