/*
 * This service is responsible for all communication with the server regarding refresh tokens.
 * Only a post is necessary for this endpoint.
*/

app.factory('regularRefreshTokenService', ['$resource', function($resource) {
    // return $resource('https://vopro6.ugent.be/api/refresh_token/regular');
    // return $resource('http://server-thuis.no-ip.biz:8080/refresh_token/regular');
    return $resource('http://localhost:8080/refresh_token/regular');
}]);
