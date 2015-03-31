'use strict';
angular.module('mgcrea.WafApp');


myApp.controller('ListCtrl', function($scope, $modal,$log) {

    $scope.sites=[{"https":true,"ip":"0.0.0.0","mode":true,"wlList":null,"msgError":"error nigga","nomDomaine":"fdzeefe","port":8945},{"https":true,"ip":"0.0.0.0","mode":false,"wlList":null,"msgError":"error ","nomDomaine":"kali.com","port":8945},{"https":true,"ip":"0.0.0.0","mode":false,"wlList":null,"msgError":"error ","nomDomaine":"reddit.com","port":8945},{"https":true,"ip":"0.0.0.0","mode":false,"wlList":["sfdfdfsdf","sdfsfdfsf","sdfsdfds","sdfsdfsdf","sfsdfsdffsd"],"msgError":"error","nomDomaine":"ilem.com","port":8945}];
    // Modal: called by edit(site) and Add new user
    $scope.open = function() {
      $log.info("running");
      var modalInstance = $modal.open({
        templateUrl: 'add_site_modal',
        controller: 'ModalCtrl',
        resolve: {
          items: function () {
            return $scope.sites;
          }
        }
      });
    };


});

myApp.controller('ModalCtrl', function($scope, $modalInstance, items) {

$scope.items=items;
$scope.isSelected=false;
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
