package com.whist.game.scenes;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.Screen;
import com.badlogic.gdx.graphics.GL20;
import com.badlogic.gdx.scenes.scene2d.Actor;
import com.badlogic.gdx.scenes.scene2d.Stage;
import com.badlogic.gdx.scenes.scene2d.ui.Label;
import com.badlogic.gdx.scenes.scene2d.ui.ScrollPane;
import com.badlogic.gdx.scenes.scene2d.ui.Skin;
import com.badlogic.gdx.scenes.scene2d.ui.Table;
import com.badlogic.gdx.scenes.scene2d.ui.TextButton;
import com.badlogic.gdx.scenes.scene2d.utils.ChangeListener;
import com.badlogic.gdx.utils.viewport.ExtendViewport;
import com.whist.game.StartClient;
import com.whist.game.generics.Room;
import com.whist.game.utils.Constants;

import java.util.ArrayList;
import java.util.List;


public class JoinRoomScreen implements Screen {

    private StartClient mainController;
    private Skin skin;
    private Stage stage;
    private List<Room> rooms = new ArrayList<>();



    public JoinRoomScreen(StartClient mainController) {
        this.mainController = mainController;
    }

    @Override
    public void show() {



        stage = new Stage(new ExtendViewport(Constants.WORLD_WIDTH,Constants.WORLD_HEIGHT));
        skin = new Skin(Gdx.files.internal("skin.json"));

        Gdx.input.setInputProcessor(stage);


        List<Room> rooms = new ArrayList<>();
        Room room = new Room("Room1",7,5);
        rooms.add(room);
        Table table = new Table();
        table.debug();

        table.center();

        table.defaults().expandX().fill().space(5f);
        refreshTable(table,this.rooms);


        ScrollPane scrollPane = new ScrollPane(table,skin);
        scrollPane.setWidth(Constants.WORLD_WIDTH*2-200);
        scrollPane.setHeight(Constants.WORLD_HEIGHT-100);
        scrollPane.setPosition(50,50);
        scrollPane.debug();


        TextButton backBtn = new TextButton("Back",skin);
        backBtn.setPosition(15,15);
        backBtn.setHeight(30);
        backBtn.setWidth(100);

        backBtn.addListener(new ChangeListener() {
            @Override
            public void changed(ChangeEvent event, Actor actor) {
                mainController.goToMainMenu();
            }
        });
        // stage.addActor(table);
        stage.addActor(backBtn);
        stage.addActor(scrollPane);


    }

    public void refreshTable(Table table, List<Room> rooms){
        for (Room rm:rooms) {
            table.add(new Label(rm.getRoomID(),skin));
            table.add(new Label("[" + rm.getNrOfPlayers()+ "/" + rm.getMaxCapacity() +"]",skin));
            TextButton joinBtn = new TextButton("Join",skin);
            table.add(joinBtn);
            joinBtn.addListener(new ChangeListener() {
                @Override
                public void changed(ChangeEvent event, Actor actor) {
                    mainController.goToLobby();
                }
            });

            table.row();
        }

    }

    @Override
    public void render(float delta) {
        Gdx.gl.glClearColor(0.8f,0.8f, 0.8f, 1.0f);
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



    public List<Room> getRooms() {
        return rooms;
    }

    public void setRooms(List<Room> rooms) {
        this.rooms = rooms;
    }
}
