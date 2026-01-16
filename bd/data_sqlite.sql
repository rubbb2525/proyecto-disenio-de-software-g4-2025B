-- ===========================
-- DATOS INICIALES: GESTIÓN DE AYUDANTES FIS-EPN (SQLite)
-- ===========================

-- ===========================
-- INSERTAR DIRECTORES (17 directores reales)
-- ===========================
INSERT INTO MiembroFIS (numero_unico, cedula, correo_institucional, password, nombres, apellidos, telefono, tipo_miembro, especialidad_director, estado) VALUES
('EPN001', '1701234567', 'sandra.sanchez@epn.edu.ec', '$2a$10$hashed1', 'SANDRA PATRICIA', 'SANCHEZ GORDON', '0991234567', 'DIRECTOR', 'Computación Centrado en el Humano', 'ACTIVO'),
('EPN002', '1702345678', 'rosa.navarrette@epn.edu.ec', '$2a$10$hashed2', 'ROSA DEL CARMEN', 'NAVARRETE RUEDA', '0992345678', 'DIRECTOR', 'Sistemas de Información', 'ACTIVO'),
('EPN003', '1703456789', 'jenny.torres@epn.edu.ec', '$2a$10$hashed3', 'JENNY GABRIELA', 'TORRES OLMEDO', '0993456789', 'DIRECTOR', 'Ciberseguridad', 'ACTIVO'),
('EPN004', '1704567890', 'maria.hallo@epn.edu.ec', '$2a$10$hashed4', 'MARIA ASUNCION', 'HALLO CARRASCO', '0994567890', 'DIRECTOR', 'Minería de Datos', 'ACTIVO'),
('EPN005', '1705678901', 'carlos.montenegro@epn.edu.ec', '$2a$10$hashed5', 'CARLOS ESTALESMIT', 'MONTENEGRO ARMAS', '0995678901', 'DIRECTOR', 'Sistemas de Información', 'ACTIVO'),
('EPN006', '1706789012', 'pamela.flores@epn.edu.ec', '$2a$10$hashed6', 'PAMELA CATHERINE', 'FLORES NARANJO', '0996789012', 'DIRECTOR', 'Ingeniería de Software', 'ACTIVO'),
('EPN007', '1707890123', 'enrique.larco@epn.edu.ec', '$2a$10$hashed7', 'ENRIQUE ANDRES', 'LARCO AMPUDIA', '0997890123', 'DIRECTOR', 'Sistemas de Información', 'ACTIVO'),
('EPN008', '1708901234', 'myriam.hernandez@epn.edu.ec', '$2a$10$hashed8', 'MYRIAM BEATRIZ', 'HERNANDEZ ALVAREZ', '0998901234', 'DIRECTOR', 'Seguridad Informática', 'ACTIVO'),
('EPN009', '1709012345', 'luis.mafla@epn.edu.ec', '$2a$10$hashed9', 'LUIS ENRIQUE', 'MAFLA GALLEGOS', '0999012345', 'DIRECTOR', 'Seguridad Informática', 'ACTIVO'),
('EPN010', '1710123456', 'edison.loza@epn.edu.ec', '$2a$10$hashed10', 'EDISON FERNANDO', 'LOZA AGUIRRE', '0990123456', 'DIRECTOR', 'Sistemas de Información', 'ACTIVO'),
('EPN011', '1711234567', 'tania.calle@epn.edu.ec', '$2a$10$hashed11', 'TANIA ELIZABETH', 'CALLE JIMENEZ', '0991234567', 'DIRECTOR', 'Interacción Humano-Computador', 'ACTIVO'),
('EPN012', '1712345678', 'myriam.penafiel@epn.edu.ec', '$2a$10$hashed12', 'MYRIAM GUADALUPE', 'PEÑAFIEL AGUILAR', '0992345678', 'DIRECTOR', 'Sistemas de Información', 'ACTIVO'),
('EPN013', '1713456789', 'regina.tenemaza@epn.edu.ec', '$2a$10$hashed13', 'REGINA MARITZOL', 'TENEMAZA VERA', '0993456789', 'DIRECTOR', 'Interacción Humano-Computador', 'ACTIVO'),
('EPN014', '1714567890', 'diana.yacchirema@epn.edu.ec', '$2a$10$hashed14', 'DIANA CECILIA', 'YACCHIREMA VARGAS', '0994567890', 'DIRECTOR', 'Sistemas de Información', 'ACTIVO'),
('EPN015', '1715678901', 'marco.benalcazar@epn.edu.ec', '$2a$10$hashed15', 'MARCO ENRIQUE', 'BENALCAZAR PALACIOS', '0995678901', 'DIRECTOR', 'Inteligencia Artificial', 'ACTIVO'),
('EPN016', '1716789012', 'oswaldo.santorum@epn.edu.ec', '$2a$10$hashed16', 'MARCO OSWALDO', 'SANTORUM GAIBOR', '0996789012', 'DIRECTOR', 'Sistemas de Información', 'ACTIVO'),
('EPN017', '1717890123', 'sang.yoo@epn.edu.ec', '$2a$10$hashed17', 'SANG GUUN', 'YOO', '0997890123', 'DIRECTOR', 'Computación Centrado en el Humano', 'ACTIVO');

