package com.massivecraft.factions.cmd;

import com.darkblade12.particleeffect.ParticleEffect;
import com.massivecraft.factions.struct.Permission;
import com.massivecraft.factions.zcore.util.TL;

public class CmdTrail extends FCommand {

    public CmdTrail() {
        super();
        this.aliases.add("trail");
        this.aliases.add("flytrail");

        this.optionalArgs.put("on/off/effect", "flip");
        this.optionalArgs.put("particle", "particle");

        this.permission = Permission.FLY_TRAILS.node;
        this.senderMustBeMember = true;
        this.senderMustBeModerator = false;
    }

    @Override
    public void perform() {
        // Let's flip it
        if (!argIsSet(0)) {
            fme.setFlyTrailsState(!fme.getFlyTrailsState());
        } else {
            // They are setting a particle type
            if (argAsString(0).equalsIgnoreCase("effect")) {
                if (argIsSet(1)) {
                    String effectName = argAsString(1);
                    ParticleEffect particleEffect = ParticleEffect.fromName(effectName);
                    if (particleEffect == null) {
                        fme.msg(TL.COMMAND_FLYTRAILS_PARTICLE_INVALID);
                        return;
                    }

                    if (p.perm.has(me, Permission.FLY_TRAILS.node + "." + particleEffect.getName())) {
                        fme.setFlyTrailsEffect(particleEffect);
                    } else {
                        fme.msg(TL.COMMAND_FLYTRAILS_PARTICLE_PERMS, particleEffect.getName());
                    }
                } else {
                    fme.msg(TL.COMMAND_FLYTRAILS_PARTICLE_INVALID);
                }
            } else {
                boolean state = argAsBool(0);
                fme.setFlyTrailsState(state);
            }
        }
    }

    @Override
    public TL getUsageTranslation() {
        return TL.COMMAND_FLY_DESCRIPTION;
    }

}