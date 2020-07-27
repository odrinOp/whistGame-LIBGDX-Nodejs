package com.whist.game;

import com.badlogic.gdx.Game;
import com.badlogic.gdx.Gdx;

import com.whist.game.generics.Room;
import com.whist.game.scenes.*;

import com.whist.game.utils.AppState;


import com.whist.game.utils.Constants;
import com.whist.game.utils.DTO.LobbyData;
import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;


import java.net.URISyntaxException;
import java.util.ArrayList;
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
    JoinRoomScreen joinRoomScreen;
    LoadingScreen loadingScreen;

    GameScreen gameScreen;
    CredentialsScreen credentialsScreen;


    private Socket socket;
    private AppState state = AppState.LOADING;
    boolean changeState = false;
    /*server related */


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
                case JOIN_ROOM:
                    setScreen(joinRoomScreen);
                    break;
                case LOBBY_ROOM:
                    setScreen(lobbyScreen);
                    break;
                case LOADING:
                    setScreen(loadingScreen);
                    break;
                case GAME_SCREEN:
                    setScreen(gameScreen);
                    break;
                case CREDENTIALS:
                    setScreen(credentialsScreen);
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
        joinRoomScreen = new JoinRoomScreen(this);
        loadingScreen = new LoadingScreen(this);
        gameScreen = new GameScreen(this);
        credentialsScreen = new CredentialsScreen(this);

        login();
        setScreen(loadingScreen);

    }

    public void goToCreateRoom() {
        state = AppState.CREATE_ROOM;
        changeState = true;
    }

    public void goToMainMenu() {
        state = AppState.MAIN_MENU;
        changeState = true;
    }



    private void configSocketEvents() {


        socket.on("connected", new Emitter.Listener() {
            @Override
            public void call(Object... args) {
                state = AppState.MAIN_MENU;
                changeState = true;
            }
        });

        socket.on("lobbyData", new Emitter.Listener() {
            @Override
            public void call(Object... args) {
                JSONObject data = (JSONObject) args[0];
                Gdx.app.log(TAG,data.toString());
                System.out.println(data);
                LobbyData lobbyData = LobbyData.convertFromJSON(data);


                initLobbyScreen(lobbyData.getRoomName(),lobbyData.getOwner(),lobbyData.getPlayers());
                System.out.println(lobbyData.getPlayers());
                state = AppState.LOBBY_ROOM;
                changeState = true;


            }
        });

        socket.on("getRoomsRP", new Emitter.Listener() {
            @Override
            public void call(Object... args) {
                Gdx.app.log("ConfigSocketEvents-getRoomsRP","Here");
                JSONObject data = (JSONObject) args[0];
                try{
                    //Gdx.app.log(TAG,data.toString());
                    System.out.println("JSON DATA = " + data);

                    int numberOfRooms = Integer.parseInt(data.getString("num_of_rooms"));

                    System.out.println("int numberOfRooms = " + numberOfRooms);



                    List<Room> rooms = new ArrayList<>();
                    System.out.println();
                    JSONArray roomsJSONArray = data.getJSONArray("rooms");
                    for(int i = 0; i<roomsJSONArray.length() ; i++){
                        Room rm = new Room();
                        rm.setNrOfPlayers(roomsJSONArray.getJSONObject(i).getInt("players"));
                        rm.setRoomID(roomsJSONArray.getJSONObject(i).getString("roomID"));
                        rm.setMaxCapacity(roomsJSONArray.getJSONObject(i).getInt("capacity"));
                        rooms.add(rm);


                    }
                    System.out.println("DONE GETTING ROOMS");
                    System.out.println(rooms);
                    joinRoomScreen.setRooms(rooms);


                    state = AppState.JOIN_ROOM;
                    changeState = true;

                } catch (Exception e) {

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

    private void login(){
        Gdx.app.log(TAG,"Trying to connect to server: " + Constants.serverHTTP);
        try {
            socket = IO.socket(Constants.serverHTTP);
            configSocketEvents();
            socket.connect();

            configSocketEvents();
        } catch (URISyntaxException e) {
            e.printStackTrace();
        }

    }

    public void createRoom(String nickname, String room) {
        try {

            JSONObject createRoomData = new JSONObject();
            createRoomData.put("nickname",nickname);
            createRoomData.put("roomID",room);
            socket.emit("createRoom",createRoomData);
            state = AppState.LOADING;
            changeState = true;


        } catch (JSONException e) {
            e.printStackTrace();
        }
    }

    public void disconnect() {
        if(socket != null) {
            socket.disconnect();
            socket = null;
        }
    }

    public void joinRoom(String nickname, String room) {
        try{
            JSONObject joinRoomData = new JSONObject();
            joinRoomData.put("nickname",nickname);
            joinRoomData.put("roomID",room);

            socket.emit("joinRoom",joinRoomData);
            state = AppState.LOADING;
            changeState = true;

        } catch (JSONException e) {
            e.printStackTrace();
        }
    }

    public void goToJoinRoom() {
        socket.emit("getRoomsRQ");
        state = AppState.LOADING;
        changeState = true;
    }



    @Override
    public void dispose() {
        super.dispose();
        System.out.println("Disposed here!");
        if(socket != null)
            socket.disconnect();
    }

    public void leaveRoom() {
        if(socket != null)
            socket.emit("leaveRoom");
    }

    public void getRooms() {
        socket.emit("getRoomsRQ");
    }


   // TODO de implementat asta in server gen...
    public void bid(int bidAmount) {
        if(bidAmount < 0 || bidAmount>8){
            throw new NumberFormatException("bet not in [0,8]");
        }
        socket.emit("bid", bidAmount);
    }

    public void goToGame() {
        state = AppState.GAME_SCREEN;
        changeState = true;
    }
    public void goToCredentialsScreen(String roomID) {
        credentialsScreen.setRoomID(roomID);
        state = AppState.CREDENTIALS;
        changeState = true;


    }
}
