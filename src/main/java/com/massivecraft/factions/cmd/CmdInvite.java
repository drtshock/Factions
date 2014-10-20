package com.massivecraft.factions.cmd;

import com.massivecraft.factions.Conf;
import com.massivecraft.factions.FPlayer;
import com.massivecraft.factions.struct.Permission;
import com.massivecraft.factions.zcore.util.TL;

import mkremins.fanciful.FancyMessage;

import org.bukkit.ChatColor;

public class CmdInvite extends FCommand {

    public CmdInvite() {
        super();
        this.aliases.add("invite");
        this.aliases.add("inv");

        this.requiredArgs.add("player name");
        //this.optionalArgs.put("", "");

        this.permission = Permission.INVITE.node;
        this.disableOnLock = true;

        senderMustBePlayer = true;
        senderMustBeMember = false;
        senderMustBeModerator = true;
        senderMustBeAdmin = false;
    }

    @Override
    public void perform() {
        FPlayer you = this.argAsBestFPlayerMatch(0);
        if (you == null) {
            return;
        }

        if (you.getFaction() == myFaction) {
            msg(TL.CMD_INVITE_ALREADY_MEMBER.toString(), you.getName(), myFaction.getTag());
            msg(TL.CMD_INVITE_KICK.toString() + p.cmdBase.cmdKick.getUseageTemplate(false));
            return;
        }

        // if economy is enabled, they're not on the bypass list, and this command has a cost set, make 'em pay
        if (!payForCommand(Conf.econCostInvite, "to invite someone", "for inviting someone")) {
            return;
        }

        myFaction.invite(you);
        if(!you.isOnline()) return;

        // Tooltips, colors, and commands only apply to the string immediately before it.
        FancyMessage message = new FancyMessage(fme.describeTo(you, true))
                                       .tooltip(TL.CMD_INVITE_TOOLTIP.toString()).command("f join " + myFaction.getTag())
                                       .then(TL.CMD_INVITE_HAS.toString()).color(ChatColor.YELLOW)
                                       .tooltip(TL.CMD_INVITE_TOOLTIP.toString()).command("f join " + myFaction.getTag())
                                       .then(myFaction.describeTo(you))
                                       .tooltip(TL.CMD_INVITE_TOOLTIP.toString()).command("f join " + myFaction.getTag());

        message.send(you.getPlayer());

        //you.msg("%s<i> invited you to %s", fme.describeTo(you, true), myFaction.describeTo(you));
        myFaction.msg(TL.CMD_INVITE_INVITED.toString(), fme.describeTo(myFaction, true), you.describeTo(myFaction));
    }

}
