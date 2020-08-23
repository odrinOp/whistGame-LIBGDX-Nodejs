package com.whist.game.scenes;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.Screen;
import com.badlogic.gdx.graphics.GL20;


import com.badlogic.gdx.graphics.GLTexture;
import com.badlogic.gdx.graphics.Texture;
import com.badlogic.gdx.graphics.g2d.BitmapFont;
import com.badlogic.gdx.graphics.g2d.SpriteBatch;
import com.badlogic.gdx.graphics.g2d.TextureRegion;
import com.badlogic.gdx.math.Vector2;
import com.badlogic.gdx.scenes.scene2d.Actor;
import com.badlogic.gdx.scenes.scene2d.Stage;
import com.badlogic.gdx.scenes.scene2d.ui.Label;
import com.badlogic.gdx.scenes.scene2d.ui.Skin;
import com.badlogic.gdx.scenes.scene2d.ui.Slider;
import com.badlogic.gdx.scenes.scene2d.ui.TextButton;
import com.badlogic.gdx.scenes.scene2d.utils.ChangeListener;
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
import static java.lang.Math.min;
import static java.lang.Math.sqrt;

public class GameScreen implements Screen {

    float screenWidth = 0;
    float screenHeight = 0;

    ScreenViewport viewport;
    Stage stage;
    Skin skin;
    StartClient mainController;
    private SpriteBatch batch;
    private TextureRegion[][] regions;
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

    Card aux;

    public List<String> cardsStrList =  new ArrayList<>();
    public List<Card> hand = new ArrayList<>();
    public List<Card> putDownCards = new ArrayList<>();
    //Todo de verificat astia sa mearga bine;

    public Queue<Player> players = new ArrayDeque<>();

    int nrOfCards = 8;

    Slider bidSlider;
    Label bidVal;
    TextButton bidButton;


    public void init() {

    }

    void initBidHuUD(){

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
    }

    public void initGraphics(){
        //cardBack = new TextureRegion(cardSprite,648,0,587,81);
        regions = TextureRegion.split(cardSprite, 81, 117);
    }

    public GameScreen(StartClient mainController){
        System.out.println("[GameScreen] : Constructor");
        batch = new SpriteBatch();
        initGraphics();
        this.mainController=mainController;
    }

    @Override
    public void show() {
        //todo de adaugat un label cu numarul de playeri sus in dreapta
        hand.clear();
        System.out.println("[GameScreen] : Show");
        viewport = new ScreenViewport();
        stage = new Stage(viewport);

        skin = new Skin(Gdx.files.internal("skin.json"));
        Gdx.input.setInputProcessor(stage);


        Texture texture = new Texture(Gdx.files.internal("OCRAsrd.png"));
        texture.setFilter(Texture.TextureFilter.Linear, Texture.TextureFilter.Linear);
        BitmapFont font = new BitmapFont(Gdx.files.internal("OCRAsrd.fnt"), new TextureRegion(texture), false);

        //putDownCards.add(new Card("h-12",regions,screenWidth/2 ,screenHeight/2,this));

        cardsStrList.add("h-2");
        cardsStrList.add("d-2");
        cardsStrList.add("c-2");
        cardsStrList.add("s-2");
        cardsStrList.add("h-3");
        cardsStrList.add("d-3");
        cardsStrList.add("c-3");
//        cardsStrList.add("s-3");
        Player hudi =  new Player("Hudy",8);
        Player odr  =  new Player("Odrin",8);
        Player hudi2  =  new Player("Hudy2",8);

        players.add(hudi);
        players.add(odr);
        players.add(hudi2);


//        Card card =  new Card("b-2",regions,screenWidth/2,screenHeight/2,this);

        initBidHuUD();
        initOpponetCards(players);

       // System.out.println("put down cards  = " + putDownCards);

        hand = initCards(cardsStrList);

        addCardsToScene(hand,stage);
        addCardsToScene(putDownCards,stage);

       // stage.addActor(bidSlider);
        //stage.addActor(bidVal);
       // stage.addActor(bidButton);
        //stage.addActor(card.getCardActor());

    }
     List<Card> initCards(List<String> cards){
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

    //todo coada de plyeri tebuie initializate bine
    void initOpponetCards(Queue<Player> players){
        Card card;
        for (Player pl : players) {
            for(int i = 0; i< nrOfCards; i++){
                card =  new Card("b-2",regions,screenWidth/2,screenHeight*9/10,this);
                card.getCardActor().setOriginX(card.getCardActor().getWidth()/2);
                pl.cards.add(card);
                //todo vezi ce faci cu asta
               // card.getCardActor().setTouchable(Touchable.disabled);
                stage.addActor(card.getCardActor());
            }
            System.out.println(pl.cards);
        }
    }

    void updateOpponents(float delta){
        for (Player pl : players) {
            for (Card crd : pl.cards) {
                crd.update(delta,viewport);
            }
        }
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
                    //todo de pus asta false
                    canChooseCard = true;
                    aux = crd;
                    putDownCard(aux);
                    System.out.println("[GameScreen] : Card choosed = " + crd.toString() + " " + crd.originalPosition.y );
                }
            }
        }
        hand.remove(aux);
    }

    public void renderPutDownCards(float delta, List<Card> putDownCards){
            for (Card crd:putDownCards) {
                crd.update(delta,viewport);
            }
    }

    void putDownCard(Card card){
        card.getCardActor().setOriginX(card.getCardActor().getWidth()/2);
        System.out.println(screenHeight);
        card.rePosition((float)screenWidth/2,(float)screenHeight/2);
        card.setRot((putDownCards.size() - 1)*50);
        putDownCards.add(card);
    }

    @Override
    public void render(float delta) {

        viewport.apply();
        Gdx.gl.glClearColor(0.8f, 0.8f, 0.8f, 1.0f);
        Gdx.gl.glClear(GL20.GL_COLOR_BUFFER_BIT);


        //TODO write a resize fucntion for all HUDCards
        updateBidText();

        renderPutDownCards(delta,putDownCards);
        renderHUD(delta,hand);
        updateOpponents(delta);

        stage.act(delta);
        stage.draw();

    }

    void updateBidText(){
        bidVal.setText("[" + (int)bidSlider.getValue() + "/" + nrOfCards + "]");
    }

