var Player = require('./playerJS')
var Room = require('./roomJS')

module.exports = class Service{
    constructor(roomRepo) {
        this.roomRepo = roomRepo;
        this.loggedPlayers = [];
        this.loggedPlayersSize = 0;
    }

    login(jsonData){
        let socketID = jsonData.socketID;
        this.loggedPlayers[socketID] = {nickname: null, room: null};
        this.loggedPlayersSize += 1;
    }

    logout(jsonData){
        let socketID = jsonData.socketID;
        this.loggedPlayers[socketID] = null;
        this.loggedPlayersSize -= 1;
    }

    createRoom(jsonData){

        var socketID = jsonData.id;
        var nickname = jsonData.nickname;
        var roomID = jsonData.roomID;

        //this.loggedPlayers[socketID] = {name: nickname,room : null};

        if(socketID == null || nickname == null || roomID == null)
            throw "jsonData is not valid";

        console.log(jsonData);
        var player = new Player(socketID,nickname);
        var room = new Room(roomID,{});
        console.log("Created room " + room);
        room.addPlayer(player);
        console.log("Added player: " + player)

        try {
            this.roomRepo.createRoom(room);
            this.loggedPlayers[socketID].nickname = nickname;
            this.loggedPlayers[socketID].room = roomID;

            console.log(this.loggedPlayers[socketID]);
        }catch (e) {
            //this.loggedPlayers[socketID] = null;
            throw e;
        }

    }


    leaveRoom(playerID){
        var roomID = this.loggedPlayers[playerID].room;
        if(roomID == null)
            throw "This player is already logged out!";

        var room = this.roomRepo.getRoom(roomID);
        if (room == null)
            throw "Room doesn't exist!";

        room.removePlayer(playerID);

        if(room.getSize() === 0)
            this.roomRepo.disposeRoom(roomID);
        else
            this.roomRepo.updateRoom(room);


        this.loggedPlayers[playerID] = {nickname: null, room: null};
        

    }

    joinRoom(jsonData){
        var socketID = jsonData.id;
        var nickname = jsonData.nickname;
        var roomID = jsonData.roomID;

        var room = this.roomRepo.getRoom(roomID);
        if(room == null)
            throw "Room doesn't exists!";

        try {
            room.addPlayer(new Player(socketID, nickname));
            this.roomRepo.updateRoom(room);
          
            this.loggedPlayers[socketID].nickname = nickname;
            this.loggedPlayers[socketID].room = roomID;
           
        }catch (e) {
            this.loggedPlayers[socketID] = null;
            //console.log(e.message);
            throw e;
        }

    }

    getRoomForPlayer(playerID){
        if (this.loggedPlayers[playerID] == null)
            return null;

        var roomID = this.loggedPlayers[playerID].room;
        if(roomID == null)
            return null;

        return this.roomRepo.getRoom(roomID);
    }


    getLoggedPlayersSize(){
        return this.loggedPlayersSize;
    }

    getRoomRepo(){
        return this.roomRepo;
    }

    getRoom(roomID){
        return this.roomRepo.getRoom(roomID);
    }

    getNumberOfRooms(){
        return this.roomRepo.getSize();
    }

    showStats(){
        console.log("Connected clients: " + this.loggedPlayersSize);
        console.log("Available rooms: " + this.roomRepo.getSize());
    }

}