fdescribe('Test MapsService', function() {
  beforeEach(module('App'));

  var httpBackend, q, array, mapsService;

  beforeEach(inject(function($httpBackend, $q, _mapsService_) {
    q = $q;
    httpBackend = $httpBackend;
    mapsService = _mapsService_;

    array = [
      { lat: 3,
        lon: 4
      },
      { lat: 5,
        lon: 6
      }
    ];
  }));

  /**
   * Check if all the variables are defined
   */
  it('should have defined all parameters', function() {
    expect(q).toBeDefined();
    expect(httpBackend).toBeDefined();
    expect(mapsService).toBeDefined();
  });

  it("should test getLocaion", function() {
    var request1 = 'https://maps.googleapis.com/maps/api/geocode/json?latlng=3,4&key=AIzaSyBSEb7-qBPegmyEPD2poDx7MopVj0_LY2Q';
    var request2 = 'https://maps.googleapis.com/maps/api/geocode/json?latlng=5,6&key=AIzaSyBSEb7-qBPegmyEPD2poDx7MopVj0_LY2Q';
    httpBackend.whenGET(request1).respond(200, "location1");
    httpBackend.whenGET(request2).respond(200, "location2");
    var result = mapsService.getLocation(array);
    httpBackend.flush();
    expect(result.$$state.value[0].data).toEqual("location1");
    expect(result.$$state.value[1].data).toEqual("location2");
  });
});
