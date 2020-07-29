package com.whist.game.scenes;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.Screen;
import com.badlogic.gdx.graphics.GL20;
import com.badlogic.gdx.graphics.Texture;
import com.badlogic.gdx.graphics.g2d.SpriteBatch;
import com.badlogic.gdx.graphics.g2d.TextureRegion;
import com.badlogic.gdx.math.Vector2;
import com.badlogic.gdx.scenes.scene2d.Actor;
import com.badlogic.gdx.scenes.scene2d.InputEvent;
import com.badlogic.gdx.scenes.scene2d.Stage;
import com.badlogic.gdx.scenes.scene2d.Touchable;
import com.badlogic.gdx.scenes.scene2d.ui.Image;
import com.badlogic.gdx.scenes.scene2d.ui.Label;
import com.badlogic.gdx.scenes.scene2d.ui.Skin;
import com.badlogic.gdx.scenes.scene2d.ui.Table;
import com.badlogic.gdx.scenes.scene2d.ui.TextButton;
import com.badlogic.gdx.scenes.scene2d.ui.TextField;
import com.badlogic.gdx.scenes.scene2d.utils.ChangeListener;
import com.badlogic.gdx.scenes.scene2d.utils.DragListener;
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
    private TextureRegion[][] regions;
    private TextureRegion cardBack;
    Texture cardSprite = new Texture("cardSprite.gif");
    private List<Card> cards = new ArrayList<>();

    Actor act ;
    float imageX = 0;
    float imageY = 0;

            private static final float DRAG = 2.0f;
            private static final float ACCELERATION = 1000.0f;
            private static final float MAX_SPEED = 2000.0f;
            private static final float FOLLOW_MULTIPLIER = 5.0f;

            Vector2 targetPosition = new Vector2(0f,0f);// finger position

            boolean following = false;
            public Vector2 position = new Vector2(0f,0f);// pozitia cartii
            Vector2 velocity;// vector viteza al cartii

            ExtendViewport viewport;

    /*
    wait for cards from the server [! maini de una ]
    allow player to palace bet and wait for a signal from the server to emit the bet
    emit cards one by one to the server (server verification here?)
     */
    //TODO nu mi se pare deloc o idee buna ... [doamne feri sa nu-i dai cifre la asta]
    //poate butoane separate cu numarul de maini pe care vrei sa pariezi?
    //ar fi bulletProofCode si destul de simplu de folosit
    // sau am putea sa incercam un slider

    public void init() {
        position = new Vector2(0,0);
        velocity = new Vector2(0,0);
    }

    public GameScreen(StartClient mainController){
        batch = new SpriteBatch();
        initGraphics();
        this.mainController=mainController;
    }


    @Override
    public void show() {
        viewport = new ExtendViewport(Constants.WORLD_WIDTH, Constants.WORLD_HEIGHT);
        stage = new Stage(new ExtendViewport(Constants.WORLD_WIDTH,Constants.WORLD_HEIGHT));
        biddingStage = new Stage(new ExtendViewport(Constants.WORLD_WIDTH,Constants.WORLD_HEIGHT));
        skin = new Skin(Gdx.files.internal("skin.json"));
        Gdx.input.setInputProcessor(stage);
       // Gdx.input.setInputProcessor(biddingStage);

        //Not YET
//        Label bidLabel = new Label("Bid:", skin);
//        final TextField bidTF = new TextField("",skin);
//        //bidTF.setTouchable(Touchable.enabled);
//
//        Table table = new Table();
//        table.setFillParent(true);
//       // table.debug();
//        table.center();
//
//        table.add(bidLabel).pad(4);
//        table.add(bidTF).pad(4);
//
//        TextButton bidButton = new TextButton("Bid", skin);
//        bidButton.addListener(new ChangeListener() {
//            @Override
//            public void changed(ChangeEvent event, Actor actor) {
//                String bidAmoount = bidTF.getText();
//                System.out.println("[BidAmuount] = " + bidAmoount);
//                mainController.bid(Integer.parseInt(bidAmoount));
//            }
//        });
//        table.row();
//        table.add(bidButton).colspan(2).pad(4);

        act = new Image(regions[0][0]);
        act.setX(60);
        act.setY(100);
        act.setTouchable(Touchable.enabled);
        act.addListener(new DragListener() {
         @Override
            public boolean touchDown(InputEvent event, float x, float y, int pointer, int button) {
                targetPosition = new Vector2(act.getX() + x, act.getY() + y);
                following = true;
                return true;
            }

            @Override
            public void touchDragged(InputEvent event, float x, float y, int pointer) {
                if (following ==  true){
                    targetPosition = new Vector2(act.getX() + x,act.getY() + y);
                    System.out.println("touchDragged" + targetPosition.x + " " + targetPosition.y);
                }
            }

            @Override
            public void touchUp(InputEvent event, float x, float y, int pointer, int button) {
                super.touchUp(event, x, y, pointer, button);
                targetPosition =  new Vector2(act.getX() + x,act.getY() + y);
                following = false;
            }
        });
        act.debug();
        stage.addActor(act);

        //biddingStage.addActor(table);
       // biddingStage.addActor(bidButton);
       // stage.addActor(card);

    }

    public void renderHUD(){}

    public void renderCard(Card card, int x, int y,float rot){
        String[] cardVal = card.getValue().split("-");
        int cardSimb=0;
        int cardNr;

        if(cardVal[0].equals("h")){
            cardSimb = 0;
        }
        if(cardVal[0].equals("d")){
            cardSimb = 1;
        }
        if(cardVal[0].equals("c")){
            cardSimb = 2;
        }
        if(cardVal[0].equals("s")){
            cardSimb = 3;
        }
        cardNr = Integer.parseInt( cardVal[1]) - 2;

        //batch.draw(regions[0][0],x,y,0.0f,0.0f,regions[0][0].getRegionWidth(),regions[0][0].getRegionHeight(),1.0f,1.0f,rot);
        batch.draw(regions[cardSimb][cardNr],x,y,0.0f,0.0f,regions[cardSimb][cardNr].getRegionWidth(),regions[cardSimb][cardNr].getRegionHeight(),1.0f,1.0f,rot);

    }

    public void renderCardBack(int x, int y, float rot){

        batch.draw(regions[4][0],x,y,0.0f,0.0f,regions[4][0].getRegionWidth(),regions[4][0].getRegionHeight(),1.0f,1.0f,rot);

    }

    public void renderHand( List<Card> cards){
        float rot = cards.size()*5/2;
        int i = 0;
        for (Card cd:cards) {
            renderCard(cd,(int)(Constants.WORLD_WIDTH - cards.size()*20 + i),5,rot-=5);
            i+=25;
        }
    }


    @Override
    public void render(float delta) {
        viewport.apply();
        Gdx.gl.glClearColor(0.8f, 0.8f, 0.8f, 1.0f);
        Gdx.gl.glClear(GL20.GL_COLOR_BUFFER_BIT);

        batch.begin();
//        renderCard(card,10,10,-15);
//        renderHand(cards);
//        renderOpponentsHandTop(3,(int)Constants.WORLD_WIDTH,(int)Constants.WORLD_HEIGHT-40,10);
//        renderOpponentsHandRight(3,(int)Constants.WORLD_WIDTH*2,(int)Constants.WORLD_HEIGHT/2,10);
//        renderOpponentsHandLeft(3,10,(int)Constants.WORLD_HEIGHT/2 + 80,10);

        batch.end();
        //TODO write a resize fucntion for all HUDCards
        act.setX(imageX);
        act.setY(imageY);
        //TODO update function for all HUDCards
        update(delta);
        //TODO setPosition for only one of them
        act.setPosition(position.x,position.y);

        stage.act(delta);
        stage.draw();

    }

    public void update(float delta) {
        if (following) {
            Vector2 followVector = new Vector2(targetPosition.x - position.x, targetPosition.y - position.y);
            velocity.x = FOLLOW_MULTIPLIER * followVector.x;
            velocity.y = FOLLOW_MULTIPLIER * followVector.y;
        }
        velocity.clamp(0, MAX_SPEED);
        velocity.x = velocity.x - delta *DRAG *velocity.x;
        velocity.y = velocity.y - delta *DRAG *velocity.y;

        position.x = position.x + delta * velocity.x;
        position.y = position.y + delta * velocity.y;
        collideWithWalls(1,viewport.getWorldWidth(),viewport.getWorldHeight());
    }

    private void collideWithWalls(float radius, float viewportWidth, float viewportHeight) {
        if (position.x - radius < 0) {
            position.x = radius;
            velocity.x = -velocity.x;
        }
        if (position.x + act.getWidth() > viewportWidth ) {
            position.x = viewportWidth - act.getWidth();
            velocity.x = -velocity.x;
        }
        if (position.y - radius < 0) {
            position.y = radius;
            velocity.y = -velocity.y;
        }
        if (position.y + act.getHeight() > viewportHeight ) {
            position.y = viewportHeight - act.getHeight();
            velocity.y = -velocity.y;
        }
    }

    public void initGraphics(){
        cardBack = new TextureRegion(cardSprite,648,0,587,81);
        regions = TextureRegion.split(cardSprite, 81, 117);
    }

    //TODO de combinat tot cacatul asta intr-o singura functie
    public void renderOpponentsHandTop(int nrOfCards,int x,int y, float rot){
        //TODO am nevoie de o lsita de playeri cu macar numle si nuamrul de carti pe care il au in mana
        float rotOff = cards.size()*5/2;
        int i = 0;
        for (int c = 0;c<= nrOfCards;c++ ) {
            renderCardBack(x-nrOfCards*20 +i,y ,-rot - rotOff);
            i+=25;
            rotOff-=5;
        }
    }

    public void renderOpponentsHandRight(int nrOfCards,int x,int y, float rot){
        float rotOff = nrOfCards*5/2;
        int i = 0;
        for (int c = 0;c<= nrOfCards;c++ ) {
            renderCardBack(x,y - nrOfCards*20 +i ,rot + rotOff + 90);
            i+=25;
            rotOff-=5;
        }
    }

    public void renderOpponentsHandLeft(int nrOfCards,int x,int y, float rot){
        float rotOff = nrOfCards*5/2;
        int i = 0;
        for (int c = 0;c<= nrOfCards;c++ ) {
            renderCardBack(x,y - nrOfCards*20 +i ,rot - rotOff -90 );
            i+=25;
            rotOff-=5;
        }
    }

    @Override
    public void resize(int width, int height) {
        viewport.update(width, height, true);
        init();
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

    }
}
