# Instrucciones del proyecto

- Proyecto de consola Maven en Java 17; paquetes bajo `com.cipa.nomina`.
- Usa `BigDecimal` para dinero y conserva el diseño orientado a objetos y las reglas descritas en `README.md`.
- Mantén el cálculo bruto específico en cada subtipo de `Empleado`; centraliza deducciones comunes en `CalculadoraNomina`.
- Agrega pruebas unitarias JUnit 5 para reglas nuevas y sus límites; ejecuta `mvn test` cuando Maven esté disponible.
- No fijes una tasa legal de ARL sin que el equipo la confirme; la tasa es configurable.
- No publiques ni configures un remoto GitHub sin autorización y datos del repositorio del CIPA.

## Estado de configuracion

- [x] Proyecto Maven Java 17 creado en la raiz.
- [x] Reglas de negocio, tipos de empleado y pruebas unitarias agregados.
- [x] README y metodologia de desarrollo documentados.
- [x] Extensiones adicionales: no requeridas.
- [x] Tarea de VS Code: no requerida; los comandos Maven estan en el README.
- [ ] Compilacion y pruebas: pendientes porque `java`, `javac` y `mvn` no estan disponibles en el entorno actual.
- [ ] Ejecucion: pendiente de confirmacion del usuario.
