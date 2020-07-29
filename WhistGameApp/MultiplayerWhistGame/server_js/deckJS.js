
var Card = require('./Card');


module.exports =  class Deck{
    constructor(num_of_players){
        this.num_of_players = num_of_players;
        this.cards = this.createDeck(num_of_players);
        this.disposedCards = [];
    }

    createDeck(num_of_players){
        var types = ['d','s','h','c'];
        var max_value = 14;

        var total_of_cards = 8*num_of_players;
        var cards = []
        while(cards.length < total_of_cards){
            for(var type of types){
                var str = type + "-" + max_value
                cards.push(str);
            }

            max_value -= 1;
        }

        return cards;
      }

    remakeDeck(){
        for(var c of this.disposedCards){
            this.cards.push(c);
        }

        this.disposedCards = []
    
    };


    shuffleDeck(){
        this.cards.sort(()=> Math.random() - 0.5 );
    }


    drawCards(num_of_cards){
        var drawCards = [];
        for(var i = 0; i < num_of_cards; i++){
            var card = this.cards.shift();
            drawCards.push(card);
            this.disposedCards.push(card);
        }

        return drawCards;
    }

    getSizeOfDeck(){
        return this.cards.length;
    }

}