/*
 *  Mock to use the travelService in the tests.
 */
angular.module('AppMock').provider('travelServiceMock', [function() {

  this.$get = function($q) {
    return {
      query: function() {
        var deferred = $q.defer();
        return {$promise: deferred.promise};
      },
      getTravels: function() {
        var deferred = $q.defer();
        return {$promise: deferred.promise};
      },
      update: function() {
        var data = [
          {
            "coordinates" : {
              "lat" : "3.578",
              "lon" : "5,789"
            }
          }
        ];

        var deferred = $q.defer();
        deferred.resolve(data);
        return { $promise: deferred.promise };
      },
      remove: function() {
      	var deferred = $q.defer();
      	return {$promise: deferred.promise};
      }
    }
  };

}]);
