fdescribe('TravelControlServiceTest', function() {
  beforeEach(module('App'));
  beforeEach(module('AppMock'));

  var scope,
  controller,
  $location,
  $uibModal,
  $timeout,
  travelServiceMock,
  routeServiceMock,
  sharedServiceMock,
  addressServiceMock,
  localStorageService,
  travelControlService;

  var travel, route;

  /**
   * Before the testing, inject all elements that are needed to test the service
   */
  beforeEach(inject(function($rootScope, $controller, _$location_, _$uibModal_, _$timeout_,
    _routeService_, _travelService_, _localStorageService_, _$httpBackend_, _travelControlService_, _sharedService_, _addressService_) {

    httpBackend = _$httpBackend_;
    basePath = "http://localhost:8080";
    scope = $rootScope.$new();

    travelServiceMock = _travelService_;
    routeServiceMock = _routeService_;
    sharedServiceMock = _sharedService_;
    addressServiceMock = _addressService_;

    $location = _$location_;
    $uibModal = _$uibModal_;
    $timeout = _$timeout_;

    localStorageService = _localStorageService_;
    travelControlService = _travelControlService_;

    headers = {
      'Authorization': 'access',
      'Accept': 'application/json, text/plain, */*'
    };

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
    spyOn(console, 'error').and.callThrough();
    spyOn(sharedServiceMock, 'renewToken').and.callThrough();
    spyOn(sharedServiceMock, 'loadRouteEventsAndShow').and.callThrough();
    spyOn(addressServiceMock, 'initMap').and.callFake(function(mapId) {});
    spyOn(addressServiceMock, 'initDirectionsService').and.callThrough();
    spyOn(addressServiceMock, 'initDirectionsRenderer').and.callThrough();
    spyOn($location, 'path').and.callThrough();
    /**
     * The first callback is the success callback. You can return whatever you want to the calling controller
     * The second is the error callback where you can also specify a return value but not have to
     */
    spyOn($uibModal, 'open').and.callFake(function() {
        return {
            result: {
                then: function(callback, errorCallback) {
                    callback("item1");
                    errorCallback();
                }
            },
            rendered: {
                then: function(callback) {
                    callback();
                }
            }
        };
    });
    /**
     * Create a dummy user object that will be returned when the controller does a request to /user
     */
    scope.currentUser = {
      "id": "1"
    };
    /**
     * Create a dummy travel object that will be returned when the controller does a request to /user/1/travel
     */
    travel = {
      "id":"1"
    };
    scope.travelId = 0;
    scope.currentUserTravels = [travel];
    /**
     * Create a dummy route object that will be returned when the controller does a request to /user/1/travel/1/route
     */
    route = {
      "id":"1",
      "waypoints":[{
          "lon": 0.0,
          "lat": 0.0
      }]
    };
    scope.routeId = 0;
    scope.currentTravelRoutes = [route];

    httpBackend.whenGET(basePath + '/user/1/travel/1/route/1/events', function() {return headers;}).respond(200, []);

  }));

  /*
   * Check after each test if there are any pending requests left.
   */
  afterEach(function() {
    httpBackend.verifyNoOutstandingExpectation();
    httpBackend.verifyNoOutstandingRequest();
  });

  /**
   * Test whether everything has been defined.
   */
  it('should check if all parameters are defined', function() {
    expect(scope).toBeDefined();
    expect($location).toBeDefined();
    expect($uibModal).toBeDefined();
    expect($timeout).toBeDefined();
    expect(routeServiceMock).toBeDefined();
    expect(sharedServiceMock).toBeDefined();
    expect(addressServiceMock).toBeDefined();
    expect(travelServiceMock).toBeDefined();
    expect(localStorageService).toBeDefined();
  });

  /**
   * Test if the functions findAndChangeTravelId and changeTravelId work correctly.
   */
  it('should check if the functions findAndChangeTravelId changeTravelId work correctly', function() {
    httpBackend.expect('GET', basePath + '/user/1/travel/1/route', undefined, headers).respond(200, [ route ]);
    travelControlService.findAndChangeTravelId(scope, "1");
    httpBackend.flush();
    expect(scope.currentTravelRoutes).toBeDefined();
    expect(scope.routeId).toEqual(0);
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
    expect(scope.directionsService).toBeDefined();
    $timeout.flush();
    httpBackend.flush();

    /**
     * Should give an unauthorized error and re-execute the function
     */
    httpBackend.expect('GET', basePath + '/user/1/travel/1/route').respond(401);
    httpBackend.expect('GET', basePath + '/user/1/travel/1/route', undefined, headers).respond(200, [ route ]);
    travelControlService.changeTravelId(scope, 0);
    httpBackend.flush();
    expect(sharedServiceMock.renewToken).toHaveBeenCalled();
    expect(scope.currentTravelRoutes).toBeDefined();
    expect(scope.routeId).toEqual(0);
    /**
     * We expect to have 1 waypoint in the array (correctly converted to the google maps API)
     */
    result_waypoints = [];
    waypoint = JSON.parse('{ "lng": 0.0, "lat": 0.0 }');
    result_waypoints.push({
      location: waypoint,
      stopover: false
    });
    expect(scope.waypoints).toEqual(result_waypoints);
    expect(scope.directionsService).toBeDefined();
    $timeout.flush();
    httpBackend.flush();

    /**
     * Should give an error
     */
    httpBackend.expect('GET', basePath + '/user/1/travel/1/route').respond(-1);
    travelControlService.changeTravelId(scope, 0);
    httpBackend.flush();
    expect(console.error).toHaveBeenCalled();
  });

  /**
   * Test if the function openNewTravel works correctly.
   */
  it('should check if the function openNewTravel works correctly', function() {
    /**
     * Overwrite the spy.
     */
    $uibModal.open.and.callFake(function(){
      return {
          result: {
              then: function(callback, errorCallback) {
                  callback([{}, {}]);
              }
          },
          rendered: {
              then: function(callback) {
                  callback();
              }
          }
      };
    });
    /**
     * Should go into the first two if conditions
     */
    scope.currentTravelRoutes = undefined;
    scope.currentUserTravels = [];
    travelControlService.openNewTravel(scope);
    expect($uibModal.open).toHaveBeenCalled();
    expect(scope.travelId).toEqual(0);
    expect(scope.routeId).toEqual(0);
    expect(addressServiceMock.initDirectionsRenderer).toHaveBeenCalled();
    expect($location.path).toHaveBeenCalled();
    expect(addressServiceMock.initMap).toHaveBeenCalled();

    /**
     * Should go into the first else conditions
     */
    scope.currentUserTravels = [ travel ];
    scope.currentTravelRoutes = [ route ];
    travelControlService.openNewTravel(scope);
    expect($uibModal.open).toHaveBeenCalled();
    expect($location.path).toHaveBeenCalled();
    expect(addressServiceMock.initMap).toHaveBeenCalled();


    /**
     * Overwrite the spy and only return the error. So the console.error() funtion would be called
     */
    $uibModal.open.and.callFake(function(){
      return {
          result: {
              then: function(callback, errorCallback) {
                  errorCallback();
              }
          },
          rendered: {
              then: function() {}
          }
      };
    });
    travelControlService.openNewTravel(scope);
    expect(console.error).toHaveBeenCalled();

  });

  /**
   * Test if the function openEditTravel works correctly.
   */
  it('should check if the function openEditTravel works correctly', function() {
    /**
     * Overwrite the spy.
     */
    $uibModal.open.and.callFake(function(){
      return {
          result: {
              then: function(callback, errorCallback) {
                  callback({});
              }
          },
          rendered: {
              then: function(callback) {
                  callback();
              }
          }
      };
    });
    travelControlService.openEditTravel(scope, travel);
    expect($uibModal.open).toHaveBeenCalled();

    /**
     * Overwrite the spy and only return the error. So the console.error() funtion would be called
     */
    $uibModal.open.and.callFake(function(){
      return {
          result: {
              then: function(callback, errorCallback) {
                  errorCallback();
              }
          },
          rendered: {
              then: function() {}
          }
      };
    });
    travelControlService.openEditTravel(scope, travel);
    expect(console.error).toHaveBeenCalled();

  });

  /**
   * Test if function deleteTravel() has been called.
   */
  it('should check if function deleteTravel() has been called', function() {
    httpBackend.expect('DELETE', basePath + '/user/1/travel/1', undefined, headers).respond(200);
    travelControlService.deleteTravel(scope, "1");
    httpBackend.flush();
    expect(addressServiceMock.initMap).toHaveBeenCalled();
    expect($location.path).toHaveBeenCalled();

    /**
     * Should give an unauthorized error and re-execute the function
     */
    httpBackend.expect('DELETE', basePath + '/user/1/travel/1').respond(401);
    httpBackend.expect('DELETE', basePath + '/user/1/travel/1', undefined, headers).respond(200);
    travelControlService.deleteTravel(scope, "1");
    httpBackend.flush();
    expect(sharedServiceMock.renewToken).toHaveBeenCalled();
    expect(addressServiceMock.initMap).toHaveBeenCalled();
    expect($location.path).toHaveBeenCalled();

    /**
     * Should give an error
     */
    httpBackend.expect('DELETE', basePath + '/user/1/travel/1').respond(-1);
    travelControlService.deleteTravel(scope, "1");
    httpBackend.flush();
    expect(console.error).toHaveBeenCalled();
  });

});
