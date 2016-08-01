fdescribe('EventControlServiceTest', function() {
  beforeEach(module('App'));
  beforeEach(module('AppMock'));

  var scope,
  controller,
  $uibModal,
  $route,
  eventServiceMock,
  sharedServiceMock,
  addressServiceMock,
  localStorageService,
  eventControlService;

  /**
   * Before the testing, inject all elements that are needed to test the service
   */
  beforeEach(inject(function($rootScope, $controller, _$route_, _$uibModal_,
    _eventService_, _localStorageService_, _$httpBackend_, _eventControlService_, _sharedService_, _addressService_) {

    httpBackend = _$httpBackend_;
    basePath = "http://localhost:8080";
    scope = $rootScope.$new();

    eventServiceMock = _eventService_;
    sharedServiceMock = _sharedService_;
    addressServiceMock = _addressService_;

    $route = _$route_;
    $uibModal = _$uibModal_;

    localStorageService = _localStorageService_;
    eventControlService = _eventControlService_;

    headers = {
      'Authorization': 'access',
      'Accept': 'application/json, text/plain, */*'
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
    spyOn(console, 'error').and.callThrough();
    spyOn($route, 'reload').and.callThrough();
    spyOn(sharedServiceMock, 'renewToken').and.callThrough();
    spyOn(sharedServiceMock, 'loadData').and.callThrough();
    spyOn(addressServiceMock, 'initMap').and.callFake(function(mapId) {});
    /**
     * The first callback is the success callback. You can return whatever you want to the calling controller
     * The second is the error callback where you can also specify a return value but not have to
     */
    spyOn($uibModal, 'open').and.callFake(function() {
        return {
            result: {
                then: function(callback, errorCallback) {
                    callback("item1");
                    errorCallback();
                }
            },
            rendered: {
                then: function(callback) {
                    callback();
                }
            }
        };
    });

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

  /**
   * Test whether everything has been defined.
   */
  it('should check if all parameters are defined', function() {
    expect(scope).toBeDefined();
    expect($route).toBeDefined();
    expect($uibModal).toBeDefined();
    expect(eventServiceMock).toBeDefined();
    expect(sharedServiceMock).toBeDefined();
    expect(addressServiceMock).toBeDefined();
    expect(localStorageService).toBeDefined();
    expect(eventControlService).toBeDefined();
  });

  /**
   * Test if the function openNewEvent works correctly.
   */
  it('should check if the function openNewEvent works correctly', function() {
    /**
     * Overwrite the spy.
     */
    $uibModal.open.and.callFake(function(){
      return {
          result: {
              then: function(callback, errorCallback) {
                  callback();
              }
          },
          rendered: {
              then: function(callback) {
                  callback();
              }
          }
      };
    });

    eventControlService.openNewEvent();
    expect($uibModal.open).toHaveBeenCalled();
    expect($route.reload).toHaveBeenCalled();

    /**
     * Overwrite the spy and only return the error. So the console.error() funtion would be called
     */
    $uibModal.open.and.callFake(function(){
      return {
          result: {
              then: function(callback, errorCallback) {
                  errorCallback();
              }
          },
          rendered: {
              then: function() {}
          }
      };
    });
    eventControlService.openNewEvent();
    expect(console.error).toHaveBeenCalled();

  });

  /**
   * Test if the function openEditEvent works correctly.
   */
  it('should check if the function openEditEvent works correctly', function() {
    /**
     * Overwrite the spy.
     */
    $uibModal.open.and.callFake(function(){
      return {
          result: {
              then: function(callback, errorCallback) {
                  callback();
              }
          },
          rendered: {
              then: function(callback) {
                  callback();
              }
          }
      };
    });

    eventControlService.openEditEvent();
    expect($uibModal.open).toHaveBeenCalled();
    expect($route.reload).toHaveBeenCalled();

    /**
     * Overwrite the spy and only return the error. So the console.error() funtion would be called
     */
    $uibModal.open.and.callFake(function(){
      return {
          result: {
              then: function(callback, errorCallback) {
                  errorCallback();
              }
          },
          rendered: {
              then: function() {}
          }
      };
    });
    eventControlService.openEditEvent();
    expect(console.error).toHaveBeenCalled();

  });

  /**
   * Test if function deleteEvent() has been called.
   */
  it('should check if function deleteEvent() has been called', function() {
    httpBackend.expect('DELETE', basePath + '/event/1', undefined, headers).respond(200);
    eventControlService.deleteEvent("1");
    httpBackend.flush();
    expect($route.reload).toHaveBeenCalled();

    /**
     * Should give an unauthorized error and re-execute the function
     */
    httpBackend.expect('DELETE', basePath + '/event/1').respond(401);
    httpBackend.expect('DELETE', basePath + '/event/1', undefined, headers).respond(200);
    eventControlService.deleteEvent("1");
    httpBackend.flush();
    expect(sharedServiceMock.renewToken).toHaveBeenCalled();
    expect($route.reload).toHaveBeenCalled();

    /**
     * Should give an error
     */
    httpBackend.expect('DELETE', basePath + '/event/1').respond(-1);
    eventControlService.deleteEvent("1");
    httpBackend.flush();
    expect(console.error).toHaveBeenCalled();

  });

});
