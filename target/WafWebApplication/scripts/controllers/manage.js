'use strict';
var myApp =angular.module('mgcrea.WafApp');

myApp.factory("Site",function($resource){

  return $resource("/api/site/:domain");
});

myApp.controller('ListCtrl', function($scope, $modal,Site,$timeout) {
  $scope.update=function(){
    Site.query(function(data){
      $scope.sites=data;
    });
  };
  $scope.update();

  $scope.removeRecord=function(nomDomaine){
   $scope.openDialog(nomDomaine);

  };
    // Modal: called by "supprimer site"
  $scope.openDialog = function(domain) {

    var modalInstance = $modal.open({
      templateUrl: 'myModalDialog',
      controller: 'ModalDialogCtrl'

    });
    modalInstance.result.then(function(){
      Site.delete({domain:domain});
      $timeout(function(){
        $scope.update();
      },200);
    });
  };
    // Modal: called by "modifier site " and Add new site
    $scope.open = function(domain) {

      var modalInstance = $modal.open({
        templateUrl: 'add_site_modal',
        controller: 'ModalCtrl',
        resolve: {
          domain: function () {
            return domain;
          },
          modif: function () {
            return true;
          }
        }
      });
      modalInstance.result.then(function(){
        $timeout(function(){
          $scope.update();
        },1000);
      });
    };


});
myApp.controller('ModalDialogCtrl', function($scope, $modalInstance) {
  $scope.cancel = function() {
    $modalInstance.dismiss('cancel');
  };
  $scope.ok = function() {
    $modalInstance.close();
  };
});
  // modal that controls "Ajouter site" and "enregistrer site"
myApp.controller('ModalCtrl', function($scope, $modalInstance, domain,Site,modif) {
  if(typeof domain !== 'undefined') {
    Site.get({domain: domain}, function (data) {
      $scope.site = data;
    });
    $scope.modif = modif;
  }else{
    $scope.modif =false;
  }
  //cancel and quit the modal dialog;
  $scope.cancel = function() {
    $modalInstance.dismiss('cancel');
  };

  // Add new domain
  $scope.add = function() {
    Site.save($scope.site);
    $modalInstance.close();
     };

  // Save edited domain.
  $scope.save = function() {
    Site.save($scope.site);
    $modalInstance.close();

  };
});
