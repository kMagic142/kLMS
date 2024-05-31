package ro.kmagic.klms.listeners;

import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.player.PlayerDropItemEvent;
import ro.kmagic.klms.KLMS;

public class PlayerDropItemListener implements Listener {
    KLMS instance;

    public PlayerDropItemListener(KLMS instance) {
        this.instance = instance;
    }

    @EventHandler
    public void onPlayerDropItem(PlayerDropItemEvent event) {
        Player player = event.getPlayer();

        if (this.instance.getPlayers().contains(player) || this.instance.getSpectators().contains(player))
            event.setCancelled(true);
    }
}

