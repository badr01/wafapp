'use strict';

var myApp = angular.module('mgcrea.WafApp');


  myApp.controller('MainCtrl', function($scope, $location, version) {

    $scope.$path = $location.path.bind($location);
    $scope.version = version;

  })
myApp.controller('EtatCtrl', function ($scope,$http,$log) {
  $scope.isSelected = 'Allume';
  $scope.onText = 'ON';

  $scope.offText = 'OFF';
  $scope.isActive = true;
  $scope.size = 'large';
  $scope.animate = true;
  $scope.radioOff = true;
  $scope.handleWidth = "auto";
  $scope.labelWidth = "auto";
  $scope.inverse = true;

  $scope.$watch('isSelected', function() {
    $log.info("selected");
    var dataObj = {
      page:"EtatWaf",
      action:"Save",
      etat: "Allume"
    };
    var res = $http({method: 'GET', url: "http://localhost:8080/ProjectWAF/Conn",data: {'page':'EtatWaf','action':'Load'}, headers: {'Authorization': 'Basic YWRtaW46YWRtaW4='}})


    res.error(function(data, status, headers, config) {
      alert( "failure message: " + JSON.stringify({data: data}));
    });
    res.error(function(data, status, headers, config) {
      alert( "failure message: " + JSON.stringify({data: data}));
    });
    });
  });
myApp.directive('bootstrapSwitch', [
  function() {
    return {
      restrict: 'A',
      require: '?ngModel',
      link: function(scope, element, attrs, ngModel) {
        element.bootstrapSwitch();

        element.on('switchChange.bootstrapSwitch', function(event, state) {
          if (ngModel) {
            scope.$apply(function() {
              ngModel.$setViewValue(state);
            });
          }
        });

        scope.$watch(attrs.ngModel, function(newValue, oldValue) {
          if (newValue) {
            element.bootstrapSwitch('state', true, true);
          } else {
            element.bootstrapSwitch('state', false, true);
          }
        });
      }
    };
  }
]);
