package ro.kmagic.klms.listeners;

import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.player.PlayerCommandPreprocessEvent;
import ro.kmagic.klms.KLMS;
import ro.kmagic.klms.utils.MessageUtils;

public class PlayerCommandPreprocessListener implements Listener, MessageUtils {
    KLMS instance;

    public PlayerCommandPreprocessListener(KLMS instance) {
        this.instance = instance;
    }

    @EventHandler
    public void onPlayerCommandPreprocess(PlayerCommandPreprocessEvent event) {
        Player player = event.getPlayer();

        if (!this.instance.getPlayers().contains(player) && !this.instance.getSpectators().contains(player)) {
            return;
        }

        if (this.instance.getPlayers().contains(player) || this.instance.getSpectators().contains(player)) {
            for (String command : this.instance.getSettingsManager().getConfig().getStringList("Game.Command Whitelist")) {
                if (event.getMessage().contains("/" + command) || command.equalsIgnoreCase("*")) {
                    return;
                }
            }
        }

        player.sendMessage(formatAll(this.instance.getLanguageManager().getConfig().getString("General.Blocked Command")));
        event.setCancelled(true);
    }
}

