fdescribe("DialogModal test", function() {
  beforeEach(module('App'));
  beforeEach(module('AppMock'));


  beforeEach(inject(function(
    $rootScope,
    _$uibModal_,
    _dialogModal_
  ) {

    $uibModal = _$uibModal_;
    dialogModal = _dialogModal_;
    spyOn($uibModal, 'open').and.callFake(function() {
      return {
        test: "test"
      };
    });

  }));

  fit("Should open a modal when needed", function() {
      expect(dialogModal("test", "test", "test", "test")).toEqual({
      test: "test"
    });
  });

});
