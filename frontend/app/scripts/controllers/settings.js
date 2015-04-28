'use strict';
//settings controller
myApp
  .controller('SettingsController',['$scope', 'Settings', 'growl', '$modal', '$timeout', function ($scope, Settings, growl, $modal, $timeout) {
    //loading settings
    Settings.get(function (data) {
      $scope.options = data;
    });

    //openning the update action confirmation dialog
    $scope.open = function () {

      var saveModalInstance = $modal.open({
        templateUrl: 'saveModalDialog',
        controller: 'SaveModalController'

      });
      saveModalInstance.result.then(function () {
        Settings.save($scope.options, function () {
          growl.success("Enregistré!");
        }, function () {
          growl.error("Echec d'enregistrement!");
        });
        $timeout(function () {
          Settings.get(function (data) {
            $scope.options = data;
          });
        }, 1000);
      });
    };


    $scope.editorEnabled = {
      nginxConfDir: false,
      sitesEnabledDir: false,
      naxsiConfDir: false,
      haproxyConfDir: false,
      siteCertDir: false,
      ddosRate:false,
      whitelistRules: false
    };

    $scope.editable = {
      nginxConfDir: "",
      sitesEnabledDir: "",
      naxsiConfDir: "",
      haproxyConfDir: "",
      siteCertDir: "",
      ddosRate:"",
      whitelistRules: []
    };
    $scope.enableEditor = function (elm) {
      $scope.editorEnabled[elm] = true;
      $scope.editable[elm] = $scope.options[elm];
    };

    $scope.disableEditor = function (elm) {
      $scope.editorEnabled[elm] = false;

    };

    $scope.save = function (elm) {
      $scope.options[elm] = $scope.editable[elm];
      $scope.disableEditor(elm);
    };
    $scope.removeWL = function (ix) {
      $scope.options['whitelistRules'].splice(ix, 1);
    };
    $scope.savewl = function () {
      var jsonarr = [];
      $scope.options['whitelistRules'].forEach(function (elt, ix, arr) {
        jsonarr.push(elt.id);
      });
      if (jsonarr.indexOf($scope.reg.id) == -1)
        $scope.options['whitelistRules'].push(angular.copy($scope.reg));
    }

  }]);

myApp.controller('SaveModalController',['$scope', '$modalInstance', function ($scope, $modalInstance) {
  $scope.cancel = function () {
    $modalInstance.dismiss('cancel');
  };
  $scope.ok = function () {
    $modalInstance.close();
  };
}]);
