package com.cometproject.server.network.messages.incoming.room.floor;

import com.cometproject.server.config.CometSettings;
import com.cometproject.server.config.Locale;
import com.cometproject.server.game.rooms.models.CustomFloorMapData;
import com.cometproject.server.game.rooms.types.Room;
import com.cometproject.server.network.messages.incoming.Event;
import com.cometproject.server.network.messages.outgoing.notification.AdvancedAlertMessageComposer;
import com.cometproject.server.protocol.messages.MessageEvent;
import com.cometproject.server.network.sessions.Session;
import com.cometproject.server.utilities.JsonFactory;


public class SaveFloorMessageEvent implements Event {
    @Override
    public void handle(Session client, MessageEvent msg) throws Exception {
        String model = msg.readString();
        final int doorX = msg.readInt();
        final int doorY = msg.readInt();
        final int doorRotation = msg.readInt();
        final int wallThickness = msg.readInt();
        final int floorThickness = msg.readInt();
        final int wallHeight = msg.readInt();

        if (client.getPlayer().getEntity() == null || client.getPlayer().getEntity().getRoom() == null) {
            return;
        }

        Room room = client.getPlayer().getEntity().getRoom();

        if ((room.getData().getOwnerId() != client.getPlayer().getId() && !client.getPlayer().getPermissions().getRank().roomFullControl())) {
            return;
        }

        model = model.replace((char) 10, (char) 0);
        String[] modelData = model.split(String.valueOf((char) 13));

        int sizeY = modelData.length;
        int sizeX = modelData[0].length();

        if (sizeX < 2 || sizeY < 2 || (CometSettings.floorEditorMaxX != 0 && sizeX > CometSettings.floorEditorMaxX) || (CometSettings.floorEditorMaxY != 0 && sizeY > CometSettings.floorEditorMaxY) || (CometSettings.floorEditorMaxTotal != 0 && CometSettings.floorEditorMaxTotal < (sizeX * sizeY))) {
            client.send(new AdvancedAlertMessageComposer("Invalid Model", Locale.get("command.floor.size")));
            return;
        }

        boolean hasTiles = false;
        boolean validDoor = false;

        try {
            for (int y = 0; y < sizeY; y++) {
                String modelLine = modelData[y];

                for (int x = 0; x < sizeX; x++) {
                    if (x < (modelLine.length() + 1)) {
                        if (!Character.toString(modelLine.charAt(x)).equals("x")) {
                            if (x == doorX && y == doorY) {
                                validDoor = true;
                            }

                            hasTiles = true;
                        }
                    }
                }
            }
        } catch (Exception e) {
            client.send(new AdvancedAlertMessageComposer("Invalid Model", "There seems to be a problem parsing this floor plan, please either try again or contact an admin!"));
        }

        if (!hasTiles || !validDoor) {
            client.send(new AdvancedAlertMessageComposer("Invalid Model", Locale.get("command.floor.invalid")));
            return;
        }

        room.getData().setThicknessWall(wallThickness);
        room.getData().setThicknessFloor(floorThickness);

        final CustomFloorMapData floorMapData = new CustomFloorMapData(doorX, doorY, doorRotation, model.trim(), wallHeight);

        room.getData().setHeightmap(JsonFactory.getInstance().toJson(floorMapData));
        room.getData().save();

        client.send(new AdvancedAlertMessageComposer("Model Saved", Locale.get("command.floor.complete"), "Go", "event:navigator/goto/" + client.getPlayer().getEntity().getRoom().getId(), ""));

        room.getItems().commit();
        room.setIdleNow();
    }
    public static short parse(char input)
    {

        switch (input)
        {
            default:
            case '0':
                return 0;
            case '1':
                return 1;
            case '2':
                return 2;
            case '3':
                return 3;
            case '4':
                return 4;
            case '5':
                return 5;
            case '6':
                return 6;
            case '7':
                return 7;
            case '8':
                return 8;
            case '9':
                return 9;
            case 'a':
                return 10;
            case 'b':
                return 11;
            case 'c':
                return 12;
            case 'd':
                return 13;
            case 'e':
                return 14;
            case 'f':
                return 15;
            case 'g':
                return 16;
            case 'h':
                return 17;
            case 'i':
                return 18;
            case 'j':
                return 19;
            case 'k':
                return 20;
            case 'l':
                return 21;
            case 'm':
                return 22;
            case 'n':
                return 23;
            case 'o':
                return 24;
            case 'p':
                return 25;
            case 'q':
                return 26;
            case 'r':
                return 27;
            case 's':
                return 28;
            case 't':
                return 29;
            case 'u':
                return 30;
            case 'v':
                return 31;
            case 'w':
                return 32;
        }
    }

}
