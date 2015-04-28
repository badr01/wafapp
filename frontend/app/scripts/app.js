'use strict';
var loading=false;
var myApp=angular.module('WafApp', ['angular-growl','ngResource','ngTagsInput','ui.bootstrap','smart-table', 'ngRoute', 'angularHighlightTextarea'])

  .constant('version', 'v0.1.0')

  .config(function($locationProvider, $routeProvider) {

    $locationProvider.html5Mode(false);

    $routeProvider
      .when('/', {
        templateUrl: './views/etat.html'
      })
      .when('/etat', {
        templateUrl: './views/etat.html'
      })
      .when('/manage', {
        templateUrl: './views/manage.html'
      })
      .when('/journalisation', {
        templateUrl: './views/journalisation.html'
      })
      .when('/journalisationb', {
        templateUrl: './views/journalisationb.html'
      })
      .when('/settings', {
        templateUrl: './views/settings.html'
      })
      .when('/update', {
        templateUrl: './views/update.html'
      })
      .otherwise({
        redirectTo: './'
      });

  }).config([
    "$httpProvider",
    function ($httpProvider) {
      $httpProvider
        .interceptors.push("httpInterceptor");
    }
  ]).config(['growlProvider', function(growlProvider) {
    growlProvider.globalReversedOrder(true);
    growlProvider.globalTimeToLive(3000);
  }]);
