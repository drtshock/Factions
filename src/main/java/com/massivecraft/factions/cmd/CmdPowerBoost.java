package com.massivecraft.factions.cmd;

import com.massivecraft.factions.FPlayer;
import com.massivecraft.factions.Faction;
import com.massivecraft.factions.FactionsPlugin;
import com.massivecraft.factions.struct.Permission;
import com.massivecraft.factions.util.TL;
import com.mojang.brigadier.arguments.IntegerArgumentType;
import com.mojang.brigadier.arguments.StringArgumentType;
import com.mojang.brigadier.builder.ArgumentBuilder;
import com.mojang.brigadier.builder.LiteralArgumentBuilder;
import com.mojang.brigadier.builder.RequiredArgumentBuilder;
import com.mojang.brigadier.tree.ArgumentCommandNode;
import com.mojang.brigadier.tree.LiteralCommandNode;

public class CmdPowerBoost extends FCommand {

    public CmdPowerBoost() {
        super();
        this.aliases.add("powerboost");

        this.requiredArgs.add("p/f/player/faction | set/add");
        this.requiredArgs.add("name | p/f/player/faction");
        this.requiredArgs.add("#/reset | name");
        this.optionalArgs.put("#/reset", "");

        this.requirements = new CommandRequirements.Builder(Permission.POWERBOOST)
                .brigadier(PowerBoostBrigadier.class)
                .build();
    }

    @Override
    public void perform(CommandContext context) {
        boolean legacy = context.args.size() == 3;
        String subcommand = "add";
        if (!legacy) {
            subcommand = context.argAsString(0).toLowerCase();
            if (!subcommand.equals("set") && !subcommand.equals("add")) {
                context.msg(TL.COMMAND_POWERBOOST_UNKNOWN_SUBCOMMAND, subcommand);
                return;
            }
        }

        // this offset is in case add/set is being used, args must be shifted to accommodate
        int offset = legacy ? 0 : 1;

        String type = context.argAsString(offset).toLowerCase();
        boolean doPlayer = true;
        if (type.equals("f") || type.equals("faction")) {
            doPlayer = false;
        } else if (!type.equals("p") && !type.equals("player")) {
            context.msg(TL.COMMAND_POWERBOOST_HELP_1);
            context.msg(TL.COMMAND_POWERBOOST_HELP_2);
            return;
        }

        Double targetPower = context.argAsDouble(2 + offset);
        if (targetPower == null) {
            if (context.argAsString(2 + offset).equalsIgnoreCase("reset")) {
                targetPower = 0D;
            } else {
                context.msg(TL.COMMAND_POWERBOOST_INVALIDNUM);
                return;
            }
        }

        String target;

        if (doPlayer) {
            FPlayer targetPlayer = context.argAsBestFPlayerMatch(1 + offset);
            if (targetPlayer == null) {
                return;
            }

            if (subcommand.equals("add") && targetPower != 0) {
                targetPower += targetPlayer.getPowerBoost();
            }
            targetPlayer.setPowerBoost(targetPower);
            target = TL.COMMAND_POWERBOOST_PLAYER.format(targetPlayer.getName());
        } else {
            Faction targetFaction = context.argAsFaction(1 + offset);
            if (targetFaction == null) {
                return;
            }

            if (subcommand.equals("add") && targetPower != 0) {
                targetPower += targetFaction.getPowerBoost();
            }
            targetFaction.setPowerBoost(targetPower);
            target = TL.COMMAND_POWERBOOST_FACTION.format(targetFaction.getTag());
        }

        int roundedPower = (int) Math.round(targetPower);
        context.msg(TL.COMMAND_POWERBOOST_BOOST, target, roundedPower);
        if (context.player != null) {
            FactionsPlugin.getInstance().log(TL.COMMAND_POWERBOOST_BOOSTLOG.toString(), context.fPlayer.getName(), target, roundedPower);
        }
    }

    protected class PowerBoostBrigadier implements BrigadierProvider {
        @Override
        public ArgumentBuilder<Object, ?> get(ArgumentBuilder<Object, ?> parent) {
            ArgumentCommandNode<Object, ?> generic = RequiredArgumentBuilder.argument("p/f/player/faction", StringArgumentType.word())
                    .then(RequiredArgumentBuilder.argument("name", StringArgumentType.word())
                    .then(RequiredArgumentBuilder.argument("amount", IntegerArgumentType.integer()))).build();

            return parent.then(generic)
                    .then(LiteralArgumentBuilder.literal("set").then(generic))
                    .then(LiteralArgumentBuilder.literal("add").then(generic));
        }
    }

    @Override
    public TL getUsageTranslation() {
        return TL.COMMAND_POWERBOOST_DESCRIPTION;
    }
}
