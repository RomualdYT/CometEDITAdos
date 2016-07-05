package com.cometproject.server.game.groups.items.types;

import java.sql.ResultSet;
import java.sql.SQLException;


public class GroupSymbolColour {
    private int id;
    private String colour;

    public GroupSymbolColour(ResultSet data) throws SQLException {
        this.id = data.getInt("id");
        this.colour = data.getString("firstvalue");
    }

    public int getId() {
        return id;
    }

    public String getColour() {
        return colour;
    }
}
