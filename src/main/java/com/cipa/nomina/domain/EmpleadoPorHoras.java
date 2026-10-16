package com.cipa.nomina.domain;

import java.math.BigDecimal;
import java.math.RoundingMode;

/**
 * <p><b>Responsabilidad:</b> representar a quien recibe pago segun las horas trabajadas.</p>
 * <p><b>Funcionamiento:</b> liquida hasta 40 horas a tarifa normal y las horas adicionales a 1.5;
 * tambien informa si aplica el fondo de ahorro segun antiguedad y aceptacion.</p>
 */
public final class EmpleadoPorHoras extends Empleado {
    private static final BigDecimal UMBRAL_HORAS = new BigDecimal("40");
    private static final BigDecimal MULTIPLICADOR_EXTRA = new BigDecimal("1.5");
    private final BigDecimal tarifaPorHora;
    private final BigDecimal horasTrabajadas;
    private final int aniosServicio;
    private final boolean aceptaFondoAhorro;

    public EmpleadoPorHoras(String identificacion, String nombre, BigDecimal tarifaPorHora,
                            BigDecimal horasTrabajadas, int aniosServicio, boolean aceptaFondoAhorro) {
        super(identificacion, nombre);
        // Paso 1: validar tarifa, horas y antiguedad antes de guardar los datos del periodo.
        this.tarifaPorHora = requireNonNegative(tarifaPorHora, "La tarifa por hora no puede ser negativa");
        this.horasTrabajadas = requireNonNegative(horasTrabajadas, "Las horas trabajadas no pueden ser negativas");
        if (aniosServicio < 0) {
            throw new IllegalArgumentException("Los anios de servicio no pueden ser negativos");
        }
        this.aniosServicio = aniosServicio;
        this.aceptaFondoAhorro = aceptaFondoAhorro;
    }

    @Override
    public BigDecimal calcularSalarioBruto() {
        // Paso 1: separar como horas normales las primeras 40 horas o menos.
        BigDecimal horasNormales = horasTrabajadas.min(UMBRAL_HORAS);
        // Paso 2: calcular las horas que quedan por encima del limite semanal.
        BigDecimal horasExtra = horasTrabajadas.subtract(horasNormales);
        // Paso 3: sumar pago normal y pago extra, que usa la tarifa multiplicada por 1.5.
        // Paso 4: redondear el salario bruto a dos decimales.
        return horasNormales.multiply(tarifaPorHora)
                .add(horasExtra.multiply(tarifaPorHora).multiply(MULTIPLICADOR_EXTRA))
                .setScale(2, RoundingMode.HALF_UP);
    }

    @Override
    public boolean aplicaFondoAhorro() {
        // El ahorro solo se descuenta si lleva mas de un anio y acepto participar.
        // Con ambas condiciones verdaderas, la calculadora reserva el 2% del salario bruto.
        return aceptaFondoAhorro && aniosServicio > 1;
    }
}
