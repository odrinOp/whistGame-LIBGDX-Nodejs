package com.whist.game.repositories;

import com.whist.game.generics.Room;

import java.util.Comparator;
import java.util.LinkedList;
import java.util.List;


public class RoomRepo {
    private List<Room> list;

    public RoomRepo() {
        this.list = new LinkedList<>();
    }

    public void addRoom(Room room){
        list.add(room);

    }


    public void clear(){
        list = new LinkedList<>();
    }

    /**
     *Return a list with all the rooms which have a specific charSequence
     * @param name -- the charSequence
     * @return --all the rooms if name === ""; or the filtered list
     */
    public List<Room> filterByName(String name){
        if(name.equals(""))
            return list;
        List<Room> filteredRooms = new LinkedList<>();
        for (Room r: this.list){
            if(r.getRoomID().contains(name))
                filteredRooms.add(r);
        }
        return filteredRooms;
    }

    /**
     * Return a list with all rooms which a player can enter; (eg. there are 5 players in the room and the cap is 6);
     *
     * @return all the rooms available and open
     */
    public List<Room> filterByCapacity(){
        List<Room> filteredRooms = new LinkedList<>();
        for (Room r: this.list){
            if(r.getNrOfPlayers()< r.getMaxCapacity())
                filteredRooms.add(r);
        }
        return filteredRooms;
    }





}
