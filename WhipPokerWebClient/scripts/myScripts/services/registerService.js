// authenticationService
/** this service is used to autheticated a user, and in case of good authentication save the current user in the rootScope*/

var app = angular.module("poker_module");
app.service('registerService',function($http,$rootScope,$q,WEB_URLS){

    this.login = function(alias,url){
        return $q(function(resolve,reject){
            var user = {};
            var responseUser = {};
            var method = 'POST';
            var response = null;
            var dataSend = JSON.stringify(alias);
            $http({
                method: method
                , url: WEB_URLS.REGISTER
                , data: dataSend
                , headers: 'Content-Type:application/JSON'
                , timeout: 60000
            }).
            then(function (response) {
                user=response.data.alias;
      
                resolve(response.data.alias);
            }, function (response) {
                reject(response);
            })
        })
    }

    this.logout = function(user){
        user = {};
    }

    this.isAuthenticated = function(user){
        return !angular.equals({},user);
    }

    this.isAuthorized = function(user, propertyName){

    }


});