-- ===========================
-- INSERTAR JEFE DE DEPARTAMENTO
-- ===========================
INSERT INTO MiembroFIS (numero_unico, cedula, correo_institucional, password, nombres, apellidos, telefono, tipo_miembro, estado, fecha_inicio_gestion) VALUES
('JEF001', '0102030405', 'jefe.informatica@epn.edu.ec', '$2a$10$hashedJefe', 'JUAN CARLOS', 'PEREZ GARCIA', '0987654321', 'JEFE_DEPARTAMENTO', 'ACTIVO', '2023-01-01');

-- ===========================
-- INSERTAR AYUDANTES (30 ayudantes de ejemplo)
-- ===========================
INSERT INTO MiembroFIS (numero_unico, cedula, correo_institucional, password, nombres, apellidos, telefono, tipo_miembro, carrera, nivel, promedio, estado, fecha_registro) VALUES
('AYD001', '1801234567', 'carlos.martinez@epn.edu.ec', '$2a$10$hashedA1', 'CARLOS ANDRES', 'MARTINEZ LOPEZ', '0981122334', 'AYUDANTE', 'Computación', 5, 8.5, 'ACTIVO', '2023-03-15'),
('AYD002', '1802345678', 'ana.rodriguez@epn.edu.ec', '$2a$10$hashedA2', 'ANA MARIA', 'RODRIGUEZ PEREZ', '0982233445', 'AYUDANTE', 'Sistemas de Información', 4, 9.2, 'ACTIVO', '2023-04-10'),
('AYD003', '1803456789', 'luis.garcia@epn.edu.ec', '$2a$10$hashedA3', 'LUIS FERNANDO', 'GARCIA SANCHEZ', '0983344556', 'AYUDANTE', 'Inteligencia Artificial', 6, 8.8, 'ACTIVO', '2023-02-20'),
('AYD004', '1804567890', 'maria.lopez@epn.edu.ec', '$2a$10$hashedA4', 'MARIA JOSE', 'LOPEZ GONZALEZ', '0984455667', 'AYUDANTE', 'Seguridad Informática', 5, 9.0, 'ACTIVO', '2023-05-05'),
('AYD005', '1805678901', 'javier.ramirez@epn.edu.ec', '$2a$10$hashedA5', 'JAVIER ALBERTO', 'RAMIREZ VARGAS', '0985566778', 'AYUDANTE', 'Computación', 4, 8.3, 'ACTIVO', '2023-06-12'),
('AYD006', '1806789012', 'sofia.mendoza@epn.edu.ec', '$2a$10$hashedA6', 'SOFIA ELENA', 'MENDOZA CASTRO', '0986677889', 'AYUDANTE', 'Sistemas de Información', 5, 9.1, 'ACTIVO', '2023-01-30'),
('AYD007', '1807890123', 'david.torres@epn.edu.ec', '$2a$10$hashedA7', 'DAVID ALEJANDRO', 'TORRES RUIZ', '0987788990', 'AYUDANTE', 'Ingeniería de Software', 6, 8.7, 'ACTIVO', '2023-03-25'),
('AYD008', '1808901234', 'carolina.silva@epn.edu.ec', '$2a$10$hashedA8', 'CAROLINA BEATRIZ', 'SILVA MORA', '0988899001', 'AYUDANTE', 'Interacción Humano-Computador', 4, 9.4, 'ACTIVO', '2023-04-18'),
('AYD009', '1809012345', 'andres.castro@epn.edu.ec', '$2a$10$hashedA9', 'ANDRES FELIPE', 'CASTRO DIAZ', '0989900112', 'AYUDANTE', 'Computación', 5, 8.6, 'ACTIVO', '2023-05-22'),
('AYD010', '1810123456', 'patricia.rios@epn.edu.ec', '$2a$10$hashedA10', 'PATRICIA ALEXANDRA', 'RIOS ORTIZ', '0980011223', 'AYUDANTE', 'Sistemas de Información', 6, 9.3, 'ACTIVO', '2023-02-14'),
('AYD011', '1811234567', 'diego.vargas@epn.edu.ec', '$2a$10$hashedA11', 'DIEGO MAURICIO', 'VARGAS SANCHEZ', '0981122334', 'AYUDANTE', 'Ciberseguridad', 5, 8.9, 'ACTIVO', '2023-06-10'),
('AYD012', '1812345678', 'valentina.cruz@epn.edu.ec', '$2a$10$hashedA12', 'VALENTINA ROCIO', 'CRUZ MORA', '0982233445', 'AYUDANTE', 'Inteligencia Artificial', 4, 9.0, 'ACTIVO', '2023-07-05'),
('AYD013', '1813456789', 'ricardo.moreno@epn.edu.ec', '$2a$10$hashedA13', 'RICARDO JAVIER', 'MORENO GOMEZ', '0983344556', 'AYUDANTE', 'Computación', 6, 8.4, 'ACTIVO', '2023-08-15'),
('AYD014', '1814567890', 'isabel.fontana@epn.edu.ec', '$2a$10$hashedA14', 'ISABEL CAROLINA', 'FONTANA VELASCO', '0984455667', 'AYUDANTE', 'Sistemas de Información', 5, 9.2, 'ACTIVO', '2023-09-01'),
('AYD015', '1815678901', 'sergio.acosta@epn.edu.ec', '$2a$10$hashedA15', 'SERGIO LUIS', 'ACOSTA FLORES', '0985566778', 'AYUDANTE', 'Ingeniería de Software', 4, 8.7, 'ACTIVO', '2023-10-10'),
('AYD016', '1816789012', 'laura.reyes@epn.edu.ec', '$2a$10$hashedA16', 'LAURA ALEJANDRA', 'REYES ORTIZ', '0986677889', 'AYUDANTE', 'Interacción Humano-Computador', 5, 9.5, 'ACTIVO', '2023-11-05'),
('AYD017', '1817890123', 'pablo.mendez@epn.edu.ec', '$2a$10$hashedA17', 'PABLO ANDRES', 'MENDEZ SILVA', '0987788990', 'AYUDANTE', 'Ciberseguridad', 6, 8.8, 'ACTIVO', '2023-12-01'),
('AYD018', '1818901234', 'natalia.rojas@epn.edu.ec', '$2a$10$hashedA18', 'NATALIA FERNANDA', 'ROJAS TORRES', '0988899001', 'AYUDANTE', 'Minería de Datos', 4, 9.1, 'ACTIVO', '2024-01-08'),
('AYD019', '1819012345', 'alejandro.diaz@epn.edu.ec', '$2a$10$hashedA19', 'ALEJANDRO RAFAEL', 'DIAZ GUTIERREZ', '0989900112', 'AYUDANTE', 'Computación', 5, 8.5, 'ACTIVO', '2024-01-10'),
('AYD020', '1820123456', 'catalina.ortiz@epn.edu.ec', '$2a$10$hashedA20', 'CATALINA MONICA', 'ORTIZ CAMPOS', '0980011223', 'AYUDANTE', 'Sistemas de Información', 6, 9.4, 'ACTIVO', '2024-01-12'),
('AYD021', '1821234567', 'fernando.valle@epn.edu.ec', '$2a$10$hashedA21', 'FERNANDO MIGUEL', 'VALLE GARCIA', '0981122334', 'AYUDANTE', 'Ingeniería de Software', 5, 8.6, 'ACTIVO', '2024-01-14'),
('AYD022', '1822345678', 'claudia.santos@epn.edu.ec', '$2a$10$hashedA22', 'CLAUDIA PATRICIA', 'SANTOS MIRANDA', '0982233445', 'AYUDANTE', 'Inteligencia Artificial', 4, 9.3, 'ACTIVO', '2024-01-16'),
('AYD023', '1823456789', 'manuel.torres@epn.edu.ec', '$2a$10$hashedA23', 'MANUEL FRANCISCO', 'TORRES PONCE', '0983344556', 'AYUDANTE', 'Ciberseguridad', 5, 8.9, 'ACTIVO', '2024-01-18'),
('AYD024', '1824567890', 'gloria.lopez@epn.edu.ec', '$2a$10$hashedA24', 'GLORIA MERCEDES', 'LOPEZ RIVERA', '0984455667', 'AYUDANTE', 'Computación', 6, 8.7, 'ACTIVO', '2024-01-20'),
('AYD025', '1825678901', 'victor.ramon@epn.edu.ec', '$2a$10$hashedA25', 'VICTOR RAMON', 'RAMON DELGADO', '0985566778', 'AYUDANTE', 'Sistemas de Información', 4, 9.2, 'ACTIVO', '2024-01-22'),
('AYD026', '1826789012', 'roxana.duran@epn.edu.ec', '$2a$10$hashedA26', 'ROXANA ADRIANA', 'DURAN SANTOS', '0986677889', 'AYUDANTE', 'Interacción Humano-Computador', 5, 9.0, 'ACTIVO', '2024-01-24'),
('AYD027', '1827890123', 'herman.loaiza@epn.edu.ec', '$2a$10$hashedA27', 'HERMAN GUILLERMO', 'LOAIZA MOLINA', '0987788990', 'AYUDANTE', 'Minería de Datos', 6, 8.8, 'ACTIVO', '2024-01-26'),
('AYD028', '1828901234', 'monica.garcia@epn.edu.ec', '$2a$10$hashedA28', 'MONICA ELIZABETH', 'GARCIA HERRERA', '0988899001', 'AYUDANTE', 'Ingeniería de Software', 4, 8.5, 'ACTIVO', '2024-01-28'),
('AYD029', '1829012345', 'rafael.nunez@epn.edu.ec', '$2a$10$hashedA29', 'RAFAEL ANTONIO', 'NUNEZ CRUZ', '0989900112', 'AYUDANTE', 'Ciberseguridad', 5, 9.1, 'ACTIVO', '2024-02-01'),
('AYD030', '1830123456', 'viviana.reina@epn.edu.ec', '$2a$10$hashedA30', 'VIVIANA CAROLINA', 'REINA TORRES', '0980011223', 'AYUDANTE', 'Computación', 6, 9.3, 'ACTIVO', '2024-02-03');

