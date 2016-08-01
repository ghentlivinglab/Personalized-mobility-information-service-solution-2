app.factory('locationService', ['$http', '$resource', function($http, $resource) {
  return function(customHeaders) {
    // return $resource('https://vopro6.ugent.be/api/user/:userId/point_of_interest/:locationId', {userId: '@id', locationId: '@id'},
    // return $resource('http://server-thuis.no-ip.biz:8080/user/:userId/point_of_interest/:locationId', {userId: '@id', locationId: '@id'},
    return $resource('http://localhost:8080/user/:userId/point_of_interest/:locationId', {userId: '@id', locationId: '@id'},
    {
      get: {
        method: 'GET',
        headers: customHeaders || {}
      },
      getLocations: {
          method: 'GET',
          isArray: true,
          headers: customHeaders || {}
      },
      update: {
        method: 'PUT',
        headers: customHeaders || {}
      },
      save: {
        method: 'POST',
        headers: customHeaders || {}
      },
      remove: {
        method: "DELETE",
        headers: customHeaders || {}
      }
    });
  };
}]);
