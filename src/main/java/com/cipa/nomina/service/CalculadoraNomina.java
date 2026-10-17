package com.cipa.nomina.service;

import com.cipa.nomina.domain.Empleado;

import java.math.BigDecimal;
import java.math.RoundingMode;
import java.util.Objects;

/**
 * <p><b>Responsabilidad:</b> coordinar la liquidacion mensual sin conocer la formula particular
 * de cada tipo de empleado.</p>
 * <p><b>Funcionamiento:</b> solicita el bruto al empleado, aplica deducciones comunes, verifica
 * el neto y devuelve cada importe separado en un {@link ResumenNomina}.</p>
 */
public final class CalculadoraNomina {
    private static final BigDecimal TASA_SEGURIDAD_Y_PENSION = new BigDecimal("0.04");
    private static final BigDecimal TASA_FONDO_AHORRO = new BigDecimal("0.02");
    private static final BigDecimal CERO_MONETARIO = new BigDecimal("0.00");
    private static final BigDecimal BENEFICIO_ALIMENTACION = new BigDecimal("1000000.00");
    private final BigDecimal tasaArl;

    /**
     * Crea la calculadora con la tasa ARL que defina la empresa.
     * La tasa se inyecta porque la actividad no entrega un porcentaje para usar como regla legal.
     *
     * @param tasaArl tasa como fraccion entre 0 y 1; por ejemplo, 0.01 representa 1%
     */
    public CalculadoraNomina(BigDecimal tasaArl) {
        Objects.requireNonNull(tasaArl, "La tasa ARL es obligatoria");
        // Impedir tasas fuera del rango evita deducciones negativas o porcentajes mayores al 100%.
        if (tasaArl.signum() < 0 || tasaArl.compareTo(BigDecimal.ONE) > 0) {
            throw new IllegalArgumentException("La tasa ARL debe estar entre 0 y 1");
        }
        this.tasaArl = tasaArl;
    }

    /**
     * Liquida un empleado siguiendo el flujo de nomina.
     * Primero calcula bruto y deducciones; luego valida que el neto no sea negativo y agrega
     * el beneficio empresarial que corresponda.
     *
     * @param empleado empleado concreto; su subtipo determina la formula del salario bruto
     * @return desglose monetario de la liquidacion del empleado
     */
    public ResumenNomina calcular(Empleado empleado) {
        Objects.requireNonNull(empleado, "El empleado es obligatorio");

        // Etapa 1: pedir al subtipo su salario bruto y normalizarlo a dos decimales.
        BigDecimal bruto = empleado.calcularSalarioBruto().setScale(2, RoundingMode.HALF_UP);

        // Etapa 2: calcular la deduccion conjunta de seguridad/pension y la ARL configurada.
        BigDecimal seguridadYPension = porcentaje(bruto, TASA_SEGURIDAD_Y_PENSION);
        BigDecimal arl = porcentaje(bruto, tasaArl);

        // Etapa 3: consultar al empleado; si califica, reservar el 2% para su fondo de ahorro.
        BigDecimal ahorro = empleado.aplicaFondoAhorro()
            ? porcentaje(bruto, TASA_FONDO_AHORRO)
            : CERO_MONETARIO;

        // Etapa 4: restar todas las deducciones al bruto para obtener el neto disponible.
        BigDecimal neto = bruto.subtract(seguridadYPension).subtract(arl).subtract(ahorro)
                .setScale(2, RoundingMode.HALF_UP);
        if (neto.signum() < 0) {
            throw new IllegalArgumentException("El salario neto no puede ser negativo");
        }

        // Etapa 5: informar alimentacion para empleados permanentes; la paga la empresa y no se deduce.
        BigDecimal alimentacion = empleado.esPermanente() ? BENEFICIO_ALIMENTACION : CERO_MONETARIO;

        // Se devuelve cada importe por separado para que el recibo pueda mostrar el desglose.
        return new ResumenNomina(empleado.getIdentificacion(), empleado.getNombre(), bruto,
                seguridadYPension, arl, ahorro, neto, alimentacion);
    }

    private BigDecimal porcentaje(BigDecimal base, BigDecimal tasa) {
        // Multiplicar el bruto por la fraccion produce el valor monetario de la deduccion.
        // HALF_UP conserva dos decimales y redondea el tercer decimal hacia arriba desde cinco.
        return base.multiply(tasa).setScale(2, RoundingMode.HALF_UP);
    }
}