-- ===========================
-- INSERTAR PROYECTOS DE INVESTIGACIÓN
-- ===========================

-- PROYECTOS INTERNOS
INSERT INTO ProyectoInvestigacion (codigo_proyecto, nombre_proyecto, descripcion, fecha_inicio, fecha_fin, presupuesto, tipo_proyecto, ayudantes_planificados, director_numero_unico, estado) VALUES
('PII-19-02', 'Modelo y Prototipo para Creación de Perfiles de Estudiantes con Discapacidades en Ambientes e-Learning', 'Diseño de modelos para personalización de entornos e-learning accesibles', '2020-01-15', '2022-12-15', 15000.00, 'INTERNO', 3, 'EPN001', 'FINALIZADO'),
('PII-18-01', 'Semántica embebida con JSON-LD para generar información enriquecida en resultados de búsqueda de Recursos Educativos Abiertos', 'Implementación de metadatos semánticos para recursos educativos', '2019-03-01', '2021-03-01', 12000.00, 'INTERNO', 2, 'EPN002', 'FINALIZADO'),
('PII-18-02', 'Aplicación de la teoría de juego para fortalecer las habilidades de gestión en cyberseguridad', 'Desarrollo de simulaciones basadas en teoría de juegos para capacitación en seguridad', '2019-04-10', '2021-04-10', 18000.00, 'INTERNO', 3, 'EPN003', 'FINALIZADO'),
('PII-18-04', 'Marco de trabajo para la predicción de contribuyentes deudores con alto riesgo de no pago de impuestos usando técnicas de minería de datos', 'Sistema predictivo para detección de morosidad tributaria', '2019-05-20', '2021-05-20', 20000.00, 'INTERNO', 4, 'EPN004', 'ACTIVO'),
('PII-17-02', 'Minería de texto para construir la filogenia de los insectos vectores de la enfermedad de chagas', 'Análisis textual para estudios filogenéticos en entomología', '2018-02-15', '2020-02-15', 14000.00, 'INTERNO', 2, 'EPN004', 'FINALIZADO'),
('PII-17-05', 'Utilización de técnicas de análisis de textos para evaluar iniciativas sociales y ecológicas de sostenibilidad en los últimos 15 años', 'Estudio de sostenibilidad mediante análisis de textos académicos', '2018-06-01', '2020-06-01', 16000.00, 'INTERNO', 3, 'EPN005', 'FINALIZADO'),
('PII-17-11', 'Caracterización de ejercicios de diseño de software para potenciar la habilidad de abstraer', 'Metodología para enseñanza de diseño de software', '2018-08-10', '2020-08-10', 13000.00, 'INTERNO', 2, 'EPN006', 'ACTIVO'),
('PII-17-12', 'Mejora de las competencias para la vida en función de la calidad de las aplicaciones educativas web y móviles', 'Evaluación de impacto de aplicaciones educativas en competencias', '2018-09-15', '2020-09-15', 17000.00, 'INTERNO', 3, 'EPN007', 'FINALIZADO'),
('PII-17-14', 'Detección de ramsonware a gran escala por medio de seguridad cognitiva', 'Sistema de detección temprana de ransomware mediante IA', '2018-10-20', '2020-10-20', 22000.00, 'INTERNO', 4, 'EPN008', 'ACTIVO'),
('PII-17-15', 'Diseño de un sistema seguro de gestión de identidades para el Registro Civil del Ecuador', 'Arquitectura de gestión de identidades para entidad gubernamental', '2018-11-01', '2020-11-01', 25000.00, 'INTERNO', 3, 'EPN009', 'ACTIVO');

