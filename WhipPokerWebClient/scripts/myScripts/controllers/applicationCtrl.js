var applicationContoller = function ($scope,registerService) {
  
$scope.userSession ={
    user:{}
}
$scope.$watch('userSession.user',function(newVal,oldVal){
   
        if(newVal != oldVal ){
           
             $scope.$broadcast('userSession.user',{user:newVal});
            
          /*  if(registerService.isAuthenticated(newVal)){
                  $scope.$broadcast(AUTH_EVENTS.loginSuccess,{"val":newVal});
                 
            }else{
                 $scope.$broadcast(AUTH_EVENTS.loginFailed);
                $scope.$broadcast(AUTH_EVENTS.notAuthenticated);
                
            }*/
        }
    });
    
};
angular.module("poker_module").controller("applicationCtrl", applicationContoller);
