'use strict';

var myApp = angular.module('mgcrea.WafApp');


  myApp.controller('MainCtrl', function($scope, $location, version) {

    $scope.$path = $location.path.bind($location);
    $scope.version = version;

  })
myApp.controller('EtatCtrl', function ($scope,$http,$log,alertService) {
  $scope.isSelected = '';
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
    var res = $http({method: 'GET', url: "https://localhost:8080/api/status"})
    res.success(function(data) {
      //alert( "failure message: " + JSON.stringify({data: data}));
      alertService.addAlert('success',data);
    });

    res.error(function(data) {
      //alert( "failure message: " + JSON.stringify({data: data}));
      alertService.addAlert('danger',data);
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
myApp.service('alertService', function ($timeout) {
  var data = [];

  this.alerts = function () {
    return data;
  };
  this.addAlert = function (type,msg) {
    data.push({
      type:type, msg:msg
    });
    $timeout(function(){data.splice(data.indexOf({
      type:type, msg:msg
    }),1);
    }, 3000);
  };
  this.deleteAlert = function (index) {
   data.splice(index, 1);
  };
});
myApp.controller('alertCtrl',function ($scope,alertService) {
  $scope.alerts =alertService.alerts();

  $scope.closeAlert = function(index) {
    alertService.deleteAlert(index);
  };

});
