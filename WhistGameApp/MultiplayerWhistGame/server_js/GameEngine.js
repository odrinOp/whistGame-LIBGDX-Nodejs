var Deck = require('./deckJS');
module.exports = class GameEngine{
    constructor(players)
    {
        this.players = players;
        this.deck = new Deck(players.length);
        this.current_round = 1;
        this.max_round = 2;
        this.num_of_cards = 8;
        this.stage = "drawCards";
        
        this.orderedPlayers = players;
        this.bids = [];


        
    }

    restartRound(){
        this.deck.remakeDeck();
        this.deck.shuffleDeck();
        this.bids = [];
    }

    drawCardsForAllPlayers(){

        this.restartRound();
        var bigData = [];
        for(var p of this.players){
            var cards = this.deck.drawCards(this.num_of_cards);
            var data = {player: p.nickname, cards};
            bigData.push(data);
        }

        this.stage = "bid";
        return bigData;
    }

    bidSystemForPlayers(bid){
        for(var p of this.players){
            if(p.)
        }

    }

    engine(bid = 0){
        if(this.stage === "drawCards")
            return this.drawCardsForAllPlayers();
        
        if(this.stage === "bid")
            return this.bidSystemForPlayers(bid);
    }









}