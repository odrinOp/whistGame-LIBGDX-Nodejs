package com.whist.game.generics;

public class Room {

    private String roomID;
    private int maxCapacity;
    private int nrOfPlayers;

    public Room(){};

    public Room(String roomID, int maxCapacity, int nrOfPlayers) {
        this.maxCapacity = maxCapacity;
        this.nrOfPlayers = nrOfPlayers;
        this.roomID = roomID;
    }

    public String getRoomID() {
        return roomID;
    }

    public void setRoomID(String roomID) {
        this.roomID = roomID;
    }

    public int getMaxCapacity() {
        return maxCapacity;
    }

    public void setMaxCapacity(int maxCapacity) {
        this.maxCapacity = maxCapacity;
    }

    public int getNrOfPlayers() {
        return nrOfPlayers;
    }

    public void setNrOfPlayers(int nrOfPlayers) {
        this.nrOfPlayers = nrOfPlayers;
    }

    @Override
    public String toString() {
        return "Room{" +
                "roomID='" + roomID + '\'' +
                ", maxCapacity=" + maxCapacity +
                ", nrOfPlayers=" + nrOfPlayers +
                '}';
    }
}
