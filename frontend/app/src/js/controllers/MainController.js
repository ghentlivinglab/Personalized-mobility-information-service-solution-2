app.controller('MainController', [
  '$scope',
  '$route',
  '$location',
  '$uibModal',
  'userService',
  'sharedService',
  'regularRefreshTokenService',
  'accessTokenService',
  'localStorageService',
  function(
    $scope,
    $route,
    $location,
    $uibModal,
    userService,
    sharedService,
    regularRefreshTokenService,
    accessTokenService,
    localStorageService) {

      /**
      * Function to open a new modal for login
      */
      $scope.openLoginModal = function() {
        var modalInstance = $uibModal.open({
          templateUrl: 'js/directives/loginDirective.html',
          controller: 'LoginController'
        });

        modalInstance.result.then(
          function(){
            /**
            * After all the tokens were succesfully saved, we redirect the user to his or her userindex page
            */
            $location.path('/userindex');
          }, function() {}
        );
      };

      /**
      * Function that initializes the index page only if there is no user still logged in.
      */
      $scope.initPage = function() {
        /**
        * If the user is still logged in, we redirect to his or her user index page
        */
        if (localStorageService.get('accessToken') !== null) {
          $location.path('/userindex');
        } else {
          if ($location.hash() === "login") {
            $scope.openLoginModal();
          }
          $scope.currentPage = $route.current.templateUrl;

          /*
          * Load all eventtypes.
          */
          $scope.eventTypes = sharedService.getTypes();

          /**
          * Call function that will load all current events, create map and show all events with markers
          */
          sharedService.loadAllEventsAndShow($scope);
        }
      };

      $scope.initPage();

      $scope.openRegistration = function() {
        $uibModal.open({
          templateUrl: 'view/user/userRegistration.html',
          controller: 'UserController',
          backdrop: 'static',
          size: 'lg'
        });
      };

    }

  ]);
