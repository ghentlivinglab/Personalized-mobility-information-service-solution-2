app.factory('adminService', ['$resource', function($resource) {
  return function(customHeaders) {
    // return $resource('https://vopro6.ugent.be/api/admin/admin', {},
    // return $resource('http://server-thuis.no-ip.biz:8080/admin/admin', {},
    return $resource('http://localhost:8080/admin/admin', {},
    {
        save: {
            method: 'POST',
            headers: customHeaders || {}
        },
        getAdmins: {
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
