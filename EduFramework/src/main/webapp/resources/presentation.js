$(function () {

    var next = $('#next');
    var prev = $('#prev');
    var restart = $('#restart');
    var currentStepContainer = $('#currentStep');
    var stepCountContainer = $('#stepCount');

    var socket = $.atmosphere;

    // We are now ready to cut the request
    var request = { url: 'async/api/v1/presentation?lectureId='+chatId,
        contentType : "application/json",
        logLevel : 'debug',
        transport : 'websocket' ,
        enableProtocol : true,
        headers : {"eduSecureToken":secureToken},
        fallbackTransport: 'long-polling'};


    request.onOpen = function(response) {
    };

    request.onTransportFailure = function(errorMsg, request) {
        jQuery.atmosphere.info(errorMsg);
        if ( window.EventSource ) {
            request.fallbackTransport = "sse";
        }
    };

    request.onReconnect = function (request, response) {
        socket.info("Reconnecting");
    };

    request.onMessage = function (response) {
        var message = response.responseBody;
        try {
            var json = jQuery.parseJSON(message);
        } catch (e) {
            console.log('This doesn\'t look like a valid JSON: ', message.data);
            return;
        }
            
            if(json.messageType === "presentationStatusMessage"){
                var currentStep = json.currentStep;
                var stepCount = json.stepCount;
                changeStep(currentStep, stepCount);
            }
    };

    request.onClose = function(response) {
        logged = false;
    };

    request.onError = function(response) {
    	alert("ERROR");
    };

    var subSocket = socket.subscribe(request);

    next.click(function(e) {
    	subSocket.push(jQuery.stringifyJSON({preDirection : "next"}));
    });
    
    prev.click(function(e) {
    	subSocket.push(jQuery.stringifyJSON({preDirection : "prev"}));
    });
    
    restart.click(function(e) {
    	subSocket.push(jQuery.stringifyJSON({preDirection : "restart"}));
    });
    
    function changeStep(currentStep, stepCount) {
    	currentStepContainer.html(currentStep);
    	stepCountContainer.html(stepCount);
    }

});
