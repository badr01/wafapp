'use strict';
//header page controlller
myApp
  .controller('MainCtrl', function($scope, $location, version) {

    $scope.$path = $location.path.bind($location);
    $scope.version = version;

  })
//Status controller
myApp
  .controller('EtatCtrl', function ($scope,Status,alertService) {
  $scope.status={output:"",nginxStatus:false,haproxyStatus:false};
  $scope.execute=function(cmd){
    Status.query({cmd: cmd},function(data) {
      $scope.status=data;
      alertService.addAlert('success',data.output);
    },function(data) {
      alertService.addAlert('danger',data.output);
    });
  };

    var res = Status.query({cmd: 'NGINX_STATUS'},function(data) {
      $scope.status=data;
      alertService.addAlert('success',data.output);
    },function(data) {
      alertService.addAlert('danger',data.output);
    });

    });

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
