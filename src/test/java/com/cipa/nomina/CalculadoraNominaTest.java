package com.cipa.nomina;

import com.cipa.nomina.domain.EmpleadoAsalariado;
import com.cipa.nomina.domain.EmpleadoPorComision;
import com.cipa.nomina.domain.EmpleadoPorHoras;
import com.cipa.nomina.domain.EmpleadoTemporal;
import com.cipa.nomina.service.CalculadoraNomina;
import com.cipa.nomina.service.ResumenNomina;
import org.junit.jupiter.api.Test;

import java.math.BigDecimal;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertThrows;

/**
 * <p><b>Responsabilidad:</b> comprobar automaticamente las reglas de negocio de nomina.</p>
 * <p><b>Funcionamiento:</b> cada prueba prepara empleados, ejecuta la calculadora y compara
 * los importes obtenidos con los esperados; los casos de error verifican que se rechacen
 * datos prohibidos.</p>
 */
class CalculadoraNominaTest {
    // Se usa ARL cero en las pruebas generales para aislar las otras reglas; hay otra prueba para ARL.
    private final CalculadoraNomina calculadora = new CalculadoraNomina(BigDecimal.ZERO);

    /** Comprueba el limite de cinco anios y el beneficio de alimentacion del asalariado. */
    @Test
    void asalariadoRecibeBonoSoloDespuesDeCincoAniosYAlimentacion() {
        ResumenNomina justoCinco = calculadora.calcular(
                new EmpleadoAsalariado("1", "Ana", new BigDecimal("1000000"), 5));
        ResumenNomina masDeCinco = calculadora.calcular(
                new EmpleadoAsalariado("2", "Eva", new BigDecimal("1000000"), 6));

        assertEquals(new BigDecimal("1000000.00"), justoCinco.salarioBruto());
        assertEquals(new BigDecimal("1100000.00"), masDeCinco.salarioBruto());
        assertEquals(new BigDecimal("1000000.00"), masDeCinco.beneficioAlimentacion());
        assertEquals(new BigDecimal("44000.00"), masDeCinco.deduccionSeguridadYPension());
    }

    /** Comprueba horas normales, horas extra y ahorro aceptado con antiguedad suficiente. */
    @Test
    void empleadoPorHorasPagaHorasExtraYFondoSoloConAntiguedadYConsentimiento() {
        EmpleadoPorHoras empleado = new EmpleadoPorHoras("3", "Luis", new BigDecimal("10000"),
                new BigDecimal("42"), 2, true);
        ResumenNomina resumen = calculadora.calcular(empleado);

        assertEquals(new BigDecimal("430000.00"), resumen.salarioBruto());
        assertEquals(new BigDecimal("8600.00"), resumen.deduccionFondoAhorro());
        assertEquals(new BigDecimal("404200.00"), resumen.salarioNeto());
        assertEquals(BigDecimal.ZERO.setScale(2), resumen.beneficioAlimentacion());
    }

    /** Comprueba que negar el acceso al fondo evita la deduccion aunque haya antiguedad suficiente. */
    @Test
    void empleadoPorHorasNoAplicaFondoSiNoAcepta() {
        ResumenNomina resumen = calculadora.calcular(new EmpleadoPorHoras("4", "Luz",
                new BigDecimal("10000"), new BigDecimal("40"), 3, false));
        assertEquals(new BigDecimal("400000.00"), resumen.salarioBruto());
        assertEquals(BigDecimal.ZERO.setScale(2), resumen.deduccionFondoAhorro());
    }

}
