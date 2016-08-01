/* Test Confirmation */
fdescribe('Test ModalInstanceController', function() {

	beforeEach(module('App'));

	var scope, controller, settings, $uibModalInstance;

  // before the testing, we inject all the elements that are needed to test the controller
  beforeEach(inject(function ($rootScope, $controller) {

    scope = $rootScope.$new();
    $uibModalInstance = {
        close: jasmine.createSpy('uibModalInstance.close'),
        dismiss: jasmine.createSpy('uibModalInstance.dismiss'),
        result: {
            then: jasmine.createSpy('uibModalInstance.result.then')
        }
    };
    settings = {};
    controller = $controller('ModalInstanceController', {
      $scope: scope,
      $uibModalInstance: $uibModalInstance,
      settings: settings
    });

  }));

  it('should check if all arguments are defined', function() {
    expect(scope).toBeDefined();
    expect($uibModalInstance).toBeDefined();
    expect(controller).toBeDefined();
  });

  it("should call close when $scope.ok is invoked", function() {
    scope.ok();
    expect($uibModalInstance.close).toHaveBeenCalled();
  });

  it("should call dismiss when $scope.cancel is invoked", function() {
    scope.cancel();
    expect($uibModalInstance.dismiss).toHaveBeenCalledWith('cancel');
  });

});
