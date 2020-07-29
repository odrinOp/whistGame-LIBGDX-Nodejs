var app = require('express')();
var server = require('http').Server(app);
var io = require('socket.io')(server);
//var ngrok = require('ngrok');
//var localtunnel1 = require('localtunnel');
var Player = require('./playerJS');
var Room = require('./roomJS');
var RoomRepo = require('./roomRepo');
var Service = require('./serviceJS');
var Deck = require('./deckJS');




function testService() {
    var roomRepo = new RoomRepo();
    var service = new Service(roomRepo);

    var jsonData = {id: 1, nickname: "aaa", roomID :"abc"};
    service.createRoom(jsonData);

    var jsonData = {id: 2, nickname: "bbb", roomID :"cba"};
    service.createRoom(jsonData);

    var jsonData = {id: 3, nickname: "ccc", roomID :"ggd"};
    service.createRoom(jsonData);

    var jsonData = {id: 4, nickname: "fff", roomID :"ggd"};
    service.joinRoom(jsonData);

    console.log("Number of created rooms: " + service.getNumberOfRooms());
    console.log("Number of logged players: " + service.getLoggedPlayersSize());

    console.log("Player with id 1 want to leave");

    service.leaveRoom(1);
    console.log("Number of created rooms: " + service.getNumberOfRooms());
    console.log("Number of logged players: " + service.getLoggedPlayersSize());

    var room = service.getRoomForPlayer(2);
    console.log(room.toJSON());


}
function testRoomRepo() {
    var roomRepo = new RoomRepo();

    roomRepo.createRoom(new Room("aaa"));
    roomRepo.createRoom(new Room("bbb"));
    roomRepo.createRoom(new Room("ccc"));
    roomRepo.createRoom(new Room("ddd"));
    roomRepo.createRoom(new Room("eee"));

    console.log(roomRepo.toJSON());


}
function testRoom(){
    var room = new Room("aaa");
    console.assert(room.getSize() === 0);

    var p1 = new Player(1,"odrin");
    var p2 = new Player(2,"aaa");
    var p3 = new Player(3,"bbb");
    var p4 = new Player(4,"ccc");
    var p5 = new Player(5,"ddd");
    var p6 = new Player(6,"eee");
    //var p7 = new Player(7,"fff");

    room.addPlayer(p1);
    room.addPlayer(p2);
    room.addPlayer(p3);
    room.addPlayer(p4);
    room.addPlayer(p5);
    room.addPlayer(p6);
   // room.addPlayer(p7);

    //console.assert(room.getSize() === 3);

    room.removePlayer(p1.id);
    room.removePlayer(12223);
    //console.assert(room.getSize() === 2);

    console.log(room.toJSON())


}
function testPlayer() {
    var p1 = new Player(1,"odrin");
    var p2 = new Player(2,"aaa");
    var p3 = new Player(3,"ccc");

    console.log(p1.toJSON());
    console.log(p2.toJSON())
    console.log(p3.toJSON())
}
function test(){

    //testService()
    //testRoomRepo()
    //testRoom();
    //testPlayer()
    testDeck();
}

function testDeck(){
    var deck = new Deck(4);
    console.log("Size of the deck")
    console.log("Expected: " + (4*8));
    console.log("Current: " + deck.getSizeOfDeck());


    deck.shuffleDeck();
    console.log("First draw ");
    var cards = deck.drawCards(8);
    console.log(cards);
    
    console.log("Second draw");
    cards = deck.drawCards(8);
    console.log(cards);


    console.log("Size of the deck")
    console.log("Expected: " + (2*8));
    console.log("Current: " + deck.getSizeOfDeck());

    deck.remakeDeck();
    console.log("Size of the deck")
    console.log("Expected: " + (4*8));
    console.log("Current: " + deck.getSizeOfDeck());


}

 //test();

var roomRepo = new RoomRepo();
//console.log("Number of rooms: " + roomRepo.getSize());

var service = new Service(roomRepo);



server.listen(8080,function () {
    console.log("Server is running at port 8080");
});




io.on('connection',socket=>{
    console.log("New client connected: ID:" + socket.id);
    service.login({socketID: socket.id});
    socket.emit("connected");

    socket.on('createRoom',data=>{
        var jsonData = {id: socket.id,nickname:data.nickname,roomID: data.roomID};
        console.log('Player ' + data.nickname + 'want to create room ' +  data.room);
        try{
            
            service.createRoom(jsonData);
            socket.join(data.roomID);

            var room = service.getRoomForPlayer(socket.id);
            socket.emit("lobbyData",room.toJSON());
            console.log(room.toJSON());

            service.showStats();

        }catch (e) {
            socket.emit("errorMessage",{msg: e});
        }
    });

    socket.on('joinRoom', data=>{
        var jsonData = {id: socket.id,nickname:data.nickname,roomID: data.roomID};
        console.log("Player " + data.nickname +" wants to enter to room " + data.roomID)
        try{
            service.joinRoom(jsonData);
            socket.join(data.roomID);

            var room = service.getRoomForPlayer(socket.id);
            io.to(data.roomID).emit("lobbyData",room.toJSON());
            console.log(room.toJSON());
            service.showStats();
        }catch (e) {
            console.log("Error on joining room " + e);
            socket.emit("errorMessage",{msg: e});
        }
    });

    socket.on('getRoomsRQ',()=>{
        var jsonData = service.getAllRooms();
        console.log('player with socketID: ' + socket.id + ' want to get info about all the rooms');
        console.log(jsonData);
        socket.emit('getRoomsRP',jsonData);
    })

    socket.on('leaveRoom',()=>{
        try{
            var room = service.getRoomForPlayer(socket.id);
            if(room != null){
                service.leaveRoom(socket.id);
                socket.leave(room.roomID);
                io.to(room.roomID).emit("lobbyData",room.toJSON());
            }
            
        }catch (e){
            socket.emit("errorMessage",{msg:e});
        }
    })

    socket.on('ready',()=>{
        var socketID = socket.id;
        
        testingWhile(socket);
    })
    socket.on('disconnect',()=>{
        try{

            var room = service.getRoomForPlayer(socket.id);
            if (room != null)
                {    
                    service.leaveRoom(socket.id);;
                    socket.leave(room.roomID);
                    io.to(room.roomID).emit("lobbyData",room.toJSON());
                    
                }
            service.logout({socketID: socket.id});
            console.log("Client " + socket.id + " disconnected" );
            

        
            
            service.showStats();

        }catch (e) {
            console.log("Error on disconnect!" + e);
            socket.emit("errorMessage",{msg:e});
        }
    });

});


function testingWhile(socket){
     var x = 0
     console.log("Client " + socket.id + " is testing while");
    var flag = true
     while(flag){

        socket.on('disconnect',()=>{
            flag = false
        })
        x += 1;
        if(x === 100)
            x = 0;
    }
}

