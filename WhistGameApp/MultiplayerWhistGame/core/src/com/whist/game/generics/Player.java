package com.whist.game.generics;

public class Player {

    private String nickName;


    public Player(String nickName) {
        this.nickName = nickName;
    }

    @Override
    public String toString() {
        return "{" +
                "nickName='" + nickName + '\'' +
                '}';
    }

    public String getNickName() {
        return nickName;
    }

    public void setNickName(String nickName) {
        this.nickName = nickName;
    }
}
