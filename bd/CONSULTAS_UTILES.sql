-- ===========================
-- REFERENCIA RÁPIDA DE CONSULTAS SQLite
-- Gestión de Ayudantes FIS-EPN
-- ===========================

-- ===========================
-- 1. CONSULTAS DE DIRECTORES
-- ===========================

-- Todos los directores activos
SELECT numero_unico, nombres, apellidos, especialidad_director, correo_institucional
FROM MiembroFIS
WHERE tipo_miembro = 'DIRECTOR' AND estado = 'ACTIVO'
ORDER BY apellidos;

-- Directores con número de proyectos
SELECT m.numero_unico, m.nombres, m.apellidos, COUNT(p.id_proyecto) as num_proyectos
FROM MiembroFIS m
LEFT JOIN ProyectoInvestigacion p ON m.numero_unico = p.director_numero_unico
WHERE m.tipo_miembro = 'DIRECTOR'
GROUP BY m.numero_unico
ORDER BY num_proyectos DESC;

-- Directores con especialidad específica
SELECT * FROM v_directores WHERE especialidad_director LIKE '%Sistemas%';

-- ===========================
-- 2. CONSULTAS DE AYUDANTES
-- ===========================

-- Todos los ayudantes activos
SELECT * FROM v_ayudantes_activos ORDER BY promedio DESC;

-- Ayudantes por carrera
SELECT carrera, COUNT(*) as cantidad, AVG(promedio) as promedio_promedio
FROM v_ayudantes_activos
GROUP BY carrera
ORDER BY cantidad DESC;

-- Ayudantes por nivel
SELECT nivel, COUNT(*) as cantidad
FROM v_ayudantes_activos
GROUP BY nivel
ORDER BY nivel;

-- Top 10 ayudantes por promedio
SELECT numero_unico, nombre_completo, carrera, nivel, promedio
FROM v_ayudantes_activos
ORDER BY promedio DESC
LIMIT 10;

-- Ayudantes con promedio mayor a 9.0
SELECT * FROM v_ayudantes_activos WHERE promedio > 9.0;

-- Buscar ayudante por nombre
SELECT * FROM MiembroFIS 
WHERE tipo_miembro = 'AYUDANTE' AND nombres LIKE '%CARLOS%';

-- ===========================
-- 3. CONSULTAS DE PROYECTOS
-- ===========================

-- Todos los proyectos
SELECT * FROM v_proyectos_directores;

-- Proyectos por tipo
SELECT tipo_proyecto, COUNT(*) as cantidad, SUM(presupuesto) as presupuesto_total
FROM ProyectoInvestigacion
GROUP BY tipo_proyecto;

-- Proyectos activos con su director
SELECT p.codigo_proyecto, p.nombre_proyecto, p.presupuesto, 
       (m.nombres || ' ' || m.apellidos) as director
FROM ProyectoInvestigacion p
LEFT JOIN MiembroFIS m ON p.director_numero_unico = m.numero_unico
WHERE p.estado = 'ACTIVO'
ORDER BY p.presupuesto DESC;

-- Presupuesto total por tipo de proyecto
SELECT tipo_proyecto, SUM(presupuesto) as presupuesto_total
FROM ProyectoInvestigacion
GROUP BY tipo_proyecto;

-- Proyectos con más ayudantes asignados
SELECT p.codigo_proyecto, p.nombre_proyecto, COUNT(a.id_asignacion) as ayudantes_asignados
FROM ProyectoInvestigacion p
LEFT JOIN AsignacionAyudanteProyecto a ON p.codigo_proyecto = a.proyecto_codigo AND a.estado = 'ACTIVO'
GROUP BY p.codigo_proyecto
ORDER BY ayudantes_asignados DESC;

-- ===========================
-- 4. CONSULTAS DE ASIGNACIONES
-- ===========================

-- Todas las asignaciones activas
SELECT * FROM v_asignaciones_activas;

-- Ayudantes activos con su proyecto
SELECT a.id_asignacion,
       (m.nombres || ' ' || m.apellidos) as ayudante,
       m.carrera,
       p.codigo_proyecto,
       p.nombre_proyecto,
       a.horas_semanales,
       a.salario_por_hora,
       (a.horas_semanales * a.salario_por_hora * 4.33) as salario_mensual
FROM AsignacionAyudanteProyecto a
JOIN MiembroFIS m ON a.ayudante_numero_unico = m.numero_unico
JOIN ProyectoInvestigacion p ON a.proyecto_codigo = p.codigo_proyecto
WHERE a.estado = 'ACTIVO'
ORDER BY salario_mensual DESC;

-- Costo total mensual en ayudantes por proyecto
SELECT p.codigo_proyecto, p.nombre_proyecto,
       SUM(a.horas_semanales * a.salario_por_hora * 4.33) as costo_mensual_ayudantes
FROM ProyectoInvestigacion p
LEFT JOIN AsignacionAyudanteProyecto a ON p.codigo_proyecto = a.proyecto_codigo AND a.estado = 'ACTIVO'
GROUP BY p.codigo_proyecto
ORDER BY costo_mensual_ayudantes DESC;

