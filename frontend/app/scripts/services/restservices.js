/**
 * Created by laassiri on 17/04/15.
 */
'use strict';
//rest api services
myApp
  .factory("Site", function ($resource) {

  return $resource("./api/site/:domain");
})
  .factory("Status", function($resource) {
  return $resource("./api/terminal",{cmd:'@cmd'},{query: { method: "GET", isArray: false }});
})
  .factory("Error", function($resource) {
  return $resource("./api/logs/error",{from:'@from',to:'@to'});
})
  .factory("Access", function($resource) {
  return $resource("./api/logs/access",{from:'@from',to:'@to'});
})
  .factory("Update", function ($resource) {

  return $resource("./api/update");
})
  .factory("Backups", function ($resource) {
  return $resource("./api/backups");
})
  .factory("Restore", function ($resource) {
  return $resource("./api/backups/restore/:key");
})
  .factory("Settings", function ($resource) {
  return $resource("/api/settings");
});
