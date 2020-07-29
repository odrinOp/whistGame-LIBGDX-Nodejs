var GameEngine = require('./GameEngine');

module.exports = class Room{
    constructor(id,options) {
        this.roomID = id;
        this.loggedPlayers = [];
        
        this.owner = null;
        this.MAX_CAPACITY = 6;
        this.locked = false;

        this.gameEngine = null;
        this.configureOptions(options);
        
    };

    configureOptions(options){
        if(options.MAX_CAPACITY != null)
            this.MAX_CAPACITY = options.MAX_CAPACITY;

        if(options.locked != null)
            this.locked = options.locked;

    };

    createGameEngine(){
        this.gameEngine = new GameEngine(this.loggedPlayers);
    };

    getGameEngine(){
        return this.gameEngine;
    };

    /**
     * Add a new player to room
     * @param player -> the new player to add
     * @throws: "Nickname already in use!" -> if someone else used this nickname;
     * @throws ""Room is full!"" -> if the capacity of the room is equal to 6;
     */
    addPlayer(player){
        const found = this.loggedPlayers.find(localPlayer => localPlayer.nickname === player.nickname);
        if(found != null)
            throw "Nickname already in use!";


        if(this.loggedPlayers.length === this.MAX_CAPACITY)
            throw "Room is full!";

        if(this.owner == null)
            this.owner = player;

        this.loggedPlayers.push(player);
    };


    /**
     * Remove a player from room
     * @param playerID -> the socketID of the player;
     *
     */
    removePlayer(playerID){
        this.loggedPlayers = this.loggedPlayers.filter(value => value.id !== playerID);
        
        if(this.owner != null && this.owner.id === playerID){
            this.owner = null;
            this.chooseAnotherOwner();
        }
    };

    chooseAnotherOwner(){
        if(this.loggedPlayers.length > 0)
            this.owner = this.loggedPlayers[0];
    };

    getSize(){
        return this.loggedPlayers.length;
    };

    /**
     * Verify if a player is part of the room
     * @param playerID -> the socketID of the player we search for
     * @return: player if the player exists, otherwise null;
     */
    playerExists(playerID){
        return this.loggedPlayers.find(localPlayer => localPlayer.id === playerID);

    };
    
    

    toJSON(){
        var logged = []
        for(var p of this.loggedPlayers){
            logged.push(p.toJSON());
        }

        return {
            roomID: this.roomID,
            capacity: this.MAX_CAPACITY,
            players: logged,
            owner:this.owner.nickname
            
        };
    };

    toJSON2(){
        var loggedPlayers = this.loggedPlayers.length;
        return{
            roomID: this.roomID,
            capacity: this.MAX_CAPACITY,
            players: loggedPlayers,
        };
    };

}
