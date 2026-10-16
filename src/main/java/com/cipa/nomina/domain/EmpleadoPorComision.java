package com.cipa.nomina.domain;

import java.math.BigDecimal;
import java.math.RoundingMode;

/**
 * <p><b>Responsabilidad:</b> representar al empleado permanente que vende y recibe una comision.</p>
 * <p><b>Funcionamiento:</b> suma el salario base y la comision sobre ventas; cuando estas superan
 * COP 20.000.000, agrega un bono del 3% sobre el total vendido.</p>
 */
public final class EmpleadoPorComision extends Empleado {
    private static final BigDecimal UMBRAL_VENTAS = new BigDecimal("20000000");
    private static final BigDecimal BONO_VENTAS = new BigDecimal("0.03");
    private final BigDecimal salarioBase;
    private final BigDecimal ventas;
    private final BigDecimal porcentajeComision;

    public EmpleadoPorComision(String identificacion, String nombre, BigDecimal salarioBase,
                               BigDecimal ventas, BigDecimal porcentajeComision) {
        super(identificacion, nombre);
        // Paso 1: validar salario, ventas y tasa de comision antes de aplicar las formulas.
        this.salarioBase = requireNonNegative(salarioBase, "El salario base no puede ser negativo");
        this.ventas = requireNonNegative(ventas, "Las ventas no pueden ser negativas");
        this.porcentajeComision = requireNonNegative(porcentajeComision, "La comision no puede ser negativa");
        if (porcentajeComision.compareTo(BigDecimal.ONE) > 0) {
            throw new IllegalArgumentException("La comision debe expresarse como una fraccion entre 0 y 1");
        }
    }

    @Override
    public BigDecimal calcularSalarioBruto() {
        // Paso 1: obtener la comision multiplicando las ventas por la tasa pactada.
        BigDecimal comision = ventas.multiply(porcentajeComision);
        // Paso 2: calcular el bono solo al superar el umbral; exactamente COP 20.000.000 no califica.
        BigDecimal bono = ventas.compareTo(UMBRAL_VENTAS) > 0
                ? ventas.multiply(BONO_VENTAS)
                : BigDecimal.ZERO;
        // Paso 3: sumar los tres conceptos y redondear el salario bruto a dos decimales.
        return salarioBase.add(comision).add(bono).setScale(2, RoundingMode.HALF_UP);
    }

    @Override
    public boolean esPermanente() {
        // La calculadora interpreta este tipo como elegible para alimentacion empresarial.
        return true;
    }
}
