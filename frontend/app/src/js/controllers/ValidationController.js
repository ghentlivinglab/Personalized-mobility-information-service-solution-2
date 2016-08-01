/**
* ValidationController sends the confirmation pin that can be find in the url immediately to the server
* to check if it is valid. If so, backend sets verified to true and the user can log in.
*/
app.controller('ValidationController', ['$scope', '$location', 'verifyService', 'localStorageService',
function($scope, $location, verifyService, localStorageService) {

  $scope.validation = function() {
    /**
    * Check if the url contains the needed parameters
    * string.indexOf(substr) > -1 checks if 'substr' is a substring of 'string'
    */
    if ($location.absUrl().indexOf("?user=") > -1 && $location.absUrl().indexOf("&pin=") > -1) {
      var userId = $location.search().user;
      var pin = $location.search().pin;

      var verifyObject = JSON.parse('{"email_verification_pin": "' + pin + '"}');

      verifyService.save({u: userId}, verifyObject).$promise.then(
        function(result) {
          $location.path('/index');
          // delete the search parameters from the path
          $location.search('user', null);
          $location.search('pin', null);
          // add a hash to the path if the user isn't already logged in
          var refreshToken = localStorageService.get("refreshToken");
          if (refreshToken === null) {
            $location.hash('login');
          }
        },

        /**
        * If there is an error, we handle it.
        */
        function(error) {
          console.error(error);
        }
      );
    }
  };

  $scope.validation();

}]);
