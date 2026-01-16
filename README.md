# Sistema de Gestión de Ayudantes FIS - EPN

Sistema completo para la gestión de ayudantes de investigación en la Facultad de Ingeniería de Sistemas de la Escuela Politécnica Nacional.

---

## 📋 Descripción

Sistema desarrollado en Java con interfaz gráfica Swing que permite gestionar:
- Proyectos de investigación
- Ayudantes asignados a proyectos
- Directores de proyectos
- Solicitudes de ayudantes
- Reportes y notificaciones

**Base de Datos**: SQLite con JDBC

---

## 🚀 Inicio Rápido

### Requisitos
- Java JDK 21+
- Sistema operativo: Windows/Linux/MacOS

### Compilar
```bash
cd "C:/Marck/prj_DiseñoSW"
javac -d bin src/**/*.java -cp "lib/sqlite-jdbc-3.44.0.0.jar;lib/slf4j-api-2.0.9.jar;lib/slf4j-simple-2.0.9.jar"
```

### Ejecutar

**Windows:**
```bash
java -cp "bin;lib/sqlite-jdbc-3.44.0.0.jar;lib/slf4j-api-2.0.9.jar;lib/slf4j-simple-2.0.9.jar" App
```

**Linux/MacOS:**
```bash
java -cp "bin:lib/sqlite-jdbc-3.44.0.0.jar:lib/slf4j-api-2.0.9.jar:lib/slf4j-simple-2.0.9.jar" App
```

---

## 🔐 Credenciales de Acceso

### Jefa de Departamento
- **Usuario**: `jefa@epn.edu.ec`
- **Contraseña**: `123456`
- **Permisos**: Gestión completa del sistema

### Director de Proyecto
- **Usuario**: `director@epn.edu.ec`
- **Contraseña**: `123456`
- **Permisos**: Gestión de su proyecto y ayudantes

---

## 📊 Base de Datos

### Información
- **Archivo**: `bd/gestion_ayudantes.db` (SQLite)
- **Tamaño**: ~216 KB
- **Registros**: 96+ (48 directores, 30 ayudantes, 19 proyectos)

### Estructura de Tablas
1. **MiembroFIS** - Usuarios (directores, ayudantes, jefa)
2. **ProyectoInvestigacion** - Proyectos de investigación
3. **AsignacionAyudanteProyecto** - Relación ayudante-proyecto
4. **Mensaje** - Sistema de mensajería
5. **Notificacion** - Notificaciones del sistema

### Consultar la BD
```bash
sqlite3 bd/gestion_ayudantes.db

# Ver proyectos
SELECT codigo_proyecto, nombre_proyecto, estado FROM ProyectoInvestigacion;

# Ver ayudantes activos
SELECT nombres, apellidos, carrera FROM MiembroFIS 
WHERE tipo_miembro='AYUDANTE' AND estado='ACTIVO';
```

Ver consultas útiles en: `bd/CONSULTAS_UTILES.sql`

---

## 📁 Estructura del Proyecto

```
prj_DiseñoSW/
├── src/
│   ├── App.java                 # Punto de entrada
│   ├── controladores/           # Lógica de control
│   ├── dominio/                 # Modelos de negocio
│   ├── gestores/                # Gestores de notificaciones/reportes
│   ├── persistencia/            # DAOs con JDBC/SQLite
│   ├── sistema/                 # Sistema principal
│   └── vistas/                  # Interfaces gráficas (Swing)
├── bd/
│   ├── gestion_ayudantes.db     # Base de datos SQLite
│   └── CONSULTAS_UTILES.sql     # Queries de ejemplo
├── lib/
│   ├── sqlite-jdbc-3.44.0.0.jar # Driver JDBC SQLite
│   ├── slf4j-api-2.0.9.jar      # API de logging
│   └── slf4j-simple-2.0.9.jar   # Implementación logging
├── bin/                         # Clases compiladas
├── UML/                         # Diagramas PlantUML
└── README.md                    # Este archivo
```

---

## 🔧 Arquitectura

