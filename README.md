# Tarea 4 - Formulario, Patrones y Conexiones a BD

**Programación I**  
**ITLA - Instituto Tecnológico de Las Américas**  
**Profesor:** Kelyn Tejada Belliard

**Estudiante:** Josías David Alcalá Gómez  
**Matrícula:** 2025-1088  
**Grupo:** 3

## Descripción

Aplicación de escritorio desarrollada en Java con Swing y MySQL. Permite iniciar sesión, registrar usuarios y administrar el listado de clientes registrados. La interfaz está construida con Swing y la persistencia se realiza mediante JDBC.

## Funcionalidades

- Inicio de sesión con usuario y contraseña oculta.
- Mensaje de error cuando faltan el usuario o la contraseña.
- Registro con siete campos: usuario, nombre, apellido, teléfono, correo electrónico, contraseña y confirmación.
- Validación de campos obligatorios, indicando cuál falta.
- Validación de que la contraseña y su confirmación coincidan.
- Listado de clientes con nombre, apellido, teléfono, correo electrónico y usuario.
- Creación, actualización y eliminación de usuarios.
- Actualización de datos sin obligar a cambiar la contraseña.
- Borrado lógico mediante `deleted_at`.
- Cierre de sesión y regreso al login.

## Tecnologías

- Java 17
- Swing
- MySQL 8
- JDBC con MySQL Connector/J 9.0.0
- Eclipse (opcional)

## Base de datos

Nombre: `tarea4_db`  
Tabla: `usuarios`

Columnas: `idUser`, `UserName`, `Nombre`, `Apellido`, `Telefono`, `Email`, `Password`, `created_at`, `updated_at`, `deleted_at`.

`UserName` y `Email` son únicos. `Password` almacena el hash en formato `salt:hash`; la contraseña nunca se guarda en texto plano. `deleted_at` se usa para el borrado lógico.

### Creación de la base

1. Abrir MySQL Workbench y conectar a la instancia local (`localhost:3306`).
2. Abrir `script.sql` y ejecutarlo.
3. Verificar que aparezca `tarea4_db` con la tabla `usuarios`.

Si la base ya existe con otro esquema, se puede eliminar y volver a crear:

```sql
DROP DATABASE IF EXISTS tarea4_db;
```

Después de eso, ejecutar `script.sql` de nuevo.

### Configuración de la conexión

La conexión se configura en `src/util/ConexionDB.java`:

```java
private static final String URL = "jdbc:mysql://localhost:3306/tarea4_db?useSSL=false&allowPublicKeyRetrieval=true&serverTimezone=UTC";
private static final String USUARIO = "root";
private static final String CLAVE = "TU_CLAVE_LOCAL";
```

`USUARIO` y `CLAVE` deben corresponder a la instalación local de MySQL.

## Cómo ejecutar

### Eclipse

1. **File → Import → General → Existing Projects into Workspace**.
2. Seleccionar la carpeta del proyecto.
3. Ejecutar `Main` como **Java Application**.

El proyecto incluye `.classpath` con el conector de `lib/`.

### Consola (Windows)

Desde la raíz del proyecto, en PowerShell:

```powershell
New-Item -ItemType Directory -Force bin | Out-Null
$files = (Get-ChildItem -Recurse -Filter *.java src).FullName
javac -encoding UTF-8 -cp "lib/mysql-connector-j-9.0.0.jar" -d bin $files Main.java
java -cp "bin;lib/mysql-connector-j-9.0.0.jar" Main
```

En Git Bash:

```bash
mkdir -p bin
javac -encoding UTF-8 -cp "lib/mysql-connector-j-9.0.0.jar" -d bin src/**/*.java Main.java
java -cp "bin;lib/mysql-connector-j-9.0.0.jar" Main
```

En Windows el separador del classpath es `;`.

## Estructura del proyecto

```text
Tarea4-Formulario-Patrones-ConexionesBD/
├── .classpath
├── .gitignore
├── .project
├── LICENSE
├── README.md
├── script.sql
├── Main.java
├── lib/
│   └── mysql-connector-j-9.0.0.jar   (controlador JDBC)
└── src/
    ├── util/
    │   ├── ConexionDB.java            (Singleton - conexión a BD)
    │   └── PasswordUtil.java          (PBKDF2 con HMAC-SHA-256)
    ├── model/
    │   ├── Persona.java               (Clase abstracta)
    │   └── Usuario.java               (Extiende Persona)
    ├── dao/
    │   ├── UsuarioDAO.java            (Interfaz)
    │   └── UsuarioDAOMySQL.java       (Implementación MySQL)
    ├── view/
    │   ├── BaseFrame.java             (Extiende JFrame)
    │   ├── Login.java
    │   ├── Registro.java
    │   └── Principal.java
    └── factory/
        └── VentanaFactory.java        (Patrón Fábrica)
```

## Aspectos de diseño

- **Abstracción:** `Persona` es una clase abstracta y `UsuarioDAO` define las operaciones de acceso a datos sin depender de una implementación concreta.
- **Encapsulamiento:** los atributos de `Persona` y `Usuario` son privados y se acceden mediante getters y setters.
- **Herencia:** `Usuario` extiende `Persona`; `Login`, `Registro` y `Principal` extienden `BaseFrame`.
- **Polimorfismo:** `UsuarioDAOMySQL` implementa `UsuarioDAO`, y `Usuario` sobrescribe `getRol()`.
- **Singleton:** `ConexionDB` mantiene una única conexión a la base de datos.
- **Fábrica:** `VentanaFactory` centraliza la creación y navegación de las ventanas.
- **DAO:** `UsuarioDAO` separa el contrato de acceso a datos de su implementación en MySQL.
- **PreparedStatement:** todas las consultas usan parámetros `?`.
- **PBKDF2 + salt:** `PasswordUtil` genera el hash de cada contraseña con un salt aleatorio.
- **Borrado lógico:** los usuarios eliminados se marcan con `deleted_at` y se excluyen de las consultas.

## Notas de implementación

- La conexión se mantiene abierta durante la ejecución; los DAO cierran únicamente el `PreparedStatement` y el `ResultSet`.
- La tabla de la pantalla principal se actualiza después de registrar, actualizar o eliminar un usuario.
- En el modo de edición, los campos de contraseña pueden dejarse vacíos para conservar la contraseña actual.

## Problemas comunes

| Error | Solución |
|---|---|
| `Unknown database 'tarea4_db'` | Ejecutar `script.sql`. |
| `Access denied for user 'root'` | Revisar `USUARIO` y `CLAVE` en `ConexionDB.java`. |
| `Communications link failure` | Verificar que el servicio de MySQL esté iniciado. |
| `Public Key Retrieval is not allowed` | La URL ya incluye `allowPublicKeyRetrieval=true`. |

## Licencia

Proyecto académico de la Tarea 4 de Programación I. Consulta `LICENSE`.
