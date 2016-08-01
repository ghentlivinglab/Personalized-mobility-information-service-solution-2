/**
 * This controller will handle all the logic in reference to the admin user.
 */
app.controller('AdminController', ['$scope', '$route', '$location', '$timeout', 'localStorageService', 'sharedService', 'userService', 'adminService', 'adminDeleteUserService', 'operatorService', 'dataDumpService',
  function($scope, $route, $location, $timeout, localStorageService, sharedService, userService, adminService, adminDeleteUserService, operatorService, dataDumpService) {

    var header;

    /**
     * Function to (re)load the data from the admins and operators
     */
    $scope.reloadData = function() {
      adminService(header).getAdmins().$promise.then(function(result) {
        $scope.admins = result;
      }, function(error) {
        if (error.status === 401) {
          sharedService.renewToken();
          $scope.reloadData();
        } else {
          console.error(error);
        }
      });

      operatorService(header).getOperators().$promise.then(function(result) {
        $scope.operators = result;
      }, function(error) {
        if (error.status === 401) {
          sharedService.renewToken();
          $scope.reloadData();
        } else {
          console.error(error);
        }
      });
    };

    $scope.initPage = function() {
      /**
       * If the user is not logged in or is not an admin, he/she had no rights to go to this page,
       * so we redirect to their user index page
       */
      if (localStorageService.get("refreshToken") === null || localStorageService.get("refreshToken").role !== 'administrator') {
        $location.path('/userindex');
      }

      $scope.currentPage = $route.current.templateUrl;

      header = {'Authorization': localStorageService.get("accessToken").token};

      $scope.templates = [
        { name: "userList", url: "view/admin/userList.html"},
        { name: "operatorList", url: "view/admin/operatorList.html"}
      ];

      /**
       * Set the initial view to the userlist.
       */
      $scope.template = $scope.templates[0];

      /**
       * Because we cannot let the current admin remove his/her own admin rights,
       * we need the current admin user. (There has always to be at least one admin)
       */
      userService(header).get({u: localStorageService.get("refreshToken").user_id}).$promise.then(function(result) {
        $scope.currentUser = result;
      }, function(error) {
        if (error.status === 401) {
          sharedService.renewToken();
          $scope.initPage();
        } else {
          console.error(error);
        }
      });

      /**
       * Get all the users to show a userlist to the admin
       */
      userService(header).getUsers().$promise.then(function(result) {
        $scope.users = result;
      }, function(error) {
        if (error.status === 401) {
          sharedService.renewToken();
          $scope.initPage();
        } else {
          console.error(error);
        }
      });

      dataDumpService(header).getDataDump().$promise.then(function(result) {
        $scope.dataDump = result;
      }, function(error) {
        if (error.status === 401) {
          sharedService.renewToken();
          $scope.initPage();
        } else {
          console.error(error);
        }
      });

      /**
       * Load all admins and all the operators
       */
      $scope.reloadData();
    };

    $scope.initPage();

    /**
     * Funtion to change the role of an existing user to admin
     */
    $scope.makeAdmin = function(email) {
      adminService(header).save(email).$promise.then(
        function(result) {
          $scope.reloadData();
          $location.path();
          /**
           * Show success alert message for 2 seconds
           */
          $scope.message = email + " werd succesvol toegevoegd als admin";
          $timeout(function() {
            $scope.message = "";
          }, 2000);
        },
        function(error) {
          if (error.status === 401) {
            sharedService.renewToken();
            $scope.makeAdmin(email);
          } else {
            console.error(error);
          }
        }
      );
    };

    /**
     * Funtion to change the role of an existing user to operator
     */
    $scope.makeOperator = function(email) {
      operatorService(header).save(email).$promise.then(
        function(result) {
          $scope.reloadData();
          $location.path();
          /**
           * Show success alert message for 2 seconds
           */
          $scope.message = email + " werd succesvol toegevoegd als operator";
          $timeout(function() {
            $scope.message = "";
          }, 2000);
        },
        function(error) {
          if (error.status === 401) {
            sharedService.renewToken();
            $scope.makeOperator(email);
          } else {
            console.error(error);
          }
        }
      );
    };

    /**
     * Function for the admin to delete a user
     */
    $scope.deleteUser = function(userId) {
      adminDeleteUserService(header).remove({u: userId}).$promise.then(
        function(result) {
          $route.reload();
        },
        function(error) {
          if (error.status === 401) {
            sharedService.renewToken();
            $scope.deleteUser(userId);
          } else {
            console.error(error);
          }
        }
      );
    };

    /**
     * Function to check if a userId belongs to the admin list
     */
    $scope.isAdmin = function(userId) {
      if ($scope.admins !== undefined) {
        for (i = 0; i < $scope.admins.length; i++) {
          if ($scope.admins[i].id === userId) {
            return true;
          }
        }
        return false;
      }
    };

    /**
     * Function to check if a userId belongs to the operator list
     */
    $scope.isOperator = function(userId) {
      if ($scope.operators !== undefined) {
        for (var i = 0; i < $scope.operators.length; i++) {
          if ($scope.operators[i].id === userId) {
            return true;
          }
        }
        return false;
      }
    };

    /**
     * Function that is called when the admin wants to download a datadump
     */
    $scope.clickDownload = function(jsonDump, filename) {
      var link = document.createElement('a');
      link.href = 'data:' + "text/json;charset=utf-8," + encodeURIComponent(JSON.stringify(jsonDump));
      link.target = '_blank';
      link.download = filename;
      link.click();
    };

    $scope.downloadDataDump = function() {
      $scope.clickDownload($scope.dataDump, "dataDump.json");
    };

    $scope.downloadUsersDump = function() {
      $scope.clickDownload($scope.dataDump.users, "userDataDump.json");
    };

    $scope.downloadTravelsDump = function() {
      $scope.clickDownload($scope.dataDump.travels, "travelDataDump.json");
    };

    $scope.downloadPointOfInterestsDump = function() {
      $scope.clickDownload($scope.dataDump.point_of_interests, "pointOfInterestsDataDump.json");
    };

    $scope.downloadEventsDump = function() {
      $scope.clickDownload($scope.dataDump.events, "eventsDataDump.json");
    };

    $scope.downloadEventTypesDump = function() {
      $scope.clickDownload($scope.dataDump.eventtypes, "eventTypesDataDump.json");
    };

    /**
     * Function to change the role of an admin to user (the user keeps existing)
     */
    $scope.deleteAdmin = function(email) {
      adminService(header).remove(email).$promise.then(function(result) {
        $scope.reloadData();
        $location.path();
      }, function(error) {
        if (error.status === 401) {
          sharedService.renewToken();
          $scope.deleteAdmin(email);
        } else {
          console.error(error);
        }
      });
    };

    /**
     * Function to change the role of an operator to user (the user keeps existing)
     */
    $scope.deleteOperator = function(email) {
      operatorService(header).remove(email).$promise.then(function(result) {
        $scope.reloadData();
        $location.path();
      }, function(error) {
        if (error.status === 401) {
          sharedService.renewToken();
          $scope.deleteOperator(email);
        } else {
          console.error(error);
        }
      });
    };

  }
]);