### Patrón MVC + DAO
- **Modelo**: Clases en `dominio/`
- **Vista**: Interfaces Swing en `vistas/`
- **Controlador**: Lógica en `controladores/`
- **DAO**: Persistencia JDBC en `persistencia/`

### Patrón Singleton
- `SistemaGestionAyudantes` - Sistema principal
- `JefaDepartamento` - Instancia única de jefa

### Base de Datos
- SQLite con JDBC
- PreparedStatements (SQL injection safe)
- Foreign keys habilitadas
- INSERT OR REPLACE para upserts

---

## 🎯 Funcionalidades Principales

### Para Jefa de Departamento
- ✓ Visualizar todos los proyectos
- ✓ Aprobar/rechazar solicitudes de ayudantes
- ✓ Gestionar ayudantes activos
- ✓ Generar reportes
- ✓ Ver notificaciones del sistema

### Para Director
- ✓ Gestionar su proyecto asignado
- ✓ Solicitar ayudantes
- ✓ Asignar tareas a ayudantes
- ✓ Registrar horas de trabajo
- ✓ Enviar mensajes

---

## ⚠️ Solución de Problemas

### Error: ClassNotFoundException: org.slf4j.LoggerFactory
**Solución**: Incluir SLF4J en el classpath
```bash
java -cp "bin;lib/sqlite-jdbc-3.44.0.0.jar;lib/slf4j-api-2.0.9.jar;lib/slf4j-simple-2.0.9.jar" App
```

### Error: ClassNotFoundException: App
**Solución Windows**: Usar `;` como separador
```bash
java -cp "bin;lib/*" App
```

**Solución Linux/Mac**: Usar `:` como separador
```bash
java -cp "bin:lib/*" App
```

### Contraseña incorrecta
**Solución**: Recompilar después de últimos cambios
```bash
javac -d bin src/persistencia/MiembroFISDAO.java -cp "lib/*;bin"
```

---

## 🔄 Migración de Serialización a SQLite

Este proyecto fue migrado de serialización Java (.dat files) a SQLite:
- ✓ Todos los DAOs ahora usan JDBC
- ✓ Base de datos poblada con datos reales
- ✓ Sin archivos .dat (obsoletos)
- ✓ Mejor rendimiento y consultas SQL

---

## 📝 Consultas SQL Útiles

```sql
-- Contar proyectos por estado
SELECT estado, COUNT(*) as total 
FROM ProyectoInvestigacion 
GROUP BY estado;

-- Ayudantes por proyecto
SELECT p.nombre_proyecto, COUNT(a.numero_unico_ayudante) as num_ayudantes
FROM ProyectoInvestigacion p
LEFT JOIN AsignacionAyudanteProyecto a ON p.codigo_proyecto = a.codigo_proyecto
GROUP BY p.codigo_proyecto;

-- Directores sin proyecto
SELECT m.nombres, m.apellidos, m.correo_institucional
FROM MiembroFIS m
LEFT JOIN ProyectoInvestigacion p ON m.numero_unico = p.numero_unico_director
WHERE m.tipo_miembro = 'DIRECTOR' 
  AND p.codigo_proyecto IS NULL;
```

---

## 📦 Dependencias

| Librería | Versión | Propósito |
|----------|---------|-----------|
| SQLite JDBC | 3.44.0.0 | Driver de base de datos |
| SLF4J API | 2.0.9 | API de logging |
| SLF4J Simple | 2.0.9 | Implementación de logging |
| Java Swing | JDK 21 | Interfaz gráfica |

---

## 👥 Autores

- Sistema desarrollado para la Facultad de Ingeniería de Sistemas - EPN
- Fecha: Enero 2026

---

## 📄 Licencia

Proyecto académico - Escuela Politécnica Nacional

---

## 🆘 Soporte

Para problemas o consultas:
1. Verificar que Java JDK 21+ esté instalado
2. Confirmar que todas las librerías estén en `/lib/`
3. Revisar que la base de datos esté en `/bd/`
4. Recompilar el proyecto desde cero si hay errores

**Estado**: ✅ Sistema completamente funcional con SQLite integrado
