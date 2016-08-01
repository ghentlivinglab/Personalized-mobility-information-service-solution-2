fdescribe('NewTravelController', function() {
  beforeEach(module('App'));

  var controller, modal, scope, sharedService, travelService, routeService, errorService, user, modalInstance, basePath;

  beforeEach(inject(function(
    $rootScope,
    $controller,
    $routeParams,
    $injector,
    _$q_,
    _$httpBackend_,
    _$filter_,
    _sharedService_,
    _travelService_,
    _routeService_,
    _errorService_,
    _addressService_,
    _localStorageService_) {

    /**
     * Create the needed dependencies
     */
    q = _$q_;
    basePath = "http://localhost:8080";
    $filter = _$filter_;
    scope = $rootScope.$new();
    sharedService = _sharedService_;
    travelService = _travelService_;
    routeService = _routeService_;
    errorService = _errorService_;
    addressService = _addressService_;
    localStorageService = _localStorageService_;
    uibModalInstance = {
      rendered: {
        then: function(callback, errorCallback) {
          callback();
        }
      },
      map: {
        test: "test"
      },
      dismiss: function(test) {
        return;
      },
      close: function() {
        return;
      }
    };

    // An array containing an eventType to return when the server stub gets a request for possible eventTypes
    types = [{
      "type": "ROAD_CLOSED"
    }];

    /**
     * Stub for the server.
     * This wil enable us to test the handling of different status codes and response objects
     */
    $httpBackend = _$httpBackend_;

    /**
     * create the user id to be able to test the travel post method
     */
    user = {
      id: "1"
    };
    travel = {
      id: "1"
    };

    /**
     * initialize travelInfo for testing setStartAddress and setEndAddress
     */
    scope.travelInfo = {};

    /**
     * Create the necassery spies
     */
    spyOn(sharedService, 'initTravelReg').and.callThrough();
    spyOn(sharedService, 'setEventTypeRelevance').and.callThrough();
    spyOn(addressService, 'validateAddress').and.callThrough();
    spyOn(sharedService, 'findWaypoints').and.callFake(function() {
      var deferred = q.defer();
      deferred.resolve("test");
      return deferred.promise;
    });
    spyOn(sharedService, 'setStartEndTime').and.callFake(function() {});
    spyOn(sharedService, 'renewToken').and.callThrough();
    spyOn(addressService, 'setAddress').and.callFake(function() {});
    spyOn(errorService, 'handleTravelError').and.callThrough();
    spyOn(errorService, 'handleRouteError').and.callThrough();
    spyOn(uibModalInstance, 'close').and.callThrough();
    spyOn(uibModalInstance, 'dismiss').and.callThrough();
    spyOn(localStorageService, 'get').and.callFake(function(){
      return "test";
    });


    $httpBackend.whenGET(basePath + '/eventtype?transportation_type=').respond(200);
    /**
     * Create the controller we need to test
     */
    controller = $controller('NewTravelController', {
      $scope: scope,
      $uibModalInstance: uibModalInstance,
      travelService: travelService,
      routeService: routeService,
      user: user,
      sharedService: sharedService,
      addressService: addressService,
      errorService: errorService,
      localStorageService: localStorageService
    });

    spyOn(scope, 'register').and.callThrough();
    /**
     * We forsee the possibility that in each test, there can be a request to the server for the eventTypes
     */
    $httpBackend.whenGET(basePath + '/eventtype?tranportation_type=').respond(200, types);
    $httpBackend.whenGET(basePath + '/event').respond(200, types);
    scope.$apply();
  }));

  /**
   * Check if all the needed parameters for the test are defined
   */
  it('should have defined all parameters', function() {
    expect(scope).toBeDefined();
    expect(controller).toBeDefined();
    expect(sharedService).toBeDefined();
    expect(uibModalInstance).toBeDefined();
    expect(addressService).toBeDefined();
    expect(user).toBeDefined();
    expect(travelService).toBeDefined();
    expect(routeService).toBeDefined();
    expect(localStorageService).toBeDefined();
  });

  /**
   * Check if the user and travel id are really set to be 1. Otherwise some tests will fail
   */
  it('should set userId and travelId', function() {
    expect(user.id).toBe("1");
    expect(travel.id).toBe("1");
  });

  /**
   * The init functions should be called when the controller is instantiated
   */
  it('should call initTravelReg and setDefaultTravelOptions', function() {
    expect(sharedService.initTravelReg).toHaveBeenCalled();
  });

  /**
   * The init functions should have set the default parameters on init
   */
  it('should have set the default parameters on init', function() {
    /**
     * Set the default values that need to be filled in in the travel object when starting the controller.
     * This needs to be done to avoid getters on objects that not exist
     */
    var travel = {};
    travel.startpoint = {};
    travel.endpoint = {};
    travel.recurring = new Array(7);
    travel.time_interval = ['00:00', '00:00'];
    travel.is_arrival_time = true;
    for (i = 0; i < 7; i++) {
      travel.recurring[i] = false;
    }

    /**
     * Set the default values that need to be filled in in the route object when starting the controller.
     * This needs to be done to avoid getters on objects that not exist
     */
    var route = {};
    route.active = true;
    route.waypoints = [];
    route.transportation_type = "";
    route.notify_for_event_types = [];

    /**
     * The possible transportation types
     */
    var transportTypes = [{
      name: 'Bus',
      code: 'bus'
    }, {
      name: 'Auto',
      code: 'car'
    }, {
      name: 'Fiets',
      code: 'bike'
    }, {
      name: 'Trein',
      code: 'train'
    }, {
      name: 'Tram',
      code: 'streetcar'
    }];

    /**
     * check if the travel object is correctly set
     */
    expect(scope.travel).toEqual(travel);

    /**
     * check if the route object is correctly set
     */
    expect(scope.route).toEqual(route);

    /**
     * Check if the correct transportation types are added
     */
    expect(scope.transportTypes).toEqual(transportTypes);

  });

  it('should call the correct functions on registration and post travel and route', function() {
    /**
     * We expect that the travel will be posted to the server with the following URL
     * and the correct variables are filled in.
     */
    $httpBackend.expect('POST', basePath + '/user/1/travel').respond(200, travel);
    /**
     * Since we don't do anything with the response of the created route given by the server, we can just return an empty object.
     */
    $httpBackend.expect('POST', basePath + '/user/1/travel/1/route').respond(200, {});

    scope.register();

    /**
     * Flush and process all the requests given to the backend
     */
    $httpBackend.flush();

    /**
     * If the response code of the server is 200, we successfully posted a travel and a route
     * and we need to check if the modal is closed.
     */
    expect(uibModalInstance.close).toHaveBeenCalled();

    /**
     * Check if the correct function in sharedService are called
     */
    expect(sharedService.setEventTypeRelevance).toHaveBeenCalled();
    expect(sharedService.findWaypoints).toHaveBeenCalled();
  });


  /**
   * Test all errors for posting a travel
   */
  it('Should handle connection refused error code correctly for posting a travel', function() {
    /**
     * Return a status code of -1
     */
    $httpBackend.expect('POST', basePath + '/user/1/travel').respond(-1, " Connection refused");

    /**
     * Call the register function
     */
    scope.register();
    $httpBackend.flush();

    expect(errorService.handleTravelError).toHaveBeenCalled();
    expect(scope.connectionRefused).toBe(true);
  });

  it('Should handle a random error code correctly for posting a travel', function() {
    /**
     * Return a random status code
     */
    $httpBackend.expect('POST', basePath + '/user/1/travel').respond(125, {
      statusText: "Something went wrong"
    });

    /**
     * Call the register function
     */
    scope.register();
    $httpBackend.flush();
    expect(errorService.handleTravelError).toHaveBeenCalled();
    expect(scope.travelServerError).toBe(true);
  });

  it('Should handle an internal server error code correctly for posting a travel', function() {
    /**
     * Return a random status code
     */
    $httpBackend.expect('POST', basePath + '/user/1/travel').respond(500, {
      statusText: "Internal server error"
    });

    /**
     * Call the register function with a dummy object
     */
    scope.register();
    $httpBackend.flush();
    expect(errorService.handleTravelError).toHaveBeenCalled();
    expect(scope.travelServerError).toBe(true);
  });

  it('Should handle a travel already exists error code correctly for posting a travel', function() {
    /**
     * Return a random status code
     */
    $httpBackend.expect('POST', basePath + '/user/1/travel').respond(404, {
      statusText: "Internal server error"
    });

    /**
     * Call the register function with a dummy object
     */
    scope.register();
    $httpBackend.flush();
    expect(errorService.handleTravelError).toHaveBeenCalled();
    expect(scope.travelAlreadyExists).toBe(true);
  });

  it('Should handle aunauthorized error code correctly for posting a travel', function() {
    /**
     * Return a random status code
     */
    $httpBackend.expect('POST', basePath + '/user/1/travel').respond(401, {
      statusText: "Internal server error"
    });

    /**
     * Call the register function with a dummy object
     */
    scope.register();
    scope.register.and.callFake(function(){return;});
    $httpBackend.flush();
    expect(sharedService.renewToken).toHaveBeenCalled();
  });


  /**
   * Test all errors for posting a route after a travel was successfully posted
   */
  it('Should handle connection refused error code correctly for posting a route', function() {
    /**
     * Return a status code of -1
     */
    $httpBackend.expect('POST', basePath + '/user/1/travel').respond(200, travel);
    $httpBackend.expect('POST', basePath + '/user/1/travel/1/route').respond(-1, " Connection refused");

    /**
     * Call the register function
     */
    scope.register();
    $httpBackend.flush();

    expect(errorService.handleRouteError).toHaveBeenCalled();
    expect(scope.connectionRefused).toBe(true);
  });

  it('Should handle a random error code correctly for posting a route', function() {
    /**
     * Return a random status code
     */
    $httpBackend.expect('POST', basePath + '/user/1/travel').respond(200, travel);
    $httpBackend.expect('POST', basePath + '/user/1/travel/1/route').respond(125, {
      statusText: "Something went wrong"
    });

    /**
     * Call the register function
     */
    scope.register();
    $httpBackend.flush();
    expect(errorService.handleRouteError).toHaveBeenCalled();
    expect(scope.routeServerError).toBe(true);
  });

  it('Should handle an internal server error code correctly for posting a route', function() {
    /**
     * Return a random status code
     */
    $httpBackend.expect('POST', basePath + '/user/1/travel').respond(200, travel);
    $httpBackend.expect('POST', basePath + '/user/1/travel/1/route').respond(500, {
      statusText: "Internal server error"
    });

    /**
     * Call the register function with a dummy object
     */
    scope.register();
    $httpBackend.flush();
    expect(errorService.handleRouteError).toHaveBeenCalled();
    expect(scope.routeServerError).toBe(true);
  });

  it('Should handle a travel already exists error code correctly for posting a route', function() {
    /**
     * Return a random status code
     */
    $httpBackend.expect('POST', basePath + '/user/1/travel').respond(200, travel);
    $httpBackend.expect('POST', basePath + '/user/1/travel/1/route').respond(409, {
      statusText: "Internal server error"
    });

    /**
     * Call the register function with a dummy object
     */
    scope.register();
    $httpBackend.flush();
    expect(errorService.handleRouteError).toHaveBeenCalled();
    expect(scope.routeAlreadyExists).toBe(true);
  });

  it('Should handle an unauthorized error code correctly for posting a route', function() {
    /**
     * Return a random status code
     */
    $httpBackend.expect('POST', basePath + '/user/1/travel').respond(200, travel);
    $httpBackend.expect('POST', basePath + '/user/1/travel/1/route').respond(401, {
      statusText: "Internal server error"
    });

    /**
     * Call the register function with a dummy object
     */
    scope.register();
    scope.register.and.callFake(function(){
      return;
    });
    $httpBackend.flush();
    expect(sharedService.renewToken).toHaveBeenCalled();
  });

  /**
   * test $scope.isAddressValid()
   */
  it('should call the validateAddress function from addressService', function() {
    var address = {
      "address_components": "Galglaan 1, 9000, Gent"
    };
    scope.isAddressValid(address);
    expect(addressService.validateAddress).toHaveBeenCalled();
  });

  /**
   * test $scope.setStartAddress()
   */
  it('should call the setAddress function from addressService', function() {
    travel = {};
    travel.route = {};
    scope.setStartAddress(travel);
    expect(addressService.setAddress).toHaveBeenCalled();
  });

  /**
   * test $scope.setEndAddress()
   */
  it('should call the setAddress function from addressService', function() {
    travel = {};
    travel.route = {};
    scope.setEndAddress(travel);
    expect(addressService.setAddress).toHaveBeenCalled();
  });

  it('Should close the modal when clicking the button', function(){
    scope.close();
    expect(uibModalInstance.dismiss).toHaveBeenCalledWith("Closed new travel modal");
  });

});
