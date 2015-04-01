'use strict';
var myApp =angular.module('mgcrea.WafApp');

myApp.factory("Site",function($resource){

  return $resource("/rest/site/:domain");
});

myApp.controller('ListCtrl', function($scope, $modal,$log,Site) {

  Site.query(function(data){
  $scope.sites=data;
    });
    // Modal: called by edit(site) and Add new user
    $scope.open = function(domain) {
      $log.info("running");
      var modalInstance = $modal.open({
        templateUrl: 'add_site_modal',
        controller: 'ModalCtrl',
        resolve: {
          domain: function () {
            return domain;
          }
        }
      });
    };


});

myApp.controller('ModalCtrl', function($scope, $modalInstance, domain,Site) {

  Site.get({domain:domain},function(data){
    $scope.site=data;
  });

  //$scope.isSelected=false;
  $scope.cancel = function() {
    $modalInstance.dismiss('cancel');
  };

  // Add new user
  $scope.add = function() {

    $modalInstance.dismiss('cancel');
  };

  // Save edited user.
  $scope.save = function() {
    $modalInstance.dismiss('cancel');

  };
});
