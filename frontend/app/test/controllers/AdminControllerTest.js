fdescribe('AdmincontrolleTest', function() {
  beforeEach(module('App'));
  beforeEach(module('AppMock'));

  /*
   * Instantiate the controller with a mock of the userService and spy on the query-method.
   */
  beforeEach(inject(function($rootScope, $controller, _$route_, _$timeout_, _userService_, _$location_, _accessTokenService_, _localStorageService_, _adminService_, _adminDeleteUserService_, _operatorService_, _dataDumpService_, _sharedService_, _$httpBackend_) {
    $controller = $controller;
    scope = $rootScope.$new();
    $route = _$route_;
    $location = _$location_;
    $timeout = _$timeout_;
    localStorageService = _localStorageService_;
    userService = _userService_;
    adminService = _adminService_;
    sharedService = _sharedService_;
    adminDeleteUserService = _adminDeleteUserService_;
    operatorService = _operatorService_;
    dataDumpService = _dataDumpService_;
    $httpBackend = _$httpBackend_;

    basePath = "http://localhost:8080";

    $route.current = {
      templateUrl: 'test'
    };

    spyOn(localStorageService, 'get').and.callFake(function(value) {
      return {
        token: 'test',
        role: 'user',
        user_id: 1
      };
    });

    controller = $controller('AdminController', {
      $scope: scope,
      $route: $route,
      $location: $location,
      $timeout: $timeout,
      localStorageService: localStorageService,
      sharedService: sharedService,
      userService: userService,
      adminService: adminService,
      adminDeleteUserService: adminDeleteUserService,
      operatorService: operatorService,
      dataDumpService: dataDumpService
    });


    $httpBackend.whenGET('view/user/userIndex.html').respond(200);
    spyOn(sharedService, 'renewToken').and.callThrough();
    spyOn(console, 'error').and.callThrough();
    spyOn(scope, 'reloadData').and.callThrough();
    spyOn($location, 'path').and.callThrough();
    spyOn(scope, 'initPage').and.callThrough();
    spyOn(scope, 'makeAdmin').and.callThrough();
    spyOn(scope, 'makeOperator').and.callThrough();
    spyOn($route, 'reload').and.callThrough();
    spyOn(scope, 'deleteUser').and.callThrough();
    spyOn(scope, 'clickDownload').and.callFake(function(){return;});
    spyOn(scope, 'deleteAdmin').and.callThrough();
    spyOn(scope, 'deleteOperator').and.callThrough();
  }));

  it("Should reload the data correctly when needed", function() {
    $httpBackend.whenGET(basePath + '/admin/admin').respond(200, ["test", "iets"]);
    $httpBackend.whenGET(basePath + '/admin/operator').respond(200, ["test"]);
    $httpBackend.whenGET(basePath + '/user').respond(200, ['iets']);
    $httpBackend.whenGET(basePath + '/user/1').respond(200, 'test');
    $httpBackend.whenGET(basePath + '/admin/data_dump').respond(200, {
      test: "test"
    });
    scope.reloadData();
    $httpBackend.flush();
    $timeout(function() {
      expect(scope.admins).toEqual(['test', 'iets']);
      expect(scope.operators).toEqual('test');
    }, 150);



  });

  it('Should correctly handle errors from adminService in reloadData(other error)', function() {
    $httpBackend.whenGET(basePath + '/admin/admin').respond(500);
    $httpBackend.whenGET(basePath + '/admin/operator').respond(200, ["test"]);
    $httpBackend.whenGET(basePath + '/user').respond(200, ['iets']);
    $httpBackend.whenGET(basePath + '/user/1').respond(200, 'test');
    $httpBackend.whenGET(basePath + '/admin/data_dump').respond(200, {
      test: "test"
    });
    scope.reloadData();
    $httpBackend.flush();
    expect(console.error).toHaveBeenCalled();
    expect(sharedService.renewToken).not.toHaveBeenCalled();


  });

  it('Should correctly handle errors from adminService in reloadData(error 401)', function() {
    $httpBackend.whenGET(basePath + '/admin/admin').respond(401);
    $httpBackend.whenGET(basePath + '/admin/operator').respond(200, ["test"]);
    $httpBackend.whenGET(basePath + '/user').respond(200, ['iets']);
    $httpBackend.whenGET(basePath + '/user/1').respond(200, 'test');
    $httpBackend.whenGET(basePath + '/admin/data_dump').respond(200, {
      test: "test"
    });
    scope.reloadData();
    scope.reloadData.and.callFake(function() {
      return;
    });
    $httpBackend.flush();
    expect(console.error).not.toHaveBeenCalled();
    expect(sharedService.renewToken).toHaveBeenCalled();
    expect(scope.reloadData).toHaveBeenCalled();
  });

  it('Should correctly handle errors from operatorService in reloadData(other error)', function() {
    $httpBackend.whenGET(basePath + '/admin/admin').respond(200, ["test", "iets"]);
    $httpBackend.whenGET(basePath + '/admin/operator').respond(500);
    $httpBackend.whenGET(basePath + '/user').respond(200, ['iets']);
    $httpBackend.whenGET(basePath + '/user/1').respond(200, 'test');
    $httpBackend.whenGET(basePath + '/admin/data_dump').respond(200, {
      test: "test"
    });
    scope.reloadData();
    $httpBackend.flush();
    expect(console.error).toHaveBeenCalled();
    expect(sharedService.renewToken).not.toHaveBeenCalled();
  });

  it('Should correctly handle errors from operatorService in reloadData(error 401)', function() {
    $httpBackend.whenGET(basePath + '/admin/admin').respond(200, ["test", "iets"]);
    $httpBackend.whenGET(basePath + '/admin/operator').respond(401);
    $httpBackend.whenGET(basePath + '/user').respond(200, ['iets']);
    $httpBackend.whenGET(basePath + '/user/1').respond(200, 'test');
    $httpBackend.whenGET(basePath + '/admin/data_dump').respond(200, {
      test: "test"
    });
    scope.reloadData();
    scope.reloadData.and.callFake(function() {
      return;
    });
    $httpBackend.flush();
    expect(scope.reloadData).toHaveBeenCalled();
    expect(sharedService.renewToken).toHaveBeenCalled();
  });

  it('Should correctly init page', function() {
    $httpBackend.whenGET(basePath + '/admin/admin').respond(200, ["test", "iets"]);
    $httpBackend.whenGET(basePath + '/admin/operator').respond(200, ["test"]);
    $httpBackend.whenGET(basePath + '/user').respond(200, ['iets']);
    $httpBackend.whenGET(basePath + '/user/1').respond(200, {
      test: "test"
    });
    $httpBackend.whenGET(basePath + '/admin/data_dump').respond(200, {
      test: "test"
    });
    scope.initPage();
    $httpBackend.flush();
    expect(scope.currentUser.toJSON()).toEqual({
      test: "test"
    });
    expect(scope.dataDump.toJSON()).toEqual({
      test: "test"
    });
    expect(scope.reloadData).toHaveBeenCalled();
  });

  it('Sould correctly handle error from userService when getting specific user(401)', function(){
    $httpBackend.whenGET(basePath + '/admin/admin').respond(200, ["test", "iets"]);
    $httpBackend.whenGET(basePath + '/admin/operator').respond(200, ["test"]);
    $httpBackend.whenGET(basePath + '/user').respond(200, ['iets']);
    $httpBackend.whenGET(basePath + '/user/1').respond(401);
    $httpBackend.whenGET(basePath + '/admin/data_dump').respond(200, {
      test: "test"
    });

    scope.initPage();
    scope.initPage.and.callFake(function(){
      return;
    });
    $httpBackend.flush();
    expect(sharedService.renewToken).toHaveBeenCalled();
    expect(scope.initPage).toHaveBeenCalled();
  });

  it('Sould correctly handle error from userService when getting specific user(500)', function(){
    $httpBackend.whenGET(basePath + '/admin/admin').respond(200, ["test", "iets"]);
    $httpBackend.whenGET(basePath + '/admin/operator').respond(200, ["test"]);
    $httpBackend.whenGET(basePath + '/user').respond(200, ['iets']);
    $httpBackend.whenGET(basePath + '/user/1').respond(500);
    $httpBackend.whenGET(basePath + '/admin/data_dump').respond(200, {
      test: "test"
    });

    scope.initPage();
    scope.initPage.and.callFake(function(){
      return;
    });
    $httpBackend.flush();
    expect(sharedService.renewToken).not.toHaveBeenCalled();
    expect(scope.initPage).toHaveBeenCalled();
    expect(console.error).toHaveBeenCalled();
  });

  it('Sould correctly handle error from userService when getting all users(401)', function(){
    $httpBackend.whenGET(basePath + '/admin/admin').respond(200, ["test", "iets"]);
    $httpBackend.whenGET(basePath + '/admin/operator').respond(200, ["test"]);
    $httpBackend.whenGET(basePath + '/user').respond(401);
    $httpBackend.whenGET(basePath + '/user/1').respond(200, {
      test: "test"
    });
    $httpBackend.whenGET(basePath + '/admin/data_dump').respond(200, {
      test: "test"
    });

    scope.initPage();
    scope.initPage.and.callFake(function(){
      return;
    });
    $httpBackend.flush();
    expect(sharedService.renewToken).toHaveBeenCalled();
    expect(scope.initPage).toHaveBeenCalled();
  });

  it('Sould correctly handle error from userService when getting all users(500)', function(){
    $httpBackend.whenGET(basePath + '/admin/admin').respond(200, ["test", "iets"]);
    $httpBackend.whenGET(basePath + '/admin/operator').respond(200, ["test"]);
    $httpBackend.whenGET(basePath + '/user').respond(500);
    $httpBackend.whenGET(basePath + '/user/1').respond(200, {
      test: "test"
    });
    $httpBackend.whenGET(basePath + '/admin/data_dump').respond(200, {
      test: "test"
    });

    scope.initPage();
    scope.initPage.and.callFake(function(){
      return;
    });
    $httpBackend.flush();
    expect(sharedService.renewToken).not.toHaveBeenCalled();
    expect(scope.initPage).toHaveBeenCalled();
    expect(console.error).toHaveBeenCalled();
  });

  it('Sould correctly handle error from userService when getting datadump(401)', function(){
    $httpBackend.whenGET(basePath + '/admin/admin').respond(200, ["test", "iets"]);
    $httpBackend.whenGET(basePath + '/admin/operator').respond(200, ["test"]);
    $httpBackend.whenGET(basePath + '/user').respond(401);
    $httpBackend.whenGET(basePath + '/user/1').respond(200, {
      test: "test"
    });
    $httpBackend.whenGET(basePath + '/admin/data_dump').respond(401);

    scope.initPage();
    scope.initPage.and.callFake(function(){
      return;
    });
    $httpBackend.flush();
    expect(sharedService.renewToken).toHaveBeenCalled();
    expect(scope.initPage).toHaveBeenCalled();
  });

  it('Sould correctly handle error from userService when getting datadump(500)', function(){
    $httpBackend.whenGET(basePath + '/admin/admin').respond(200, ["test", "iets"]);
    $httpBackend.whenGET(basePath + '/admin/operator').respond(200, ["test"]);
    $httpBackend.whenGET(basePath + '/user').respond(500);
    $httpBackend.whenGET(basePath + '/user/1').respond(200, {
      test: "test"
    });
    $httpBackend.whenGET(basePath + '/admin/data_dump').respond(500);
    scope.initPage();
    scope.initPage.and.callFake(function(){
      return;
    });
    $httpBackend.flush();
    expect(sharedService.renewToken).not.toHaveBeenCalled();
    expect(scope.initPage).toHaveBeenCalled();
    expect(console.error).toHaveBeenCalled();
  });

  /*it('Should correctly change user id', function(){
    scope.users = [];
    scope.users[0] = {
      id: 15
    };
    scope.users[1] = {
      id: 20
    };
    scope.findAndChangeUserId(20);
    expect(scope.userId).toEqual(20);

  });*/

  it("Should correctly make admin", function(){
    $httpBackend.whenGET(basePath + '/admin/admin').respond(200, ["test", "iets"]);
    $httpBackend.whenGET(basePath + '/admin/operator').respond(200, ["test"]);
    $httpBackend.whenGET(basePath + '/user').respond(500);
    $httpBackend.whenGET(basePath + '/user/1').respond(200, {
      test: "test"
    });
    $httpBackend.whenGET(basePath + '/admin/data_dump').respond(500);
    $httpBackend.whenPOST(basePath + "/admin/admin").respond(200);

    scope.makeAdmin("Test@iets.be");
    $httpBackend.flush();
    expect(scope.reloadData).toHaveBeenCalled();
    expect($location.path).toHaveBeenCalled();
    expect(scope.message).toEqual("Test@iets.be werd succesvol toegevoegd als admin");
    $timeout.flush();
    expect(scope.message).toEqual("");
  });

  it("Should correctly ask refresh token when expired when making admin", function(){
    $httpBackend.whenGET(basePath + '/admin/admin').respond(200, ["test", "iets"]);
    $httpBackend.whenGET(basePath + '/admin/operator').respond(200, ["test"]);
    $httpBackend.whenGET(basePath + '/user').respond(500);
    $httpBackend.whenGET(basePath + '/user/1').respond(200, {
      test: "test"
    });
    $httpBackend.whenGET(basePath + '/admin/data_dump').respond(500);
    $httpBackend.whenPOST(basePath + "/admin/admin").respond(401);

    scope.makeAdmin("Test@iets.be");
    scope.makeAdmin.and.callFake(function(){
      return;
    });
    $httpBackend.flush();
    expect(scope.makeAdmin).toHaveBeenCalled();
    expect(sharedService.renewToken).toHaveBeenCalled();
  });

  it("Should correctly print error when it is another error when making admin", function(){
    $httpBackend.whenGET(basePath + '/admin/admin').respond(200, ["test", "iets"]);
    $httpBackend.whenGET(basePath + '/admin/operator').respond(200, ["test"]);
    $httpBackend.whenGET(basePath + '/user').respond(500);
    $httpBackend.whenGET(basePath + '/user/1').respond(200, {
      test: "test"
    });
    $httpBackend.whenGET(basePath + '/admin/data_dump').respond(500);
    $httpBackend.whenPOST(basePath + "/admin/admin").respond(500);

    scope.makeAdmin("Test@iets.be");
    $httpBackend.flush();
    expect(console.error).toHaveBeenCalled();
  });

  it("Should correctly make operator", function(){
    $httpBackend.whenGET(basePath + '/admin/admin').respond(200, ["test", "iets"]);
    $httpBackend.whenGET(basePath + '/admin/operator').respond(200, ["test"]);
    $httpBackend.whenGET(basePath + '/user').respond(500);
    $httpBackend.whenGET(basePath + '/user/1').respond(200, {
      test: "test"
    });
    $httpBackend.whenGET(basePath + '/admin/data_dump').respond(200);
    $httpBackend.whenPOST(basePath + "/admin/operator").respond(200);

    scope.makeOperator("Test@iets.be");
    $httpBackend.flush();
    expect(scope.reloadData).toHaveBeenCalled();
    expect($location.path).toHaveBeenCalled();
    expect(scope.message).toEqual("Test@iets.be werd succesvol toegevoegd als operator");
    $timeout.flush();
    expect(scope.message).toEqual("");
  });

  it("Should correctly ask refresh token when expired when making operator", function(){
    $httpBackend.whenGET(basePath + '/admin/admin').respond(200, ["test", "iets"]);
    $httpBackend.whenGET(basePath + '/admin/operator').respond(200, ["test"]);
    $httpBackend.whenGET(basePath + '/user').respond(500);
    $httpBackend.whenGET(basePath + '/user/1').respond(200, {
      test: "test"
    });
    $httpBackend.whenGET(basePath + '/admin/data_dump').respond(200);
    $httpBackend.whenPOST(basePath + "/admin/operator").respond(401);

    scope.makeOperator("Test@iets.be");
    scope.makeOperator.and.callFake(function(){
      return;
    });
    $httpBackend.flush();
    expect(scope.makeOperator).toHaveBeenCalled();
    expect(sharedService.renewToken).toHaveBeenCalled();
  });

  it("Should correctly print error when it is another error when making operator", function(){
    $httpBackend.whenGET(basePath + '/admin/admin').respond(200, ["test", "iets"]);
    $httpBackend.whenGET(basePath + '/admin/operator').respond(200, ["test"]);
    $httpBackend.whenGET(basePath + '/user').respond(500);
    $httpBackend.whenGET(basePath + '/user/1').respond(200, {
      test: "test"
    });
    $httpBackend.whenGET(basePath + '/admin/data_dump').respond(200);
    $httpBackend.whenPOST(basePath + "/admin/operator").respond(500);

    scope.makeOperator("Test@iets.be");
    $httpBackend.flush();
    expect(console.error).toHaveBeenCalled();
  });

  it('Should correctly delete user', function(){
    $httpBackend.whenGET(basePath + '/admin/admin').respond(200, ["test", "iets"]);
    $httpBackend.whenGET(basePath + '/admin/operator').respond(200, ["test"]);
    $httpBackend.whenGET(basePath + '/user').respond(500);
    $httpBackend.whenGET(basePath + '/user/1').respond(200, {
      test: "test"
    });
    $httpBackend.whenGET(basePath + '/admin/data_dump').respond(200);
    $httpBackend.whenDELETE(basePath + "/admin/user/1").respond(200, "test");
    scope.deleteUser(1);
    $httpBackend.flush();
    expect($route.reload).toHaveBeenCalled();

  });

  it('Should correctly refresh token when it is expired when deleting user', function(){
    $httpBackend.whenGET(basePath + '/admin/admin').respond(200, ["test", "iets"]);
    $httpBackend.whenGET(basePath + '/admin/operator').respond(200, ["test"]);
    $httpBackend.whenGET(basePath + '/user').respond(500);
    $httpBackend.whenGET(basePath + '/user/1').respond(200, {
      test: "test"
    });
    $httpBackend.whenGET(basePath + '/admin/data_dump').respond(200);
    $httpBackend.whenDELETE(basePath + "/admin/user/1").respond(401);
    scope.deleteUser(1);
    scope.deleteUser.and.callFake(function(){
      return;
    });
    $httpBackend.flush();
    expect(sharedService.renewToken).toHaveBeenCalled();
    expect(scope.deleteUser).toHaveBeenCalled();
  });

  it('Should correctly print error when deleting user', function(){
    $httpBackend.whenGET(basePath + '/admin/admin').respond(200, ["test", "iets"]);
    $httpBackend.whenGET(basePath + '/admin/operator').respond(200, ["test"]);
    $httpBackend.whenGET(basePath + '/user').respond(500);
    $httpBackend.whenGET(basePath + '/user/1').respond(200, {
      test: "test"
    });
    $httpBackend.whenGET(basePath + '/admin/data_dump').respond(200);
    $httpBackend.whenDELETE(basePath + "/admin/user/1").respond(500);
    scope.deleteUser(1);
    $httpBackend.flush();
    expect(console.error).toHaveBeenCalled();
  });

  it('Should correctly check if person with userId is admin', function(){
    $httpBackend.whenGET(basePath + '/admin/admin').respond(200, ["test", "iets"]);
    $httpBackend.whenGET(basePath + '/admin/operator').respond(200, ["test"]);
    $httpBackend.whenGET(basePath + '/user').respond(500);
    $httpBackend.whenGET(basePath + '/user/1').respond(200, {
      test: "test"
    });
    $httpBackend.whenGET(basePath + '/admin/data_dump').respond(200);
    scope.admins = [];
    scope.admins[0] = {
      id: 15
    };
    expect(scope.isAdmin(15)).toBeTruthy();
    expect(scope.isAdmin(20)).not.toBeTruthy();

  });

  it('Should correctly check if person with userId is operator', function(){
    $httpBackend.whenGET(basePath + '/admin/admin').respond(200, ["test", "iets"]);
    $httpBackend.whenGET(basePath + '/admin/operator').respond(200, ["test"]);
    $httpBackend.whenGET(basePath + '/user').respond(500);
    $httpBackend.whenGET(basePath + '/user/1').respond(200, {
      test: "test"
    });
    $httpBackend.whenGET(basePath + '/admin/data_dump').respond(200);
    scope.operators = [];
    scope.operators[0] = {
      id: 15
    };
    expect(scope.isOperator(15)).toBeTruthy();
    expect(scope.isOperator(20)).not.toBeTruthy();

  });

  it('Should start download dump', function(){
    $httpBackend.whenGET(basePath + '/admin/admin').respond(200, ["test", "iets"]);
    $httpBackend.whenGET(basePath + '/admin/operator').respond(200, ["test"]);
    $httpBackend.whenGET(basePath + '/user').respond(500);
    $httpBackend.whenGET(basePath + '/user/1').respond(200, {
      test: "test"
    });
    $httpBackend.whenGET(basePath + '/admin/data_dump').respond(200);
    scope.clickDownload({test:"test"}, 'testDump.txt');
  });

  it('Should start download dump', function(){
    $httpBackend.whenGET(basePath + '/admin/admin').respond(200, ["test", "iets"]);
    $httpBackend.whenGET(basePath + '/admin/operator').respond(200, ["test"]);
    $httpBackend.whenGET(basePath + '/user').respond(500);
    $httpBackend.whenGET(basePath + '/user/1').respond(200, {
      test: "test"
    });
    $httpBackend.whenGET(basePath + '/admin/data_dump').respond(200);
    scope.downloadDataDump();
    expect(scope.clickDownload).toHaveBeenCalled();
  });

  it('Should start download users dump', function(){
    $httpBackend.whenGET(basePath + '/admin/admin').respond(200, ["test", "iets"]);
    $httpBackend.whenGET(basePath + '/admin/operator').respond(200, ["test"]);
    $httpBackend.whenGET(basePath + '/user').respond(500);
    $httpBackend.whenGET(basePath + '/user/1').respond(200, {
      test: "test"
    });
    $httpBackend.whenGET(basePath + '/admin/data_dump').respond(200);
    scope.dataDump = {};
    scope.dataDump.users = "test";
    scope.downloadUsersDump();
    expect(scope.clickDownload).toHaveBeenCalled();
  });

  it('Should start download travels dump', function(){
    $httpBackend.whenGET(basePath + '/admin/admin').respond(200, ["test", "iets"]);
    $httpBackend.whenGET(basePath + '/admin/operator').respond(200, ["test"]);
    $httpBackend.whenGET(basePath + '/user').respond(500);
    $httpBackend.whenGET(basePath + '/user/1').respond(200, {
      test: "test"
    });
    $httpBackend.whenGET(basePath + '/admin/data_dump').respond(200);
    scope.dataDump = {};
    scope.dataDump.travels = "test";
    scope.downloadTravelsDump();
    expect(scope.clickDownload).toHaveBeenCalled();
  });

  it('Should start download POI dump', function(){
    $httpBackend.whenGET(basePath + '/admin/admin').respond(200, ["test", "iets"]);
    $httpBackend.whenGET(basePath + '/admin/operator').respond(200, ["test"]);
    $httpBackend.whenGET(basePath + '/user').respond(500);
    $httpBackend.whenGET(basePath + '/user/1').respond(200, {
      test: "test"
    });
    $httpBackend.whenGET(basePath + '/admin/data_dump').respond(200);
    scope.dataDump = {};
    scope.dataDump.travels = "test";
    scope.downloadPointOfInterestsDump();
    expect(scope.clickDownload).toHaveBeenCalled();
  });

  it('Should start download Events dump', function(){
    $httpBackend.whenGET(basePath + '/admin/admin').respond(200, ["test", "iets"]);
    $httpBackend.whenGET(basePath + '/admin/operator').respond(200, ["test"]);
    $httpBackend.whenGET(basePath + '/user').respond(500);
    $httpBackend.whenGET(basePath + '/user/1').respond(200, {
      test: "test"
    });
    $httpBackend.whenGET(basePath + '/admin/data_dump').respond(200);
    scope.dataDump = {};
    scope.dataDump.travels = "test";
    scope.downloadEventsDump();
    expect(scope.clickDownload).toHaveBeenCalled();
  });

  it('Should start download Events dump', function(){
    $httpBackend.whenGET(basePath + '/admin/admin').respond(200, ["test", "iets"]);
    $httpBackend.whenGET(basePath + '/admin/operator').respond(200, ["test"]);
    $httpBackend.whenGET(basePath + '/user').respond(500);
    $httpBackend.whenGET(basePath + '/user/1').respond(200, {
      test: "test"
    });
    $httpBackend.whenGET(basePath + '/admin/data_dump').respond(200);
    scope.dataDump = {};
    scope.dataDump.travels = "test";
    scope.downloadEventTypesDump();
    expect(scope.clickDownload).toHaveBeenCalled();
  });

  it('Should correctly delete Admin', function(){
    $httpBackend.whenGET(basePath + '/admin/admin').respond(200, ["test", "iets"]);
    $httpBackend.whenGET(basePath + '/admin/operator').respond(200, ["test"]);
    $httpBackend.whenGET(basePath + '/user').respond(500);
    $httpBackend.whenGET(basePath + '/user/1').respond(200, {
      test: "test"
    });
    $httpBackend.whenGET(basePath + '/admin/data_dump').respond(200);
    $httpBackend.whenPUT(basePath + '/admin/admin').respond(200);
    scope.deleteAdmin("test@dat.be");
    $httpBackend.flush();
    expect(scope.reloadData).toHaveBeenCalled();
    expect($location.path).toHaveBeenCalled();
  });

  it('Should correctly ask new refresh token when expired when deleting admin', function(){
    $httpBackend.whenGET(basePath + '/admin/admin').respond(200, ["test", "iets"]);
    $httpBackend.whenGET(basePath + '/admin/operator').respond(200, ["test"]);
    $httpBackend.whenGET(basePath + '/user').respond(500);
    $httpBackend.whenGET(basePath + '/user/1').respond(200, {
      test: "test"
    });
    $httpBackend.whenGET(basePath + '/admin/data_dump').respond(200);
    $httpBackend.whenPUT(basePath + '/admin/admin').respond(401);
    scope.deleteAdmin("test@dat.be");
    scope.deleteAdmin.and.callFake(function(){
      return;
    });
    $httpBackend.flush();
    expect(scope.deleteAdmin).toHaveBeenCalled();
    expect(sharedService.renewToken).toHaveBeenCalled();
  });

  it('Should correctly print error', function(){
    $httpBackend.whenGET(basePath + '/admin/admin').respond(200, ["test", "iets"]);
    $httpBackend.whenGET(basePath + '/admin/operator').respond(200, ["test"]);
    $httpBackend.whenGET(basePath + '/user').respond(500);
    $httpBackend.whenGET(basePath + '/user/1').respond(200, {
      test: "test"
    });
    $httpBackend.whenGET(basePath + '/admin/data_dump').respond(200);
    $httpBackend.whenPUT(basePath + '/admin/admin').respond(500);
    scope.deleteAdmin("test@dat.be");
    $httpBackend.flush();
    expect(console.error).toHaveBeenCalled();
  });

  it('Should correctly delete operator', function(){
    $httpBackend.whenGET(basePath + '/admin/admin').respond(200, ["test", "iets"]);
    $httpBackend.whenGET(basePath + '/admin/operator').respond(200, ["test"]);
    $httpBackend.whenGET(basePath + '/user').respond(500);
    $httpBackend.whenGET(basePath + '/user/1').respond(200, {
      test: "test"
    });
    $httpBackend.whenGET(basePath + '/admin/data_dump').respond(200);
    $httpBackend.whenPUT(basePath + '/admin/operator').respond(200);
    scope.deleteOperator("test@dat.be");
    $httpBackend.flush();
    expect(scope.reloadData).toHaveBeenCalled();
    expect($location.path).toHaveBeenCalled();
  });

  it('Should correctly ask new refresh token when expired when deleting admin', function(){
    $httpBackend.whenGET(basePath + '/admin/admin').respond(200, ["test", "iets"]);
    $httpBackend.whenGET(basePath + '/admin/operator').respond(200, ["test"]);
    $httpBackend.whenGET(basePath + '/user').respond(500);
    $httpBackend.whenGET(basePath + '/user/1').respond(200, {
      test: "test"
    });
    $httpBackend.whenGET(basePath + '/admin/data_dump').respond(200);
    $httpBackend.whenPUT(basePath + '/admin/operator').respond(401);
    scope.deleteOperator("test@dat.be");
    scope.deleteOperator.and.callFake(function(){
      return;
    });
    $httpBackend.flush();
    expect(scope.deleteOperator).toHaveBeenCalled();
    expect(sharedService.renewToken).toHaveBeenCalled();
  });

  it('Should correctly print error', function(){
    $httpBackend.whenGET(basePath + '/admin/admin').respond(200, ["test", "iets"]);
    $httpBackend.whenGET(basePath + '/admin/operator').respond(200, ["test"]);
    $httpBackend.whenGET(basePath + '/user').respond(500);
    $httpBackend.whenGET(basePath + '/user/1').respond(200, {
      test: "test"
    });
    $httpBackend.whenGET(basePath + '/admin/data_dump').respond(200);
    $httpBackend.whenPUT(basePath + '/admin/operator').respond(500);
    scope.deleteOperator("test@dat.be");
    $httpBackend.flush();
    expect(console.error).toHaveBeenCalled();
  });
});
