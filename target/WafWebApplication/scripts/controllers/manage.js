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


    // Modal: called by "Afficher"
  $scope.openWL = function(site) {

    var modalInstance = $modal.open({
      templateUrl: 'wl_modal',
      controller: 'WlModalCtrl',
      size:'lg',
      resolve: {
        site: function () {
          return site;
        }
      }
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
      //saving modifications after dialog dismissal
      modalInstance.result.then(function(){
        $timeout(function(){
          $scope.update();
        },1000);
      });
    };


});


  //controls the removal confirmation modal dialog
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


//whitelidst modal controller

myApp.controller('WlModalCtrl',function($scope,$modalInstance,site,Site){
  //dismiss and quit the modal dialog;
  $scope.cancel = function() {
    $modalInstance.dismiss('cancel');
  };
  $scope.wls=site.wlList;

  $scope.site=site;
  $scope.modifyWL= function (wl) {
    $scope.current=wl;
  }
  $scope.ok=function(){
    if($scope.site.wlList==null)$scope.site.wlList=[];
    if($scope.current!=null) {
      $scope.site.wlList.push($scope.current);
      Site.save($scope.site);
    }
  }
});
