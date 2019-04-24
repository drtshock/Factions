package com.massivecraft.factions.cmd;

import com.massivecraft.factions.Conf;
import com.massivecraft.factions.P;
import com.massivecraft.factions.cmd.claim.*;
import com.massivecraft.factions.cmd.money.CmdMoney;
import com.massivecraft.factions.cmd.relations.CmdRelationAlly;
import com.massivecraft.factions.cmd.relations.CmdRelationEnemy;
import com.massivecraft.factions.cmd.relations.CmdRelationNeutral;
import com.massivecraft.factions.cmd.relations.CmdRelationTruce;
import com.massivecraft.factions.cmd.role.CmdDemote;
import com.massivecraft.factions.cmd.role.CmdPromote;
import com.massivecraft.factions.config.FactionConfig;
import com.massivecraft.factions.zcore.util.TL;
import me.lucko.commodore.CommodoreProvider;
import org.bukkit.Bukkit;
import org.bukkit.command.Command;
import org.bukkit.command.CommandExecutor;
import org.bukkit.command.CommandSender;

import javax.inject.Inject;
import javax.inject.Singleton;
import java.util.*;
import java.util.logging.Level;

@Singleton
public class FCmdRoot extends FCommand implements CommandExecutor {

    @Inject
    private FactionConfig config;

    public BrigadierManager brigadierManager;

    @Inject CmdAdmin cmdAdmin;
    @Inject CmdAutoClaim cmdAutoClaim;
    @Inject CmdBoom cmdBoom;
    @Inject CmdBypass cmdBypass;
    @Inject CmdChat cmdChat;
    @Inject CmdChatSpy cmdChatSpy;
    @Inject CmdClaim cmdClaim;
    @Inject CmdConfig cmdConfig;
    @Inject CmdCreate cmdCreate;
    @Inject CmdDeinvite cmdDeinvite;
    @Inject CmdDescription cmdDescription;
    @Inject CmdDisband cmdDisband;
    @Inject CmdFly cmdFly;
    @Inject public CmdHelp cmdHelp;
    @Inject CmdHome cmdHome;
    @Inject CmdInvite cmdInvite;
    @Inject CmdJoin cmdJoin;
    @Inject CmdKick cmdKick;
    @Inject CmdLeave cmdLeave;
    @Inject CmdList cmdList;
    @Inject CmdLock cmdLock;
    @Inject CmdMap cmdMap;
    @Inject CmdMod cmdMod;
    @Inject CmdMoney cmdMoney;
    @Inject CmdOpen cmdOpen;
    @Inject CmdOwner cmdOwner;
    @Inject CmdOwnerList cmdOwnerList;
    @Inject CmdPeaceful cmdPeaceful;
    @Inject CmdPermanent cmdPermanent;
    @Inject CmdPermanentPower cmdPermanentPower;
    @Inject CmdPowerBoost cmdPowerBoost;
    @Inject CmdPower cmdPower;
    @Inject CmdRelationAlly cmdRelationAlly;
    @Inject CmdRelationEnemy cmdRelationEnemy;
    @Inject CmdRelationNeutral cmdRelationNeutral;
    @Inject CmdRelationTruce cmdRelationTruce;
    @Inject CmdReload cmdReload;
    @Inject CmdSafeunclaimall cmdSafeunclaimall;
    @Inject CmdSaveAll cmdSaveAll;
    @Inject CmdSethome cmdSethome;
    @Inject CmdShow cmdShow;
    @Inject CmdStatus cmdStatus;
    @Inject CmdStuck cmdStuck;
    @Inject CmdTag cmdTag;
    @Inject CmdTitle cmdTitle;
    @Inject CmdToggleAllianceChat cmdToggleAllianceChat;
    @Inject CmdUnclaim cmdUnclaim;
    @Inject CmdUnclaimall cmdUnclaimall;
    @Inject CmdVersion cmdVersion;
    @Inject CmdWarunclaimall cmdWarunclaimall;
    @Inject CmdSB cmdSB;
    @Inject CmdShowInvites cmdShowInvites;
    @Inject CmdAnnounce cmdAnnounce;
    @Inject CmdSeeChunk cmdSeeChunk;
    @Inject CmdConvert cmdConvert;
    @Inject CmdFWarp cmdFWarp;
    @Inject CmdSetFWarp cmdSetFWarp;
    @Inject CmdDelFWarp cmdDelFWarp;
    @Inject CmdModifyPower cmdModifyPower;
    @Inject CmdLogins cmdLogins;
    @Inject CmdClaimLine cmdClaimLine;
    @Inject CmdTop cmdTop;
    @Inject CmdAHome cmdAHome;
    @Inject CmdPerm cmdPerm;
    @Inject CmdPromote cmdPromote;
    @Inject CmdDemote cmdDemote;
    @Inject CmdSetDefaultRole cmdSetDefaultRole;
    @Inject CmdMapHeight cmdMapHeight;
    @Inject CmdClaimAt cmdClaimAt;
    @Inject CmdBan cmdban;
    @Inject CmdUnban cmdUnban;
    @Inject CmdBanlist cmdbanlist;
    @Inject CmdColeader cmdColeader;
    @Inject CmdNear cmdNear;

