'use strict';
/*Pagination controller*/

myApp.controller('paginationBCtrl', ['$scope', 'Error', '$modal', function ($scope, Error, $modal) {
  Error.query(function (data) {
    data.forEach(function (elt, ix, arr) {
      arr[ix].time = new Date(Date.parse(elt._id.time.$date)).toLocaleString('en-US', { hour12: false });
      arr[ix].host = elt._id.host;
      arr[ix].client_ip = elt._id.client_ip;
      arr[ix].path = elt._id.path;
    });
    $scope.rowCollection = angular.fromJson(data);
    window.aa = angular.fromJson(data);

  });


  $scope.displayCollection = [].concat($scope.rowCollection);
  $scope.itemsByPage = 15;
  $scope.predicates = [{key: "Filtrer par Host", "value": "host"}, {
    key: "Filtrer par User IP",
    "value": "client_ip"
  }, {key: "Filtrer par URI Path", "value": "path"}];
  $scope.selectedPredicate = $scope.predicates[0].value;
  $scope.isCollapsed = true;
  // Modal: called by "supprimer site"
  $scope.openDialog = function (rules, size) {

    var $modalInstance = $modal.open({
      templateUrl: 'rulesModalDialog',
      controller: 'ShowDialogCtrl',
      size: size,
      resolve: {
        rules: function () {
          return rules;
        }
      }

    });
  }
}]);

//show rules modal controller
myApp.controller('ShowDialogCtrl', function ($scope, $modalInstance, rules,Settings) {
  $scope.rules = rules;
  Settings.get(function (data) {
    $scope.descs={};
    data.whitelistRules.forEach(function (elt, ix, arr) {
      $scope.descs[elt.id]=elt.desc;
    });
  });
  //dismiss and quit the modal dialog;
  $scope.cancel = function () {
    $modalInstance.dismiss('cancel');
  };
});

//experimental feature : filter highlighting match zone in requests
myApp.filter('highlight', function ($sce) {
  return function (text, phrase) {
    if (phrase) text = text.replace(new RegExp('(' + phrase + '=[^& ]*)', 'gi'),
      '<span class="badge bg-primary">$1</span>')

    return $sce.trustAsHtml(text)
  }
})

/*date picker controller logic*/

myApp.controller('DatepickerBCtrl',['$scope','Error', function ($scope, Error) {
  $scope.today = function (dt) {
    $scope[dt] = new Date();
  };
  $scope.today();

  $scope.clear = function (dt) {
    $scope[dt] = null;
  };

  $scope.getLog = function () {
    Error.query({from: $scope.dtFrom, to: $scope.dtTo}, function (data) {
      data.forEach(function (elt, ix, arr) {
        arr[ix].time = new Date(Date.parse(elt._id.time.$date)).toLocaleString('en-US', { hour12: false });
        arr[ix].host = elt._id.host;
        arr[ix].client_ip = elt._id.client_ip;
        arr[ix].path = elt._id.path;
      });
      $scope.$parent.rowCollection = angular.fromJson(data);
    });
  };

  $scope.open = function ($event, opened) {
    $event.preventDefault();
    $event.stopPropagation();

    $scope[opened] = opened;
  };

  $scope.dateOptions = {
    formatYear: 'yy',
    startingDay: 1
  };


  $scope.format = 'MM/dd/yyyy, HH:mm:ss';
}]);
