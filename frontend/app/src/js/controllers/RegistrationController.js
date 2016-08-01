/*
This controller will handle all the logic in reference to the registration.
*/
app.controller('RegistrationController', [
  '$scope',
  '$filter',
  '$location',
  'sharedService',
  'addressService',
  'errorService',
  'registrationService',
  'travelControlService',
  'locationControlService',
  'localStorageService',
  'accessTokenService',
  'userService',
function(
  $scope,
  $filter,
  $location,
  sharedService,
  addressService,
  errorService,
  registrationService,
  travelControlService,
  locationControlService,
  localStorageService,
  accessTokenService,
  userService) {

  $scope.templates = [
    { name: "travel", url: "view/user/routeContent.html"},
    { name: "location", url: "view/user/locationContent.html"},
    { name: "info", url: "view/user/registrationInfo.html"}
  ];

  $scope.index = 0;
  $scope.template = $scope.templates[$scope.index];
  $scope.map = addressService.initMap('map-canvas');

  sharedService.loadData($scope);

  $scope.openNewLocation = function() {
    locationControlService.openNewLocation($scope);
  };

  $scope.openEditLocation = function(locationId) {
    locationControlService.openEditLocation($scope, locationId);
  };

  $scope.deleteLocation = function(locationId) {
    locationControlService.deleteLocation($scope, locationId);
  };

  $scope.findAndChangeLocationId = function(locationId) {
    locationControlService.findAndChangeLocationId($scope, locationId);
  };

  $scope.openNewTravel = function() {
    travelControlService.openNewTravel($scope);
  };

  $scope.openEditTravel = function(travel) {
    travelControlService.openEditTravel($scope, travel);
  };

  $scope.deleteTravel = function(travelId) {
    travelControlService.deleteTravel($scope, travelId);
  };

  $scope.findAndChangeTravelId = function(travelId) {
    travelControlService.findAndChangeTravelId($scope, travelId, $scope.currentUser.id);
  };

  $scope.onClickTemplate = function(temp) {
    $scope.template = $scope.templates[temp];
    $scope.map = addressService.initMap('map-canvas');

    // Initialize the map and show the directions on the map when routecontent is loaded
    if (temp === 0 && $scope.currentUserTravels.length !== 0) {
        $scope.directionsDisplay = addressService.initDirectionsRenderer($scope.map, true);
        $scope.directionsService = addressService.initDirectionsService($scope, $scope.currentUserTravels[$scope.travelId], $scope.currentTravelRoutes[$scope.routeId], $scope.waypoints);
    }

    // Initialize the map and show the directions on the map when locationContent is loaded
    if (temp === 1 && ($scope.currentUserLocations.length !== 0)) {
        var currentAddress = $scope.currentUserLocations[$scope.locationId].address.street + " " +
        $scope.currentUserLocations[$scope.locationId].address.housenumber + ", " + $scope.currentUserLocations[$scope.locationId].address.postal_code + " " + $scope.currentUserLocations[$scope.locationId].address.city;

        addressService.initEditShowMap($scope,
                                       $scope.map,
                                       currentAddress,
                                       $scope.currentUserLocations[$scope.locationId].radius,
                                       false);
      }
    };

    $scope.register = function() {
      localStorageService.remove("registrationUser");
      registrationService.clearRegistration();
      $location.path('/userindex');
    };

    /**
    * Cancel the registration, delete user account and return to the index page
    */
    $scope.cancel = function() {
      var accessToken = localStorageService.get("accessToken");
      var header = {
        'Authorization': accessToken.token
      };

      userService(header).remove({u: $scope.currentUser.id}).$promise.then(
        function(result) {
          localStorageService.clearAll();
          registrationService.clearRegistration();
          $location.path('/index');
        },
        function(error) {
          if (error.status === 401) {
            sharedService.renewToken();
            $scope.cancel();
          } else {
            errorService.handleUserError($scope, error);
          }
        }
      );
    };

    $scope.convertTrueFalse = function(input){
      return input ? 'Ja' : 'Neen';
    };

    /**
     * Function to translate the transport type given by the backend to dutch
     * @param  input  A string containing the transport type in english
     */
    $scope.translateTransportType = function(input) {
        var transportTypes = sharedService.getTransportTypes();
        var type = $filter('filter')(transportTypes, {
            code: input
        }, true);
        return type[0].name;
    };

    /**
     * Function to convert time string given by the backend from format HH:mm:ss to HH:mm
     * @param time Time in string format HH:mm:ss
     */
    $scope.parseTime = function(time) {
        return time.slice(0, 5);
    };

    $scope.parseDays = function() {
        /**
         * Array containing all the days of the week in dutch
         */
        var weekdays = ["Ma", "Di", "Wo", "Do", "Vr", "Za", "Zo"];
        var days;
        for (var i = 0; i < $scope.currentUserTravels[$scope.travelId].recurring.length; i++) {
            if ($scope.currentUserTravels[$scope.travelId].recurring[i] === true) {
                if (days === undefined) {
                    days = weekdays[i];
                } else {
                    days += ", ";
                    days += weekdays[i];
                }
            }
        }
        return days;
    };

    $scope.parseAddress = function(address) {
      var housenumber;
      if (address.housenumber !== "") {
        housenumber = " " + address.housenumber;
      } else {
        housenumber = "";
      }

      return address.street + housenumber + ", " + address.city + " " + address.postal_code + " " + address.country;
    };
  }
]);
