'use strict';

angular.module('mgcrea.WafApp', ['ngResource','ui.bootstrap','smart-table', 'ngRoute', 'frapontillo.bootstrap-switch'])

  .constant('version', 'v0.1.0')

  .config(function($locationProvider, $routeProvider,$httpProvider) {

      //Enable cross domain calls
      $httpProvider.defaults.useXDomain = true;
    delete $httpProvider.defaults.headers.common['X-Requested-With'];

    $locationProvider.html5Mode(false);

    $routeProvider
      .when('/', {
        templateUrl: 'views/home.html'
      })
      .when('/etat', {
        templateUrl: '../views/etat.html'
      })
      .when('/manage', {
        templateUrl: '../views/manage.html'
      })
      .when('/whitelists', {
        templateUrl: '../views/whitelists.html'
      })
      .when('/journalisation', {
        templateUrl: '../views/journalisation.html'
      })
      .when('/journalisationb', {
        templateUrl: '../views/journalisationb.html'
      })
      .when('/update', {
        templateUrl: '../views/update.html'
      })
      .otherwise({
        redirectTo: '/'
      });

  });

