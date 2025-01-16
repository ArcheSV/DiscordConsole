# **DiscordConsolePlugin**

**DiscordConsolePlugin** es un plugin de Minecraft que conecta la consola del servidor y el chat del juego con Discord, permitiendo ejecutar comandos desde Discord y enviar logs y mensajes del chat al canal correspondiente.

## 🚀 **Características**
- Envía los mensajes del chat de Minecraft a un canal de Discord.
- Ejecuta comandos del servidor desde un canal de Discord.
- Envía los logs de la consola del servidor a un canal de Discord.

## 📦 **Instalación**

1. **Descargar el plugin de releases o compilalo tu mismo:**
   - Asegúrate de compilar el proyecto utilizando Maven para obtener el archivo `.jar`. Puedes usar el siguiente comando:
     ```bash
     mvn clean package
     ```
     El archivo se generará en la carpeta `target`.

2. **Añadir el plugin al servidor:**
   - Coloca el archivo `.jar` en la carpeta `plugins` de tu servidor de Minecraft.

3. **Configurar el archivo `config.yml`:**
   - Después de ejecutar el servidor por primera vez, edita el archivo `plugins/DiscordConsolePlugin/config.yml` con los valores de tu bot de Discord y los canales:

     ```yaml
     bot-token: "DISCORD TOKEN"         # Reemplaza con el token de tu bot
     channel-id: "CHAT_CHANNEL_ID"      # ID del canal para chat
     logs-channel-id: "LOGS_CHANNEL_ID" # ID del canal para logs
     ```

4. **Reiniciar el servidor:**
   - Reinicia el servidor para cargar los cambios y activar el plugin.

## ⚙️ **Configuración**

### **Archivo `config.yml`**
Asegúrate de configurar correctamente los siguientes parámetros en `config.yml`:

```yaml
bot-token: "DISCORD TOKEN"         # Token del bot de Discord
channel-id: "CHAT_CHANNEL_ID"      # ID del canal donde se enviará el chat del servidor
logs-channel-id: "LOGS_CHANNEL_ID" # ID del canal donde se enviarán los logs
```

### **Permisos del bot en Discord**
El bot debe tener los siguientes permisos en los canales configurados:
- Leer mensajes.
- Enviar mensajes.
- Acceder al historial de mensajes.
- Administrar mensajes (si es necesario).

Además, habilita los **Gateway Intents** requeridos en el [panel de aplicaciones de Discord](https://discord.com/developers/applications):
- **Message Content Intent**.
- **Guild Messages Intent**.

## 🕹️ **Comandos**

No hay comandos directos en el juego, ya que el plugin se enfoca en integrar Discord con la consola y el chat.

## 🔒 **Permisos**

El plugin no introduce permisos nuevos en Minecraft. Sin embargo, asegúrate de que el bot de Discord tenga los permisos adecuados en el canal configurado.

## 💻 **Dependencias**

Este plugin utiliza las siguientes dependencias:
- [JDA (Java Discord API)](https://github.com/DV8FromTheWorld/JDA) para la integración con Discord.
- [Spigot API](https://www.spigotmc.org/) para la integración con Minecraft.

Asegúrate de que Maven esté configurado correctamente para descargar estas dependencias. El archivo `pom.xml` ya incluye las configuraciones necesarias.

## ❓ **Preguntas Frecuentes**

### **¿Qué versiones de Minecraft son compatibles?**
El plugin es compatible con Minecraft 1.8 hasta 1.21.4

### **¿Cómo obtengo el token del bot y el ID de los canales de Discord?**
1. Crea un bot en el [portal de desarrolladores de Discord](https://discord.com/developers/applications).
2. Copia el token del bot y pégalo en el archivo `config.yml`.
3. Habilita el modo de desarrollador en Discord y haz clic derecho en el canal deseado para copiar su ID.
