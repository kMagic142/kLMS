package ro.kmagic.klms.language;

import me.clip.placeholderapi.expansion.PlaceholderExpansion;
import org.bukkit.entity.Player;
import ro.kmagic.klms.KLMS;

public class PlaceholderAPI extends PlaceholderExpansion {

    private final KLMS plugin;

    public PlaceholderAPI(KLMS plugin) {
        this.plugin = plugin;
    }
    @Override
    public String getAuthor() {
        return "kMagic";
    }

    @Override
    public String getIdentifier() {
        return "klms";
    }

    @Override
    public String getVersion() {
        return "1.0.0";
    }

    @Override
    public boolean persist() {
        return true;
    }

    @Override
    public String onPlaceholderRequest(Player player, String params) {
        if(params.equalsIgnoreCase("time_until_start")){
            return String.valueOf(plugin.getGameManager().getTimer());
        }

        return null;
    }
}
