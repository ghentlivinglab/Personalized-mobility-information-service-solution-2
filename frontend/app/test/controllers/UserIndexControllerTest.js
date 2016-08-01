/**
 * Test for UserIndexController.
 */
fdescribe('UserIndexController', function() {
  beforeEach(module('App'));
  beforeEach(module('AppMock'));

  var scope,
      controller,
      $route,
      $location,
      $uibModal,
      $timeout,
      $filter,
      userServiceMock,
      eventServiceMock,
      sharedServiceMock,
      addressServiceMock,
      localStorageService,
      locationControlServiceMock,
      travelControlServiceMock,
      eventControlServiceMock;

  var user, headers;

  /**
   * Before the testing, inject all elements that are needed to test the controller
   */
  beforeEach(inject(function($rootScope, $controller, _userService_, _eventService_,
    _$uibModal_, _$timeout_, _$filter_, _$httpBackend_, _$route_,
    _$location_, _localStorageService_) {

    httpBackend = _$httpBackend_;
    basePath = "http://localhost:8080";
    scope = $rootScope.$new();

    userServiceMock = _userService_;
    eventServiceMock = _eventService_;
    sharedServiceMock = jasmine.createSpyObj('sharedService', ['getTransportTypes', 'loadData', 'loadRouteEventsAndShow', 'loadLocationEventsAndShow', 'renewToken']);
    addressServiceMock = jasmine.createSpyObj('addressService', ['initMap', 'initDirectionsRenderer', 'initDirectionsService', 'initEditShowMap']);

    $route = _$route_;
    $location = _$location_;
    $timeout = _$timeout_;
    $filter = _$filter_;

    localStorageService = _localStorageService_;
    locationControlServiceMock = jasmine.createSpyObj('locationControlService', ['findAndChangeLocationId', 'openNewLocation', 'openEditLocation', 'deleteLocation']);
    travelControlServiceMock = jasmine.createSpyObj('travelControlService', ['findAndChangeTravelId', 'openNewTravel', 'openEditTravel', 'deleteTravel']);
    eventControlServiceMock = jasmine.createSpyObj('eventControlService', ['openNewEvent', 'openEditEvent', 'deleteEvent']);

    headers = {
      'Authorization': 'access',
      'Accept': 'application/json, text/plain, */*'
    };

    $uibModal = _$uibModal_;
    modalInstance = $uibModal.open({
      template: '<p></p>'
    });

    /**
     * Mock the current state of the route
     */
    $route.current = {templateUrl: 'test'};

    /**
     * Create a dummy user object that will be returned when the controller does a request to /user
     */
    scope.currentUser = {
      "id": "1",
      "validated": {
        "email": false
      }
    };
    /**
     * Create two dummy travel objects that will be returned when the controller does a request to /user/1/travel
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
          "lon":0.0,
          "lat":0.0
        },
        "housenumber":"2",
        "postal_code":"1755"
      },
      "endpoint":{
        "id":"2",
        "street":"Galglaan",
        "city":"Gent",
        "country":"BE",
        "coordinates":{
          "lon":3.7186009,
          "lat":51.0281817
        },
        "housenumber":"1",
        "postal_code":"9000"
      },
      "time_interval":["08:00","10:00"]
    };
    travel2 = {
      "id":"2",
      "name":"test",
      "startpoint":{
        "id":"3",
        "street":"Galglaan",
        "city":"Gent",
        "country":"BE",
        "coordinates":{
          "lon":3.7186009,
          "lat":51.0281817
        },
        "housenumber":"1",
        "postal_code":"9000"
      },
      "endpoint":{
        "id":"4",
        "street":"Honingenveldstraat",
        "city":"Gooik",
        "country":"BE",
        "coordinates":{
          "lon":0.0,
          "lat":0.0
        },
        "housenumber":"2",
        "postal_code":"1755"
      },
      "time_interval":["08:00","10:00"]
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
      }],
      "transportation_type":"train",
      "notify_for_event_types":[],
      "active": true
    };
    route2 = {
      "id":"2",
      "waypoints":[{
          "lon": 0.0,
          "lat": 0.0
      }],
      "transportation_type":"train",
      "notify_for_event_types":[],
      "active": true
    };
    scope.routeId = 0;
    scope.currentTravelRoutes = [route];
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
    poi2 = {
      "id":"2",
      "address":{
        "id":"2",
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
    scope.locationId = 0;
    scope.currentUserLocations = [poi];

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

    scope.directionsDisplay = {};
    scope.directionsDisplay.setMap = function(){
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
    spyOn($uibModal, 'open').and.callThrough();
    spyOn($location, 'path').and.callThrough();
    spyOn(scope.marker, 'setPosition').and.callThrough();
    spyOn(scope.marker, 'setVisible').and.callThrough();
    spyOn(scope.marker, 'getVisible').and.callThrough();
    spyOn(scope.circle, 'setVisible').and.callThrough();
    spyOn(scope.circle, 'setCenter').and.callThrough();
    spyOn(scope.circle, 'setRadius').and.callThrough();
    spyOn(scope.circle, 'getBounds').and.callThrough();
    spyOn(scope.directionsDisplay, 'setMap').and.callThrough();
    spyOn(console, 'error').and.callThrough();

    controller = $controller('UserIndexController', {
      '$scope': scope,
      '$route': $route,
      '$location': $location,
      '$uibModal': $uibModal,
      '$timeout': $timeout,
      '$filter': $filter,
      'userService': userServiceMock,
      'eventService': eventServiceMock,
      'sharedService': sharedServiceMock,
      'addressService': addressServiceMock,
      'localStorageService': localStorageService,
      'locationControlService': locationControlServiceMock,
      'travelControlService': travelControlServiceMock,
      'eventControlService': eventControlServiceMock
    });

  }));

  /**
   * Before the testing, instantiate all scope elements that are necessary to test the functions
   */
  beforeEach(function() {

    scope.templates = [
      { name: "eventList", url: "view/event/eventList.html" },
      { name: "routeView", url: "view/user/routeContent.html" },
      { name: "locationView", url:"view/user/locationContent.html" }
    ];
    httpBackend.whenGET('view/login/loginPage.html').respond(200);
    httpBackend.whenGET('view/user/verificationModal.html').respond(200);
    httpBackend.whenGET(basePath + '/user', function() {return headers;}).respond(200, [ user ]);
    httpBackend.whenGET(basePath + '/user/1', function() {return headers;}).respond(200, user);
    httpBackend.whenGET(basePath + '/user/1/travel', function() {return headers;}).respond(200, [ travel, travel2 ]);
    httpBackend.whenGET(basePath + '/user/1/travel/1', function() {return headers;}).respond(200, travel);
    httpBackend.whenGET(basePath + '/user/1/travel/1/route', function() {return headers;}).respond(200, [ route, route2 ]);
    httpBackend.whenGET(basePath + '/user/1/point_of_interest', function() {return headers;}).respond(200, [ poi, poi2 ]);
    httpBackend.flush();
  });

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
  it('should check if all arguments of controller are defined', function() {
    expect(scope).toBeDefined();
    expect(controller).toBeDefined();
    expect($route).toBeDefined();
    expect($location).toBeDefined();
    expect($uibModal).toBeDefined();
    expect($timeout).toBeDefined();
    expect($filter).toBeDefined();
    expect(userServiceMock).toBeDefined();
    expect(eventServiceMock).toBeDefined();
    expect(sharedServiceMock).toBeDefined();
    expect(addressServiceMock).toBeDefined();
    expect(localStorageService).toBeDefined();
    expect(locationControlServiceMock).toBeDefined();
    expect(travelControlServiceMock).toBeDefined();
    expect(eventControlServiceMock).toBeDefined();
  });

  /**
   * Test if the function initPage() works correctly.
   */
  it('should check if the function initPage() works correctly', function() {
    /**
     * Test if we go into the if condition
     */
    localStorageService.set("accessToken", null);
    scope.initPage();
    expect($location.path).toHaveBeenCalled();

    /**
     * Test if we go into the else condition
     */
    localStorageService.set("accessToken", {"token": "access"});
    scope.initPage();
    expect(scope.currentPage).toBeDefined();
    expect(scope.templates).toBeDefined();
    expect(scope.template).toBeDefined();
    expect(scope.waypoints).toBeDefined();
    expect(sharedServiceMock.loadData).toHaveBeenCalled();
    expect($uibModal.open).toHaveBeenCalled();

  });

  /**
   * Test if the function logout() works correctly.
   */
  it('should check if the function logout() works correctly', function() {
    scope.logout();
    // localStorage should be totaly cleared
    expect(scope.store).toEqual({});
    expect($location.path).toHaveBeenCalled();
  });

  /**
   * Test if the function onClickTemplate() works correctly.
   */
  it('should check the function onClickTemplate()', function() {
    scope.waypoints = [];
    scope.currentUserTravels = [{}];
    scope.currentTravelRoutes = [{}];
    scope.onClickTemplate(1);
    expect(addressServiceMock.initMap).toHaveBeenCalled();
    $timeout.flush();
    expect(addressServiceMock.initDirectionsRenderer).toHaveBeenCalled();
    expect(addressServiceMock.initDirectionsService).toHaveBeenCalled();
    expect(sharedServiceMock.loadRouteEventsAndShow).toHaveBeenCalled();
    scope.onClickTemplate(2);
    $timeout.flush();
    expect(addressServiceMock.initMap).toHaveBeenCalled();
    expect(addressServiceMock.initEditShowMap).toHaveBeenCalled();
    expect(sharedServiceMock.loadLocationEventsAndShow).toHaveBeenCalled();
  });

  /**
   * Test if the function findAndChangeTravelId() works correctly.
   */
  it('should check the function findAndChangeTravelId()', function() {
    scope.findAndChangeTravelId("1");
    expect(travelControlServiceMock.findAndChangeTravelId).toHaveBeenCalled();
  });

  /**
   * Test if the function findAndChangeLocationId() works correctly.
   */
  it('should check the function findAndChangeLocationId()', function() {
    scope.findAndChangeLocationId("1");
    expect(locationControlServiceMock.findAndChangeLocationId).toHaveBeenCalled();
  });

  /**
   * Test if $uibModal.open() has been called.
   */
  it('should check if $uibModal.open has been called', function() {
    scope.openNewTravel();
    expect(travelControlServiceMock.openNewTravel).toHaveBeenCalled();

    scope.openEditTravel(travel);
    expect(travelControlServiceMock.openEditTravel).toHaveBeenCalled();

    scope.deleteTravel("0");
    expect(travelControlServiceMock.deleteTravel).toHaveBeenCalled();

    scope.openNewLocation();
    expect(locationControlServiceMock.openNewLocation).toHaveBeenCalled();

    scope.openEditLocation(poi);
    expect(locationControlServiceMock.openEditLocation).toHaveBeenCalled();

    scope.deleteLocation("1");
    expect(locationControlServiceMock.deleteLocation).toHaveBeenCalled();

    scope.openNewEvent();
    expect(eventControlServiceMock.openNewEvent).toHaveBeenCalled();

    scope.openEditEvent({});
    expect(eventControlServiceMock.openEditEvent).toHaveBeenCalled();

    scope.deleteEvent("0");
    expect(eventControlServiceMock.deleteEvent).toHaveBeenCalled();

    httpBackend.expect('GET', 'js/directives/editUserDirective.html').respond(200);
    scope.openEditUser();
    httpBackend.flush();
    expect($uibModal.open).toHaveBeenCalled();
  });


  /**
   * Test if function deteleUser() has been called.
   */
  it('should check if function deleteUser() has been called', function() {
    httpBackend.expect('DELETE', basePath + '/user/1', undefined, headers).respond(200);
    scope.deleteUser();
    httpBackend.flush();
    expect(localStorageService.clearAll).toHaveBeenCalled();
    expect($location.path).toHaveBeenCalled();

    /**
     * Should give an unauthorized error and re-execute the function
     */
    localStorageService.set("accessToken", {"token": "access"});
    httpBackend.expect('DELETE', basePath + '/user/1').respond(401);
    httpBackend.expect('DELETE', basePath + '/user/1', undefined, headers).respond(200);
    scope.deleteUser();
    httpBackend.flush();
    expect(sharedServiceMock.renewToken).toHaveBeenCalled();
    expect(localStorageService.clearAll).toHaveBeenCalled();
    expect($location.path).toHaveBeenCalled();

    /**
     * Should give an error
     */
    localStorageService.set("accessToken", {"token": "access"});
    httpBackend.expect('DELETE', basePath + '/user/1').respond(-1);
    scope.deleteUser();
    httpBackend.flush();
    expect(console.error).toHaveBeenCalled();
  });

  /**
   * Test if function convertTrueFalse() works correctly
   */
  it('should check if function convertTrueFalse() works correctly', function() {
    expect(scope.convertTrueFalse(true)).toEqual('Ja');
    expect(scope.convertTrueFalse(false)).toEqual('Neen');
  });

  /**
   * Test if function translateTransportType() works correctly
   */
  // it('should check if function translateTransportType() works correctly', function() {
  //   scope.translateTransportType("bike");
  //   expect(scope.type).toBeDefined();
  //   expect(sharedServiceMock.getTransportTypes).toHaveBeenCalled();
  // });

  /**
   * Test if function parseTime() works correctly
   */
  it('should check if function parseTime() works correctly', function() {
    expect(scope.parseTime("13:20:24")).toEqual("13:20");
  });

  /**
   * Test if function parseDays() works correctly
   */
  it('should check if function parseDays() works correctly', function() {
    var travel = {
                  // Ma,   Di,   Wo,    Do,    Vr,   Za,   Zo
      'recurring': [true, true, false, true, false, true, true]
    };
    scope.currentUserTravels = [travel];
    expect(scope.parseDays()).toEqual("Ma, Di, Do, Za, Zo");
  });

  /**
   * Test if function parseAddress() works correctly
   */
  it('should check if function parseAddress() works correctly', function() {
    /**
     * Check function if we go into the if condition
     */
    var address = {
      "housenumber": "0",
      "street": "test",
      "city": "city",
      "postal_code": "0000",
      "country": "country"
    };
    expect(scope.parseAddress(address)).toEqual("test 0, city 0000 country");
    /**
     * Check function if we go into the else condition
     */
    address.housenumber = "";
    expect(scope.parseAddress(address)).toEqual("test, city 0000 country");
  });

});
