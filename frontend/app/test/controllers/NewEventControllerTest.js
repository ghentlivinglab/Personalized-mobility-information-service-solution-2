fdescribe('NewEventController', function() {
  beforeEach(module('App'));

  beforeEach(inject(function(
    $rootScope,
    $controller,
    _$httpBackend_,
    _$filter_,
    _$timeout_,
    _sharedService_,
    _mapsService_,
    _localStorageService_,
    _eventService_,
    _errorService_,
    _addressService_) {

    /**
     * Set the variables to be used in the tests
     */
    basePath = "http://localhost:8080";

    $filter = _$filter_;
    $timeout = _$timeout_;
    $httpBackend = _$httpBackend_;
    scope = $rootScope.$new();
    sharedService = _sharedService_;
    eventService = _eventService_;
    mapsService = _mapsService_;
    localStorageService = _localStorageService_;
    errorService = _errorService_;
    addressService = _addressService_;

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

    scope.event = {
      type: {
        type: "JAM"
      },
      description: "Dit is een test"
    };


    /**
     * Initialize the controller to test it.
     */
    controller = $controller('NewEventController', {
      $scope: scope,
      $uibModalInstance: uibModalInstance,
      $timeout: $timeout,
      addressService: addressService,
      sharedService: sharedService,
      eventService: eventService,
      mapsService: mapsService,
      errorService: errorService,
      localStorageService: localStorageService
    });

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
    spyOn(sharedService, 'getTypes').and.callThrough();
    spyOn(uibModalInstance, 'close').and.callThrough();
    spyOn(uibModalInstance, 'dismiss').and.callThrough();
    spyOn(errorService, 'handleEventError').and.callThrough();
    spyOn(sharedService, 'renewToken').and.callThrough();
    spyOn(scope, 'register').and.callThrough();
    spyOn(addressService, 'validateAddress').and.callFake(function() {});
    spyOn(mapsService, 'getLocation').and.callFake(function() {

      var data = [{
        data: {
          results: [{
            formatted_address: "Test"
          }]
        }
      }];

      return {
        then: function(callback, errorCallback) {
          callback(data);
        }
      };
    });
    spyOn(localStorageService, 'get').and.callFake(function() {
      return "test";
    });

  }));

  /**
   * Check if the eventTypes are retreived from the sharedService
   */
  it('Sould correctly set the eventTypes', function() {

    var check = {
      "ACCIDENT": "Ongeval",
      "JAM": "File",
      "WEATHERHAZARD": "Gevaar",
      "HAZARD": "Gevaar op de weg",
      "MISC": "Ander Type",
      "CONSTRUCTION": "Wegenwerken",
      "ROAD_CLOSED": "Weg afgesloten"
    };

    expect(scope.eventTypes).toEqual(check);
  });

  /**
   * Check if a successful post is handled correctly
   */
  it('Should correctly register a new event', function() {

    /**
     * Simulate a successful post
     */
    $httpBackend.expectPOST(basePath + '/event').respond(200, "ok");

    scope.register();

    /*expect(scope.event.coordinates).toEqual({
      lat: 51.1235648,
      lon: 3.15647
    });*/
    expect(scope.event.relevant_for_transportation_types).toEqual([]);
    expect(scope.event.active).toBeTruthy();
    expect(scope.event.publication_time).toBeDefined();
    expect(scope.event.last_edit_time).toBeDefined();
    expect(scope.event.active).toBeTruthy();
    expect(scope.event.formatted_address).toEqual("Test");
    $httpBackend.flush();
    expect(uibModalInstance.close).toHaveBeenCalled();

  });

  it('Should correctly handle a server error code (user not logged in )', function() {
    /**
     * Simulate an error (user not logged in )
     */
    $httpBackend.expectPOST(basePath + '/event').respond(401);

    scope.register();
    scope.register.and.callFake(function() {
      return;
    });
    $httpBackend.flush();

    expect(uibModalInstance.close).not.toHaveBeenCalled();
    expect(sharedService.renewToken).toHaveBeenCalled();
  });

  it('Should correctly handle a server error code (user not authorised)', function() {

    /**
     * Simulate an error (user not  authorised )
     */
    $httpBackend.expectPOST(basePath + '/event').respond(403);

    scope.register();

    $httpBackend.flush();

    expect(uibModalInstance.close).not.toHaveBeenCalled();
    expect(errorService.handleEventError).toHaveBeenCalled();
    expect(scope.userNotAuthorised).toBeTruthy();
  });

  it('Should correctly register the edited event when it is not a jam', function() {

    scope.event.type.type = "ONGEVAL";
    var response = $httpBackend.whenPOST(basePath + '/event');
    response.respond(200);
    scope.register();
    $httpBackend.flush();
    expect(uibModalInstance.close).toHaveBeenCalled();
    expect(scope.event.publication_time).toBeDefined();
    expect(scope.event.last_edit_time).toBeDefined();
    expect(scope.event.relevant_for_transportation_types).toEqual([]);
    expect(scope.event.coordinates).toBeDefined();

  });

  it('Should call the correct function when checking address for validity', function() {
    scope.isAddressValid("test");
    expect(addressService.validateAddress).toHaveBeenCalled();
  });

  it('Should close the modal when the button is clicked', function() {
    scope.close();
    expect(uibModalInstance.dismiss).toHaveBeenCalledWith("Closed new event modal");
  });
});
