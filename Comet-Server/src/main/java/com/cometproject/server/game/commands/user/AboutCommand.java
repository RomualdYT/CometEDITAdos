package com.cometproject.server.game.commands.user;

import com.cometproject.server.boot.Comet;
import com.cometproject.server.config.CometSettings;
import com.cometproject.server.config.Locale;
import com.cometproject.server.game.GameCycle;
import com.cometproject.server.game.commands.ChatCommand;
import com.cometproject.server.network.messages.outgoing.notification.AdvancedAlertMessageComposer;
import com.cometproject.server.network.sessions.Session;
import com.cometproject.server.utilities.CometStats;

import java.text.NumberFormat;


public class AboutCommand extends ChatCommand {

    @Override
    public void execute(Session client, String message[]) {
        StringBuilder about = new StringBuilder();
        NumberFormat format = NumberFormat.getInstance();

        CometStats cometStats = CometStats.get();

        boolean aboutDetailed = client.getPlayer().getPermissions().getRank().aboutDetailed();
        boolean aboutStats = client.getPlayer().getPermissions().getRank().aboutStats();

        if (CometSettings.aboutShowRoomsActive || CometSettings.aboutShowRoomsActive || CometSettings.aboutShowUptime || aboutDetailed) {
            about.append("<b>Status de Ados Dreams</b><br>Basé sur Comet, merci aux développeurs de Comet<br>");

            if (CometSettings.aboutShowPlayersOnline || aboutDetailed)
                about.append("Joueurs en ligne: " + format.format(cometStats.getPlayers()) + "<br>");

            if (CometSettings.aboutShowRoomsActive || aboutDetailed)
                about.append("Apparts ouverts: " + format.format(cometStats.getRooms()) + "<br>");

            if (CometSettings.aboutShowUptime || aboutDetailed)
                about.append("Emulateur en marche depuis : " + cometStats.getUptime() + "<br>");

            about.append("Client version: " + Session.CLIENT_VERSION + "<br>");
        }

        // This will be visible to developers on the manager, no need to display it to the end-user.
        /*if (client.getPlayer().getPermissions().hasPermission("about_detailed")) {
            about.append("<br><b>Server Info</b><br>");
            about.append("Allocated memory: " + format.format(cometStats.getAllocatedMemory()) + "MB<br>");
            about.append("Used memory: " + format.format(cometStats.getUsedMemory()) + "MB<br>");

            about.append("Process ID: " + CometRuntime.processId + "<br>");
            about.append("OS: " + cometStats.getOperatingSystem() + "<br>");
            about.append("CPU cores:  " + cometStats.getCpuCores() + "<br>");
            about.append("Threads:  " + ManagementFactory.getThreadMXBean().getThreadCount() + "<br>");
        }*/

        if (aboutStats) {
            about.append("<br><br><b>Hotel Stats</b><br>");
            about.append("Online record: " + GameCycle.getInstance().getOnlineRecord() + "<br>");
            about.append("Record since last reboot: " + GameCycle.getInstance().getCurrentOnlineRecord() + " ");
        }

        client.send(new AdvancedAlertMessageComposer(
                "Dreams Emu - " + Comet.getBuild(),
                about.toString(), "doublecoins_promo"
        ));
    }

    @Override
    public String getPermission() {
        return "about_command";
    }

    @Override
    public String getDescription() {
        return Locale.get("command.about.description");
    }
}
