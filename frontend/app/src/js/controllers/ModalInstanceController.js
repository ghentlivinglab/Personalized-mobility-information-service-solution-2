// setup the Controller to watch the click
app.controller('ModalInstanceController', ['$scope', '$uibModalInstance', 'settings',
  function ($scope, $uibModalInstance, settings) {
    // add settings to scope
    angular.extend($scope, settings);
    // ok button clicked
    $scope.ok = function () {
      $uibModalInstance.close(true);
    };
    // cancel button clicked
    $scope.cancel = function () {
      $uibModalInstance.dismiss('cancel');
    };
  }]
);
