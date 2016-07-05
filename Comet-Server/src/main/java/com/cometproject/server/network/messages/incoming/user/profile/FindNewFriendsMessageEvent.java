package com.cometproject.server.network.messages.incoming.user.profile;

import com.cometproject.server.config.Locale;
import com.cometproject.server.network.messages.incoming.Event;
import com.cometproject.server.network.messages.outgoing.notification.NotificationMessageComposer;
import com.cometproject.server.protocol.messages.MessageEvent;
import com.cometproject.server.network.sessions.Session;


/**
 * Created by RomualdPortable on 23/05/2016.
 */
public class FindNewFriendsMessageEvent implements Event {
    @Override
    public void handle(Session client, MessageEvent msg) throws Exception {
        client.send(new NotificationMessageComposer("furni_placement_error", Locale.getOrDefault("Information", "Fonction pas encore implantée !")));

    }
}
