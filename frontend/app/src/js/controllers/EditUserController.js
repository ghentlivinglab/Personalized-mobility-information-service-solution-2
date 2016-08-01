app.controller('EditUserController', [
    '$scope',
    '$route',
    '$uibModalInstance',
    '$timeout',
    'user',
    'sharedService',
    'userService',
    'errorService',
    'changeEmailService',
    'changePasswordService',
    'localStorageService',
    'accessTokenService',
    function(
        $scope,
        $route,
        $uibModalInstance,
        $timeout,
        user,
        sharedService,
        userService,
        errorService,
        changeEmailService,
        changePasswordService,
        localStorageService,
        accessTokenService) {

        $scope.user = user;
        $scope.oldUser = angular.copy(user);
        $scope.oldEmail = angular.copy(user.email);
        $scope.newEmail = angular.copy(user.email);
        $scope.oldPassword = "";
        $scope.newPassword = "";
        $scope.newPasswordAgain = "";
        var header = { 'Authorization': localStorageService.get('accessToken').token };

        $scope.countries = [{
            name: 'België',
            code: 'BE'
        }, {
            name: 'Nederland',
            code: 'NL'
        }, {
            name: 'Frankrijk',
            code: 'FR'
        }, {
            name: 'Luxemburg',
            code: 'LU'
        }];

        $scope.close = function() {
            $uibModalInstance.dismiss("Closed edit user modal");
            $route.reload();
        };

        $scope.opslaan1 = function(updatedUser) {
            header = {
                'Authorization': localStorageService.get("accessToken").token
            };
            userService(header).update(updatedUser).$promise.then(
                function(result) {
                    /**
                     * If first or last name have been changed, we show success message for 2 seconds
                     */
                    if ($scope.oldUser.first_name !== result.first_name || $scope.oldUser.last_name !== result.last_name) {
                        $scope.oldUser = result;
                        $scope.successMessage1 = "Voornaam/achternaam succesvol gewijzigd.";
                        $timeout(function() {
                            $scope.successMessage1 = "";
                        }, 2000);
                    }
                },
                function(error) {
                    if (error.status === 401) {
                        sharedService.renewToken();
                        $scope.opslaan1(updatedUser);
                    } else {
                        console.error(error);
                    }
                }
            );

        };

        $scope.opslaan2 = function(user) {
            header = {
                'Authorization': localStorageService.get("accessToken").token
            };
            /**
             * Check if the email has been changed
             */
            if ($scope.oldEmail !== $scope.newEmail) {
                var emailBody = {
                    'old_email': $scope.oldEmail,
                    'new_email': $scope.newEmail
                };
                changeEmailService(header).save({
                    u: user.id
                }, emailBody).$promise.then(function(refreshToken) {
                    localStorageService.set("refreshToken", refreshToken);
                    accessTokenService({
                        'Authorization': refreshToken.token
                    }).save(refreshToken).$promise.then(
                        function(accessToken) {
                            localStorageService.set("accessToken", accessToken);
                            $scope.oldEmail = $scope.newEmail;
                            $scope.user.email = $scope.newEmail;
                            $scope.userAlreadyExists = "";
                            /**
                             * Show success message for 2 seconds
                             */
                            $scope.successMessage2 = "E-mail succesvol gewijzigd.";
                            $timeout(function() {
                                $scope.successMessage2 = "";
                            }, 2000);
                        },
                        // Error handling for accessTokenService
                        function(error) {
                            if (error.status === 401) {
                                sharedService.renewToken();
                                $scope.opslaan2(user);
                            } else {
                                console.error(error);
                            }
                        }
                    );
                }, function(error) {
                    if (error.status === 401) {
                        sharedService.renewToken();
                        $scope.opslaan2(user);
                    } else {
                        $scope.userAlreadyExists = "Het email adres is al in gebruik. Gelieve in te loggen of een ander email adres te kiezen.";
                        console.error(error);
                    }
                });
            }
        };

        $scope.opslaan3 = function(user) {
            header = {
                'Authorization': localStorageService.get("accessToken").token
            };
            if ($scope.oldPassword !== "" && $scope.newPassword !== "" && $scope.oldPassword !== $scope.newPassword) {
                    var passwordBody = {
                        'old_password': $scope.oldPassword,
                        'new_password': $scope.newPassword
                    };
                    changePasswordService(header).save({
                        u: user.id
                    }, passwordBody).$promise.then(function(refreshToken) {
                        localStorageService.set("refreshToken", refreshToken);
                        accessTokenService({
                            'Authorization': refreshToken.token
                        }).save(refreshToken).$promise.then(
                            function(accessToken) {
                                localStorageService.set("accessToken", accessToken);
                                $scope.oldPassword = "";
                                $scope.newPassword = "";
                                $scope.newPasswordAgain = "";
                                /**
                                 * Show success message for 2 seconds
                                 */
                                $scope.successMessage3 = "Wachtwoord succesvol gewijzigd.";
                                $scope.passwordError = "";
                                $timeout(function() {
                                    $scope.successMessage3 = "";
                                }, 2000);
                                if ($scope.registration !== undefined) {
                                    $scope.registration.$setPristine();
                                }
                            },
                            // Error handling for accessTokenService
                            function(error) {
                                if (error.status === 401) {
                                    sharedService.renewToken();
                                    $scope.opslaan3(user);
                                } else {
                                    console.error(error);
                                }
                            }
                        );
                    }, function(error) {
                        if (error.status === 401) {
                            sharedService.renewToken();
                            $scope.opslaan3(user);
                        } else {
                            errorService.handleChangePasswordError(error, $scope);
                            console.error(error);
                        }
                    });
                  }
        };

        $scope.validatePassword = function(passw) {
            var strongRegex = new RegExp("^(?=.*[a-z])(?=.*[A-Z])(?=.*[0-9])(?=.{8,})");
            return strongRegex.test(passw);
        };

    }
]);
