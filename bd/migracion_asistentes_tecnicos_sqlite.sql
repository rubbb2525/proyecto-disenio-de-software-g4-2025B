-- database: gestion_ayudantes.db
-- ===========================
-- MIGRACIÓN SQLITE: Soporte para Asistentes y Técnicos de Investigación
-- ===========================
-- Archivo: migracion_asistentes_tecnicos_sqlite.sql
-- Este script actualiza la base de datos SQLite para soportar los nuevos tipos de contratación

PRAGMA foreign_keys = OFF;

-- ===========================
-- 1. CREAR TABLAS NUEVAS PARA ASISTENTES Y TÉCNICOS
-- ===========================

-- Tabla de Asistentes de Investigación (contratación profesional)
-- Son miembros de la universidad con formación avanzada
CREATE TABLE IF NOT EXISTS asistentes (
    codigo_unico TEXT PRIMARY KEY,
    cedula TEXT NOT NULL UNIQUE,
    correo_institucional TEXT NOT NULL UNIQUE,
    nombres TEXT NOT NULL,
    apellidos TEXT NOT NULL,
    telefono TEXT,
    carrera TEXT NOT NULL,
    nivel INTEGER NOT NULL,
    ira REAL NOT NULL,
    titulo_academico TEXT,  -- Nuevo: título académico del asistente
    area_especializacion TEXT,  -- Nuevo: área de especialización
    horas_semanales INTEGER NOT NULL,
    salario_mensual REAL NOT NULL,
    estado TEXT NOT NULL DEFAULT 'ACTIVO',
    fecha_registro DATETIME NOT NULL,
    fecha_finalizacion DATETIME,
    motivo_salida TEXT,
    codigo_proyecto TEXT,
    FOREIGN KEY (codigo_unico) REFERENCES miembros_epn(codigo_unico),
    FOREIGN KEY (codigo_proyecto) REFERENCES proyectos(codigo_proyecto)
);

-- Tabla de Técnicos de Investigación (contratación técnica - externos)
-- Son personal externo sin vinculación universitaria


CREATE TABLE IF NOT EXISTS tecnicos (
    id_tecnico TEXT PRIMARY KEY,
    cedula TEXT NOT NULL UNIQUE,
    correo_electronico TEXT NOT NULL UNIQUE,
    nombres TEXT NOT NULL,
    apellidos TEXT NOT NULL,
    telefono TEXT,
    especialidad_tecnica TEXT NOT NULL,
    anios_experiencia INTEGER NOT NULL,
    empresa_origen TEXT,
    horas_semanales INTEGER NOT NULL,
    salario_mensual REAL NOT NULL,
    estado TEXT NOT NULL DEFAULT 'ACTIVO',
    fecha_registro DATETIME NOT NULL,
    fecha_finalizacion DATETIME,
    motivo_salida TEXT,
    codigo_proyecto TEXT,
    FOREIGN KEY (codigo_proyecto) REFERENCES proyectos(codigo_proyecto)
);

-- ===========================
-- 2. CREAR ÍNDICES PARA OPTIMIZACIÓN
-- ===========================

-- Índices para asistentes
CREATE INDEX IF NOT EXISTS idx_asistentes_estado ON asistentes(estado);
CREATE INDEX IF NOT EXISTS idx_asistentes_proyecto ON asistentes(codigo_proyecto);
CREATE INDEX IF NOT EXISTS idx_asistentes_carrera ON asistentes(carrera);
CREATE INDEX IF NOT EXISTS idx_asistentes_titulo ON asistentes(titulo_academico);

-- Índices para técnicos
CREATE INDEX IF NOT EXISTS idx_tecnicos_estado ON tecnicos(estado);
CREATE INDEX IF NOT EXISTS idx_tecnicos_proyecto ON tecnicos(codigo_proyecto);
CREATE INDEX IF NOT EXISTS idx_tecnicos_especialidad ON tecnicos(especialidad_tecnica);
CREATE INDEX IF NOT EXISTS idx_tecnicos_empresa ON tecnicos(empresa_origen);

PRAGMA foreign_keys = ON;

-- ===========================
-- 3. VISTAS PARA CONSULTAS UNIFICADAS
-- ===========================

-- Vista unificada de todo el personal (ayudantes, asistentes y técnicos)
CREATE VIEW IF NOT EXISTS v_personal_completo AS
SELECT 
    codigo_unico,
    nombres,
    apellidos,
    cedula,
    'AYUDANTE' as tipo_personal,
    carrera as area_trabajo,
    nivel,
    ira,
    horas_semanales,
    salario_mensual,
    estado,
    fecha_registro,
    codigo_proyecto
FROM ayudantes
UNION ALL
SELECT 
    codigo_unico,
    nombres,
    apellidos,
    cedula,
    'ASISTENTE' as tipo_personal,
    area_especializacion as area_trabajo,
    nivel,
    ira,
    horas_semanales,
    salario_mensual,
    estado,
    fecha_registro,
    codigo_proyecto
FROM asistentes
UNION ALL
SELECT 
    codigo_unico,
    nombres,
    apellidos,
    cedula,
    'TECNICO' as tipo_personal,
    especialidad_tecnica as area_trabajo,
    NULL as nivel,
    NULL as ira,
    horas_semanales,
    salario_mensual,
    estado,
    fecha_registro,
    codigo_proyecto
FROM tecnicos;

-- Vista de personal activo por proyecto
CREATE VIEW IF NOT EXISTS v_personal_activo_por_proyecto AS
SELECT 
    p.codigo_proyecto,
    p.nombre_proyecto,
    pc.tipo_personal,
    pc.nombres || ' ' || pc.apellidos as nombre_completo,
    pc.area_trabajo,
    pc.horas_semanales,
    pc.salario_mensual,
    pc.estado
