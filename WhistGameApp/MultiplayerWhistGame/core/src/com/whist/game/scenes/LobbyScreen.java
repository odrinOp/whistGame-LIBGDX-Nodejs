package com.whist.game.scenes;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.Screen;
import com.badlogic.gdx.graphics.GL20;
import com.badlogic.gdx.scenes.scene2d.Actor;
import com.badlogic.gdx.scenes.scene2d.Stage;
import com.badlogic.gdx.scenes.scene2d.ui.HorizontalGroup;
import com.badlogic.gdx.scenes.scene2d.ui.Label;
import com.badlogic.gdx.scenes.scene2d.ui.Skin;
import com.badlogic.gdx.scenes.scene2d.ui.TextButton;
import com.badlogic.gdx.scenes.scene2d.ui.VerticalGroup;
import com.badlogic.gdx.scenes.scene2d.utils.ChangeListener;
import com.badlogic.gdx.utils.viewport.ExtendViewport;
import com.ray3k.stripe.scenecomposer.SceneComposerStageBuilder;
import com.whist.game.StartClient;
import com.whist.game.utils.Constants;

import java.util.List;

public class LobbyScreen implements Screen {

    StartClient mainController;
    Stage stage;
    Skin skin;
    private final String TAG = LobbyScreen.class.getSimpleName();
    private String roomName = "";
    private String owner = "";
    private List<String> players;
    private Label roomLabel;
    private Label ownerLabel;
    private VerticalGroup playersBox;

    public LobbyScreen(StartClient mainController) {
        this.mainController = mainController;


    }

    @Override
    public void show() {
        stage = new Stage(new ExtendViewport(Constants.WORLD_WIDTH,Constants.WORLD_HEIGHT));
        skin = new Skin(Gdx.files.internal("skin.json"));

        SceneComposerStageBuilder builder = new SceneComposerStageBuilder();
        builder.build(stage,skin,Gdx.files.internal("lobbyScene.json"));

        Gdx.input.setInputProcessor(stage);

        HorizontalGroup hBox = stage.getRoot().findActor("hBox");
        roomLabel= hBox.findActor("room");
        ownerLabel = hBox.findActor("owner");

        playersBox = stage.getRoot().findActor("players");

        TextButton backBtn = stage.getRoot().findActor("back");

        updateScreen();

        backBtn.addListener(new ChangeListener() {
            @Override
            public void changed(ChangeEvent event, Actor actor) {
                mainController.leaveRoom();
                mainController.goToMainMenu();
            }
        });

        TextButton readyBtn = new TextButton("Ready",skin);
        readyBtn.setPosition(550,15);
        readyBtn.setHeight(30);
        readyBtn.setWidth(100);

        readyBtn.addListener(new ChangeListener() {
            @Override
            public void changed(ChangeEvent event, Actor actor) {
                mainController.goToGame();
                Gdx.app.log("Credentials","Button Pressed");
            }
        });

        stage.addActor(readyBtn);

    }

    @Override
    public void render(float delta) {
        Gdx.gl.glClearColor(1.0f, 1.0f, 1.0f, 1.0f);
        Gdx.gl.glClear(GL20.GL_COLOR_BUFFER_BIT);

        stage.act(delta);
        stage.draw();
    }

    @Override
    public void resize(int width, int height) {
        stage.getViewport().update(width, height,true);
    }

    @Override
    public void pause() {

    }

    @Override
    public void resume() {

    }

    @Override
    public void hide() {

    }

    @Override
    public void dispose() {
        stage.dispose();
        skin.dispose();
    }

    public void setRoomName(String roomName) {
        this.roomName = roomName;
    }

    public void setOwnerName(String ownerName) {
        this.owner = ownerName;
    }

    public void setPlayersName(List<String> playersName) {
        this.players = playersName;
    }

    public void updateScreen() {

        roomLabel.setText("Room's name: " + roomName);
        ownerLabel.setText("Owner: " + owner);

        playersBox.clear();

      for(String player: players){
            playersBox.addActor(new Label(player,skin));
        }
    }
}
