package com.massivecraft.factions.cmd;

import com.massivecraft.factions.FactionsPlugin;
import com.massivecraft.factions.cmd.claim.CmdAutoClaim;
import com.massivecraft.factions.cmd.claim.CmdClaim;
import com.massivecraft.factions.cmd.claim.CmdClaimAt;
import com.massivecraft.factions.cmd.claim.CmdClaimFill;
import com.massivecraft.factions.cmd.claim.CmdClaimLine;
import com.massivecraft.factions.cmd.claim.CmdSafeunclaimall;
import com.massivecraft.factions.cmd.claim.CmdUnclaim;
import com.massivecraft.factions.cmd.claim.CmdUnclaimall;
import com.massivecraft.factions.cmd.claim.CmdWarunclaimall;
import com.massivecraft.factions.cmd.money.CmdMoney;
import com.massivecraft.factions.cmd.relations.CmdRelationAlly;
import com.massivecraft.factions.cmd.relations.CmdRelationEnemy;
import com.massivecraft.factions.cmd.relations.CmdRelationNeutral;
import com.massivecraft.factions.cmd.relations.CmdRelationTruce;
import com.massivecraft.factions.cmd.role.CmdDemote;
import com.massivecraft.factions.cmd.role.CmdPromote;
import com.massivecraft.factions.landraidcontrol.DTRControl;
import com.massivecraft.factions.landraidcontrol.PowerControl;
import com.massivecraft.factions.util.TL;
import me.lucko.commodore.CommodoreProvider;
import org.bukkit.Bukkit;
import org.bukkit.command.Command;
import org.bukkit.command.CommandExecutor;
import org.bukkit.command.CommandSender;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.Collections;

public class FCmdRoot extends FCommand implements CommandExecutor {

    private static FCmdRoot cmdBase;

    public static FCmdRoot getInstance() {
        return cmdBase;
    }

    public CmdAutoHelp cmdAutoHelp = new CmdAutoHelp();

    public BrigadierManager brigadierManager;

