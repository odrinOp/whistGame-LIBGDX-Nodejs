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
        stage = new Stage(new ExtendViewport(Gdx.graphics.getWidth(),Gdx.graphics.getHeight()));
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
        scrollPane.setWidth(Gdx.graphics.getWidth()-200);
        scrollPane.setHeight(Gdx.graphics.getHeight()-100);
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

    @Override
    public void render(float delta) {
        Gdx.gl.glClearColor(0.8f,0.8f, 0.8f, 1.0f);
        Gdx.gl.glClear(GL20.GL_COLOR_BUFFER_BIT);

        stage.act(delta);
        stage.draw();
    }

    @Override
    public void resize(int width, int height) {
        //todo schimba constantele cu rezolutia din resize
        //todo de facut la tot resize
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

    public void refreshTable(Table table, List<Room> rooms){
        table.defaults().width(110);
        for (final Room rm:rooms) {
            table.row().setActorHeight(20);
            table.add(new Label(rm.getRoomID(),skin)).width(rm.getRoomID().length()*20);//!! NETESTAT
            table.add(new Label("[" + rm.getNrOfPlayers()+ "/" + rm.getMaxCapacity() +"]",skin)).width(50).expandX();
            TextButton joinBtn = new TextButton("Join",skin);
            joinBtn.setHeight(30);
            table.add(joinBtn).width(100).pad(3);

            joinBtn.addListener(new ChangeListener() {
                @Override
                public void changed(ChangeEvent event, Actor actor) {
                    mainController.goToCredentialsScreen(rm.getRoomID());
                }
            });
            table.row();
        }
    }

    public List<Room> getRooms() {
        return rooms;
    }

    public void setRooms(List<Room> rooms) {
        this.rooms = rooms;
    }
}
