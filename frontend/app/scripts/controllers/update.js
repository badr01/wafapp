'use strict';

var myApp = angular.module('mgcrea.WafApp');
myApp.controller('UpdateCntrl',function($scope,Backups,Update,$timeout){
  Backups.query(function (data) {
    $scope.ops = data;
  });

var refresh =function() {
  Backups.query(function (data) {
    $scope.ops = data;
  });
};
$scope.update=function(){
  Update.get();
  $timeout(function () {
    refresh();
  }, 200);
};
$scope.toDate=function(ts){
  return new Date(ts).toLocaleString();
};
})
