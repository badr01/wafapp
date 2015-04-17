'use strict';
//settings controller
myApp
  .controller('SettingsController', function ($scope, Settings) {


    $scope.options = {
      nginxConfDir: "/etc/nginx/",
      sitesEnabledDir: "/etc/nginx/sites-enabled/",
      naxsiConfDir: "/etc/nginx/naxsi/naxsi_config/",
      haproxyConfDir: "/etc/haproxy/",
      siteCertDir: "/etc/nginx/certificate/",
      whitelistRules: [{
        "desc": "weird request, unable to parse",
        "id": "1"
      }, {
        "desc": "request too big, stored on disk and not parsed",
        "id": "2"
      }, {"desc": "invalid hex encoding, null bytes", "id": "10"}, {
        "desc": "unknown content-type",
        "id": "11"
      }, {"desc": "invalid formatted url", "id": "12"}, {
        "desc": "invalid POST format",
        "id": "13"
      }, {"desc": "invalid POST boundary", "id": "14"}, {"desc": "sql keywords", "id": "1000"}, {
        "desc": "double quote",
        "id": "1001"
      }, {"desc": "0x, possible hex encoding", "id": "1002"}, {
        "desc": "mysql comment (/*)",
        "id": "1003"
      }, {"desc": "mysql comment (*/)", "id": "1004"}, {
        "desc": "mysql keyword (|)",
        "id": "1005"
      }, {"desc": "mysql keyword (&&)", "id": "1006"}, {"desc": "mysql comment (--)", "id": "1007"}, {
        "desc": "semicolon",
        "id": "1008"
      }, {"desc": "equal in var, probable sql/xss", "id": "1009"}, {
        "desc": "parenthesis, probable sql/xss",
        "id": "1010"
      }, {"desc": "parenthesis, probable sql/xss", "id": "1011"}, {
        "desc": "simple quote",
        "id": "1013"
      }, {"desc": "comma", "id": "1015"}, {"desc": "mysql comment (#)", "id": "1016"}, {
        "desc": "http:// in var",
        "id": "1100"
      }, {"desc": "https:// in var", "id": "1101"}, {"desc": "ftp:// in var", "id": "1102"}, {
        "desc": "php:// in var",
        "id": "1103"
      }, {"desc": "sftp:// in var", "id": "1104"}, {"desc": "zlib:// in var", "id": "1105"}, {
        "desc": "data:// in var",
        "id": "1106"
      }, {"desc": "glob:// in var", "id": "1107"}, {"desc": "phar:// in var", "id": "1108"}, {
        "desc": "file:// in var",
        "id": "1109"
      }, {"desc": "double dot", "id": "1200"}, {"desc": "obvious probe", "id": "1202"}, {
        "desc": "obvious windows path",
        "id": "1203"
      }, {"desc": "obvious probe", "id": "1204"}, {"desc": "backslash", "id": "1205"}, {
        "desc": "html open tag",
        "id": "1302"
      }, {"desc": "html close tag", "id": "1303"}, {"desc": "[, possible js", "id": "1310"}, {
        "desc": "], possible js",
        "id": "1311"
      }, {"desc": "~ character", "id": "1312"}, {"desc": "grave accent !", "id": "1314"}, {
        "desc": "double encoding !",
        "id": "1315"
      }, {"desc": "utf7/8 encoding", "id": "1400"}, {
        "desc": "M$ encoding",
        "id": "1401"
      }, {"desc": "Content is neither mulipart/x-www-form..", "id": "1402"}, {
        "desc": "asp/php file upload!",
        "id": "1500"
      }]
    };
    $scope.editorEnabled = {
      nginxConfDir: false,
      sitesEnabledDir: false,
      naxsiConfDir: false,
      haproxyConfDir: false,
      siteCertDir: false,
      whitelistRules: false
    };

    $scope.editable = {
      nginxConfDir: "",
      sitesEnabledDir: "",
      naxsiConfDir: "",
      haproxyConfDir: "",
      siteCertDir: "",
      whitelistRules: []
    };

    var firststart = true;
    window.ar = $scope.isdsbl;
    $scope.$watch('options', function () {
      if (!firststart) {
        $scope.isdsbl = false;
      } else  firststart = false;
    }, true)
    $scope.enableEditor = function (elm) {
      $scope.editorEnabled[elm] = true;
      $scope.editable[elm] = $scope.options[elm];
    };

    $scope.disableEditor = function (elm) {
      $scope.editorEnabled[elm] = false;

    };

    $scope.save = function (elm) {
      $scope.options[elm] = $scope.editable[elm];
      $scope.disableEditor(elm);
    };
    $scope.removeWL = function (ix) {
      $scope.options['whitelistRules'].splice(ix, 1);
    }
    $scope.savewl = function () {
      var jsonarr = [];
      $scope.options['whitelistRules'].forEach(function (elt, ix, arr) {
        jsonarr.push(elt.id);
      });
      if (jsonarr.indexOf($scope.reg.id) == -1)
        $scope.options['whitelistRules'].push(angular.copy($scope.reg));
    }
    $scope.saveconf = function () {
      Settings.save($scope.options);
      $scope.isdsbl = true;
    }
  })
