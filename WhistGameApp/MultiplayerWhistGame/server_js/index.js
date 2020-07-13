var app = require('express')();
var server = require('http').Server(app);
var io = require('socket.io')(server);

var playerJS = require('./player.js');
var roomRepoJS = require('./roomRepo.js');
var roomJS = require('./room.js');


server.listen(8080,function () {
	// body...
	console.log("Server is now running on port 8080...");
});



var roomRepo = new roomRepoJS();


io.on('connection',function(socket){
	//id = socket.id;
	console.log("Client connected! ID: " + socket.id);
	socket.emit("socketID", {id: socket.id});

    socket.on("createRoom",data=>{
        var player = new playerJS(data.nickname,data.socketID)
        var room = new roomJS(data.roomID,player);
        try {
			roomRepo.addRoom(room);
			socket.join(room.roomID);

			socket.emit("lobbyData",{roomID: room.roomID, owner: room.owner.nickanme(), players: {nickname : room.owner.nickanme() }});

		}catch (e) {
			socket.emit("createRoomError", {msg: e});
		}

    })

	socket.on("joinRoom",data=>{
		var player = new playerJS(data.nickname,data.socketID);
		//var roomID =
	})

	socket.on('disconnect',function(){

       
	});






});


