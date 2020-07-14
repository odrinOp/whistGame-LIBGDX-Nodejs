var Room = require("./roomJS");
module.exports = class RoomRepo{
    constructor(options) {
        this.rooms = [];
        this.createRoom(new Room("test",{locked : true}));
        //this.locked = true;

    }

    createRoom(room){
        const found = this.rooms.find(localRoom => localRoom.roomID === room.roomID);
        if(found != null)
            throw "The name of the room already exists!";

        this.rooms.push(room);
        console.log("New Room Created!");
    }

    getRoom(roomID){
        return this.rooms.find(localRoom => localRoom.roomID === roomID);
    }

    updateRoom(room){
        for(var localRoom of this.rooms){
            if(localRoom.roomID === room.roomID) {
                localRoom = room
                return;
            }
        }
    }

    getSize(){
        return this.rooms.length;
    }


    disposeRoom(roomID){
        this.rooms = this.rooms.filter(localRoom => localRoom.roomID !== roomID ||
                                                    localRoom.roomID === roomID && localRoom.locked);
    }

    toJSON(){
        var data = {};
        data.num_of_rooms = this.rooms.length;
        var roomsData = [];
        for(var localRooms of this.rooms){
            roomsData.push(localRooms.toJSON());
        }

        data.rooms = roomsData;
        return data;

    }

}