-- account
insert into account (accountid, email, first_name, last_name, salt, password, mute_notifications, email_validated)
	values (1, 'phenderson0@biglobe.ne.jp', 'Peter', 'Henderson', E'\\x46479a41dc9e35b7366733f0021028f17a309ff65702bcb20a95815e7e8d60f3', '6847e74879103c8df1f1bd01ffd1bc375887a854b5b9d7057b7eb73d3381860a', false, false);
insert into account (accountid, email, first_name, last_name, salt, password, mute_notifications, email_validated)
	values (2, 'mpeterson1@cyberchimps.com', 'Melissa', 'Peterson', E'\\x0d85e180f73bd64c32892d7f6edd5455d50ecc98d1b171283f5e58173840f22d', 'bd26022cc0d2bf5d09b1b93ef5dbc135819f9f301ef89cfe55222483de49f5e4', true, true);
ALTER SEQUENCE accountid_seq RESTART WITH 3;

-- city
insert into city (cityid, city, postal_code, country)
	values (1, 'Gent', '9000', 'BE');
insert into city (cityid, city, postal_code, country)
	values (2, 'Dendermonde', '9200', 'BE');
insert into city (cityid, city, postal_code, country)
	values (3, 'Antwerpen', '2000', 'BE');
ALTER SEQUENCE cityid_seq RESTART WITH 4;

-- street
insert into street (streetid, cityid, name)
	values (1, 1, 'Jozef Plateaustraat');
insert into street (streetid, cityid, name)
	values (2, 1, 'Kattenberg');
insert into street (streetid, cityid, name)
	values (3, 1, 'Galglaan');
insert into street (streetid, cityid, name)
	values (4, 2, 'Serwouterstraat');
insert into street (streetid, cityid, name)
	values (5, 3, 'Meir');
ALTER SEQUENCE streetid_seq RESTART WITH 6;

-- address
insert into address (addressid, streetid, housenumber, latitude, longitude, cartesianX, cartesianY)
	values (1, 1, '1', 51.046154, 3.722360, 254.66443231896088, -824.0232963758901);
insert into address (addressid, streetid, housenumber, latitude, longitude, cartesianX, cartesianY)
	values (2, 2, '2', 51.042541, 3.723728, 350.584295593268, -1225.9668861972777);
insert into address (addressid, streetid, housenumber, latitude, longitude, cartesianX, cartesianY)
	values (3, 3, '3', 51.028102, 3.718671, -3.996660969761499, -2832.295005054164);
insert into address (addressid, streetid, housenumber, latitude, longitude, cartesianX, cartesianY)
	values (4, 4, '4', 51.020986, 4.119796, 28121.628453000758, -3623.94476568699);
insert into address (addressid, streetid, housenumber, latitude, longitude, cartesianX, cartesianY)
	values (5, 5, '5', 51.218347, 4.405376, 48145.60108010631, 18332.321171403513);
ALTER SEQUENCE addressid_seq RESTART WITH 6;

-- eventtype
insert into eventtype (eventtypeid, type, relevant_transportation_types)
	values (1, 'JAM', '{"car", "bus"}');
insert into eventtype (eventtypeid, type, relevant_transportation_types)
	values (2, 'SNOW', '{"car", "bus", "bike"}');
insert into eventtype (eventtypeid, type, relevant_transportation_types)
	values (3, 'STRIKE', '{"streetcar", "bus"}');
insert into eventtype (eventtypeid, type, relevant_transportation_types)
	values (4, 'ROAD_BLOCKED', '{"car"}');
ALTER SEQUENCE eventtypeid_seq RESTART WITH 5;

-- location
insert into location (locationid, accountid, addressid, name, radius, active, notify_email, notify_cell)
	values (1, 1, 1, 'home', 5000, true, true, false);
insert into location (locationid, accountid, addressid, name, radius, active, notify_email, notify_cell)
	values (2, 1, 5, 'work', 1000, true, false, false);
insert into location (locationid, accountid, addressid, name, radius, active, notify_email, notify_cell)
	values (3, 2, 3, 'de sterre', 2000, true, true, true);
ALTER SEQUENCE locationid_seq RESTART WITH 4;

-- travel
insert into travel (travelid, accountid, startpoint, endpoint, name, begin_time, end_time, is_arrival_time, mon, tue, wed, thu, fri, sat, sun)
	values (1, 1, 1, 5, 'home->work', '08:00', '09:00', false, true, true, true, true, true, false, false);
insert into travel (travelid, accountid, startpoint, endpoint, name, begin_time, end_time, is_arrival_time, mon, tue, wed, thu, fri, sat, sun)
	values (2, 1, 5, 1, 'work->home', '16:00', '17:00', false, true, true, true, true, true, false, false);
insert into travel (travelid, accountid, startpoint, endpoint, name, begin_time, end_time, is_arrival_time, mon, tue, wed, thu, fri, sat, sun)
	values (3, 2, 4, 2, 'hobby', '12:00', '13:30', true, false, false, false, false, false, false, true);
ALTER SEQUENCE travelid_seq	RESTART WITH 4;

-- route
insert into route (routeid, travelid, user_waypoints, full_waypoints, transportation_type, active, notify_email,notify_cell)
	values (1, 1, NULL, '{ {51.046219, 3.726892}, {51.038623, 3.726248}, {51.038643, 3.736290}, {51.031119, 3.739630}, {51.028195, 3.742949}, {51.042366, 3.785280}, {51.079048, 4.004868}, {51.126910, 4.081562}, {51.213862, 4.353423}, {51.190653, 4.413875} }', 'car', true, false, false);
insert into route (routeid, travelid, user_waypoints, full_waypoints, transportation_type, active, notify_email,notify_cell)
	values (2, 2, NULL,'{{51.038643, 3.736290}, {51.038623, 3.726248}, {51.046219, 3.726892}}', 'car', true, true, false);
insert into route (routeid, travelid, user_waypoints, full_waypoints, transportation_type, active, notify_email,notify_cell)
	values (3, 2, '{ {51.190056, 3.843931} }', '{{51.038643, 3.736290}, {51.038623, 3.726248}, {51.046219, 3.726892}}', 'car', true, false, false);
insert into route (routeid, travelid, user_waypoints, full_waypoints, transportation_type, active, notify_email,notify_cell)
	values (4, 3, NULL, '{ {51.046293, 3.724927}, {51.046238, 3.726813}, {51.038627, 3.726196}, {51.038627, 3.726196}, {51.028789, 3.722253}, {51.028323, 3.718476}}', 'car', true, false, false);
ALTER SEQUENCE routeid_seq RESTART WITH 5;

-- route_eventtype
insert into route_eventtype (route_eventtypeid, routeid, eventtypeid)
	values (1, 1, 1);
insert into route_eventtype (route_eventtypeid, routeid, eventtypeid)
	values (2, 1, 2);
insert into route_eventtype (route_eventtypeid, routeid, eventtypeid)
	values (3, 1, 4);
insert into route_eventtype (route_eventtypeid, routeid, eventtypeid)
	values (4, 2, 1);
insert into route_eventtype (route_eventtypeid, routeid, eventtypeid)
	values (5, 2, 2);
insert into route_eventtype (route_eventtypeid, routeid, eventtypeid)
	values (6, 2, 4);
insert into route_eventtype (route_eventtypeid, routeid, eventtypeid)
	values (7, 3, 4);
ALTER SEQUENCE route_eventtypeid_seq RESTART WITH 8;