-- PROYECTOS SEMILLA
INSERT INTO ProyectoInvestigacion (codigo_proyecto, nombre_proyecto, descripcion, fecha_inicio, fecha_fin, presupuesto, tipo_proyecto, ayudantes_planificados, director_numero_unico, estado) VALUES
('PIS-20-02', 'Agentes de adquisición, procesamiento y respuesta de un sistema emergente para gestión de accidentabilidad en vehículos', 'Sistema inteligente para gestión de accidentes vehiculares', '2021-03-01', '2023-12-31', 8000.00, 'SEMILLA', 2, 'EPN008', 'ACTIVO'),
('PIS-20-07', 'Un enfoque interoperable basado en IoT para apoyar el envejecimiento activo y saludable de los adultos mayores', 'Plataforma IoT para monitoreo de adultos mayores', '2021-04-15', '2023-12-31', 7500.00, 'SEMILLA', 2, 'EPN014', 'ACTIVO'),
('PIS-19-11', 'Estudio longitudinal cualitativo sobre la abstracción y el diseño de software a través de ejercicios de diseño', 'Investigación cualitativa sobre procesos cognitivos en diseño de software', '2020-02-01', '2022-02-01', 6000.00, 'SEMILLA', 2, 'EPN006', 'FINALIZADO'),
('PIS-17-10', 'Minería de datos para detección de escenarios relacionados con el delito de tráfico de personas', 'Detección de patrones de tráfico de personas mediante ML', '2018-08-20', '2020-08-20', 9000.00, 'SEMILLA', 2, 'EPN008', 'FINALIZADO'),
('PIS-17-15', 'Control de dispositivos a través del pensamiento (ondas cerebrales)', 'Interfaz cerebro-computadora para control de dispositivos', '2018-11-15', '2020-11-15', 9500.00, 'SEMILLA', 2, 'EPN017', 'ACTIVO');

