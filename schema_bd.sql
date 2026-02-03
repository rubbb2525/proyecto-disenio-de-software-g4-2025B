-- Script SQL para SQLite3 - Sistema Gestión Ayudantes FIS-EPN
-- Este archivo crea la estructura de la BD SQLite

-- Habilitar claves foráneas
PRAGMA foreign_keys = ON;

-- Tabla de miembros EPN (base)
CREATE TABLE IF NOT EXISTS miembros_epn (
    codigo_unico TEXT PRIMARY KEY,
    cedula TEXT NOT NULL UNIQUE,
    correo_institucional TEXT NOT NULL UNIQUE,
    password TEXT NOT NULL,
    nombres TEXT NOT NULL,
    apellidos TEXT NOT NULL,
    telefono TEXT,
    rol TEXT NOT NULL,
    estado TEXT NOT NULL DEFAULT 'ACTIVO'
);

-- Tabla de estudiantes
CREATE TABLE IF NOT EXISTS estudiantes (
    codigo_unico TEXT PRIMARY KEY,
    cedula TEXT NOT NULL UNIQUE,
    correo_institucional TEXT NOT NULL UNIQUE,
    nombres TEXT NOT NULL,
    apellidos TEXT NOT NULL,
    telefono TEXT,
    carrera TEXT NOT NULL,
    ira REAL NOT NULL,
    nivel INTEGER NOT NULL,
    FOREIGN KEY (codigo_unico) REFERENCES miembros_epn(codigo_unico)
);

-- Tabla de proyectos
CREATE TABLE IF NOT EXISTS proyectos (
    codigo_proyecto TEXT PRIMARY KEY,
    nombre_proyecto TEXT NOT NULL,
    descripcion TEXT,
    fecha_inicio DATETIME NOT NULL,
    fecha_fin DATETIME NOT NULL,
    estado TEXT NOT NULL DEFAULT 'ACTIVO',
    categoria_proyecto TEXT NOT NULL DEFAULT 'INVESTIGACION' CHECK(categoria_proyecto IN ('INVESTIGACION','VINCULACION','TRANSFERENCIA_TECNOLOGICA')),
    tipo_proyecto TEXT NOT NULL CHECK(tipo_proyecto IN ('INTERNO', 'SEMILLA', 'GRUPAL', 'MULTIDISCIPLINARIO', 'TRANSFERENCIA_TECNOLOGICA', 'TRANSFERENCIA', 'VINCULACION', 'VINCULACION_CON_FINANCIAMIENTO')),
    ayudantes_planificados INTEGER NOT NULL,
    codigo_director TEXT,
    FOREIGN KEY (codigo_director) REFERENCES miembros_epn(codigo_unico)
);

