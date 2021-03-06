$(function () {
    "use strict";

    var content = $('#content');
    var input = $('#input');
    var status = $('#status');
    var myName = false;
    var author = null;
    var logged = false;
    var socket = $.atmosphere;

    // We are now ready to cut the request
    var request = { url: 'async/api/v1/chat?lectureId='+chatId,
        contentType : "application/json",
        logLevel : 'debug',
        transport : 'websocket' ,
        enableProtocol : true,
        headers : {"eduSecureToken":secureToken, "senderName": senderName},
        fallbackTransport: 'long-polling'};


    request.onOpen = function(response) {
        content.html($('<p>', { text: 'Atmosphere connected using ' + response.transport }));
        input.removeAttr('disabled').focus();
        status.text('Print message:');
    };


    request.onTransportFailure = function(errorMsg, request) {
        jQuery.atmosphere.info(errorMsg);
        if ( window.EventSource ) {
            request.fallbackTransport = "sse";
        }
    };

    request.onReconnect = function (request, response) {
        socket.info("Reconnecting")
    };

    request.onMessage = function (response) {
        var message = response.responseBody;
        try {
            var json = jQuery.parseJSON(message);
        } catch (e) {
            console.log('This doesn\'t look like a valid JSON: ', message.data);
            return;
        }
            input.removeAttr('disabled').focus();
            
            if(jQuery.isArray(json)){
            	jQuery.each(json, function(idx, obj) {
                	if(obj.isAuthorized == true && obj.messageType === "chatMessage"){
                		var me = obj.author == author;
                		addMessage(obj.author, obj.message, me ? 'blue' : 'black');
                	} 
                });
            } else {
            	if(json.isAuthorized == true && json.messageType === "chatMessage"){
            		var me = json.author == author;
            		addMessage(json.author, json.message, me ? 'blue' : 'black');
            	}            	
            }

    };

    request.onClose = function(response) {
        logged = false;
    }

    request.onError = function(response) {
        content.html($('<p>', { text: 'Sorry, but there\'s some problem with your '
            + 'socket or the server is down' }));
    };

    var subSocket = socket.subscribe(request);

    input.keydown(function(e) {
        if (e.keyCode === 13) {
            var msg = $(this).val();

            subSocket.push(jQuery.stringifyJSON({ author: senderName, message: msg }));
            $(this).val('');

            input.attr('disabled', 'disabled');
            if (myName === false) {
                myName = msg;
            }
        }
    });

    function addMessage(author, message, color) {
        content.append('<p><span style="color:' + color + '">' + author + '</span> @ ' + ': ' + message + '</p>');
    }
});
