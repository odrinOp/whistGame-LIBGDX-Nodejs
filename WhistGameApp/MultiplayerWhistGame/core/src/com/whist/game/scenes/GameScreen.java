package com.whist.game.scenes;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.Screen;
import com.badlogic.gdx.graphics.GL20;
import com.badlogic.gdx.graphics.Texture;
import com.badlogic.gdx.graphics.g2d.SpriteBatch;
import com.badlogic.gdx.graphics.g2d.TextureRegion;
import com.badlogic.gdx.scenes.scene2d.Actor;
import com.badlogic.gdx.scenes.scene2d.Stage;
import com.badlogic.gdx.scenes.scene2d.ui.Label;
import com.badlogic.gdx.scenes.scene2d.ui.Skin;
import com.badlogic.gdx.scenes.scene2d.ui.Table;
import com.badlogic.gdx.scenes.scene2d.ui.TextButton;
import com.badlogic.gdx.scenes.scene2d.ui.TextField;
import com.badlogic.gdx.scenes.scene2d.utils.ChangeListener;
import com.badlogic.gdx.utils.viewport.ExtendViewport;
import com.whist.game.StartClient;
import com.whist.game.generics.Card;
import com.whist.game.utils.Constants;

import java.util.ArrayList;
import java.util.List;

public class GameScreen implements Screen {

    Stage biddingStage;
    Stage stage;
    Skin skin;
    StartClient mainController;
    private SpriteBatch batch;

    int playerCount;
    private TextureRegion[][] regions;
    Texture cardSprite = new Texture("cardSprite.gif");

    private List<Card> cards = new ArrayList<>();

    /*

    wait for cards from the server [! maini de una ]
    allow player to palace bet and wait for a signal from the server to emit the bet
    emit cards one by one to the server (server verification here?)

     */

    public GameScreen(StartClient mainController){
        batch = new SpriteBatch();
        initGraphics();
        this.mainController=mainController;
    }


    @Override
    public void show() {
        stage = new Stage(new ExtendViewport(Constants.WORLD_WIDTH,Constants.WORLD_HEIGHT));
        skin = new Skin(Gdx.files.internal("skin.json"));
        Gdx.input.setInputProcessor(stage);

        Label bidLabel = new Label("Bid:", skin);
        final TextField bidTF = new TextField("",skin);

        Table table = new Table();
        table.setFillParent(true);
       // table.debug();
        table.center();

        table.add(bidLabel).pad(4);
        table.add(bidTF).pad(4);

        TextButton bidButton = new TextButton("Bid", skin);
        bidButton.addListener(new ChangeListener() {
            @Override
            public void changed(ChangeEvent event, Actor actor) {
                String bidAmoount = bidTF.getText();
                System.out.println("[BidAmuount] = " + bidAmoount);
                mainController.bid(Integer.parseInt(bidAmoount));

                //TODO nu mi se pare deloc o idee buna ... [doamne feri sa nu-i dai cifre la asta]
                //poate butoane separate cu numarul de maini pe care vrei sa pariezi?
                //ar fi bulletProofCode si destul de simplu de folosit
                // sau am putea sa incercam un slider
            }
        });
        table.row();
        table.add(bidButton).colspan(2).pad(4);


        stage.addActor(table);
       // stage.addActor(bidButton);

    }

    public void renderCards(int x, int y,float rot){

        //TODO de generalizat
        // si de adaugat rotatie
        batch.begin();
        batch.draw(regions[0][0],x,y,0.0f,0.0f,regions[0][0].getRegionWidth(),regions[0][0].getRegionHeight(),1.0f,1.0f,rot);
        batch.end();
    }

    public void initGraphics(){
        regions = TextureRegion.split(cardSprite, 81, 117);
    }

    @Override
    public void render(float delta) {
        Gdx.gl.glClearColor(0.8f, 0.8f, 0.8f, 1.0f);
        Gdx.gl.glClear(GL20.GL_COLOR_BUFFER_BIT);

        renderCards(10,10,-15);

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
