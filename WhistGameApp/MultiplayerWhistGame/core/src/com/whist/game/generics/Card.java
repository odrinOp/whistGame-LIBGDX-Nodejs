package com.whist.game.generics;

import com.badlogic.gdx.scenes.scene2d.Actor;

//TODO de extins Actor?
public class Card extends Actor {

    private String value;



    public void setValue(String value) {
        this.value = value;
    }

    public Card(String val){
        this.value = val;

    }

    public void render(){

    }


    public String getValue() {
        return value;
    }
}