//todo resize si put down cards
    public void resizeHUD(){
        boolean portrait = false;
        boolean landscape = false;
        float width = min(screenHeight,screenWidth) /4 * 0.6f;
        float height = min(screenHeight,screenWidth) /4;
        float xOffset;
        float rotOffset = 3;
        float rot =  hand.size()/2 * rotOffset;

        if (screenHeight > screenWidth){
            xOffset = screenWidth/12;
            portrait = true;
        }else{
            xOffset = screenWidth/20;
            landscape = true;
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

        for (Card crd :putDownCards) {
            crd.getCardActor().setWidth(width);
            crd.getCardActor().setHeight(height);
            crd.getCardActor().setOriginX(width/2);
            crd.rePosition(screenWidth/2,screenHeight/2);
        }

        bidSlider.setWidth(screenWidth/4);
        bidSlider.setDebug(true);
        bidSlider.setHeight(screenHeight/15);
        bidSlider.getStyle().knob.setMinHeight(screenHeight/15);
        bidSlider.getStyle().knob.setMinWidth(screenHeight/16);
        bidSlider.getStyle().background.setMinHeight(bidSlider.getHeight()/2);

        bidVal.setSize(screenHeight/15,screenHeight/15);
        //bidVal.getStyle().font.
    }

    void resizeOpponents(){
        //todo de adaugat nicknameul undedva pe Ecran (dupa ce invat partea de fonturi)
        Vector2 plPos;
        Vector2 centerPos;

            if(players.size() == 1){
                //CENTER UP
                 Player pl = players.remove();
                     plPos = new Vector2(screenWidth/2,screenHeight/2);
                     centerPos =  new Vector2(screenWidth/2, screenHeight *14.4f/10);
                        pl.positionCardsHor((int)screenWidth, (int)screenHeight, plPos,centerPos,screenHeight/2,180,true);
                     players.add(pl);
            }

            if(players.size() == 2){
                //UP left
                Player pl = players.remove();
                         plPos = new Vector2(screenWidth/8,screenHeight/2);
                         centerPos =  new Vector2(0, screenHeight *1.35f);
                             pl.positionCardsHor((int)screenWidth, (int)screenHeight, plPos,centerPos,screenHeight/2,210,true);
                    players.add(pl);
                //UP right
                    pl = players.remove();
                        plPos = new Vector2(screenWidth*7/8,screenHeight/2);
                        centerPos =  new Vector2(screenWidth, screenHeight *1.35f);
                            pl.positionCardsHor((int)screenWidth, (int)screenHeight, plPos,centerPos,screenHeight/2,-210,true);
                    players.add(pl);
            }

            if(players.size() == 3) {
                System.out.println("3 Plyers");

                Player pl = players.remove();
                    plPos = new Vector2(screenWidth /8,screenHeight/2);
                    centerPos =  new Vector2(-screenWidth/2.5f, screenHeight /2);
                        pl.positionCardsVert((int)screenWidth, (int)screenHeight, plPos,centerPos,screenWidth/2.5f,-90,false);
                players.add(pl);

                pl = players.remove();
                    plPos = new Vector2(screenWidth/2,screenHeight/2);
                    centerPos =  new Vector2(screenWidth/2, screenHeight *14.4f/10);
                        pl.positionCardsHor((int)screenWidth, (int)screenHeight, plPos,centerPos,screenHeight/2,180,true);
                players.add(pl);

                pl = players.remove();
                    plPos = new Vector2(screenWidth/2,screenHeight/2);
                    centerPos =  new Vector2(screenWidth + screenWidth/3 , screenHeight / 2);
                        pl.positionCardsVert((int)screenWidth, (int)screenHeight, plPos,centerPos,screenWidth/2.5f,90,true);
                players.add(pl);
            }

        if(players.size() == 4) {
            //TODO implement this
        }

        if(players.size() == 5) {
            //TODO and this you lazy fuck
        }

    }

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
        resizeOpponents();

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


    public float getScreenWidth() {
        return screenWidth;
    }

    public float getScreenHeight() {
        return screenHeight;
    }
}
