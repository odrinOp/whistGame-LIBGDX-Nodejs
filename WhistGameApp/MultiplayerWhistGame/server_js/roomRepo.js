module.exports = class RoomRepo {
    constructor() {
        this.__list = [];
    }

    addRoom(room){
        if (this.__exists(room))
            throw "Room ID already exists!";
        this.__list.push(room);
    }

    __exists(room) {
        for (var localRoom in this.__list){
            if(localRoom.roomID() == room.roomID())
                return true;
        }
        return false;
    }

    getRoom(roomID){
        for(var room in this.__list){
            if(room.roomID() == roomID)
                return room;
        }
        return null;
    }

    getPlayerRoom(playerID){
        for(var room in this.__list){
            var player = room.getPlayerId();
            if(player != null)
                return room.roomID();
        }
        return null;
    }

    addPlayerToRoom(player,roomID){
        var room = this.getRoom(roomID);
        if(room != null);
        room.
    }







}