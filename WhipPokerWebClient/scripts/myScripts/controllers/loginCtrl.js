var loginController = function ($scope,$location,registerService,URLS,$state) {
    $scope.failConnectMessage = "Incorrect Username or Password"
    $scope.go = function(){
        var userName = $scope.userName;
        var password = $scope.password;

        registerService.login(userName,password).then(
            function(user){
                if(registerService.isAuthenticated(user)){

                    $scope.userSession.user = user;
                    $state.go('whipPoker');
                 
                  }else{
                    $scope.failConnect = true; 
                }
            },function(user){
                $scope.failConnect = true; 
            })
    }



};
angular.module("poker_module").controller("loginCtrl", loginController);
