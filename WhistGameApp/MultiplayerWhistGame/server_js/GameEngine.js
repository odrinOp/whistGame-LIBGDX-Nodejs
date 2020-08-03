var Deck = require('./deckJS');
module.exports = class GameEngine{
    constructor(players)
    {
        this.players = players;
        this.max_round = 2;
        this.current_round = 1;
        this.num_of_cards = 8;
        this.deck = new Deck(players.length);
        this.state = 0;
    }

    getState(){
        switch (state){
            case 0:
                return "not_started";
            case 1:
                return "started";
            case 2:
                return "finished";
            default:
                return "canceled";
        };
    }


    getPlayers(){
        return this.players;
    }


}