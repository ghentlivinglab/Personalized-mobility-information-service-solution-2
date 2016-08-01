angular.module('AppMock').service('adminServiceMock', ['$q', function($q) {
    return function(headers) {
        return {
            getAdmins: function() {
                var data = "test";
                var deferred = $q.defer();
                deferred.resolve(data);
                return {
                    $promise: deferred.promise
                };
            }
        };
    };
}]);
