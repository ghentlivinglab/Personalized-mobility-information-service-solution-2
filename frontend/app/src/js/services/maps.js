/*
 * Factory to change the geocode to longitude and latitude
*/

app.factory('mapsService', ['$http', '$q', function($http, $q) {

 this.getLocation = function(array) {
  var deferred = $q.defer();
  var noOfCalls = array.length;
  var results = [];
  var called = 0;

  angular.forEach(array, function(item, key){
   // lon en lat moeten omgewisseld worden in juiste versie!
   $http.get('https://maps.googleapis.com/maps/api/geocode/json?latlng=' + item.lat + ',' + item.lon + '&key=AIzaSyBSEb7-qBPegmyEPD2poDx7MopVj0_LY2Q')
   .then(function(result) {
    results.push(result);
    called++;
    if(called === noOfCalls){
     deferred.resolve(results);
    }
   });
  });
  return deferred.promise;
 };

 return this;
}]);
