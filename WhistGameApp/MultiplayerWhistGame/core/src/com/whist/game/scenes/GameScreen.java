package com.whist.game.scenes;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.Screen;
import com.badlogic.gdx.graphics.GL20;
import com.badlogic.gdx.graphics.Texture;
import com.badlogic.gdx.graphics.g2d.SpriteBatch;
import com.badlogic.gdx.graphics.g2d.TextureRegion;
import com.badlogic.gdx.math.CatmullRomSpline;
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

import java.beans.beancontext.BeanContextChild;
import java.util.ArrayList;
import java.util.Collection;
import java.util.Collections;
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

    Card[] crd;
    List<String> cardsStrList =  new ArrayList<>();
    List<Card> cards = new ArrayList<>();

    Card gen;


    public void init() {

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

//        Gdx.input.setInputProcessor(biddingStage);
//
//        Not YET
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
//        biddingStage.addActor(table);
//         biddingStage.addActor(bidButton);
//         stage.addActor(card);
        //crd = new Card[8];
//        crd[0] = new Card("b-2",regions);//back of the card
        //stage.addActor(crd[0].getCardActor());

//        Card c1 = new Card("h-2",regions);
//        Card c2 = new Card("d-2",regions);
//        Card c3 = new Card("c-2",regions);
//        Card c4 = new Card("s-2",regions);

//        cards.add(c1);
//        cards.add(c2);
//        cards.add(c3);
//        cards.add(c4);

        cardsStrList.add("h-2");
        cardsStrList.add("d-2");
        cardsStrList.add("c-2");
        cardsStrList.add("s-2");
        System.out.println(cardsStrList);
        gen = new Card("h-4",regions,viewport.getScreenWidth()/2 ,100);
        stage.addActor(gen.getCardActor());

        cards = initCards(cardsStrList);
        addCardsToScene(cards,stage);


//ToDO init cards

    }

    public List<Card> initCards(List<String> cards){
        float rot = cards.size()*5/2;
        int i = 0;
        List<Card> ret = new ArrayList<>();
        Card card;

        if(cards.size() >8){
            throw new IndexOutOfBoundsException("More then 8 cards Impossible");
        }

        Collections.sort(cards);

        for (String str:cards) {
            //ToDo gen ar merge si validate
             card =  new Card(str,regions,viewport.getScreenWidth()/2 + i,5);
            ret.add(card);
            i += 25;
        }
        return ret;
    }

    public void addCardsToScene(List<Card> cards,Stage stage){
        for (Card crd:cards) {
            stage.addActor(crd.getCardActor());
        }
    }

    //TODO lista de Stringuri pe care le transforma in Cards
    public void renderHUD(float delta, List<Card> cards){
        for (Card crd:cards) {
            crd.update(delta,viewport);
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

        //TODO update function for all HUDCards

        //crd[0].update(delta,viewport);
        //TODO setPosition for only one of them
        gen.update(delta,viewport);
        //crd.render();
        renderHUD(delta,cards);
        stage.act(delta);
        stage.draw();

    }



    public void initGraphics(){
        cardBack = new TextureRegion(cardSprite,648,0,587,81);
        regions = TextureRegion.split(cardSprite, 81, 117);
    }

    //TODO de combinat tot cacatul asta intr-o singura functie
    public void renderOpponentsHandTop(int nrOfCards,int x,int y, float rot){
//        //TODO am nevoie de o lsita de playeri cu macar numle si nuamrul de carti pe care il au in mana
//        float rotOff = cards.size()*5/2;
//        int i = 0;
//        for (int c = 0;c<= nrOfCards;c++ ) {
//            renderCardBack(x-nrOfCards*20 +i,y ,-rot - rotOff);
//            i+=25;
//            rotOff-=5;
//        }
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
