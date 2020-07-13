module.exports = class Room {
    constructor(roomID,owner) {

        this._loggedPlayers = [owner];
        this._roomID = roomID;
        this._owner = owner; // "ACTIVE, CANCELED,COMPLETED,CLOSED"

        /*ACTIVE -> The room is open to everyone to join;
          CANCELED -> The host left the room; The room will be destroyed!
          COMPLETED -> The game ended;
          CLOSED -> The room is closed for new players;(the match already started);
        */
    }
    get roomID() {
        return this._roomID;
    }

    set roomID(value) {
        this._roomID = value;
    }

    get owner() {
        return this._owner;
    }

    set owner(value) {
        this._owner = value;
    }

    get loggedPlayers(){
        return this._loggedPlayers;
    }

    set loggedPlayers(value){
        this._loggedPlayers = value;
    }

    addPlayer(player){
        if(this._loggedPlayers.length == 6)
            throw "Room is full!";

        for(var localPlayer in this.__loggedPlayers){
            if(localPlayer.nickname() === player.nickanme())
                throw "Nickname already in use!";
        }

        this._loggedPlayers.push(player);


    }

    getPlayer(playerID){
        for(var player in this._loggedPlayers){
            if(player.socketID() === playerID)
                return player;
        }

        return null;
    }

}