-- PROYECTOS GRUPALES
INSERT INTO ProyectoInvestigacion (codigo_proyecto, nombre_proyecto, descripcion, fecha_inicio, fecha_fin, presupuesto, tipo_proyecto, ayudantes_planificados, director_numero_unico, estado) VALUES
('PIGR-19-07', 'Reconocimiento de gestos de la mano usando señales electromiográficas e inteligencia artificial', 'Sistema de reconocimiento de gestos mediante EMG y IA', '2020-06-01', '2023-12-31', 30000.00, 'GRUPALES', 4, 'EPN015', 'ACTIVO');

-- PROYECTOS TRANSFERENCIA TECNOLÓGICA
INSERT INTO ProyectoInvestigacion (codigo_proyecto, nombre_proyecto, descripcion, fecha_inicio, fecha_fin, presupuesto, tipo_proyecto, ayudantes_planificados, director_numero_unico, estado) VALUES
('PIE-CEPRA-XIII', 'Reconocimiento de gestos de la mano usando señales electromiográficas (EMG) e inteligencia artificial', 'Transferencia tecnológica de sistema de reconocimiento de gestos', '2019-05-01', '2021-05-01', 35000.00, 'TRANSFERENCIA_TECNOLOGICA', 3, 'EPN015', 'ACTIVO'),
('PIE-INEDITA-01', 'Un framework como herramienta de apoyo para mejorar las habilidades socio-cognitivas', 'Framework para desarrollo de habilidades socio-cognitivas', '2019-02-15', '2021-02-15', 28000.00, 'TRANSFERENCIA_TECNOLOGICA', 3, 'EPN016', 'ACTIVO'),
('PIE-CEPRA-XI', 'Sistema de tele-rehabilitación para la auto-reeducación post-quirúrgica de cadera', 'Plataforma de tele-rehabilitación para pacientes post-quirúrgicos', '2017-09-01', '2019-09-01', 32000.00, 'TRANSFERENCIA_TECNOLOGICA', 3, 'EPN001', 'FINALIZADO');

