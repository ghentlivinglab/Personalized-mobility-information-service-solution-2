fdescribe('EditLocationController', function() {
  beforeEach(module('App'));
  beforeEach(module('AppMock'));

  var scope,
  controller,
  $uibModalInstance,
  user,
  location,
  locationService,
  addressService,
  sharedService,
  errorService,
  localStorageService;

  beforeEach(inject(function($controller, _$httpBackend_, _$q_, $rootScope,
    _locationService_, _errorService_, _sharedService_, _addressService_, _localStorageService_) {

  	// Set basepath for all the httpbackend requests
    basePath = "http://localhost:8080";
    q = _$q_;
    scope = $rootScope.$new();
    $httpBackend = _$httpBackend_;

    headers = {
      'Authorization': 'access',
      'Accept': 'application/json, text/plain, */*',
      'Content-Type': 'application/json;charset=utf-8'
    };

    user = {};
    user.id = 1;

    map = {};

    location = {};
    location.id = 1;
    location.name = "test";
    location.radius = 30;
    location.address = {};
    location.address.street = "Honingenveldstraat";
    location.address.housenumber = "2";
    location.address.postal_code = "9000";
    location.address.city = "Gent";


    sharedService = _sharedService_;
    errorService = _errorService_;
    addressService = _addressService_;
    locationService = _locationService_;
    localStorageService = _localStorageService_;
    localStorageService.set("accessToken", {'token': 'access'});

    $uibModalInstance = {
      rendered: {
        then: function() {
          var deferred = q.defer();
          deferred.resolve();
          return deferred.promise;
        }
      },
      close: function() {
        return true;
      },
      dismiss: function() {
        return true;
      }
    };

    /**
     * Initialize the controller to test it.
     */
    controller = $controller('EditLocationController', {
      $scope: scope,
      $uibModalInstance: $uibModalInstance,
      user: user,
      location: location,
      locationService: locationService,
      addressService: addressService,
      sharedService: sharedService,
      errorService: errorService,
      localStorageService: localStorageService
    });

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
    spyOn(addressService, 'setAddress').and.callFake(function() {});
    spyOn(sharedService, 'renewToken').and.callThrough();
    spyOn($uibModalInstance, 'close').and.callThrough();
    spyOn($uibModalInstance, 'dismiss').and.callThrough();
    spyOn(errorService, 'handlePointOfInterestError').and.callThrough();
    spyOn(addressService, 'validateAddress').and.callFake(function() {});

  }));

  /**
   * Check if all parameters are defined
   */
   it('Should check if all parameters are defined', function() {
     expect(scope).toBeDefined();
     expect($uibModalInstance).toBeDefined();
     expect(user).toBeDefined();
     expect(location).toBeDefined();
     expect(locationService).toBeDefined();
     expect(addressService).toBeDefined();
     expect(sharedService).toBeDefined();
     expect(errorService).toBeDefined();
     expect(localStorageService).toBeDefined();
   });

	/**
	 * Check if the correct variables are initialized
	 */
	it('Should correctly initialize the locationInfo objects in the scope', function(){
    scope.initPage();
		/**
		 * Test if the locationInfo is correctly set in the scope
		 */
		expect(scope.locationInfo.name).toEqual('test');
		expect(scope.locationInfo.radius).toEqual(30);

    /**
     * Test if we go into the else condition
     */
    location.address.housenumber = "";
    scope.initPage();
    expect(scope.currentAddress).toEqual("Honingenveldstraat, 9000 Gent");
	});

  /**
   * Test if the function close() works correctly
   */
  it('Should check if the function close() works correctly', function() {
    scope.close();
    expect($uibModalInstance.dismiss).toHaveBeenCalled();
  });

	/**
	 * Check if the editing works correctly
	 */
	it('Should correctly edit and put a location to the server', function(){

		/**
		 * Set a location so we get in the if-statement
		 */
		scope.locationInfo.location = "test";

		$httpBackend.expectPUT(basePath + '/user/1/point_of_interest/1', undefined, headers).respond(200, "ok");

		scope.register();
		$httpBackend.flush();

		expect($uibModalInstance.close).toHaveBeenCalled();
		expect(addressService.setAddress).toHaveBeenCalled();

	});

	/**
	 * Check if errors are handeled correctly
	 */
	it('Should correctly handle an error', function(){

		/**
		 * Set a location so we get in the if-statement
		 */
		scope.locationInfo.location = "test";

		$httpBackend.expectPUT(basePath + '/user/1/point_of_interest/1', undefined, headers).respond(154);
		scope.register();
		$httpBackend.flush();

		expect($uibModalInstance.close).not.toHaveBeenCalled();
		expect(addressService.setAddress).toHaveBeenCalled();
		expect(errorService.handlePointOfInterestError).toHaveBeenCalled();
		expect(scope.POIServerError).toBeTruthy();

    /**
     * Should give an unauthorized error and re-execute the function
     */
    $httpBackend.expectPUT(basePath + '/user/1/point_of_interest/1', undefined, headers).respond(401);
    $httpBackend.expectPUT(basePath + '/user/1/point_of_interest/1', undefined, headers).respond(154);
		scope.register();
		$httpBackend.flush();

    expect(sharedService.renewToken).toHaveBeenCalled();
		expect($uibModalInstance.close).not.toHaveBeenCalled();
		expect(addressService.setAddress).toHaveBeenCalled();
		expect(errorService.handlePointOfInterestError).toHaveBeenCalled();
		expect(scope.POIServerError).toBeTruthy();
	});

	/**
	 * Check if the right function is called when checking the address validity
	 */
	it('Should call the correct function when checking the address validity', function(){
		scope.isAddressValid("test");

		expect(addressService.validateAddress).toHaveBeenCalled();
	});



});
