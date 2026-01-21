# Lista de Iconos Necesarios para el Sistema de Gestión de Ayudantes

## Iconos Principales (32x32 o 48x48 px)

### Navegación y General
1. **home.png** - 🏠 Icono de inicio/home para el botón "Inicio"
2. **logo.png** - 🏛️ Logo institucional EPN/FIS (reemplaza emoji de edificio)
3. **epn-logo.png** - Logo oficial de la Escuela Politécnica Nacional

### Usuarios y Autenticación
4. **user.png** - 👤 Icono de usuario/perfil
5. **user-circle.png** - 👤 Avatar circular de usuario
6. **eye-open.png** - 👁️ Ver contraseña (ojo abierto)
7. **eye-closed.png** - 🙈 Ocultar contraseña (ojo cerrado)
8. **login.png** - Icono de inicio de sesión
9. **logout.png** - 🚪 Icono de salir/cerrar sesión

### Comunicación y Notificaciones
10. **bell.png** - 🔔 Campana de notificaciones
11. **bell-badge.png** - 🔔 Campana con badge numérico
12. **message.png** - 📩 Icono de mensaje/correo
13. **send.png** - ✉️ Enviar mensaje
14. **inbox.png** - Bandeja de entrada

### Acciones Principales
15. **add.png** - ➕ Agregar/añadir nuevo
16. **edit.png** - ✏️ Editar
17. **delete.png** - ❌ Eliminar/dar de baja
18. **remove.png** - ⭕ Remover
19. **refresh.png** - 🔄 Actualizar/refrescar
20. **save.png** - 💾 Guardar
21. **cancel.png** - ❌ Cancelar
22. **confirm.png** - ✅ Confirmar/aceptar

### Búsqueda y Filtros
23. **search.png** - 🔍 Buscar/lupa
24. **filter.png** - Filtros
25. **clear-filter.png** - Limpiar filtros

### Gestión de Ayudantes
26. **users.png** - 👥 Grupo de usuarios/ayudantes
27. **student.png** - 🎓 Estudiante
28. **assistant.png** - Ayudante/asistente
29. **check-active.png** - ✅ Estado activo
30. **check-inactive.png** - ⭕ Estado inactivo

### Proyectos y Reportes
31. **project.png** - 📊 Proyecto
32. **report.png** - 📄 Reporte/documento
33. **chart.png** - 📊 Gráfico/estadística
34. **folder.png** - 📁 Carpeta de proyecto
35. **document.png** - 📋 Documento

### Exportación y Archivos
36. **export-pdf.png** - 📑 Exportar a PDF
37. **export-excel.png** - 📊 Exportar a Excel
38. **export-html.png** - 🌐 Exportar a HTML
39. **upload.png** - 📤 Subir archivo
40. **download.png** - 📥 Descargar archivo

### Estados y Alertas
41. **success.png** - ✅ Éxito
42. **error.png** - ❌ Error
43. **warning.png** - ⚠️ Advertencia
44. **info.png** - ℹ️ Información

### Navegación de Tabla
45. **arrow-left.png** - ◀️ Anterior
46. **arrow-right.png** - ▶️ Siguiente
47. **arrow-up.png** - ▲ Ordenar ascendente
48. **arrow-down.png** - ▼ Ordenar descendente

## Iconos Secundarios (24x24 o 16x16 px)

### Badges y Mini-iconos
49. **badge-notification.png** - Badge numérico para notificaciones
50. **dot-active.png** - Punto de estado activo (verde)
51. **dot-inactive.png** - Punto de estado inactivo (rojo)

### Tabs y Secciones
52. **tab-users.png** - Tab de ayudantes
53. **tab-reports.png** - Tab de reportes

## Formato Recomendado

### Tamaños:
- **Grandes (Logo, Principal)**: 128x128 px, 96x96 px
- **Medianos (Botones, Acciones)**: 48x48 px, 32x32 px
- **Pequeños (Iconos inline, badges)**: 24x24 px, 16x16 px

### Formatos:
- **PNG** con transparencia (preferido para iconos con fondos)
- **SVG** (preferido para escalabilidad)
- **ICO** (para el icono de la aplicación principal)

### Paleta de Colores del Sistema:
- **Primario**: #003DA5 (Azul EPN)
- **Secundario**: #4A90E2 (Azul claro)
- **Éxito**: #2ECC71 (Verde)
- **Error**: #E74C3C (Rojo)
- **Advertencia**: #F39C12 (Naranja)
- **Texto**: #2C3E50 (Gris oscuro)

## Fuentes Gratuitas de Iconos

1. **Font Awesome** (https://fontawesome.com/)
2. **Material Icons** (https://fonts.google.com/icons)
3. **Feather Icons** (https://feathericons.com/)
4. **Heroicons** (https://heroicons.com/)
5. **Bootstrap Icons** (https://icons.getbootstrap.com/)
6. **Flaticon** (https://www.flaticon.com/) - Requiere atribución
7. **IconMonstr** (https://iconmonstr.com/)

## Notas de Implementación

Actualmente las vistas usan emojis Unicode (🏠, 👤, 🔔, etc.). Para una aplicación profesional:

1. Reemplazar emojis con iconos PNG/SVG en la carpeta `src/vistas/icons/`
2. Usar `ImageIcon` de Swing: 
   ```java
   ImageIcon icon = new ImageIcon(getClass().getResource("/vistas/icons/home.png"));
   JLabel lblInicio = new JLabel("Inicio", icon, JLabel.LEFT);
   ```
3. Considerar crear un `IconManager` o `ResourceManager` para cargar iconos centralizadamente
4. Mantener versiones en diferentes tamaños para diferentes contextos (toolbar, botones, listas)

## Iconos Críticos (Prioridad Alta)

Para empezar, estos son los más importantes:
- logo.png (identidad)
- user.png (autenticación)
- bell.png (notificaciones)
- add.png, delete.png, refresh.png (acciones CRUD)
- search.png (búsqueda)
- report.png, chart.png (reportes)
- success.png, error.png (feedback)
