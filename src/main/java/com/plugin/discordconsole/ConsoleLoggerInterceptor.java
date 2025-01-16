package com.plugin.discordconsole;

import net.dv8tion.jda.api.entities.channel.concrete.TextChannel;

import java.util.logging.Handler;
import java.util.logging.Level;
import java.util.logging.LogRecord;

public class ConsoleLoggerInterceptor extends Handler {

    private final TextChannel logsChannel;

    public ConsoleLoggerInterceptor(TextChannel logsChannel) {
        this.logsChannel = logsChannel;
    }

    @Override
    public void publish(LogRecord record) {
        if (logsChannel == null) return;

        // filtrar logs de menor nivel que INFO
        if (record.getLevel().intValue() < Level.INFO.intValue()) {
            return;
        }

        String msg = record.getMessage();
        if (msg == null) return;

        // Envía el log al canal de logs
        logsChannel.sendMessage("[CONSOLE] " + msg).queue();
    }

    @Override
    public void flush() { }

    @Override
    public void close() throws SecurityException { }
}
