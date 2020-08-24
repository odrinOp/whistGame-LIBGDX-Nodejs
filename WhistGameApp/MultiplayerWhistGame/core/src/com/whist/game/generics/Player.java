package com.whist.game.generics;

import com.badlogic.gdx.math.Vector2;
import com.badlogic.gdx.scenes.scene2d.Actor;
import com.badlogic.gdx.scenes.scene2d.Touchable;

import java.util.ArrayList;
import java.util.List;

import javax.jws.Oneway;

import static java.lang.Math.abs;
import static java.lang.Math.min;
import static java.lang.Math.sqrt;

public class Player extends Actor {

    private String nickName;
    private int nrOfCards;
    private Vector2 pos;


    public List<Card> cards = new ArrayList<>();


    public Player(String nickName, int nrOfCards) {
        this.nickName = nickName;
        this.nrOfCards = nrOfCards;
    }

    //todo pot fi combinate in aceeasi funtie in functie de cele 2 cadrane in care se deseneaza
    //todo in functie de unde e playerul fata de centrul cercului
    public void positionCardsHor(int width, int height,Vector2 plPos,Vector2 centerPos,float R,float rotation, boolean inverse){
        float xOffset =  width / 45;
        float rotOffset = 3;
        float rot;

        boolean portrait = false;
        boolean landscape = false;

        float cardWidth = min(height,width) /4 * 0.6f;
        float cardHeight = min(height,width) /4;


        if (height > width){
            portrait = true;
        }else{
            landscape = true;
        }

        float x = plPos.x - cards.size()*xOffset/2;
        float y ;
        rot =  cards.size()/2 * rotOffset;
        for (Card crd : cards) {

            if(inverse){
                y = (float) (-sqrt(abs(R*R - (x-centerPos.x)*(x-centerPos.x))) + centerPos.y);
            }else{
                y = (float) (sqrt(abs(R*R - (x-centerPos.x)*(x-centerPos.x))) + centerPos.y);
            }

            crd.getCardActor().setWidth(cardWidth*3/4);
            crd.getCardActor().setHeight(cardHeight*3/4);
            crd.getCardActor().setOriginX(cardWidth*3/4/2);
            crd.rePosition(x,y);
            crd.setRot( - rot + rotation );
            crd.setTouchable(Touchable.disabled);
            rot -= rotOffset;
            x += xOffset;
        }
    }

    public void positionCardsVert(int width, int height,Vector2 plPos,Vector2 centerPos,float R,float rotation, boolean inverse){

        float xOffset =  width / 45;
        float rotOffset = 3;
        float rot;

        boolean portrait = false;
        boolean landscape = false;

        float cardWidth = min(height,width) /4 * 0.6f;
        float cardHeight = min(height,width) /4;


        if (height > width){
            portrait = true;
        }else{
            landscape = true;
        }

        //float x = width/2 - cards.size()*xOffset/2;
        float x ;
        float y = plPos.y - cards.size()*xOffset/2;
        rot =  cards.size()/2 * rotOffset;
        for (Card crd : cards) {

            if(inverse){
                x = (float) (-sqrt(abs(R*R - (y-centerPos.y)*(y-centerPos.y))) + centerPos.x);
                crd.setRot( rot + rotation );
            }else{
                x = (float) (sqrt(abs(R*R - (y-centerPos.y)*(y-centerPos.y))) + centerPos.x);
                crd.setRot( - rot + rotation );
            }

            crd.getCardActor().setWidth(cardWidth*3/4);
            crd.getCardActor().setHeight(cardHeight*3/4);
            crd.getCardActor().setOriginX(cardWidth*3/4/2);
            crd.rePosition(x,y);
            crd.setTouchable(Touchable.disabled);
            rot -= rotOffset;
            y += xOffset;
        }
    }


    public int getNrOfCards() {
        return nrOfCards;
    }

    public void setNrOfCards(int nrOfCards) {
        this.nrOfCards = nrOfCards;
    }

    public String getNickName() {
        return nickName;
    }

    public void setNickName(String nickName) {
        this.nickName = nickName;
    }

    public Vector2 getPos() {
        return pos;
    }

    public void setPos(Vector2 pos) {
        this.pos = pos;
    }

    @Override
    public String toString() {
        return "Player{" +
                "nickName='" + nickName + '\'' +
                ", nrOfCards=" + nrOfCards +
                '}';
    }
}
