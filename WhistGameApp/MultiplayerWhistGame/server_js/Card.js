module.exports = class Card{
    constructor(type,value){
        this.type = type;
        this.value = value;
    }

    toJSON(){
        var str = this.type + "-" + this.value;
        return {card: str};
    }
}