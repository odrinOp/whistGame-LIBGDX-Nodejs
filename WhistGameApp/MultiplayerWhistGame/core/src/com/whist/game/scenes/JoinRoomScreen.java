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
import com.badlogic.gdx.scenes.scene2d.ui.TextField;
import com.badlogic.gdx.scenes.scene2d.ui.VerticalGroup;
import com.badlogic.gdx.scenes.scene2d.utils.ChangeListener;
import com.badlogic.gdx.utils.viewport.ExtendViewport;
import com.ray3k.stripe.scenecomposer.SceneComposerStageBuilder;
import com.whist.game.StartClient;
import com.whist.game.utils.Constants;


public class JoinRoomScreen implements Screen {

    private StartClient mainController;
    private Skin skin;
    private Stage stage;

    public JoinRoomScreen(StartClient mainController) {
        this.mainController = mainController;
    }

    @Override
    public void show() {
        stage = new Stage(new ExtendViewport(Constants.WORLD_WIDTH,Constants.WORLD_HEIGHT));
        skin = new Skin(Gdx.files.internal("skin.json"));

        Gdx.input.setInputProcessor(stage);

        Label nameLabel = new Label("Name:", skin);
        Label addressLabel = new Label("Dummy:", skin);
        Label addressLabe2 = new Label("Dummy:", skin);
        Label addressLabe3 = new Label("Dummy:", skin);
        Label addressLabe4 = new Label("Dummy:", skin);



        Table table = new Table();
        table.debug();
        // table.setFillParent(true);
        table.center();

        table.defaults().expandX().fill().space(5f);
        //table.pad(10f);
        table.add(nameLabel);

        table.row();
        table.add(addressLabel);

        table.row();
        table.add(addressLabe2);
        table.row();
        table.add(addressLabe3);
        table.row();
        table.add(addressLabe4);


        //table.right().bottom();

        ScrollPane scrollPane = new ScrollPane(table,skin);
        scrollPane.setWidth(400f);
        scrollPane.setWidth(400f);
        scrollPane.setPosition(100,100);
        scrollPane.debug();


        TextButton backBtn = new TextButton("Back",skin);
        backBtn.setPosition(Constants.WORLD_WIDTH/2,Constants.WORLD_HEIGHT/10);
        backBtn.setHeight(30);
        backBtn.setWidth(100);

//        joinRoomBtn.addListener(new ChangeListener() {
//            @Override
//            public void changed(ChangeEvent event, Actor actor) {
//               // String nickname = nicknameField.getText();
//               // String room = roomField.getText();
//
//                mainController.joinRoom(nickname,room);
//            }
//        });



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
}
