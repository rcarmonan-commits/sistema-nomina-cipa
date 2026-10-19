# Sistema de Nomina CIPA

Aplicacion de consola en Java 17 para calcular la nomina mensual de empleados asalariados, por horas, por comision y temporales. Los valores monetarios se manejan con `BigDecimal` en pesos colombianos (COP).

## Reglas implementadas

- Asalariado: salario mensual fijo y bono del 10% cuando la antiguedad es mayor a cinco anos.
- Por horas: hasta 40 horas a tarifa normal; cada hora adicional se paga a 1.5 veces la tarifa.
- Por comision: salario base, porcentaje sobre ventas y bono adicional del 3% de ventas cuando estas superan COP 20.000.000.
- Temporal: salario mensual fijo y duracion de contrato positiva, sin bonos ni beneficios adicionales.
- Seguridad social y pension: deduccion total del 4% del salario bruto.
- ARL: deduccion con tasa configurable al construir `CalculadoraNomina`.
- Alimentacion: beneficio empresarial de COP 1.000.000 para empleados asalariados y por comision; no se resta del neto del trabajador.
- Fondo de ahorro: deduccion opcional del 2% del bruto para empleados por horas con mas de un ano de antiguedad que acepten participar.
- Se rechazan entradas negativas para salarios, tarifas, horas, ventas y tasas; la calculadora tambien rechaza un salario neto negativo.

## Decisiones y supuestos

- El enunciado no define la tasa de ARL. Por eso se inyecta como fraccion entre 0 y 1, sin fijar una tasa legal. El ejemplo de consola usa `0.00522` solo como dato demostrativo y debe ajustarse a la clase de riesgo aplicable.
- El 4% de seguridad social y pension se interpreta como una deduccion conjunta del 4%, segun el enunciado.
- La comision se expresa como fraccion: `0.05` equivale al 5%.
- El fondo de ahorro se contabiliza como deduccion del neto disponible, aunque el dinero queda destinado al ahorro del empleado.
- Los empleados temporales tienen contrato por un numero entero positivo de meses.
- Los importes se redondean a dos decimales con `HALF_UP`.

## Diseno

El dominio aplica polimorfismo: cada subtipo de `Empleado` calcula su salario bruto. `CalculadoraNomina` concentra las deducciones y beneficios comunes; `ResumenNomina` es un resultado inmutable. Esto mantiene cada regla cerca de su responsabilidad y permite extender tipos de empleado sin duplicar la liquidacion general.

## Metodologia de desarrollo

Se propone trabajar de forma iterativa e incremental, con practicas compatibles con Scrum y TDD:

1. Refinar historias y criterios de aceptacion a partir de las reglas de negocio.
2. Crear pruebas unitarias para umbrales y casos limite antes o junto con cada regla.
3. Implementar el dominio usando encapsulamiento, abstraccion y polimorfismo; aplicar SOLID, en particular responsabilidad unica y abierto/cerrado.
4. Refactorizar despues de cada incremento para quitar duplicacion y mantener nombres y metodos claros.
5. Integrar cambios pequenos mediante Git, ejecutar pruebas y revisar el resultado antes de compartir cada avance.

Para la exposicion CIPA, pueden mostrar una regla por tipo de empleado, la calculadora comun, las pruebas de limites, y el historial de commits creado por el equipo en su repositorio.

## Requisitos

- JDK 17
- Apache Maven 3.9 o posterior

## Compilar y probar

Desde la raiz del proyecto:

```powershell
mvn test
```

## Ejecutar la demostracion

```powershell
mvn -q package
java -cp target/classes com.cipa.nomina.App
```

La demostracion imprime cuatro liquidaciones de ejemplo. La tasa ARL se configura en `App` y se debe reemplazar por la que corresponda al caso que presente el equipo.

## Control de versiones y GitHub

Para evidenciar el trabajo del CIPA, inicialicen Git en esta carpeta, creen commits pequenos por funcionalidad y publiquen el repositorio en la cuenta u organizacion del equipo. No se ha configurado un remoto ni publicado este proyecto, porque se requiere la URL y el acceso del repositorio del grupo.
