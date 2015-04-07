'use strict';
var myApp = angular.module('mgcrea.WafApp');

myApp.factory("Access", function($resource) {
  return $resource("/rest/logs/access",{from:'@from',to:'@to'});
});

/*Pagination controller*/

myApp.controller('paginationCtrl',['$scope','Access', function ($scope,Access) {
  Access.query(function(data) {
   data.forEach(function(elt,ix,arr){
      arr[ix].time=elt.time.$date
    });
    $scope.rowCollection=angular.fromJson(data);
    window.aa=angular.fromJson(data);

  });
$scope.displayCollection=[].concat($scope.rowCollection);
  $scope.itemsByPage=15;


}]);

/*date picker controller logic*/

myApp.controller('DatepickerCtrl', function ($scope,Access) {
  $scope.today = function(dt) {
    $scope[dt] = new Date();
  };
  $scope.today();

  $scope.clear = function (dt) {
    $scope[dt] = null;
  };

  $scope.getLog=function(){
    Access.query({from: $scope.dtFrom,to:$scope.dtTo},function(data) {
      $scope.$parent.rowCollection=[];
    });
  };

   $scope.open = function($event,opened) {
    $event.preventDefault();
    $event.stopPropagation();

    $scope[opened] = opened;
  };

  $scope.dateOptions = {
    formatYear: 'yy',
    startingDay: 1
  };


  $scope.format ='dd.MM.yyyy HH:mm:ss';
});
