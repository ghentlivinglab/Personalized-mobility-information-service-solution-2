app.factory('travelService', ['$http', '$resource', function($http, $resource) {
  return function(customHeaders) {
    // return $resource('https://vopro6.ugent.be/api/user/:u/travel/:travelId', {u: '@id', travelId: '@id'},
    // return $resource('http://server-thuis.no-ip.biz:8080/user/:u/travel/:travelId', {u: '@id', travelId: '@id'},
    return $resource('http://localhost:8080/user/:u/travel/:travelId', {u: '@id', travelId: '@id'},
    {
      get: {
        method: 'GET',
        headers: customHeaders || {}
      },
      getTravels: {
          method: 'GET',
          isArray:true,
          headers: customHeaders || {}
      },
      update: {
       method:'PUT',
        headers: customHeaders || {}
      },
      save: {
        method: 'POST',
        headers: customHeaders || {}
      },
      remove: {
        method: "DELETE",
        headers: customHeaders || {}
      }
    });
  };

}]);
