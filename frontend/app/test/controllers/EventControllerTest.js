fdescribe('EventController', function() {
  beforeEach(module('App'));
  beforeEach(module('AppMock'));

  var controller, scope, eventServiceMock, sharedService, uibModalInstance;

  beforeEach(inject(function(
    $rootScope,
    $controller,
    $routeParams,
    _sharedService_,
    _localStorageService_,
    _$uibModal_) {

    /**
     * Set httpBackend basePath
     */
    basePath = "http://localhost:8080";

    scope = $rootScope.$new();
    sharedService = _sharedService_;
    localStorageService =_localStorageService_;
    $uibModal = _$uibModal_;
    scope.map = {
      setCenter: function(){
        return;
      }
    };

    spyOn(localStorageService, 'get').and.callFake(function(){
      return {user_id: 1};
    });
    spyOn(sharedService, 'loadUserEventsAndShow').and.callFake(function(){
      return;
    });
    spyOn(scope.map, 'setCenter').and.callThrough();
    spyOn(sharedService, 'getTypes').and.callFake(function(){
      return {test: "test"};
    });
    spyOn($uibModal, 'open').and.callFake(function(){return;});
    controller = $controller('EventController', {
      $scope: scope,
      $routeParams: { userId: 1 },
      sharedService: sharedService,
      localStorageService: localStorageService,
      $uibModal: $uibModal
    });
    scope.$apply();
  }));

  it('should have defined all parameters', function() {
    expect(scope).toBeDefined();
    expect(localStorageService).toBeDefined();
    expect(sharedService).toBeDefined();
    expect(controller).toBeDefined();
  });

  it('should set eventtypes', function() {
    expect(scope.eventTypes).toEqual({test: "test"});
  });

  it('should call the function in sharedService to load and show all events of a particular user', function() {
    expect(sharedService.loadUserEventsAndShow).toHaveBeenCalledWith(scope, 1);
  });

  it('call the right functions when opening a marker', function() {
    scope.markers = [];
    scope.markers[0] = {
      getPosition: function(){
        return 0;
      }
    };
    scope.openMarker(0);
    expect(scope.map.setCenter).toHaveBeenCalledWith(0);
  });

  it('Should open a modal when deleting an event', function(){
    scope.confirmAndDelete(10);
    expect(scope.deleteIndex).toBe(10);
    expect($uibModal.open).toHaveBeenCalled();
  });

});
