package ro.kmagic.klms.utils;

import org.bukkit.Bukkit;
import org.bukkit.ChatColor;
import org.bukkit.command.CommandSender;
import org.bukkit.configuration.file.FileConfiguration;

public interface MessageUtils {
    default String formatAll(String string) {
        return ChatColor.translateAlternateColorCodes('&', string);
    }

    default void sendLogger(String string) {
        Bukkit.getConsoleSender().sendMessage(formatAll(string));
    }

    default void sendCenteredMessage(CommandSender player, String message) {
        if (message == null || message.equals("")) {
            player.sendMessage("");
            return;
        }
        message = ChatColor.translateAlternateColorCodes('&', message);

        int messagePxSize = 0;
        boolean previousCode = false;
        boolean isBold = false;

        for (char c : message.toCharArray()) {
            if (c == '§') {
            previousCode = true;
        } else if (previousCode) {
            previousCode = false;
            isBold = (c == 'l' || c == 'L');
        } else {
            FontSize dFI = FontSize.getDefaultFontInfo(c);
            messagePxSize += isBold ? dFI.getBoldLength() : dFI.getLength();
            messagePxSize++;
        }
    }

    int halvedMessageSize = messagePxSize / 2;
    int toCompensate = 154 - halvedMessageSize;
    int spaceLength = FontSize.SPACE.getLength() + 1;
    int compensated = 0;
    StringBuilder sb = new StringBuilder();
    while (compensated < toCompensate) {
        sb.append(" ");
        compensated += spaceLength;
    }
    player.sendMessage(sb.toString() + message);
}

    default String convertTime(long time, FileConfiguration language) {
        StringBuilder stringBuilder = new StringBuilder();

        int years = (int)(time / 1000L / 60L / 60L / 24L / 30L / 12L);
        if (years != 0)
            stringBuilder.append(years).append(" ").append((years == 1) ? language.getString("General.Word Year") : language.getString("General.Word Years"));
        int months = (int)(time / 1000L / 60L / 60L / 24L / 30L % 12L);
        if (months != 0)
            stringBuilder.append((stringBuilder.length() != 0) ? " " : "").append(months).append(" ").append((months == 1) ? language.getString("General.Word Month") : language.getString("General.Word Months"));
        int days = (int)(time / 1000L / 60L / 60L / 24L % 30L);
        if (days != 0)
            stringBuilder.append((stringBuilder.length() != 0) ? " " : "").append(days).append(" ").append((days == 1) ? language.getString("General.Word Day") : language.getString("General.Word Days"));
        int hours = (int)(time / 1000L / 60L / 60L % 24L);
        if (hours != 0)
            stringBuilder.append((stringBuilder.length() != 0) ? " " : "").append(hours).append(" ").append((hours == 1) ? language.getString("General.Word Hour") : language.getString("General.Word Hours"));
        int minutes = (int)(time / 1000L / 60L % 60L);
        if (minutes != 0)
            stringBuilder.append((stringBuilder.length() != 0) ? " " : "").append(minutes).append(" ").append((minutes == 1) ? language.getString("General.Word Minute") : language.getString("General.Word Minutes"));
        int seconds = (int)(time / 1000L % 60L);
        if (seconds != 0) {
            stringBuilder.append((stringBuilder.length() != 0) ? " " : "").append(seconds).append(" ").append((seconds == 1) ? language.getString("General.Word Second") : language.getString("General.Word Seconds"));
        }
        return stringBuilder.toString();
    }
}
