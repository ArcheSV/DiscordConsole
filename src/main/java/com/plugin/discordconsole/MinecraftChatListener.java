package com.plugin.discordconsole;

import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.player.AsyncPlayerChatEvent;

import net.dv8tion.jda.api.entities.channel.concrete.TextChannel;

/**
 * Listener que intercepta el chat de Minecraft y lo manda al canal de Discord
 */
public class MinecraftChatListener implements Listener {

    private final TextChannel consoleChannel;

    public MinecraftChatListener(TextChannel consoleChannel) {
        this.consoleChannel = consoleChannel;
    }

    @EventHandler
    public void onPlayerChat(AsyncPlayerChatEvent event) {
        if (consoleChannel == null) return;

        String playerName = event.getPlayer().getName();
        String message = event.getMessage();

        // Envía "jugador: mensaje" al canal de Discord
        consoleChannel.sendMessage(playerName + ": " + message).queue();
    }
}
