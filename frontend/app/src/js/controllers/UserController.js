/*
 *This controller will handle all the logic in reference to the creation of a user.
 */
app.controller('UserController', [
  '$scope',
  '$location',
  '$uibModalInstance',
  'sharedService',
  'registrationService',
  'localStorageService',
  'regularRefreshTokenService',
  'accessTokenService',
function(
  $scope,
  $location,
  $uibModalInstance,
  sharedService,
  registrationService,
  localStorageService,
  regularRefreshTokenService,
  accessTokenService) {

    $scope.initData = function() {
      if (registrationService.getUser() !== undefined) {
        $scope.user = registrationService.getUser();
      } else {
        $scope.user = {};
        sharedService.setDefaultUserOptions($scope.user);
      }
    };

    $scope.initData();

    $scope.close = function() {
      $uibModalInstance.dismiss("Closed userregistration modal.");
      registrationService.clearRegistration();
      localStorageService.clearAll();
    };

    $scope.register = function() {
      registrationService.setUser($scope.user);
      localStorageService.set("registrationUser", $scope.user);

      $scope.token = {
        email : $scope.user.email,
        password : $scope.user.password
      };

      sharedService.postUser($scope, $scope.user);
      $scope.registerUser();
    };

    $scope.validatePassword = function(passw) {
      var strongRegex = new RegExp("^(?=.*[a-z])(?=.*[A-Z])(?=.*[0-9])(?=.{8,})");
      return strongRegex.test(passw);
    };

    $scope.registerUser = function() {
      $scope.$watch('result', function(newValue, oldValue) {
        if ($scope.result) {
          regularRefreshTokenService.save($scope.token).$promise.then(
            function(refreshToken) {
              localStorageService.set("refreshToken", refreshToken);

              accessTokenService({'Authorization': refreshToken.token}).save(refreshToken).$promise.then(
                function(accessToken) {
                  localStorageService.set("accessToken", accessToken);

                  $uibModalInstance.close();
                  $location.path('/registration');
                },
                // Error handling for accessTokenService
                function(error) {
                  $scope.error = "iets misgelopen";
                }
              );
            },
            // Error handling for regularRefreshTokenService
            function(error) {
              $scope.error = "Inloggegevens zijn incorrect";
            }
          );
        }
      });
    };

  }
]);
