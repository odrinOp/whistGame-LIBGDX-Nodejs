package com.whist.game.mainMenuScreen;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.Screen;
import com.badlogic.gdx.graphics.GL20;
import com.badlogic.gdx.scenes.scene2d.Actor;
import com.badlogic.gdx.scenes.scene2d.Stage;
import com.badlogic.gdx.scenes.scene2d.ui.Skin;
import com.badlogic.gdx.scenes.scene2d.ui.TextButton;
import com.badlogic.gdx.scenes.scene2d.utils.ChangeListener;
import com.badlogic.gdx.utils.viewport.ExtendViewport;
import com.ray3k.stripe.scenecomposer.SceneComposerStageBuilder;
import com.whist.game.StartClient;
import com.whist.game.utils.Constants;

public class MainMenuScreen implements Screen {
    Stage stage;
    Skin skin;
    StartClient mainController;
    private final String TAG = MainMenuScreen.class.getSimpleName();

    public MainMenuScreen(StartClient mainController) {
        this.mainController = mainController;



    }

    @Override
    public void show() {

        stage = new Stage(new ExtendViewport(Constants.WORLD_WIDTH,Constants.WORLD_HEIGHT));
        skin = new Skin(Gdx.files.internal("skin.json"));

        SceneComposerStageBuilder builder = new SceneComposerStageBuilder();
        builder.build(stage,skin,Gdx.files.internal("mainScene.json"));
        Gdx.input.setInputProcessor(stage);
        //Add button listeners

        TextButton createRoomBtn = stage.getRoot().findActor("createRoom");
        TextButton joinRoomBtn = stage.getRoot().findActor("joinRoom");
        TextButton exitBtn = stage.getRoot().findActor("exit");

        createRoomBtn.addListener(new ChangeListener() {
            @Override
            public void changed(ChangeEvent event, Actor actor) {
                Gdx.app.log(TAG + "-" + actor.getName(),"Pressed");
                mainController.goToCreateRoom();
            }
        });

        joinRoomBtn.addListener(new ChangeListener() {
            @Override
            public void changed(ChangeEvent event, Actor actor) {
                Gdx.app.log(TAG + "-" + actor.getName(), "Pressed");
            }
        });

        exitBtn.addListener(new ChangeListener() {
            @Override
            public void changed(ChangeEvent event, Actor actor) {
                Gdx.app.log(TAG + "-" + actor.getName(), "Pressed" );
                Gdx.app.exit();
            }
        });
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
}
