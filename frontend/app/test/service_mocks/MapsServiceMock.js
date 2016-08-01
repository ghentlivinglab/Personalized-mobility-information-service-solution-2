/*
 *  Mock to use the mapsService in the tests.
 */
angular.module('AppMock').service('mapsServiceMock', ['$q', function($q) {

  return {
    getLocation: function() {
      var data = [
        {
          "data" : {
            "results" : [
              {
                "formatted_address" : "Hallo"
              }
            ]
          }
        }
      ];

      var deferred = $q.defer();
      deferred.resolve(data);
      return deferred.promise;
    },
    getLonLats: function() {
        var results = [];
        results[0] = {};
        results[0].data = {};
        results[0].data.results = [];
        results[0].data.results[0] = {}
        results[0].data.results[0].address_components = [];
        results[0].data.results[0].address_components[1] = {};
        results[0].data.results[0].address_components[1] = "Honingenveldstraat";
        results[0].data.results[0].address_components[2] = {};
        results[0].data.results[0].address_components[2] = "Gooik";
        results[0].data.results[0].geometry = {};
        results[0].data.results[0].geometry.location = {
          lng: "4.132607",
          lat: "50.792004"
        };
        var deferred = $q.defer();
        deferred.resolve(results);
        return deferred.promise;
       }
    }

}]);
