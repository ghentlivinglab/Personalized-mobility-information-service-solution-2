/*
 * Test for MainController.
 */
fdescribe('MainController', function() {
    beforeEach(module('App'));
    beforeEach(module('AppMock'));

    var controller, scope, $route, userServiceMock;
    var fakeModal = {
        open: function() {
            return {
                result: {
                    then: function(callback) {
                        callback("item1");
                    }
                }
            };
        }
    };
    /*
     * Instantiate the controller with a mock of the userService and spy on the query-method.
     */
    beforeEach(inject(function($rootScope, $controller, _$route_, _userServiceMock_, _$uibModal_, _$location_, _sharedService_, _regularRefreshTokenService_, _accessTokenService_, _localStorageService_) {
        scope = $rootScope.$new();
        userServiceMock = _userServiceMock_;
        $route = _$route_;
        $location = _$location_;
        $uibModal = _$uibModal_;
        $location = _$location_;
        sharedService = _sharedService_;
        regularRefreshTokenService = _regularRefreshTokenService_;
        accessTokenService = _accessTokenService_;
        localStorageService = _localStorageService_;

        /**
         * Mock the current state of the route
         */
        $route.current = {
            templateUrl: 'test'
        };

        controller = $controller('MainController', {
            $scope: scope,
            $route: $route,
            $location: $location,
            $uibModal: $uibModal,
            userService: userServiceMock,
            sharedService: sharedService,
            regularRefreshTokenService: regularRefreshTokenService,
            accessTokenService: accessTokenService,
            localStorageService: localStorageService

        });

        spyOn(userServiceMock, 'getUsers');
        spyOn($location, 'path');
        /**
         * Finally able to go in modalinstance result in the controllers
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
                }
            };
        });
        spyOn(localStorageService, 'get').and.callFake(function(value){
                return 'test';
        });
        spyOn($location, 'hash').and.callFake(function(){
            return "login";
        });
        spyOn(scope, 'openLoginModal').and.callThrough();
        spyOn(sharedService, 'getTypes').and.callThrough();
        spyOn(sharedService, 'loadAllEventsAndShow').and.callThrough();
    }));

    /*
     * Test whether everything had been defined.
     */
    it('schould have defined controller and scope', function() {
        expect(scope).toBeDefined();
        expect(userServiceMock).toBeDefined();
        expect(controller).toBeDefined();
    });

    it('should correctly open the login modal and call location on success', function() {
        scope.openLoginModal();
        expect($location.path).toHaveBeenCalledWith('/userindex');
    });

    it('should correctly open the login modal and do nothing on error', function() {
        /**
         * Overwrite the spy and only return the error. So the $location.pas() funtion would not be called
         */
        $uibModal.open.and.callFake(function(){
            return {
                result: {
                    then: function(callback, errorCallback) {
                        errorCallback();
                    }
                }
            };
        });

        scope.openLoginModal();
        expect($location.path).not.toHaveBeenCalled();
    });

    it('should change the path when the access token is defined', function(){
            scope.initPage();
            expect($location.path).toHaveBeenCalled();
    });

    it('should call the openLoginModal if the access token is not set and the current location is login', function(){
        // Overwrite the fake function call with one that returns null so the login modal will be opened
        localStorageService.get.and.callFake(function(){
            return null;
        });
        scope.initPage();

        expect(scope.openLoginModal).toHaveBeenCalled();
        expect(scope.currentPage).toEqual('test');
        expect(sharedService.getTypes).toHaveBeenCalled();
        expect(sharedService.loadAllEventsAndShow).toHaveBeenCalled();
    });

    it('Should open a modal when trying to register', function(){
        scope.openRegistration();

        expect($uibModal.open).toHaveBeenCalled();
    });

});
