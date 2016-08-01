app.controller('ValidationReminderController', [
  '$scope',
  '$location',
  '$uibModalInstance',
  'user',
  'reverifyMailService',
  'localStorageService',
function(
  $scope,
  $location,
  $uibModalInstance,
  user,
  reverifyMailService,
  localStorageService) {

  $scope.close = function() {
    $uibModalInstance.close();
  };

  $scope.resendMail = function() {
    var accessToken = localStorageService.get("accessToken");
    var header = {
      'Authorization': accessToken.token
    };

    reverifyMailService(header).save({ u: user.id }, user.email).$promise.then(
        function(result) {
            $uibModalInstance.close();
        },
        function(error) {
            $scope.reverifyMailWentWrong = true;
        }
      );
  };

}]);
