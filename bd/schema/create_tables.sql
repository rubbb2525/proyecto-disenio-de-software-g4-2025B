-- ===========================
-- BASE DE DATOS: GESTIÓN DE AYUDANTES FIS-EPN
-- ===========================
-- Crear base de datos
CREATE DATABASE IF NOT EXISTS gestion_ayudantes;
USE gestion_ayudantes;

-- ===========================
-- TABLA: MIEMBRO_FIS
-- ===========================
CREATE TABLE IF NOT EXISTS MiembroFIS (
    numero_unico VARCHAR(10) PRIMARY KEY,
    cedula VARCHAR(13) UNIQUE NOT NULL,
    correo_institucional VARCHAR(100) UNIQUE NOT NULL,
    password VARCHAR(255) NOT NULL,
    nombres VARCHAR(100) NOT NULL,
    apellidos VARCHAR(100) NOT NULL,
    telefono VARCHAR(20),
    tipo_miembro ENUM('DIRECTOR', 'AYUDANTE', 'JEFE_DEPARTAMENTO') NOT NULL,
    
    -- Campos específicos para DIRECTOR
    especialidad_director VARCHAR(100),
    
    -- Campos específicos para AYUDANTE
    carrera VARCHAR(100),
    nivel INT CHECK (nivel >= 1 AND nivel <= 10),
    promedio DECIMAL(3,1) CHECK (promedio >= 0 AND promedio <= 10),
    
    -- Campos comunes
    estado ENUM('ACTIVO', 'INACTIVO', 'BAJA') DEFAULT 'ACTIVO',
    fecha_registro DATETIME DEFAULT CURRENT_TIMESTAMP,
    fecha_finalizacion DATETIME,
    motivo_salida VARCHAR(255),
    
    -- Campos específicos para JEFE
    fecha_inicio_gestion DATE,
    
    INDEX idx_correo (correo_institucional),
    INDEX idx_tipo (tipo_miembro),
    INDEX idx_estado (estado)
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4 COLLATE=utf8mb4_unicode_ci;

-- ===========================
-- TABLA: PROYECTO_INVESTIGACION
-- ===========================
CREATE TABLE IF NOT EXISTS ProyectoInvestigacion (
    codigo_proyecto VARCHAR(50) PRIMARY KEY,
    nombre_proyecto VARCHAR(255) NOT NULL,
    descripcion TEXT,
    fecha_inicio DATE NOT NULL,
    fecha_fin DATE,
    presupuesto DECIMAL(15,2),
    tipo_proyecto ENUM('INTERNO', 'SEMILLA', 'GRUPALES', 'VINCULACION_CON_FINANCIAMIENTO', 'TRANSFERENCIA_TECNOLOGICA') NOT NULL,
    ayudantes_planificados INT DEFAULT 0,
    director_numero_unico VARCHAR(10) NOT NULL,
    estado ENUM('ACTIVO', 'FINALIZADO', 'CANCELADO') DEFAULT 'ACTIVO',
    fecha_creacion DATETIME DEFAULT CURRENT_TIMESTAMP,
    
    FOREIGN KEY (director_numero_unico) REFERENCES MiembroFIS(numero_unico),
    INDEX idx_director (director_numero_unico),
    INDEX idx_tipo (tipo_proyecto),
    INDEX idx_estado (estado)
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4 COLLATE=utf8mb4_unicode_ci;

-- ===========================
-- TABLA: ASIGNACION_AYUDANTE_PROYECTO
-- ===========================
CREATE TABLE IF NOT EXISTS AsignacionAyudanteProyecto (
    id INT AUTO_INCREMENT PRIMARY KEY,
    ayudante_numero_unico VARCHAR(10) NOT NULL,
    proyecto_codigo VARCHAR(50) NOT NULL,
    fecha_asignacion DATE NOT NULL,
    fecha_finalizacion DATE,
    estado ENUM('ACTIVO', 'BAJA', 'FINALIZADO') DEFAULT 'ACTIVO',
    horas_semanales INT DEFAULT 15,
    salario_por_hora DECIMAL(8,2) DEFAULT 4.50,
    fecha_creacion DATETIME DEFAULT CURRENT_TIMESTAMP,
    
    FOREIGN KEY (ayudante_numero_unico) REFERENCES MiembroFIS(numero_unico),
    FOREIGN KEY (proyecto_codigo) REFERENCES ProyectoInvestigacion(codigo_proyecto),
    UNIQUE KEY unique_asignacion (ayudante_numero_unico, proyecto_codigo),
    INDEX idx_ayudante (ayudante_numero_unico),
    INDEX idx_proyecto (proyecto_codigo),
    INDEX idx_estado (estado)
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4 COLLATE=utf8mb4_unicode_ci;

-- ===========================
-- TABLA: MENSAJE
-- ===========================
CREATE TABLE IF NOT EXISTS Mensaje (
    id_mensaje VARCHAR(50) PRIMARY KEY,
    fecha_envio DATETIME NOT NULL,
    asunto VARCHAR(255),
    contenido TEXT NOT NULL,
    remitente_numero_unico VARCHAR(10) NOT NULL,
    destinatario_numero_unico VARCHAR(10) NOT NULL,
    proyecto_codigo VARCHAR(50),
    leido BOOLEAN DEFAULT FALSE,
    respuesta TEXT,
    fecha_respuesta DATETIME,
    fecha_creacion DATETIME DEFAULT CURRENT_TIMESTAMP,
    
    FOREIGN KEY (remitente_numero_unico) REFERENCES MiembroFIS(numero_unico),
    FOREIGN KEY (destinatario_numero_unico) REFERENCES MiembroFIS(numero_unico),
    FOREIGN KEY (proyecto_codigo) REFERENCES ProyectoInvestigacion(codigo_proyecto),
    INDEX idx_remitente (remitente_numero_unico),
    INDEX idx_destinatario (destinatario_numero_unico),
    INDEX idx_leido (leido),
    INDEX idx_fecha (fecha_envio)
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4 COLLATE=utf8mb4_unicode_ci;

-- ===========================
-- TABLA: NOTIFICACION
-- ===========================
CREATE TABLE IF NOT EXISTS Notificacion (
    id_notificacion VARCHAR(50) PRIMARY KEY,
    fecha_creacion DATETIME NOT NULL DEFAULT CURRENT_TIMESTAMP,
    mensaje TEXT NOT NULL,
    tipo_notificacion ENUM('NUEVO_AYUDANTE', 'BAJA_AYUDANTE', 'PROGRESO_PROYECTO', 'RECORDATORIO', 'CAMBIO_ESTADO', 'INFORME') DEFAULT 'RECORDATORIO',
    leida BOOLEAN DEFAULT FALSE,
    jefe_departamento_numero_unico VARCHAR(10) NOT NULL,
    proyecto_codigo VARCHAR(50),
    ayudante_numero_unico VARCHAR(10),
    
    FOREIGN KEY (jefe_departamento_numero_unico) REFERENCES MiembroFIS(numero_unico),
    FOREIGN KEY (proyecto_codigo) REFERENCES ProyectoInvestigacion(codigo_proyecto),
    FOREIGN KEY (ayudante_numero_unico) REFERENCES MiembroFIS(numero_unico),
    INDEX idx_jefe (jefe_departamento_numero_unico),
    INDEX idx_leida (leida),
    INDEX idx_fecha (fecha_creacion)
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4 COLLATE=utf8mb4_unicode_ci;

-- ===========================
-- TABLA: REPORTE
-- ===========================
CREATE TABLE IF NOT EXISTS Reporte (
    id_reporte VARCHAR(50) PRIMARY KEY,
    fecha_generacion DATETIME NOT NULL DEFAULT CURRENT_TIMESTAMP,
    tipo_reporte ENUM('GENERAL', 'POR_PROYECTO', 'POR_CARRERA', 'POR_NIVEL', 'PRESUPUESTARIO') DEFAULT 'GENERAL',
    titulo VARCHAR(255) NOT NULL,
    contenido LONGTEXT,
    ruta_archivo VARCHAR(255),
    jefe_generador_numero_unico VARCHAR(10) NOT NULL,
    
    FOREIGN KEY (jefe_generador_numero_unico) REFERENCES MiembroFIS(numero_unico),
    INDEX idx_tipo (tipo_reporte),
    INDEX idx_fecha (fecha_generacion)
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4 COLLATE=utf8mb4_unicode_ci;

-- ===========================
-- TABLA: REPORTE_AYUDANTE (Relación muchos a muchos)
-- ===========================
CREATE TABLE IF NOT EXISTS ReporteAyudante (
    reporte_id VARCHAR(50) NOT NULL,
    ayudante_numero_unico VARCHAR(10) NOT NULL,
    
    PRIMARY KEY (reporte_id, ayudante_numero_unico),
    FOREIGN KEY (reporte_id) REFERENCES Reporte(id_reporte),
    FOREIGN KEY (ayudante_numero_unico) REFERENCES MiembroFIS(numero_unico)
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4 COLLATE=utf8mb4_unicode_ci;

-- ===========================
-- ÍNDICES ADICIONALES PARA OPTIMIZACIÓN
-- ===========================
CREATE INDEX idx_mf_tipo_estado ON MiembroFIS(tipo_miembro, estado);
CREATE INDEX idx_pi_director_tipo ON ProyectoInvestigacion(director_numero_unico, tipo_proyecto);
CREATE INDEX idx_aap_proyecto_estado ON AsignacionAyudanteProyecto(proyecto_codigo, estado);
CREATE INDEX idx_mensaje_fechas ON Mensaje(fecha_envio, remitente_numero_unico, destinatario_numero_unico);

-- ===========================
-- FIN DEL ESQUEMA
-- ===========================
