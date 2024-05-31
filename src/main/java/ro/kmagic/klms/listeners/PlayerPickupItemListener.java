package ro.kmagic.klms.listeners;

import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.player.PlayerPickupItemEvent;
import ro.kmagic.klms.KLMS;

public class PlayerPickupItemListener implements Listener {
    KLMS instance;

    public PlayerPickupItemListener(KLMS instance) {
        this.instance = instance;
    }

    @EventHandler
    public void onPlayerPickupItem(PlayerPickupItemEvent event) {
        Player player = event.getPlayer();

        if (this.instance.getPlayers().contains(player) || this.instance.getSpectators().contains(player))
            event.setCancelled(true);
    }
}

