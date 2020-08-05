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
import com.badlogic.gdx.scenes.scene2d.ui.Slider;
import com.badlogic.gdx.scenes.scene2d.ui.TextButton;
import com.badlogic.gdx.scenes.scene2d.utils.ChangeListener;
import com.badlogic.gdx.utils.viewport.ExtendViewport;
import com.badlogic.gdx.utils.viewport.ScreenViewport;
import com.whist.game.StartClient;
import com.whist.game.generics.Card;
import com.whist.game.generics.Player;
import com.whist.game.utils.Constants;
import java.util.ArrayDeque;
import java.util.ArrayList;
import java.util.Collections;
import java.util.List;
import java.util.Queue;

import static java.lang.Math.abs;
import static java.lang.Math.floor;
import static java.lang.Math.min;
import static java.lang.Math.sqrt;

public class GameScreen implements Screen {
    float screenWidth = 0;
    float screenHeight = 0;

    ScreenViewport viewport;
    Stage biddingStage;
    Stage stage;
    Skin skin;
    StartClient mainController;
    private SpriteBatch batch;
    private TextureRegion[][] regions;
    private TextureRegion cardBack;
    Texture cardSprite = new Texture("cardSprite.gif");

    public boolean canChooseCard = true;


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


    public List<String> cardsStrList =  new ArrayList<>();
    public List<Card> hand = new ArrayList<>();

    public List<Card> putDownCards = new ArrayList<>();
    //Todo de verificat astia sa mearga bine;
    public Queue<Player> players = new ArrayDeque<>();

    int nrOfCards = 8;
    Slider bidSlider ;
    Label bidVal ;
    TextButton bidButton;


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
        viewport = new ScreenViewport();
        stage = new Stage(viewport);

        skin = new Skin(Gdx.files.internal("skin.json"));
        Gdx.input.setInputProcessor(stage);


        bidSlider = new Slider(0f,8f,1, false, skin);
        bidVal = new Label( "[" + (int)bidSlider.getValue() + "/" + nrOfCards + "]",skin);
        bidButton =  new TextButton("Bid",skin);



        bidVal.setPosition(screenWidth/2 + bidSlider.getWidth()/2,screenHeight/2);
        bidSlider.setPosition(screenWidth/2 - bidSlider.getWidth()/2,screenHeight/2);
        bidButton.setPosition(screenWidth/2 ,screenHeight/2 - bidSlider.getHeight()*2);
        bidButton.addListener(new ChangeListener() {
            @Override
            public void changed(ChangeEvent event, Actor actor) {
                System.out.println("[Game] Bid btn pressed");
                //todo careful cu castul la int
                mainController.bidRP((int)bidSlider.getValue());
                //TODO mainController.sendBid() sau ceva...
            }
        });


        stage.addActor(bidSlider);
        stage.addActor(bidVal);
        stage.addActor(bidButton);

        cardsStrList.add("h-2");
        cardsStrList.add("d-2");
        cardsStrList.add("c-2");
        cardsStrList.add("s-2");
        cardsStrList.add("h-3");
        cardsStrList.add("d-3");
        cardsStrList.add("c-3");
//        cardsStrList.add("s-3");


        hand = initCards(cardsStrList);
        addCardsToScene(hand,stage);

    }

    public void updateBidText(){
        bidVal.setText("[" + (int)bidSlider.getValue() + "/" + nrOfCards + "]");
    }

    public List<Card> initCards(List<String> cards){
        float rot = cards.size()*5/2;
        int i = cards.size()*40/2;
        List<Card> ret = new ArrayList<>();
        Card card;

        if(cards.size() >8){
            System.out.println( "NR OF CARDS " + cards.size());
            System.out.println(cards);
            //throw new IndexOutOfBoundsException("More then 8 cards Impossible");
        }
        Collections.sort(cards);

        //ToDo gen ar merge si validate
        for (String str:cards) {
            card =  new Card(str,regions,screenWidth - i,Constants.CARD_HAND_Y,this);
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
            if(crd.originalPosition.y >= screenHeight/2){
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
        updateBidText();
        renderPutDownCards(delta,putDownCards);
        renderHUD(delta,hand);
        stage.act(delta);
        stage.draw();

    }

    public void initGraphics(){
        cardBack = new TextureRegion(cardSprite,648,0,587,81);
        regions = TextureRegion.split(cardSprite, 81, 117);
    }

    public void resizeHUD(){
        float width = min(screenHeight,screenWidth) /4 * 0.6f;
        float height = min(screenHeight,screenWidth) /4;
        float xOffset;
        float rotOffset = 3;
        float rot =  hand.size()/2 * rotOffset;
        if (screenHeight > screenWidth){
            xOffset = screenWidth/12;
        }else{
            xOffset = screenWidth/20;
        }
        float x = screenWidth/2 - xOffset*hand.size()/2 + xOffset/2;
        float y;
        //cerc
        float Cy = -screenHeight *90/100;
        float Cx = screenWidth/2;
        float R =  screenHeight;


        for (Card crd :hand) {
            y = (float) (sqrt(abs(R*R - (x-Cx)*(x-Cx))) + Cy);
            crd.getCardActor().setWidth(width);
            crd.getCardActor().setHeight(height);
            crd.getCardActor().setOriginX(width/2);
            crd.rePosition(x -crd.getCardActor().getWidth()/4,y);
            crd.setRot(rot);
            x += xOffset;
            rot -= rotOffset;
        }
    }

    //ToDo asta da rezolutia ferestrei calumea
    @Override
    public void resize(int width, int height) {
       // System.out.println("[GameScreen] : resize");
        screenHeight = height;
        screenWidth = width;
        System.out.println("[GameScreen] resize : width=" + screenWidth + "|" + " height=" + screenHeight);

        bidVal.setPosition(screenWidth/2 + bidSlider.getWidth()/2,screenHeight/2);
        bidSlider.setPosition(screenWidth/2 - bidSlider.getWidth()/2,screenHeight/2);
        bidButton.setPosition(screenWidth/2 ,screenHeight/2 - bidSlider.getHeight()*2);
        resizeHUD();

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
            renderCard(cd,(int)(screenWidth - cards.size()*20 + i),5,rot-=5);
            i+=25;
        }
    }

    public float getScreenWidth() {
        return screenWidth;
    }

    public float getScreenHeight() {
        return screenHeight;
    }
}
