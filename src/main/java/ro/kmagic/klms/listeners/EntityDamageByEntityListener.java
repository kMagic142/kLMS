package ro.kmagic.klms.listeners;

import org.bukkit.entity.Entity;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.entity.EntityDamageByEntityEvent;
import ro.kmagic.klms.KLMS;

public class EntityDamageByEntityListener implements Listener {
    KLMS instance;

    public EntityDamageByEntityListener(KLMS instance) {
        this.instance = instance;
    }

    @EventHandler
    public void onEntityDamageByEntity(EntityDamageByEntityEvent event) {
        Entity entity = event.getDamager();
        if (!(entity instanceof Player)) {
            return;
        }

        Player player = (Player)entity;
        if (this.instance.getSpectators().contains(player))
            event.setCancelled(true);
    }
}

