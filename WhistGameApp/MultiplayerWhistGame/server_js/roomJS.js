module.exports = class Room{
    constructor(id,options) {
        this.roomID = id;
        this.loggedPlayers = [];

        this.MAX_CAPACITY = 6;
        if(options.MAX_CAPACITY != null)
            this.MAX_CAPACITY = options.MAX_CAPACITY;

        this.locked = false;
        if(options.locked != null)
            this.locked = options.locked;
    }

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


        this.loggedPlayers.push(player);
    }


    /**
     * Remove a player from room
     * @param playerID -> the socketID of the player;
     *
     */
    removePlayer(playerID){
        this.loggedPlayers = this.loggedPlayers.filter(value => value.id !== playerID);
    }

    getSize(){
        return this.loggedPlayers.length;
    }

    /**
     * Verify if a player is part of the room
     * @param playerID -> the socketID of the player we search for
     * @return: player if the player exists, otherwise null;
     */
    playerExists(playerID){
        return this.loggedPlayers.find(localPlayer => localPlayer.id === playerID);

    }


    toJSON(){
        var logged = []
        for(var p of this.loggedPlayers){
            logged.push(p.toJSON());
        }

        return {
            roomID: this.roomID,
            capacity: this.MAX_CAPACITY,
            players: logged
        };
    }

}
