package com.massivecraft.factions.cmd;

import com.massivecraft.factions.struct.Permission;
import com.massivecraft.factions.FPlayer;
import com.massivecraft.factions.util.TL;
import org.bukkit.Location;

public class CmdCoords extends FCommand {

    public CmdCoords() {
        super();
        this.aliases.add("coords");
        this.aliases.add("coord");

        this.requirements = new CommandRequirements.Builder(Permission.COORDS)
                .playerOnly()
                .memberOnly()
                .build();
    }

    @Override
    public void perform(CommandContext context) {
        Location location = context.player.getLocation();
        String message = TL.COMMAND_COORDS_MESSAGE.toString().replace("{player}", context.player.getDisplayName()).replace("{x}", (int) location.getX() + "")
                .replace("{y}", (int) location.getY() + "").replace("{z}", (int) location.getZ() + "").replace("{world}", location.getWorld().getName());
        for (FPlayer fPlayer : context.faction.getFPlayers()) {
            fPlayer.sendMessage(message);
        }
    }

    @Override
    public TL getUsageTranslation() {
        return TL.COMMAND_COORDS_DESCRIPTION;
    }

}
