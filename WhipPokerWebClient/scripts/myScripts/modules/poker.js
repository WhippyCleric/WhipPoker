///<reference path="angular.js"/>
//create the module which represent the main of our app

'use strict';
var app = angular.module("poker_module",['ui.router','ui.date','ui.bootstrap','angular-momentjs','ngDialog',]);

app.config(function($stateProvider,$urlRouterProvider,URLS) { 
        
        // Système de routage
        $stateProvider
        .state('login', {
            url:URLS.LOGIN,
            templateUrl: '../../../views/logon/login.html',
           
        })
        .state('whipPoker', {
            url:URLS.POKER,
            templateUrl: '../../../views/whipPoker.html',
          
        });
        $urlRouterProvider.otherwise(URLS.LOGIN);
    });

