/*
 * Test for EditTravelController
 */
fdescribe('Test ValidationReminderController', function() {
  beforeEach(module('App'));

  var controller,
      scope,
      uibModalInstance,
      location,
      user,
      reverifyMailService,
      localStorageService,
      store,
      httpBackend,
      basePath;

  beforeEach(inject(function(
    $controller,
    $rootScope,
    $location,
    $httpBackend,
    _reverifyMailService_) {

    scope = $rootScope.$new();
    httpBackend = $httpBackend;
    basePath = "http://localhost:8080";
    location = $location;
    reverifyMailService = _reverifyMailService_;
    localStorageService = {
      get: function(key) {
        return store[key];
      }
    };

    user = {
      id: 1,
      email: "test@gmail.com"
    };

    uibModalInstance = {
      close: function() {
        return true;
      }
    };

    /**
     * Spy on the following functions.
     */
    spyOn(uibModalInstance, 'close').and.callThrough();

    /**
     * Initialize the controller to test it.
     */
    store = { "accessToken": "token"};

    controller = $controller('ValidationReminderController', {
      $scope: scope,
      $location: location,
      $uibModalInstance: uibModalInstance,
      user: user,
      reverifyMailService: reverifyMailService,
      localStorageService: localStorageService
    });
  }));

  it('should have defined all parameters', function() {
    expect(scope).toBeDefined();
    expect(location).toBeDefined();
    expect(uibModalInstance).toBeDefined();
    expect(user).toBeDefined();
    expect(reverifyMailService).toBeDefined();
    expect(localStorageService).toBeDefined();
  });

  it('should close the modal', function() {
    scope.close();
    expect(uibModalInstance.close).toHaveBeenCalled();
  });

  it('should close the modal when resending the mail succeeds', function() {
    httpBackend.expectPOST(basePath + '/user/1/reverify_email').respond(200);

    scope.resendMail();
    httpBackend.flush();

    expect(uibModalInstance.close).toHaveBeenCalled();
    expect(scope.reverifyMailWentWrong).toBe(undefined);
  });

  it('should show the error message when resending the mail fails', function() {
    httpBackend.expectPOST(basePath + '/user/1/reverify_email').respond(400);

    scope.resendMail();
    httpBackend.flush();

    expect(scope.reverifyMailWentWrong).toBe(true);
  });

});
