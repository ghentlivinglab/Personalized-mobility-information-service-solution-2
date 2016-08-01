app.factory('adminDeleteUserService', ['$resource', function($resource) {
  return function(customHeaders) {
    // return $resource('https://vopro6.ugent.be/api/admin/user/:u', {u: '@id'},
    // return $resource('http://server-thuis.no-ip.biz:8080/admin/user/:u', {u: '@id'},
    return $resource('http://localhost:8080/admin/user/:u', {u: '@id'},
    {
        remove: {
          method: 'DELETE',
          headers: customHeaders || {}
        }
    });
  };
}]);
