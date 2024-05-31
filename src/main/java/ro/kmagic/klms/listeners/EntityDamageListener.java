package ro.kmagic.klms.listeners;

import org.bukkit.entity.Entity;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.EventPriority;
import org.bukkit.event.Listener;
import org.bukkit.event.entity.EntityDamageEvent;
import ro.kmagic.klms.KLMS;
import ro.kmagic.klms.game.GameStatus;

public class EntityDamageListener implements Listener {
    KLMS instance;

    public EntityDamageListener(KLMS instance) {
        this.instance = instance;
    }

    @EventHandler(priority = EventPriority.LOWEST, ignoreCancelled = true)
    public void onEntityDamage(EntityDamageEvent event) {
        Entity entity = event.getEntity();
        if (!(entity instanceof Player)) {
            return;
        }

        GameStatus status = this.instance.getGameManager().getStatus();
        Player player = (Player)entity;

        if (this.instance.getSpectators().contains(player)) {
            event.setCancelled(true);

            return;
        }
        if (this.instance.getPlayers().contains(player) && (
                status.equals(GameStatus.WAITING) || status.equals(GameStatus.STARTING) || status.equals(GameStatus.GRACE))) {
            event.setCancelled(true);
            return;
        }
    }
}

