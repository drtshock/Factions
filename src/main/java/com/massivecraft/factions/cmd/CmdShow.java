package com.massivecraft.factions.cmd;

import com.massivecraft.factions.*;
import com.massivecraft.factions.integration.Econ;
import com.massivecraft.factions.struct.Permission;
import com.massivecraft.factions.struct.Relation;
import com.massivecraft.factions.struct.Role;
import com.massivecraft.factions.zcore.util.TL;

import java.util.Collection;

public class CmdShow extends FCommand {

    public CmdShow() {
        this.aliases.add("show");
        this.aliases.add("who");

        this.optionalArgs.put("faction tag", "yours");

        this.permission = Permission.SHOW.node;
        this.disableOnLock = false;

        this.senderMustBePlayer = true;
        this.senderMustBeMember = false;
        this.senderMustBeModerator = false;
        this.senderMustBeAdmin = false;
    }

    public void perform() {
        Faction faction = this.myFaction;
        if (argIsSet(0)) {
            faction = argAsFaction(0);
            if (faction == null || faction.isNone()) {
                msg(TL.COMMAND_SHOW_NOFACTION_OTHER);
                return;
            }

        }

        if (faction.isNone()) {
            msg(TL.COMMAND_SHOW_NOFACTION_SELF);
            return;
        }

        if (!payForCommand(Conf.econCostShow, TL.COMMAND_SHOW_TOSHOW, TL.COMMAND_SHOW_FORSHOW)) {
            return;
        }

        Collection<FPlayer> admins = faction.getFPlayersWhereRole(Role.ADMIN);
        Collection<FPlayer> mods = faction.getFPlayersWhereRole(Role.MODERATOR);
        Collection<FPlayer> normals = faction.getFPlayersWhereRole(Role.NORMAL);

        msg((p).txt.titleize(faction.getTag(this.fme)));
        msg(TL.COMMAND_SHOW_DESCRIPTION, faction.getDescription());
        if (!faction.isNormal()) {
            return;
        }

        String peaceStatus = "";
        if (faction.isPeaceful()) {
            peaceStatus = "     " + Conf.colorNeutral + TL.COMMAND_SHOW_PEACEFUL.toString();
        }

        msg(TL.COMMAND_SHOW_JOINING.toString() + peaceStatus, (faction.getOpen() ? TL.COMMAND_SHOW_UNINVITED.toString() : TL.COMMAND_SHOW_INVITATION.toString()));

        double powerBoost = faction.getPowerBoost();
        String boost = (powerBoost == 0) ? "" : (powerBoost > 0.0D ? TL.COMMAND_SHOW_BONUS.toString() : TL.COMMAND_SHOW_PENALTY.toStrin()) + powerBoost + ")";
         msg(TL.COMMAND_SHOW_POWER, faction.getLandRounded(), faction.getPowerRounded(), faction.getPowerMaxRounded(), boost);

        if (faction.isPermanent()) {
            msg(TL.COMMAND_SHOW_PERMANENT);
        }

        if (Econ.shouldBeUsed()) {
            double value = Econ.calculateTotalLandValue(faction.getLandRounded());
            double refund = value * Conf.econClaimRefundMultiplier;
            if (value > 0.0D) {
                String stringValue = Econ.moneyString(value);
                String stringRefund = (refund > 0.0) ? (TL.COMMAND_SHOW_DEPRECIATED.format(Econ.moneyString(refund))) : "";
                msg(TL.COMMAND_SHOW_LANDVALUE, stringValue, stringRefund);
            }

            if (Conf.bankEnabled) {
                msg(TL.COMMAND_SHOW_BANKCONTAINS, Econ.moneyString(Econ.getBalance(faction.getAccountId())));
            }

        }

        String allyList = p.txt.parse("<a>" + TL.COMMAND_SHOW_ALLIES);
        String enemyList = p.txt.parse("<a>" + TL.COMMAND_SHOW_ENEMIES);
        for (Faction otherFaction : Factions.getInstance().getAllFactions()) {
            if (otherFaction != faction) {
                Relation rel = otherFaction.getRelationTo(faction);
                if ((rel.isAlly()) || (rel.isEnemy())) {
                    String listpart = otherFaction.getTag(this.fme) + p.txt.parse("<i>") + ", ";
                    if (rel.isAlly()) {
                        allyList = allyList + listpart;
                    } else if (rel.isEnemy()) {
                        enemyList = enemyList + listpart;
                    }
                }
            }
        }

        if (allyList.endsWith(", ")) {
            allyList = allyList.substring(0, allyList.length() - 2);
        }
        if (enemyList.endsWith(", ")) {
            enemyList = enemyList.substring(0, enemyList.length() - 2);
        }
        sendMessage(allyList);
        sendMessage(enemyList);

        String onlineList = TL.COMMAND_SHOW_MEMBERSONLINE;
        String offlineList = TL.COMMAND_SHOW_MEMBERSOFFLINE;
        for (FPlayer follower : admins) {
            String listpart = follower.getNameAndTitle(this.fme) + p.txt.parse("<i>") + ", ";
            if (follower.isOnlineAndVisibleTo(this.me)) {
                onlineList = onlineList + listpart;
            } else {
                offlineList = offlineList + listpart;
            }
        }
        for (FPlayer follower : mods) {
            String listpart = follower.getNameAndTitle(this.fme) + p.txt.parse("<i>") + ", ";

            if (follower.isOnlineAndVisibleTo(this.me)) {
                onlineList = onlineList + listpart;
            } else {
                offlineList = offlineList + listpart;
            }
        }

        for (FPlayer follower : normals) {
            String listpart = follower.getNameAndTitle(this.fme) + p.txt.parse("<i>") + ", ";
            if (follower.isOnlineAndVisibleTo(this.me)) {
                onlineList = onlineList + listpart;
            } else {
                offlineList = offlineList + listpart;
            }
        }

        if (onlineList.endsWith(", ")) {
            onlineList = onlineList.substring(0, onlineList.length() - 2);
        }
        if (offlineList.endsWith(", ")) {
            offlineList = offlineList.substring(0, offlineList.length() - 2);
        }

        sendMessage(onlineList);
        sendMessage(offlineList);
    }
}
