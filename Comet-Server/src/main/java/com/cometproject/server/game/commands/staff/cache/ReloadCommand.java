package com.cometproject.server.game.commands.staff.cache;

import com.cometproject.api.networking.sessions.BaseSession;
import com.cometproject.server.config.CometSettings;
import com.cometproject.server.config.Locale;
import com.cometproject.server.game.achievements.AchievementManager;
import com.cometproject.server.game.catalog.CatalogManager;
import com.cometproject.server.game.commands.ChatCommand;
import com.cometproject.server.game.commands.CommandManager;
import com.cometproject.server.game.groups.GroupManager;
import com.cometproject.server.game.items.ItemManager;
import com.cometproject.server.game.landing.LandingManager;
import com.cometproject.server.game.moderation.BanManager;
import com.cometproject.server.game.moderation.ModerationManager;
import com.cometproject.server.game.navigator.NavigatorManager;
import com.cometproject.server.game.permissions.PermissionsManager;
import com.cometproject.server.game.pets.PetManager;
import com.cometproject.server.game.pets.commands.PetCommandManager;
import com.cometproject.server.game.polls.PollManager;
import com.cometproject.server.game.polls.types.Poll;
import com.cometproject.server.game.quests.QuestManager;
import com.cometproject.server.game.rooms.RoomManager;
import com.cometproject.server.network.NetworkManager;
import com.cometproject.server.network.messages.outgoing.catalog.CatalogPublishMessageComposer;
import com.cometproject.server.network.messages.outgoing.moderation.ModToolMessageComposer;
import com.cometproject.server.network.messages.outgoing.notification.MotdNotificationMessageComposer;
import com.cometproject.server.network.messages.outgoing.room.polls.InitializePollMessageComposer;
import com.cometproject.server.network.sessions.Session;


public class ReloadCommand extends ChatCommand {

    @Override
    public void execute(Session client, String[] params) {
        String command = params.length == 0 ? "" : params[0];

        switch (command) {
            default:
                client.send(new MotdNotificationMessageComposer(
                        "Voici toutes les choses que l'on peut reload à l'aide de la commande :reload <type> !\n\n" +
                                "- bans\n" +
                                "- catalog ou catalogue\n" +
                                "- navigator\n" +
                                "- permissions\n" +
                                "- catalog\n" +
                                "- news\n" +
                                "- config\n" +
                                "- items\n" +
                                "- filter\n" +
                                "- locale\n" +
                                "- modpresets\n" +
                                "- groupitems\n" +
                                "- models\n" +
                                "- music\n" +
                                "- quests\n" +
                                "- achievements\n" +
                                "- pets\n" +
                                "- polls"
                ));

                break;
            case "bans":
                BanManager.getInstance().loadBans();

                sendNotif(Locale.get("command.reload.bans"), client);
                break;

            case "catalog":
                CatalogManager.getInstance().loadItemsAndPages();
                CatalogManager.getInstance().loadGiftBoxes();

                NetworkManager.getInstance().getSessions().broadcast(new CatalogPublishMessageComposer(true));
                sendNotif(Locale.get("command.reload.catalog"), client);
                break;

            case "catalogue":
                CatalogManager.getInstance().loadItemsAndPages();
                CatalogManager.getInstance().loadGiftBoxes();

                NetworkManager.getInstance().getSessions().broadcast(new CatalogPublishMessageComposer(true));
                sendNotif(Locale.get("command.reload.catalog"), client);
                break;

            case "navigator":
                NavigatorManager.getInstance().loadCategories();
                NavigatorManager.getInstance().loadPublicRooms();
                NavigatorManager.getInstance().loadStaffPicks();

                sendNotif(Locale.get("command.reload.navigator"), client);
                break;

            case "permissions":
                PermissionsManager.getInstance().loadRankPermissions();
                PermissionsManager.getInstance().loadPerks();
                PermissionsManager.getInstance().loadCommands();

                sendNotif(Locale.get("command.reload.permissions"), client);
                break;

            case "config":
                CometSettings.initialize();

                sendNotif(Locale.get("command.reload.config"), client);
                break;

            case "news":
                LandingManager.getInstance().loadArticles();

                sendNotif(Locale.get("command.reload.news"), client);
                break;

            case "items":
                ItemManager.getInstance().loadItemDefinitions();

                sendNotif(Locale.get("command.reload.items"), client);
                break;

            case "filter":
                RoomManager.getInstance().getFilter().loadFilter();

                sendNotif(Locale.get("command.reload.filter"), client);
                break;

            case "locale":
                Locale.reload();
                CommandManager.getInstance().reloadAllCommands();

                sendNotif(Locale.get("command.reload.locale"), client);
                break;

            case "modpresets":
                ModerationManager.getInstance().loadPresets();

                sendNotif(Locale.get("command.reload.modpresets"), client);

                for (BaseSession session : NetworkManager.getInstance().getSessions().getByPlayerPermission("mod_tool")) {
                    session.send(new ModToolMessageComposer());
                }
                break;

            case "groupitems":
                GroupManager.getInstance().getGroupItems().load();
                sendNotif(Locale.get("command.reload.groupitems"), client);
                break;

            case "models":
                RoomManager.getInstance().loadModels();
                sendNotif(Locale.get("command.reload.models"), client);
                break;

            case "music":
                ItemManager.getInstance().loadMusicData();
                sendNotif(Locale.get("command.reload.music"), client);
                break;

            case "quests":
                QuestManager.getInstance().loadQuests();
                sendNotif(Locale.get("command.reload.quests"), client);
                break;

            case "achievements":
                AchievementManager.getInstance().loadAchievements();

                sendNotif(Locale.get("command.reload.achievements"), client);
                break;

            case "pets":
                PetManager.getInstance().loadPetRaces();
                PetManager.getInstance().loadPetSpeech();
                PetManager.getInstance().loadTransformablePets();
                PetCommandManager.getInstance().initialize();

                sendNotif(Locale.get("command.reload.pets"), client);
                break;

            case "polls":
                PollManager.getInstance().initialize();

                if(PollManager.getInstance().roomHasPoll(client.getPlayer().getEntity().getRoom().getId())) {
                    Poll poll = PollManager.getInstance().getPollByRoomId(client.getPlayer().getEntity().getRoom().getId());

                    client.getPlayer().getEntity().getRoom().getEntities().broadcastMessage(new InitializePollMessageComposer(poll.getPollId(), poll.getPollTitle(), poll.getThanksMessage()));
                }

                sendNotif(Locale.get("command.reload.polls"), client);
                break;


        }
    }

    @Override
    public boolean isAsync() {
        return true;
    }

    @Override
    public String getPermission() {
        return "reload_command";
    }

    @Override
    public String getDescription() {
        return Locale.get("command.reload.description");
    }
}
