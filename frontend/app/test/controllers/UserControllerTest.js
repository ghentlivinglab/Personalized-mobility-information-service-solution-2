fdescribe('Test UserController', function() {
  beforeEach(module('App'));

  var controller,
      scope,
      location,
      uibModalInstance,
      sharedService,
      registrationService,
      localStorageService,
      regularRefreshTokenService,
      accessTokenService,
      store,
      basePath,
      httpBackend;

  beforeEach(inject(function(
    $rootScope,
    $location,
    $controller,
    $httpBackend,
    _registrationService_,
    _sharedService_,
    _localStorageService_,
    _regularRefreshTokenService_,
    _accessTokenService_) {

      /**
       * Create the needed dependencies
       */
      scope = $rootScope.$new();
      location = $location;
      sharedService = _sharedService_;
      registrationService = _registrationService_;
      localStorageService = _localStorageService_;
      regularRefreshTokenService = _regularRefreshTokenService_;
      accessTokenService = _accessTokenService_;

      uibModalInstance = {
          open: {
              then: function(callback, errorCallback) {
                  callback("open");
                  errorCallback();
              }
          },
          dismiss: function() {
              return true;
          },
          close: function() {
              return true;
          }
      };

      store = {};
      basePath = "http://localhost:8080";
      httpBackend = $httpBackend;

      /**
       * Create the necassery spies
       */
      spyOn(localStorageService, 'clearAll').and.callFake(function() {
        store = {};
      });
      spyOn(localStorageService, 'set').and.callFake(function(key, value) {
        store[key] = value;
      });
      spyOn(uibModalInstance, 'open').and.callThrough();
      spyOn(uibModalInstance, 'dismiss').and.callThrough();
      spyOn(uibModalInstance, 'close').and.callThrough();
      spyOn(registrationService, 'getUser').and.callThrough();
      spyOn(registrationService, 'setUser').and.callThrough();
      spyOn(registrationService, 'clearRegistration').and.callThrough();
      spyOn(sharedService, 'setDefaultUserOptions').and.callThrough();
      spyOn(sharedService, 'postUser').and.callThrough();
      spyOn(location, 'path').and.callThrough();
      // spyOn(scope, 'registerUser');

      /**
       * Create the controller we need to test
       */
      controller = $controller('UserController', {
        $scope: scope,
        $location: location,
        $uibModalInstance: uibModalInstance,
        sharedService: sharedService,
        registrationService: registrationService,
        localStorageService: localStorageService,
        regularRefreshTokenService: regularRefreshTokenService,
        accessTokenService: accessTokenService
      });

  }));

  /**
   * Check if all the needed parameters for the test are defined
   */
  it('should have defined all parameters', function() {
    expect(scope).toBeDefined();
    expect(location).toBeDefined();
    expect(uibModalInstance).toBeDefined();
    expect(sharedService).toBeDefined();
    expect(registrationService).toBeDefined();
    expect(localStorageService).toBeDefined();
    expect(regularRefreshTokenService).toBeDefined();
    expect(accessTokenService).toBeDefined();
    expect(controller).toBeDefined();
  });

  /**
   * Test if the function initData works correctly
   */
  it('should check if the function initData works correctly', function() {
    /**
     * We set a dummy user to check if we go into the if condition
     */
    registrationService.setUser({"id": "1"});
    scope.initData();
    expect(registrationService.getUser).toHaveBeenCalled();
    expect(scope.user.id).toBe("1");

    /**
     * We set the user to undefined to check if we go into the else condition
     */
    registrationService.setUser(undefined);
    scope.initData();
    expect(sharedService.setDefaultUserOptions).toHaveBeenCalled();
    expect(registrationService.setUser).toHaveBeenCalled();
  });

  /**
   * Test if the function validatePassword works correctly
   */
  it('should check if the function validatePassword works correctly', function() {
    var passw = "";
    expect(scope.validatePassword(passw)).toBeFalsy();
    passw = "test";
    expect(scope.validatePassword(passw)).toBeFalsy();
    passw = "test123";
    expect(scope.validatePassword(passw)).toBeFalsy();
    passw = "testje123";
    expect(scope.validatePassword(passw)).toBeFalsy();
    passw = "tesTje123";
    expect(scope.validatePassword(passw)).toBeTruthy();
  });

  it('should dismiss the modal', function() {
    scope.close();
    expect(uibModalInstance.dismiss).toHaveBeenCalled();
    expect(registrationService.clearRegistration).toHaveBeenCalled();
    expect(localStorageService.clearAll).toHaveBeenCalled();
  });

  it('should register the user correctly', function() {
    scope.user = {
      email: "test@gmail.com",
      password: "test"
    };
    scope.register();

    expect(registrationService.setUser).toHaveBeenCalled();
    expect(localStorageService.set).toHaveBeenCalled();
    expect(scope.token).toEqual({email: "test@gmail.com", password: "test"});
    expect(sharedService.postUser).toHaveBeenCalled();
    // expect(scope.registerUser).toHaveBeenCalled();
  });

  it('should registerUser', function() {
    scope.token = "token";

    httpBackend.expectPOST(basePath + '/refresh_token/regular').respond(200, {token: "refreshtoken"});
    httpBackend.expectPOST(basePath + '/access_token').respond(200, {token: "accesstoken"});

    scope.registerUser();
    scope.result = true;
    scope.$digest();
    httpBackend.flush();

    expect(localStorageService.set).toHaveBeenCalled();
    expect(localStorageService.set).toHaveBeenCalled();
    expect(uibModalInstance.close).toHaveBeenCalled();
    expect(location.path).toHaveBeenCalledWith('/registration');
    scope.$apply();
    expect(location.path()).toBe('/registration');
  });

  it('should fail getting the refreshtoken in registerUser', function() {
    scope.token = "token";

    httpBackend.expectPOST(basePath + '/refresh_token/regular').respond(400);

    scope.registerUser();
    scope.result = true;
    scope.$digest();
    httpBackend.flush();

    expect(localStorageService.set).not.toHaveBeenCalled();
  });


  it('should fail getting the accesstoken in registerUser', function() {
    scope.token = "token";

    httpBackend.expectPOST(basePath + '/refresh_token/regular').respond(200, {token: "refreshtoken"});
    httpBackend.expectPOST(basePath + '/access_token').respond(400);

    scope.registerUser();
    scope.result = true;
    scope.$digest();
    httpBackend.flush();

    expect(localStorageService.set).toHaveBeenCalled();
  });
});
