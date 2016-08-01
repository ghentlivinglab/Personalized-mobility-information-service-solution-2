/**
 * locationEventService is used to get all the events that are relevant to a specific location of a user
 */
app.factory('locationEventService', ['$http', '$resource', function($http, $resource) {
  return function(customHeaders) {
    // return $resource('https://vopro6.ugent.be/api/user/:userId/point_of_interest/:locationId/events', {userId: '@id', locationId: '@id'},
    // return $resource('http://server-thuis.no-ip.biz:8080/user/:userId/point_of_interest/:locationId/events', {userId: '@id', locationId: '@id'},
    return $resource('http://localhost:8080/user/:userId/point_of_interest/:locationId/events', {userId: '@id', locationId: '@id'},
    {
      getEvents: {
          method: 'GET',
          isArray: true,
          headers: customHeaders || {}
      }
    });
  };
}]);
