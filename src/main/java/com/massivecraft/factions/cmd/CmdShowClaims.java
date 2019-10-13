package com.massivecraft.factions.cmd;

import com.massivecraft.factions.Board;
import com.massivecraft.factions.FLocation;
import com.massivecraft.factions.perms.Role;
import com.massivecraft.factions.struct.Permission;
import com.massivecraft.factions.tag.Tag;
import com.massivecraft.factions.util.TL;

import java.util.ArrayList;
import java.util.Collections;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

public class CmdShowClaims extends FCommand {

    private static Pattern WORLD_PATTERN = Pattern.compile("\\{showclaims_(.*)}");

    public CmdShowClaims() {
        this.aliases.add("showclaims");

        this.requirements = new CommandRequirements.Builder(Permission.SHOWCLAIMS)
                .memberOnly()
                .withRole(Role.MODERATOR)
                .noDisableOnLock()
                .build();
    }

    @Override
    public void perform(CommandContext context) {
        Map<String, List<FLocation>> worldClaims = new HashMap<>();
        for (FLocation claim : Board.getInstance().getAllClaims(context.faction)) {
            String world = claim.getWorldName();
            if (worldClaims.containsKey(world)) {
                worldClaims.get(world).add(claim);
            } else {
                worldClaims.put(world, new ArrayList<>(Collections.singletonList(claim)));
            }
        }

        for (String line : plugin.conf().factions().commands().showClaims().getShow()) {
            Matcher matcher = WORLD_PATTERN.matcher(line);
            if (matcher.find()) {
                String world = matcher.group(1);
                StringBuilder claims = new StringBuilder();
                if (worldClaims.containsKey(world)) {
                    for (FLocation claim : worldClaims.get(world)) {
                        claims.append(parseLocation(claim));
                    }
                }
                line = line.replace(matcher.group(0), claims.toString());
            }
            context.msg(Tag.parsePlain(context.faction, context.fPlayer, line));
        }
    }

    private String parseLocation(FLocation location) {
        String template = plugin.conf().factions().commands().showClaims().getCoordinate();
        return template.replace("{x}", Long.toString(location.getX()))
                .replace("{z}", Long.toString(location.getZ()));
    }

    @Override
    public TL getUsageTranslation() {
        return TL.COMMAND_SHOW_COMMANDDESCRIPTION;
    }

}