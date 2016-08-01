fdescribe("ErrorService test", function() {
  beforeEach(module('App'));
  beforeEach(inject(function(
    $rootScope,
    _errorService_
  ) {
    scope = $rootScope.$new();
    errorService = _errorService_;
  }));


  it('Should set the correct error messages when getting error in login', function() {
    errorService.handleLoginError({
      status: 404
    }, scope);
    expect(scope.error).toEqual("Het opgegeven e-mailadres bestaat niet");

    errorService.handleLoginError({
      status: 401
    }, scope);
    expect(scope.error).toEqual("Het opgegeven wachtwoord is incorrect");

    errorService.handleLoginError({
      status: 1
    }, scope);
    expect(scope.error).toEqual("Inloggegevens zijn incorrect");
  });

  it('Should set the correct error messages when getting error in PointOfInterest', function() {
    errorService.handlePointOfInterestError({
      status: -1
    }, scope);
    expect(scope.connectionRefused).toBeTruthy();

    errorService.handlePointOfInterestError({
      status: 404
    }, scope);
    expect(scope.POIUserNotExist).toBeTruthy();

    errorService.handlePointOfInterestError({
      status: 409
    }, scope);
    expect(scope.POIAlreadyExists).toBeTruthy();

    errorService.handlePointOfInterestError({
      status: 500
    }, scope);
    expect(scope.POIServerError).toBeTruthy();
  });

  it('Should set the correct error messages when getting error in TravelPost', function() {
    errorService.handleTravelError({
      status: -1
    }, scope);
    expect(scope.connectionRefused).toBeTruthy();

    errorService.handleTravelError({
      status: 404
    }, scope);
    expect(scope.travelAlreadyExists).toBeTruthy();


    errorService.handleTravelError({
      status: 500
    }, scope);
    expect(scope.travelServerError).toBeTruthy();

    errorService.handleTravelError({
      status: 423
    }, scope);
    expect(scope.travelServerError).toBeTruthy();
  });

  it('Should set the correct error messages when getting error in RoutePost', function() {
    errorService.handleRouteError({
      status: -1
    }, scope);
    expect(scope.connectionRefused).toBeTruthy();

    errorService.handleRouteError({
      status: 401
    }, scope);
    expect(scope.userLoginError).toBeTruthy();

    errorService.handleRouteError({
      status: 403
    }, scope);
    expect(scope.userPrivilegeError).toBeTruthy();

    errorService.handleRouteError({
      status: 404
    }, scope);
    expect(scope.resourceNotExisting).toBeTruthy();


    errorService.handleRouteError({
      status: 409
    }, scope);
    expect(scope.routeAlreadyExists).toBeTruthy();

    errorService.handleRouteError({
      status: 423
    }, scope);
    expect(scope.routeServerError).toBeTruthy();
  });

  it('Should set the correct error messages when getting error in User connection', function() {
    errorService.handleUserError({
      status: -1
    }, scope);
    expect(scope.connectionRefused).toBeTruthy();

    errorService.handleUserError({
      status: 401
    }, scope);
    expect(scope.userNotLoggedIn).toBeTruthy();

    errorService.handleUserError({
      status: 403
    }, scope);
    expect(scope.userNotAdmin).toBeTruthy();

    errorService.handleUserError({
      status: 404
    }, scope);
    expect(scope.userDoesNotExist).toBeTruthy();


    errorService.handleUserError({
      status: 409
    }, scope);
    expect(scope.userAlreadyExists).toBeTruthy();

    errorService.handleUserError({
      status: 423
    }, scope);
    expect(scope.userServerError).toBeTruthy();
  });

  it('Should set the correct error messages when getting error in Event', function() {
    errorService.handleEventError({
      status: 401
    }, scope);
    expect(scope.usernotLoggdIn).toBeTruthy();

    errorService.handleEventError({
      status: 403
    }, scope);
    expect(scope.userNotAuthorised).toBeTruthy();

  });

  it('Should set the correct error messages when getting error in ChangePassword', function() {
    errorService.handleChangePasswordError({
      status: 401
    }, scope);
    expect(scope.usernotLoggdIn).toBeTruthy();

    errorService.handleChangePasswordError({
      status: 403
    }, scope);
    expect(scope.wrongOldPwd).toBeTruthy();

    errorService.handleChangePasswordError({
      status: 404
    }, scope);
    expect(scope.userDoesNotExist).toBeTruthy();

    errorService.handleChangePasswordError({
      status: 123
    }, scope);
    expect(scope.userServerError).toBeTruthy();

  });
});
