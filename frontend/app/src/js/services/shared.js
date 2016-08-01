/**
 * This service contains dictionaries to translate the different event types and the methods to create google maps, addresses, etc.
 */
app.factory('sharedService', [
  'localStorageService',
  'eventTypeService',
  'userService',
  'userEventService',
  'travelService',
  'addressService',
  'eventService',
  'locationService',
  'locationEventService',
  'routeService',
  'routeEventService',
  'errorService',
  'accessTokenService',
  '$filter',
  '$q',
  function(
    localStorageService,
    eventTypeService,
    userService,
    userEventService,
    travelService,
    addressService,
    eventService,
    locationService,
    locationEventService,
    routeService,
    routeEventService,
    errorService,
    accessTokenService,
    $filter,
    $q) {

    var sharedservice = {};

    var eventTypes = {
      "ACCIDENT": "Ongeval",
      "JAM": "File",
      "WEATHERHAZARD": "Gevaar",
      "HAZARD": "Gevaar op de weg",
      "MISC": "Ander Type",
      "CONSTRUCTION": "Wegenwerken",
      "ROAD_CLOSED": "Weg afgesloten"
    };

    sharedservice.getTypes = function() {
      return eventTypes;
    };

    var transportTypes = [{
      name: 'Bus',
      code: 'bus'
    }, {
      name: 'Auto',
      code: 'car'
    }, {
      name: 'Fiets',
      code: 'bike'
    }, {
      name: 'Trein',
      code: 'train'
    }, {
      name: 'Tram',
      code: 'streetcar'
    }];

    sharedservice.getTransportTypes = function() {
      return transportTypes;
    };

    /**********************************************************
     ** Functions for creating/editing users, travels, routes **
     ***********************************************************/

    /**
     * Function to set the standard objects needed to create or edit a travel.
     *
     * @param    scope   The scope of the calling controller
     */
    sharedservice.initTravelReg = function(scope) {

      /**
       * We set the travel and route object for the watch function in updateDirections. If not so, we get an error because the object does not exist
       */
      scope.travel = {};
      scope.travel.startpoint = {};
      scope.travel.endpoint = {};
      scope.travel.recurring = new Array(7);
      scope.travel.is_arrival_time = true;

      scope.travel.time_interval = [];
      scope.travel.time_interval[0] = "00:00";
      scope.travel.time_interval[1] = "00:00";

      for (var i = 0; i < 7; i++) {
        scope.travel.recurring[i] = false;
      }

      scope.route = {};
      scope.route.active = true;
      scope.route.waypoints = [];
      scope.route.transportation_type = "";
      scope.route.notify_for_event_types = [];

      /**
       * The transportTypes array is used by the transportType dropdown box in the register/edit page
       */
      scope.transportTypes = sharedservice.getTransportTypes();
    };

    /**
     * Function to add all the eventTypes to the given route to be relevant
     *
     * @param    route   The route for which we need to set the eventTypes it is relevant for
     */
    sharedservice.setEventTypeRelevance = function(route) {
      /**
       * We get the transportmode of the route
       */
      var transportMode = route.transportation_type;

      /**
       * We get all the eventTypes from the server. We get a promise which we need to resolve later on
       */
      var eventTypes = eventTypeService.query({
        transportation_type: transportMode
      });

      /**
       * Create the array containing all the eventTypes that are relevant for the route
       */
      route.notify_for_event_types = [];

      /**
       * Execute the promise and process the resulting data
       */
      eventTypes.$promise.then(
        /**
         * If the eventTypes are correctly fetched from the server, we execute the following function
         */
        function(result) {
          /**
           * We check for all the eventTypes if the eventType is relevant for the transportation mode of the route. If so, we add the eventType to the route
           */
          for (var i = 0; i < result.length; i++) {
            route.notify_for_event_types.push(result[i].type);
          }
        },

        /**
         * If something went wrong when getting the data, we write the error to the console
         */
        function(error) {
          if (error.status === -1)
            console.error("Could not connect to server: Connection refused");
          else
            console.error("Error: " + error.statusText);
        }
      );
    };

    /**
     * Function to set the default options when registering a new user
     *
     * @param    scope   The scope of the registration controller
     */
    sharedservice.setDefaultUserOptions = function(user) {
      /**
       * We set the notifications by default on true
       */
      user.mute_notifications = false;
    };

    /**
     * Function to set the start and end time of a travel in the correct time format
     *
     * @param    travel     The travel for which the start and end time needs to be set
     * @param    start      The start time of the travel in a date object
     * @param    end        The end time of the travel in a date object
     */
    sharedservice.setStartEndTime = function(travel, start, end) {

      /**
       * Set the time format
       */
      var timeFormat = "HH:mm";

      /**
       * create the time interval for the travel
       */
      travel.time_interval = [];

      /**
       * Set the start and end time
       */
      travel.time_interval[0] = sharedservice.parseTime(start, timeFormat);
      travel.time_interval[1] = sharedservice.parseTime(end, timeFormat);
    };

    /**
     * Function that will parse a date object to the correct string representation
     *
     * @param    time    The time that needs to be parsed to the correct string
     * @param    format  String representation of the needed format
     */
    sharedservice.parseTime = function(time, format) {
      return $filter('date')(time, format);
    };

    /**
     * Function that will check if there are waypoints given by the user, extract them, convert them and then add them to the waypoints array
     *
     * @param    route               The route in which we need to add the possible waypoints
     * @param    directionsRenderer  The directions renderer that is currently showing the route
     */
    sharedservice.findWaypoints = function(travel, route, directionsRenderer) {
      var deferred = $q.defer();
      sharedservice.fillInWaypoints(travel, route, directionsRenderer);
      sharedservice.fillInLegs(route, directionsRenderer);
      deferred.resolve("OK");
      return deferred.promise;
    };

    /**
     * Help function to fill in the waypoints in the route and add the coordinates of the start and end point of the route
     *
     * @param    route           The route in which we need to set the waypoints
     * @param    coordinates     A promise containing the coordinates of all the waypoints, including the start- and endpoint of the route
     * @param    defer  A deferred object made in the findWaypoints method
     */
    sharedservice.fillInWaypoints = function(travel, route, directionsRenderer) {
      /**
       * We have to do everything in the callback function for synchronisation reasons
       */
      var googleRouteInfo = directionsRenderer.directions.routes[0].legs[0];
      route.waypoints = [];
      for (var i = 0; i < googleRouteInfo.via_waypoints.length; i++) {
        route.waypoints[i] = {};
        route.waypoints[i].lat = googleRouteInfo.via_waypoints[i].lat();
        route.waypoints[i].lon = googleRouteInfo.via_waypoints[i].lng();
      }
    };

    /**
     * Function to convert the waypoints given from the backend to the correct format for google maps to display them. We convert Lon to Lng
     *
     * @param  waypoints   An array containing the waypoints for a route in the format given by the server
     * @return  formatted  Array containing the waypoints in correct format
     */
    sharedservice.convertWaypoints = function(waypoints) {
      var formatted = [];

      for (var i = 0; i < waypoints.length; i++) {

        /**
         * stringify waypoint[i] and replace the string "lon" to "lng"
         */
        var stringified = JSON.stringify(waypoints[i]);
        stringified = stringified.replace('"lon"', '"lng"');

        /**
         * parse the changed string back to JSON format
         */
        var waypoint = JSON.parse(stringified);
        formatted.push({
          location: waypoint,
          stopover: false
        });
      }
      return formatted;
    };

    /**
     * Function to find the legs of a given route and add the to the route object. This is needed for the backend to be able to couple events to routes
     * @param  route the route object in which we need to store the coordinates of the legs
     * @param directionsRenderer The directionsRenderer objcet containing the route given by the user
     */
    sharedservice.fillInLegs = function(route, directionsRenderer) {
      route.full_waypoints = [];
      for (var i = 0; i < directionsRenderer.directions.routes[0].overview_path.length; i++) {
        if (i === 0) {
          route.full_waypoints.push({
            lat: directionsRenderer.directions.routes[0].overview_path[i].lat(),
            lon: directionsRenderer.directions.routes[0].overview_path[i].lng()
          });
        }
        route.full_waypoints.push({
          lat: directionsRenderer.directions.routes[0].overview_path[i].lat(),
          lon: directionsRenderer.directions.routes[0].overview_path[i].lng()
        });
      }
    };

    /**
     * Simple function to add a new poi to a user
     *
     * @param    user        The user we need to add the new POI to
     * @param    POI         The POI object containing all the fields except the address
     * @param    address     An address object as given by Google autocomplete
     */
    sharedservice.createPOI = function(POI, address) {
      if (addressService.validateAddress(address)) {
        /**
         * create the address object and fill it in
         */
        POI.address = {};
        addressService.setAddress(POI.address, address);

        /**
         * Add the POI to the list of POI
         */
        return POI;
      }
    };

    /**
     * Function to post the user to the server and check whether the post was successfull or not.
     *
     * @param    scope   The scope of the calling controller. Needed to set the error messages in the webpages
     * @param    user    The user object that needs to be posted to the server
     */
    sharedservice.postUser = function(scope, user) {
      userService({}).save(user).$promise.then(
        function(result) {
          scope.currentUser = result;
          scope.result = true;
        },
        function(error) {
          errorService.handleUserError(error, scope);
          scope.result = false;
        }
      );
    };

    /*
     * Function that processes an array of events (result) to create markers to show on a map
     */
    processEvents = function(scope, result, dropAnimation) {
      /**
       * get the refreshToken to see if the user is admin or operator
       */
      var token = localStorageService.get("refreshToken");
      /**
       * Make sure all previous markers are removed from the map
       */
      if (scope.markers !== undefined) {
        for (var k = 0; k < scope.markers.length; k++) {
          scope.markers[k].setMap(null);
        }
      }
      /**
       * Make sure all previous polylines (jams) are removed from the map
       */
      if (scope.polylines !== undefined) {
        for (var l = 0; l < scope.polylines.length; l++) {
          scope.polylines[l].setMap(null);
        }
      }
      /*
       * Array containing the coordinates and addresses of the events.
       */
      scope.addresses = [];
      var res = [];
      angular.forEach(result, function(value, key) {
        res.push(result[key].coordinates);
        scope.addresses.push(result[key].formatted_address);
      });

      /**
       * Array that will contain the markers of all the events
       * We use this array to be able to show the infowindow when clicking an event in the event list
       */
      scope.markers = [];
      /**
       * The jaminfo array will contain objects containing all the info for a specific jam at this index
       * The jams array will contain all the polylines that show a jam on the map.
       * We need these 2 arrays to be able to correctly add info windows to the jams
       */
      var jaminfo = [];
      var jams = [];
      var jamEvents = [];
      var infoWindow = new google.maps.InfoWindow();

      for (var i = 0; i < res.length; i++) {
        var marker = {};

        /**
         * If an event has a jam, we need to do some special things
         */
        if (result[i].jams.length !== 0) {

          for (var j = 0; j < result[i].jams.length; j++) {

            /**
             * Create custom marker showing the beginning of the jam
             * The position of this marker will be taken to show the infowindow when hovering the jam or clicking the jam in the events list
             */
            var marker1 = new google.maps.Marker({
              map: scope.map,
              position: new google.maps.LatLng(result[i].jams[j].line[0].lat, result[i].jams[j].line[0].lon),
              title: result[i].description,
              visible: false
            });

            /**
             * Add all the info of the current traffic jam in an object and push it in the jaminfo array
             * We will use this info later on when the info windows are added to the jams
             */
            jaminfo.push({
              marker: marker1,
              description: result[i].description,
              address: result[i].formatted_address,
              type: eventTypes[result[i].type.type]
            });

            /**
             * Push the jamm in the jam array. We will give this array as argument to the processJams function which will show the jams and add info windows
             */
            jams.push(result[i].jams[j]);
            jamEvents.push(result[i]);

            /**
             * Add the marker of the jam to the markers array so the info window can be opened when the user clicks on an event in the list
             */
            scope.markers.push(marker1);
          }
        }

        /**
         * If the event contains no jams, we only show 1 marker
         */
        else {
          var image = {
            url: 'https://developers.google.com/maps/documentation/javascript/examples/full/images/beachflag.png',
            // This marker is 20 pixels wide by 32 pixels high.
            size: new google.maps.Size(20, 32),
            // The origin for this image is (0, 0).
            origin: new google.maps.Point(0, 0),
            // The anchor for this image is the base of the flagpole at (0, 32).
            anchor: new google.maps.Point(0, 32)
          };

          if (dropAnimation === true) {
            marker = new google.maps.Marker({
              map: scope.map,
              position: new google.maps.LatLng(res[i].lat, res[i].lon),
              title: result[i].description,
              icon: image,
              animation: google.maps.Animation.DROP
            });
          } else {
            marker = new google.maps.Marker({
              map: scope.map,
              position: new google.maps.LatLng(res[i].lat, res[i].lon),
              title: result[i].description,
              icon: image
            });
          }

          //Attach click event to the marker.
          (function(marker, description, address, type) {
            google.maps.event.addListener(marker, "mouseover", function(e) {
              infoWindow.setContent("<div style = 'width:200px;min-height:40px'> <p> " + description + "</p>Adres: " + address + "</div>");
              infoWindow.open(scope.map, marker);
            });
          })(marker, result[i].description, scope.addresses[i], eventTypes[result[i].type.type]);


          if (token !== null) {
            if (token.role === "operator" || token.role === "administrator") {
              var currentEvent = result[i];
              var currentId = currentEvent.id;
              //Attach click event to the marker.
              (function(marker, currentEvent, $scope, currentId) {
                google.maps.event.addListener(marker, "dblclick", function(e) {
                  scope.openEditEvent(currentEvent);
                });
                google.maps.event.addListener(marker, "rightclick", function(e) {
                  scope.confirmAndDelete(currentId);
                });
              })(marker, currentEvent, scope, currentId);
            }
          }
          /**
           * Again, push the marker so the info window can be opened when the event is selected in the list
           */
          scope.markers.push(marker);
        }
      }

      /**
       * Earlier on, we seperated all the jams in a seperate array.
       * Now, we call the function that will show these jams on the map and add the correct info windows
       */
      processJams(scope, jaminfo, jams, infoWindow, jamEvents);
    };

    /**
     * Function that gets all the events to show on the login page
     */
    sharedservice.loadAllEventsAndShow = function(scope) {
      /*
       * Execute verything in the promise for synchronisation reasons.
       */
      eventService({}).getRecentEvents().$promise.then(function(result) {
        scope.map = addressService.initMap('login-map');
        processEvents(scope, result, false);
      });
    };

    /**
     * Function that gets all the events that are relevant to a specific user to show on the user index page
     */
    sharedservice.loadUserEventsAndShow = function(scope, userId) {
      var header = {
        'Authorization': localStorageService.get("accessToken").token
      };
      /*
       * If the user is an operator or an admin, we show them all the events
       */
      if (localStorageService.get("refreshToken").role === "operator" || localStorageService.get("refreshToken").role === "administrator") {
        eventService({}).getRecentEvents().$promise.then(function(result) {
          scope.events = result;
          scope.map = addressService.initMap('map-canvas');
          processEvents(scope, result, false);
        });
      } else {
        userEventService(header).getEvents({
          userId: userId
        }).$promise.then(function(result) {
          scope.events = result;
          scope.map = addressService.initMap('map-canvas');
          processEvents(scope, result, true);
        }, function(error) {
          if (error.status === 401) {
            sharedservice.renewToken();
            sharedservice.loadUserEventsAndShow(scope, userId);
          }
        });
      }
    };

    /**
     * Function that gets all the events that are relevant to a specific location of a user
     */
    sharedservice.loadLocationEventsAndShow = function(scope, userId, locationId) {
      var header = {
        'Authorization': localStorageService.get("accessToken").token
      };
      /*
       * Execute verything in the promise for synchronisation reasons.
       */
      locationEventService(header).getEvents({
        userId: userId,
        locationId: locationId
      }).$promise.then(function(result) {
        processEvents(scope, result, true);
      }, function(error) {
        if (error.status === 401) {
          sharedservice.renewToken();
          sharedservice.loadLocationEventsAndShow(scope, userId, locationId);
        }
      });
    };

    /**
     * Function that gets all the events that are relevant to a specific travel/route of a user
     */
    sharedservice.loadRouteEventsAndShow = function(scope, userId, travelId, routeId) {
      var header = {
        'Authorization': localStorageService.get("accessToken").token
      };
      /*
       * Execute verything in the promise for synchronisation reasons.
       */
      routeEventService(header).getEvents({
        userId: userId,
        travelId: travelId,
        routeId: routeId
      }).$promise.then(function(result) {
        processEvents(scope, result, true);
      }, function(error) {
        if (error.status === 401) {
          sharedservice.renewToken();
          sharedservice.loadRouteEventsAndShow(scope, userId, travelId, routeId);
        }
      });
    };

    /**
     * Private function in the service to create the polylines for all the jams that are present
     * This function will on it's turn call the addJamInfoWindow function which will add an info window to the newly created polyline that represents a jam
     */
    var processJams = function(scope, jaminfo, jams, infoWindow, jamEvents) {
      var token = localStorageService.get("refreshToken");
      /**
       * Watch the polylines array. If all the polylines of all the jams are made, we can add the info windows
       */
      scope.$watchCollection('polylines', function() {
        if (scope.polylines.length === jams.length) {
          for (i = 0; i < jams.length; i++) {
            createJamInfoWindow(infoWindow, scope.map, scope.polylines[i], jaminfo[i].marker, jaminfo[i].description, jaminfo[i].address, jaminfo[i].type);
            if (token !== null && (token.role === "administrator" || token.role === "operator")) {
              createJamEditListener(scope, scope.polylines[i], jaminfo[i].marker, jamEvents[i]);
              createJamDeleteListener(scope, scope.polylines[i], jaminfo[i].marker, jamEvents[i].id);
            }
          }
        }
      }, true);

      scope.polylines = [];

      /**
       * Create the polylines representing the jams on the map
       */
      for (i = 0; i < jams.length; i++) {
        createPolyline(scope, jams[i]);
      }

    };

    /**
     * Function that will create the polyline for showing a traffic jam on the map
     * The function will also add the polyline to the polylines array of the scope so we later can add listeners to the line
     */
    var createPolyline = function(scope, jam) {
      var coordinates = [];
      for (var k = 0; k < jam.line.length; k++) {
        var coord = {
          lat: jam.line[k].lat,
          lng: jam.line[k].lon
        };
        coordinates.push(coord);
      }

      var polyline = new google.maps.Polyline({
        path: coordinates,
        strokeColor: '#FF0000',
        strokeOpacity: 0.6,
        strokeWeight: 5,
        zIndex: google.maps.Marker.MAX_ZINDEX
      });

      polyline.setMap(scope.map);
      scope.polylines.push(polyline);
    };

    /**
     * Create an info window for the given line and marker
     * We give the infoWindow als an argument so only 1 window  at a time is open on the map
     * All the other info in the info window is extracted in the calling function
     */
    var createJamInfoWindow = function(InfoWindow, map, line, marker, description, address, type) {

      (function(infoWindow, map, line, marker, description, address, type) {
        google.maps.event.addListener(line, "mouseover", function(e) {
          infoWindow.setContent("<div style = 'width:200px;min-height:40px'> <p> " + description + "</p>Adres: " + address + "</div>");
          infoWindow.open(map, marker);
        });
        google.maps.event.addListener(marker, "mouseover", function(e) {
          infoWindow.setContent("<div style = 'width:200px;min-height:40px'> <p> " + description + "</p>Adres: " + address + "</div>");
          infoWindow.open(map, marker);
        });

      })(InfoWindow, map, line, marker, description, address, type);
    };

    /**
     * Function to open the edit event modal when double clicking on a jam line
     */
    var createJamEditListener = function(scope, line, marker, event) {
      (function($scope, line, marker, currentEvent) {
        google.maps.event.addListener(line, "dblclick", function(e) {
          $scope.openEditEvent(currentEvent);
        });
        google.maps.event.addListener(marker, "dblclick", function(e) {
          $scope.openEditEvent(currentEvent);
        });

      })(scope, line, marker, event);

    };

    var createJamDeleteListener = function(scope, line, marker, id) {
      (function($scope, line, marker, id) {
        google.maps.event.addListener(line, "rightclick", function(e) {
          $scope.confirmAndDelete(id);
        });
        google.maps.event.addListener(marker, "rightclick", function(e) {
          $scope.confirmAndDelete(id);
        });

      })(scope, line, marker, id);
    };

    sharedservice.loadData = function(scope) {
      var refreshToken = localStorageService.get("refreshToken");
      var accessToken = localStorageService.get("accessToken");
      var header = {
        'Authorization': accessToken.token
      };

      userService(header).get({
        u: refreshToken.user_id
      }).$promise.then(function(result) {
        scope.currentUser = result;

        /**
         * Load all the travels of the user in currentUserTravels array
         */
        travelService(header).getTravels({
          u: scope.currentUser.id
        }).$promise.then(function(value) {
          scope.currentUserTravels = value;

          if (scope.currentUserTravels.length !== 0) {
            /**
             * Set first travel as default travel
             */
            scope.travelId = 0;

            /**
             * Get all the routes that are relevant to the current travel
             */
            routeService(header).getRoutes({
              u: scope.currentUser.id,
              travelId: scope.currentUserTravels[0].id
            }).$promise.then(function(data) {
              scope.currentTravelRoutes = data;
              if (scope.currentTravelRoutes.length !== 0) {
                scope.waypoints = [];

                /**
                 * Set first route as default route
                 */
                scope.routeId = 0;

                for (var j = 0; j < scope.currentTravelRoutes[0].waypoints.length; j++) {
                  /**
                   * Stringify waypoint[i] and replace the string "lon" to "lng"
                   */
                  var stringified = JSON.stringify(scope.currentTravelRoutes[0].waypoints[j]);
                  stringified = stringified.replace('"lon"', '"lng"');

                  /**
                   * Parse the changed string back to JSON format
                   */
                  var waypoint = JSON.parse(stringified);
                  scope.waypoints[j] = {
                    location: waypoint,
                    stopover: false
                  };
                }
              }
            }, function(error) {
              if (error.status === 401) {
                sharedservice.renewToken();
                sharedservice.loadData(scope);
              }
            });
          }
        }, function(error) {
          if (error.status === 401) {
            sharedservice.renewToken();
            sharedservice.loadData(scope);
          }
        });

        /**
         * Retreive all the locations of the current user
         */
        locationService(header).getLocations({
          userId: scope.currentUser.id
        }).$promise.then(
          function(value) {
            scope.currentUserLocations = value;

            if (scope.currentUserLocations.length !== 0) {
              scope.currentUserLocation = scope.currentUserLocations[0];

              /**
               * Set location id variable so we can check which location is currently selected in the rest of the controller
               */
              scope.locationId = 0;
            }
          },
          function(error) {
            if (error.status === 401) {
              sharedservice.renewToken();
              sharedservice.loadData(scope);
            }
          }
        );
      }, function(error) {
        if (error.status === 401) {
          sharedservice.renewToken();
          sharedservice.loadData(scope);
        }
      });
    };

    sharedservice.renewToken = function() {
      var deferred = $q.defer();
      var refreshToken = localStorageService.get("refreshToken");
      var accessToken = localStorageService.get("accessToken");
      var exp = new Date($filter('date')(new Date(accessToken.exp), "yyyy-MM-dd'T'HH:mm"));
      /**
       * We make sure the token hasn't been expired by substracting the time by 10 minutes (1000*60*10 milliseconds)
       */
      var now = new Date($filter('date')(new Date(), "yyyy-MM-dd'T'HH:mm"));

      /**
       * If the expiration time is before or equal to the current time, we make a request for a new access token.
       */
      if (exp <= now && accessToken !== null) {
        accessTokenService({
          'Authorization': refreshToken.token
        }).save(refreshToken).$promise.then(
          /**
           * If the token has successfully been posted, we save the new access token in the local storage
           */
          function(accessToken) {
            localStorageService.set("accessToken", accessToken);
            console.log("ok");
          },
          /**
           * If there is an error, we show the user the error message
           */
          function(error) {
            console.error(error);
          }
        );
      }
      return deferred.promise;
    };

    return sharedservice;
  }
]);
