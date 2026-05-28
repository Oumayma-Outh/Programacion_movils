# ✅ VERIFICACIÓN RÁPIDA - Antes de presentar

Ejecuta estos checks 5 minutos antes de presentar:

## 1. ¿Gradle compila sin errores?
```
✅ Build → Make Project
Resultado esperado: Build successful
```

## 2. ¿La app abre sin crashes?
```
✅ Run → Run 'app'
Resultado esperado: App se abre, pantalla de inicio visible
```

## 3. ¿Cloudinary está inicializado?
```
✅ Logcat durante onCreate
Buscar: "Cloudinary inicializado correctamente"
```

## 4. ¿Se puede seleccionar y subir imagen?
```
✅ Perfil → Editar imagen → Elegir → Actualizar
Resultado esperado:
- Imagen aparece en preview
- Barra de progreso se muestra
- Mensaje de éxito al terminar
```

## 5. ¿Se pueden cambiar preferencias?
```
✅ Perfil → Preferencias → Cambiar valores → Guardar
Resultado esperado:
- Mensaje "Preferencias guardadas"
- Valores persisten al reabrirse
```

## 6. ¿Funciona multi-idioma?
```
✅ Sistema → Configuración → Idioma → English
✅ Reabre app
Resultado esperado: Textos en inglés
```

## 7. ¿Responde bien a orientación?
```
✅ Ctrl+Flecha derecha (rotar dispositivo)
Resultado esperado: Layout se adapta sin errores
```

## 8. ¿Aparecen los diálogos?
```
(Agregar un botón de prueba en MainActivity)
✅ Toast.makeText().show() funciona
✅ DialogManager.mostrarAlertaSimple() funciona
```

## 9. ¿Se muestra la imagen en perfil?
```
✅ Ir a PerfilActivity
Resultado esperado: 
- Imagen cargada con Glide
- O placeholder por defecto
```

## 10. ¿No hay errores en Logcat?
```
✅ Ejecutar: `adb logcat | grep -E "(Error|Exception|CRASH)"`
Resultado esperado: Sin errores críticos
```

---

## 🚨 PROBLEMAS COMUNES Y SOLUCIONES

### ❌ "Build failed"
**Solución:**
```
File → Invalidate Caches → Restart
Build → Clean Project
Build → Rebuild Project
```

### ❌ "Cloudinary no se inicializa"
**Solución:**
- Verifica que MainActivity.kt llamа a `CloudinaryManager.initialize(this)`
- Revisa el Logcat: debe mostrar "Cloudinary inicializado correctamente"

### ❌ "Imagen no se sube"
**Solución:**
- Verifica conexión a internet
- Revisa permisos en AndroidManifest
- Checa que Cloud Name sea `dblb0kl2q`

### ❌ "Idioma no cambia"
**Solución:**
- Reinicia la app después de cambiar idioma del sistema
- Verifica que existen los archivos:
  - `res/values-es/strings.xml`
  - `res/values-en/strings.xml`
  - `res/values-fr/strings.xml`

### ❌ "Notificaciones no aparecen"
**Solución:**
- Abre Preferencias y activa "Notificaciones"
- En Android 13+: concede permiso POST_NOTIFICATIONS
- Verifica que `AppNotificationManager.crearCanalNotificacion()` se llamó

### ❌ "App se cierra al subir imagen"
**Solución:**
- Revisa Logcat para ver el error exacto
- Verifica que la URI de imagen es válida
- Comprueba permisos de lectura en Firebase

---

## 📋 CHECKLIST FINAL (5 minutos antes)

- [ ] ¿Gradle compila? ✅
- [ ] ¿App abre sin crashes? ✅
- [ ] ¿Puedo subir imagen a Cloudinary? ✅
- [ ] ¿Veo la imagen en perfil? ✅
- [ ] ¿Puedo cambiar preferencias? ✅
- [ ] ¿Se guardan los cambios? ✅
- [ ] ¿Funcionan los 3 idiomas? ✅
- [ ] ¿App responde a rotación? ✅
- [ ] ¿No hay errores en Logcat? ✅
- [ ] ¿Menú principal funciona? ✅
- [ ] ¿Fragmentos Usuarios/Chats cargan? ✅
- [ ] ¿Puedo navegar entre pantallas? ✅

---

## 🎯 DEMOSTRACIÓN AL PROFESOR (Guion sugerido)

1. **"Voy a mostrar la integración de Cloudinary"**
   - Abre Perfil → Editar imagen
   - Selecciona una foto
   - Muestra preview
   - Sube a Cloudinary
   - ✅ "¡La imagen se guardó en Cloudinary y la URL en Firebase!"

2. **"Tengo 3 idiomas completamente funcionales"**
   - Cambiar a English
   - Reabre app
   - "¿Ves que todo está en inglés? Cambio a Francés"
   - Reabre app
   - ✅ "¡Multi-idioma completamente funcional!"

3. **"La app es responsive en todos los dispositivos"**
   - Rota dispositivo
   - ✅ "¡Se adapta automáticamente!"

4. **"Tiene preferencias configurables"**
   - Abre Preferencias
   - Muestra todas las opciones
   - Cambia valores
   - Guarda
   - ✅ "¡Todas se guardan en la base de datos local!"

5. **"Incluye notificaciones con sonido y vibración"**
   - Muestra Preferencias
   - Explica que está integrado con AppNotificationManager
   - ✅ "¡Completamente configurables!"

6. **"La base de datos usa Firebase y Cloudinary"**
   - Muestra que imagen está en Cloudinary
   - ✅ "¡Almacenamiento en la nube completamente funcional!"

7. **"El código está bien estructurado y documentado"**
   - Muestra carpeta Utilidades con Managers
   - Muestra comentarios en el código
   - ✅ "¡Código limpio y mantenible!"

---

## ⏱️ TIMING ESTIMADO

- Verificación: 3-5 minutos
- Demostración: 5-10 minutos
- Preguntas: 5 minutos
- **Total: 15-20 minutos**

---

## 🎁 PUNTOS BONUS POSIBLES

Si el profesor pregunta:
- "¿Cómo escalarias la app?" → Cloud Functions, Firestore
- "¿Seguridad?" → Firebase Security Rules, Tokens
- "¿Performance?" → Lazy loading, Pagination
- "¿Analytics?" → Firebase Analytics está lista
- "¿Testing?" → Unit tests con JUnit

---

**¡Estás listo! Good luck! 🍀**
