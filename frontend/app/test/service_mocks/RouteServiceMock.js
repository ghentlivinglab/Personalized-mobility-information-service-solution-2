/*
 *  Mock to use the routeService in the tests.
 */
angular.module('AppMock').provider('routeServiceMock', [function() {

  this.$get = function($q) {
    return {
      query: function() {
        var deferred = $q.defer();
        return deferred.promise;
      },
      getRoutes: function() {
        var deferred = $q.defer();
        return {$promise: deferred.promise};
      },
      remove: function() {
      	var deferred = $q.defer();
      	return {$promise: deferred.promise};
      }
    }
  };

}]);
