'use strict';
var myApp = angular.module('mgcrea.WafApp');

myApp.factory("Error", function($resource) {
  return $resource("/api/logs/error",{from:'@from',to:'@to'});
});

/*Pagination controller*/

myApp.controller('paginationBCtrl',['$scope','Error','$modal', function ($scope,Error,$modal) {
  Error.query(function(data) {
    data.forEach(function(elt,ix,arr){
      arr[ix].time=elt._id.time.$date;
      arr[ix].host=elt._id.host;
      arr[ix].client_ip=elt._id.client_ip;
      arr[ix].path=elt._id.path;
    });
    $scope.rowCollection=angular.fromJson(data);
    window.aa=angular.fromJson(data);

  });
  $scope.displayCollection=[].concat($scope.rowCollection);
  $scope.itemsByPage=15;
  $scope.predicates = [{key:"Filter by Host","value":"host"},{key:"Filter by User IP","value":"client ip"},{key:"Filter by URI Path","value":"path"}];
  $scope.selectedPredicate = $scope.predicates[0].value;
  $scope.isCollapsed=true;
  // Modal: called by "supprimer site"
  $scope.openDialog = function(rules,size) {

    var $modalInstance = $modal.open({
      templateUrl: 'rulesModalDialog',
      controller: 'ShowDialogCtrl',
      size: size,
      resolve:{rules:function(){
        return rules;
      }}

    });
}}]);

//show rules modal controller
  myApp.controller('ShowDialogCtrl',function($scope,$modalInstance,rules){
  $scope.rules=rules;

  //dismiss and quit the modal dialog;
  $scope.cancel = function() {
    $modalInstance.dismiss('cancel');
  };
  });

//experimental feature : filter highlighting match zone in requests
  myApp.filter('highlight', function($sce) {
  return function(text, phrase) {
    if (phrase) text = text.replace(new RegExp('('+phrase+'=[^ ]*)', 'gi'),
      '<span class="badge bg-primary">$1</span>')

    return $sce.trustAsHtml(text)
  }
})

/*date picker controller logic*/

myApp.controller('DatepickerBCtrl', function ($scope,Error) {
  $scope.today = function(dt) {
    $scope[dt] = new Date();
  };
  $scope.today();

  $scope.clear = function (dt) {
    $scope[dt] = null;
  };

  $scope.getLog=function(){
    Error.query({from: $scope.dtFrom,to:$scope.dtTo},function(data) {
      data.forEach(function(elt,ix,arr){
        arr[ix].time=elt._id.time.$date;
        arr[ix].host=elt._id.host;
        arr[ix].client_ip=elt._id.client_ip;
        arr[ix].path=elt._id.path;
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
