-- ===========================
-- SCHEMA: GESTIÓN DE AYUDANTES FIS-EPN (SQLite)
-- ===========================

-- ===========================
-- TABLA: MiembroFIS (Base para DIRECTOR, JEFE_DEPARTAMENTO, AYUDANTE)
-- ===========================
CREATE TABLE IF NOT EXISTS MiembroFIS (
    id_miembro INTEGER PRIMARY KEY AUTOINCREMENT,
    numero_unico TEXT NOT NULL UNIQUE,
    cedula TEXT NOT NULL UNIQUE,
    correo_institucional TEXT NOT NULL UNIQUE,
    password TEXT NOT NULL,
    nombres TEXT NOT NULL,
    apellidos TEXT NOT NULL,
    telefono TEXT,
    tipo_miembro TEXT NOT NULL CHECK(tipo_miembro IN ('DIRECTOR', 'JEFE_DEPARTAMENTO', 'AYUDANTE')),
    
    -- Campos específicos para DIRECTOR
    especialidad_director TEXT,
    
    -- Campos específicos para AYUDANTE
    carrera TEXT,
    nivel INTEGER CHECK(nivel >= 1 AND nivel <= 10),
    promedio REAL CHECK(promedio >= 0 AND promedio <= 10),
    
    -- Campos comunes
    estado TEXT NOT NULL DEFAULT 'ACTIVO' CHECK(estado IN ('ACTIVO', 'INACTIVO', 'BAJA')),
    fecha_registro DATETIME DEFAULT CURRENT_TIMESTAMP,
    fecha_inicio_gestion DATETIME,
    fecha_termino_gestion DATETIME
);

-- Índices para MiembroFIS
CREATE INDEX IF NOT EXISTS idx_miembro_tipo ON MiembroFIS(tipo_miembro);
CREATE INDEX IF NOT EXISTS idx_miembro_correo ON MiembroFIS(correo_institucional);
CREATE INDEX IF NOT EXISTS idx_miembro_estado ON MiembroFIS(estado);
CREATE INDEX IF NOT EXISTS idx_miembro_numero_unico ON MiembroFIS(numero_unico);

-- ===========================
-- TABLA: ProyectoInvestigacion
-- ===========================
CREATE TABLE IF NOT EXISTS ProyectoInvestigacion (
    id_proyecto INTEGER PRIMARY KEY AUTOINCREMENT,
    codigo_proyecto TEXT NOT NULL UNIQUE,
    nombre_proyecto TEXT NOT NULL,
    descripcion TEXT,
    fecha_inicio DATE NOT NULL,
    fecha_fin DATE NOT NULL,
    presupuesto REAL,
    categoria_proyecto TEXT NOT NULL DEFAULT 'INVESTIGACION' CHECK(categoria_proyecto IN ('INVESTIGACION', 'VINCULACION', 'TRANSFERENCIA_TECNOLOGICA')),
tipo_proyecto TEXT NOT NULL CHECK(tipo_proyecto IN ('INTERNO', 'SEMILLA', 'GRUPAL', 'MULTIDISCIPLINARIO', 'TRANSFERENCIA_TECNOLOGICA', 'TRANSFERENCIA', 'VINCULACION', 'VINCULACION_CON_FINANCIAMIENTO'))
    ayudantes_planificados INTEGER DEFAULT 0,
    director_numero_unico TEXT,
    estado TEXT NOT NULL DEFAULT 'ACTIVO' CHECK(estado IN ('ACTIVO', 'FINALIZADO', 'PAUSADO', 'CANCELADO')),
    fecha_creacion DATETIME DEFAULT CURRENT_TIMESTAMP,
    
    -- Clave foránea
    FOREIGN KEY (director_numero_unico) REFERENCES MiembroFIS(numero_unico)
);

-- Índices para ProyectoInvestigacion
CREATE INDEX IF NOT EXISTS idx_proyecto_codigo ON ProyectoInvestigacion(codigo_proyecto);
CREATE INDEX IF NOT EXISTS idx_proyecto_tipo ON ProyectoInvestigacion(tipo_proyecto);
CREATE INDEX IF NOT EXISTS idx_proyecto_estado ON ProyectoInvestigacion(estado);
CREATE INDEX IF NOT EXISTS idx_proyecto_director ON ProyectoInvestigacion(director_numero_unico);

-- ===========================
-- TABLA: AsignacionAyudanteProyecto
-- ===========================
CREATE TABLE IF NOT EXISTS AsignacionAyudanteProyecto (
    id_asignacion INTEGER PRIMARY KEY AUTOINCREMENT,
    ayudante_numero_unico TEXT NOT NULL,
    proyecto_codigo TEXT NOT NULL,
    fecha_asignacion DATE NOT NULL,
    estado TEXT NOT NULL DEFAULT 'ACTIVO' CHECK(estado IN ('ACTIVO', 'FINALIZADO', 'BAJA', 'SUSPENDIDO')),
    horas_semanales INTEGER DEFAULT 15,
    salario_por_hora REAL,
    fecha_final_asignacion DATE,
    motivo_baja TEXT,
    
    -- Claves foráneas
    FOREIGN KEY (ayudante_numero_unico) REFERENCES MiembroFIS(numero_unico) ON DELETE CASCADE,
    FOREIGN KEY (proyecto_codigo) REFERENCES ProyectoInvestigacion(codigo_proyecto) ON DELETE CASCADE,
    
    -- Restricción de unicidad
    UNIQUE(ayudante_numero_unico, proyecto_codigo, estado)
);

