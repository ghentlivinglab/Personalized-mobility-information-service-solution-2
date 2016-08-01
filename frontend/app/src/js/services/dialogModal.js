/*
 Open a modal confirmation dialog window with the UI Bootstrap uibModal service.
 This is a basic modal that can display a message with okay or cancel buttons.
 It returns a promise that is resolved or rejected based on okay/cancel clicks.
 The following settings can be passed:

    message         the message to pass to the modal body
    title           (optional) title for modal window
    okButton        text for OK button. set false to not include button
    cancelButton    text for Cancel button. ste false to not include button

*/
app.factory('dialogModal', ['$uibModal', function($uibModal) {
    return function (message, title, okButton, cancelButton) {
        // setup default values for buttons
        // if a button value is set to false, then that button won't be included
        okButton = okButton === false ? false : (okButton || 'Bevestigen');
        cancelButton = cancelButton === false ? false : (cancelButton || 'Annuleren');

        // open modal and return the instance (which will resolve the promise on ok/cancel clicks)
        var modalInstance = $uibModal.open({
            templateUrl: 'js/directives/modalInstanceDirective.html',
            controller: 'ModalInstanceController',
            resolve: {
                settings: function() {
                    return {
                        modalTitle: title,
                        modalBody: message,
                        okButton: okButton,
                        cancelButton: cancelButton
                    };
                }
            }
        });
        // return the modal instance
        return modalInstance;
    };
}]);
