app.controller('NewEventController', [
  '$scope',
  '$uibModalInstance',
  '$timeout',
  'addressService',
  'sharedService',
  'eventService',
  'mapsService',
  'errorService',
  'localStorageService',
  function(
    $scope,
    $uibModalInstance,
    $timeout,
    addressService,
    sharedService,
    eventService,
    mapsService,
    errorService,
    localStorageService) {

    /**
     * Get all the possible eventTypes and store them in an array so the user can select a type
     */
    $scope.eventTypes = sharedService.getTypes();
    $scope.markerdrag = false;
    $scope.jamdrag = false;


    /**
     * Array that will contain the 1 (or 2) markers for giving the position of the event
     */

    addressService.watchEventAddressMarker($scope);
    /*
     * Function to close the modal whern the x is clicked
     */
    $scope.close = function() {
      $uibModalInstance.dismiss("Closed new event modal");
    };


    /**
     * Function that will be called when the modal is rendered and will initialize everything we need for showing markers and jam's
     */
    $scope.initializeMap = function() {
      /**
       * Get the map that is rendered with the opening of the modal
       */
      $scope.map = $uibModalInstance.map;
      /**
       * Initialize the 2 markers that can be displayed on the map when making a new event
       */
      $scope.marker = new google.maps.Marker({
        map: $scope.map,
        position: {
          lat: 51.0579824,
          lng: 3.7191563
        },
        draggable: true,
        visible: false
      });

      /**
       * Initialize directions renderer and display for showing a traffic jam
       */
      $scope.directionsService = new google.maps.DirectionsService();
      $scope.directionsDisplay = new google.maps.DirectionsRenderer({
        draggable: true,
        suppressBicyclingLayer: true,
        polylineOptions: {
          strokeColor: '#FF0000'
        }
      });

      /**
       * Check if the marker or jam has been dragged so the marker won't change position when changing event Type
       */
      google.maps.event.addListener($scope.marker, 'dragend', function() {
        $scope.markerdrag = true;
      });
      google.maps.event.addListener($scope.directionsDisplay, 'directions_changed', function() {
        $scope.jamdrag = true;
      });
    };

    $scope.register = function() {
      /**
       * Get the coordinates of the marker(s) and fill them in the correct fields
       */
      if ($scope.event.type.type === 'JAM' || $scope.event.type.type === 'ROAD_CLOSED') {
        $scope.event.jams = [];
        $scope.event.jams[0] = {};
        $scope.event.jams[0].line = [];
        for (var i = 0; i < $scope.directionsDisplay.directions.routes[0].overview_path.length; i++) {
          if (i === 0) {
            $scope.event.jams[0].line[i] = {
              lat: $scope.directionsDisplay.directions.routes[0].overview_path[i].lat(),
              lon: $scope.directionsDisplay.directions.routes[0].overview_path[i].lng()
            };
          }
          $scope.event.jams[0].line[i] = {
            lat: $scope.directionsDisplay.directions.routes[0].overview_path[i].lat(),
            lon: $scope.directionsDisplay.directions.routes[0].overview_path[i].lng()
          };
        }
        $scope.event.jams[0].publicationTime = sharedService.parseTime(new Date(), "yyyy-MM-dd'T'HH:mm:ss.sss");
        $scope.event.coordinates = {};
        $scope.event.coordinates.lat = $scope.directionsDisplay.directions.routes[0].legs[0].start_location.lat();
        $scope.event.coordinates.lon = $scope.directionsDisplay.directions.routes[0].legs[0].start_location.lng();
      } else {
        $scope.event.coordinates = {};
        $scope.event.coordinates.lat = $scope.marker.position.lat();
        $scope.event.coordinates.lon = $scope.marker.position.lng();
      }
      /**
       *  Make a default array with relevant_for_transportation types. This is mandatory for the API but is filled in by the backend for us
       */
      $scope.event.relevant_for_transportation_types = [];

      /**
       * Set the active boolean automatically on true
       */
      $scope.event.active = true;

      /**
       * Set the publication and last edited Time to Today
       */
      $scope.event.publication_time = sharedService.parseTime(new Date(), "yyyy-MM-dd'T'HH:mm:ss.sss");
      $scope.event.last_edit_time = $scope.event.publication_time;
      var place = mapsService.getLocation([$scope.event.coordinates]);
      place.then(function(result) {
        $scope.event.formatted_address = result[0].data.results[0].formatted_address;
        eventService({
          'Authorization': localStorageService.get('accessToken').token
        }).save($scope.event).$promise.then(
          function(result) {
            $uibModalInstance.close();
          },
          function(error) {
            if (error.status === 401) {
              sharedService.renewToken();
              $scope.register();
            } else {
              errorService.handleEventError(error, $scope);
            }
          }
        );
      });


    };


    $scope.isAddressValid = function(address) {
      return addressService.validateAddress(address);
    };


    /**
     * Only when the modal is rendered, we will render the map. This because of synchronisation reasons
     */
    $uibModalInstance.rendered.then(function() {
      $scope.initializeMap();
    });
  }
]);
