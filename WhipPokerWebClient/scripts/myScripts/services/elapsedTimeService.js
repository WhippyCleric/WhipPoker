// elapsedTimeService.js
/** this service is used to get the time elapsed between an instant given at parameter and the current instant*/

var app = angular.module("poker_module");
app.service('elapsedTime',function(dateFilter){

    this.elapsedTime = function(givenDate){
        
        return ((new Date()).getTime() - givenDate.getTime());
    }



});