-- Índices para AsignacionAyudanteProyecto
CREATE INDEX IF NOT EXISTS idx_asignacion_ayudante ON AsignacionAyudanteProyecto(ayudante_numero_unico);
CREATE INDEX IF NOT EXISTS idx_asignacion_proyecto ON AsignacionAyudanteProyecto(proyecto_codigo);
CREATE INDEX IF NOT EXISTS idx_asignacion_estado ON AsignacionAyudanteProyecto(estado);
CREATE INDEX IF NOT EXISTS idx_asignacion_fecha ON AsignacionAyudanteProyecto(fecha_asignacion);

-- ===========================
-- TABLA: Mensaje
-- ===========================
CREATE TABLE IF NOT EXISTS Mensaje (
    id_mensaje TEXT PRIMARY KEY,
    fecha_envio DATETIME NOT NULL DEFAULT CURRENT_TIMESTAMP,
    asunto TEXT NOT NULL,
    contenido TEXT NOT NULL,
    remitente_numero_unico TEXT NOT NULL,
    destinatario_numero_unico TEXT NOT NULL,
    proyecto_codigo TEXT,
    leido BOOLEAN DEFAULT 0,
    fecha_lectura DATETIME,
    respuesta_a_mensaje_id TEXT,
    
    -- Claves foráneas
    FOREIGN KEY (remitente_numero_unico) REFERENCES MiembroFIS(numero_unico) ON DELETE CASCADE,
    FOREIGN KEY (destinatario_numero_unico) REFERENCES MiembroFIS(numero_unico) ON DELETE CASCADE,
    FOREIGN KEY (proyecto_codigo) REFERENCES ProyectoInvestigacion(codigo_proyecto) ON DELETE SET NULL,
    FOREIGN KEY (respuesta_a_mensaje_id) REFERENCES Mensaje(id_mensaje) ON DELETE SET NULL
);

-- Índices para Mensaje
CREATE INDEX IF NOT EXISTS idx_mensaje_remitente ON Mensaje(remitente_numero_unico);
CREATE INDEX IF NOT EXISTS idx_mensaje_destinatario ON Mensaje(destinatario_numero_unico);
CREATE INDEX IF NOT EXISTS idx_mensaje_proyecto ON Mensaje(proyecto_codigo);
CREATE INDEX IF NOT EXISTS idx_mensaje_leido ON Mensaje(leido);
CREATE INDEX IF NOT EXISTS idx_mensaje_fecha ON Mensaje(fecha_envio);

-- ===========================
-- TABLA: Notificacion
-- ===========================
CREATE TABLE IF NOT EXISTS Notificacion (
    id_notificacion TEXT PRIMARY KEY,
    fecha_creacion DATETIME NOT NULL DEFAULT CURRENT_TIMESTAMP,
    mensaje TEXT NOT NULL,
    tipo_notificacion TEXT NOT NULL DEFAULT 'OTRO' CHECK(tipo_notificacion IN 
        ('NUEVO_AYUDANTE', 'BAJA_AYUDANTE', 'PROGRESO_PROYECTO', 'RECORDATORIO', 'EVENTO', 'MENSAJE', 'OTRO')),
    leida BOOLEAN DEFAULT 0,
    fecha_lectura DATETIME,
    jefe_departamento_numero_unico TEXT NOT NULL,
    proyecto_codigo TEXT,
    ayudante_numero_unico TEXT,
    
    -- Claves foráneas
    FOREIGN KEY (jefe_departamento_numero_unico) REFERENCES MiembroFIS(numero_unico) ON DELETE CASCADE,
    FOREIGN KEY (proyecto_codigo) REFERENCES ProyectoInvestigacion(codigo_proyecto) ON DELETE SET NULL,
    FOREIGN KEY (ayudante_numero_unico) REFERENCES MiembroFIS(numero_unico) ON DELETE SET NULL
);

-- Índices para Notificacion
CREATE INDEX IF NOT EXISTS idx_notif_jefe ON Notificacion(jefe_departamento_numero_unico);
CREATE INDEX IF NOT EXISTS idx_notif_leida ON Notificacion(leida);
CREATE INDEX IF NOT EXISTS idx_notif_tipo ON Notificacion(tipo_notificacion);
CREATE INDEX IF NOT EXISTS idx_notif_fecha ON Notificacion(fecha_creacion);
CREATE INDEX IF NOT EXISTS idx_notif_proyecto ON Notificacion(proyecto_codigo);