    public CmdAdmin cmdAdmin = new CmdAdmin();
    public CmdAutoClaim cmdAutoClaim = new CmdAutoClaim();
    public CmdBoom cmdBoom = new CmdBoom();
    public CmdBypass cmdBypass = new CmdBypass();
    public CmdChat cmdChat = new CmdChat();
    public CmdChatSpy cmdChatSpy = new CmdChatSpy();
    public CmdClaim cmdClaim = new CmdClaim();
    public CmdCoords cmdCoords = new CmdCoords();
    public CmdCreate cmdCreate = new CmdCreate();
    public CmdDeinvite cmdDeinvite = new CmdDeinvite();
    public CmdDescription cmdDescription = new CmdDescription();
    public CmdDisband cmdDisband = new CmdDisband();
    public CmdFly cmdFly = new CmdFly();
    public CmdHelp cmdHelp = new CmdHelp();
    public CmdHome cmdHome = new CmdHome();
    public CmdInvite cmdInvite = new CmdInvite();
    public CmdJoin cmdJoin = new CmdJoin();
    public CmdKick cmdKick = new CmdKick();
    public CmdLeave cmdLeave = new CmdLeave();
    public CmdList cmdList = new CmdList();
    public CmdLock cmdLock = new CmdLock();
    public CmdMap cmdMap = new CmdMap();
    public CmdMod cmdMod = new CmdMod();
    public CmdMoney cmdMoney = new CmdMoney();
    public CmdOpen cmdOpen = new CmdOpen();
    public CmdOwner cmdOwner = new CmdOwner();
    public CmdOwnerList cmdOwnerList = new CmdOwnerList();
    public CmdPeaceful cmdPeaceful = new CmdPeaceful();
    public CmdPermanent cmdPermanent = new CmdPermanent();
    public CmdPermanentPower cmdPermanentPower = new CmdPermanentPower();
    public CmdPowerBoost cmdPowerBoost = new CmdPowerBoost();
    public CmdPower cmdPower = new CmdPower();
    public CmdDTR cmdDTR = new CmdDTR();
    public CmdRelationAlly cmdRelationAlly = new CmdRelationAlly();
    public CmdRelationEnemy cmdRelationEnemy = new CmdRelationEnemy();
    public CmdRelationNeutral cmdRelationNeutral = new CmdRelationNeutral();
    public CmdRelationTruce cmdRelationTruce = new CmdRelationTruce();
    public CmdReload cmdReload = new CmdReload();
    public CmdSafeunclaimall cmdSafeunclaimall = new CmdSafeunclaimall();
    public CmdSaveAll cmdSaveAll = new CmdSaveAll();
    public CmdSethome cmdSethome = new CmdSethome();
    public CmdDelhome cmdDelhome = new CmdDelhome();
    public CmdShow cmdShow = new CmdShow();
    public CmdStatus cmdStatus = new CmdStatus();
    public CmdStuck cmdStuck = new CmdStuck();
    public CmdTag cmdTag = new CmdTag();
    public CmdTitle cmdTitle = new CmdTitle();
    public CmdToggleAllianceChat cmdToggleAllianceChat = new CmdToggleAllianceChat();
    public CmdUnclaim cmdUnclaim = new CmdUnclaim();
    public CmdUnclaimall cmdUnclaimall = new CmdUnclaimall();
    public CmdVersion cmdVersion = new CmdVersion();
    public CmdWarunclaimall cmdWarunclaimall = new CmdWarunclaimall();
    public CmdSB cmdSB = new CmdSB();
    public CmdShowInvites cmdShowInvites = new CmdShowInvites();
    public CmdAnnounce cmdAnnounce = new CmdAnnounce();
    public CmdSeeChunk cmdSeeChunk = new CmdSeeChunk();
    public CmdWarp cmdWarp = new CmdWarp();
    public CmdWarpOther cmdWarpOther = new CmdWarpOther();
    public CmdSetWarp cmdSetWarp = new CmdSetWarp();
    public CmdDelWarp cmdDelWarp = new CmdDelWarp();
    public CmdModifyPower cmdModifyPower = new CmdModifyPower();
    public CmdLogins cmdLogins = new CmdLogins();
    public CmdClaimLine cmdClaimLine = new CmdClaimLine();
    public CmdClaimFill cmdClaimFill = new CmdClaimFill();
    public CmdTop cmdTop = new CmdTop();
    public CmdAHome cmdAHome = new CmdAHome();
    public CmdPerm cmdPerm = new CmdPerm();
    public CmdPromote cmdPromote = new CmdPromote();
    public CmdDemote cmdDemote = new CmdDemote();
    public CmdSetDefaultRole cmdSetDefaultRole = new CmdSetDefaultRole();
    public CmdMapHeight cmdMapHeight = new CmdMapHeight();
    public CmdClaimAt cmdClaimAt = new CmdClaimAt();
    public CmdBan cmdban = new CmdBan();
    public CmdUnban cmdUnban = new CmdUnban();
    public CmdBanlist cmdbanlist = new CmdBanlist();
    public CmdColeader cmdColeader = new CmdColeader();
    public CmdNear cmdNear = new CmdNear();
    public CmdTrail cmdTrail = new CmdTrail();
    public CmdDebug cmdDebug = new CmdDebug();
    public CmdTNT cmdTNT = new CmdTNT();

