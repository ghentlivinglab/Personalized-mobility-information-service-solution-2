/*
 * Test for RegistrationController
 */
fdescribe('Test RegistrationController', function() {
  beforeEach(module('App'));

  var scope,
      filter,
      location,
      controller,
      sharedService,
      addressService,
      errorService,
      registrationService,
      travelControlService,
      locationControlService,
      localStorageService,
      accessTokenService,
      userService,
      regularRefreshTokenService,
      httpBackend,
      basePath;

  beforeEach(inject(function(
    $controller,
    $rootScope,
    $location,
    $filter,
    _sharedService_,
    _addressService_,
    _errorService_,
    _registrationService_,
    _travelControlService_,
    _locationControlService_,
    _localStorageService_,
    _accessTokenService_,
    _userService_,
    _regularRefreshTokenService_,
    _$httpBackend_) {

    scope = $rootScope.$new();
    location = $location;
    filter = $filter;
    sharedService = _sharedService_;
    addressService = _addressService_;
    errorService = _errorService_;
    registrationService = _registrationService_;
    travelControlService = _travelControlService_;
    locationControlService = _locationControlService_;
    localStorageService = _localStorageService_;
    accessTokenService = _accessTokenService_;
    userService = _userService_;
    regularRefreshTokenService = _regularRefreshTokenService_;
    httpBackend = _$httpBackend_;

    // basePath = "https://vopro6.ugent.be/api";
    basePath = "http://localhost:8080";

    /**
     * Spy on the following functions.
     */
    scope.store = {
       "registrationUser": "user",
       "refreshToken": "refresh",
       "accessToken": {
         token: "access"
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
      spyOn(localStorageService, 'remove').and.callFake(function (key) {
          scope.store[key] = undefined;
      });

      spyOn(addressService, 'initMap').and.callFake(function() {});
      spyOn(addressService, 'initDirectionsRenderer').and.callFake(function() {});
      spyOn(addressService, 'initDirectionsService').and.callFake(function() {});
      spyOn(addressService, 'initEditShowMap').and.callFake(function() {});

      spyOn(errorService, 'handleUserError').and.callThrough();

      spyOn(sharedService, 'loadData').and.callFake(function(scope) {
        scope.currentUserTravels = [];
        scope.currentUserLocations = [];
      });
      spyOn(sharedService, 'renewToken').and.callThrough();

      // spyOn(locationControlService, 'openNewLocation').and.callFake(function() {});
      spyOn(locationControlService, 'openNewLocation').and.callThrough();
      spyOn(locationControlService, 'openEditLocation').and.callThrough();
      spyOn(locationControlService, 'deleteLocation').and.callThrough();
      spyOn(locationControlService, 'findAndChangeLocationId').and.callThrough();

      spyOn(travelControlService, 'openNewTravel').and.callThrough();
      spyOn(travelControlService, 'openEditTravel').and.callThrough();
      spyOn(travelControlService, 'deleteTravel').and.callThrough();
      spyOn(travelControlService, 'findAndChangeTravelId').and.callThrough();

      spyOn(registrationService, 'clearRegistration').and.callThrough();

      spyOn(location, 'path').and.callThrough();

    /**
     * Initialize the controller to test it.
     */
    controller = $controller('RegistrationController', {
      $scope: scope,
      $filter: filter,
      $location: location,
      sharedService: sharedService,
      addressService: addressService,
      errorService: errorService,
      registrationService: registrationService,
      travelControlService: travelControlService,
      locationControlService: locationControlService,
      localStorageService: localStorageService,
      accessTokenService: accessTokenService,
      userService: userService,
      regularRefreshTokenService: regularRefreshTokenService
    });
  }));

  it('should initialize all the parameters correctly', function() {
    expect(scope).toBeDefined();
    expect(filter).toBeDefined();
    expect(location).toBeDefined();
    expect(sharedService).toBeDefined();
    expect(addressService).toBeDefined();
    expect(registrationService).toBeDefined();
    expect(travelControlService).toBeDefined();
    expect(locationControlService).toBeDefined();
    expect(localStorageService).toBeDefined();
    expect(accessTokenService).toBeDefined();
    expect(regularRefreshTokenService).toBeDefined();
    expect(userService).toBeDefined();
    expect(regularRefreshTokenService).toBeDefined();
    expect(controller).toBeDefined();
    expect(httpBackend).toBeDefined();
  });

  it('should set parameters in scope correctly', function() {
    expect(scope.index).toBe(0);
    expect(scope.template.name).toBe("travel");
  });

  it('should call openNewLocation in locationControlService', function() {
    scope.openNewLocation();
    expect(locationControlService.openNewLocation).toHaveBeenCalled();
  });

  it('should call openEditLocation in locationControlService', function() {
    scope.openEditLocation();
    expect(locationControlService.openEditLocation).toHaveBeenCalled();
  });

  it('should call deleteLocation in locationControlService', function() {
    scope.currentUser = { id: 1 };
    scope.deleteLocation();
    expect(locationControlService.deleteLocation).toHaveBeenCalled();
  });

  it('should call findAndChangeLocationId in locationControlService', function() {
    scope.findAndChangeLocationId();
    expect(locationControlService.findAndChangeLocationId).toHaveBeenCalled();
  });

  it('should call openNewTravel in travelControlService', function() {
    scope.openNewTravel();
    expect(travelControlService.openNewTravel).toHaveBeenCalled();
  });

  it('should call openEditTravel in travelControlService', function() {
    scope.openEditTravel();
    expect(travelControlService.openEditTravel).toHaveBeenCalled();
  });

  it('should call deleteTravel in travelControlService', function() {
    scope.currentUser = { id: 1 };
    scope.deleteTravel();
    expect(travelControlService.deleteTravel).toHaveBeenCalled();
  });

  it('should call findAndChangeTravelId in travelControlService', function() {
    scope.currentUser = { id: 1 };
    scope.findAndChangeTravelId();
    expect(travelControlService.findAndChangeTravelId).toHaveBeenCalled();
  });

  it('should test onClickTemplate when travels are displayed', function() {
    scope.travelId = 0;
    scope.routeId = 0;
    scope.currentTravelRoutes = [];
    scope.currentUserTravels = [{
      id: 1
    }];

    scope.onClickTemplate(0);
    expect(scope.template.name).toBe("travel");
    expect(addressService.initDirectionsRenderer).toHaveBeenCalled();
    expect(addressService.initDirectionsService).toHaveBeenCalled();
  });

  it('should test onClickTemplate when locations are displayed', function() {
    scope.locationId = 0;
    scope.currentUserLocations = [{
      id: 1,
      address: {
        street: "Patijntjesstraat",
        housenumber: "3",
        city: "Gent",
        postal_code: "9000",
        country: "BE"
      }
    }];

    var address = "Patijntjesstraat 3, 9000 Gent";

    scope.onClickTemplate(1);
    expect(scope.template.name).toBe("location");
    expect(addressService.initEditShowMap).toHaveBeenCalled();
  });

  it('should complete the registration', function() {
    scope.register();
    expect(scope.store.registrationUser).toBe(undefined);
    expect(registrationService.clearRegistration).toHaveBeenCalled();
    expect(location.path).toHaveBeenCalledWith('/userindex');
    scope.$apply();
    expect(location.path()).toBe('/userindex');
  });

  it('should correctly cancel the registration', function() {
    scope.currentUser = { id: 1 };

    httpBackend.expectDELETE(basePath + '/user/1').respond(200);
    scope.cancel();
    httpBackend.flush();

    expect(localStorageService.clearAll).toHaveBeenCalled();
    expect(registrationService.clearRegistration).toHaveBeenCalled();
    expect(location.path).toHaveBeenCalledWith('/index');
    scope.$apply();
    expect(location.path()).toBe('/index');
  });

  it('should not cancel the registration', function() {
    scope.currentUser = { id: 1 };

    httpBackend.expectDELETE(basePath + '/user/1').respond(500);
    scope.cancel();
    httpBackend.flush();

    expect(errorService.handleUserError).toHaveBeenCalled();
  });

  it('should retry to cancel the registration', function() {
    scope.currentUser = { id: 1 };

    httpBackend.expectDELETE(basePath + '/user/1').respond(401);
    httpBackend.expectDELETE(basePath + '/user/1').respond(200);
    scope.cancel();
    httpBackend.flush();

    expect(sharedService.renewToken).toHaveBeenCalled();
  });

  it('should convert correctly', function() {
    expect(scope.convertTrueFalse(true)).toBe('Ja');
    expect(scope.convertTrueFalse(false)).toBe('Neen');
  });

  it('should translate the transportation type correctly', function() {
    var transportatation_type = "train";
    expect(scope.translateTransportType(transportatation_type)).toBe("Trein");
  });

  it('should parse the time correctly', function() {
    expect(scope.parseTime("08:00:00")).toBe("08:00");
  });

  it('should parse the days correctly', function() {
    scope.travelId = 0;
    scope.currentUserTravels = [];
    scope.currentUserTravels[0] = {};
    scope.currentUserTravels[0].recurring = [true, false, false, true, false, false, false];
    var result = "Ma, Do";
    expect(scope.parseDays()).toBe(result);
  });

  it('should parse the addres correctly', function() {
    var address = {
      street: "Patijntjesstraat",
      housenumber: "2",
      city: "Gent",
      postal_code: "9031",
      country: "BE"
    };

    expect(scope.parseAddress(address)).toBe("Patijntjesstraat 2, Gent 9031 BE");

    address = {
      street: "Patijntjesstraat",
      city: "Gent",
      housenumber: "",
      postal_code: "9031",
      country: "BE"
    };

    expect(scope.parseAddress(address)).toBe("Patijntjesstraat, Gent 9031 BE");
  });

});
