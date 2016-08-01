/**
 * routeEventService is used to get all the events that are relevant to a specific travel/route of a user
 */
app.factory('routeEventService', ['$http', '$resource', function($http, $resource) {
  return function(customHeaders) {
    // return $resource('https://vopro6.ugent.be/api/user/:userId/travel/:travelId/route/:routeId/events', {userId: '@id', travelId: '@id', routeId: '@id'},
    // return $resource('http://server-thuis.no-ip.biz:8080/user/:userId/travel/:travelId/route/:routeId/events', {userId: '@id', travelId: '@id', routeId: '@id'},
    return $resource('http://localhost:8080/user/:userId/travel/:travelId/route/:routeId/events', {userId: '@id', travelId: '@id', routeId: '@id'},
    {
      getEvents: {
          method: 'GET',
          isArray: true,
          headers: customHeaders || {}
      }
    });
  };
}]);
