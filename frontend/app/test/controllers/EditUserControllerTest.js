/*
 * Test for EditUserController.
 */
fdescribe('EditUserController', function() {
  beforeEach(module('App'));
  beforeEach(module('AppMock'));

  var scope,
  controller,
  $uibModalInstance,
  $route,
  $timeout,
  user,
  userService,
  sharedService,
  errorService,
  changeEmailService,
  changePasswordService,
  localStorageService,
  accessTokenService,
  httpBackend,
  basePath;

  /*
   * Before the testing, inject all elements that are needed to test the controller
   */
  beforeEach(inject(function($rootScope, $controller, _$route_, _$timeout_, _$httpBackend_,
    _userService_, _sharedService_, _errorService_, _changeEmailService_,
    _changePasswordService_, _localStorageService_, _accessTokenService_) {

    scope = $rootScope.$new();
    httpBackend = _$httpBackend_;
    basePath = "http://localhost:8080";
    /*
     * Spy on functions of uibModalInstance
     */
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
     $route = _$route_;
     $timeout = _$timeout_;

    headers = {
      'Authorization': 'access',
      'Accept': 'application/json, text/plain, */*',
      'Content-Type': 'application/json;charset=utf-8'
    };


    /*
     * Create a test user
     */
    user = {
      "id" : "1",
      "first_name": "test",
      "last_name": "test",
      "email": "test@test.com"
    };
    userService = _userService_;
    sharedService = _sharedService_;
    errorService = _errorService_;
    changeEmailService = _changeEmailService_;
    changePasswordService = _changePasswordService_;
    localStorageService = _localStorageService_;
    accessTokenService = _accessTokenService_;

    controller = $controller('EditUserController', {
      '$scope': scope,
      '$route': $route,
      '$uibModalInstance': $uibModalInstance,
      '$timeout': $timeout,
      'user': user,
      'sharedService': sharedService,
      'userService': userService,
      'errorService': errorService,
      'changeEmailService': changeEmailService,
      'changePasswordService': changePasswordService,
      'localStorageService': localStorageService,
      'accessTokenService': accessTokenService
    });

    scope.userAlreadyExists = false;

    spyOn($uibModalInstance, 'close').and.callThrough();
    spyOn($uibModalInstance, 'dismiss').and.callThrough();
    spyOn($route, 'reload').and.callThrough();
    spyOn(sharedService, 'renewToken').and.callThrough();
    spyOn(console, 'error').and.callThrough();

    httpBackend.whenGET('view/login/loginPage.html').respond(200);
    httpBackend.flush();

  }));

  /*
   * Check after each test if there are any pending requests left.
   */
  afterEach(function() {
    httpBackend.verifyNoOutstandingExpectation();
    httpBackend.verifyNoOutstandingRequest();
  });

  /*
   * Test whether everything has been defined.
   */
  it('should check if all arguments of controller are defined', function() {
    expect(scope).toBeDefined();
    expect(controller).toBeDefined();
    expect($uibModalInstance).toBeDefined();
    expect(user).toBeDefined();
    expect(userService).toBeDefined();
  });

  /*
   * Test if scope elements in controller have been defined
   */
  it('should check if all scope elements in EditUserController have been defined', function() {
    expect(scope.countries).toBeDefined();
    expect(scope.user).toBeDefined();
  });

  /**
   * Test if the function close() works correctly
   */
  it('Should check if the function close() works correctly', function() {
    scope.close();
    expect($uibModalInstance.dismiss).toHaveBeenCalled();
    expect($route.reload).toHaveBeenCalled();
  });

  /*
   * Test if the function opslaan1() works correctly
   */
  it('should test if the function opslaan1() works correctly', function() {
    /*
     * Wait for the request to be send.
     */
    httpBackend.expect('PUT', basePath + '/user/1', undefined, headers).respond(200, {"first_name": "", "last_name": ""});
    scope.opslaan1(scope.user);
    httpBackend.flush();

    expect(scope.oldUser).toBeDefined();
    expect(scope.successMessage1).toEqual("Voornaam/achternaam succesvol gewijzigd.");
    $timeout.flush();
    expect(scope.successMessage1).toEqual("");

    /**
     * Should give an unauthorized error and re-execute the function
     */
    httpBackend.expect('PUT', basePath + '/user/1', undefined, headers).respond(401);
    httpBackend.expect('PUT', basePath + '/user/1', undefined, headers).respond(200);
    scope.opslaan1(scope.user);
    httpBackend.flush();

    expect(sharedService.renewToken).toHaveBeenCalled();
    expect(scope.oldUser).toBeDefined();
    expect(scope.successMessage1).toEqual("Voornaam/achternaam succesvol gewijzigd.");
    $timeout.flush();
    expect(scope.successMessage1).toEqual("");

    /**
     * Should give an error
     */
    httpBackend.expect('PUT', basePath + '/user/1', undefined, headers).respond(-1);
    scope.opslaan1(scope.user);
    httpBackend.flush();

    expect(console.error).toHaveBeenCalled();

  });

  /*
   * Test if the function opslaan2() works correctly
   */
  it('should test if the function opslaan2() works correctly', function() {
    scope.oldEmail = "test@test2.com";
    scope.newEmail = "test@test.com";
    var emailBody = {
      'old_email': scope.oldEmail,
      'new_email': scope.newEmail
    };
    var header = {"Accept":"application/json, text/plain, */*","Content-Type":"application/json;charset=utf-8"};
    /*
     * Should give an unauthorized error and re-execute the function
     */
    httpBackend.expect('POST', basePath + '/user/1/change_email', emailBody, headers).respond(401);
    httpBackend.expect('POST', basePath + '/user/1/change_email', emailBody, headers).respond(200, emailBody);
    httpBackend.expect('POST', basePath + '/access_token', emailBody, header).respond(401);
    httpBackend.expect('POST', basePath + '/user/1/change_email', emailBody, headers).respond(200, emailBody);
    httpBackend.expect('POST', basePath + '/access_token', {'old_email': 'test@test2.com', 'new_email': 'test@test.com'}, header).respond(200);
    scope.opslaan2(scope.user);
    httpBackend.flush();

    expect(sharedService.renewToken).toHaveBeenCalled();

    expect(scope.oldEmail).toEqual(scope.newEmail);
    expect(scope.user.email).toEqual(scope.newEmail);
    expect(scope.userAlreadyExists).toEqual("");
    expect(scope.successMessage2).toEqual("E-mail succesvol gewijzigd.");
    $timeout.flush();
    expect(scope.successMessage2).toEqual("");

  });

  /*
   * Test if the function opslaan3() works correctly
   */
  it('should test if the function opslaan3() works correctly', function() {
    scope.oldPassword = "Test1234";
    scope.newPassword = "1234Test";
    var passwordBody = {
      'old_password': scope.oldPassword,
      'new_password': scope.newPassword
    };
    var header = {"Accept":"application/json, text/plain, */*","Content-Type":"application/json;charset=utf-8"};
    /*
     * Should give an unauthorized error and re-execute the function
     */
    httpBackend.expect('POST', basePath + '/user/1/change_password', passwordBody, header).respond(401);
    httpBackend.expect('POST', basePath + '/user/1/change_password', passwordBody, header).respond(200, {'token': 'refresh'});
    httpBackend.expect('POST', basePath + '/access_token', {'token': 'refresh'}, {"Authorization":"refresh","Accept":"application/json, text/plain, */*","Content-Type":"application/json;charset=utf-8"}).respond(401);
    httpBackend.expect('POST', basePath + '/user/1/change_password', passwordBody, header).respond(200, {'token': 'refresh'});
    httpBackend.expect('POST', basePath + '/access_token', {'token': 'refresh'}, {"Authorization":"refresh","Accept":"application/json, text/plain, */*","Content-Type":"application/json;charset=utf-8"}).respond(200);
    scope.opslaan3(scope.user);
    httpBackend.flush();

    expect(sharedService.renewToken).toHaveBeenCalled();

    expect(scope.oldPassword).toEqual("");
    expect(scope.newPassword).toEqual("");
    expect(scope.newPasswordAgain).toEqual("");
    expect(scope.successMessage3).toEqual("Wachtwoord succesvol gewijzigd.");
    $timeout.flush();
    expect(scope.successMessage3).toEqual("");

  });

  /**
   * Test if the function validatePassword() works correctly
   */
   it('Should check if the function validatePassword() works correctly', function() {
     expect(scope.validatePassword("Test1234")).toBeTruthy();
     expect(scope.validatePassword("Ki6j9KhKZe")).toBeTruthy();
     expect(scope.validatePassword("Test1")).toBeFalsy();
     expect(scope.validatePassword("testtesttest")).toBeFalsy();
     expect(scope.validatePassword("1234567890")).toBeFalsy();
   });

});
