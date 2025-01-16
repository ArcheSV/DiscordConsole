package com.plugin.discordconsole;

import org.bukkit.Bukkit;
import org.bukkit.command.ConsoleCommandSender;
import org.bukkit.event.Listener;
import org.bukkit.plugin.PluginManager;
import org.bukkit.plugin.java.JavaPlugin;

import java.util.logging.Logger;

import net.dv8tion.jda.api.JDA;
import net.dv8tion.jda.api.JDABuilder;
// Import para Gateway Intents
import net.dv8tion.jda.api.requests.GatewayIntent;

import net.dv8tion.jda.api.entities.channel.concrete.TextChannel;
import net.dv8tion.jda.api.events.message.MessageReceivedEvent;
import net.dv8tion.jda.api.hooks.ListenerAdapter;

public class DiscordConsolePlugin extends JavaPlugin {

    private JDA jda;
    private DiscordListener discordListener;
    private ConsoleLoggerInterceptor consoleHandler;

    // Canales de Discord
    private TextChannel consoleChannel; // Para chat de Minecraft
    private TextChannel logsChannel;    // Para logs

    @Override
    public void onEnable() {
        // Carga el config.yml
        saveDefaultConfig();

        // Obtiene los valores de la config
        String botToken      = getConfig().getString("bot-token");
        String channelId     = getConfig().getString("channel-id");
        String logsChannelId = getConfig().getString("logs-channel-id");

        try {
            // Creamos nuestro listener para Discord
            discordListener = new DiscordListener();

            // Construir JDA con los Gateway Intents necesarios, incluido MESSAGE_CONTENT
            JDABuilder builder = JDABuilder.createDefault(
                botToken,
                GatewayIntent.GUILD_MESSAGES,
                GatewayIntent.DIRECT_MESSAGES,
                GatewayIntent.MESSAGE_CONTENT
            )
            .addEventListeners(discordListener);

            // Inicia sesión en Discord
            jda = builder.build();
            jda.awaitReady(); 

            // Canal de chat
            consoleChannel = jda.getTextChannelById(channelId);
            if (consoleChannel == null) {
                getLogger().severe("No se encontró el canal (channel-id): " + channelId);
            } else {
                consoleChannel.sendMessage("¡Servidor conectado! (Chat)").queue();
            }

            // Canal de logs
            logsChannel = jda.getTextChannelById(logsChannelId);
            if (logsChannel == null) {
                getLogger().severe("No se encontró el canal de logs (logs-channel-id): " + logsChannelId);
            } else {
                logsChannel.sendMessage("¡Servidor conectado! (Logs activos)").queue();
            }

            // Interceptar logs de la consola
            interceptConsoleLogs();

        } catch (InterruptedException e) {
            getLogger().severe("Error al iniciar sesión en Discord: " + e.getMessage());
        }

        // Registrar el listener de chat de Minecraft para que envíe mensajes al canal de Discord
        PluginManager pm = getServer().getPluginManager();
        pm.registerEvents(new MinecraftChatListener(consoleChannel), this);

        getLogger().info("DiscordConsolePlugin habilitado!");
    }

    @Override
    public void onDisable() {
        getLogger().info("DiscordConsolePlugin deshabilitado!");

        // Quitar el Handler de logs
        if (consoleHandler != null) {
            Logger bukkitLogger = Bukkit.getLogger();
            bukkitLogger.removeHandler(consoleHandler);
        }

        // Remover el listener y cerrar JDA
        if (jda != null) {
            if (discordListener != null) {
                jda.removeEventListener(discordListener);
            }
            jda.shutdownNow();
        }
    }

    /**
     * Intercepta logs de la consola y los envía a logsChannel
     */
    private void interceptConsoleLogs() {
        if (logsChannel == null) return;

        Logger bukkitLogger = Bukkit.getLogger();
        consoleHandler = new ConsoleLoggerInterceptor(logsChannel);
        bukkitLogger.addHandler(consoleHandler);
    }

    /**
     * Listener para los mensajes de Discord en el canal
     */
    private class DiscordListener extends ListenerAdapter {
        @Override
        public void onMessageReceived(MessageReceivedEvent event) {
            // Ignoramos mensajes de bots
            if (event.getAuthor().isBot()) return;

            // Solo el canal configurado
            String configuredChannelId = getConfig().getString("channel-id");
            if (!event.getChannel().getId().equals(configuredChannelId)) return;

            if (!DiscordConsolePlugin.this.isEnabled()) {
                return;
            }

            // Texto del mensaej
            String message = event.getMessage().getContentRaw();

            // Programar la ejecución en el hilo principal de Bukkit
            Bukkit.getScheduler().runTask(DiscordConsolePlugin.this, () -> {
                if (!DiscordConsolePlugin.this.isEnabled()) {
                    return;
                }

                // Ejecutar comando
                ConsoleCommandSender console = Bukkit.getConsoleSender();
                Bukkit.dispatchCommand(console, message);
            });
        }
    }
}
