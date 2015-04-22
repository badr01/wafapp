'use strict';
//site list controller
myApp.controller('ListCtrl', function ($scope, $modal, Site, $timeout) {
  $scope.update = function () {
    Site.query(function (data) {
      $scope.sites = data;
    });
  };
  $scope.update();

  $scope.removeRecord = function (nomDomaine) {
    $scope.openDialog(nomDomaine);

  };


  // Modal: called by "supprimer site"
  $scope.openDialog = function (domain) {

    var modalInstance = $modal.open({
      templateUrl: 'myModalDialog',
      controller: 'ModalDialogCtrl'
    });
    modalInstance.result.then(function () {
      Site.delete({domain: domain});
      $timeout(function () {
        $scope.update();
      }, 200);
    });
  };


  // Modal: called by "Afficher"
  $scope.openWL = function (site) {

    var modalInstance = $modal.open({
      templateUrl: 'wl_modal',
      controller: 'WlModalCtrl',
      size: 'lg',
      resolve: {
        site: function () {
          return site;
        }
      }
    });
  };


  // Modal: called by "modifier site " and Add new site
  $scope.open = function (domain) {

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
    modalInstance.result.then(function () {
      $timeout(function () {
        $scope.update();
      }, 1000);
    });
  };


});

//controls the removal confirmation modal dialog
myApp.controller('ModalDialogCtrl', function ($scope, $modalInstance) {
  $scope.cancel = function () {
    $modalInstance.dismiss('cancel');
  };
  $scope.ok = function () {
    $modalInstance.close();
  };
});

// modal that controls "Ajouter site" and "enregistrer site"
myApp.controller('ModalCtrl', function ($scope, $modalInstance, domain, Site, modif) {
  if (typeof domain !== 'undefined') {
    Site.get({domain: domain}, function (data) {
      $scope.site = data;
    });
    $scope.modif = modif;
  } else {
    $scope.modif = false;
  }
  //cancel and quit the modal dialog;
  $scope.cancel = function () {
    $modalInstance.dismiss('cancel');
  };

  // Add new domain
  $scope.add = function () {
    Site.save($scope.site);
    $modalInstance.close();
  };

  // Save edited domain.
  $scope.save = function () {
    Site.save($scope.site);
    $modalInstance.close();

  };
});


//whitelist modal controller