-- ===========================
-- INSERTAR ASIGNACIONES DE AYUDANTES A PROYECTOS
-- ===========================
INSERT INTO AsignacionAyudanteProyecto (ayudante_numero_unico, proyecto_codigo, fecha_asignacion, estado, horas_semanales, salario_por_hora) VALUES
-- Proyecto PII-19-02 (3 ayudantes)
('AYD001', 'PII-19-02', '2023-03-15', 'FINALIZADO', 15, 4.50),
('AYD008', 'PII-19-02', '2023-04-10', 'FINALIZADO', 15, 4.50),
('AYD015', 'PII-19-02', '2023-05-05', 'FINALIZADO', 15, 4.50),

-- Proyecto PII-18-04 (4 ayudantes)
('AYD002', 'PII-18-04', '2023-04-10', 'ACTIVO', 15, 4.50),
('AYD009', 'PII-18-04', '2023-05-22', 'ACTIVO', 15, 4.50),
('AYD016', 'PII-18-04', '2023-06-18', 'ACTIVO', 15, 4.50),
('AYD023', 'PII-18-04', '2023-07-12', 'ACTIVO', 15, 4.50),

-- Proyecto PII-17-14 (4 ayudantes)
('AYD004', 'PII-17-14', '2023-05-05', 'ACTIVO', 15, 4.50),
('AYD011', 'PII-17-14', '2023-06-30', 'ACTIVO', 15, 4.50),
('AYD018', 'PII-17-14', '2023-08-15', 'ACTIVO', 15, 4.50),
('AYD025', 'PII-17-14', '2023-09-10', 'ACTIVO', 15, 4.50),

-- Proyecto PIS-20-02 (2 ayudantes)
('AYD003', 'PIS-20-02', '2023-02-20', 'ACTIVO', 15, 4.50),
('AYD010', 'PIS-20-02', '2023-03-25', 'ACTIVO', 15, 4.50),

-- Proyecto PIGR-19-07 (4 ayudantes)
('AYD005', 'PIGR-19-07', '2023-06-12', 'ACTIVO', 15, 4.50),
('AYD012', 'PIGR-19-07', '2023-07-08', 'ACTIVO', 15, 4.50),
('AYD019', 'PIGR-19-07', '2023-08-25', 'ACTIVO', 15, 4.50),
('AYD026', 'PIGR-19-07', '2023-09-20', 'ACTIVO', 15, 4.50),

