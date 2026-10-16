package com.cipa.nomina.service;

import java.math.BigDecimal;

/**
 * <p><b>Responsabilidad:</b> transportar el resultado de una liquidacion sin recalcularlo.</p>
 * <p><b>Funcionamiento:</b> conserva en un objeto inmutable la identificacion, el bruto,
 * cada deduccion, el neto a pagar y la alimentacion cubierta por la empresa. Mantenerlos
 * separados permite mostrar y revisar el desglose de nomina.</p>
 *
 * @param identificacion documento del empleado liquidado
 * @param nombre nombre del empleado liquidado
 * @param salarioBruto ingreso calculado por el tipo concreto de empleado
 * @param deduccionSeguridadYPension deduccion conjunta del 4% del bruto
 * @param deduccionArl deduccion calculada con la tasa ARL configurada
 * @param deduccionFondoAhorro ahorro descontado cuando el empleado por horas califica
 * @param salarioNeto valor restante despues de las deducciones
 * @param beneficioAlimentacion beneficio empresarial, no descontado del neto
 */
public record ResumenNomina(
        String identificacion,
        String nombre,
        BigDecimal salarioBruto,
        BigDecimal deduccionSeguridadYPension,
        BigDecimal deduccionArl,
        BigDecimal deduccionFondoAhorro,
        BigDecimal salarioNeto,
        BigDecimal beneficioAlimentacion
) {
}
