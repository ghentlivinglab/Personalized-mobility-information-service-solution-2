app.factory('dataDumpService', ['$resource', function($resource) {
  return function(customHeaders) {
    // return $resource('https://vopro6.ugent.be/api/admin/data_dump', {},
    // return $resource('http://server-thuis.no-ip.biz:8080/admin/data_dump', {},
    return $resource('http://localhost:8080/admin/data_dump', {},
    {
        getDataDump: {
          method: 'GET',
          headers: customHeaders || {}
        }
    });
  };
}]);
