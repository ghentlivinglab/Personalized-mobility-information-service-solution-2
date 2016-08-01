/*
 *  Mock to use the userService in the tests.
 */
angular.module('AppMock').provider('userServiceMock', [function() {

  this.$get = function($q) {
    return {
      get: function() {
        var deferred = $q.defer();
        return {$promise: deferred.promise};
      },
      query: function() {
        var deferred = $q.defer();
        return {$promise: deferred.promise};
      },
      remove: function() {
        var deferred = $q.defer();
        return {$promise: deferred.promise};
      },
      update: function() {
      	var deferred = $q.defer();
      	return {$promise: deferred.promise};
      },
      getUsers: function() {
        var deferred = $q.defer();
        return {$promise: deferred.promise};
      }
    };
  };

}]);
