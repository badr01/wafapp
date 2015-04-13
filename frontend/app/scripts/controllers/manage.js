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

myApp.controller('WlModalCtrl',function($scope,$modalInstance,site,Site,$http){
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
  $scope.ids = [];
  $scope.zones=[
    {value:"all",key:"Toute les zones"},
    {value:"ARGS",key:"Args"},
    {value:"$ARGS_VAR",key:"Variables args"},
    {value:"$ARGS_VAR_X",key:"Variables args regex"},
    {value:"HEADERS",key:"Headers"},
    {value:"$HEADERS_VAR",key:"Variables header"},
    {value:"$HEADERS_VAR_X",key:"Variables header regexp"},
    {value:"BODY",key:"Body"},
    {value:"$BODY_VAR",key:"Variables body"},
    {value:"$BODY_VAR_X",key:"Variables body regexp"},
    {value:"$URL",key:"URL"},
    {value:"$URL_X",key:"URL regexp"},
    {value:"FILE_EXT",key:"Extension du fichier "}
  ];
  $scope.urls=[
    {value:"all",key:"Tout les URLs"},
    {value:"$URL",key:"URL"},
    {value:"$URL_X",key:"URL regexp"}
    ];
  $scope.urlFltr= $scope.urls[0];
  $scope.zone= $scope.zones[0];
  $scope.dsblurl= true;
  $scope.dsblzone= true;

  $scope.isDsbl=function(){
    if( $scope.dsblurl.value=="all") {
      $scope.dsblurl =true;
      $scope.urlContent="";
    }else {$scope.dsblurl =false;}
    if($scope.zone.value=="all"||$scope.zone.value=="BODY"||$scope.zone.value=="HEADERS"||$scope.zone.value=="ARGS"||$scope.zone.value=="FILE_EXT") {
      $scope.dsblzone = true;
      $scope.zoneContent = "";
    }else{ $scope.dsblzone=false;}
  }
  ;
  window.ab={"dsbl":$scope.dsblzone,"zone": $scope.zone};
  window.ac={"dsbl":$scope.dsblurl,"zone": $scope.urlFltr};

  window.aa=$scope.ids;

  $scope.loadRules = function($query) {
    return $http.get('data.json', { cache: true}).then(function(response) {
      var rules = response.data.filter(function(country) {
        return country.id.indexOf($query) != -1;
      });
      return rules;
    });
  };
  $scope.option={
    words: [{
      color: '#37F230',
      words: ['^BasicRule wl:[0-9]{1,4}(,[0-9]{1,4})*\\s"mz:(\\$?URL(_X)?(:[^|;]*)?)?(\\|?\\$?(ARGS|ARGS_VAR:[^|;]*|ARGS_VAR_X:[^|;]*|HEADERS|HEADERS_VAR:[^|;]*|HEADERS_VAR_X:[^|;]|BODY|BODY_VAR:[^|;]*|BODY_VAR_X:[^|;]|URL|URL:[^|;]*|URL_X:[^|;]*)(\\|NAME)?)?";$']
    }, {
      color: '#FF534F',
      words: ['^.*$']
    }]
  };
});
