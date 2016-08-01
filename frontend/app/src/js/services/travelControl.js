app.factory('travelControlService', [
  '$location',
  '$uibModal',
  '$timeout',
  'routeService',
  'sharedService',
  'travelService',
  'addressService',
  'localStorageService',
  function(
    $location,
    $uibModal,
    $timeout,
    routeService,
    sharedService,
    travelService,
    addressService,
    localStorageService) {

      var service = {};
      /**
      * Function to find on which index the given travelId is located in the view
      */
      service.findAndChangeTravelId = function(scope, travelId) {
        for (var i = 0; i < scope.currentUserTravels.length; i++) {
          if (parseInt(scope.currentUserTravels[i].id) === parseInt(travelId)) {
            service.changeTravelId(scope, i);
          }
        }
      };

      /**
      * Function to change the current travel.
      */
      service.changeTravelId = function(scope, travelId) {
        var header = {'Authorization': localStorageService.get("accessToken").token};
        scope.waypoints = [];
        scope.travelId = travelId;
        if (scope.currentUserTravels.length !== 0) {
          routeService(header).getRoutes({
            u: scope.currentUser.id,
            travelId: scope.currentUserTravels[scope.travelId].id
          }).$promise.then(function(data) {
            scope.currentTravelRoutes = data;
            if (scope.currentTravelRoutes.length !== 0) {
              scope.routeId = 0;
              for (var j = 0; j < scope.currentTravelRoutes[0].waypoints.length; j++) {

                // stringify waypoint[i] and replace the string "lon" to "lng"
                var stringified = JSON.stringify(scope.currentTravelRoutes[0].waypoints[j]);
                stringified = stringified.replace('"lon"', '"lng"');

                /**
                * Parse the changed string back to JSON format
                */
                var waypoint = JSON.parse(stringified);
                scope.waypoints.push({
                  location: waypoint,
                  stopover: false
                });
              }
            }

            scope.directionsService = addressService.initDirectionsService(scope,
               scope.currentUserTravels[scope.travelId],
               scope.currentTravelRoutes[scope.routeId],
               scope.waypoints);

              $timeout(function() {
                sharedService.loadRouteEventsAndShow(scope, scope.currentUser.id, scope.currentUserTravels[scope.travelId].id, scope.currentTravelRoutes[scope.routeId].id);
              }, 1000);
            },
            function(error) {
              if (error.status === 401) {
                sharedService.renewToken();
                service.changeTravelId(scope, travelId);
              } else {
                console.error(error);
              }
            }
          );
        }
        return travelId;
      };


      /**
      * Function to open the modal for the new route
      */
      service.openNewTravel = function(scope) {
        var modalInstance = $uibModal.open({
          templateUrl: 'js/directives/newTravelDirective.html',
          controller: 'NewTravelController',
          resolve: {
            user: function(){
              return scope.currentUser;
            }
          },
          size: 'lg'
        });

        modalInstance.result.then(
          function(array) {
            var travel = array[0];
            var route = array[1];
            /**
            * Add the travel and route to the travelslist and routelist and set the current travel to the travel that was created
            */
            if (scope.currentTravelRoutes === undefined) { scope.currentTravelRoutes = []; }

            if (scope.currentUserTravels.length === 0 && scope.currentTravelRoutes.length === 0) {
              scope.travelId = 0;
              scope.routeId = 0;
              scope.currentUserTravels = [];
              scope.currentTravelRoutes = [];
              scope.currentUserTravels.push(travel);
              scope.currentTravelRoutes.push(route);
              scope.directionsDisplay = addressService.initDirectionsRenderer(scope.map, false);
            } else {
              scope.currentUserTravels.push(travel);
              scope.currentTravelRoutes.push(route);
            }

            service.findAndChangeTravelId(scope, travel.id);
            $location.path();
          },
          // Error handeling function
          function() {
            console.error("Could not create new travel");
          }
        );

        /**
        * Only when the modal is rendered, we can render the map. Otherwise the map won't show
        * When the modal has successfully been rendered, we initialize the map and add it to the scope of the modal
        */
        modalInstance.rendered.then(
          function(){
            modalInstance.map = addressService.initMap('map-canvas3');
          }
        );
      };

      /**
      * Function to open the modal editing a travel
      */
      service.openEditTravel = function(scope, travel) {
        var editTravel = angular.copy(travel);
        var modalInstance = $uibModal.open({
          templateUrl: 'js/directives/editTravelDirective.html',
          controller: 'EditTravelController',
          resolve: {
            user: function() {
              return scope.currentUser;
            },
            travel: function() {
              return editTravel;
            },
            map: function(){
              // console.log(scope.map2);
              return scope.map;
            }
          },
          size: 'lg'
        });

        /**
        * We check the result when the modal is closed and check for errors
        */
        modalInstance.result.then(
          function(travel) {
            service.findAndChangeTravelId(scope, travel.id);
            scope.currentUserTravels[scope.travelId] = travel;
          },
          function() {
            // TO DO
            console.error('Could not edit travel');
          }
        );

        /**
        * Only when the modal is rendered, we can render the map. Otherwise the map won't show
        * When the modal has successfully been rendered, we initialize the map and add it to the scope of the modal
        */
        modalInstance.rendered.then(
          function(){
            modalInstance.map = addressService.initMap('map-canvas2');
          }
        );
      };

      /**
      * Function to delete a travel
      */
      service.deleteTravel = function(scope, id) {
        var header = {'Authorization': localStorageService.get("accessToken").token};
        travelService(header).remove({u: scope.currentUser.id, travelId: id}).$promise.then(
          function(result) {
            /**
            * remove the travel in the currentUserTravels array and set the current travel to the first travel
            */
            for (var i = 0; i < scope.currentUserTravels.length; i++) {
              if (scope.currentUserTravels[i].id === id) {
                scope.currentUserTravels.splice(i, 1);
                scope.currentTravelRoutes.splice(i, 1);
              }
            }

            if (scope.currentUserTravels.length !== 0) {
              service.changeTravelId(scope, 0);
            } else {
              scope.map = addressService.initMap('map-canvas');
            }
            $location.path();
          },
          function(error) {
            if (error.status === 401) {
              sharedService.renewToken();
              service.deleteTravel(scope, id);
            } else {
              console.error('could not delete travel');
            }
          }
        );
      };

      return service;
    }]);
