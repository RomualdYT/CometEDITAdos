package com.cometproject.server.network.messages.incoming.user.club;

import com.cometproject.server.config.Locale;
import com.cometproject.server.network.messages.incoming.Event;
import com.cometproject.server.network.messages.outgoing.user.club.ClubStatusMessageComposer;
import com.cometproject.server.protocol.messages.MessageEvent;
import com.cometproject.server.network.messages.outgoing.notification.MotdNotificationMessageComposer;
import com.cometproject.server.network.sessions.Session;
/**
 * Created by RomualdPortable on 22/05/2016.
 */
public class GetHabboClubWindowMessageEvent implements Event {
    public void handle(Session client, MessageEvent msg) {
        client.send(new MotdNotificationMessageComposer(Locale.get("Ados Dreams") + "\n================================================\n" + "Sur Ados Dreams, le AdosClub est gratuit !"));
    }
}