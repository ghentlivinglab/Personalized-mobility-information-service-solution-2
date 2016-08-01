/**
 * userEventService is used to get all the events that are relevant to a specific user
 */
app.factory('userEventService', ['$http', '$resource', function($http, $resource) {
  return function(customHeaders) {
    // return $resource('https://vopro6.ugent.be/api/user/:userId/events', {userId: '@id'},
    // return $resource('http://server-thuis.no-ip.biz:8080/user/:userId/events', {userId: '@id'},
    return $resource('http://localhost:8080/user/:userId/events', {userId: '@id'},
    {
      getEvents: {
          method: 'GET',
          isArray: true,
          headers: customHeaders || {}
      }
    });
  };
}]);
