var app = angular.module("poker_module");
app.constant ('URLS', {
     LOGIN:"/login",
    POKER:"/table"
})
.constant('WEB_URLS',{
    REGISTER:"http://pmdunn-new:8080/whip-poker-server-0.1/register/registerPlayer",
    STATE:"http://pmdunn-new:8080/whip-poker-server-0.1/state/currentState",
    START:"http://pmdunn-new:8080/whip-poker-server-0.1/register/start",
    BET:"http://pmdunn-new:8080/whip-poker-server-0.1/event/bet",
    CALL:"http://pmdunn-new:8080/whip-poker-server-0.1/event/call",
    FOLD:"http://pmdunn-new:8080/whip-poker-server-0.1/event/fold"
})