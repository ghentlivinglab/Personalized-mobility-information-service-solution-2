/**
 * changePasswordService can be used to change the password from a specific user, given the userId.
 * The body needs to contain the old and new password.
 */
app.factory('changePasswordService', ['$http', '$resource',
function($http, $resource) {
  return function(customHeaders) {
    // return $resource('https://vopro6.ugent.be/api/user/:u/change_password', {u: '@id'},
    // return $resource('http://server-thuis.no-ip.biz:8080/user/:u/change_password', {u: '@id'},
    return $resource('http://localhost:8080/user/:u/change_password', {u: '@id'},
    {
      save: {
        method: 'POST',
        headers: customHeaders || {}
      }
    });
  };

}]);
