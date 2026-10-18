package com.cipa.nomina;

import com.cipa.nomina.domain.Empleado;
import com.cipa.nomina.domain.EmpleadoAsalariado;
import com.cipa.nomina.domain.EmpleadoPorComision;
import com.cipa.nomina.domain.EmpleadoPorHoras;
import com.cipa.nomina.domain.EmpleadoTemporal;
import com.cipa.nomina.service.CalculadoraNomina;
import com.cipa.nomina.service.ResumenNomina;

import java.math.BigDecimal;
import java.util.List;

/**
 * <p><b>Responsabilidad:</b> ofrecer un ejemplo ejecutable de liquidacion por consola.</p>
 * <p><b>Funcionamiento:</b> crea los empleados de ejemplo, los envia por la misma calculadora
 * y presenta sus resultados. El polimorfismo hace que cada objeto calcule su propio salario bruto.</p>
 *
 * <p>Metodologia de desarrollo: el proyecto se organiza de forma iterativa e incremental,
 * usando ciclos de pruebas, implementacion y refactorizacion (TDD) y revisiones de avance
 * propias de Scrum. Se eligio este enfoque porque la nomina contiene varias reglas separadas
 * y umbrales que deben comprobarse sin afectar el calculo de los otros empleados.</p>
 *
 * <p>En cada incremento se toma una regla, se define el resultado esperado, se agrega una
 * prueba unitaria, se implementa el cambio y se vuelve a ejecutar la suite. Luego se revisa
 * y refactoriza el codigo conservando el comportamiento. Por ejemplo, las pruebas comparan
 * ventas exactamente iguales a COP 20.000.000 con ventas superiores para asegurar que el bono
 * solo se aplique cuando corresponde. Los incrementos pequenos ayudan a localizar errores
 * pronto, evitar regresiones y dejar avances que el equipo pueda revisar mediante Git.</p>
 *
 * <p>La metodologia organiza el trabajo; la programacion orientada a objetos organiza el
 * codigo. Por eso cada subtipo calcula su propio salario bruto y la calculadora centraliza
 * las deducciones comunes.</p>
 */
public final class App {
    private App() {
    }

    public static void main(String[] args) {
        // Paso 1: configurar la tasa ARL; este valor es didactico y el equipo debe confirmar el real.
                BigDecimal tasaArl = new BigDecimal("0.00522");
                CalculadoraNomina calculadora = new CalculadoraNomina(tasaArl);

        // Paso 2: crear una instancia de cada tipo con los datos que necesita su formula de salario.
        List<Empleado> empleados = List.of(
                new EmpleadoAsalariado("1001", "Ana Asalariada", new BigDecimal("4500000"), 6),
                new EmpleadoPorHoras("1002", "Luis por Horas", new BigDecimal("25000"),
                        new BigDecimal("45"), 2, true),
                new EmpleadoPorComision("1003", "Marta por Comision", new BigDecimal("1800000"),
                        new BigDecimal("25000000"), new BigDecimal("0.05")),
                new EmpleadoTemporal("1004", "Carlos Temporal", new BigDecimal("2200000"), 6)
        );

                // Paso 3: pasar cada empleado por la misma calculadora; el polimorfismo calcula su salario bruto.
                System.out.println("LIQUIDACION DE NOMINA - VALORES EN COP");
                System.out.printf("Tasa ARL de ejemplo aplicada: %s%%%n%n",
                                tasaArl.multiply(new BigDecimal("100")).stripTrailingZeros().toPlainString());
        for (Empleado empleado : empleados) {
            ResumenNomina resumen = calculadora.calcular(empleado);
                        // Paso 4: imprimir cada concepto por separado para identificarlo con la rubrica.
                        imprimirResumen(empleado, resumen);
        }
    }

        /** Imprime el desglose con nombres explicitos para que se identifiquen ingresos y deducciones. */
        private static void imprimirResumen(Empleado empleado, ResumenNomina resumen) {
                System.out.println("----------------------------------------");
                System.out.printf("Empleado: %s (%s)%n", resumen.nombre(), empleado.getIdentificacion());
                System.out.printf("Tipo: %s%n", obtenerTipoEmpleado(empleado));
                System.out.printf("Salario bruto: COP %s%n", resumen.salarioBruto().toPlainString());
                System.out.printf("Deduccion obligatoria - Seguro Social y Pension (4%%): COP %s%n",
                                resumen.deduccionSeguridadYPension().toPlainString());
                System.out.printf("Deduccion obligatoria - ARL: COP %s%n", resumen.deduccionArl().toPlainString());
                System.out.printf("Beneficio - Fondo de ahorro (2%% si aplica y fue aceptado): COP %s%n",
                                resumen.deduccionFondoAhorro().toPlainString());
                System.out.printf("Beneficio empresarial - Alimentacion (no se descuenta): COP %s%n",
                                resumen.beneficioAlimentacion().toPlainString());
                System.out.printf("Salario neto a pagar: COP %s%n%n", resumen.salarioNeto().toPlainString());
        }

        /** Convierte la clase concreta a una etiqueta legible en el recibo de consola. */
        private static String obtenerTipoEmpleado(Empleado empleado) {
                if (empleado instanceof EmpleadoAsalariado) {
                        return "Asalariado";
                }
                if (empleado instanceof EmpleadoPorHoras) {
                        return "Por horas";
                }
                if (empleado instanceof EmpleadoPorComision) {
                        return "Por comision";
                }
                if (empleado instanceof EmpleadoTemporal) {
                        return "Temporal";
                }
                return "No especificado";
        }
}
