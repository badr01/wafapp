'use strict';

myApp
  .factory("httpInterceptor", ["$q", "$window", "$log",
    function ($q, $window, $log) {
      return {
        "response": function (response) {
          var responseHeaders;
          responseHeaders = response.headers();
          if(responseHeaders["content-type"]!=undefined) {
            if (responseHeaders["content-type"]
                .indexOf("text/html") !== -1
              && response.data
              && response.data
                .indexOf('<meta name="unauthorized" content="true">')
              !== -1) {
              $window.location.reload();
              return $q.reject(response);
            }
          }
          return response;
        }
      };
    }
  ]);