myApp.controller('WlModalCtrl', function ($scope, $modalInstance, site, Site, $http, growl) {
  //dismiss and quit the modal dialog;
  $scope.cancel = function () {
    $modalInstance.dismiss('cancel');
  };

  $scope.validate = function () {
    if ($scope.site["wlList"] == null)$scope.site["wlList"] = [];
    var rx = /BasicRule wl:[0-9]{1,4}(,[0-9]{1,4})*\s("mz:(\$?URL(_X)?(:[^|;]*)?)?(\|?\$?(ARGS|ARGS_VAR:[^|;]*|ARGS_VAR_X:[^|;]*|HEADERS|HEADERS_VAR:[^|;]*|HEADERS_VAR_X:[^|;]|BODY|BODY_VAR:[^|;]*|BODY_VAR_X:[^|;]|URL|URL:[^|;]*|URL_X:[^|;]*)(\|NAME)?)?")?;/;
    var wl = $scope.current == undefined ? "" : $scope.current;
    var wls = $scope.textarea == undefined ? [""] : $scope.textarea.split('\n');
    if (rx.test(wl) && $scope.site["wlList"].indexOf(wl) == -1) {
      $scope.site["wlList"].push(wl);
    }
    ;
    wls.forEach(function (elt, ix, array) {
       if (rx.test(elt) && $scope.site["wlList"].indexOf(elt) == -1) {
        $scope.site["wlList"].push(elt);
      }
    });
  };
  $scope.site = angular.copy(site);
  $scope.wls = $scope.site["wlList"];

  /*EXPERIMENTAL FEATURE  $scope.modifyWL= function (ix) {
   $scope.current=$scope.wls[ix];
   }*/

  $scope.removeWL = function (ix) {
    $scope.wls.splice(ix, 1);
  };

  $scope.ok = function () {
    Site.save($scope.site, function () {
      growl.success("Opération réussie");
    }, function () {
      growl.success("Opération échouée");
    });
    $modalInstance.close();
  };
  $scope.ids = [];

  $scope.zones = [
    {value: "all", key: "Toute les zones"},
    {value: "ARGS", key: "Args"},
    {value: "$ARGS_VAR", key: "Variables args"},
    {value: "$ARGS_VAR_X", key: "Variables args regex"},
    {value: "HEADERS", key: "Headers"},
    {value: "$HEADERS_VAR", key: "Variables header"},
    {value: "$HEADERS_VAR_X", key: "Variables header regexp"},
    {value: "BODY", key: "Body"},
    {value: "$BODY_VAR", key: "Variables body"},
    {value: "$BODY_VAR_X", key: "Variables body regexp"},
    {value: "$URL", key: "URL"},
    {value: "$URL_X", key: "URL regexp"},
    {value: "FILE_EXT", key: "Extension du fichier "}
  ];

  $scope.urls = [
    {value: "all", key: "Tout les URLs"},
    {value: "$URL", key: "URL"},
    {value: "$URL_X", key: "URL regexp"}
  ];

  $scope.urlFltr = $scope.urls[0];
  $scope.zone = $scope.zones[0];
  $scope.dsblurl = true;
  $scope.dsblzone = true;


  $scope.isDsbl = function () {
    if ($scope.urlFltr.value == "all") {
      $scope.dsblurl = true;
      $scope.urlContent = "";
    } else {
      $scope.dsblurl = false;
    }
    if ($scope.zone.value == "$URL" || $scope.zone.value == "$URL_X") {
      $scope.urlFltr = $scope.urls[0];
      $scope.dsblurl = true;
      $scope.urlContent = "";
    }
    if ($scope.zone.value == "all" || $scope.zone.value == "BODY" || $scope.zone.value == "HEADERS" || $scope.zone.value == "ARGS" || $scope.zone.value == "FILE_EXT") {
      $scope.dsblzone = true;
      $scope.zoneContent = "";
    } else {
      $scope.dsblzone = false;
    }
  };

  $scope.loadRules = function ($query) {
    return $http.get('/api/settings', {cache: true}).then(function (response) {
      var rules = response.data.whitelistRules.filter(function (ruules) {
        return ruules.id.indexOf($query) != -1;
      });
      return rules;
    });
  };

  //watch changes in inputs and reflect them in main input
  $scope.$watch('[urlFltr,zone,urlContent,zoneContent,chkName,ids]', function () {

    $scope.current = buildStrIds($scope.ids) + buildStrMz($scope.urlFltr, $scope.urlContent, $scope.zone, $scope.zoneContent, $scope.chkName) + ";";

  }, true);


  //useful functions
  var buildStrIds = function (ids) {
    if (ids.length != 0) {
      var str = "BasicRule wl:"
      ids.forEach(function (elt, ix, arr) {
        str += elt.id + ","
      });
      return str.substring(0, str.lastIndexOf(','));
    } else return "";
  };

  var buildStrMz = function (fltr, fltrc, zone, zonec, nm) {
    var str = "";
    if (fltr['value'] != "all" && zone['value'] != "$URL" && zone['value'] != "$URL_X") {
      str = " \"mz:" + fltr['value'] + (fltrc != undefined && fltrc != "" ? ":" + fltrc : "");
    } else if (fltr['value'] == "all" && zone['value'] != "$URL" && zone['value'] != "$URL_X") {
      str = " \"mz:";
    } else {
      str = " \"mz:URL|";
    }
    if (zone['value'] != "all" && zone['value'] != "$URL" && zone['value'] != "$URL_X" && fltr['value'] != "all") {
      str += "|" + zone['value'] + (zonec != undefined && zonec != "" ? ":" + zonec : "");
    } else if (zone['value'] != "all" && fltr['value'] == "all") {
      str += zone['value'] + (zonec != undefined && zonec != "" ? ":" + zonec : "");
    } else {
      str = "";
    }
    if (nm) {
      str += "|NAME";
    }
    return str != "" ? str += "\"" : str;
  };


  //rules highlighter options
  $scope.option = {
    words: [{
      color: '#37F230',
      words: ['^BasicRule wl:[0-9]{1,4}(,[0-9]{1,4})*\\s*("mz:(\\$?URL(_X)?(:[^|;]*)?)?(\\|?\\$?(ARGS|ARGS_VAR:[^|;]*|ARGS_VAR_X:[^|;]*|HEADERS|HEADERS_VAR:[^|;]*|HEADERS_VAR_X:[^|;]|BODY|BODY_VAR:[^|;]*|BODY_VAR_X:[^|;]|URL|URL:[^|;]*|URL_X:[^|;]*)(\\|NAME)?)?")?;$']
    }, {
      color: '#FF534F',
      words: ['^.*$']
    }]
  };
});

