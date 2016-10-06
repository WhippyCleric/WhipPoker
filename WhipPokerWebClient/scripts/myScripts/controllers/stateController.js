var stateController = function ($scope, stateService, WEB_URLS, $window, $http) {

    $scope.initCont = function(){
        $scope.showStartButton=true;
            $scope.showBetButton=false;
            $scope.showCallButton=false;
            $scope.showFoldButton=false;
        $scope.getState();
    };
    
 $scope.getState = function() {
     $scope.screenHeight =  $window.innerHeight - 57;//remove the horizontal header. 
      var urlget = WEB_URLS.STATE + "?id=" + $scope.userSession.user;
        $scope.timeState = stateService.stream(stateService.getState, urlget, 500, $scope, $scope.userSession.user);
  };

    $scope.start = function(){
            $scope.showStartButton=false;
            $scope.showBetButton=true;
            $scope.showCallButton=true;
            $scope.showFoldButton=true;
        
            var method = 'GET';
            $http({
                method: method
                , url: WEB_URLS.START
                , headers: 'Content-Type:application/JSON'
                , timeout: 60000
            })
    };


};
angular.module("poker_module").controller("stateCtrl", stateController);
