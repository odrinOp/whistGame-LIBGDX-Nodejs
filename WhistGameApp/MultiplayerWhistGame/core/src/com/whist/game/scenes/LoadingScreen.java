package com.whist.game.scenes;


import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.Screen;
import com.badlogic.gdx.graphics.GL20;
import com.badlogic.gdx.scenes.scene2d.Stage;
import com.badlogic.gdx.scenes.scene2d.ui.Label;
import com.badlogic.gdx.scenes.scene2d.ui.Skin;
import com.badlogic.gdx.utils.Align;
import com.badlogic.gdx.utils.viewport.ExtendViewport;
import com.ray3k.stripe.scenecomposer.SceneComposerStageBuilder;
import com.whist.game.StartClient;
import com.whist.game.utils.Constants;

public class LoadingScreen implements Screen {
    Stage stage;
    Skin skin;
    StartClient mainController;
    private final String TAG = LoadingScreen.class.getSimpleName();
    Label loadingTxt;
    String addingTxt = "";


    int i = 1;


    public LoadingScreen(StartClient mainController) {
        this.mainController = mainController;
    }

    @Override
    public void show() {

        stage = new Stage(new ExtendViewport(Constants.WORLD_WIDTH,Constants.WORLD_HEIGHT));
        skin = new Skin(Gdx.files.internal("skin.json"));

        SceneComposerStageBuilder builder = new SceneComposerStageBuilder();
        builder.build(stage,skin,Gdx.files.internal("loadingScene.json"));

        Gdx.input.setInputProcessor(stage);

        loadingTxt = stage.getRoot().findActor("loading");
        loadingTxt.setAlignment(Align.left);


    }

    @Override
    public void render(float delta) {
        Gdx.gl.glClearColor(1.0f, 1.0f, 1.0f, 1.0f);
        Gdx.gl.glClear(GL20.GL_COLOR_BUFFER_BIT);

        updateText(delta);

        stage.act(delta);
        stage.draw();
    }

    private void updateText(float delta) {
        if(loadingTxt == null)
            return;

        if (i%20 == 0){
            addingTxt+= ".";
        }
        i++;
        if(i == 79) {
            i = 1;
            addingTxt = "";
        }




        loadingTxt.setText("Loading" + addingTxt);
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
