/*
 *  Mock to use the locationService in the tests.
 */
angular.module('AppMock').provider('locationServiceMock', [function() {

  this.$get = function($q) {
    return {
      query: function() {
        var deferred = $q.defer();
        return {$promise: deferred.promise};
      },
      getLocations: function() {
        var deferred = $q.defer();
        return {$promise: deferred.promise};
      },
      update: function() {
        var deferred = $q.defer();
        deferred.resolve(data);
        return { $promise: deferred.promise };
      },
      remove: function() {
      	var deferred = $q.defer();
      	return {$promise: deferred.promise};
      }
    };
  };

}]);
