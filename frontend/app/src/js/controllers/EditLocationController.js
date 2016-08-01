app.controller('EditLocationController', [
  '$scope',
  '$uibModalInstance',
  'user',
  'location',
  'locationService',
  'addressService',
  'sharedService',
  'errorService',
  'localStorageService',
function(
  $scope,
  $uibModalInstance,
  user,
  location,
  locationService,
  addressService,
  sharedService,
  errorService,
  localStorageService){

    var header;

    $scope.initPage = function() {
      header = {'Authorization': localStorageService.get("accessToken").token};
      /**
       * Set the variables of the current location info
       */
      var housenumber;
      if (location.address.housenumber !== "") {
         housenumber = " " + location.address.housenumber;
      } else {
         housenumber = "";
      }

      $scope.currentAddress = location.address.street +
                             housenumber + ", " +
                             location.address.postal_code + " " +
                             location.address.city;

      $scope.locationInfo = {};
      $scope.locationInfo.name = location.name;
      $scope.locationInfo.radius = location.radius;
      $scope.locationInfo.active = location.active;

      /**
      * Get the map that is rendered when the modal has been rendered
      */
      $uibModalInstance.rendered.then(function(){
       /**
       * Get the map that is rendered with the opening of the modal
       */
       $scope.map = $uibModalInstance.map;
       /**
       * Call function initialising all the needed objects for showing a location
       */
       addressService.initEditShowMap($scope, $scope.map, $scope.currentAddress, $scope.locationInfo.radius, true);
      });
    };

    $scope.initPage();

    $scope.close = function() {
      $uibModalInstance.dismiss("Closed edit location modal");
    };

    $scope.register = function(){
      location.name = $scope.locationInfo.name;
      location.radius = $scope.locationInfo.radius;
      location.active = $scope.locationInfo.active;

      if( $scope.locationInfo.location !== undefined){
        location.address = {};
        addressService.setAddress(location.address, $scope.locationInfo.location);
      }

      locationService(header).update({userId: user.id, locationId: location.id}, location).$promise.then(
        /**
         * If the point of interest has successfully been saved to the user, we close the modal,
         */
        function(result) {
          $uibModalInstance.close(result);
        },
        /**
         * If there is an error, we handle it.
         */
        function(error) {
          if (error.status === 401) {
            sharedService.renewToken();
            $scope.register();
          } else {
            console.error(error);
            errorService.handlePointOfInterestError(error, $scope);
          }
        }
      );

    };


    $scope.isAddressValid = function(address) {
      return addressService.validateAddress(address);
    };

  }
]);