    public FCmdRoot() {
        super();

        cmdBase = this;

        if (CommodoreProvider.isSupported()) {
            brigadierManager = new BrigadierManager();
        }

        this.aliases.addAll(FactionsPlugin.getInstance().conf().getCommandBase());
        this.aliases.removeAll(Collections.<String>singletonList(null));  // remove any nulls from extra commas

        this.setHelpShort("The faction base command");
        this.helpLong.add(FactionsPlugin.getInstance().txt().parseTags("<i>This command contains all faction stuff."));

        this.addSubCommand(this.cmdAdmin);
        this.addSubCommand(this.cmdAutoClaim);
        this.addSubCommand(this.cmdBoom);
        this.addSubCommand(this.cmdBypass);
        this.addSubCommand(this.cmdChat);
        this.addSubCommand(this.cmdToggleAllianceChat);
        this.addSubCommand(this.cmdChatSpy);
        this.addSubCommand(this.cmdClaim);
        this.addSubCommand(this.cmdCoords);
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
        this.addSubCommand(this.cmdRelationAlly);
        this.addSubCommand(this.cmdRelationEnemy);
        this.addSubCommand(this.cmdRelationNeutral);
        this.addSubCommand(this.cmdRelationTruce);
        this.addSubCommand(this.cmdReload);
        this.addSubCommand(this.cmdSafeunclaimall);
        this.addSubCommand(this.cmdSaveAll);
        this.addSubCommand(this.cmdSethome);
        this.addSubCommand(this.cmdDelhome);
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
        this.addSubCommand(this.cmdWarp);
        this.addSubCommand(this.cmdWarpOther);
        this.addSubCommand(this.cmdSetWarp);
        this.addSubCommand(this.cmdDelWarp);
        this.addSubCommand(this.cmdLogins);
        this.addSubCommand(this.cmdClaimLine);
        this.addSubCommand(this.cmdClaimFill);
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
        this.addSubCommand(this.cmdDebug);
        if (FactionsPlugin.getInstance().getLandRaidControl() instanceof PowerControl) {
            FactionsPlugin.getInstance().getLogger().info("Using POWER for land/raid control. Enabling power commands.");
            this.addSubCommand(this.cmdPermanentPower);
            this.addSubCommand(this.cmdPower);
            this.addSubCommand(this.cmdPowerBoost);
            this.addSubCommand(this.cmdModifyPower);
        } else if (FactionsPlugin.getInstance().getLandRaidControl() instanceof DTRControl) {
            FactionsPlugin.getInstance().getLogger().info("Using DTR for land/raid control. Enabling DTR commands.");
            this.addSubCommand(this.cmdDTR);
        }
        if (FactionsPlugin.getInstance().conf().commands().tnt().isEnable()) {
            this.addSubCommand(this.cmdTNT);
            FactionsPlugin.getInstance().getLogger().info("Enabling TNT bank management");
        }
        if (Bukkit.getServer().getPluginManager().isPluginEnabled("FactionsTop")) {
            FactionsPlugin.getInstance().getLogger().info("Found FactionsTop plugin. Disabling our own /f top command.");
        } else {
            this.addSubCommand(this.cmdTop);
        }
        if (FactionsPlugin.getInstance().isHookedPlayervaults()) {
            FactionsPlugin.getInstance().getLogger().info("Found PlayerVaults hook, adding /f vault and /f setmaxvault commands.");
            this.addSubCommand(new CmdSetMaxVaults());
            this.addSubCommand(new CmdVault());
        }

        if (FactionsPlugin.getInstance().conf().commands().fly().isEnable()) {
            this.addSubCommand(this.cmdFly);
            this.addSubCommand(this.cmdTrail);
            FactionsPlugin.getInstance().getLogger().info("Enabling /f fly command");
        } else {
            FactionsPlugin.getInstance().getLogger().info("Faction flight set to false in main.conf. Not enabling /f fly command.");
        }

        if (CommodoreProvider.isSupported()) {
            brigadierManager.build();
        }
    }

    @Override
    public void perform(CommandContext context) {
        context.commandChain.add(this);
        this.cmdHelp.execute(context);
    }

    @Override
    public boolean onCommand(CommandSender sender, Command command, String label, String[] args) {
        if (!plugin.worldUtil().isEnabled(sender)) {
            sender.sendMessage(TL.GENERIC_DISABLEDWORLD.toString());
            return false;
        }

        this.execute(new CommandContext(sender, new ArrayList<>(Arrays.asList(args)), label));
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