-- Proyectos con estado finalizado
('AYD007', 'PII-17-11', '2023-03-25', 'BAJA', 15, 4.50);

-- ===========================
-- INSERTAR MENSAJES
-- ===========================
INSERT INTO Mensaje (id_mensaje, fecha_envio, asunto, contenido, remitente_numero_unico, destinatario_numero_unico, proyecto_codigo, leido) VALUES
('MSG001', '2024-01-15 10:30:00', 'Revisión de planificación de ayudantes', 'Estimado Director, solicito revisar la planificación de ayudantes para el próximo semestre.', 'JEF001', 'EPN001', 'PII-19-02', 1),
('MSG002', '2024-01-16 14:20:00', 'Informe de progreso trimestral', 'Por favor enviar informe de progreso del proyecto PII-18-04 antes del 30 de enero.', 'JEF001', 'EPN004', 'PII-18-04', 0),
('MSG003', '2024-01-17 09:15:00', 'Capacitación en seguridad informática', 'Invitación a capacitación en nuevas técnicas de seguridad el próximo viernes.', 'EPN008', 'EPN001', NULL, 1),
('MSG004', '2024-01-18 11:45:00', 'Solicitud de ampliación de presupuesto', 'Solicito considerar ampliación de presupuesto para contratación de ayudantes adicionales.', 'EPN015', 'JEF001', 'PIGR-19-07', 0);

-- ===========================
-- INSERTAR NOTIFICACIONES
-- ===========================
INSERT INTO Notificacion (id_notificacion, fecha_creacion, mensaje, tipo_notificacion, leida, jefe_departamento_numero_unico, proyecto_codigo, ayudante_numero_unico) VALUES
('NOT001', '2024-01-10 08:30:00', 'Se ha registrado un nuevo ayudante: CARLOS MARTINEZ en el proyecto PII-19-02', 'NUEVO_AYUDANTE', 1, 'JEF001', 'PII-19-02', 'AYD001'),
('NOT002', '2024-01-12 16:45:00', 'El ayudante DAVID TORRES ha sido dado de baja del proyecto PII-17-11', 'BAJA_AYUDANTE', 0, 'JEF001', 'PII-17-11', 'AYD007'),
('NOT003', '2024-01-14 10:20:00', 'El proyecto PIS-20-02 ha alcanzado el 75% de sus objetivos', 'PROGRESO_PROYECTO', 0, 'JEF001', 'PIS-20-02', NULL),
('NOT004', '2024-01-15 14:15:00', 'Recordatorio: Reunión de evaluación de proyectos el próximo martes', 'RECORDATORIO', 1, 'JEF001', NULL, NULL);

-- ===========================
-- INSERTAR REPORTES
-- ===========================
INSERT INTO Reporte (id_reporte, fecha_generacion, tipo_reporte, titulo, contenido, ruta_archivo, jefe_generador_numero_unico) VALUES
('REP001', '2024-01-05 09:00:00', 'GENERAL', 'Reporte Anual de Ayudantes 2023', 'Análisis completo de ayudantes activos, proyectos y distribución por carrera', '/reportes/2023_general.pdf', 'JEF001'),
('REP002', '2024-01-10 11:30:00', 'POR_PROYECTO', 'Reporte Proyecto PII-19-02', 'Estado actual, ayudantes asignados y progreso del proyecto', '/reportes/PII-19-02_2024.pdf', 'JEF001'),
('REP003', '2024-01-12 15:45:00', 'POR_CARRERA', 'Reporte Ayudantes Carrera Computación', 'Distribución de ayudantes de computación por proyecto y nivel', '/reportes/computacion_2024.pdf', 'JEF001');

-- ===========================
-- INSERTAR RELACIÓN REPORTE-AYUDANTES
-- ===========================
INSERT INTO ReporteAyudante (reporte_id, ayudante_numero_unico) VALUES
('REP001', 'AYD001'),
('REP001', 'AYD002'),
('REP001', 'AYD003'),
('REP002', 'AYD001'),
('REP002', 'AYD008'),
('REP002', 'AYD015'),
('REP003', 'AYD001'),
('REP003', 'AYD005'),
('REP003', 'AYD009');

-- ===========================
-- FIN DEL SCRIPT DE INSERCIÓN
-- ===========================
