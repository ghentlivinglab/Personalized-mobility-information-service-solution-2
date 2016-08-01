fdescribe('SharedService', function() {
	beforeEach(module('App'));
	beforeEach(module('AppMock'));

	var scope, sharedService, localStorageService, eventTypeService, userService, userEventService,
	travelService, addressService, eventService, locationService, locationEventService, routeService, routeEventService,
	errorService, accessTokenService, $filter, $q, httpBackend, basePath, headers;

	var user, travel, route, poi, events;

	 beforeEach(inject(function(
    	$rootScope,
	    $injector,
			_sharedService_,
			_localStorageService_,
			_eventTypeService_,
			_userService_,
			_userEventService_,
			_travelService_,
			_addressService_,
			_eventService_,
			_locationService_,
			_locationEventService_,
			_routeService_,
			_routeEventService_,
			_errorService_,
			_accessTokenService_,
			_$filter_,
			_$q_,
			_$httpBackend_) {

		/**
		 * Create the needed dependencies
		 */
		basePath = 'http://localhost:8080';
		scope = $rootScope.$new();
		sharedService = _sharedService_;
		localStorageService = _localStorageService_;
		eventTypeService = _eventTypeService_;
		userService = _userService_;
		userEventService = _userEventService_;
		travelService = _travelService_;
		addressService = _addressService_;
		eventService = _eventService_;
		locationService = _locationService_;
		locationEventService = _locationEventService_;
		routeService = _routeService_;
		routeEventService = _routeEventService_;
		errorService = _errorService_;
		accessTokenService = _accessTokenService_;
		$filter = _$filter_;
		$q = _$q_;
		httpBackend = _$httpBackend_;

		scope.directionsDisplay = {
			setDirections: function(data){
				return data;
			}
		};

		headers = {
      'Authorization': 'access',
      'Accept': 'application/json, text/plain, */*'
    };

		directionsService = new google.maps.DirectionsService();
		geocoder = new google.maps.Geocoder();
		/**
		 * Create default travel and route object
		 */
		travel = {
			"id":"1",
			"name":"test",
			"startpoint":{
					"id":"1",
					"street":"Honingenveldstraat",
					"city":"Gooik",
					"country":"BE",
					"coordinates":{
						"lon":0.0,"lat":0.0
					},
					"housenumber":"2",
					"postal_code":"1755"
			},
			"endpoint":{
					"id":"2","street":"Galglaan",
					"city":"Gent",
					"country":"BE",
					"coordinates":{
						"lon":3.7186009,"lat":51.0281817
					},
					"housenumber":"1",
					"postal_code":"9000"
			},
			"recurring":[false,false,false,false,false,false,false],
			"time_interval":["08:00","10:00"],
			"is_arrival_time":true
		};
		scope.currentUserTravels = [ travel ];

		route = {
      "id":"1",
      "waypoints":[{
          "lon": 0.0,
          "lat": 0.0
      }],
      "transportation_type":"train",
      "notify_for_event_types":[],
      "active": true
    };
		scope.currentTravelRoutes = [ route ];

		/**
     * Create two dummy point_of_interest objects that will be returned when the controller does a request to /user/1/point_of_interest
     */
    poi = {
      "id":"1",
      "address":{
        "id":"1",
        "street":"Honingenveldstraat",
        "city":"Gooik",
        "country":"BE",
        "coordinates":{
          "lon":0.0,
          "lat":0.0
        },
        "housenumber":"2",
        "postal_code":"1755"
      }
    };
		scope.currentUserLocations = [ poi ];

		user = {
      "id": "1",
      "validated": {
        "email": false
      }
    };

		transport_types = [
			{name: 'Bus', code: 'bus'},
			{name: 'Auto', code: 'car'},
			{name: 'Fiets', code: 'bike'},
			{name: 'Trein', code: 'train'},
			{name: 'Tram', code: 'streetcar'}
		];

		// An array containing an eventType to return when the server stub gets a request for possible eventTypes
		types = [{
		  "type":"ROAD_CLOSED",
		}];

		events = [{
			"id":"570d2d4e74918d2482dd5745",
			"coordinates":{
				"lat":40.714224,
				"lon":-73.961452
			},
			"active":true,
			"publication_time":"10:15:30",
			"last_edit_time":"10:15:30",
			"description":"testevent",
			"jams": [{
        "line": [{
          "lat": "0.0",
					"lon": "0.0"
        }, {
					"lat": "0.0",
					"lon": "0.0"
        }]
      }],
			"type":{
				"type":"JAM"
			},
			"relevant_for_transportation_types":["car","bike"]
		}];

		/**
    * Create a dummy marker and polyline object used in tests and spy on the functions
    */
    scope.marker = {};
    scope.marker.setMap = function() {
      return ;
    };
		scope.markers = [ scope.marker ];
		scope.polyline = {};
		scope.polyline.setMap = function() {
			return ;
		};
		scope.polylines = [ scope.polyline ];

		/**
		 * Set the spies
		 */
		spyOn(scope.marker, 'setMap').and.callThrough();
		spyOn(scope.polyline, 'setMap').and.callThrough();
		spyOn(scope.directionsDisplay, 'setDirections').and.callThrough();
		spyOn(sharedService, 'parseTime').and.callThrough();
		spyOn(sharedService, 'fillInWaypoints').and.callThrough();
		spyOn(sharedService, 'fillInLegs').and.callThrough();
		spyOn(sharedService, 'renewToken').and.callThrough();
		spyOn(errorService, 'handleUserError').and.callThrough();
		spyOn(addressService, 'validateAddress').and.callThrough();
		spyOn(addressService, 'setAddress').and.callThrough();
		spyOn(addressService, 'initMap').and.callFake(function(){return true;});
		spyOn(errorService, 'handleRouteError').and.callThrough();
		spyOn(errorService, 'handlePointOfInterestError').and.callThrough();

		/**
     * Spy on the following functions.
     */
    scope.store = {
      "registrationUser": "user",
      "refreshToken": {
        "token": "refresh",
        "user_id": '1'
      },
      "accessToken": {
        "token": "access"
      }
    };
    spyOn(localStorageService, 'get').and.callFake(function (key) {
    return scope.store[key];
    });
    spyOn(localStorageService, 'set').and.callFake(function (key, value) {
    scope.store[key] = value;
    });
    spyOn(localStorageService, 'clearAll').and.callFake(function () {
      scope.store = {};
    });
		/**
		 * Create a fake Google Directions Service
		 */
		spyOn(directionsService, 'route').and.callFake(function(){
			var fakeresult = arguments[0];
			var fakeStatus;

			/**
			 * Fake error code to see if the directionsRenderes isn't called when the address is invalid
			 */
			if(fakeresult.origin === 'false 10 1755 Gooik'){
				fakeStatus = google.maps.DirectionsStatus.ERROR;
			}else{
				fakeStatus = google.maps.DirectionsStatus.OK;
			}
			var callback = arguments[1];
			return callback(fakeresult, fakeStatus);
		});

		spyOn(geocoder, 'geocode').and.callFake(function(){
			var fakeresult = [{address_components: [ {types: [ "postal_code" ], long_name: "9000"}]}];
			var fakeStatus = google.maps.GeocoderStatus.OK;
			var callback = arguments[1];
			return callback(fakeresult, fakeStatus);
		});

		localStorageService.set("refreshToken", scope.store.refreshToken);
		localStorageService.set("accessToken", scope.store.accessToken);

		httpBackend.whenGET(basePath + '/user').respond(200, [ user ]);
		httpBackend.whenGET(basePath + '/user/1').respond(200, user);
    httpBackend.whenGET(basePath + '/user/1/travel').respond(200, [ travel ]);
    httpBackend.whenGET(basePath + '/user/1/travel/1').respond(200, travel);
		httpBackend.whenGET(basePath + '/user/1/travel/1/route').respond(200, [ route ]);
		httpBackend.whenGET(basePath + '/user/1/point_of_interest').respond(200, [ poi ]);
		httpBackend.whenPOST(basePath + '/access_token').respond(200, {"accessToken": "access"});

	 }));

	/**
	 * Check after each test if there are expected requests to the server that have not been done
	 */
	afterEach(function() {
		httpBackend.verifyNoOutstandingExpectation();
		httpBackend.verifyNoOutstandingRequest();
	});

	/**
	 * Check if all the variables are defined
	 */
	it('Parameters need to be defined',function(){
		expect(scope).toBeDefined();
		expect(sharedService).toBeDefined();
		expect(localStorageService).toBeDefined();
		expect(eventTypeService).toBeDefined();
		expect(userService).toBeDefined();
		expect(userEventService).toBeDefined();
		expect(travelService).toBeDefined();
		expect(addressService).toBeDefined();
		expect(eventService).toBeDefined();
		expect(locationService).toBeDefined();
		expect(locationEventService).toBeDefined();
		expect(routeService).toBeDefined();
		expect(routeEventService).toBeDefined();
		expect(errorService).toBeDefined();
		expect(accessTokenService).toBeDefined();
		expect($filter).toBeDefined();
		expect($q).toBeDefined();
	});

	/**
	 * Check if all the eventTypes are correctly returned
	 */
	it('Should correctly return the eventTypes', function(){

		// Create a checkArray containing all the eventTypes that need to be returned by sharedService
		var check = {
	        "ACCIDENT": "Ongeval",
	        "JAM": "File",
	        "WEATHERHAZARD": "Gevaar",
	        "HAZARD": "Gevaar op de weg",
	        "MISC": "Ander Type",
	        "CONSTRUCTION": "Wegenwerken",
	        "ROAD_CLOSED": "Weg afgesloten"
	      };

	      expect(sharedService.getTypes()).toEqual(check);
	});

	/**
	 * Check if the correct transportTypes are returned
	 */
	it('Should return the correct transportTypes', function(){

		expect(sharedService.getTransportTypes()).toEqual(transport_types);

	});

	/**
	 * Check if the needed parameters are initiated when calling the function
	 */
	it('Should correctly init the travel registration', function(){
		sharedService.initTravelReg(scope);

		expect(scope.travel.startpoint).toEqual({});
		expect(scope.travel.endpoint).toEqual({});
		expect(scope.travel.is_arrival_time).toBeTruthy();
		expect(scope.travel.recurring.length).toEqual(7);
		expect(scope.transportTypes).toEqual(transport_types);
		expect(scope.route.waypoints).toEqual( [] );
		expect(scope.route.transportation_type).toEqual( "" );
		expect(scope.route.notify_for_event_types).toEqual( [] );
	});

	/**
	 * Check if the eventType relevance is correctly set and the errors are correctly processed
	 */
	it('Should correctly set the event Type relevance', function(){

		/**
		 * Create the backend stub and respond with a dummy eventType that is only relevant to car and bike
		 */
		httpBackend.expect('GET', basePath + '/eventtype?' + 'transportation_type=car').respond(200, types);

		/**
		 * Call the function in sharedService and flush the request
		 */
		var route = {};
		route.transportation_type = "car";

		sharedService.setEventTypeRelevance(route);
		httpBackend.flush();

		/**
		 * Because we set the transportation type to car, we expect that the eventType is added to the relevant eventTypes
		 */
		expect(route.notify_for_event_types).toBeDefined();
		expect(route.notify_for_event_types[0]).toEqual(types[0].type);


		/**
		 * Change the transportation type of the route to bike
		 */
		route.transportation_type = "bike";

		/**
		 * Expect a request
		 */
		httpBackend.expectGET(basePath + '/eventtype?' + 'transportation_type=bike').respond(200, types);
		/**
		 * Call the function in sharedService and flush the request
		 */
		sharedService.setEventTypeRelevance(route);
		httpBackend.flush();

		/**
		 * Because we set the transportation type to bike, we expect that the eventType is added to the relevant eventTypes
		 */
		expect(route.notify_for_event_types).toBeDefined();
		expect(route.notify_for_event_types[0]).toEqual(types[0].type);


		/**
		 * Change the transportation type of the route to bus
		 */
		route.transportation_type = "bus";

		/**
		 * Expect a request
		 */
		httpBackend.expectGET(basePath + '/eventtype?' + 'transportation_type=bus').respond(200, types);
		/**
		 * Call the function in sharedService and flush the request
		 */
		sharedService.setEventTypeRelevance(route);
		httpBackend.flush();

		/**
		 * Because we set the transportation type to bus, we expect that the eventType is added to the relevant eventTypes
		 */
		expect(route.notify_for_event_types).toBeDefined();
 		expect(route.notify_for_event_types[0]).toEqual(types[0].type);


		/**
		 * Change the transportation type of the route to bike
		 */
		route.transportation_type = "train";

		/**
		 * Expect a request
		 */
		httpBackend.expectGET(basePath + '/eventtype?' + 'transportation_type=train').respond(200, types);
		/**
		 * Call the function in sharedService and flush the request
		 */
		sharedService.setEventTypeRelevance(route);
		httpBackend.flush();

		/**
		 * Because we set the transportation type to train, we expect that the eventType is added to the relevant eventTypes
		 */
		expect(route.notify_for_event_types).toBeDefined();
  	expect(route.notify_for_event_types[0]).toEqual(types[0].type);


		/**
		 * Change the transportation type of the route to bike
		 */
		route.transportation_type = "streetcar";

		/**
		 * Expect a request
		 */
		httpBackend.expectGET(basePath + '/eventtype?' + 'transportation_type=streetcar').respond(200, types);
		/**
		 * Call the function in sharedService and flush the request
		 */
		sharedService.setEventTypeRelevance(route);
		httpBackend.flush();

		/**
		 * Because we set the transportation type to streetcar, we expect that the eventType is added to the relevant eventTypes
		 */
		expect(route.notify_for_event_types).toBeDefined();
   	expect(route.notify_for_event_types[0]).toEqual(types[0].type);

		/**
		* Now we will return an error from the server and check if the error is correctly printed to the console
		*/
		spyOn(console, 'error');
		httpBackend.expectGET( basePath + '/eventtype?' + 'transportation_type=streetcar').respond(-1, "test");
		sharedService.setEventTypeRelevance(route);
		httpBackend.flush();
		expect(console.error).toHaveBeenCalledWith("Could not connect to server: Connection refused");

		/**
		* Now we will return an error from the server and check if the error is correctly printed to the console
		*/
		httpBackend.expectGET( basePath + '/eventtype?' + 'transportation_type=streetcar').respond(404, "test");
		sharedService.setEventTypeRelevance(route);
		httpBackend.flush();
		expect(console.error).toHaveBeenCalled();

	});

	/**
	 * check if the default parameters of a user are correctly set
	 */
	it('Should correctly set the default user options', function(){
		user = {};
		sharedService.setDefaultUserOptions(user);
		expect(user.mute_notifications).not.toBeTruthy();
	});

	/**
	 * Check if the start and endTime are correctly set
	 */
	it('Should correctly set the start and end time', function() {
		var startTimeObject = new Date(1970, 0, 1, 22, 0);
		var endTimeObject = new Date(1970, 0, 1, 23, 59);
		var travel = {};

		sharedService.setStartEndTime(travel, startTimeObject, endTimeObject);
		expect(travel.time_interval[0]).toEqual($filter('date')(startTimeObject, "HH:mm"));
		expect(travel.time_interval[1]).toEqual($filter('date')(endTimeObject, "HH:mm"));
		expect(sharedService.parseTime).toHaveBeenCalled();
	});

	/**
	 * Check if the parseTime function correctly parses the date objects
	 */
	it('Should correctly parse the time', function(){
		var date = new Date(1970, 0, 1, 22, 0);

		expect(sharedService.parseTime(date, "HH:mm")).toEqual("22:00");
		expect(sharedService.parseTime(date, "HH:mm:ss")).toEqual("22:00:00");
		expect(sharedService.parseTime(date, "HH")).toEqual("22");
		expect(sharedService.parseTime(date, "HH mm.ss")).toEqual("22 00.00");
	});

	/**
	 * Check if the waypoints are correctly parsed and added to the route
	 */
	it('Should correctly find waypoints and pass them to fillInWaypoints() and fillInLegs()', function() {

		/**
		 * Create a dummy directions renderer with 3 known waypoints
		 */
		var directionsRenderer = {};
	  directionsRenderer.directions = {};
		directionsRenderer.directions.routes = [];
		directionsRenderer.directions.routes[0] = {};
		directionsRenderer.directions.routes[0].legs = [];
		directionsRenderer.directions.routes[0].legs[0] = {};
		directionsRenderer.directions.routes[0].legs[0].via_waypoints = [];
		directionsRenderer.directions.routes[0].legs[0].via_waypoints[0] = {
			lat: function() {return 0.0;},
			lng: function() {return 0.0;}
		};
		directionsRenderer.directions.routes[0].overview_path = [];
		directionsRenderer.directions.routes[0].overview_path[0] = {
			lat: function() {return 0.0;},
			lng: function() {return 0.0;}
		};

		sharedService.findWaypoints(travel, route, directionsRenderer);
		expect(sharedService.fillInWaypoints).toHaveBeenCalled();
		result_waypoints = [{
				"lon": 0.0,
				"lat": 0.0
		}];
		expect(route.waypoints).toEqual(result_waypoints);
		expect(sharedService.fillInLegs).toHaveBeenCalled();
		result_waypoints = [{
				"lon": 0.0,
				"lat": 0.0
		}, {
				"lon": 0.0,
				"lat": 0.0
		}];
		expect(route.full_waypoints).toEqual(result_waypoints);
	});

	/**
	 * check if waypoints are created in the correct format for the google map to display them
	 */
	it('Should correctly convert waypoints', function(){

		/**
		 * Create an array with coordinates of waypoints
		 */
		var waypoints = [];
		waypoints[0] = {
			lon: "4.132607",
			lat: "50.792004"
		};
		waypoints[1] = {
			lon: "3.131507",
			lat: "51.782204"
		};

		/**
		 * Convert the array
		 */
		var result = sharedService.convertWaypoints(waypoints);

		expect(result[0]).toEqual({
			location: {
				lng: "4.132607",
				lat: "50.792004"
			},
			stopover: false
		});

		expect(result[1]).toEqual({
			location: {
				lng: "3.131507",
				lat: "51.782204"
			},
			stopover: false
		});
	});

	/**
	 * Check if the function correctly calls the travel post function when the user is successfully posted
	 */
	it('Should correctly post a user',  function(){

		user.id = undefined;
		travel.id = undefined;
		route.id = undefined;
		var travels = [];
		travels[0] = travel;
		var routes = [];
		routes[0] = route;
		var locations = [];
		locations[0] = {
			"address":{
				"street":"Jozef Plateaustraat",
				"housenumber":"22",
				"city":"Gent",
				"country":"BE",
				"postal_code":"9000",
				"coordinates":{
					"lat":51.046143,
					"lon":3.724859
				}},
				"name":"home",
				"radius":5000,
				"active":true,
				"notify_for_event_types":[],
				"notify":{
					"email":false,
					"cell_number":false
				}
		};


		/**
		 * Create the backend stub and respond with a dummy eventType that is only relevant to car and bike
		 */
		httpBackend.expectPOST(basePath + '/user').respond(200, {id: 1});
		sharedService.postUser(scope, user);
		httpBackend.flush();
		expect(scope.currentUser).toBeDefined();
		expect(scope.result).toBeTruthy();

		/**
		 * Should give a handleUserError
		 */
		httpBackend.expectPOST(basePath + '/user').respond(-1);
		sharedService.postUser(scope, user);
		httpBackend.flush();
		expect(errorService.handleUserError).toHaveBeenCalled();
		expect(scope.result).toBeFalsy();

	});

	/**
	 * Check if a new location is correctly made
	 */
	it('Should correctly create a new location', function(){
		/**
		 * Delete the points_of_interests array of the user to be able to test if a new array is made
		 */
		user.points_of_interest = undefined;

		/**
		 * Create a fake formatted address as given by the google services
		 */
		var formatted = {};
		formatted.address_components = [];
		formatted.address_components[0] = {};
		formatted.address_components[0].types = [];
		formatted.address_components[0].types[0] = "street_number";
		formatted.address_components[0].long_name = "12";
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

		formatted.geometry = {};
		formatted.geometry.location = {};
		formatted.geometry.location.lat = function(){
			return 51.0281817;
		};

		formatted.geometry.location.lng = function(){
			return 3.7186009;
		};
		/**
		 * Create dummy Poi object containing just a name
		 */
		var POI = {
			naam: "test"
		};


		POI = sharedService.createPOI(POI, formatted);

		expect(addressService.setAddress).toHaveBeenCalled();
		expect(POI.naam).toEqual("test");
		expect(POI.address.street).toEqual(formatted.address_components[1].long_name);
		expect(POI.address.city).toEqual(formatted.address_components[2].long_name);
		expect(POI.address.housenumber).toEqual(formatted.address_components[0].long_name);
		expect(POI.address.country).toEqual(formatted.address_components[3].short_name);

	});

	/**
	 * Test if the function loadAllEventsAndShow() works correctly
	 * Tests also the help functions processEvents(), processJams() and createPolyline()
	 */
	it('Should check if the function loadAllEventsAndShow() and all the help functions work correctly', function() {
		/**
		 * Test if we go into the if condition of the help function processEvents() where at least one event contains a non-empty jam array
		 * Tests also the help functions processJams and createPolyline
		 */
		httpBackend.expect('GET', basePath + '/event?' + 'recent=true').respond(200, events);
		sharedService.loadAllEventsAndShow(scope);
		httpBackend.flush();

		expect(scope.map).toBeDefined();
		expect(addressService.initMap).toHaveBeenCalled();
		expect(scope.marker.setMap).toHaveBeenCalled();
		expect(scope.polyline.setMap).toHaveBeenCalled();
		expect(scope.addresses).toBeDefined();
		expect(scope.markers).toBeDefined();
		expect(scope.polylines).toBeDefined();

		/**
		 * Test if we go into the else condition of the help function processEvents() where at least no events contain a non-empty jam array
		 */
		events[0].jams = [];
		httpBackend.expect('GET', basePath + '/event?' + 'recent=true').respond(200, events);
		sharedService.loadAllEventsAndShow(scope);
		httpBackend.flush();

		expect(scope.map).toBeDefined();
		expect(addressService.initMap).toHaveBeenCalled();
		expect(scope.markers).toBeDefined();
	});

	/**
	 * Test if the function loadUserEventsAndShow() works correctly
	 */
	it('Should test if the function loadUserEventsAndShow() works correctly', function() {
		httpBackend.expect('GET', basePath + '/user/1/events').respond(200, events);
		sharedService.loadUserEventsAndShow(scope, "1");
		httpBackend.flush();

		expect(scope.events).toBeDefined();
		expect(scope.map).toBeDefined();
		expect(addressService.initMap).toHaveBeenCalled();

		/**
		 * Should give an unauthorized error and re-execute the function
		 */
		httpBackend.expect('GET', basePath + '/user/1/events').respond(401);
		httpBackend.expect('GET', basePath + '/user/1/events').respond(200, events);
		sharedService.loadUserEventsAndShow(scope, "1");
		httpBackend.flush();

		expect(sharedService.renewToken).toHaveBeenCalled();
		expect(scope.events).toBeDefined();
		expect(scope.map).toBeDefined();
		expect(addressService.initMap).toHaveBeenCalled();

		/**
		 * Should go into the if condition where the user is an admin or operator and there is a request to get all the recent events
		 */
		localStorageService.set("refreshToken", {"role": "operator"});
		httpBackend.expect('GET', basePath + '/event?' + 'recent=true').respond(200, events);
		sharedService.loadUserEventsAndShow(scope, "1");
		httpBackend.flush();

		expect(scope.events).toBeDefined();
		expect(scope.map).toBeDefined();
		expect(addressService.initMap).toHaveBeenCalled();
	});

	/**
	 * Test if the function loadLocationEventsAndShow() works correctly
	 */
	it('Should test if the function loadLocationEventsAndShow() works correctly', function() {
		httpBackend.expect('GET', basePath + '/user/1/point_of_interest/1/events').respond(200, events);
		sharedService.loadLocationEventsAndShow(scope, "1", "1");
		httpBackend.flush();

		// the help function processEvents already tested in loadAllEventsAndShow() test

		/**
		 * Should give an unauthorized error and re-execute the function
		 */
		httpBackend.expect('GET', basePath + '/user/1/point_of_interest/1/events').respond(401);
		httpBackend.expect('GET', basePath + '/user/1/point_of_interest/1/events').respond(200, events);
		sharedService.loadLocationEventsAndShow(scope, "1", "1");
		httpBackend.flush();

		expect(sharedService.renewToken).toHaveBeenCalled();
	});

	/**
	 * Test if the function loadRouteEventsAndShow() works correctly
	 */
	it('Should test if the function loadRouteEventsAndShow() works correctly', function() {
		httpBackend.expect('GET', basePath + '/user/1/travel/1/route/1/events').respond(200, events);
		sharedService.loadRouteEventsAndShow(scope, "1", "1", "1");
		httpBackend.flush();

		// the help function processEvents already tested in loadAllEventsAndShow() test

		/**
		 * Should give an unauthorized error and re-execute the function
		 */
		httpBackend.expect('GET', basePath + '/user/1/travel/1/route/1/events').respond(401);
		httpBackend.expect('GET', basePath + '/user/1/travel/1/route/1/events').respond(200, events);
		sharedService.loadRouteEventsAndShow(scope, "1", "1", "1");
		httpBackend.flush();

		expect(sharedService.renewToken).toHaveBeenCalled();
	});

	/**
   * Test if the function loadData() works correctly
   */
  it('should check the function loadData()', function() {
    httpBackend.expect('GET', basePath + '/user/1').respond(200, user);
    httpBackend.expect('GET', basePath + '/user/1/travel').respond(200, [travel]);
		httpBackend.expect('GET', basePath + '/user/1/point_of_interest').respond(200, [poi]);
		httpBackend.expect('GET', basePath + '/user/1/travel/1/route').respond(200, [route]);

    sharedService.loadData(scope);
    httpBackend.flush();

    /**
     * Check if functions have been called and id's have been defined
     */
    expect(scope.currentUser).toBeDefined();
    expect(scope.currentUserTravels).toBeDefined();
    expect(scope.travelId).toBe(0);
    expect(scope.currentTravelRoutes).toBeDefined();
    expect(scope.waypoints).toBeDefined();
    expect(scope.routeId).toBe(0);

    /**
     * We expect to have 1 waypoint in the array (correctly converted to the google maps API)
     */
    var result_waypoints = [];
    var waypoint = JSON.parse('{ "lng": 0.0, "lat": 0.0 }');
    result_waypoints.push({
      location: waypoint,
      stopover: false
    });
    expect(scope.waypoints).toEqual(result_waypoints);

    /**
		 * Should give an unauthorized error for get user/1 and re-execute the function
		 */
		httpBackend.expect('GET', basePath + '/user/1').respond(401);

		sharedService.loadData(scope);
    httpBackend.flush();
		expect(sharedService.renewToken).toHaveBeenCalled();

		/**
		 * Should give an unauthorized error for get user/1/travel and re-execute the function
		 */
		httpBackend.expect('GET', basePath + '/user/1').respond(200, user);
		httpBackend.expect('GET', basePath + '/user/1/travel').respond(401);

		sharedService.loadData(scope);
    httpBackend.flush();
		expect(sharedService.renewToken).toHaveBeenCalled();

		/**
		 * Should give an unauthorized error for get user/1/travel/1/route and re-execute the function
		 */
		httpBackend.expect('GET', basePath + '/user/1').respond(200, user);
		httpBackend.expect('GET', basePath + '/user/1/travel').respond(200, [travel]);
    httpBackend.expect('GET', basePath + '/user/1/travel/1/route').respond(401);

		sharedService.loadData(scope);
    httpBackend.flush();
		expect(sharedService.renewToken).toHaveBeenCalled();

		/**
		 * Should give an unauthorized error for get user/1/point_of_interest and re-execute the function
		 */
		httpBackend.expect('GET', basePath + '/user/1').respond(200, user);
		httpBackend.expect('GET', basePath + '/user/1/travel').respond(200, [travel]);
		httpBackend.expect('GET', basePath + '/user/1/point_of_interest').respond(401);

		sharedService.loadData(scope);
    httpBackend.flush();
		expect(sharedService.renewToken).toHaveBeenCalled();

  });

	/**
	 * Test if the access token is renewed when it is expired
	 */
	it('Should renew the access token if it is expired', function() {
		var expiration = new Date($filter('date')(new Date(), "yyyy-MM-dd'T'HH:mm"));
		/**
		 * Make sure the token is expired by substracting the expiration time by 10 hours (1000*60*60*10 milliseconds)
		 */
		expiration.setTime(expiration.getTime() - (1000*60*60*10));
		/**
     * Spy on the following functions.
     */
    scope.store = {
      "registrationUser": "user",
      "refreshToken": {
        "token": "refresh",
        "user_id": '1'
      },
      "accessToken": {
        "token": "access",
				"exp": expiration
      }
    };

		httpBackend.expect('POST', basePath + '/access_token').respond(200);
		sharedService.renewToken();
		httpBackend.flush();

		localStorageService.set("accessToken", {"token": "access"});
	});


});
