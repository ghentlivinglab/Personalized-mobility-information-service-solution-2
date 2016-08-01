/*
 * This service is responsible for all communication with the server regarding access tokens.
 * Only a post is necessary for this endpoint.
*/
app.factory('accessTokenService', ['$resource', function($resource) {
  return function(customHeaders) {
    // return $resource('https://vopro6.ugent.be/api/access_token', {},
    // return $resource('http://server-thuis.no-ip.biz:8080/access_token', {},
    return $resource('http://localhost:8080/access_token', {},
    {
        save: {
            method: 'POST',
            headers: customHeaders || {}
        }
    });
  };
}]);
