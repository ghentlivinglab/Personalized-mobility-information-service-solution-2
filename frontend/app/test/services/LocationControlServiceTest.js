fdescribe('LocationControlServiceTest', function() {
  beforeEach(module('App'));
  beforeEach(module('AppMock'));

  var scope,
  controller,
  $uibModal,
  $timeout,
  $location,
  sharedServiceMock,
  addressServiceMock,
  locationServiceMock,
  localStorageService,
  locationControlService;

  var poi;

  /**
   * Before the testing, inject all elements that are needed to test the service
   */
  beforeEach(inject(function($rootScope, $controller, _$location_, _$uibModal_, _$timeout_,
    _locationService_, _localStorageService_, _$httpBackend_, _locationControlService_, _sharedService_, _addressService_) {

    httpBackend = _$httpBackend_;
    basePath = "http://localhost:8080";
    scope = $rootScope.$new();

    locationServiceMock = _locationService_;
    sharedServiceMock = _sharedService_;
    addressServiceMock = _addressService_;

    $location = _$location_;
    $uibModal = _$uibModal_;
    $timeout = _$timeout_;

    localStorageService = _localStorageService_;
    locationControlService = _locationControlService_;

    headers = {
      'Authorization': 'access',
      'Accept': 'application/json, text/plain, */*'
    };

    /**
    * Create a dummy marker object used in tests and spy on the functions
    */
    scope.marker = {};
    scope.marker.setMap = function(){
      return ;
    };
    scope.marker.setPosition = function(){
      return ;
    };
    scope.marker.setVisible = function(){
      return ;
    };
    scope.marker.getVisible = function(){
      return true;
    };

    /**
    * Create a dummy circle object used in tests and spy on the functions
    */
    scope.circle = {};
    scope.circle.setMap = function(){
      return ;
    };
    scope.circle.setCenter = function(){
      return ;
    };
    scope.circle.setVisible = function(){
      return ;
    };
    scope.circle.setRadius = function(){
      return ;
    };
    scope.circle.getBounds = function(){
      return ;
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
    spyOn(sharedServiceMock, 'loadData').and.callThrough();
    spyOn(sharedServiceMock, 'loadLocationEventsAndShow').and.callThrough();
    spyOn(addressServiceMock, 'initMap').and.callFake(function(mapId) {});
    spyOn(addressServiceMock, 'initEditShowMap').and.callFake(function(scope, map, address, radius, boolean) {});
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
     * Create a dummy point of interest (location) object that will be returned when the controller does a request to /user/1/point_of_interest
     */
    poi = {
      "id":"1",
      "address": {
        "street": "test",
        "housenumber": "0",
        "postal_code": "0000",
        "city": "test"
      }
    };
    scope.locationId = 0;
    scope.currentUserLocations = [ poi ];

    httpBackend.whenGET(basePath + '/user/1', function() {return headers;}).respond(200, {"id": "1"});
    httpBackend.whenGET(basePath + '/user/1/travel', function() {return headers;}).respond(200, [ {"id": "1"} ]);
    httpBackend.whenGET(basePath + '/user/1/travel/1', function() {return headers;}).respond(200, {"id": "1"});
    var route = {
      "id":"1",
      "waypoints":[{
          "lon": 0.0,
          "lat": 0.0
      }]
    };
    httpBackend.whenGET(basePath + '/user/1/travel/1/route', function() {return headers;}).respond(200, [ route ]);
    httpBackend.whenGET(basePath + '/user/1/point_of_interest', function() {return headers;}).respond(200, [ poi ]);
    httpBackend.whenGET(basePath + '/user/1/point_of_interest/1/events', function() {return headers;}).respond(200, []);

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
    expect(locationServiceMock).toBeDefined();
    expect(sharedServiceMock).toBeDefined();
    expect(addressServiceMock).toBeDefined();
    expect(localStorageService).toBeDefined();
    expect(locationControlService).toBeDefined();
  });

  /**
   * Test if the functions findAndChangeTravelId and changeTravelId work correctly.
   */
  it('should check if the functions findAndChangeTravelId changeTravelId work correctly', function() {
    locationControlService.findAndChangeLocationId(scope, "1");
    expect(scope.currentUserLocations).toBeDefined();
    expect(scope.locationId).toEqual(0);

    expect(addressServiceMock.initEditShowMap).toHaveBeenCalled();
    $timeout.flush();
    expect(sharedServiceMock.loadLocationEventsAndShow).toHaveBeenCalled();
    httpBackend.flush();
  });

  /**
   * Test if the function openNewLocation works correctly.
   */
  it('should check if the function openNewLocation works correctly', function() {
    /**
     * Overwrite the spy.
     */
    $uibModal.open.and.callFake(function(){
      return {
          result: {
              then: function(callback, errorCallback) {
                  callback(poi);
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
    scope.currentUserLocations = undefined;
    locationControlService.openNewLocation(scope);
    expect($uibModal.open).toHaveBeenCalled();
    expect(scope.locationId).toEqual(0);
    expect(addressServiceMock.initMap).toHaveBeenCalled();
    expect(addressServiceMock.initEditShowMap).toHaveBeenCalled();

    /**
     * Should go into the first else conditions
     */
    scope.currentUserLocations = [ poi ];
    locationControlService.openNewLocation(scope);
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
    locationControlService.openNewLocation(scope);
    expect(console.error).toHaveBeenCalled();

  });

  /**
   * Test if the function openEditLocation works correctly.
   */
  it('should check if the function openEditLocation works correctly', function() {
    /**
     * Overwrite the spy.
     */
    $uibModal.open.and.callFake(function(){
      return {
          result: {
              then: function(callback, errorCallback) {
                  callback(poi);
              }
          },
          rendered: {
              then: function(callback) {
                  callback();
              }
          }
      };
    });
    locationControlService.openEditLocation(scope, poi);
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
    locationControlService.openEditLocation(scope, poi);
    expect(console.error).toHaveBeenCalled();

  });

  /**
   * Test if function deleteLocation() has been called.
   */
  it('should check if function deleteLocation() has been called', function() {
    httpBackend.expect('DELETE', basePath + '/user/1/point_of_interest/1', undefined, headers).respond(200);
    locationControlService.deleteLocation(scope, "1");
    httpBackend.flush();
    expect(addressServiceMock.initMap).toHaveBeenCalled();

    /**
     * Should give an unauthorized error and re-execute the function
     */
    httpBackend.expect('DELETE', basePath + '/user/1/point_of_interest/1').respond(401);
    httpBackend.expect('DELETE', basePath + '/user/1/point_of_interest/1', undefined, headers).respond(200);
    locationControlService.deleteLocation(scope, "1");
    httpBackend.flush();
    expect(sharedServiceMock.renewToken).toHaveBeenCalled();
    expect(addressServiceMock.initMap).toHaveBeenCalled();

    /**
     * Should give an error
     */
    httpBackend.expect('DELETE', basePath + '/user/1/point_of_interest/1').respond(-1);
    locationControlService.deleteLocation(scope, "1");
    httpBackend.flush();
    expect(console.error).toHaveBeenCalled();
  });

});
