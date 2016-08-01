/*
 * This service is responsible for all communication with the server regarding events.
 * It contains all methods to get, post, put or delete events.
*/
app.factory('eventService', ['$resource', function($resource) {
    return function(customHeaders) {
        // return $resource('https://vopro6.ugent.be/api/event/:eventId', {eventId: '@id'},
        // return $resource('http://server-thuis.no-ip.biz:8080/event/:eventId', {eventId: '@id'},
        return $resource('http://localhost:8080/event/:eventId', {eventId: '@id'},
        {
            getRecentEvents: {
                method: 'GET',
                isArray: true,
                params: {
                    recent: true
                }
            },
            update: {
                method: 'PUT',
                headers: customHeaders || {}
            },
            save: {
                method: 'POST',
                headers: customHeaders || {}
            },
            remove: {
                method: "DELETE",
                headers: customHeaders || {}
            }
        });
    };
}]);
