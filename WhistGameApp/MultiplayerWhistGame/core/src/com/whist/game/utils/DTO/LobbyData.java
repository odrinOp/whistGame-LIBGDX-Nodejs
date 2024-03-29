package com.whist.game.utils.DTO;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.LinkedList;
import java.util.List;

public class LobbyData {

    String owner;
    String roomName;
    List<String> players;

    public LobbyData(String roomName, List<String> players,String owner) {

        this.owner = owner;
        this.roomName = roomName;
        this.players = players;
    }

    public String getRoomName() {
        return roomName;
    }

    public List<String> getPlayers() {
        return players;
    }

    public String getOwner() {
        return owner;
    }

    public static LobbyData convertFromJSON(JSONObject data) {
        try {
            String roomName = data.getString("roomID");
            JSONArray array = data.getJSONArray("players");
            String owner = data.getString("owner");
            List<String> playersName = new LinkedList<>();
            for (int i = 0; i < array.length(); i++) {
                String playerName = array.getJSONObject(i).getString("nickname");
                playersName.add(playerName);
            }
            return new LobbyData(roomName,playersName, owner);

        } catch (JSONException e) {
            e.printStackTrace();
        }
        return null;

    }
}