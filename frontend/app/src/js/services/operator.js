app.factory('operatorService', ['$resource', function($resource) {
  return function(customHeaders) {
    // return $resource('https://vopro6.ugent.be/api/admin/operator', {},
    // return $resource('http://server-thuis.no-ip.biz:8080/admin/operator', {},
    return $resource('http://localhost:8080/admin/operator', {},
    {
        save: {
            method: 'POST',
            headers: customHeaders || {}
        },
        getOperators: {
          method: 'GET',
          isArray: true,
          headers: customHeaders || {}
        },
        remove: {
          method: 'PUT',
          headers: customHeaders || {}
        }
    });
  };
}]);
