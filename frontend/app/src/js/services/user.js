/*
 * The user service factory will do all the communication with the server.
 * The controllers must call the correct function to get, post, delete or edit users.
 * The functions then will give the data to a callback function
 */
app.factory('userService', ['$http', '$resource', function($http, $resource) {
  return function(customHeaders) {
    // return $resource('https://vopro6.ugent.be/api/user/:u', {u: '@id'},
    // return $resource('http://server-thuis.no-ip.biz:8080/user/:u', {u: '@id'},
    return $resource('http://localhost:8080/user/:u', {u: '@id'},
    {
      get: {
        method: 'GET',
        headers: customHeaders || {}
      },
      getUsers: {
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
        headers: {}
      },
      remove: {
        method: "DELETE",
        headers: customHeaders || {}
      }
    });
  };

}]);
