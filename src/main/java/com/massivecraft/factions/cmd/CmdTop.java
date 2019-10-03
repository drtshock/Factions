package com.massivecraft.factions.cmd;

import com.massivecraft.factions.FPlayer;
import com.massivecraft.factions.Faction;
import com.massivecraft.factions.Factions;
import com.massivecraft.factions.cmd.top.*;
import com.massivecraft.factions.integration.Econ;
import com.massivecraft.factions.struct.Permission;
import com.massivecraft.factions.util.TL;
import org.bukkit.Bukkit;
import org.bukkit.OfflinePlayer;
import org.bukkit.entity.Player;

import java.util.*;
import java.util.function.Function;
import java.util.stream.Collectors;

public class CmdTop extends FCommand {

    private static final Map<String, Function<Faction, FTopValue>> topValueGenerators = new HashMap<>();
    static {
        topValueGenerators.put("members", f -> new FTopGTIntValue(f.getFPlayers().size()));
        topValueGenerators.put("start", f -> new FTopFoundedValue(f.getFoundedDate()));
        topValueGenerators.put("power", f -> new FTopGTIntValue(f.getPowerRounded()));
        topValueGenerators.put("land", f -> new FTopGTIntValue(f.getLandRounded()));
        topValueGenerators.put("online", f -> new FTopGTIntValue(f.getFPlayersWhereOnline(true).size()));
        Function<Faction, FTopValue> money = f -> {
            double monies = Econ.getBalance(f.getAccountId());
            monies += f.getFPlayers().stream()
                    .mapToDouble(fp -> Econ.getBalance(UUID.fromString(fp.getId())).orElse(0.0))
                    .sum();
            return new FTopBalanceValue(monies);
        };

        topValueGenerators.put("money", money);
        topValueGenerators.put("balance", money);
        topValueGenerators.put("bal", money);
    }

    public CmdTop() {
        super();
        this.aliases.add("top");
        this.aliases.add("t");

        this.requiredArgs.add("criteria");
        this.optionalArgs.put("page", "1");

        this.requirements = new CommandRequirements.Builder(Permission.TOP).noDisableOnLock().build();
    }

    @Override
    public void perform(CommandContext context) {
        // Can sort by: money, members, online, allies, enemies, power, land.
        // Get all Factions and remove non player ones.

        String criteria = context.argAsString(0);

        Function<Faction, FTopValue> ftopGenerator = topValueGenerators.get(criteria);
        if (ftopGenerator == null) {
            context.msg(TL.COMMAND_TOP_INVALID, criteria);
            return;
        }

        List<FTopFacValPair> sortedFactions = Factions.getInstance().getAllFactions().stream()
                .filter(Faction::isNormal)
                .map(f -> new FTopFacValPair(f, ftopGenerator.apply(f)))
                .sorted()
                .collect(Collectors.toList());

        int sortedFactionsCount = sortedFactions.size();

        final int pageheight = 9;
        int pagenumber = context.argAsInt(1, 1);
        int pagecount = (sortedFactionsCount / pageheight) + 1;
        if (pagenumber > pagecount) {
            pagenumber = pagecount;
        } else if (pagenumber < 1) {
            pagenumber = 1;
        }
        int start = (pagenumber - 1) * pageheight;
        int end = start + pageheight;
        if (end > sortedFactionsCount) {
            end = sortedFactionsCount;
        }

        // One line for the header, end - start lines for the factions
        List<String> lines = new ArrayList<>(1 + end - start);

        lines.add(TL.COMMAND_TOP_TOP.format(criteria.toUpperCase(), pagenumber, pagecount));

        int rank = start + 1;
        for (FTopFacValPair fvpair : sortedFactions.subList(start, end)) {
            // Get the relation color if player is executing this.
            Faction faction = fvpair.faction;
            String fac = context.sender instanceof Player ? faction.getRelationTo(context.fPlayer).getColor() + faction.getTag() : faction.getTag();
            lines.add(TL.COMMAND_TOP_LINE.format(rank, fac, fvpair.value.getDisplayString()));
            rank++;
        }

        context.sendMessage(lines);
    }

    @Override
    public TL getUsageTranslation() {
        return TL.COMMAND_TOP_DESCRIPTION;
    }
}
