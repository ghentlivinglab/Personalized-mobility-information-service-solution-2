app.factory('registrationService', [function() {
  var user;

  return {
    getUser: function() { return user; },
    setUser: function(new_user) { user = new_user; },
    clearRegistration: function() {
      user = undefined;
    }
  };
}]);
