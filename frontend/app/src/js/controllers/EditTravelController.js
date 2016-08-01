app.controller('EditTravelController', [
  '$scope',
  '$route',
  '$uibModalInstance',
  'user',
  'travel',
  'map',
  'travelService',
  'routeService',
  'sharedService',
  'addressService',
  'errorService',
  'localStorageService',
  function(
    $scope,
    $route,
    $uibModalInstance,
    user,
    travel,
    map,
    travelService,
    routeService,
    sharedService,
    addressService,
    errorService,
    localStorageService) {

      var header = {'Authorization': localStorageService.get("accessToken").token};

      /**
      * Wait until the modal is rendered
      */
      $scope.user = user;
      $scope.travel = travel;

      $scope.initRouteData = function() {
        routeService(header).getRoutes({u: user.id, travelId: $scope.travel.id}).$promise.then(function(data) {
          $scope.route = data[0];
          /**
          * Set the waypoints array
          */
          $scope.waypoints = sharedService.convertWaypoints($scope.route.waypoints);
          $scope.directionsDisplay = addressService.initDirectionsRenderer($scope.map, true);
          $scope.directionsService = addressService.initDirectionsService(
            $scope,
            $scope.travel,
            $scope.route,
            $scope.waypoints);
          }, function(error) {
            if (error.status === 401) {
              sharedService.renewToken();
              $route.reload();
            }
          });
        };

        $uibModalInstance.rendered.then(function() {
          /**
          * Get the map that is rendered with the opening of the modal
          */
          $scope.map = $uibModalInstance.map;

          /**
          * Create a directions renderer for showing the directions on the map
          * and a directions Serivce for asking directions to google.
          */
          $scope.initRouteData();
        });



        $scope.parseTime = function(time) {
          return time.slice(0, 5);
        };

        /**
        * set the user and travel to be accessible in the view
        */
        $scope.travelInfo = {};

        var start_housenumber;
        if ($scope.travel.startpoint.housenumber !== "") {
          start_housenumber = " " + $scope.travel.startpoint.housenumber;
        } else {
          start_housenumber = "";
        }

        var end_housenumber;
        if ($scope.travel.endpoint.housenumber !== "") {
          end_housenumber = " " + $scope.travel.endpoint.housenumber;
        } else {
          end_housenumber = "";
        }
        $scope.currentStartAddress = $scope.travel.startpoint.street +
        start_housenumber + ", " +
        $scope.travel.startpoint.postal_code + " " +
        $scope.travel.startpoint.city;

        $scope.currentEndAddress = $scope.travel.endpoint.street +
        end_housenumber + ", " +
        $scope.travel.endpoint.postal_code + " " +
        $scope.travel.endpoint.city;

        $scope.travel.time_interval[0] = $scope.parseTime($scope.travel.time_interval[0]);
        $scope.travel.time_interval[1] = $scope.parseTime($scope.travel.time_interval[1]);

        /**
        * Set the transport types for the selector in the form
        */
        $scope.transportTypes = sharedService.getTransportTypes();

        $scope.close = function() {
          $uibModalInstance.dismiss("Closed edit travel modal");
        };

        $scope.register = function(travel, route) {
          sharedService.setEventTypeRelevance($scope.route);

          /**
          * Find the possible waypoints given by the user when editing the route and save it
          */
          sharedService.findWaypoints(travel, route, $scope.directionsDisplay).then(
            function(result) {
              /**
              * At this point, the travel is valid and ready to be sent to the server
              */
              travelService(header).update({u: $scope.user.id}, travel).$promise.then(
                /**
                * If the travel has successfully been saved, we go back to the user index page
                */
                function(result) {
                  /**
                  * We post the updated route to the updated travel
                  */
                  routeService(header).update({u: user.id, travelId: result.id}, route).$promise.then(
                    function() {
                      $uibModalInstance.close(result);
                    },
                    function(error) {
                      if (error.status === 401) {
                        sharedService.renewToken();
                        $scope.register(travel, route);
                      } else {
                        errorService.handleRouteError(error, $scope);
                      }
                    }
                  );
                },
                /**
                * If there is an error, we will show it to the user
                */
                function(error) {
                  if (error.status === 401) {
                    sharedService.renewToken();
                    $scope.register(travel, route);
                  } else {
                    errorService.handleTravelError(error, $scope);
                  }
                }
              );

            },
            function(error){
              console.error("Could not set waypoints");
            }
          );
        };


        /**
        * Help function to determine if a valid address had been entered.
        * More particular, we check if a housnumber has been entered.
        * @param  address   The address object given by google autocomplete when filling in the address in the form.
        */
        $scope.isAddressValid = function(address) {
          return addressService.validateAddress(address);
        };

        /**
        * Call the function in the shared service setting the start address of the current route
        */
        $scope.setStartAddress = function() {
          $scope.travel.startpoint = {};
          addressService.setAddress($scope.travel.startpoint, $scope.travelInfo.startAddress);
        };

        /**
        * Call the function in the shared service setting the end address of the current route
        */
        $scope.setEndAddress = function() {
          $scope.travel.endpoint = {};
          addressService.setAddress($scope.travel.endpoint, $scope.travelInfo.endAddress);
        };

      }
    ]);
