app.factory('eventTypeService', ['$http', '$resource', function($http, $resource) {

  // return $resource('https://vopro6.ugent.be/api/eventtype/:typeId', {typeId: '@id'});
  // return $resource('http://server-thuis.no-ip.biz:8080/eventtype/:typeId', {typeId: '@id'});
  return $resource('http://localhost:8080/eventtype/:typeId', {typeId: '@id'});
}]);
