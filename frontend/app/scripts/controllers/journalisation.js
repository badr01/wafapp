var myApp = angular.module('mgcrea.WafApp');

/*Pagination controller*/

myApp.controller('paginationCtrl', ['$scope', function (scope) {
  var
    ipL = ['122.14.35.87', '124.5.2.47', '98.25.1.47', '36.48.10.4', '91.7.24.1'],
    timestampL = ['15/12/2014', '24/02/2015', '02/03/2014', '18/09/2015', '25/05/2015'],
    httpCodeL = ['200', '206', '301', '500', '404'],
    uriL = ['/fdd', '/hjg', '/ghg', '/rrrr', '/rerr'],
    urlL = ['/azze/fdgd', '/sfdg/ezez', '/rtrt/uik', '/tryty/tyjy', '/erer/reg'];

  function createRandomItem() {
    var
      ip = ipL[Math.floor(Math.random() * 4)],
      timestamp = timestampL[Math.floor(Math.random() * 4)],
      uri = uriL[Math.floor(Math.random() * 4)],
      httpCode = httpCodeL[Math.floor(Math.random() * 4)],
      url = urlL[Math.floor(Math.random() * 4)]


    return{
      ip: ip,
      timestamp: timestamp,
      uri: uri,
      httpCode: httpCode,
      url: url
    };
  }

  scope.itemsByPage=15;

  scope.rowCollection = [];
  for (var j = 0; j < 2000; j++) {
    scope.rowCollection.push(createRandomItem());
  }
}]);

/*date picker controller*/

myApp.controller('DatepickerDemoCtrl', function ($scope) {
  $scope.today = function(dt) {
    $scope[dt] = new Date();
  };
  $scope.today();

  $scope.clear = function (dt) {
    $scope[dt] = null;
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


  $scope.format ='dd.MM.yyyy';
});
