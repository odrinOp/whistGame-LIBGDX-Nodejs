package com.whist.game;

import com.badlogic.gdx.Game;
import com.badlogic.gdx.Gdx;
import com.whist.game.createRoomWindow.CreateRoomScreen;
import com.whist.game.lobbyScene.LobbyScreen;

import com.whist.game.mainMenuScreen.MainMenuScreen;
import com.whist.game.utils.AppState;


import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;


import java.net.URISyntaxException;
import java.util.LinkedList;
import java.util.List;

import io.socket.client.IO;
import io.socket.client.Socket;
import io.socket.emitter.Emitter;

public class StartClient extends Game  {

    private String TAG = StartClient.class.getSimpleName();
    /* screens */
    MainMenuScreen mainMenuScreen;
    CreateRoomScreen createRoomScreen;
    LobbyScreen lobbyScreen;


    private Socket socket;
    private AppState state = AppState.MAIN_MENU;
    boolean changeState = false;
    /*server related */
    private final static int port = 8080;
    private final static String ipAddr = "localhost";


    @Override
    public void render() {
        super.render();
        if(changeState) {
            switch (state) {
                case MAIN_MENU:
                    setScreen(mainMenuScreen);
                    break;
                case CREATE_ROOM:
                    setScreen(createRoomScreen);
                    break;
                case LOBBY_ROOM:
                    setScreen(lobbyScreen);
                    break;
            }
            changeState = false;
        }

    }





    @Override
    public void create() {

        mainMenuScreen = new MainMenuScreen(this);
        createRoomScreen = new CreateRoomScreen(this);
        lobbyScreen = new LobbyScreen(this);
        setScreen(mainMenuScreen);
    }

    public void goToCreateRoom() {
        state = AppState.CREATE_ROOM;
        changeState = true;
    }

    public void goToMainMenu() {
        state = AppState.MAIN_MENU;
        changeState = true;
    }



    private void  configSocketEvents() {
        socket.on("socketID", new Emitter.Listener() {
            @Override
            public void call(Object... args) {
                JSONObject data = (JSONObject) args[0];

                try {
                    String id = data.getString("id");
                    Gdx.app.log("SocketID",id);
                } catch (JSONException e) {
                    e.printStackTrace();
                }
            }
        });


        socket.on("lobbyData", new Emitter.Listener() {
            @Override
            public void call(Object... args) {
                JSONObject data = (JSONObject) args[0];
                try{
                    Gdx.app.log(TAG,data.toString());
                    String roomName = data.getString("room");
                    String ownerName = data.getString("owner");
                    JSONArray array = data.getJSONArray("players");

                    List<String> playersName = new LinkedList<>();
                    for(int i = 0; i<array.length();i++){
                        String playerName = array.getJSONObject(i).getString("name");
                        playersName.add(playerName);
                    }


                    initLobbyScreen(roomName,ownerName,playersName);
                    state = AppState.LOBBY_ROOM;
                    changeState = true;



                } catch (JSONException e) {
                    e.printStackTrace();
                }
            }
        });

    }

    private void initLobbyScreen(String roomName, String ownerName, List<String> playersName) {
            lobbyScreen.setRoomName(roomName);
            lobbyScreen.setOwnerName(ownerName);
            lobbyScreen.setPlayersName(playersName);

    }

    public void createRoom(String nickname, String room) {
        try {
            Gdx.app.log(TAG,"Trying to connect to server: " + "http://" + ipAddr + ":" + port);
            socket = IO.socket("http://" + ipAddr + ":" + port);
            socket.connect();


            JSONObject createRoomData = new JSONObject();
            createRoomData.put("nickname",nickname);
            createRoomData.put("room",room);
            socket.emit("createRoom",createRoomData);
            configSocketEvents();

        } catch (URISyntaxException | JSONException e) {
            e.printStackTrace();
        }
    }

    public void disconnect() {
        if(socket != null)
            socket.disconnect();
    }
}
