package com.massivecraft.factions.cmd;

import com.google.inject.Singleton;
import com.massivecraft.factions.P;
import com.massivecraft.factions.zcore.CommandVisibility;
import com.massivecraft.factions.zcore.util.TL;

import java.util.ArrayList;

@Singleton
public class CmdAutoHelp extends FCommand {

    public CmdAutoHelp() {
        this.aliases.add("?");
        this.aliases.add("h");
        this.aliases.add("help");

        this.setHelpShort("");

        this.optionalArgs.put("page", "1");
    }

    @Override
    public void perform(CommandContext context) {
        if (this.commandChain.size() == 0) {
            return;
        }
        FCommand pcmd = this.commandChain.get(this.commandChain.size() - 1);

        ArrayList<String> lines = new ArrayList<>();

        lines.addAll(pcmd.helpLong);

        for (FCommand scmd : pcmd.subCommands) {
            if (scmd.visibility == CommandVisibility.VISIBLE) {
                lines.add(scmd.getUseageTemplate(this.commandChain, true));
            }
        }

        context.sendMessage(P.p.txt.getPage(lines, context.argAsInt(0, 1), TL.COMMAND_AUTOHELP_HELPFOR.toString() + pcmd.aliases.get(0) + "\""));
    }

    @Override
    public TL getUsageTranslation() {
        return TL.COMMAND_HELP_DESCRIPTION;
    }
}
