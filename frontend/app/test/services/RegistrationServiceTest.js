fdescribe('AddressService', function() {
	beforeEach(module('App'));

  var registrationService;

	beforeEach(inject(function(_registrationService_) {
		registrationService = _registrationService_;
	}));

	/**
	 * Check if the user is correctly returned
	 */
	it('Should correctly set and get the user', function(){
		registrationService.setUser({"id": 1, "naam": "test"});

		expect(registrationService.getUser()).toEqual({"id": 1, "naam": "test"});
	});

  it('should clear the user', function() {
    registrationService.setUser({"id": 1, "naam": "test"});
    registrationService.clearRegistration();
    expect(registrationService.getUser()).toBe(undefined);
  });

});
