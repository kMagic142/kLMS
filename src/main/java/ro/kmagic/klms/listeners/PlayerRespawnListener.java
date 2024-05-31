package ro.kmagic.klms.listeners;

import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.player.PlayerRespawnEvent;
import ro.kmagic.klms.KLMS;
import ro.kmagic.klms.utils.MessageUtils;
import ro.kmagic.klms.utils.PlayerUtils;

public class PlayerRespawnListener implements Listener, MessageUtils, PlayerUtils {
    KLMS instance;

    public PlayerRespawnListener(KLMS instance) {
        this.instance = instance;
    }

    @EventHandler
    public void onPlayerDeath(PlayerRespawnEvent event) {
        Player player = event.getPlayer();

        if (this.instance.getPlayers().contains(player))
            this.instance.getGameManager().removePlayer(player);
    }
}