    @Inject
    public FCmdRoot(P p) {
        super();
        p.injector.injectMembers(this);

        if (CommodoreProvider.isSupported()) {
            brigadierManager = new BrigadierManager();
        }

        this.aliases.addAll(Conf.baseCommandAliases);
        this.aliases.removeAll(Collections.<String>singletonList(null));  // remove any nulls from extra commas

        this.setHelpShort("The faction base command");
        this.helpLong.add(p.txt.parseTags("<i>This command contains all faction stuff."));

        this.addSubCommand(this.cmdAdmin);
        this.addSubCommand(this.cmdAutoClaim);
        this.addSubCommand(this.cmdBoom);
        this.addSubCommand(this.cmdBypass);
        this.addSubCommand(this.cmdChat);
        this.addSubCommand(this.cmdToggleAllianceChat);
        this.addSubCommand(this.cmdChatSpy);
        this.addSubCommand(this.cmdClaim);
        this.addSubCommand(this.cmdConfig);
        this.addSubCommand(this.cmdCreate);
        this.addSubCommand(this.cmdDeinvite);
        this.addSubCommand(this.cmdDescription);
        this.addSubCommand(this.cmdDisband);
        this.addSubCommand(this.cmdHelp);
        this.addSubCommand(this.cmdHome);
        this.addSubCommand(this.cmdInvite);
        this.addSubCommand(this.cmdJoin);
        this.addSubCommand(this.cmdKick);
        this.addSubCommand(this.cmdLeave);
        this.addSubCommand(this.cmdList);
        this.addSubCommand(this.cmdLock);
        this.addSubCommand(this.cmdMap);
        this.addSubCommand(this.cmdMod);
        this.addSubCommand(this.cmdMoney);
        this.addSubCommand(this.cmdOpen);
        this.addSubCommand(this.cmdOwner);
        this.addSubCommand(this.cmdOwnerList);
        this.addSubCommand(this.cmdPeaceful);
        this.addSubCommand(this.cmdPermanent);
        this.addSubCommand(this.cmdPermanentPower);
        this.addSubCommand(this.cmdPower);
        this.addSubCommand(this.cmdPowerBoost);
        this.addSubCommand(this.cmdRelationAlly);
        this.addSubCommand(this.cmdRelationEnemy);
        this.addSubCommand(this.cmdRelationNeutral);
        this.addSubCommand(this.cmdRelationTruce);
        this.addSubCommand(this.cmdReload);
        this.addSubCommand(this.cmdSafeunclaimall);
        this.addSubCommand(this.cmdSaveAll);
        this.addSubCommand(this.cmdSethome);
        this.addSubCommand(this.cmdShow);
        this.addSubCommand(this.cmdStatus);
        this.addSubCommand(this.cmdStuck);
        this.addSubCommand(this.cmdTag);
        this.addSubCommand(this.cmdTitle);
        this.addSubCommand(this.cmdUnclaim);
        this.addSubCommand(this.cmdUnclaimall);
        this.addSubCommand(this.cmdVersion);
        this.addSubCommand(this.cmdWarunclaimall);
        this.addSubCommand(this.cmdSB);
        this.addSubCommand(this.cmdShowInvites);
        this.addSubCommand(this.cmdAnnounce);
        this.addSubCommand(this.cmdSeeChunk);
        this.addSubCommand(this.cmdConvert);
        this.addSubCommand(this.cmdFWarp);
        this.addSubCommand(this.cmdSetFWarp);
        this.addSubCommand(this.cmdDelFWarp);
        this.addSubCommand(this.cmdModifyPower);
        this.addSubCommand(this.cmdLogins);
        this.addSubCommand(this.cmdClaimLine);
        this.addSubCommand(this.cmdAHome);
        this.addSubCommand(this.cmdPerm);
        this.addSubCommand(this.cmdPromote);
        this.addSubCommand(this.cmdDemote);
        this.addSubCommand(this.cmdSetDefaultRole);
        this.addSubCommand(this.cmdMapHeight);
        this.addSubCommand(this.cmdClaimAt);
        this.addSubCommand(this.cmdban);
        this.addSubCommand(this.cmdUnban);
        this.addSubCommand(this.cmdbanlist);
        this.addSubCommand(this.cmdColeader);
        this.addSubCommand(this.cmdNear);
        if (Bukkit.getServer().getPluginManager().isPluginEnabled("FactionsTop")) {
            P.p.log(Level.INFO, "Found FactionsTop plugin. Disabling our own /f top command.");
        } else {
            this.addSubCommand(this.cmdTop);
        }
        if (P.p.isHookedPlayervaults()) {
            P.p.log("Found playervaults hook, adding /f vault and /f setmaxvault commands.");
            this.addSubCommand(new CmdSetMaxVaults());
            this.addSubCommand(new CmdVault());
        }

        if (config.fly.enabled) {
            this.addSubCommand(this.cmdFly);
            P.p.log(Level.INFO, "Enabling /f fly command");
        } else {
            P.p.log(Level.WARNING, "Faction flight set to false in config.yml. Not enabling /f fly command.");
        }

        if (CommodoreProvider.isSupported()) {
            brigadierManager.build();
        }
    }

    @Override
    public void perform(CommandContext context) {
        this.commandChain.add(this);
        this.cmdHelp.execute(context, this.commandChain);
    }

    @Override
    public boolean onCommand(CommandSender sender, Command command, String label, String[] args) {
        this.execute(new CommandContext(sender, new ArrayList<>(Arrays.asList(args)), label), new ArrayList<FCommand>());
        return true;
    }

    @Override
    public void addSubCommand(FCommand subCommand) {
        super.addSubCommand(subCommand);
        if (CommodoreProvider.isSupported()) {
            brigadierManager.addSubCommand(subCommand);
        }
    }

    @Override
    public TL getUsageTranslation() {
        return TL.GENERIC_PLACEHOLDER;
    }

}
