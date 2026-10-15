package com.cipa.nomina.domain;

import java.math.BigDecimal;
import java.math.RoundingMode;

/**
 * <p><b>Responsabilidad:</b> representar al empleado de salario fijo mensual.</p>
 * <p><b>Funcionamiento:</b> calcula el salario base y, si la antiguedad supera cinco anios,
 * suma un bono mensual equivalente al 10% del salario.</p>
 */
public final class EmpleadoAsalariado extends Empleado {
    private static final BigDecimal BONO_ANTIGUEDAD = new BigDecimal("0.10");
    private final BigDecimal salarioMensual;
    private final int aniosServicio;

    public EmpleadoAsalariado(String identificacion, String nombre, BigDecimal salarioMensual, int aniosServicio) {
        super(identificacion, nombre);
        // Paso 1: validar salario y antiguedad para que la formula reciba datos permitidos.
        this.salarioMensual = requireNonNegative(salarioMensual, "El salario no puede ser negativo");
        if (aniosServicio < 0) {
            throw new IllegalArgumentException("Los anios de servicio no pueden ser negativos");
        }
        this.aniosServicio = aniosServicio;
    }

    @Override
    public BigDecimal calcularSalarioBruto() {
        // Paso 1: inicializar el bono en cero para quienes aun no superan cinco anios.
        // Paso 2: al superar cinco anios, calcular el 10% del sueldo mensual.
        BigDecimal bono = aniosServicio > 5
                ? salarioMensual.multiply(BONO_ANTIGUEDAD).setScale(2, RoundingMode.HALF_UP)
                : BigDecimal.ZERO;
        // Paso 3: sumar sueldo fijo y bono; el resultado es el bruto del periodo.
        return salarioMensual.add(bono);
    }

    @Override
    public boolean esPermanente() {
        // La calculadora usa esta respuesta para agregar el beneficio de alimentacion empresarial.
        return true;
    }
}
