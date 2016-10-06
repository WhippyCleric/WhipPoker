var stateController = function ($scope, stateService, WEB_URLS, $window, $http) {

    $scope.initCont = function(){
        $scope.showStartButton=true;
        $scope.showBetButtons=false;
      
        $scope.notTurn=true;
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
            var dataSend = JSON.stringify(amount);
            $http({
                method: method
                , url: WEB_URLS.BET + "?id=" + $scope.userSession.user
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
