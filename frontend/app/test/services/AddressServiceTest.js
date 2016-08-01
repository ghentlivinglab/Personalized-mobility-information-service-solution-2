fdescribe('AddressService', function() {
  beforeEach(module('App'));
  var geocoder, marker, map;
  beforeEach(inject(function(
    $rootScope,
    $injector,
    $q,
    _$httpBackend_,
    _$timeout_,
    _$filter_,
    _$compile_,
    _sharedService_,
    _addressService_) {

    /**
     * Create the needed dependencies
     */
    $filter = _$filter_;
    $timeout = _$timeout_;
    scope = $rootScope.$new();
    sharedService = _sharedService_;
    addressService = _addressService_;
    scope.directionsDisplay = {
      setDirections: function(data) {
        return data;
      }
    };
    directionsService = new google.maps.DirectionsService();
    geocoder = new google.maps.Geocoder();

    /**
     * Create default travel and route object
     */
    travel = {
      "id": "3",
      "name": "test",
      "startpoint": {
        "id": "1",
        "street": "Honingenveldstraat",
        "city": "Gooik",
        "country": "BE",
        "coordinates": {
          "lon": 0.0,
          "lat": 0.0
        },
        "postal_code": "1755"
      },
      "endpoint": {
        "id": "2",
        "street": "Galglaan",
        "city": "Gent",
        "country": "BE",
        "coordinates": {
          "lon": 3.7186009,
          "lat": 51.0281817
        },
        "housenumber": "1",
        "postal_code": "9000"
      },
      "recurring": [false, false, false, false, false, false, false],
      "time_interval": ["08:00", "10:00"],
      "is_arrival_time": true
    };

    route = {
      "id": "3",
      "waypoints": [],
      "transportation_type": "train",
      "notify_for_event_types": [],
      "active": true
    };

    /**
     * Create a dummy marker object used in tests and spy on the functions
     */
    scope.marker = {};
    scope.marker.setPosition = function() {
      return;
    };
    scope.marker.setVisible = function() {
      return;
    };
    scope.marker.getVisible = function() {
      return true;
    };
    /**
     * Create a dummy circle object used in tests and spy on the functions
     */
    scope.circle = {};
    scope.circle.setCenter = function() {
      return;
    };

    scope.circle.setVisible = function() {
      return;
    };
    scope.circle.setRadius = function() {
      return;
    };
    scope.circle.getBounds = function() {
      return;
    };


    /**
     * Set the spies
     */
    spyOn(scope.directionsDisplay, 'setDirections').and.callThrough();
    spyOn(addressService, 'updateDirections').and.callThrough();
    spyOn(addressService, 'retreivePostalCode').and.callThrough();
    spyOn(addressService, 'initNewMarker').and.callThrough();
    spyOn(addressService, 'initNewRadiusCircle').and.callThrough();
    spyOn(addressService, 'watchAddressMarker').and.callThrough();
    spyOn(addressService, 'watchCircleRadius').and.callThrough();
    spyOn(scope.marker, 'setPosition').and.callThrough();
    spyOn(scope.marker, 'setVisible').and.callThrough();
    spyOn(scope.marker, 'getVisible').and.callThrough();
    spyOn(scope.circle, 'setVisible').and.callThrough();
    spyOn(scope.circle, 'setCenter').and.callThrough();
    spyOn(scope.circle, 'setRadius').and.callThrough();
    spyOn(scope.circle, 'getBounds').and.callThrough();
    var constructorSpy = spyOn(google.maps, 'Geocoder');
    geocoder = jasmine.createSpyObj('Geocoder', ['geocode']);
    constructorSpy.and.returnValue(geocoder);

    var constructorSpy2 = spyOn(google.maps, 'Marker');
    marker = jasmine.createSpyObj('Marker', ['setPosition', 'setVisible', 'getVisible']);
    constructorSpy2.and.returnValue(marker);

    var constructorSpy3 = spyOn(google.maps, 'Map');
    map = jasmine.createSpyObj('Map', ['']);
    constructorSpy3.and.returnValue(map);

    var constructorSpy4 = spyOn(google.maps, 'DirectionsRenderer');
    directionsRenderer = jasmine.createSpyObj('DirectionsRenderer', ['']);
    constructorSpy3.and.returnValue(directionsRenderer);
    /**
     * Create a fake Google Directions Service
     */
    spyOn(directionsService, 'route').and.callFake(function() {
      var fakeresult = arguments[0];
      var fakeStatus;

      /*
       * Fake error code to see if the directionsRenderes isn't called when the address is invalid
       */
      if (fakeresult.origin === 'false 10 1755 Gooik') {
        fakeStatus = google.maps.DirectionsStatus.ERROR;
      } else {
        fakeStatus = google.maps.DirectionsStatus.OK;
      }
      var callback = arguments[1];
      return callback(fakeresult, fakeStatus);
    });

    geocoder.geocode.and.callFake(function(request, callback) {
      var data = [{
        geometry: {
          location: "test"
        },
        address_components: [{
          types: [
            "postal_code"
          ]
        }]
      }];
      callback(data, google.maps.GeocoderStatus.OK);
    });
    marker.setPosition.and.callFake(function() {
      return;
    });
    marker.setPosition.and.callFake(function() {
      return;
    });
    marker.getVisible.and.callFake(function() {
      return;
    });
    /**
     * Create a fake geocoder service to be able to test the retreivePostalCode method
     */
    /*spyOn(geocoder, 'geocode').and.callFake(function(){
    	var fakeresult = [{address_components: [ {types: [ "postal_code" ], long_name: "9000"}]}];
    	var fakeStatus = google.maps.GeocoderStatus.OK;
    	var callback = arguments[1];
    	return callback(fakeresult, fakeStatus);
    });*/

  }));

  /**
   * Check if all the variables are defined
   */
  it('Parameters need to be defined', function() {
    expect($filter).toBeDefined();
    expect(scope).toBeDefined();
    expect(sharedService).toBeDefined();
    expect(addressService).toBeDefined();
    expect(scope.directionsDisplay).toBeDefined();
    expect(travel).toBeDefined();
    expect(directionsService).toBeDefined();
  });



  it('Should automatically start watching the start and end address when making a new directions service', function() {
    addressService.initDirectionsService(scope, travel, []);
    expect(addressService.updateDirections).toHaveBeenCalled();
  });

  it('Correctly handle the watch when updating directions (transport type)', function() {
    addressService.updateDirections(scope, directionsService, travel, route, []);

    /**
     * We change the transportation type
     */
    route.transportation_type = "car";

    /**
     * We manually trigger the watch function
     */
    scope.$apply();
    expect(scope.directionsDisplay.setDirections).toHaveBeenCalled();
  });

  it('Correctly handle the watch when updating directions (undefined)', function() {
    addressService.updateDirections(scope, directionsService, travel, route, []);
    /**
     * We change the transportation type
     */
    travel.startpoint = undefined;
    /**
     * We manually trigger the watch function
     */
    scope.$apply();
    expect(scope.directionsDisplay.setDirections).not.toHaveBeenCalled();
  });

  it('Correctly handle the watch when updating directions (startpoint street)', function() {
    travel.startpoint.housenumber = "";
    addressService.updateDirections(scope, directionsService, travel, route, []);
    /**
     * We change the street of the startpoint
     */
    travel.startpoint.street = "Jozef plateaustraat";

    /**
     * We manually trigger the watch function
     */
    scope.$apply();
    expect(scope.directionsDisplay.setDirections).toHaveBeenCalled();
  });

  it('Correctly handle the watch when updating directions (endpoint street)', function() {
    travel.endpoint.housenumber = "";
    addressService.updateDirections(scope, directionsService, travel, route, []);
    /**
     * We change the street of the endpoint
     */
    travel.endpoint.street = "Jozef plateaustraat";

    /**
     * We manually trigger the watch function
     */
    scope.$apply();
    expect(scope.directionsDisplay.setDirections).toHaveBeenCalled();
  });

  it('Correctly handle the watch when updating directions (startpoint housenumber)', function() {
    addressService.updateDirections(scope, directionsService, travel, route, []);
    /**
     * We change the housenumber of the startpoint
     */
    travel.endpoint.housenumber = "10";

    /**
     * We manually trigger the watch function
     */
    scope.$apply();
    expect(scope.directionsDisplay.setDirections).toHaveBeenCalled();
  });

  it('Correctly handle the watch when updating directions (endpoint housenumber)', function() {
    addressService.updateDirections(scope, directionsService, travel, route, []);
    /**
     * We change the housenumber of the endpoint
     */
    travel.endpoint.housenumber = "10";

    /**
     * We manually trigger the watch function
     */
    scope.$apply();
    expect(scope.directionsDisplay.setDirections).toHaveBeenCalled();
  });

  /**
   * We check if the directionsRenderer isn't called when the address is wrong and the directionsService does not return an OK status
   */
  it('Correctly handle the watch when updating directions (wrong address)', function() {
    travel.startpoint.street = 'false';
    addressService.updateDirections(scope, directionsService, travel, route, []);
    /**
     * We change the housenumber of the endpoint
     */
    travel.startpoint.housenumber = "10";

    /**
     * We manually trigger the watch function
     */
    scope.$apply();
    expect(scope.directionsDisplay.setDirections).not.toHaveBeenCalled();
  });

  /**
   * Check if the correct transit mode is selected from the transportation mode given by the server
   */
  it('Should find the correct transit mode', function() {
    expect(addressService.findTransit_mode("bus")).toEqual([google.maps.TransitMode.BUS]);
    expect(addressService.findTransit_mode("train")).toEqual([google.maps.TransitMode.TRAIN]);
    expect(addressService.findTransit_mode("streetcar")).toEqual([google.maps.TransitMode.TRAM]);
    expect(addressService.findTransit_mode("car")).toEqual([]);
    expect(addressService.findTransit_mode("bike")).toEqual([]);
  });

  /**
   * Check if the correct google transport modes are retreived when giving a travel mode as formatted by the server
   */
  it('Should find the correct travel mode', function() {
    expect(addressService.findTravel_mode("car")).toEqual(google.maps.TravelMode.DRIVING);
    expect(addressService.findTravel_mode("train")).toEqual(google.maps.TravelMode.TRANSIT);
    expect(addressService.findTravel_mode("streetcar")).toEqual(google.maps.TravelMode.TRANSIT);
    expect(addressService.findTravel_mode("bus")).toEqual(google.maps.TravelMode.TRANSIT);
    expect(addressService.findTravel_mode("bike")).toEqual(google.maps.TravelMode.BICYCLING);
  });

  /**
   * Test the address validation
   */
  it('Should correctly validate address', function() {

    /**
     * Create an address with too little components. This implies that there is no housenumber given
     */
    var address = {};
    address.address_components = [];
    for (i = 0; i < 3; i++) {
      address.address_components[i] = "test";
    }

    expect(addressService.validateAddress(address)).not.toBeTruthy();

    /**
     * We add an extra element to address_components so now we have the correct amount of components
     */
    address.address_components[4] = "test";
    expect(addressService.validateAddress(address)).toBeTruthy();
  });

  it('Should correctly set the address', function() {
    /**
     * Create a test address object in which the right components will be stored
     */
    var testAddress = {};

    /**
     * Create a fake formatted address as given by the google services
     */
    var formatted = {};
    formatted.address_components = [];
    formatted.address_components[0] = {};
    formatted.address_components[0].types = [];
    formatted.address_components[0].types[0] = "street_number";
    formatted.address_components[0].long_name = undefined;
    formatted.address_components[1] = {};
    formatted.address_components[1].types = [];
    formatted.address_components[1].types[0] = "route";
    formatted.address_components[1].long_name = "Honingenveldstraat";
    formatted.address_components[2] = {};
    formatted.address_components[2].types = [];
    formatted.address_components[2].types[0] = "locality";
    formatted.address_components[2].long_name = "Gooik";
    formatted.address_components[3] = {};
    formatted.address_components[3].types = [];
    formatted.address_components[3].types[0] = "country";
    formatted.address_components[3].short_name = "België";
    formatted.address_components[4] = {};
    formatted.address_components[4].types = [];
    formatted.address_components[4].types[0] = "postal_code";
    formatted.address_components[4].long_name = "1755";
    formatted.address_components[5] = {};
    formatted.address_components[5].types = [];
    formatted.address_components[5].types[0] = "test";
    formatted.address_components[5].long_name = "1755";
    formatted.address_components[6] = {};
    formatted.address_components[6].types = [];
    formatted.address_components[6].types[0] = "test";
    formatted.address_components[6].long_name = "1755";

    /**
     * Create stub lat and lng functions
     */
    formatted.geometry = {};
    formatted.geometry.location = {};

    formatted.geometry.location.lat = function() {
      return 51.0281817;
    };

    formatted.geometry.location.lng = function() {
      return 3.7186009;
    };

    /**
     * Call the setAddress function
     */
    addressService.setAddress(testAddress, formatted);

    /**
     * Check if the fields are correctly filled in
     */
    expect(testAddress.street).toEqual(formatted.address_components[1].long_name);
    expect(testAddress.housenumber).toEqual('');
    expect(testAddress.city).toEqual(formatted.address_components[2].long_name);
    expect(testAddress.country).toEqual(formatted.address_components[3].short_name);
    expect(testAddress.postal_code).toEqual(formatted.address_components[4].long_name);

    /**
     * Now, delete the postal code and check if the retreivePostalCode function is beeing called
     */
    testAddress.postal_code = undefined;
    formatted.address_components[4].types[0] = "test";
    addressService.setAddress(testAddress, formatted);

    expect(addressService.retreivePostalCode).toHaveBeenCalled();
  });

  /**
   * Call the retreivePostalCode method and see if there are no errors
   */
  it('Should call retreivePostalCode without there being any errors', function() {


    var stringified = JSON.stringify(travel.endpoint.coordinates);
    stringified = stringified.replace('"lon"', '"lng"');

    var coordinate = JSON.parse(stringified);

    addressService.retreivePostalCode(coordinate, geocoder);
  });

  /**
   * Check if the correct functions are called when initing a new map to show a new location
   */
  it('Should correctly call all the functions in initNewLocationMap', function() {
    scope.map = {};
    scope.locationInfo = {};
    scope.locationInfo.radius = 20;

    addressService.initNewLocationMap(scope, scope.map);

    expect(addressService.initNewMarker).toHaveBeenCalled();
    expect(addressService.initNewRadiusCircle).toHaveBeenCalled();
    expect(addressService.watchAddressMarker).toHaveBeenCalled();
    expect(addressService.watchCircleRadius).toHaveBeenCalled();
  });

  /**
   * Check if the correct functions are called when making a map for showing/editing a location
   */
  it('Should correctly init a map for editing and showing a location', function() {
    scope.map = {};
    scope.map.fitBounds = function() {
      return "";
    };
    scope.map.setCenter = function() {
      return;
    };
    scope.locationInfo = {};
    scope.locationInfo.location = {};
    scope.locationInfo.location.geometry = {};
    scope.locationInfo.location.geometry.location = {
      "lat": 51.0281817,
      "lng": 3.7186009
    };

    addressService.initEditShowMap(scope, scope.map, "Honingenveldstraat 2, 1755 Gooik", 1000, true);

    $timeout(function() {
      expect(addressService.watchAddressMarker).toHaveBeenCalled();
      expect(addressService.watchCircleRadius).toHaveBeenCalled();
    }, 150);
    $timeout.flush();
  });

  /**
   * Check if the location is correctly watched
   */
  it('Should correctly watch the address of the current location', function() {
    /**
     * Create the needed variables for the function not to crash
     */
    scope.locationInfo = {};
    scope.locationInfo.location = {};
    scope.locationInfo.location.geometry = {};
    scope.locationInfo.location.geometry.location = {
      "lat": 51.0281817,
      "lng": 3.7186009
    };
    scope.map = {};
    scope.map.setCenter = function() {
      return "";
    };


    /**
     * Call the tested function
     */
    addressService.watchAddressMarker(scope);

    /**
     * Change the locationInfo object and apply the scope
     */
    scope.locationInfo.test = {};
    scope.$apply();

    /**
     * Check if the watch function has catched the change and called the correct functions
     */
    expect(scope.marker.setPosition).toHaveBeenCalled();
    expect(scope.marker.setVisible).toHaveBeenCalled();
    expect(scope.circle.setCenter).toHaveBeenCalled();
    expect(scope.circle.setVisible).toHaveBeenCalled();
  });

  /**
   * Check if a new circle is correctly instatiated
   */
  it('Should correctly init a new circle', function() {
    scope.marker.position = {
      "lat": 51.234,
      "lng": 3.16754
    };

    scope.map = {};

    /**
     * Call the initRadiusCircle in a registration context
     */
    addressService.initNewRadiusCircle(scope, scope.map, scope.marker, 100, 'register');

    /**
     * We expect the radius to have been correctly set and the visibility to be set to false
     */
    expect(scope.circle.radius).toEqual(100);
    expect(scope.circle.getVisible()).not.toBeTruthy();

    /**
     * Call the initRadiusCircle in a registration context
     */
    addressService.initNewRadiusCircle(scope, scope.map, scope.marker, 20, 'show');

    /**
     * We expect the radius to have been correctly set and the visibility to be set to true
     */
    expect(scope.circle.radius).toEqual(20);
    expect(scope.circle.getVisible()).toBeTruthy();
  });

  /**
   * Check if the location radius is correctly watched
   */
  it('Should correctly watch the radius of the current location', function() {
    scope.locationInfo = {};
    scope.map = {};
    scope.map.fitBounds = function() {
      return;
    };
    spyOn(scope.map, 'fitBounds').and.callThrough();
    scope.locationInfo.radius = 50;

    addressService.watchCircleRadius(scope);

    scope.locationInfo.radius = 60;
    scope.$apply();

    expect(scope.map.fitBounds).toHaveBeenCalled();
    expect(scope.circle.getBounds).toHaveBeenCalled();
    expect(scope.circle.setRadius).toHaveBeenCalled();
    expect(scope.marker.getVisible).toHaveBeenCalled();
  });

  it('Should correctly watch address marker', function() {
    scope.locationInfo = {};
    scope.locationInfo.formatted_address = "Honingenveldstraat 2";
    scope.locationInfo.geometry = {};
    scope.locationInfo.geometry.location = "test";

    scope.locationInfo2 = {};
    scope.locationInfo2.formatted_address = "Galglaan 1";
    scope.locationInfo2.geometry = {};
    scope.locationInfo2.geometry.location = "test";

    scope.markerdrag = false;

    scope.directionsDisplay = {
      setMap: function() {
        return;
      },
      setDirections: function() {
        return;
      }
    };

    scope.directionsService = {
      route: function(request, result) {
        result("test");
      }
    };

    scope.map = {
      setCenter: function() {
        return;
      }
    };

    spyOn(scope.map, 'setCenter').and.callThrough();
    spyOn(scope.directionsDisplay, 'setMap').and.callThrough();
    spyOn(scope.directionsDisplay, 'setDirections').and.callThrough();
    spyOn(scope.directionsService, 'route').and.callThrough();

    scope.event = {
      type: {
        type: "ONGEVAL"
      }
    };

    addressService.watchEventAddressMarker(scope);

    /**
     * Change the formatted address to triger the watch function
     */
    scope.locationInfo.formatted_address = "Honingenveldstraat 3";
    scope.$apply();
    $timeout.flush();

    /**
     * Expect the correct functions to have been called
     */
    expect(scope.map.setCenter).toHaveBeenCalled();
    expect(scope.marker.setPosition).toHaveBeenCalled();
    expect(scope.marker.setVisible).toHaveBeenCalled();
    expect(scope.directionsDisplay.setMap).toHaveBeenCalled();

    /**
     * Make the event type a jam and check if the correct functions are called
     */
    scope.event.type.type = "JAM";
    scope.$apply();
    $timeout.flush();
  });

  it('Should correctly init a map', function() {
    addressService.initMap("test");
    expect(google.maps.Map).toHaveBeenCalled();
  });

  it('Should init a directionsRenderer from google when asked', function() {
    addressService.initDirectionsRenderer("test", false);
    expect(google.maps.DirectionsRenderer).toHaveBeenCalledWith({
      draggable: false,
      suppressBicyclingLayer: true,
      map: "test"
    });
  });
});
