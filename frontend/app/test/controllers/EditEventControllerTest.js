fdescribe("EditEventController", function() {
  beforeEach(module('App'));

  beforeEach(inject(function(
    $rootScope,
    _$q_,
    $controller,
    _$httpBackend_,
    _$uibModal_,
    _$timeout_,
    _addressService_,
    _sharedService_,
    _mapsService_,
    _eventService_,
    _errorService_,
    _localStorageService_) {

    basePath = "http://localhost:8080";
    event = {
      type: {
        type: 'JAM'
      },
      id: 1,
      jams: [{
        line: [{
          test: "test"
        }, {
          test: "test"
        }]
      }]
    };

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

    q = _$q_;
    mapsService = _mapsService_;
    scope = $rootScope.$new();
    $httpBackend = _$httpBackend_;
    sharedService = _sharedService_;
    addressService = _addressService_;
    localStorageService = _localStorageService_;
    errorService = _errorService_;

    spyOn(mapsService, 'getLocation').and.callFake(function() {


      data = [{
        data: {
          results: [
            "test"
          ]
        }
      }, {
        data: {
          results: [
            "Iets"
          ]
        }
      }];

      return {
        then: function(callback, errorCallback) {
          callback(data);
        }
      };

    });

    spyOn(sharedService, 'getTypes').and.callFake(function() {
      return "test";
    });
    spyOn(addressService, 'watchEventAddressMarker').and.callThrough();
    spyOn(uibModalInstance, 'dismiss').and.callThrough();
    spyOn(localStorageService, 'get').and.callFake(function() {
      return {
        token: "test"
      };
    });

    controller = $controller('EditEventController', {
      $scope: scope,
      $uibModalInstance: uibModalInstance,
      $timeout: _$timeout_,
      event: event,
      addressService: addressService,
      sharedService: sharedService,
      mapsService: mapsService,
      eventService: _eventService_,
      errorService: errorService,
      localStorageService: localStorageService
    });

    spyOn(scope, 'initializeMap').and.callThrough();
    spyOn(uibModalInstance, 'close').and.callThrough();
    spyOn(scope, 'register').and.callThrough();
    spyOn(sharedService, 'renewToken').and.callFake(function() {
      return "test";
    });
    spyOn(errorService, 'handleEventError').and.callFake(function(error, scope) {
      return;
    });
    spyOn(addressService, 'validateAddress').and.callFake(function(){
      return;
    });
  }));

  it('Should call initializeMap when controller is instantiated ', function() {
    expect(scope.eventTypes).toEqual("test");
    expect(scope.markerdrag).toEqual(false);
    expect(scope.jamdrag).toEqual(false);
    expect(scope.event).toEqual(event);
    expect(addressService.watchEventAddressMarker).toHaveBeenCalled();
  });

  it('Should set the correct locationinfo when opening modal (eventType not equal to jam and equal to jam)', inject(function($controller, _$timeout_, _addressService_, _eventService_, _errorService_, _localStorageService_) {
    event = {
      type: {
        type: 'ONGEVAL'
      },
      jams: [{
        line: [{
          test: "test"
        }, {
          test: "test"
        }]
      }]
    };

    controller = $controller('EditEventController', {
      $scope: scope,
      $uibModalInstance: uibModalInstance,
      $timeout: _$timeout_,
      event: event,
      addressService: _addressService_,
      sharedService: sharedService,
      mapsService: mapsService,
      eventService: _eventService_,
      errorService: _errorService_,
      localStorageService: _localStorageService_
    });


    expect(scope.locationInfo).toEqual("Iets");
    expect(scope.locationInfo2).toEqual("test");

    event = {
      type: {
        type: 'ONGEVAL'
      },
      jams: []
    };

    controller = $controller('EditEventController', {
      $scope: scope,
      $uibModalInstance: uibModalInstance,
      $timeout: _$timeout_,
      event: event,
      addressService: _addressService_,
      sharedService: sharedService,
      mapsService: mapsService,
      eventService: _eventService_,
      errorService: _errorService_,
      localStorageService: _localStorageService_
    });
    expect(scope.locationInfo).toEqual("test");
  }));

  it('Should correctly initialize a map', function() {
    scope.initializeMap();
    expect(scope.map).toEqual({
      test: "test"
    });
    expect(scope.marker).toBeDefined();
    expect(scope.directionsService).toBeDefined();
    expect(scope.directionsDisplay).toBeDefined();
  });

  it('Should call the dismiss function of the modal instance when closing modal', function() {
    scope.close();
    expect(uibModalInstance.dismiss).toHaveBeenCalled();
  });

  it('Should correctly register the edited event and parse errors', function() {

    scope.directionsDisplay = {
      directions: {
        routes: [{
          overview_path: [
            {
                lat: function() {
                  return 0.0;
                },
                lng: function() {
                  return 0.0;
                }
            }, {
                lat: function() {
                  return 0.0;
                },
                lng: function() {
                  return 0.0;
                }
            }
          ],
          legs: [{
            start_location: {
              lat: function() {
                return 0.0;
              },
              lng: function() {
                return 0.0;
              }
            },
            end_location: {
              lat: function() {
                return 0.0;
              },
              lng: function() {
                return 0.0;
              }
            }
          }]
        }]
      }
    };
    var response = $httpBackend.whenPUT(basePath + '/event/1');
    response.respond(200);
    scope.register();
    $httpBackend.flush();
    expect(uibModalInstance.close).toHaveBeenCalled();
    expect(scope.event.publication_time).toBeDefined();
    expect(scope.event.last_edit_time).toBeDefined();
    expect(scope.event.relevant_for_transportation_types).toEqual([]);
    expect(scope.event.coordinates).toBeDefined();


    response.respond(500);
    scope.register();

    $httpBackend.flush();

    expect(errorService.handleEventError).toHaveBeenCalled();

    response.respond(401);
    scope.register();
    scope.register.and.callFake(function() {
      return;
    });
    $httpBackend.flush();

    expect(sharedService.renewToken).toHaveBeenCalled();
    expect(scope.register).toHaveBeenCalled();

    scope.event.type.type = "ONGEVAL";
    scope.$apply();
    scope.register();

  });

  it('Should correctly register the edited event when it is not a jam', function() {

    scope.directionsDisplay = {
      directions: {
        routes: [{
          overview_path: [
            {
                lat: function() {
                  return 0.0;
                },
                lng: function() {
                  return 0.0;
                }
            }, {
                lat: function() {
                  return 0.0;
                },
                lng: function() {
                  return 0.0;
                }
            }
          ],
          legs: [{
            start_location: {
              lat: function() {
                return 0.0;
              },
              lng: function() {
                return 0.0;
              }
            },
            end_location: {
              lat: function() {
                return 0.0;
              },
              lng: function() {
                return 0.0;
              }
            }
          }]
        }]
      }
    };
    scope.event.type.type = "ONGEVAL";
    var response = $httpBackend.whenPUT(basePath + '/event/1');
    response.respond(200);
    scope.register();
    $httpBackend.flush();
    expect(uibModalInstance.close).toHaveBeenCalled();
    expect(scope.event.publication_time).toBeDefined();
    expect(scope.event.last_edit_time).toBeDefined();
    expect(scope.event.relevant_for_transportation_types).toEqual([]);
    expect(scope.event.coordinates).toBeDefined();

  });

  it('Should call the addressValid function checking address validity', function(){
    scope.isAddressValid("Galglaan");
    expect(addressService.validateAddress).toHaveBeenCalled();
  });

  it('Should correctly validate the form', function(){
    scope.event = event;
    scope.locationInfo2 = "test";
    expect(scope.checkJamRoadClosed()).toBeTruthy();


    scope.locationInfo2 = undefined;
    expect(scope.checkJamRoadClosed()).not.toBeTruthy();

    scope.event.type.type = "ROAD_CLOSED";

    scope.locationInfo2 = "test";
    expect(scope.checkJamRoadClosed()).toBeTruthy();


    scope.locationInfo2 = undefined;
    expect(scope.checkJamRoadClosed()).not.toBeTruthy();

    scope.event.type.type = "DANGER";

    scope.locationInfo2 = "test";
    expect(scope.checkJamRoadClosed()).toBeTruthy();


    scope.locationInfo2 = undefined;
    expect(scope.checkJamRoadClosed()).toBeTruthy();
  });
});
