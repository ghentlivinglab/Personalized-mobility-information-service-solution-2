app.factory('eventControlService', [
  '$route',
  '$uibModal',
  'eventService',
  'addressService',
  'sharedService',
  'localStorageService',
  function(
    $route,
    $uibModal,
    eventService,
    addressService,
    sharedService,
    localStorageService) {

      var service = {};

      service.openNewEvent = function() {
        var modalInstance = $uibModal.open({
          templateUrl: 'js/directives/newEventDirective.html',
          controller: 'NewEventController',
          size: 'lg'
        });

        /**
        * We check the result when the modal is closed and check for errors
        */
        modalInstance.result.then(
          /**
          * Success handeling function
          */
          function() {
            $route.reload();
          },

          /**
          * Error handeling function
          */
          function(error) {
            console.error(error);
          }
        );

        /**
         * Only when the modal is rendered, we can render the map. Otherwise the map won't show.
         * When the modal has successfully been rendered, we initialize the map and add it to the scope of the modal
         */
        modalInstance.rendered.then(
          function() {
            modalInstance.map = addressService.initMap('map-canvas4');
          }
        );
      };

      service.openEditEvent = function(event) {
        var modalInstance = $uibModal.open({
          templateUrl: 'js/directives/editEventDirective.html',
          controller: 'EditEventController',
          resolve: {
            event: function() {
              return event;
            }
          },
          size: 'lg'
        });

        /**
        * We check the result when the modal is closed and check for errors
        */
        modalInstance.result.then(
          /**
          * Success handeling function
          */
          function() {
            $route.reload();
          },

          /**
          * Error handeling function
          */
          function(error) {
            console.error(error);
          }
        );

        /**
         * Only when the modal is rendered, we can render the map. Otherwise the map won't show.
         * When the modal has successfully been rendered, we initialize the map and add it to the scope of the modal
         */
        modalInstance.rendered.then(
          function() {
            modalInstance.map = addressService.initMap('map-canvas4');
          }
        );
      };

      service.deleteEvent = function(eventId) {
        eventService({'Authorization': localStorageService.get("accessToken").token}).remove({eventId: eventId}).$promise.then(
          function() {
            $route.reload();
          }, function(error) {
            if (error.status === 401) {
              sharedService.renewToken();
              service.deleteEvent(eventId);
            } else {
              console.error(error);
            }
          }
        );
      };

      return service;
}]);
