package com.whist.game.generics;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.Screen;
import com.badlogic.gdx.graphics.g2d.TextureRegion;
import com.badlogic.gdx.math.Vector2;
import com.badlogic.gdx.scenes.scene2d.Actor;
import com.badlogic.gdx.scenes.scene2d.InputEvent;
import com.badlogic.gdx.scenes.scene2d.Touchable;
import com.badlogic.gdx.scenes.scene2d.ui.Image;
import com.badlogic.gdx.scenes.scene2d.utils.DragListener;
import com.badlogic.gdx.utils.viewport.Viewport;
import com.whist.game.StartClient;
import com.whist.game.scenes.GameScreen;

public class Card extends Actor implements Comparable<Card>{

    private static final float DRAG = 2.0f;
    private static final float MAX_SPEED = 2000.0f;
    private static final float FOLLOW_MULTIPLIER = 5.0f;
    public Vector2 position ;// pozitia cartii
    public Vector2 originalPosition;

    boolean following = false;
    boolean goingBack = false;
    public boolean choosed = false;

    Vector2 targetPosition = new Vector2(0f,0f);// finger position
    Vector2 velocity;// vector viteza al cartii

    private String Symbol;
    private int intSymbol; // asta da ordinea cartilor in mana
    private int value;
    private Actor cardActor;

    GameScreen gameScreen;

    //-----------------------------------------------------------------

    public String getSymbol() {
        return Symbol;
    }

    public int getValue() {
        return value;
    }

    public Actor getCardActor() {
        return cardActor;
    }

    public void init() {
        position = new Vector2(0,0);
        velocity = new Vector2(0,0);
    }

    public Card(String val, TextureRegion[][] regions, float x, float y, final GameScreen gameScreen){
        String[] cardVal = val.split("-");

        position = new Vector2(x,y);
        originalPosition = new Vector2(x,y);
        velocity = new Vector2(0,0);

        int cardSimb = 0;
        int cardNr = 0;

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
        if(cardVal[0].equals("b")){
            cardSimb = 4;
        }
        cardNr = Integer.parseInt( cardVal[1]) - 2;

        final Card thisCard = this;

        intSymbol = cardSimb;
        this.cardActor =  new Image(regions[cardSimb][cardNr]);
        cardActor.setPosition(originalPosition.x,originalPosition.y);
        cardActor.setX(60);
        cardActor.setY(100);
        cardActor.setTouchable(Touchable.enabled);
        cardActor.addListener(new DragListener() {
            @Override
            public boolean touchDown(InputEvent event, float x, float y, int pointer, int button) {
                targetPosition = new Vector2(cardActor.getX() + x, cardActor.getY() + y);
                following = true;
                goingBack = false;
                return true;
            }

            @Override
            public void touchDragged(InputEvent event, float x, float y, int pointer) {
                if (following ==  true){
                    targetPosition = new Vector2(cardActor.getX() + x,cardActor.getY() + y);
                   // System.out.println("touchDragged" + targetPosition.x + " " + targetPosition.y);
                }
                if(following == true && targetPosition.y > Gdx.graphics.getHeight()/2){
                    if(gameScreen.canChooseCard){
                        System.out.println("[Card] : Gdx.height = " + Gdx.graphics.getHeight());
                        originalPosition.y = Gdx.graphics.getHeight()/2;
                        //ToDo ceva .emmit() pentru o singura carte
                        //mainController.sendCard();
                    }

                }
                goingBack = false;
            }

            @Override
            public void touchUp(InputEvent event, float x, float y, int pointer, int button) {
                //super.touchUp(event, x, y, pointer, button);
                //System.out.println("[current poz]: " + position.toString());
                // System.out.println("[origin poz]: " + originalPosition.toString());
                targetPosition =  originalPosition;
                following = false;
                goingBack = true;
            }
        });
        cardActor.debug();

        this.Symbol = cardVal[0];
        this.value = cardNr;
        this.gameScreen = gameScreen;
    }

    public void render(){
        cardActor.setPosition(position.x,position.y);
    }

    public void update(float delta, Viewport viewport){
        if (following) {
            Vector2 followVector = new Vector2(targetPosition.x - position.x, targetPosition.y - position.y);
            velocity.x = FOLLOW_MULTIPLIER * followVector.x;
            velocity.y = FOLLOW_MULTIPLIER * followVector.y;
        }
        //TODO vezi ce faci cu asta
        if (goingBack) {
            Vector2 followVector = new Vector2(targetPosition.x - position.x, targetPosition.y - position.y);
            velocity.x = FOLLOW_MULTIPLIER * followVector.x;
            velocity.y = FOLLOW_MULTIPLIER * followVector.y;
        }
        velocity.clamp(0, MAX_SPEED);
        velocity.x = velocity.x - delta *DRAG *velocity.x;
        velocity.y = velocity.y - delta *DRAG *velocity.y;

        position.x = position.x + delta * velocity.x;
        position.y = position.y + delta * velocity.y;
        collideWithWalls(viewport.getWorldWidth(),viewport.getWorldHeight());

        cardActor.setPosition(position.x,position.y);
    }

    private void collideWithWalls( float viewportWidth, float viewportHeight) {
        if (position.x < 0) {
            position.x = 0;
            velocity.x = -velocity.x;
        }
        if (position.x + cardActor.getWidth() > viewportWidth ) {
            position.x = viewportWidth - cardActor.getWidth();
            velocity.x = -velocity.x;
        }
        if (position.y  < 0) {
            position.y = 0;
            velocity.y = -velocity.y;
        }
        if (position.y + cardActor.getHeight() > viewportHeight ) {
            position.y = viewportHeight - cardActor.getHeight();
            velocity.y = -velocity.y;
        }
    }

    public void scale(float x, float y){
        cardActor.setX(x);
        cardActor.setY(y);
    }

    public void setRot(float rot){
        cardActor.setRotation(rot);
    }

    public void setPosition(float x, float y){
        cardActor.setX(x);
        cardActor.setY(y);
    }

    @Override
    //ToDo de revizitat asta (merge da' nu e perfecta)
    public int compareTo(Card card) {
        if(this.intSymbol < card.intSymbol){
            if(this.value < card.value){
                return 1;
            }
            else{
                return -1;
            }
        }else {
           return 1;

        }
    }

    @Override
    public String toString() {
        return "Card{" +
                "Symbol='" + Symbol + '\'' +
                ", value=" + value +
                '}';
    }
}
