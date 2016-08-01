app.factory('errorService', [function() {

  return {
    handleLoginError: function(error, $scope) {
      switch (error.status) {
        case 404:
          $scope.error = "Het opgegeven e-mailadres bestaat niet";
          break;

        case 401:
          $scope.error = "Het opgegeven wachtwoord is incorrect";
          break;

        default:
          $scope.error = "Inloggegevens zijn incorrect";
          break;
      }
    },

    handlePointOfInterestError: function(error, $scope) {
      switch (error.status) {
        /*
         * Connection refused error
         */
        case -1:
          console.error("Could not connect to server: Connection refused");
          $scope.connectionRefused = true;
          break;

          /*
           * Internal server error
           */
        case 404:
          console.error("Error: " + error.statusText);
          $scope.POIUserNotExist = true;
          break;

          /*
           * Travel already exists
           */
        case 409:
          console.error("Error: " + error.statusText);
          $scope.POIAlreadyExists = true;
          break;

        default:
          console.error("Error: " + error.statusText);
          $scope.POIServerError = true;
      }
    },

    handleTravelError: function(error, $scope) {
      switch (error.status) {
        /*
         * Connection refused error
         */
        case -1:
          console.error("Could not connect to server: Connection refused");
          $scope.connectionRefused = true;
          break;

          /*
           * Internal server error
           */
        case 500:
          console.error("Error: " + error.statusText);
          $scope.travelServerError = true;
          break;

          /*
           * Travel already exists
           */
        case 404:
          console.error("Error: " + error.statusText);
          $scope.travelAlreadyExists = true;
          break;

        default:
          console.error("Error: " + error.statusText);
          $scope.travelServerError = true;
      }
    },

    handleRouteError: function(error, scope) {
      switch (error.status) {
        case -1:
          console.error("Could not connect to server: Connection refused");
          scope.connectionRefused = true;
          break;

        case 401:
          console.error("User not logged in");
          scope.userLoginError = true;
          break;

        case 403:
          console.error("User does not have the right privileges");
          scope.userPrivilegeError = true;
          break;

        case 404:
          console.error("User, travel or route does not exist");
          scope.resourceNotExisting = true;
          break;

        case 409:
          console.error("Route already exists");
          scope.routeAlreadyExists = true;
          break;

        default:
          console.error("Error:" + error.statusText);
          scope.routeServerError = true;
      }
    },

    handleUserError: function(error, $scope) {
      switch (error.status) {
        case -1:
          console.error("Could not connect to server: Connection refused");
          $scope.connectionRefused = true;
          break;

        case 401:
          console.error("The user is not logged in");
          $scope.userNotLoggedIn = true;
          break;

        case 403:
          console.error("The requested user is not the current user or the current user is not an Administrator");
          $scope.userNotAdmin = true;
          break;

        case 404:
          console.error("User does not exist");
          $scope.userDoesNotExist = true;
          break;

        case 409:
          console.error("The user already exists");
          $scope.userAlreadyExists = true;
          break;

        default:
          console.error("Error: " + error.statusText);
          $scope.userServerError = true;
      }
    },

    handleEventError: function(error, $scope) {
      switch (error.status) {
        case 401:
          $scope.usernotLoggdIn = true;
          break;
        case 403:
          $scope.userNotAuthorised = true;
          break;
        default:
          break;
      }
    },

    handleChangePasswordError: function(error, $scope) {
      switch (error.status) {
        case 401:
          $scope.usernotLoggdIn = true;
          break;
        case 403:
          $scope.wrongOldPwd = true;
          break;
        case 404:
          $scope.userDoesNotExist = true;
          break;
        default:
          $scope.userServerError = true;
      }
    }
  };

}]);
