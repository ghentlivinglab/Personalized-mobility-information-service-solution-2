app.factory('reverifyMailService', ['$http', '$resource', function($http, $resource) {

  // return $resource('https://vopro6.ugent.be/api/user/:u/verify', {u: '@id'},
  // return $resource('http://server-thuis.no-ip.biz:8080/user/:u/verify', {u: '@id'},
  return function(customHeaders) {
    return $resource('http://localhost:8080/user/:u/reverify_email', {u: '@id'},
    {
      save: {
        method: 'POST',
        headers: customHeaders || {}
      }
    });
  };

}]);