FROM proyectos p
LEFT JOIN v_personal_completo pc ON p.codigo_proyecto = pc.codigo_proyecto
WHERE pc.estado = 'ACTIVO'
ORDER BY p.codigo_proyecto, pc.tipo_personal;

-- Vista de estadísticas por tipo de personal
CREATE VIEW IF NOT EXISTS v_estadisticas_personal AS
SELECT 
    tipo_personal,
    COUNT(*) as total,
    SUM(CASE WHEN estado = 'ACTIVO' THEN 1 ELSE 0 END) as activos,
    SUM(horas_semanales) as horas_totales,
    SUM(salario_mensual) as costo_total_mensual,
    AVG(salario_mensual) as salario_promedio
FROM v_personal_completo
GROUP BY tipo_personal;

-- ===========================
-- 4. EJEMPLOS DE INSERCIÓN (COMENTADOS)
-- ===========================

-- Ejemplo: Insertar un Asistente de Investigación

-- Primero insertar en miembros_epn
INSERT INTO miembros_epn (codigo_unico, cedula, correo_institucional, password, nombres, apellidos, telefono, rol, estado)
VALUES ('ASIST001', '1234567890', 'asistente.prueba@epn.edu.ec', 'N/A', 'JUAN CARLOS', 'PEREZ LOPEZ', '0999999999', 'ASISTENTE', 'ACTIVO');

-- Luego insertar en asistentes
INSERT INTO asistentes (
    codigo_unico, cedula, correo_institucional, nombres, apellidos, telefono,
    carrera, nivel, ira, titulo_academico, area_especializacion,
    horas_semanales, salario_mensual, estado, fecha_registro, codigo_proyecto
) VALUES (
    'ASIST001', '1234567890', 'asistente.prueba@epn.edu.ec', 
    'JUAN CARLOS', 'PEREZ LOPEZ', '0999999999',
    'SOFTWARE', 10, 9.5, 'Magíster en Ciencias de la Computación', 'Inteligencia Artificial',
    30, 800.00, 'ACTIVO', datetime('now'), 'PII-19-02'
);


-- Ejemplo: Insertar un Técnico de Investigación

-- Primero insertar en miembros_epn
INSERT INTO tecnicos (
    id_tecnico, cedula, correo_electronico, nombres, apellidos, telefono,
    especialidad_tecnica, anios_experiencia, empresa_origen,
    horas_semanales, salario_mensual, estado, fecha_registro, codigo_proyecto
) VALUES (
    'TEC001', '0987654321', 'tecnico.externo@empresa.com',
    'MARIA FERNANDA', 'GONZALEZ TORRES', '0988888888',
    'Desarrollo de Software', 5, 'TechCorp S.A.',
    40, 1200.00, 'ACTIVO', datetime('now'), 'PII-19-02'
);



-- ===========================
-- 5. CONSULTAS ÚTILES
-- ===========================

-- Ver todos los asistentes activos
-- SELECT * FROM asistentes WHERE estado = 'ACTIVO';

-- Ver todos los técnicos activos
-- SELECT * FROM tecnicos WHERE estado = 'ACTIVO';

-- Ver todo el personal de un proyecto
-- SELECT * FROM v_personal_activo_por_proyecto WHERE codigo_proyecto = 'PII-19-02';

-- Ver estadísticas de personal
-- SELECT * FROM v_estadisticas_personal;

-- Ver cuántos colaboradores hay por tipo
-- SELECT tipo_personal, COUNT(*) as total FROM v_personal_completo GROUP BY tipo_personal;

-- Ver personal ordenado por salario
-- SELECT nombres || ' ' || apellidos as nombre, tipo_personal, salario_mensual 
-- FROM v_personal_completo 
-- WHERE estado = 'ACTIVO' 
-- ORDER BY salario_mensual DESC;

-- Ver carga horaria total por proyecto
-- SELECT 
--     codigo_proyecto,
--     nombre_proyecto,
--     SUM(horas_semanales) as horas_totales,
--     COUNT(*) as total_personal
-- FROM v_personal_activo_por_proyecto
-- GROUP BY codigo_proyecto, nombre_proyecto
-- ORDER BY horas_totales DESC;

-- ===========================
-- 6. NOTAS IMPORTANTES
-- ===========================

-- DIFERENCIAS ENTRE TIPOS DE PERSONAL:

-- AYUDANTES (tabla: ayudantes)
--   - Estudiantes de la universidad
--   - Tienen: carrera, nivel, ira
--   - Correo institucional (@epn.edu.ec)
--   - Máximo 32 horas semanales
--   - Ya existente en el sistema

-- ASISTENTES (tabla: asistentes)
--   - Miembros de la universidad con formación avanzada
--   - Tienen: carrera, nivel, ira, titulo_academico, area_especializacion
--   - Correo institucional (@epn.edu.ec)
--   - Máximo 32 horas semanales
--   - NUEVO en esta migración

-- TÉCNICOS (tabla: tecnicos)
--   - Personal EXTERNO sin vinculación universitaria
--   - Tienen: especialidad_tecnica, anios_experiencia, empresa_origen
--   - NO tienen: carrera, nivel, ira (no son estudiantes)
--   - Correo puede ser cualquiera (no necesariamente @epn.edu.ec)
--   - Máximo 40 horas semanales
--   - NUEVO en esta migración

-- ===========================
-- FIN DE LA MIGRACIÓN
-- ===========================
