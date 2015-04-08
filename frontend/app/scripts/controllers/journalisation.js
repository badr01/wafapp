'use strict';
var myApp = angular.module('mgcrea.WafApp');

myApp.factory("Access", function($resource) {
  return $resource("/api/logs/access",{from:'@from',to:'@to'});
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
  $scope.predicates = [{key:"Filter by Host","value":"host"},{key:"Filter by User_IP","value":"client_ip"},{key:"Filter by User","value":"client"},{key:"Filter by URI_Path","value":"path"},{key:"Filter by Method","value":"method"},{key:"Filter by HTTP_Status","value":"code"},{key:"Filter by Referer","value":"referer"},{key:"Filter by Bytes","value":"size"},{key:"Filter by X_Forwarded_For","value":"x_forwarded_for"},{key:"Filter by User_Agent","value":"user_agent"}];
  $scope.selectedPredicate = $scope.predicates[0].value;

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
      data.forEach(function(elt,ix,arr){
        arr[ix].time=elt.time.$date
      });
      $scope.$parent.rowCollection=angular.fromJson(data);
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
