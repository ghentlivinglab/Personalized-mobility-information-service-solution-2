/*
 * This service gets all events the user is interested in.
 */
app.controller('EventController', [
  '$scope',
  '$routeParams',
  'sharedService',
  'localStorageService',
  '$uibModal',
function(
  $scope,
  $routeParams,
  sharedService,
  localStorageService,
  $uibModal) {


  /*
   * Load all eventtypes.
   */
  $scope.eventTypes = sharedService.getTypes();

  /**
   * Call function that will load all the events that are relevant to a specific user, create a map
   * and add markers for each of these events
   */
  sharedService.loadUserEventsAndShow($scope, localStorageService.get("refreshToken").user_id);

  /**
   * Set the center of the map to the selected event and show to infoWindow
   */
  $scope.openMarker = function(index){
    $scope.map.setCenter($scope.markers[index].getPosition());
    google.maps.event.trigger($scope.markers[index],'mouseover');
  };

  $scope.confirmAndDelete = function(id){
    $scope.deleteIndex = id;
    $uibModal.open({
      templateUrl: 'js/directives/confirmModal.html',
      size:'lg',
      scope: $scope,
      bindToController: true
    });
  };
}]);
