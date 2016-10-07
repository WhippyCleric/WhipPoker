var stateController = function ($rootScope, $scope, stateService, WEB_URLS, $window, $http) {

    $scope.initCont = function(){
        $rootScope.showStartButton=true;
        $rootScope.showBetButtons=false;
      
        $rootScope.cardSlot01 = "../../images/blank.png";
        $rootScope.cardSlot02 = "../../images/blank.png";
        $rootScope.cardSlot11 = "../../images/blank.png";
        $rootScope.cardSlot12 = "../../images/blank.png";
        $rootScope.cardSlot21 = "../../images/blank.png";
        $rootScope.cardSlot22 = "../../images/blank.png";
        $rootScope.cardSlot31 = "../../images/blank.png";
        $rootScope.cardSlot32 = "../../images/blank.png";
        $rootScope.cardSlot41 = "../../images/blank.png";
        $rootScope.cardSlot42 = "../../images/blank.png";
        $rootScope.cardSlot51 = "../../images/blank.png";
        $rootScope.cardSlot52 = "../../images/blank.png";
        $rootScope.cardSlot61 = "../../images/blank.png";
        $rootScope.cardSlot62 = "../../images/blank.png";
        $rootScope.cardSlot71 = "../../images/blank.png";
        $rootScope.cardSlot72 = "../../images/blank.png";
        $rootScope.cardSlot81 = "../../images/blank.png";
        $rootScope.cardSlot82 = "../../images/blank.png";
        $rootScope.cardSlot91 = "../../images/blank.png";
        $rootScope.cardSlot92 = "../../images/blank.png";
        $rootScope.centerCard1= "../../images/blank.png";
        $rootScope.centerCard2= "../../images/blank.png";
        $rootScope.centerCard3= "../../images/blank.png";
        $rootScope.centerCard4= "../../images/blank.png";
        $rootScope.centerCard5= "../../images/blank.png";
        
        $rootScope.notTurn=true;
        $scope.getState();
    };
    
 $scope.getState = function() {
     $scope.screenHeight =  $window.innerHeight - 57;//remove the horizontal header. 
      var urlget = WEB_URLS.STATE + "?id=" + $scope.userSession.user;
        $scope.timeState = stateService.stream(stateService.getState, urlget, 500, $scope, $scope.userSession.user);
  };

    $scope.start = function(){

            var method = 'GET';
            $http({
                method: method
                , url: WEB_URLS.START
                , headers: 'Content-Type:application/JSON'
                , timeout: 60000
            })
    };
    
       
    $scope.bet = function(amount){
            var method = 'POST';
            var dataSend = "";
            $http({
                method: method
                , url: WEB_URLS.BET + "?id=" + $scope.userSession.user + "&amount=" + amount
                , data: dataSend
                , headers: 'Content-Type:application/JSON'
                , timeout: 60000
            })
    };
    
    $scope.call = function(){
            var method = 'POST';
            var dataSend = "";
            $http({
                method: method
                , url: WEB_URLS.CALL + "?id=" + $scope.userSession.user
                , data: dataSend
                , headers: 'Content-Type:application/JSON'
                , timeout: 60000
            })
    };
    
    $scope.fold = function(){
            var method = 'POST';
            var dataSend = "";
            $http({
                method: method
                , url: WEB_URLS.FOLD + "?id=" + $scope.userSession.user
                , data: dataSend
                , headers: 'Content-Type:application/JSON'
                , timeout: 60000
            })
    };


};
angular.module("poker_module").controller("stateCtrl", stateController);
