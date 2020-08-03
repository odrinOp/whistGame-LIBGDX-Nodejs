package com.whist.game.generics;

public class Player {

    private String nickName;


    public Player(String nickName) {
        this.nickName = nickName;
    }



    public String getNickName() {
        return nickName;
    }

    public void setNickName(String nickName) {
        this.nickName = nickName;
    }
}
