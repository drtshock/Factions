package com.massivecraft.factions.cmd;

import com.google.inject.Inject;
import com.google.inject.Singleton;
import com.massivecraft.factions.P;
import com.massivecraft.factions.config.FactionConfig;
import com.massivecraft.factions.struct.Permission;
import com.massivecraft.factions.util.SeeChunkUtil;
import com.massivecraft.factions.zcore.util.TL;

@Singleton
public class CmdSeeChunk extends FCommand {

    @Inject
    private FactionConfig config;

    public CmdSeeChunk() {
        super();
        this.aliases.add("seechunk");
        this.aliases.add("sc");

        this.requirements = new CommandRequirements.Builder(Permission.SEECHUNK)
                .playerOnly()
                .build();
    }

    @Override
    public void perform(CommandContext context) {
        if (config.seeChunk.particles) {
            boolean toggle = false;
            if (context.args.size() == 0) {
                toggle = !context.fPlayer.isSeeingChunk();
            } else if (context.args.size() == 1) {
                toggle = context.argAsBool(0);
            }
            context.fPlayer.setSeeingChunk(toggle);
            context.msg(TL.COMMAND_SEECHUNK_TOGGLE, toggle ? "enabled" : "disabled");
        } else {
            SeeChunkUtil.showPillars(context.player, context.fPlayer, null, false);
        }
    }

    @Override
    public TL getUsageTranslation() {
        return TL.COMMAND_SEECHUNK_DESCRIPTION;
    }

}
