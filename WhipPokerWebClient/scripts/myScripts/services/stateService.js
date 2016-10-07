var app = angular.module("poker_module");
app.service('stateService',function($http,$rootScope,$q,WEB_URLS, $timeout, elapsedTime){
    this.getState = function(url, username){
        return $q(function(resolve, reject){
            var method = 'GET';
            var response = null;
            $http({
                method: method
                , url: url
                , headers: 'Content-Type:application/JSON'
                , timeout: 60000
            }).
            then(function (response) {
                $rootScope.datadump = JSON.stringify(response.data);
                var seatsArray = response.data.table.seats;
               
                if(response.data.table.state!="PENDING_START"){
                    $rootScope.showStartButton=false;
                    $rootScope.showBetButtons=true;
                }
                
                $rootScope.currentPot = response.data.table.currentPot;
                
                var cardsArray = response.data.table.currentCards;
                if(cardsArray[0]!=null){
                    $rootScope.centerCard1= "../../images/" + cardsArray[0].suit + cardsArray[0].value + ".png";
                }else{
                    $rootScope.centerCard1= "../../images/blank.png";
                }
                if(cardsArray[1]!=null){
                    $rootScope.centerCard2= "../../images/" + cardsArray[1].suit + cardsArray[1].value + ".png";
                }else{
                    $rootScope.centerCard2= "../../images/blank.png";
                }
                if(cardsArray[2]!=null){
                    $rootScope.centerCard3= "../../images/" + cardsArray[2].suit + cardsArray[2].value + ".png";
                }else{
                    $rootScope.centerCard3= "../../images/blank.png";
                }
                if(cardsArray[3]!=null){
                    $rootScope.centerCard4= "../../images/" + cardsArray[3].suit + cardsArray[3].value + ".png";
                }else{
                    $rootScope.centerCard4= "../../images/blank.png";
                }
                if(cardsArray[4]!=null){
                    $rootScope.centerCard5= "../../images/" + cardsArray[4].suit + cardsArray[4].value + ".png";
                }else{
                    $rootScope.centerCard5= "../../images/blank.png";
                }
               
                if(seatsArray[0].player !=null){
                    $rootScope.seat0 = seatsArray[0];
                    
                     if(seatsArray[0].state=='OCCUPIED_WAITING' || seatsArray[0].state=='OCCUPIED_ACTION'){
                         if(response.data.table.state=="SHOWDOWN"){
                              $rootScope.cardSlot01 = "../../images/" + seatsArray[0].hand.cards[0].suit + seatsArray[0].hand.cards[0].value + ".png";
                                $rootScope.cardSlot02 = "../../images/" + seatsArray[0].hand.cards[1].suit + seatsArray[0].hand.cards[1].value + ".png";
                             $rootScope.notTurn=true;
                         }else{
                             if(seatsArray[0].player.alias!=username){
                                $rootScope.cardSlot01 = "../../images/back.png";
                                $rootScope.cardSlot02 = "../../images/back.png";
                             }else{
                                $rootScope.cardSlot01 = "../../images/" + response.data.hand.cards[0].suit + response.data.hand.cards[0].value + ".png";
                                $rootScope.cardSlot02 = "../../images/" + response.data.hand.cards[1].suit + response.data.hand.cards[1].value + ".png";
                                if(seatsArray[0].state=='OCCUPIED_ACTION'){
                                    $rootScope.notTurn=false;
                                }else{
                                     $rootScope.notTurn=true;
                                }
                             }
                         }
                    }else{
                         $rootScope.cardSlot01 = "../../images/blank.png";
                         $rootScope.cardSlot02 = "../../images/blank.png";
                    }
       
                }
                
                if(seatsArray[1].player !=null){
                     $rootScope.seat1 = seatsArray[1];
                    if(seatsArray[1].state=='OCCUPIED_WAITING' || seatsArray[1].state=='OCCUPIED_ACTION'){
                        if(response.data.table.state=="SHOWDOWN"){
                              $rootScope.cardSlot11 = "../../images/" + seatsArray[1].hand.cards[0].suit + seatsArray[1].hand.cards[0].value + ".png";
                                $rootScope.cardSlot12 = "../../images/" + seatsArray[1].hand.cards[1].suit + seatsArray[1].hand.cards[1].value + ".png";
                            $rootScope.notTurn=true;
                         }else{
                             if(seatsArray[1].player.alias!=username){
                                $rootScope.cardSlot11 = "../../images/back.png";
                                $rootScope.cardSlot12 = "../../images/back.png";
                             }else{
                                $rootScope.cardSlot11 = "../../images/" + response.data.hand.cards[0].suit + response.data.hand.cards[0].value + ".png";
                                $rootScope.cardSlot12 = "../../images/" + response.data.hand.cards[1].suit + response.data.hand.cards[1].value + ".png";
                                 if(seatsArray[1].state=='OCCUPIED_ACTION'){
                                    $rootScope.notTurn=false;
                                }else{
                                     $rootScope.notTurn=true;
                                }
                             }
                         }
                    }else{
                         $rootScope.cardSlot11 = "../../images/blank.png";
                         $rootScope.cardSlot12 = "../../images/blank.png";
                    }
                }
                
                if(seatsArray[2].player !=null){
                     $rootScope.seat2 = seatsArray[2];
                    if(seatsArray[2].state=='OCCUPIED_WAITING' || seatsArray[2].state=='OCCUPIED_ACTION'){
                        if(response.data.table.state=="SHOWDOWN"){
                               $rootScope.cardSlot21 = "../../images/" + seatsArray[2].hand.cards[0].suit + seatsArray[2].hand.cards[0].value + ".png";
                               $rootScope.cardSlot22 = "../../images/" + seatsArray[2].hand.cards[1].suit + seatsArray[2].hand.cards[1].value + ".png";
                            $rootScope.notTurn=true;
                         }else{
                             if(seatsArray[2].player.alias!=username){
                                $rootScope.cardSlot21 = "../../images/back.png";
                                $rootScope.cardSlot22 = "../../images/back.png";
                             }else{
                                $rootScope.cardSlot21 = "../../images/" + response.data.hand.cards[0].suit + response.data.hand.cards[0].value + ".png";
                                $rootScope.cardSlot22 = "../../images/" + response.data.hand.cards[1].suit + response.data.hand.cards[1].value + ".png";
                                 if(seatsArray[2].state=='OCCUPIED_ACTION'){
                                    $rootScope.notTurn=false;
                                }else{
                                     $rootScope.notTurn=true;
                                }
                             }
                         }
                    }else{
                         $rootScope.cardSlot21 = "../../images/blank.png";
                         $rootScope.cardSlot22 = "../../images/blank.png";
                    }
                }
                
                if(seatsArray[3].player !=null){
                     $rootScope.seat3 = seatsArray[3];
                     if(seatsArray[3].state=='OCCUPIED_WAITING' || seatsArray[3].state=='OCCUPIED_ACTION'){
                         if(response.data.table.state=="SHOWDOWN"){
                              $rootScope.cardSlot31 = "../../images/" + seatsArray[3].hand.cards[0].suit + seatsArray[3].hand.cards[0].value + ".png";
                                $rootScope.cardSlot32 = "../../images/" + seatsArray[3].hand.cards[1].suit + seatsArray[3].hand.cards[1].value + ".png";
                             $rootScope.notTurn=true;
                         }else{
                             if(seatsArray[3].player.alias!=username){
                                $rootScope.cardSlot31 = "../../images/back.png";
                                $rootScope.cardSlot32 = "../../images/back.png";
                             }else{
                                $rootScope.cardSlot31 = "../../images/" + response.data.hand.cards[0].suit + response.data.hand.cards[0].value + ".png";
                                $rootScope.cardSlot32 = "../../images/" + response.data.hand.cards[1].suit + response.data.hand.cards[1].value + ".png";
                                 if(seatsArray[3].state=='OCCUPIED_ACTION'){
                                    $rootScope.notTurn=false;
                                }else{
                                     $rootScope.notTurn=true;
                                }
                             }
                         }
                    }else{
                         $rootScope.cardSlot31 = "../../images/blank.png";
                         $rootScope.cardSlot32 = "../../images/blank.png";
                    }
                }
                
                if(seatsArray[4].player !=null){
                     $rootScope.seat4 = seatsArray[4];
                     if(seatsArray[4].state=='OCCUPIED_WAITING' || seatsArray[4].state=='OCCUPIED_ACTION'){
                         if(response.data.table.state=="SHOWDOWN"){
                              $rootScope.cardSlot41 = "../../images/" + seatsArray[4].hand.cards[0].suit + seatsArray[4].hand.cards[0].value + ".png";
                                $rootScope.cardSlot42 = "../../images/" + seatsArray[4].hand.cards[1].suit + seatsArray[4].hand.cards[1].value + ".png";
                             $rootScope.notTurn=true;
                         }else{
                             if(seatsArray[4].player.alias!=username){
                                $rootScope.cardSlot41 = "../../images/back.png";
                                $rootScope.cardSlot42 = "../../images/back.png";
                             }else{
                                $rootScope.cardSlot41 = "../../images/" + response.data.hand.cards[0].suit + response.data.hand.cards[0].value + ".png";
                                $rootScope.cardSlot42 = "../../images/" + response.data.hand.cards[1].suit + response.data.hand.cards[1].value + ".png";
                                 if(seatsArray[4].state=='OCCUPIED_ACTION'){
                                    $rootScope.notTurn=false;
                                }else{
                                     $rootScope.notTurn=true;
                                }
                             }
                         }
                    }else{
                         $rootScope.cardSlot41 = "../../images/blank.png";
                         $rootScope.cardSlot42 = "../../images/blank.png";
                    }
                }
                
                if(seatsArray[5].player !=null){
                     $rootScope.seat5 = seatsArray[5];
                    if(seatsArray[5].state=='OCCUPIED_WAITING' || seatsArray[5].state=='OCCUPIED_ACTION'){
                        if(response.data.table.state=="SHOWDOWN"){
                              $rootScope.cardSlot51 = "../../images/" + seatsArray[5].hand.cards[0].suit + seatsArray[5].hand.cards[0].value + ".png";
                                $rootScope.cardSlot52 = "../../images/" + seatsArray[5].hand.cards[1].suit + seatsArray[5].hand.cards[1].value + ".png";
                            $rootScope.notTurn=true;
                         }else{
                             if(seatsArray[5].player.alias!=username){
                                $rootScope.cardSlot51 = "../../images/back.png";
                                $rootScope.cardSlot52 = "../../images/back.png";
                             }else{
                                $rootScope.cardSlot51 = "../../images/" + response.data.hand.cards[0].suit + response.data.hand.cards[0].value + ".png";
                                $rootScope.cardSlot52 = "../../images/" + response.data.hand.cards[1].suit + response.data.hand.cards[1].value + ".png";
                                 if(seatsArray[5].state=='OCCUPIED_ACTION'){
                                    $rootScope.notTurn=false;
                                }else{
                                     $rootScope.notTurn=true;
                                }
                             }
                         }
                    }else{
                         $rootScope.cardSlot51 = "../../images/blank.png";
                         $rootScope.cardSlot52 = "../../images/blank.png";
                    }
                }
                
                if(seatsArray[6].player !=null){
                     $rootScope.seat6 = seatsArray[6];
                     if(seatsArray[6].state=='OCCUPIED_WAITING' || seatsArray[6].state=='OCCUPIED_ACTION'){
                         if(response.data.table.state=="SHOWDOWN"){
                              $rootScope.cardSlot61 = "../../images/" + seatsArray[6].hand.cards[0].suit + seatsArray[6].hand.cards[0].value + ".png";
                                $rootScope.cardSlot62 = "../../images/" + seatsArray[6].hand.cards[1].suit + seatsArray[6].hand.cards[1].value + ".png";
                             $rootScope.notTurn=true;
                         }else{
                             if(seatsArray[6].player.alias!=username){
                                $rootScope.cardSlot61 = "../../images/back.png";
                                $rootScope.cardSlot62 = "../../images/back.png";
                             }else{
                                $rootScope.cardSlot61 = "../../images/" + response.data.hand.cards[0].suit + response.data.hand.cards[0].value + ".png";
                                $rootScope.cardSlot62 = "../../images/" + response.data.hand.cards[1].suit + response.data.hand.cards[1].value + ".png";
                                 if(seatsArray[6].state=='OCCUPIED_ACTION'){
                                    $rootScope.notTurn=false;
                                }else{
                                     $rootScope.notTurn=true;
                                }
                             }
                         }
                    }
                    else{
                         $rootScope.cardSlot61 = "../../images/blank.png";
                         $rootScope.cardSlot62 = "../../images/blank.png";
                    }
                }
                
                if(seatsArray[7].player !=null){
                     $rootScope.seat7 = seatsArray[7];
                     if(seatsArray[7].state=='OCCUPIED_WAITING' || seatsArray[7].state=='OCCUPIED_ACTION'){
                         if(response.data.table.state=="SHOWDOWN"){
                              $rootScope.cardSlot71 = "../../images/" + seatsArray[7].hand.cards[0].suit + seatsArray[7].hand.cards[0].value + ".png";
                                $rootScope.cardSlot72 = "../../images/" + seatsArray[7].hand.cards[1].suit + seatsArray[7].hand.cards[1].value + ".png";
                             $rootScope.notTurn=true;
                         }else{
                             if(seatsArray[7].player.alias!=username){
                                $rootScope.cardSlot71 = "../../images/back.png";
                                $rootScope.cardSlot72 = "../../images/back.png";
                             }else{
                                $rootScope.cardSlot71 = "../../images/" + response.data.hand.cards[0].suit + response.data.hand.cards[0].value + ".png";
                                $rootScope.cardSlot72 = "../../images/" + response.data.hand.cards[1].suit + response.data.hand.cards[1].value + ".png";
                                 if(seatsArray[7].state=='OCCUPIED_ACTION'){
                                    $rootScope.notTurn=false;
                                }else{
                                     $rootScope.notTurn=true;
                                }
                             }
                         }
                    }
                    else{
                         $rootScope.cardSlot71 = "../../images/blank.png";
                         $rootScope.cardSlot72 = "../../images/blank.png";
                    }
                }
                
                if(seatsArray[8].player !=null){
                     $rootScope.seat8 = seatsArray[8];
                     if(seatsArray[8].state=='OCCUPIED_WAITING' || seatsArray[8].state=='OCCUPIED_ACTION'){
                         if(response.data.table.state=="SHOWDOWN"){
                              $rootScope.cardSlot81 = "../../images/" + seatsArray[8].hand.cards[0].suit + seatsArray[8].hand.cards[0].value + ".png";
                                $rootScope.cardSlot82 = "../../images/" + seatsArray[8].hand.cards[1].suit + seatsArray[8].hand.cards[1].value + ".png";
                             $rootScope.notTurn=true;
                         }else{
                             if(seatsArray[8].player.alias!=username){
                                $rootScope.cardSlot81 = "../../images/back.png";
                                $rootScope.cardSlot82 = "../../images/back.png";
                             }else{
                                $rootScope.cardSlot81 = "../../images/" + response.data.hand.cards[0].suit + response.data.hand.cards[0].value + ".png";
                                $rootScope.cardSlot82 = "../../images/" + response.data.hand.cards[1].suit + response.data.hand.cards[1].value + ".png";
                                 if(seatsArray[8].state=='OCCUPIED_ACTION'){
                                    $rootScope.notTurn=false;
                                }else{
                                     $rootScope.notTurn=true;
                                }
                             }
                         }
                    }else{
                         $rootScope.cardSlot81 = "../../images/blank.png";
                         $rootScope.cardSlot82 = "../../images/blank.png";
                    }
                }
                
                if(seatsArray[9].player !=null){
                     $rootScope.seat9 = seatsArray[9];
                    if(seatsArray[9].state=='OCCUPIED_WAITING' || seatsArray[9].state=='OCCUPIED_ACTION'){
                        if(response.data.table.state=="SHOWDOWN"){
                              $rootScope.cardSlot91 = "../../images/" + seatsArray[9].hand.cards[0].suit + seatsArray[9].hand.cards[0].value + ".png";
                              $rootScope.cardSlot92 = "../../images/" + seatsArray[9].hand.cards[1].suit + seatsArray[9].hand.cards[1].value + ".png";
                            $rootScope.notTurn=true;
                         }else{
                             if(seatsArray[9].player.alias!=username){
                                $rootScope.cardSlot91 = "../../images/back.png";
                                $rootScope.cardSlot92 = "../../images/back.png";
                             }else{
                                $rootScope.cardSlot91 = "../../images/" + response.data.hand.cards[0].suit + response.data.hand.cards[0].value + ".png";
                                $rootScope.cardSlot92 = "../../images/" + response.data.hand.cards[1].suit + response.data.hand.cards[1].value + ".png";
                                 if(seatsArray[9].state=='OCCUPIED_ACTION'){
                                    $rootScope.notTurn=false;
                                }else{
                                     $rootScope.notTurn=true;
                                }
                             }
                         }
                    }else{
                         $rootScope.cardSlot91 = "../../images/blank.png";
                         $rootScope.cardSlot92 = "../../images/blank.png";
                    }
                }
  
                resolve(response.data);
            }, function (response) {
                reject(response);
            })
        })
    }
    
    
      /* this variable is used to save the promise of timeout service and will use to stop the repetition of execution of the function given at parameter */
    var timeOutId = {};
    var timeElapsed = 0;

    /* function to call when we want to execute repetitively another function with a certain interval of time, it takes the as parameters the function to execute and the time interval */
    this.stream = function (functionToStream, url, timeInteval, $scope, username) {
        var startTime = new Date();
        return updateLater(functionToStream, url, timeInteval, startTime, $scope, username);
    };

    function updateLater(functionToStream, url, timeInteval, startTime, $scope, username) {

        timeOutId = $timeout(function () {
            functionToStream(url, username);
            $scope.timePrice = elapsedTime.elapsedTime(startTime);

            updateLater(functionToStream, url, timeInteval, startTime, $scope, username);
        }, timeInteval);
    }
    
     /* function to call when we want to stop the execution of the previous stream function, it takes the $scope in parameter in order to use the service $watch, and also the expression to evaluate or the variable to watch, this variable is relative to the scope*/
    this.endStream = function ($scope, timer, expression,url) {
            return $q(function (resolve, reject) {
                if (angular.isDefined(timeOutId)) {
                    $scope.$watch(expression, function (newValue, oldValue) {
                       
                        if (newValue >= timer) {
                            $timeout.cancel(timeOutId);
                            resolve(newValue);
                            stopPriceInside(url);
                        }
                    })
                } else {
                    reject();
                }

            })

        }
    
});