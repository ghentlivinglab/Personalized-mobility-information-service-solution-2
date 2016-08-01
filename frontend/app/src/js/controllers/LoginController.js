app.controller('LoginController', [
    '$scope',
    '$location',
    '$uibModalInstance',
    'regularRefreshTokenService',
    'accessTokenService',
    'localStorageService',
    'passwordService',
    'errorService',
    function(
        $scope,
        $location,
        $uibModalInstance,
        regularRefreshTokenService,
        accessTokenService,
        localStorageService,
        passwordService,
        errorService) {

        if ($location.hash() === 'login') {
            $scope.message = "Uw account werd succesvol aangemaakt. U kan nu inloggen.";
            // delete the hash from the path
            $location.url($location.path());
        }

        $scope.close = function() {
            $uibModalInstance.dismiss("Close login modal.");
        };

        $scope.login = function() {
            var token = {
                'email': $scope.useremail,
                'password': $scope.userpass
            };

            regularRefreshTokenService.save(token).$promise.then(
                /**
                 * If the token has successfully been posted, we save the refresh token in the local storage
                 */
                function(refreshToken) {
                    localStorageService.set("refreshToken", refreshToken);
                    accessTokenService({
                        'Authorization': refreshToken.token
                    }).save(refreshToken).$promise.then(
                        /**
                         * If the token has successfully been posted, we save the access token in the local storage
                         */
                        function(accessToken) {
                            localStorageService.set("accessToken", accessToken);
                            $uibModalInstance.close();
                        },
                        /**
                         * If there is an error, we show the user the error message
                         */
                        function(error) {
                            console.error(error);
                            errorService.handleLoginError(error,$scope);
                        }
                    );
                },

                /**
                 * If there is an error, we show the user the error message
                 */
                function(error) {
                    console.error(error);
                    errorService.handleLoginError(error,$scope);
                }
            );
        };

        /**
         * Function that will send a new
         */
        $scope.sendPwd = function() {
            passwordService.save($scope.useremail).$promise.then(
                function(success) {
                    $scope.pwdSent = true;
                }
            );
        };
    }
]);
