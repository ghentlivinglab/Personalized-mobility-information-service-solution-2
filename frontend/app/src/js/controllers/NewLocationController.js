app.controller('NewLocationController', [
 '$scope',
 '$uibModalInstance',
 'user',
 'locationService',
 'addressService',
 'sharedService',
 'errorService',
 'localStorageService',
function(
 $scope,
 $uibModalInstance,
 user,
 locationService,
 addressService,
 sharedService,
 errorService,
 localStorageService){

 /**
  * Set default radius to 1000 meters
  */
 $scope.locationInfo = {};
 $scope.locationInfo.radius = 1000;
 $scope.locationInfo.active = true;
 var header = {'Authorization': localStorageService.get("accessToken").token};

 /**
  * Get the map that is rendered when the modal has been rendered
  */
 $uibModalInstance.rendered.then(function(){
   /**
    * Get the map that is rendered with the opening of the modal
    */
   $scope.map = $uibModalInstance.map;

   /**
    * Call function initialising all the needed objects for showing a location on the map
    */
    addressService.initNewLocationMap($scope, $scope.map);
 });



 addressService.watchAddressMarker($scope);

  $scope.close = function() {
    $uibModalInstance.dismiss("Closed new location modal.");
  };

 $scope.register = function() {
  $scope.location  = {};
  $scope.location.name = $scope.locationInfo.name;
  $scope.location.radius = $scope.locationInfo.radius;
  $scope.location.active = $scope.locationInfo.active;
  $scope.location = sharedService.createPOI($scope.location, $scope.locationInfo.location);

  locationService(header).save({userId: user.id}, $scope.location).$promise.then(

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
             errorService.handlePointOfInterestError(error, $scope);
      }
          }
        );

 };


 $scope.isAddressValid = function(address) {
    return addressService.validateAddress(address);
 };

}]);
