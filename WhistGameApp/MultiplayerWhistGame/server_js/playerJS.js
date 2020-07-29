module.exports = class Player {
    constructor(id,nickname) {
        this.id = id;
        this.nickname = nickname;
    }

    toJSON(){
        return {
            nickname:this.nickname
        };
    }



}