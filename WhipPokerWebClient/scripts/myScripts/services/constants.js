var app = angular.module("poker_module");
app.constant ('URLS', {
     LOGIN:"/login",
    POKER:"/table"
})
.constant('WEB_URLS',{
    REGISTER:"http://localhost:8080/whip-poker-server-0.1/register/registerPlayer",
    STATE:"http://localhost:8080/whip-poker-server-0.1/state/currentState",
    START:"http://localhost:8080/whip-poker-server-0.1/register/start",
    BET:"http://localhost:8080/whip-poker-server-0.1/event/bet",
    CALL:"http://localhost:8080/whip-poker-server-0.1/event/call",
    FOLD:"http://localhost:8080/whip-poker-server-0.1/event/fold"
})