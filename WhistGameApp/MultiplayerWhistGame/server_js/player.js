module.exports = class Player{
    constructor(nickname,socketId){
        this.__nickname = nickname;
        this.__socketId = socketId;
        this.__roomID = null;
    }

    isEqual(other){
        return other.socketId == this.__socketId && other.nickname == this.__nickname;
    }

    toJSON(){
        return {nickname: this.__nickname, id: this.__socketId};
    }

    nickanme(){
        return this.__nickname;
    }

    socketID(){
        return this.__socketId;
    }

    nickname(new_nickname){
        this.__nickanme = new_nickname;
    }

    roomID(){return this.__roomID;}
    roomID(roomID){this.__roomID = roomID;}

};