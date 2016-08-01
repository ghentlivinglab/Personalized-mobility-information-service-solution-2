app.factory('addressService', ['$http', '$timeout', function($http, $timeout) {

  var service = {};

  /********************************************************************************
  ** Functions for google maps, calculating directions and displaying directions **
  *********************************************************************************/

  /**
  * Function that will initialize a map and center it at Ghent
  *
  * @param    map_id  The id of the div element in the view in which we need to initialize the map
  * @return           The newly created map
  */
  service.initMap = function(map_id) {
    var mapOptions = {
      zoom: 12,
      center: new google.maps.LatLng(51.055173, 3.715166),
      mapTypeId: google.maps.MapTypeId.ROADMAP,
      draggable: true,
      fullscreenControl: true,
      keyboardShortcuts: false
    };

    return new google.maps.Map(document.getElementById(map_id), mapOptions);
  };

  /**
  * Function that will create and return a directions rederer for the map
  *
  * @param  map The map for on which the directions renderer needs to draw the routes
  * @param  drag  Wether the rendered route can be dragged or not
  * @return       The directions renderer
  */
  service.initDirectionsRenderer = function(map, drag) {
    return new google.maps.DirectionsRenderer({
      draggable: drag,
      suppressBicyclingLayer: true,
      map: map
    });
  };
  /**
  * Function that will create and return a directions service for the map. This will calculate the route
  *
  * @param    scope       The scope of the calling controller
  * @param    travel      The current travel for which we need to calculate routes
  * @param    waypoints   An array containing possible waypoints for a route
  * @return               The directions renderer
  */
  service.initDirectionsService = function(scope, travel, route, waypoints) {
    var directionsService = new google.maps.DirectionsService();
    service.updateDirections(scope, directionsService, travel, route, waypoints);
    return directionsService;
  };

  /**
  * Function that will watch the start and end address and update the directions dynamically
  *
  * @param    scope               The scope of the calling controller
  * @param    DirectionsService   The directionsService that calculates the routes for the given travel and controller
  * @param    travel              The current travel for which we need to calculate and show routes on the map
  * @param    waypoints           Array containing possible waypoints that need to be shown on the map
  */
  service.updateDirections = function(scope, directionsService, travel, route, waypoints) {
    scope.$watch('[travel.startpoint.street, travel.endpoint.street, route.transportation_type]',
    function() {

      if (route.transportation_type !== undefined) {
        var transport = route.transportation_type;
        if (transport === 'bus' || transport === 'streetcar' || transport === 'train') {
          waypoints = [];
        }
      }

      /**
      * We dynamically change the request for the directions and display it on the map.
      * If the start and end address are not given in, we won't ask for directions. Otherwise we get an error
      */
      if (travel.startpoint !== undefined && travel.endpoint !== undefined) {
        /**
        * The request for the directions.
        */
        var start_housenumber;
        if (travel.startpoint.housenumber !== "") {
          start_housenumber = travel.startpoint.housenumber + " ";
        } else {
          start_housenumber = "";
        }

        var end_housenumber;
        if (travel.endpoint.housenumber !== "") {
          end_housenumber = travel.endpoint.housenumber + " ";
        } else {
          end_housenumber = "";
        }

        var request = {
          origin: travel.startpoint.street + " " +
          start_housenumber +
          travel.startpoint.postal_code + " " +
          travel.startpoint.city,

          destination: travel.endpoint.street + " " +
          end_housenumber +
          travel.endpoint.postal_code + " " +
          travel.endpoint.city,

          travelMode: service.findTravel_mode(route.transportation_type),
          waypoints: waypoints,
          provideRouteAlternatives: true,
          transitOptions: {
            modes: service.findTransit_mode(route.transportation_type)
          }
        };

        /**
        * We ask the directionsservice to create directions given the request parameters.
        * When directions are returned, we give them to the directionsrenderer which will display the directions on the map
        */
        directionsService.route(request, function(result, status) {
          if (status == google.maps.DirectionsStatus.OK) {
            scope.directionsDisplay.setDirections(result);
          }
        });
      }
    });
  };

  /**
  * Help function to find the correct travel mode for the directions service in google maps
  *
  * @param    mode    The travel mode given by the rest api
  * @return    mode    The correct travel mode in the correct syntax for google maps
  */
  service.findTravel_mode = function(mode) {
    if (mode == 'car') {
      return google.maps.TravelMode.DRIVING;
    } else {
      if (mode == 'bike') {
        return google.maps.TravelMode.BICYCLING;
      } else {
        return google.maps.TravelMode.TRANSIT;
      }
    }
  };

  /**
  * Help function to find the correct transit mode for the google maps directions service in google maps by checking the transport mode
  * given by the backend server
  *
  * @param    mode    The travel mode given by the rest api
  * @return   modes   Returns an array containing the correct transit mode in the correct syntax
  */
  service.findTransit_mode = function(mode) {
    var modes = [];

    switch(mode) {
      case "bus":
      modes.push(google.maps.TransitMode.BUS);
      break;
      case "train":
      modes.push(google.maps.TransitMode.TRAIN);
      break;
      case "streetcar":
      modes.push(google.maps.TransitMode.TRAM);
      break;
      default:
      break;
    }

    return modes;
  };

  /**
  * Function to get the postal code of the address because google doesn't always give the postal code with the results
  *
  * @param    address     The coordinate of the address for which we want the postal code
  * @param  geocoder  The geocoder with which we will geocode the postal codes
  * @return   postal      returns the postal code in string
  */
  service.retreivePostalCode = function(coordinate, geocoder) {
    var postal;
    geocoder.geocode({
      'location': coordinate
    }, function(results, status) {

      if (status === google.maps.GeocoderStatus.OK) {
        var result = results[0].address_components;

        for (var i = 0; i < result.length; ++i) {
          if (result[i].types[0] === "postal_code") {
            postal = result[i].long_name;
          }
        }
        return postal;
      }
    });
  };

  /**
  * Help function the set the address components of the object given to the function
  *
  * @param    addres              The object in which the address components (street, housenumber etc.) need to be set
  * @param    formatted_address   The object containing an address as given by google. It contains an array with address components
  */
  service.setAddress = function(address, formatted_address) {
    if (formatted_address !== undefined) {

      /**
      * Only set the address when the user has also given in a housenumber
      */
      if (formatted_address.address_components.length >= 4) {
        var components = formatted_address.address_components;

        /**
        * Loop to extract the components of the address
        */
        for (var i = 0; i < components.length; i++) {
          switch(components[i].types[0]) {
            case "street_number":
              address.housenumber = components[i].long_name;
              break;
            case "route":
              address.street = components[i].long_name;
              break;
            case "locality":
              address.city = components[i].long_name;
              break;
            case "country":
              address.country = components[i].short_name;
              break;
            case "postal_code":
              address.postal_code = components[i].long_name;
              break;
            default:
              break;
          }
        }

        if (address.housenumber === undefined) address.housenumber = "";

        address.coordinates = {};
        address.coordinates.lat = formatted_address.geometry.location.lat();
        address.coordinates.lon = formatted_address.geometry.location.lng();

        /**
        * Google does not always return the postal code (yeey) so in this case we have to ask for the postal code manually
        */
        if (address.postal_code === undefined) {
          var geocoder = new google.maps.Geocoder();
          address.postal_code = service.retreivePostalCode(formatted_address.geometry.location, geocoder);
        }
      }
    }
  };

  /**
  * Function to check the correctness of a given address.
  *
  * @param    address     Address object as given by google with the autocompletion
  * @return   true/false  return if the address is valid (= has a housenumber)
  */
  service.validateAddress = function(address) {
    if (address !== undefined) {
      return address.address_components.length >= 4;
    }
  };

  /**
  * Function to initialise the visualisation of a new location on the map
  */
  service.initNewLocationMap = function(scope, map) {
    service.initNewMarker(scope, map);
    service.initNewRadiusCircle(scope, scope.map, scope.marker, scope.locationInfo.radius, 'register');
    service.watchAddressMarker(scope);
    service.watchCircleRadius(scope);
  };

  /**
  * Function to initialise a maker needed for the location maps
  *
  * @param   map   The map on which the marker needs to be displayed
  * @return  New marker object with the default settings initialised
  */
  service.initNewMarker = function(scope, map) {
    scope.marker = new google.maps.Marker({
      map: map,
      anchorPoint: new google.maps.Point(51.055173, 3.715166),
      draggable: false,
      visible: false
    });
  };

  /**
  * Function that will show the map
  *
  * @param   scope   The scope of the calling controller
  * @param   map   The map on which the marker needs to be displayed
  * @param   address   The address in a formatted way "street number, postal_code city"
  * @param   radius  The ng-model object containing the radius of the location
  * @param   edit  Boolean that indicates wether we are editing or showing a location
  */
  service.initEditShowMap = function(scope, map, address, radius, edit) {
    var geocoder = new google.maps.Geocoder();

    geocoder.geocode({
      address: address
    }, function(results, status) {
      if (status == google.maps.GeocoderStatus.OK) {
        scope.marker = new google.maps.Marker({
          map: scope.map,
          position: results[0].geometry.location,
          draggable: false,
          visible: true
        });
        service.initNewRadiusCircle(scope, scope.map, scope.marker, radius, 'show');
        if (edit === true) {
          service.watchAddressMarker(scope);
          service.watchCircleRadius(scope);
        }
        scope.map.setCenter(scope.marker.position);
        scope.map.fitBounds(scope.circle.getBounds());
      }
    });
  };

  /**
  * Function to initialise a new circle object for drawing the radius of a location
  *
  * @param   map   The map on which the circle needs to br drawn
  * @param   marker  The marker around which we will draw a circle
  * @param   radius  The ng-modal containing the radius
  * @param   context   The context in which the circle is instantiated. Can exist of: register, show
  * @return    A new circle object that will draw on the map around the marker
  */
  service.initNewRadiusCircle = function(scope, map, marker, radius, context) {
    scope.circle = new google.maps.Circle({
      strokeColor: '#0066FF',
      strokeOpacity: 0.8,
      strokeWeight: 0,
      fillColor: '#0066FF',
      fillOpacity: 0.35,
      map: map,
      center: marker.position,
      radius: radius
    });

    if (context === 'register') {
      scope.circle.setVisible(false);
    } else {
      scope.circle.setVisible(true);
    }

  };

  /**
  * Function that will watch the current address of a location and change the marker position if needed
  */
  service.watchAddressMarker = function(scope) {
    scope.$watch('locationInfo.location', function() {
      if (scope.locationInfo.location !== undefined) {
        /**
        * Change the center of the map to be the marker
        */
        scope.map.setCenter(scope.locationInfo.location.geometry.location);

        /**
        * Set the new position of the marker and show it
        */
        scope.marker.setPosition(scope.locationInfo.location.geometry.location);
        scope.marker.setVisible(true);

        /**
        * Set the new position of the circle and show it
        */
        scope.circle.setCenter(scope.locationInfo.location.geometry.location);
        scope.circle.setVisible(true);

      }
    });
  };

  /**
  * Function that will watch the current address of an event and change the marker position if needed
  */
  service.watchEventAddressMarker = function(scope) {
    scope.$watch('[locationInfo.formatted_address, locationInfo2.formatted_address, event.type.type]', function() {
      /**
      * We need a timeout for the following reason:
      * We check if the marker1drag is false. When a marker is dragged, this will be set to true. But when a address has changed, this will be set to false.
      * However, the variable isn't switched fast enough so the marker doesn't change place when a new address is given in by the user.
      * So we need to wait al little for the ng-change function to be able to change the variable to false
      */
      $timeout(function() {
        if (scope.event !== undefined && scope.event.type !== undefined) {
          if (scope.locationInfo !== undefined && scope.markerdrag === false && (scope.event.type.type !== 'JAM' && scope.event.type.type !== 'ROAD_CLOSED')) {
            /**
            * Change the center of the map to be the marker
            */
            scope.map.setCenter(scope.locationInfo.geometry.location);

            /**
            * Set the new position of the marker and show it
            */
            scope.marker.setPosition(scope.locationInfo.geometry.location);
            scope.marker.setVisible(true);
            scope.directionsDisplay.setMap(null);
          }
          if (scope.locationInfo2 !== undefined && scope.locationInfo !== undefined && (scope.event.type.type === 'JAM' || scope.event.type.type === 'ROAD_CLOSED') && (scope.markerdrag === false || scope.jamdrag === false)) {
            scope.marker.setVisible(false);
            scope.directionsDisplay.setMap(scope.map);
            var request = {
              origin: scope.locationInfo.geometry.location,
              destination: scope.locationInfo2.geometry.location,
              travelMode: google.maps.TravelMode.DRIVING
            };

            scope.directionsService.route(request, function(result, status) {
              scope.directionsDisplay.setDirections(result);
            });
          }
        }
      });

    }, 100);

  };

  /**
  * Function that will watch the radius of a location and change the radius of the circle on the map accordingly
  */
  service.watchCircleRadius = function(scope) {
    scope.$watch('locationInfo.radius', function() {
      if (scope.marker.getVisible() === true) {
        scope.circle.setRadius(scope.locationInfo.radius);
        scope.map.fitBounds(scope.circle.getBounds());
      }
    });
  };

  return service;
}]);
