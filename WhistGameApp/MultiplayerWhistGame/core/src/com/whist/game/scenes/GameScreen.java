package com.whist.game.scenes;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.Screen;
import com.badlogic.gdx.graphics.GL20;
import com.badlogic.gdx.graphics.Texture;
import com.badlogic.gdx.graphics.g2d.SpriteBatch;
import com.badlogic.gdx.graphics.g2d.TextureRegion;
import com.badlogic.gdx.scenes.scene2d.Stage;
import com.badlogic.gdx.scenes.scene2d.ui.Skin;
import com.badlogic.gdx.scenes.scene2d.ui.Slider;
import com.badlogic.gdx.utils.viewport.ExtendViewport;
import com.whist.game.StartClient;
import com.whist.game.generics.Card;
import com.whist.game.generics.Player;
import com.whist.game.utils.Constants;
import java.util.ArrayDeque;
import java.util.ArrayList;
import java.util.Collections;
import java.util.List;
import java.util.Queue;

public class GameScreen implements Screen {

    float screenWidth = 0;
    float screenHeight = 0;

    ExtendViewport viewport;
    Stage biddingStage;
    Stage stage;
    Skin skin;
    StartClient mainController;
    private SpriteBatch batch;
    private TextureRegion[][] regions;
    private TextureRegion cardBack;
    Texture cardSprite = new Texture("cardSprite.gif");

    public boolean canChooseCard = true;

    public Queue<Player> players = new ArrayDeque<>();
    /*
    wait for cards from the server [! maini de una ]
    allow player to palace bet and wait for a signal from the server to emit the bet
    emit cards one by one to the server (server verification here?)
     */
    //TODO nu mi se pare deloc o idee buna ... [doamne feri sa nu-i dai cifre la asta]
    //poate butoane separate cu numarul de maini pe care vrei sa pariezi?
    //ar fi bulletProofCode si destul de simplu de folosit
    // sau am putea sa incercam un slider

    Card[] crd;
    Card gen;
    Card aux;


    List<String> cardsStrList =  new ArrayList<>();
    public List<Card> hand = new ArrayList<>();

    public List<Card> putDownCards = new ArrayList<>();


    public void init() {

    }

    public GameScreen(StartClient mainController){
        System.out.println("[GameScreen] : Constructor");
        batch = new SpriteBatch();
        initGraphics();
        this.mainController=mainController;
    }

    @Override
    public void show() {
        System.out.println("[GameScreen] : Show");
        viewport = new ExtendViewport(Constants.WORLD_WIDTH, Constants.WORLD_HEIGHT);
        stage = new Stage(new ExtendViewport(Constants.WORLD_WIDTH,Constants.WORLD_HEIGHT));
        biddingStage = new Stage(new ExtendViewport(Constants.WORLD_WIDTH,Constants.WORLD_HEIGHT));
        skin = new Skin(Gdx.files.internal("skin.json"));
        Gdx.input.setInputProcessor(stage);


        Slider bidSlider = new Slider(0f,8f,1, false, skin);

        cardsStrList.add("h-2");
        cardsStrList.add("d-2");
        cardsStrList.add("c-2");
        cardsStrList.add("s-2");
        cardsStrList.add("h-3");
        cardsStrList.add("d-3");
        cardsStrList.add("c-3");
        cardsStrList.add("s-3");

        //System.out.println(cardsStrList);
//        gen = new Card("h-4",regions,viewport.getScreenWidth()/2 ,100);

        stage.addActor(bidSlider);
//        stage.addActor(gen.getCardActor());

        hand = initCards(cardsStrList, 400);
        addCardsToScene(hand,stage);

    }

    public List<Card> initCards(List<String> cards, float xPosition){
        float rot = cards.size()*5/2;
        int i = cards.size()*40/2;
        List<Card> ret = new ArrayList<>();
        Card card;

        if(cards.size() >8){
            throw new IndexOutOfBoundsException("More then 8 cards Impossible");
        }
        Collections.sort(cards);

        //ToDo gen ar merge si validate
        for (String str:cards) {
            card =  new Card(str,regions,xPosition - i,Constants.CARD_HAND_Y,this);
            ret.add(card);
            i -= 40;
            //System.out.println("Screen width" + screenWidth);
        }
        return ret;
    }

    public void addCardsToScene(List<Card> cards,Stage stage){
        for (Card crd:cards) {
            stage.addActor(crd.getCardActor());
        }
    }

    //TODO lista de Stringuri pe care le transforma in Cards
    //itereaza prin hand
    public void renderHUD(float delta, List<Card> cards){
        for (Card crd:cards) {
            crd.update(delta,viewport);
            if(crd.originalPosition.y >= Gdx.graphics.getHeight()/2){
                if(crd.choosed == false){
                    crd.choosed = true;
                    canChooseCard = false;
                    aux = crd;
                    putDownCards.add(crd);
                    System.out.println("[GameScreen] : Card choosed = " + crd.toString() + " " + crd.originalPosition.y );
                }
            }
        }
        hand.remove(aux);

    }
    public void renderPutDownCards(float delta, List<Card> putDownCards){
        //System.out.println("[renderPutDownCards]" + putDownCards.size());
        //Todo de randat astea la coordonate prestabilite
        if(putDownCards.size() > 0){
            for (Card crd:putDownCards) {
                crd.update(delta,viewport);
            }
        }
    }


    public void renderCard(Card card, int x, int y,float rot){
//        String[] cardVal = card.getValue().split("-");
//        int cardSimb=0;
//        int cardNr;
//
//        if(cardVal[0].equals("h")){
//            cardSimb = 0;
//        }
//        if(cardVal[0].equals("d")){
//            cardSimb = 1;
//        }
//        if(cardVal[0].equals("c")){
//            cardSimb = 2;
//        }
//        if(cardVal[0].equals("s")){
//            cardSimb = 3;
//        }
//        cardNr = Integer.parseInt( cardVal[1]) - 2;
//
//        //batch.draw(regions[0][0],x,y,0.0f,0.0f,regions[0][0].getRegionWidth(),regions[0][0].getRegionHeight(),1.0f,1.0f,rot);
//        batch.draw(regions[cardSimb][cardNr],x,y,0.0f,0.0f,regions[cardSimb][cardNr].getRegionWidth(),regions[cardSimb][cardNr].getRegionHeight(),1.0f,1.0f,rot);

    }

    @Override
    public void render(float delta) {
        //System.out.println("[GameScreen] : Render");
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
        renderPutDownCards(delta,putDownCards);
        renderHUD(delta,hand);
        stage.act(delta);
        stage.draw();

    }

    public void initGraphics(){
        cardBack = new TextureRegion(cardSprite,648,0,587,81);
        regions = TextureRegion.split(cardSprite, 81, 117);
    }

    //ToDo asta da rezolutia ferestrei calumea
    @Override
    public void resize(int width, int height) {
        System.out.println("[GameScreen] : resize");
        screenHeight = height;
        screenWidth = width;
        System.out.println("[GameScreen] resize : width=" + width + "|" + " height=" + height);
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
        System.out.println("[GameScreen] : dispose");
        hand.clear();
        stage.dispose();

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
}
