package com.massivecraft.factions.listeners;

import com.massivecraft.factions.*;
import com.massivecraft.factions.struct.ChatMode;
import com.massivecraft.factions.struct.Relation;
import com.massivecraft.factions.struct.Role;
import org.bukkit.Bukkit;
import org.bukkit.ChatColor;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.EventPriority;
import org.bukkit.event.Listener;
import org.bukkit.event.player.AsyncPlayerChatEvent;

import java.util.UnknownFormatConversionException;
import java.util.logging.Level;

public class FactionsChatListener implements Listener {

    public P p;

    public FactionsChatListener(P p) {
        this.p = p;
    }

    // this is for handling slashless command usage and faction/alliance chat, set at lowest priority so Factions gets to them first
    @EventHandler(priority = EventPriority.LOW, ignoreCancelled = true)
    public void onPlayerEarlyChat(AsyncPlayerChatEvent event) {
        Player talkingPlayer = event.getPlayer();
        String msg = event.getMessage();
        FPlayer me = FPlayers.getInstance().getByPlayer(talkingPlayer);
        ChatMode chat = me.getChatMode();
        //Is it a MOD chat
        if (chat == ChatMode.MOD) {
            Faction myFaction = me.getFaction();

            String message = String.format(P.p.conf().factions().chat().getModChatFormat(), ChatColor.stripColor(me.getNameAndTag()), msg);

            //Send to all mods
            for (FPlayer fplayer : FPlayers.getInstance().getOnlinePlayers()) {
                if (myFaction == fplayer.getFaction() && fplayer.getRole().isAtLeast(Role.MODERATOR)) {
                    fplayer.sendMessage(message);
                } else if (fplayer.isSpyingChat() && me != fplayer) {
                    fplayer.sendMessage("[MCspy]: " + message);
                }
            }

            Bukkit.getLogger().log(Level.INFO, ChatColor.stripColor("ModChat " + myFaction.getTag() + ": " + message));

            event.setCancelled(true);
        } else if (chat == ChatMode.FACTION) {
            Faction myFaction = me.getFaction();

            String message = String.format(P.p.conf().factions().chat().getFactionChatFormat(), me.describeTo(myFaction), msg);
            myFaction.sendMessage(message);

            Bukkit.getLogger().log(Level.INFO, ChatColor.stripColor("FactionChat " + myFaction.getTag() + ": " + message));

            //Send to any players who are spying chat
            for (FPlayer fplayer : FPlayers.getInstance().getOnlinePlayers()) {
                if (fplayer.isSpyingChat() && fplayer.getFaction() != myFaction && me != fplayer) {
                    fplayer.sendMessage("[FCspy] " + myFaction.getTag() + ": " + message);
                }
            }

            event.setCancelled(true);
        } else if (chat == ChatMode.ALLIANCE) {
            Faction myFaction = me.getFaction();

            String message = String.format(P.p.conf().factions().chat().getAllianceChatFormat(), ChatColor.stripColor(me.getNameAndTag()), msg);

            //Send message to our own faction
            myFaction.sendMessage(message);

            //Send to all our allies
            for (FPlayer fplayer : FPlayers.getInstance().getOnlinePlayers()) {
                if (myFaction.getRelationTo(fplayer) == Relation.ALLY && !fplayer.isIgnoreAllianceChat()) {
                    fplayer.sendMessage(message);
                } else if (fplayer.isSpyingChat() && me != fplayer) {
                    fplayer.sendMessage("[ACspy]: " + message);
                }
            }

            Bukkit.getLogger().log(Level.INFO, ChatColor.stripColor("AllianceChat: " + message));

            event.setCancelled(true);
        } else if (chat == ChatMode.TRUCE) {
            Faction myFaction = me.getFaction();

            String message = String.format(P.p.conf().factions().chat().getTruceChatFormat(), ChatColor.stripColor(me.getNameAndTag()), msg);

            //Send message to our own faction
            myFaction.sendMessage(message);

            //Send to all our truces
            for (FPlayer fplayer : FPlayers.getInstance().getOnlinePlayers()) {
                if (myFaction.getRelationTo(fplayer) == Relation.TRUCE) {
                    fplayer.sendMessage(message);
                } else if (fplayer.isSpyingChat() && fplayer != me) {
                    fplayer.sendMessage("[TCspy]: " + message);
                }
            }

            Bukkit.getLogger().log(Level.INFO, ChatColor.stripColor("TruceChat: " + message));
            event.setCancelled(true);
        }
    }

