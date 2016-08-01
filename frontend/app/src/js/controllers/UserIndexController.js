app.controller('UserIndexController', [
  '$scope',
  '$route',
  '$location',
  '$uibModal',
  '$timeout',
  '$filter',
  'userService',
  'eventService',
  'sharedService',
  'addressService',
  'localStorageService',
  'locationControlService',
  'travelControlService',
  'eventControlService',
  function(
    $scope,
    $route,
    $location,
    $uibModal,
    $timeout,
    $filter,
    userService,
    eventService,
    sharedService,
    addressService,
    localStorageService,
    locationControlService,
    travelControlService,
    eventControlService) {

      /**
      * Save the refreshtoken to see what role the user has
      */
      $scope.refreshToken = localStorageService.get("refreshToken");
      var header = {'Authorization': localStorageService.get("accessToken").token};

      /**
      * Function that initializes the user index page only if there is a user logged in.
      */
      $scope.initPage = function() {
        /**
        * If the user is not logged in, we redirect to the index page
        */
        if (localStorageService.get('accessToken') === null) {
          $location.path('/index');
        } else {
          /**
          * Set the name of the current html file we are in to be able to load the correct navbar elements
          */
          $scope.currentPage = $route.current.templateUrl;

          /**
          * Switch view depending on which button you click.
          */
          $scope.templates = [
            { name: "eventList", url: "view/event/eventList.html"},
            { name: "routeView", url: "view/user/routeContent.html" },
            { name: "locationView", url:"view/user/locationContent.html"}
          ];

          /**
          * Standard view is a list of current events.
          */
          $scope.template = $scope.templates[0];


          /**
          * Set the waypoints array
          */
          $scope.waypoints = [];

          sharedService.loadData($scope);

          $scope.$watch('currentUser', function(newValue, oldValue) {
            if ($scope.currentUser !== undefined) {
              if ($scope.currentUser.validated.email === false) {
                $uibModal.open({
                  templateUrl: 'view/user/verificationModal.html',
                  controller: 'ValidationReminderController',
                  resolve: {
                    user: function(){
                      return $scope.currentUser;
                    }
                  },
                  size: 'lg'
                });
              }
            }
          });
        }
      };

      $scope.initPage();


      $scope.logout = function() {
        localStorageService.clearAll();
        $location.path('/index');
      };

      /**
      * Change view
      */
      $scope.onClickTemplate = function(temp) {
        $scope.template = $scope.templates[temp];
        $scope.map = addressService.initMap('map-canvas');

        if (temp === 1 && $scope.currentUserTravels.length !== 0) {
          /**
          * Initialize the map and show the directions on the map when routecontent is loaded
          */
          $timeout(function() {
            $scope.directionsDisplay = addressService.initDirectionsRenderer($scope.map);
            $scope.directionsService = addressService.initDirectionsService($scope, $scope.currentUserTravels[$scope.travelId], $scope.currentTravelRoutes[$scope.routeId], $scope.waypoints);
          }, 150);
          $timeout(function() {
            sharedService.loadRouteEventsAndShow($scope, $scope.currentUser.id, $scope.currentUserTravels[$scope.travelId].id, $scope.currentTravelRoutes[$scope.routeId].id);
          }, 1000);
        }

        if(temp === 2 && $scope.currentUserLocations.length !== 0){
          /**
          * Initialize the map and show the directions on the map when locationContent is loaded
          */
          $timeout(function() {
            var currentAddress = $scope.currentUserLocations[$scope.locationId].address.street + " " + $scope.currentUserLocations[$scope.locationId].address.housenumber + ", " + $scope.currentUserLocations[$scope.locationId].address.postal_code + " " + $scope.currentUserLocations[$scope.locationId].address.city;
            addressService.initEditShowMap($scope, $scope.map, currentAddress, $scope.currentUserLocations[$scope.locationId].radius, false);
          }, 150);
          $timeout(function() {
            sharedService.loadLocationEventsAndShow($scope, $scope.currentUser.id, $scope.currentUserLocations[$scope.locationId].id);
          }, 1000);
        }

      };

      /**
      * Function to find on which index the given travelId is located in the view
      */
      $scope.findAndChangeTravelId = function(travelId) {
        travelControlService.findAndChangeTravelId($scope, travelId);
      };

      /**
      * Function to find on which index the given locationId is located in the view
      */
      $scope.findAndChangeLocationId = function(locationId) {
        locationControlService.findAndChangeLocationId($scope, locationId);
      };

      /**
      * Function to open the modal for the new route
      */
      $scope.openNewTravel = function() {
        travelControlService.openNewTravel($scope);
      };

      /**
      * Function to open a new modal for creating a new location
      */
      $scope.openNewLocation = function() {
        locationControlService.openNewLocation($scope);
      };

      $scope.openEditLocation = function(location) {
        locationControlService.openEditLocation($scope, location);
      };

      /**
      * Function to open the modal for editing a user
      */
      $scope.openEditUser = function() {
        var modalInstance = $uibModal.open({
          templateUrl: 'js/directives/editUserDirective.html',
          controller: 'EditUserController',
          resolve:{
            user: function() {
              return $scope.currentUser;
            }
          },
          size: 'lg'
        });

        modalInstance.result.then(
          // Success handeling function
          function(){
            $route.reload();
          },
          // Error handeling function
          function() {
            // TO DO
          }
        );
      };

      /**
      * Function to open the modal editing a travel
      */
      $scope.openEditTravel = function(travel) {
        travelControlService.openEditTravel($scope, travel);
      };

      $scope.openNewEvent = function(){
        eventControlService.openNewEvent();
      };

      $scope.openEditEvent = function(event){
        eventControlService.openEditEvent(event);
      };

      $scope.deleteEvent = function(eventId) {
        eventControlService.deleteEvent(eventId);
      };

      /**
      * Function to delete a travel
      */
      $scope.deleteTravel = function(id) {
        travelControlService.deleteTravel($scope, id);
      };

      /**
      * Function to delete a user
      */
      $scope.deleteUser = function() {
        userService({'Authorization': localStorageService.get("accessToken").token}).remove({u: $scope.currentUser.id}).$promise.then(
          function(result) {
            localStorageService.clearAll();
            $location.path('/index');
          },
          function(error) {
            if (error.status === 401) {
              sharedService.renewToken();
              $scope.deleteUser();
            } else {
              console.error('could not delete user');
            }
          }
        );
      };

      /**
      * Function to delete a location
      */
      $scope.deleteLocation = function(id) {
        locationControlService.deleteLocation($scope, id);
      };

      /**
      * A simple function to convert a boolean to Ja of nee
      */
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
