package com.cipa.nomina.domain;

import java.math.BigDecimal;
import java.util.Objects;

/**
 * <p><b>Responsabilidad:</b> guardar la informacion compartida por todos los empleados y definir
 * el contrato comun para el calculo de nomina.</p>
 * <p><b>Funcionamiento:</b> las subclases implementan su propia formula de salario bruto; despues,
 * {@code CalculadoraNomina} aplica las deducciones comunes. Esta separacion permite agregar tipos
 * de empleado sin duplicar el calculo general.</p>
 */
public abstract class Empleado {
    private final String identificacion;
    private final String nombre;

    protected Empleado(String identificacion, String nombre) {
        // Paso 1: validar los datos comunes al crear el empleado para evitar objetos incompletos.
        this.identificacion = requireText(identificacion, "La identificacion es obligatoria");
        this.nombre = requireText(nombre, "El nombre es obligatorio");
    }

    /** Cada tipo concreto implementa aqui su formula de salario antes de deducciones. */
    public abstract BigDecimal calcularSalarioBruto();

    /** Devuelve si el empleado es permanente; por defecto, los tipos no permanentes responden falso. */
    public boolean esPermanente() {
        return false;
    }

    /** Devuelve si corresponde descontar el fondo; el empleado por horas evalua sus condiciones. */
    public boolean aplicaFondoAhorro() {
        return false;
    }

    /** Entrega la identificacion que se recibio y valido al crear el empleado. */
    public String getIdentificacion() {
        return identificacion;
    }

    /** Entrega el nombre que se recibio y valido al crear el empleado. */
    public String getNombre() {
        return nombre;
    }

    /** Valida importes compartidos: un valor nulo o negativo detiene la creacion del empleado. */
    protected static BigDecimal requireNonNegative(BigDecimal value, String message) {
        Objects.requireNonNull(value, message);
        if (value.signum() < 0) {
            throw new IllegalArgumentException(message);
        }
        return value;
    }

    /** Valida que los textos requeridos tengan contenido antes de almacenarlos. */
    private static String requireText(String value, String message) {
        if (value == null || value.isBlank()) {
            throw new IllegalArgumentException(message);
        }
        return value;
    }
}
