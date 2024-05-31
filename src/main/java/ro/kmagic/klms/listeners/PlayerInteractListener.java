package ro.kmagic.klms.listeners;

import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.block.Action;
import org.bukkit.event.player.PlayerInteractEvent;
import ro.kmagic.klms.KLMS;
import ro.kmagic.klms.utils.MessageUtils;

public class PlayerInteractListener implements Listener, MessageUtils {
    KLMS instance;

    public PlayerInteractListener(KLMS instance) {
        this.instance = instance;
    }

    @EventHandler
    public void onPlayerInteract(PlayerInteractEvent event) {
        Player player = event.getPlayer();

        if (this.instance.getPlayers().contains(player) && (
                event.getAction().equals(Action.RIGHT_CLICK_BLOCK) || event.getAction().equals(Action.RIGHT_CLICK_BLOCK) || event.getAction().equals(Action.PHYSICAL))) {
            player.sendMessage(formatAll(this.instance.getLanguageManager().getConfig().getString("General.Cant Do Player")));
            event.setCancelled(true);
        }
    }
}

