'use strict';

var myApp = angular.module('mgcrea.WafApp');
myApp.controller('UpdateCntrl', function ($scope, Backups, Update, Restore, $timeout, $modal, growl) {
  Backups.query(function (data) {
    $scope.ops = data;
  });

  //openning the restore action confirmation dialog
  $scope.openr = function (ix) {
    var index = ix;
    var restoreModalInstance = $modal.open({
      templateUrl: 'restoreModalDialog',
      controller: 'RestoreDialogCtrl'

    });
    restoreModalInstance.result.then(function () {
      restore(index);
    });
  };


  //openning the update action confirmation dialog
  $scope.openu = function () {

    var updateModalInstance = $modal.open({
      templateUrl: 'updateModalDialog',
      controller: 'UpdateDialogCtrl'

    });
    updateModalInstance.result.then(function () {
      update();
      $timeout(function () {
        refresh();
      }, 1000);
    });
  };


  var restore = function (ix) {
    Restore.get({key: ix})
      .$promise.then(function () {
        growl.success("Restoration des données sites réussie!");
      }, function () {
        growl.error("Restoration des données sites échouée!");
      })
  };

  var update = function () {
    Update.get()
      .$promise.then(function () {
        growl.success("Mise à jour des fichiers de configuration réussie!");
      }, function () {
        growl.error("Mise à jour des fichiers de configuration échouée!");
      })
  };

  var refresh = function () {
    Backups.query(function (data) {
      $scope.ops = data;
    });
  };

  $scope.toDate = function (ts) {
    return new Date(ts).toLocaleString('en-US', { hour12: false });
  };
})


//controls the restore action confirmation modal dialog
myApp.controller('RestoreDialogCtrl', function ($scope, $modalInstance) {
  $scope.cancel = function () {
    $modalInstance.dismiss('cancel');
  };
  $scope.ok = function () {
    $modalInstance.close();
  };
});

//controls the update action confirmation modal dialog
myApp.controller('UpdateDialogCtrl', function ($scope, $modalInstance) {
  $scope.cancel = function () {
    $modalInstance.dismiss('cancel');
  };
  $scope.ok = function () {
    $modalInstance.close();
  };
});