-- ===========================
-- TABLA: Reporte
-- ===========================
CREATE TABLE IF NOT EXISTS Reporte (
    id_reporte TEXT PRIMARY KEY,
    fecha_generacion DATETIME NOT NULL DEFAULT CURRENT_TIMESTAMP,
    tipo_reporte TEXT NOT NULL DEFAULT 'GENERAL' CHECK(tipo_reporte IN 
        ('GENERAL', 'POR_PROYECTO', 'POR_CARRERA', 'POR_NIVEL', 'FINANCIERO')),
    titulo TEXT NOT NULL,
    contenido TEXT,
    ruta_archivo TEXT,
    jefe_generador_numero_unico TEXT NOT NULL,
    estado TEXT NOT NULL DEFAULT 'GENERADO' CHECK(estado IN ('GENERADO', 'EN_REVISION', 'APROBADO', 'ARCHIVADO')),
    
    -- Clave foránea
    FOREIGN KEY (jefe_generador_numero_unico) REFERENCES MiembroFIS(numero_unico) ON DELETE CASCADE
);

-- Índices para Reporte
CREATE INDEX IF NOT EXISTS idx_reporte_jefe ON Reporte(jefe_generador_numero_unico);
CREATE INDEX IF NOT EXISTS idx_reporte_tipo ON Reporte(tipo_reporte);
CREATE INDEX IF NOT EXISTS idx_reporte_fecha ON Reporte(fecha_generacion);

-- ===========================
-- TABLA: ReporteAyudante (Many-to-Many: Reporte - Ayudante)
-- ===========================
CREATE TABLE IF NOT EXISTS ReporteAyudante (
    id INTEGER PRIMARY KEY AUTOINCREMENT,
    reporte_id TEXT NOT NULL,
    ayudante_numero_unico TEXT NOT NULL,
    
    -- Claves foráneas
    FOREIGN KEY (reporte_id) REFERENCES Reporte(id_reporte) ON DELETE CASCADE,
    FOREIGN KEY (ayudante_numero_unico) REFERENCES MiembroFIS(numero_unico) ON DELETE CASCADE,
    
    -- Restricción de unicidad
    UNIQUE(reporte_id, ayudante_numero_unico)
);

-- Índices para ReporteAyudante
CREATE INDEX IF NOT EXISTS idx_ra_reporte ON ReporteAyudante(reporte_id);
CREATE INDEX IF NOT EXISTS idx_ra_ayudante ON ReporteAyudante(ayudante_numero_unico);

-- ===========================
-- VISTAS AUXILIARES
-- ===========================

-- Vista: Resumen de Directores
CREATE VIEW IF NOT EXISTS v_directores AS
SELECT 
    numero_unico,
    (nombres || ' ' || apellidos) AS nombre_completo,
    correo_institucional,
    especialidad_director,
    estado
FROM MiembroFIS
WHERE tipo_miembro = 'DIRECTOR' AND estado = 'ACTIVO';

-- Vista: Resumen de Ayudantes Activos
CREATE VIEW IF NOT EXISTS v_ayudantes_activos AS
SELECT 
    m.numero_unico,
    (m.nombres || ' ' || m.apellidos) AS nombre_completo,
    m.carrera,
    m.nivel,
    m.promedio,
    m.correo_institucional,
    m.estado,
    m.fecha_registro
FROM MiembroFIS m
WHERE m.tipo_miembro = 'AYUDANTE' AND m.estado = 'ACTIVO';

-- Vista: Proyectos con Director Asignado
CREATE VIEW IF NOT EXISTS v_proyectos_directores AS
SELECT 
    p.codigo_proyecto,
    p.nombre_proyecto,
    p.tipo_proyecto,
    p.estado,
    (m.nombres || ' ' || m.apellidos) AS director,
    p.presupuesto,
    p.ayudantes_planificados
FROM ProyectoInvestigacion p
LEFT JOIN MiembroFIS m ON p.director_numero_unico = m.numero_unico
ORDER BY p.fecha_inicio DESC;

-- Vista: Asignaciones Activas
CREATE VIEW IF NOT EXISTS v_asignaciones_activas AS
SELECT 
    a.id_asignacion,
    (m.nombres || ' ' || m.apellidos) AS ayudante,
    m.carrera,
    m.nivel,
    p.codigo_proyecto,
    p.nombre_proyecto,
    a.horas_semanales,
    a.salario_por_hora,
    CAST((a.horas_semanales * a.salario_por_hora * 4.33) AS REAL) AS salario_mensual_estimado,
    a.fecha_asignacion
FROM AsignacionAyudanteProyecto a
JOIN MiembroFIS m ON a.ayudante_numero_unico = m.numero_unico
JOIN ProyectoInvestigacion p ON a.proyecto_codigo = p.codigo_proyecto
WHERE a.estado = 'ACTIVO'
ORDER BY a.fecha_asignacion DESC;

-- ===========================
-- FIN DEL SCRIPT DE SCHEMA
-- ===========================