-- Tabla de ayudantes
CREATE TABLE IF NOT EXISTS ayudantes (
    codigo_unico TEXT PRIMARY KEY,
    cedula TEXT NOT NULL UNIQUE,
    correo_institucional TEXT NOT NULL UNIQUE,
    nombres TEXT NOT NULL,
    apellidos TEXT NOT NULL,
    telefono TEXT,
    carrera TEXT NOT NULL,
    nivel INTEGER NOT NULL,
    ira REAL NOT NULL,
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

-- Tabla de notificaciones
CREATE TABLE IF NOT EXISTS notificaciones (
    id_notificacion TEXT PRIMARY KEY,
    fecha DATETIME NOT NULL DEFAULT CURRENT_TIMESTAMP,
    mensaje TEXT NOT NULL,
    tipo TEXT NOT NULL,
    leida BOOLEAN NOT NULL DEFAULT 0,
    codigo_proyecto TEXT,
    codigo_ayudante TEXT,
    FOREIGN KEY (codigo_proyecto) REFERENCES proyectos(codigo_proyecto),
    FOREIGN KEY (codigo_ayudante) REFERENCES ayudantes(codigo_unico)
);

-- Tabla de reportes
CREATE TABLE IF NOT EXISTS reportes (
    id_reporte TEXT PRIMARY KEY,
    fecha_generacion DATETIME NOT NULL DEFAULT CURRENT_TIMESTAMP,
    tipo TEXT NOT NULL,
    titulo TEXT NOT NULL,
    contenido TEXT,
    estadisticas TEXT
);

-- Crear índices para optimización
CREATE INDEX IF NOT EXISTS idx_rol ON miembros_epn(rol);
CREATE INDEX IF NOT EXISTS idx_correo ON miembros_epn(correo_institucional);
CREATE INDEX IF NOT EXISTS idx_proyecto_ayudantes ON ayudantes(codigo_proyecto);
CREATE INDEX IF NOT EXISTS idx_proyecto_director ON proyectos(codigo_director);
CREATE INDEX IF NOT EXISTS idx_notificacion_tipo ON notificaciones(tipo);
CREATE INDEX IF NOT EXISTS idx_estudiante_ira ON estudiantes(ira);

-- ====================================================================
-- DATOS DE PRUEBA
-- ====================================================================
-- Jefa de Departamento (única usuaria administrativa además de Directores)
INSERT OR IGNORE INTO miembros_epn VALUES 
('JEFA001', '1010101010', 'jefa@fis.epn.edu.ec', 'admin', 
 'JEFA', 'DEPARTAMENTO', '0996000000', 'JEFA_DEPARTAMENTO', 'ACTIVO');

-- Directores únicos identificados en los proyectos
INSERT OR IGNORE INTO miembros_epn (codigo_unico, cedula, correo_institucional, password, nombres, apellidos, telefono, rol, estado)
VALUES 
('DIR001', '1700000001', 'sandra.sanchez@epn.edu.ec', 'S@ndr4_2024!Epn', 'SANDRA PATRICIA', 'SANCHEZ GORDON', NULL, 'DIRECTOR', 'ACTIVO'),
('DIR002', '1700000002', 'rosa.navarrete@epn.edu.ec', 'R0sa_Nav@2024!', 'ROSA DEL CARMEN', 'NAVARRETE RUEDA', NULL, 'DIRECTOR', 'ACTIVO'),
('DIR003', '1700000003', 'jenny.torres@epn.edu.ec', 'J3nny_T0rr3s!24', 'JENNY GABRIELA', 'TORRES OLMEDO', NULL, 'DIRECTOR', 'ACTIVO'),
('DIR004', '1700000004', 'maria.hallo@epn.edu.ec', 'M@ria_Hall0#2024', 'MARIA ASUNCION', 'HALLO CARRASCO', NULL, 'DIRECTOR', 'ACTIVO'),
('DIR005', '1700000005', 'carlos.montenegro@epn.edu.ec', 'C@rlos_Mont3!24', 'CARLOS ESTALESMIT WILLAM', 'MONTENEGRO ARMAS', NULL, 'DIRECTOR', 'ACTIVO'),
('DIR006', '1700000006', 'pamela.flores@epn.edu.ec', 'P@m3la_Flor3s!', 'PAMELA CATHERINE', 'FLORES NARANJO', NULL, 'DIRECTOR', 'ACTIVO'),
('DIR007', '1700000007', 'enrique.larco@epn.edu.ec', 'Enr1qu3_L@rc0!24', 'ENRIQUE ANDRES', 'LARCO AMPUDIA', NULL, 'DIRECTOR', 'ACTIVO'),
('DIR008', '1700000008', 'myriam.hernandez@epn.edu.ec', 'Myri@m_H3rn@ndez', 'MYRIAM BEATRIZ', 'HERNANDEZ ALVAREZ', NULL, 'DIRECTOR', 'ACTIVO'),
('DIR009', '1700000009', 'luis.mafla@epn.edu.ec', 'Lu1s_M@fl@_2024!', 'LUIS ENRIQUE', 'MAFLA GALLEGOS', NULL, 'DIRECTOR', 'ACTIVO'),
('DIR010', '1700000010', 'edison.loza@epn.edu.ec', 'Ed1s0n_Loz@!2024', 'EDISON FERNANDO', 'LOZA AGUIRRE', NULL, 'DIRECTOR', 'ACTIVO'),
('DIR011', '1700000011', 'tania.calle@epn.edu.ec', 'T@nia_C@ll3!24', 'TANIA ELIZABETH', 'CALLE JIMENEZ', NULL, 'DIRECTOR', 'ACTIVO'),
('DIR012', '1700000012', 'myriam.penafiel@epn.edu.ec', 'Myri@m_P3ñ@!24', 'MYRIAM GUADALUPE', 'PEÑAFIEL AGUILAR', NULL, 'DIRECTOR', 'ACTIVO'),
('DIR013', '1700000013', 'regina.tenemaza@epn.edu.ec', 'R3gina_T3n3m@z@', 'REGINA MARITZOL', 'TENEMAZA VERA', NULL, 'DIRECTOR', 'ACTIVO'),
('DIR014', '1700000014', 'diana.yacchirema@epn.edu.ec', 'Di@na_Y@cchir3m@', 'DIANA CECILIA', 'YACCHIREMA VARGAS', NULL, 'DIRECTOR', 'ACTIVO'),
('DIR015', '1700000015', 'sang.yoo@epn.edu.ec', 'S@ng_Y00_2024!Epn', 'SANG GUUN', 'YOO', NULL, 'DIRECTOR', 'ACTIVO'),
('DIR016', '1700000016', 'marco.benalcazar@epn.edu.ec', 'M@rco_B3n@lc@z@r', 'MARCO ENRIQUE', 'BENALCAZAR PALACIOS', NULL, 'DIRECTOR', 'ACTIVO'),
('DIR017', '1700000017', 'marco.santorum@epn.edu.ec', 'M@rco_S@nt0rum!', 'MARCO OSWALDO', 'SANTORUM GAIBOR', NULL, 'DIRECTOR', 'ACTIVO');

-- Estudiantes (sin acceso al sistema, password fijo 'N/A')
INSERT OR IGNORE INTO miembros_epn (codigo_unico, cedula, correo_institucional, password, nombres, apellidos, telefono, rol, estado)
VALUES 
('202111079', '1760107340', 'abdelfatah.abdelrahman@epn.edu.ec', 'N/A', 'ABDELFATAH', 'ABDELRAHMAN TAREK SELIM', NULL, 'ESTUDIANTE', 'ACTIVO'),
('202310774', '1727416776', 'sergio.aconda@epn.edu.ec', 'N/A', 'SERGIO DARIO', 'ACONDA GUERRA', NULL, 'ESTUDIANTE', 'ACTIVO'),
('202010702', '1725724270', 'christian.agila@epn.edu.ec', 'N/A', 'CHRISTIAN MANUEL', 'AGILA VIVANCO', NULL, 'ESTUDIANTE', 'ACTIVO'),
('202120499', '1726383514', 'mario.aisalla@epn.edu.ec', 'N/A', 'MARIO SEBASTIAN', 'AISALLA POZO', NULL, 'ESTUDIANTE', 'ACTIVO'),
('202210035', '0202678892', 'melany.alarcon@epn.edu.ec', 'N/A', 'MELANY MADELINE', 'ALARCON BALAREZO', NULL, 'ESTUDIANTE', 'ACTIVO'),
('201921355', '2200129381', 'frankz.alarcon@epn.edu.ec', 'N/A', 'FRANKZ LENIN', 'ALARCON CANDO', NULL, 'ESTUDIANTE', 'ACTIVO'),
('202120934', '1753739364', 'dorian.alban@epn.edu.ec', 'N/A', 'DORIAN JOEL', 'ALBAN LUCAS', NULL, 'ESTUDIANTE', 'ACTIVO'),
('202010905', '1750963124', 'giulian.albuja@epn.edu.ec', 'N/A', 'GIULIAN DAVID', 'ALBUJA SALAZAR', NULL, 'ESTUDIANTE', 'ACTIVO'),
('202110549', '1726623000', 'fernando.aldaz@epn.edu.ec', 'N/A', 'FERNANDO JOSUE', 'ALDAZ LASCANO', NULL, 'ESTUDIANTE', 'ACTIVO'),
('202120757', '1751424324', 'carlos.aleman@epn.edu.ec', 'N/A', 'CARLOS ALEJANDRO', 'ALEMAN OSORIO', NULL, 'ESTUDIANTE', 'ACTIVO');

-- Inserción en tabla estudiantes
INSERT OR IGNORE INTO estudiantes (codigo_unico, cedula, correo_institucional, nombres, apellidos, telefono, carrera, ira, nivel)
VALUES 
('202111079', '1760107340', 'abdelfatah.abdelrahman@epn.edu.ec', 'ABDELFATAH', 'ABDELRAHMAN TAREK SELIM', NULL, 'SOFTWARE', 7.5, 6),
('202310774', '1727416776', 'sergio.aconda@epn.edu.ec', 'SERGIO DARIO', 'ACONDA GUERRA', NULL, 'SOFTWARE', 8.2, 2),
('202010702', '1725724270', 'christian.agila@epn.edu.ec', 'CHRISTIAN MANUEL', 'AGILA VIVANCO', NULL, 'SOFTWARE', 7.8, 8),
('202120499', '1726383514', 'mario.aisalla@epn.edu.ec', 'MARIO SEBASTIAN', 'AISALLA POZO', NULL, 'SOFTWARE', 8.5, 5),
('202210035', '0202678892', 'melany.alarcon@epn.edu.ec', 'MELANY MADELINE', 'ALARCON BALAREZO', NULL, 'SOFTWARE', 8.0, 4),
('201921355', '2200129381', 'frankz.alarcon@epn.edu.ec', 'FRANKZ LENIN', 'ALARCON CANDO', NULL, 'SOFTWARE', 7.3, 9),
('202120934', '1753739364', 'dorian.alban@epn.edu.ec', 'DORIAN JOEL', 'ALBAN LUCAS', NULL, 'SOFTWARE', 8.1, 5),
('202010905', '1750963124', 'giulian.albuja@epn.edu.ec', 'GIULIAN DAVID', 'ALBUJA SALAZAR', NULL, 'SOFTWARE', 7.9, 8),
('202110549', '1726623000', 'fernando.aldaz@epn.edu.ec', 'FERNANDO JOSUE', 'ALDAZ LASCANO', NULL, 'SOFTWARE', 8.3, 6),
('202120757', '1751424324', 'carlos.aleman@epn.edu.ec', 'CARLOS ALEJANDRO', 'ALEMAN OSORIO', NULL, 'SOFTWARE', 7.6, 5);

-- Proyectos
INSERT OR IGNORE INTO proyectos (codigo_proyecto, nombre_proyecto, descripcion, fecha_inicio, fecha_fin, estado, tipo_proyecto, ayudantes_planificados, codigo_director)
VALUES 
('PII-19-02', 'Modelo y Prototipo para Creación de Perfiles de Estudiantes con Discapacidades en Ambientes e-Learning', 'Proyecto enfocado en la creación de perfiles accesibles para estudiantes con discapacidades en entornos e-learning.', '2020-01-01', '2021-12-31', 'Cerrado', 'INTERNO', 2, 'DIR001'),
('PII-18-01', 'Semántica embebida con JSON-LD para generar información enriquecida en resultados de búsqueda de Recursos Educativos Abiertos', 'Implementación de JSON-LD para mejorar la búsqueda de recursos educativos abiertos mediante semántica enriquecida.', '2019-01-01', '2020-12-31', 'Cerrado', 'INTERNO', 2, 'DIR002'),
('PII-18-02', 'Aplicación de la teoría de juego para fortalecer las habilidades de gestión en cyberseguridad', 'Uso de teoría de juegos para desarrollar habilidades de gestión en seguridad informática.', '2019-01-01', '2020-12-31', 'Cerrado', 'INTERNO', 2, 'DIR003'),
('PII-18-04', 'Marco de trabajo para la predicción de contribuyentes deudores con alto riesgo de no pago de impuestos usando técnicas de minería de datos', 'Desarrollo de framework para predecir contribuyentes con riesgo de no pago mediante data mining.', '2019-01-01', '2020-12-31', 'Cerrado', 'INTERNO', 2, 'DIR004'),
('PII-17-02', 'Minería de texto para construir la filogenia de los insectos vectores de la enfermedad de chagas', 'Aplicación de text mining para análisis filogenético de vectores de Chagas.', '2018-01-01', '2019-12-31', 'Cerrado', 'INTERNO', 2, 'DIR004'),
('PII-17-05', 'Utilización de técnicas de análisis de textos para evaluar iniciativas sociales y ecológicas de sostenibilidad en los últimos 15 años de investigación y práctica en logística y transporte', 'Análisis textual de iniciativas de sostenibilidad en logística y transporte.', '2018-01-01', '2019-12-31', 'Cerrado', 'INTERNO', 2, 'DIR005'),
('PII-17-11', 'Caracterización de ejercicios de diseño de software para potenciar la habilidad de abstraer', 'Estudio sobre ejercicios de diseño de software y su impacto en habilidades de abstracción.', '2018-01-01', '2019-12-31', 'Cerrado', 'INTERNO', 2, 'DIR006'),
('PII-17-12', 'Mejora de las competencias para la vida en función de la calidad de las aplicaciones educativas web y móviles', 'Evaluación de aplicaciones educativas y su impacto en competencias para la vida.', '2018-01-01', '2019-12-31', 'Proceso de cierre', 'INTERNO', 2, 'DIR007'),
('PII-17-14', 'Detección de ramsonware a gran escala por medio de seguridad cognitiva', 'Sistema de detección de ransomware utilizando técnicas de seguridad cognitiva.', '2018-01-01', '2019-12-31', 'Cerrado', 'INTERNO', 2, 'DIR008'),
('PII-17-15', 'Diseño de un sistema seguro de gestión de identidades para el Registro Civil del Ecuador', 'Desarrollo de sistema seguro de gestión de identidades para entidades gubernamentales.', '2018-01-01', '2019-12-31', 'Proceso de cierre', 'INTERNO', 2, 'DIR009'),
('PII-17-18', 'Enriqueciendo Información de vigilancia estratégica mediante el uso de redes sociales empresariales y aplicaciones móviles', 'Integración de redes sociales y apps móviles para vigilancia estratégica empresarial.', '2018-01-01', '2019-12-31', 'Proceso de cierre', 'INTERNO', 2, 'DIR010'),
('PII-16-01', 'Uso de microdatos para mejorar los resultados de búsqueda de recursos educativos abiertos accesibles', 'Implementación de microdatos para optimizar búsqueda de recursos educativos accesibles.', '2017-01-01', '2018-12-31', 'Cerrado', 'INTERNO', 2, 'DIR002'),
('PII-16-02', 'Diseño de una Arquitectura de un servidor de mapas geográficos accesibles', 'Arquitectura de servidor de mapas con enfoque en accesibilidad.', '2017-01-01', '2018-12-31', 'Cerrado', 'INTERNO', 2, 'DIR011'),
('PII-16-03', 'Uso de métodos de minería de Datos Educativos para e-learning', 'Aplicación de educational data mining en plataformas e-learning.', '2017-01-01', '2018-12-31', 'Cerrado', 'INTERNO', 2, 'DIR012'),
('PII-16-04', 'Aceptabilidad de la A2R', 'Estudio de aceptabilidad de tecnologías de asistencia y rehabilitación.', '2017-01-01', '2018-12-31', 'Cerrado', 'INTERNO', 2, 'DIR013'),
('PII-16-06', 'Sistema de recomendación para revistas científicas abiertas utilizando tecnologías de Linked data', 'Sistema de recomendación basado en linked data para revistas open access.', '2017-01-01', '2018-12-31', 'Cerrado', 'INTERNO', 2, 'DIR004'),
('PII-16-07', 'Identificación de factores que influyen en la productividad del desarrollo de software', 'Análisis de factores de productividad en desarrollo de software.', '2017-01-01', '2018-12-31', 'Cerrado', 'INTERNO', 2, 'DIR006'),
('PIS-20-02', 'Agentes de adquisición, procesamiento y respuesta de un sistema emergente para gestión de accidentabilidad en vehículos basado en técnicas de inteligencia artificial', 'Sistema multi-agente para gestión de accidentabilidad vehicular usando IA.', '2021-01-01', '2023-12-31', 'En ejecución', 'SEMILLA', 3, 'DIR008'),
('PIS-20-07', 'Un enfoque interoperable basado en IoT para apoyar el envejecimiento activo y saludable de los adultos mayores', 'Plataforma IoT para monitoreo y apoyo de adultos mayores.', '2021-01-01', '2023-12-31', 'En ejecución', 'SEMILLA', 3, 'DIR014'),
('PIS-19-11', 'Estudio longitudinal cualitativo sobre la abstracción y el diseño de software a través de ejercicios de diseño', 'Investigación longitudinal sobre enseñanza de abstracción en diseño de software.', '2020-01-01', '2022-12-31', 'Proceso de cierre', 'SEMILLA', 2, 'DIR006'),
('PIS-17-10', 'Minería de datos, extracción de vector de características y obtención de modelos con reconocimiento de patrones y aprendizaje automático para detección de escenarios relacionados con el delito de tráfico de personas', 'Sistema de ML para detección de patrones en tráfico de personas.', '2018-01-01', '2020-12-31', 'Cerrado', 'SEMILLA', 3, 'DIR008'),
('PIS-17-14', 'Implementación de un entorno virtual de aprendizaje tutorado en 3D para el entrenamiento de tareas estructuradas', 'Entorno virtual 3D para entrenamiento y aprendizaje tutorado.', '2018-01-01', '2020-12-31', 'Proceso de cierre', 'SEMILLA', 2, 'DIR010'),
('PIS-17-15', 'Control de dispositivos a través del pensamiento (ondas cerebrales)', 'Interfaz cerebro-computadora para control de dispositivos mediante BCI.', '2018-01-01', '2020-12-31', 'Cerrado', 'SEMILLA', 2, 'DIR015'),
('PIGR-19-07', 'Reconocimiento de gestos de la mano usando señales electromiográficas e inteligencia artificial y su aplicación para la implementación de interfaces humano - máquina y humano - humano', 'Sistema de reconocimiento de gestos mediante EMG e IA para interfaces HMI.', '2020-01-01', '2023-12-31', 'Prórroga Técnica', 'GRUPAL', 4, 'DIR016'),
('PIE-CEPRA-XIII-2019-13', 'Reconocimiento de gestos de la mano usando señales electromiográficas (EMG) e inteligencia artificial', 'Proyecto de transferencia para reconocimiento de gestos con EMG e IA.', '2019-01-01', '2021-12-31', 'Proceso de cierre', 'TRANSFERENCIA', 3, 'DIR016'),
('PIE-INEDITA-01-2018', 'Un framework como herramienta de apoyo para mejorar las habilidades socio-cognitivas en el marco de una inclusión plena para personas con discapacidad intelectual, independientemente del lugar de residencia', 'Framework para desarrollo de habilidades socio-cognitivas en personas con discapacidad intelectual.', '2019-01-01', '2021-12-31', 'Proceso de cierre', 'TRANSFERENCIA', 3, 'DIR017'),
('PIE-CEPRA-XI-2017-15', 'Sistema de tele-rehabilitación para la auto-reeducación de los pacientes después de una cirugía de sustitución de cadera', 'Plataforma de tele-rehabilitación post-operatoria para pacientes de cirugía de cadera.', '2017-01-01', '2019-12-31', 'Cerrado', 'TRANSFERENCIA', 2, 'DIR001');

-- Nota: Ayudantes se insertarán dinámicamente (no requieren password)
