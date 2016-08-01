/*
 *  Mock to use the eventService in the tests.
 */
angular.module('AppMock').service('eventServiceMock', ['$q', function($q) {

  return {
    getAdmins: function(value) {
      var data = "test"
      /*var data = [
        {
          "coordinates" : {
            "lat" : "3.578",
            "lon" : "5,789"
          }
        }
      ];*/

      var deferred = $q.defer();
      deferred.resolve(data);
      return { $promise: deferred.promise };
    }
  };

}]);
