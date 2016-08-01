fdescribe('Test LoginController', function() {
  beforeEach(module('App'));
  beforeEach(module('AppMock'));

  var controller,
      scope,
      $location,
      $uibModalInstance,
      regularRefreshTokenService,
      accessTokenService,
      localStorageService,
      passwordService,
      errorService,
      store,
      basePath,
      httpBackend;

  beforeEach(inject(function(
    $rootScope,
    $controller,
    $httpBackend,
    _$location_,
    _regularRefreshTokenService_,
    _accessTokenService_,
    _localStorageService_,
    _passwordService_,
    _errorService_) {

      /**
       * Create the needed dependencies
       */
      scope = $rootScope.$new();
      $location = _$location_;
      regularRefreshTokenService = _regularRefreshTokenService_;
      accessTokenService = _accessTokenService_;
      localStorageService = _localStorageService_;
      passwordService = _passwordService_;
      errorService = _errorService_;


      $uibModalInstance = {
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
      spyOn($uibModalInstance, 'dismiss').and.callThrough();
      spyOn($uibModalInstance, 'close').and.callThrough();
      spyOn(errorService, 'handleLoginError').and.callThrough();
      spyOn($location, 'path').and.callThrough();
      spyOn(console, 'error').and.callThrough();

      /**
       * Create the controller we need to test
       */
      controller = $controller('LoginController', {
        $scope: scope,
        $location: $location,
        $uibModalInstance: $uibModalInstance,
        regularRefreshTokenService: regularRefreshTokenService,
        accessTokenService: accessTokenService,
        localStorageService: localStorageService,
        passwordService: passwordService,
        errorService: errorService
      });

    }));

    /*
     * Check after each test if there are any pending requests left.
     */
    afterEach(function() {
      httpBackend.verifyNoOutstandingExpectation();
      httpBackend.verifyNoOutstandingRequest();
    });

    /**
     * Test if the function close() works correctly
     */
    it('Should check if the function close() works correctly', function() {
      scope.close();
      expect($uibModalInstance.dismiss).toHaveBeenCalled();
    });

    /**
     * Test if the function login() works correctly
     */
    it('Should check if the function login() works correctly', function() {
      httpBackend.expect('POST', basePath + '/refresh_token/regular').respond(200);
      httpBackend.expect('POST', basePath + '/access_token').respond(200);
      scope.login();
      httpBackend.flush();
      expect($uibModalInstance.close).toHaveBeenCalled();

      /**
       * Should give a handleLoginError
       */
      httpBackend.expect('POST', basePath + '/refresh_token/regular').respond(-1);
      scope.login();
      httpBackend.flush();
      expect(console.error).toHaveBeenCalled();
      expect(errorService.handleLoginError).toHaveBeenCalled();

      /**
       * Should give a handleLoginError
       */
      httpBackend.expect('POST', basePath + '/refresh_token/regular').respond(200);
      httpBackend.expect('POST', basePath + '/access_token').respond(-1);
      scope.login();
      httpBackend.flush();
      expect(console.error).toHaveBeenCalled();
      expect(errorService.handleLoginError).toHaveBeenCalled();
    });

    /**
     * Test if the function sendPwd() works correctly
     */
    it('Should check if the function sendPwd() works correctly', function() {
      httpBackend.expect('POST', basePath + '/user/forgot_password').respond(200);
      scope.sendPwd();
      httpBackend.flush();
      expect(scope.pwdSent).toBeTruthy();
    });

  });
