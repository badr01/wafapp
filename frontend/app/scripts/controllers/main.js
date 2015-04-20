'use strict';
//header page controlller
myApp
  .controller('MainCtrl', function ($scope, $location, version) {

    $scope.$path = $location.path.bind($location);
    $scope.version = version;

  })
//Status controller
myApp
  .controller('EtatCtrl', function ($scope, Status, growl) {
    $scope.status = {output: "", nginxStatus: false, haproxyStatus: false};
    $scope.execute = function (cmd) {
      Status.query({cmd: cmd}, function (data) {
        $scope.status = data;
        growl.success(data.output);
      }, function (data) {
        growl.error(data.output);
      });
    };

    Status.query({cmd: 'NGINX_STATUS'}, function (data) {
      $scope.status = data;
      growl.success(data.output);
    }, function (data) {
      growl.error(data.output);
    });

  });


