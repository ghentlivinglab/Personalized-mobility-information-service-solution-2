fdescribe('Test ValidationController', function() {
  beforeEach(module('App'));

  var scope,
      controller,
      location,
      verifyService,
      localStorageService,
      httpBackend,
      store,
      basePath;

  /*
   * Instantiate the controller with a mock of the userService and spy on the query-method.
   */
  beforeEach(inject(function(
    $rootScope,
    $controller,
    $location,
    _verifyService_,
    _$httpBackend_) {

    scope = $rootScope.$new();
    location = $location;
    httpBackend = _$httpBackend_;
    verifyService = _verifyService_;
    localStorageService = {
      get: function(key) {
        if (key in store) {
          return store[key];
        } else {
          return null;
        }
      }
    };

    store = { "refreshToken" : "token"};
    basePath = "http://localhost:8080";

    spyOn(localStorageService, 'get').and.callThrough();
    spyOn(location, 'search').and.callThrough();
    spyOn(location, 'path').and.callThrough();
    spyOn(location, 'hash').and.callThrough();

    controller = $controller('ValidationController', {
      $scope: scope,
      $location: location,
      verifyService: verifyService,
      localStorageService: localStorageService
    });

  }));

  it('should have defined all paramters', function() {
    expect(scope).toBeDefined();
    expect(location).toBeDefined();
    expect(verifyService).toBeDefined();
    expect(localStorageService).toBeDefined();
    expect(httpBackend).toBeDefined();
  });

  it('should log the user in', function() {
    location.url(basePath + "/?user=1&pin=1");

    httpBackend.whenPOST(basePath + "/user/1/verify").respond(200);
    scope.validation();
    httpBackend.flush();

    scope.$apply();
    expect(location.search).toHaveBeenCalled();
    expect(location.path).toHaveBeenCalled();
    expect(location.path()).toBe("/index");

    store = {};
  });

  it('should hash the path', function() {
    location.url(basePath + "/?user=1&pin=1");
    store = {};

    httpBackend.whenPOST(basePath + "/user/1/verify").respond(200);
    scope.validation();
    httpBackend.flush();

    expect(location.search).toHaveBeenCalled();
    expect(location.path).toHaveBeenCalled();
    expect(location.path()).toBe("/index");
    expect(location.hash).toHaveBeenCalled();
  });

});
