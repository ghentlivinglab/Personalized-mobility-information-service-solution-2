/*
 * Test for EditTravelController
 */
fdescribe('Test EditTravelController', function() {
  beforeEach(module('App'));

  var q,
      controller,
      httpBackend,
      scope,
      uibModalInstance,
      $route,
      user,
      travel,
      map,
      travelService,
      routeService,
      errorService,
      sharedService,
      addressService,
      localStorageService,
      basePath,
      store,
      headers;

  beforeEach(inject(function(
    $controller,
    _$httpBackend_,
    _$q_,
    $rootScope,
    _$route_,
    _travelService_,
    _routeService_,
    _errorService_,
    _sharedService_,
    _addressService_,
    _localStorageService_) {

    // Set basepath for all the httpbackend requests
    // basePath = "https://vopro6.ugent.be/api";
    basePath = "http://localhost:8080";
    q = _$q_;
    httpBackend = _$httpBackend_;
    scope = $rootScope.$new();
    $route = _$route_;

    user = {};
    user.id = 1;
    map = {};
    sharedService = _sharedService_;
    travelService = _travelService_;
    errorService = _errorService_;
    routeService = _routeService_;
    addressService = _addressService_;
    localStorageService = _localStorageService_;

    uibModalInstance = {
      rendered: {
        then: function(callback, errorCallback) {
          callback();
        }
      },
      close: function() {
        return true;
      },
      dismiss: function() {
        return true;
      }
    };

    headers = {
      'Authorization': 'access',
      'Accept': 'application/json, text/plain, */*'
    };

    uibModalInstance.map = "map";

    travel = {
      "id": "1",
      "route": {
        "waypoints": [
          { "lon": 3.7245311, "lat": 51.0475687 },
          { "lon": 3.7296984, "lat": 51.0487834 }
        ],
        "startpoint": "3.6",
        "endpoint": "3.8",
        "transportation_types": ["bike"]
      },
      "date": "15/03/2016",
      "startpoint": {
        "street": "Warandestraat",
        "housenumber": "3",
        "postal_code": "9000",
        "city": "Gent"
      },
      "endpoint": {
        "street": "Vierhekkenstraat",
        "housenumber": "17",
        "postal_code": "9000",
        "city": "Gent"
      },
      "time_interval": ["23:59:00", "23:59:00"],
    };

    route = [{
      "id": "1",
      "waypoints": {},
      "transportation_type": "BUS",
      "active": "true"
    }];

    types = [ { name: 'Bus', code: 'bus' },
              { name: 'Auto', code: 'car' },
              { name: 'Fiets', code: 'bike' },
              { name: 'Trein', code: 'train' } ];

    /**
     * Spy on the following functions.
     */
    store = {};
    spyOn(localStorageService, 'get').and.callFake(function(key) {
      return store[key];
    });
    spyOn(localStorageService, 'set').and.callFake(function(key, value) {
      store[key] = value;
    });
    spyOn(addressService, 'initDirectionsRenderer').and.callFake(function() {
      return true;
    });
    spyOn(addressService, 'initDirectionsService').and.callFake(function() {
      return true;
    });
    spyOn(sharedService, 'convertWaypoints').and.callThrough();
    spyOn(sharedService, 'getTransportTypes').and.callThrough();
    spyOn(sharedService, 'findWaypoints').and.callFake(function(){
      var deferred = q.defer();
      deferred.resolve("test");
      return deferred.promise;
     });
    spyOn(sharedService, 'setEventTypeRelevance').and.callFake(function() {
      return "relevant_transportation_type";
    });
    spyOn(sharedService, 'renewToken').and.callThrough();
    spyOn(addressService, 'validateAddress').and.callThrough();
    spyOn(addressService, 'setAddress').and.callFake(function() { });
    spyOn(uibModalInstance, 'close').and.callThrough();
    spyOn(uibModalInstance, 'dismiss').and.callThrough();
    spyOn(errorService, 'handleTravelError').and.callThrough();
    spyOn(errorService, 'handleRouteError').and.callThrough();
    spyOn($route, 'reload').and.callThrough();

    /**
     * Initialize the controller to test it.
     */
    localStorageService.set("accessToken", {token: "access"});

    controller = $controller('EditTravelController', {
      $scope: scope,
      $route: $route,
      $uibModalInstance: uibModalInstance,
      user: user,
      travel: travel,
      map: map,
      travelService: travelService,
      routeService: routeService,
      sharedService: sharedService,
      addressService: addressService,
      errorService: errorService,
      localStorageService: localStorageService
    });

    httpBackend.whenGET('view/login/loginPage.html').respond(200);
    httpBackend.whenGET(basePath + '/user/1/travel/1/route', undefined, headers).respond(200, route);
    httpBackend.whenGET(basePath + '/eventtype').respond(200, types);
    httpBackend.flush();
  }));

  /**
   * Check after each test if there are any pending requests left.
   */
  afterEach(function() {
   httpBackend.verifyNoOutstandingExpectation();
   httpBackend.verifyNoOutstandingRequest();
  });

  it('should have defined all parameters', function() {
    expect(scope).toBeDefined();
    expect(uibModalInstance).toBeDefined();
    expect(travelService).toBeDefined();
    expect(user).toBeDefined();
    expect(travel).toBeDefined();
    expect(map).toBeDefined();
    expect(sharedService).toBeDefined();
    expect(travelService).toBeDefined();
    expect(routeService).toBeDefined();
    expect(controller).toBeDefined();
    expect(addressService).toBeDefined();
  });

  it('should set parameters in scope correctly', function() {
    expect(localStorageService.get("accessToken")).toEqual({token: "access"});

    expect(scope.user).toBeDefined();
    expect(scope.user).toBe(user);

    expect(scope.travel).toBeDefined();
    expect(scope.travel.id).toBe("1");
    expect(scope.travel).toEqual(travel);

    expect(scope.map).toBe("map");
    expect(scope.route.id).toEqual("1");

    expect(sharedService.convertWaypoints).toHaveBeenCalled();
    expect(addressService.initDirectionsRenderer).toHaveBeenCalled();
    expect(scope.directionsDisplay).toBe(true);
    expect(addressService.initDirectionsService).toHaveBeenCalled();
    expect(scope.directionsService).toBe(true);
  });

  it('should refresh the token while getting the route data', function() {
    httpBackend.expectGET(basePath + '/user/1/travel/1/route').respond(401);

    scope.initRouteData();
    httpBackend.flush();

    expect(sharedService.renewToken).toHaveBeenCalled();
    expect($route.reload).toHaveBeenCalled();
  });

  it('should set the right parameters after the modal is rendered', function() {
    expect(scope.travelInfo).toBeDefined();
    expect(scope.currentStartAddress).toBe("Warandestraat 3, 9000 Gent");
    expect(scope.currentEndAddress).toBe("Vierhekkenstraat 17, 9000 Gent");
    expect(scope.travel.time_interval[0]).toBe("23:59");
    expect(scope.travel.time_interval[1]).toBe("23:59");
    // expect(scope.transportTypes).toEqual(types);
  });

  it('should dismiss the modal', function() {
    scope.close();
    expect(uibModalInstance.dismiss).toHaveBeenCalled();
  });

  it('should call $uibModalInstance.close()', function() {
    /**
     * Wait for the request to be send.
     */
    httpBackend.whenPUT(basePath + '/user/1/travel/1').respond(200, travel);
    httpBackend.whenPUT(basePath + '/user/1/travel/1/route/1').respond(200);

    /**
     * Call the function in which the request is send.
     */
    scope.register(scope.travel, scope.route);
    httpBackend.flush();

    /**
     * Check whether the modal was closed.
     */
    expect(uibModalInstance.close).toHaveBeenCalled();
  });

  it('should call errorService.handleTravelError()', function() {
    httpBackend.expectPUT(basePath + '/user/1/travel/1').respond(-1);
    scope.register(scope.travel, scope.route);
    httpBackend.flush();

    expect(errorService.handleTravelError).toHaveBeenCalled();
    expect(scope.connectionRefused).toBe(true);
  });

  it('should retry to register when the token has expired when updating the travel', function() {
    httpBackend.expectPUT(basePath + '/user/1/travel/1').respond(401);
    httpBackend.expectPUT(basePath + '/user/1/travel/1').respond(200);
    httpBackend.whenPUT(basePath + '/user/1/travel/1/route/1').respond(200);

    scope.register(scope.travel, scope.route);
    httpBackend.flush();

    expect(sharedService.renewToken).toHaveBeenCalled();
    expect(uibModalInstance.close).toHaveBeenCalled();
  });

  it('should retry to register when the token has expired when updating the route', function() {
    httpBackend.expectPUT(basePath + '/user/1/travel/1').respond(200);
    httpBackend.expectPUT(basePath + '/user/1/travel/1/route/1').respond(401);
    httpBackend.expectPUT(basePath + '/user/1/travel/1').respond(200);
    httpBackend.whenPUT(basePath + '/user/1/travel/1/route/1').respond(200);

    scope.register(scope.travel, scope.route);
    httpBackend.flush();

    expect(sharedService.renewToken).toHaveBeenCalled();
  });

  it('should call errorService.handleRouteError()', function() {
    httpBackend.expectPUT(basePath + '/user/1/travel/1').respond(200, travel);
    httpBackend.expectPUT(basePath + '/user/1/travel/1/route/1').respond(-1);
    scope.register(scope.travel, scope.route);
    httpBackend.flush();

    /**
     * Check if the method was called.
     */
    expect(errorService.handleRouteError).toHaveBeenCalled();
  });

  it('should test #scope.isAddressValid', function() {
    scope.isAddressValid(undefined);
    expect(addressService.validateAddress).toHaveBeenCalled();
  });

  it('should set $scope.setStartAddress', function() {
    scope.setStartAddress([]);
    expect(addressService.setAddress).toHaveBeenCalled();
  });

  it('should set $scope.setEndAddress', function() {
    scope.setEndAddress([]);
    expect(addressService.setAddress).toHaveBeenCalled();
  });

});
