/**
 * Created by laassiri on 17/04/15.
 */
'use strict';
//rest api services
myApp
  .factory("Site", ['$resource',function ($resource) {

  return $resource("./api/site/:domain");
}])
  .factory("Status", ['$resource',function($resource) {
  return $resource("./api/terminal",{cmd:'@cmd'},{query: { method: "GET", isArray: false }});
}])
  .factory("Error",['$resource', function($resource) {
  return $resource("./api/logs/error",{from:'@from',to:'@to'});
}])
  .factory("Access",['$resource', function($resource) {
  return $resource("./api/logs/access",{from:'@from',to:'@to'});
}])
  .factory("Update",['$resource', function ($resource) {

  return $resource("./api/update");
}])
  .factory("Backups",['$resource', function ($resource) {
  return $resource("./api/backups");
}])
  .factory("Restore",['$resource', function ($resource) {
  return $resource("./api/backups/restore/:key");
}])
  .factory("Settings", ['$resource',function ($resource) {
  return $resource("/api/settings");
}]);
