app.factory('locationControlService', [
  '$uibModal',
  '$timeout',
  '$location',
  'sharedService',
  'addressService',
  'locationService',
  'localStorageService',
  function(
    $uibModal,
    $timeout,
    $location,
    sharedService,
    addressService,
    locationService,
    localStorageService) {

  var service = {};
  /**
   * Function to find on which index the given locationId is located in the view
   */
  service.findAndChangeLocationId = function(scope, locationId) {
    for (var i = 0; i < scope.currentUserLocations.length; i++) {
      if (parseInt(scope.currentUserLocations[i].id) === parseInt(locationId)) {
        service.changeLocationId(scope, i);
      }
    }
  };

  /**
   * Function to change the current selected location.
   */
  service.changeLocationId = function(scope, locationId) {
    scope.locationId = locationId;

    if (scope.currentUserLocations !== undefined) {
      var currentAddress = scope.currentUserLocations[locationId].address.street + " " +
                           scope.currentUserLocations[locationId].address.housenumber + ", " +
                           scope.currentUserLocations[scope.locationId].address.postal_code + " " +
                           scope.currentUserLocations[scope.locationId].address.city;

      if (scope.marker !== undefined && scope.circle !== undefined) {
        scope.marker.setMap(null);
        scope.circle.setMap(null);
      }

      addressService.initEditShowMap(scope,
                                     scope.map,
                                     currentAddress,
                                     scope.currentUserLocations[locationId].radius,
                                     false);

      $timeout(function() {
        sharedService.loadLocationEventsAndShow(scope, scope.currentUser.id, scope.currentUserLocations[scope.locationId].id);
      }, 1000);
    }
  };

  /**
   * Function to open a new modal for creating a new location
   */
  service.openNewLocation = function(scope) {
    var modalInstance = $uibModal.open({
      templateUrl: 'js/directives/newLocationDirective.html',
      controller: 'NewLocationController',
      resolve: {
        user: function() {
          return scope.currentUser;
        }
      },
      size: 'lg'
    });

    /**
     * Add the location to the locationslist and set the current location to the location that was created
     */
    modalInstance.result.then(
      function(location) {
        if (scope.currentUserLocations === undefined) {
          scope.locationId = 0;
          scope.currentUserLocations = [];
          scope.currentUserLocations.push(location);
          scope.map = addressService.initMap('map-canvas');

          var currentAddress = scope.currentUserLocations[scope.locationId].address.street + " " +
                               scope.currentUserLocations[scope.locationId].address.housenumber + ", " +
                               scope.currentUserLocations[scope.locationId].address.postal_code + " " +
                               scope.currentUserLocations[scope.locationId].address.city;


          addressService.initEditShowMap(scope,
                                         scope.map,
                                         currentAddress,
                                         scope.currentUserLocations[scope.locationId].radius,
                                         false);
          if (scope.map !== undefined) {
            google.maps.event.trigger(scope.map, 'resize');
          }

        } else {
          scope.currentUserLocations.push(location);
        }

        service.findAndChangeLocationId(scope, location.id);
      },
      function(){
        console.error('Could not create new location');
      }
    );

    /**
     * Only when the modal is rendered, we can render the map. Otherwise the map won't show.
     * When the modal has successfully been rendered, we initialize the map and add it to the scope of the modal.
     */
    modalInstance.rendered.then(
      function() {
        modalInstance.map = addressService.initMap('map-canvas4');
      }
    );
  };

  service.openEditLocation = function(scope, location) {
    var modalInstance = $uibModal.open({
      templateUrl: 'js/directives/editLocationDirective.html',
      controller: 'EditLocationController',
      resolve: {
        user: function() {
          return scope.currentUser;
        },
        location: function() {
          return location;
        }
      },
      size: 'lg'
    });

    modalInstance.result.then(
      function(location) {
        service.findAndChangeLocationId(scope, location.id);
      },
      function(){
        console.error('Could not edit location');
      }
    );

    /**
     * Only when the modal is rendered, we can render the map. Otherwise the map won't show.
     * When the modal has successfully been rendered, we initialize the map and add it to the scope of the modal
     */
    modalInstance.rendered.then(
      function() {
        modalInstance.map = addressService.initMap('map-canvas5');
      }
    );
  };


  /**
   * Remove the location in the currentUserLocations array and set the current location to the first location
   */
  service.deleteLocation = function(scope, id) {
    var header = {'Authorization': localStorageService.get("accessToken").token};
    locationService(header).remove({userId: scope.currentUser.id, locationId: id}).$promise.then(
      function(result) {
        for (var i = 0; i < scope.currentUserLocations.length; i++) {
          if (parseInt(scope.currentUserLocations[i].id) === parseInt(id)) {
            scope.currentUserLocations.splice(i, 1);
          }
        }

        if (scope.currentUserLocations.length !== 0) {
          service.changeLocationId(scope, 0);
        } else {
          scope.map = addressService.initMap('map-canvas');
        }
      },
      function(error) {
        if (error.status === 401) {
          sharedService.renewToken();
          service.deleteLocation(scope, id);
        } else {
          console.error('could not delete location');
        }
      }
    );
  };

  return service;
}]);