    // this is for handling insertion of the player's faction tag, set at highest priority to give other plugins a chance to modify chat first
    @EventHandler(priority = EventPriority.HIGHEST, ignoreCancelled = true)
    public void onPlayerChat(AsyncPlayerChatEvent event) {
        // Are we to insert the Faction tag into the format?
        // If we are not to insert it - we are done.
        if (P.p.isAnotherPluginHandlingChat()) {
            return;
        }

        Player talkingPlayer = event.getPlayer();
        String msg = event.getMessage();
        String eventFormat = event.getFormat();
        FPlayer me = FPlayers.getInstance().getByPlayer(talkingPlayer);
        int InsertIndex = P.p.conf().factions().chat().getTagInsertIndex();

        boolean padBefore = P.p.conf().factions().chat().isTagPadBefore();
        boolean padAfter = P.p.conf().factions().chat().isTagPadAfter();

        if (!P.p.conf().factions().chat().getTagReplaceString().isEmpty() && eventFormat.contains(P.p.conf().factions().chat().getTagReplaceString())) {
            // we're using the "replace" method of inserting the faction tags
            if (eventFormat.contains("[FACTION_TITLE]")) {
                eventFormat = eventFormat.replace("[FACTION_TITLE]", me.getTitle());
            }
            InsertIndex = eventFormat.indexOf(P.p.conf().factions().chat().getTagReplaceString());
            eventFormat = eventFormat.replace(P.p.conf().factions().chat().getTagReplaceString(), "");
            padBefore = false;
            padAfter=false;
        } else if (!P.p.conf().factions().chat().getTagInsertAfterString().isEmpty() && eventFormat.contains(P.p.conf().factions().chat().getTagInsertAfterString())) {
            // we're using the "insert after string" method
            InsertIndex = eventFormat.indexOf(P.p.conf().factions().chat().getTagInsertAfterString()) + P.p.conf().factions().chat().getTagInsertAfterString().length();
        } else if (!P.p.conf().factions().chat().getTagInsertBeforeString().isEmpty() && eventFormat.contains(P.p.conf().factions().chat().getTagInsertBeforeString())) {
            // we're using the "insert before string" method
            InsertIndex = eventFormat.indexOf(P.p.conf().factions().chat().getTagInsertBeforeString());
        } else if (!P.p.conf().factions().chat().isAlwaysShowChatTag()) {
            return;
        }

        String formatStart = eventFormat.substring(0, InsertIndex) + ((padBefore && !me.getChatTag().isEmpty()) ? " " : "");
        String formatEnd = ((padAfter && !me.getChatTag().isEmpty()) ? " " : "") + eventFormat.substring(InsertIndex);

        String nonColoredMsgFormat = formatStart + me.getChatTag().trim() + formatEnd;

        // Relation Colored?
        if (P.p.conf().factions().chat().isTagRelationColored()) {
            for (Player listeningPlayer : event.getRecipients()) {
                FPlayer you = FPlayers.getInstance().getByPlayer(listeningPlayer);
                String yourFormat = formatStart + me.getChatTag(you).trim() + formatEnd;
                try {
                    listeningPlayer.sendMessage(String.format(yourFormat, talkingPlayer.getDisplayName(), msg));
                } catch (UnknownFormatConversionException ex) {

                    P.p.log(Level.SEVERE, "Critical error in chat message formatting!");
                    P.p.log(Level.SEVERE, "NOTE: This can be fixed right now by setting chat tagInsertIndex to 0.");
                    return;
                }
            }

            // Messages are sent to players individually
            // This still leaves a chance for other plugins to pick it up
            event.getRecipients().clear();
        }
        // Message with no relation color.
        event.setFormat(nonColoredMsgFormat);
    }

}