-- Historial de asignaciones (activas + finalizadas)
SELECT a.id_asignacion,
       (m.nombres || ' ' || m.apellidos) as ayudante,
       p.codigo_proyecto,
       a.fecha_asignacion,
       a.estado,
       CASE WHEN a.estado = 'BAJA' THEN a.motivo_baja ELSE NULL END as razon
FROM AsignacionAyudanteProyecto a
JOIN MiembroFIS m ON a.ayudante_numero_unico = m.numero_unico
JOIN ProyectoInvestigacion p ON a.proyecto_codigo = p.codigo_proyecto
ORDER BY a.fecha_asignacion DESC;

-- ===========================
-- 5. CONSULTAS DE MENSAJES
-- ===========================

-- Todos los mensajes
SELECT * FROM Mensaje ORDER BY fecha_envio DESC;

-- Mensajes no leídos
SELECT * FROM Mensaje WHERE leido = 0 ORDER BY fecha_envio DESC;

-- Mensajes entre Jefa y Directores
SELECT m.id_mensaje, 
       (r.nombres || ' ' || r.apellidos) as remitente,
       (d.nombres || ' ' || d.apellidos) as destinatario,
       m.asunto,
       m.fecha_envio,
       m.leido
FROM Mensaje m
JOIN MiembroFIS r ON m.remitente_numero_unico = r.numero_unico
JOIN MiembroFIS d ON m.destinatario_numero_unico = d.numero_unico
WHERE m.proyecto_codigo IS NULL
ORDER BY m.fecha_envio DESC;

-- Conversación sobre un proyecto específico
SELECT * FROM Mensaje 
WHERE proyecto_codigo = 'PII-18-04'
ORDER BY fecha_envio;

-- ===========================
-- 6. CONSULTAS DE NOTIFICACIONES
-- ===========================

-- Todas las notificaciones
SELECT * FROM Notificacion ORDER BY fecha_creacion DESC;

-- Notificaciones no leídas
SELECT * FROM Notificacion WHERE leida = 0;

-- Notificaciones por tipo
SELECT tipo_notificacion, COUNT(*) as cantidad
FROM Notificacion
GROUP BY tipo_notificacion;

-- ===========================
-- 7. CONSULTAS DE REPORTES
-- ===========================

-- Todos los reportes
SELECT r.id_reporte, r.fecha_generacion, r.tipo_reporte, r.titulo,
       (m.nombres || ' ' || m.apellidos) as generado_por
FROM Reporte r
JOIN MiembroFIS m ON r.jefe_generador_numero_unico = m.numero_unico
ORDER BY r.fecha_generacion DESC;

-- Ayudantes en un reporte específico
SELECT ra.reporte_id, m.nombres, m.apellidos, m.carrera, m.nivel
FROM ReporteAyudante ra
JOIN MiembroFIS m ON ra.ayudante_numero_unico = m.numero_unico
WHERE ra.reporte_id = 'REP001'
ORDER BY m.apellidos;

-- ===========================
-- 8. CONSULTAS ANALÍTICAS
-- ===========================

-- Distribución de ayudantes por estado
SELECT m.estado, COUNT(*) as cantidad
FROM MiembroFIS m
WHERE m.tipo_miembro = 'AYUDANTE'
GROUP BY m.estado;

-- Promedio general de ayudantes
SELECT 
    COUNT(*) as total_ayudantes,
    AVG(promedio) as promedio_general,
    MIN(promedio) as promedio_minimo,
    MAX(promedio) as promedio_maximo
FROM MiembroFIS
WHERE tipo_miembro = 'AYUDANTE' AND estado = 'ACTIVO';

-- Inversión total en ayudantes (proyectos activos)
SELECT 
    COUNT(DISTINCT a.ayudante_numero_unico) as num_ayudantes,
    COUNT(a.id_asignacion) as num_asignaciones,
    SUM(a.horas_semanales * a.salario_por_hora * 4.33) as costo_mensual_total
FROM AsignacionAyudanteProyecto a
WHERE a.estado = 'ACTIVO';

-- Años de presupuesto total por tipo de proyecto
SELECT tipo_proyecto, 
       SUM(presupuesto) as presupuesto_total,
       AVG(presupuesto) as presupuesto_promedio,
       COUNT(*) as num_proyectos
FROM ProyectoInvestigacion
GROUP BY tipo_proyecto
ORDER BY presupuesto_total DESC;

-- Tasa de asignación de ayudantes
SELECT 
    (COUNT(DISTINCT a.ayudante_numero_unico) * 100.0 / 
     (SELECT COUNT(*) FROM MiembroFIS WHERE tipo_miembro = 'AYUDANTE' AND estado = 'ACTIVO')) as porcentaje_asignados
FROM AsignacionAyudanteProyecto a
WHERE a.estado = 'ACTIVO';

-- ===========================
-- FIN DE REFERENCIA RÁPIDA
-- ===========================
