app.factory('verifyService', ['$http', '$resource', function($http, $resource) {

  // return $resource('https://vopro6.ugent.be/api/user/:u/verify', {u: '@id'},
  // return $resource('http://server-thuis.no-ip.biz:8080/user/:u/verify', {u: '@id'},
  return $resource('http://localhost:8080/user/:u/verify', {u: '@id'},
  {
    update: {
     method:'PUT'
    }
  });

}]);
