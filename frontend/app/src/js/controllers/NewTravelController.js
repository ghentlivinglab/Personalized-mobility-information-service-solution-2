app.controller('NewTravelController', [
  '$scope',
  '$uibModalInstance',
  'travelService',
  'routeService',
  'user',
  'sharedService',
  'addressService',
  'errorService',
  'localStorageService',
  function(
    $scope,
    $uibModalInstance,
    travelService,
    routeService,
    user,
    sharedService,
    addressService,
    errorService,
    localStorageService) {

      var header = {'Authorization': localStorageService.get("accessToken").token};
      /**
      * Init all the needed parameters to be able to add a new travel
      */
      sharedService.initTravelReg($scope);

      $uibModalInstance.rendered.then(function(){
        /**
        * Get the map that is rendered with the opening of the modal
        */
        $scope.map = $uibModalInstance.map;

        /**
        * Create a directions renderer for showing the directions on the map and a directions Serivce for asking directions to google
        */
        $scope.directionsDisplay = addressService.initDirectionsRenderer($scope.map, true);
        $scope.directionsService = addressService.initDirectionsService($scope, $scope.travel, $scope.route, $scope.waypoints);
      });

      $scope.close = function() {
        $uibModalInstance.dismiss("Closed new travel modal");
      };

      $scope.register = function() {
        /**
        * Add only the event types that are relevant with the transportation type to the route
        */
        sharedService.setEventTypeRelevance($scope.route);
        /**
        * Find the possible waypoints given by the user and add them to the route
        */
        sharedService.findWaypoints($scope.travel, $scope.route, $scope.directionsDisplay).then(
          function(result){
            /**
            * Save the travel
            */
            travelService(header).save({u: user.id}, $scope.travel).$promise.then(
              /**
              * If the travel has successfully been saved, we go back to the user index page
              */
              function(res) {
                routeService(header).save({u: user.id, travelId: res.id}, $scope.route).$promise.then(

                  /**
                  * If the route has successfully been saved, we go back to the user index page
                  */
                  function(result) {
                    $uibModalInstance.close([res, result]);
                  },

                  /**
                  * If there is an error, we handle it.
                  */
                  function(error) {
                    if (error.status === 401) {
                      sharedService.renewToken();
                      $scope.register();
                    } else {
                      errorService.handleRouteError(error, $scope);
                    }
                  }
                );
              },
              /**
              * If there is an error, we handle it.
              */
              function(error) {
                if (error.status === 401) {
                  sharedService.renewToken();
                  $scope.register();
                } else {
                  errorService.handleTravelError(error, $scope);
                }
              }
            );
          },
          function(error){
            console.error("Could not set the waypoints");
          }
        );
      };


      /**
      * Help function to determine if a valid address had been entered. More particular, we check if a housnumber has been entered
      *
      * @param  address  The address object given by google autocomplete when filling in the address in the form.
      */
      $scope.isAddressValid = function(address) {
        return addressService.validateAddress(address);
      };

      /**
      * Call the function in the shared service setting the start address of the current route
      */
      $scope.setStartAddress = function(travel) {
        addressService.setAddress(travel.startpoint, $scope.travelInfo.startAddress);

      };

      /**
      * Call the function in the shared service setting the end address of the current route
      */
      $scope.setEndAddress = function(travel) {
        addressService.setAddress(travel.endpoint, $scope.travelInfo.endAddress);
      };

    }]);
