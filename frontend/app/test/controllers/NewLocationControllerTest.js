fdescribe('Test NewLocationController', function() {
  beforeEach(module('App'));

  var controller, modal, scope, sharedService, locationService, errorService, user, location, modalInstance, basePath;

  beforeEach(inject(function(
    $rootScope,
    $controller,
    $routeParams,
    $injector,
    _$q_,
    _$httpBackend_,
    _$filter_,
    _$uibModal_,
    _locationService_,
    _sharedService_,
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
    locationService = _locationService_;
    errorService = _errorService_;
    addressService = _addressService_;
    localStorageService = _localStorageService_;
    modal = _$uibModal_;
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
     * create the user id to be able to test the location post method
     */
    user = {
      id: "1"
    };
    location = {
      id: "1"
    };

    /**
     * initialize travelInfo for testing setStartAddress and setEndAddress
     */
    scope.travelInfo = {};

    /**
     * Create the necassery spies
     */
    spyOn(sharedService, 'createPOI').and.callThrough();
    spyOn(sharedService, 'renewToken').and.callThrough();
    spyOn(addressService, 'validateAddress').and.callThrough();
    spyOn(errorService, 'handlePointOfInterestError').and.callThrough();
    spyOn(uibModalInstance, 'close').and.callThrough();
    spyOn(uibModalInstance, 'dismiss').and.callThrough();
    spyOn(localStorageService, 'get').and.callFake(function(){
      return "test";
    });

    /**
     * Create the controller we need to test
     */
    controller = $controller('NewLocationController', {
      '$scope': scope,
      '$uibModalInstance': uibModalInstance,
      'user': user,
      'locationService': locationService,
      'sharedService': sharedService,
      'addressService': addressService,
      'errorService': errorService,
      'localStorageService': localStorageService
    });

    spyOn(scope, 'register').and.callThrough();
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
    expect(locationService).toBeDefined();
    expect(modal).toBeDefined();
  });

  /**
   * Check if the user and location id are really set to be 1. Otherwise some tests will fail
   */
  it('should set userId and travelId', function() {
    expect(user.id).toBe("1");
    expect(location.id).toBe("1");
  });

  /**
   * Test if default parameters are initialized
   */
  it('should have set the default parameters on init', function() {
    expect(scope.locationInfo).toBeDefined();
    expect(scope.locationInfo.radius).toEqual(1000);
  });

  it('should call the correct functions on registration and post location', function() {
    /**
     * We expect that the location will be posted to the server with the following URL
     * and the correct variables are filled in.
     */
    $httpBackend.expect('POST', basePath + '/user/1/point_of_interest').respond(200, location);

    scope.register();

    /**
     * Flush and process all the requests given to the backend
     */
    $httpBackend.flush();

    /**
     * If the response code of the server is 200, we successfully posted a point of interest
     * and we need to check if the modal is closed.
     */
    expect(uibModalInstance.close).toHaveBeenCalled();

    /**
     * Check if the correct functions are called
     */
    expect(sharedService.createPOI).toHaveBeenCalled();
  });

  /**
   * Test all errors for posting a location
   */
  it('Should handle connection refused error code correctly for posting a point of interest', function() {
    /**
     * Return a status code of -1
     */
    $httpBackend.expect('POST', basePath + '/user/1/point_of_interest').respond(-1, " Connection refused");

    /**
     * Call the register function
     */
    scope.register();
    $httpBackend.flush();

    expect(errorService.handlePointOfInterestError).toHaveBeenCalled();
    expect(scope.connectionRefused).toBe(true);
  });

  it('Should handle a random error code correctly for posting a point of interest', function() {
    /**
     * Return a random status code
     */
    $httpBackend.expect('POST', basePath + '/user/1/point_of_interest').respond(125, {
      statusText: "Something went wrong"
    });

    /**
     * Call the register function
     */
    scope.register();
    $httpBackend.flush();
    expect(errorService.handlePointOfInterestError).toHaveBeenCalled();
    expect(scope.POIServerError).toBe(true);
  });

  it('Should handle a travel already exists error code correctly for posting a point of interest', function() {
    /**
     * Return a random status code
     */
    $httpBackend.expect('POST', basePath + '/user/1/point_of_interest').respond(404, {
      statusText: "Internal server error"
    });

    /**
     * Call the register function
     */
    scope.register();
    $httpBackend.flush();
    expect(errorService.handlePointOfInterestError).toHaveBeenCalled();
    expect(scope.POIUserNotExist).toBe(true);
  });

  it('Should handle an internal server error code correctly for posting a point of interest', function() {
    /**
     * Return a random status code
     */
    $httpBackend.expect('POST', basePath + '/user/1/point_of_interest').respond(409, {
      statusText: "Internal server error"
    });

    /**
     * Call the register function
     */
    scope.register();
    $httpBackend.flush();
    expect(errorService.handlePointOfInterestError).toHaveBeenCalled();
    expect(scope.POIAlreadyExists).toBe(true);
  });

  it('Should handle an unauthorised server error code correctly for posting a point of interest', function() {
    /**
     * Return a random status code
     */
    $httpBackend.expect('POST', basePath + '/user/1/point_of_interest').respond(401, {
      statusText: "Internal server error"
    });

    /**
     * Call the register function
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

  it('Should close the modal when the button is clicked', function(){
    scope.close();
    expect(uibModalInstance.dismiss).toHaveBeenCalledWith("Closed new location modal.");
  });
});
