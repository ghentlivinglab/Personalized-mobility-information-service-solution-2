-- v4.03 (28/04/2016)


--------------------------
--------- TABLES ---------
--------------------------

DROP TABLE IF EXISTS account CASCADE;
DROP SEQUENCE IF EXISTS accountid_seq;
CREATE SEQUENCE accountid_seq;
CREATE TABLE account
(
    accountid integer PRIMARY KEY DEFAULT nextval('accountid_seq'),
    created_at timestamp DEFAULT current_timestamp, -- timestamp a user when he is created
    email text NOT NULL UNIQUE,
    refresh_token text,
    first_name text,
    last_name text,
    salt bytea NOT NULL,
    password text NOT NULL,
    mute_notifications boolean NOT NULL DEFAULT false,
    email_validated boolean NOT NULL DEFAULT false,
    is_operator boolean NOT NULL DEFAULT false,
    is_admin boolean NOT NULL DEFAULT false
);

DROP TABLE IF EXISTS city CASCADE;
DROP SEQUENCE IF EXISTS cityid_seq;
CREATE SEQUENCE cityid_seq;
CREATE TABLE city
(
    cityid integer PRIMARY KEY DEFAULT nextval('cityid_seq'),
    city text NOT NULL,
    postal_code text NOT NULL,
    country text NOT NULL,
    CONSTRAINT unique_city UNIQUE ( postal_code, country )
);

DROP TABLE IF EXISTS street CASCADE;
DROP SEQUENCE IF EXISTS streetid_seq;
CREATE SEQUENCE streetid_seq;
CREATE TABLE street
(
    streetid integer PRIMARY KEY DEFAULT nextval('streetid_seq'),
    cityid integer NOT NULL REFERENCES city ON UPDATE CASCADE, -- keep default for on delete: no action will trigger an exception.
    name text NOT NULL,
    CONSTRAINT unique_street UNIQUE (cityid, name)
);

DROP TABLE IF EXISTS address CASCADE;
DROP SEQUENCE IF EXISTS addressid_seq;
CREATE SEQUENCE addressid_seq;
CREATE TABLE address
(
    addressid integer PRIMARY KEY DEFAULT nextval('addressid_seq'),
    streetid integer NOT NULL REFERENCES street ON UPDATE CASCADE, -- keep default ON DELETE; when we try to delete a street that's still used => exception
    housenumber text NOT NULL,
    latitude numeric(12,8),
    longitude numeric(12,8),
    cartesianX numeric(20,8),
    cartesianY numeric(20,8),
    CONSTRAINT unique_address UNIQUE ( streetid, housenumber )
);

DROP TABLE IF EXISTS travel CASCADE;
DROP SEQUENCE IF EXISTS travelid_seq;
CREATE SEQUENCE travelid_seq;
CREATE TABLE travel
(
    travelid integer PRIMARY KEY DEFAULT nextval('travelid_seq'),
    accountid integer NOT NULL REFERENCES account ON DELETE CASCADE ON UPDATE CASCADE,
    startpoint integer NOT NULL REFERENCES address ON DELETE NO ACTION ON UPDATE CASCADE,
    endpoint integer NOT NULL REFERENCES address ON DELETE NO ACTION ON UPDATE CASCADE,
    name text NOT NULL,
    begin_time time NOT NULL,
    end_time time NOT NULL,
    is_arrival_time boolean NOT NULL DEFAULT true, -- true => arrival; false => departure
    mon boolean NOT NULL DEFAULT false,
    tue boolean NOT NULL DEFAULT false,
    wed boolean NOT NULL DEFAULT false,
    thu boolean NOT NULL DEFAULT false,
    fri boolean NOT NULL DEFAULT false,
    sat boolean NOT NULL DEFAULT false,
    sun boolean NOT NULL DEFAULT false
);

DROP TABLE IF EXISTS route CASCADE;
DROP SEQUENCE IF EXISTS routeid_seq;
CREATE SEQUENCE routeid_seq;
CREATE TABLE route
(
    routeid integer PRIMARY KEY DEFAULT nextval('routeid_seq'),
    travelid integer NOT NULL REFERENCES travel ON DELETE CASCADE ON UPDATE CASCADE,
    user_waypoints numeric(12, 8)[][],
    full_waypoints numeric(12, 8)[][],
    transportation_type text NOT NULL DEFAULT 'car',
    active boolean NOT NULL DEFAULT true,
    notify_email boolean NOT NULL DEFAULT false,
    notify_cell boolean NOT NULL DEFAULT false
);

DROP TABLE IF EXISTS route_gridpoint CASCADE;
DROP SEQUENCE IF EXISTS route_gridpointid_seq;
CREATE SEQUENCE route_gridpointid_seq;
CREATE TABLE route_gridpoint
(
    route_gridpointid integer PRIMARY KEY DEFAULT nextval('route_gridpointid_seq'),
    routeid integer NOT NULL REFERENCES route ON UPDATE CASCADE ON DELETE CASCADE,
    gridx integer NOT NULL,
    gridy integer NOT NULL
);

DROP TABLE IF EXISTS location CASCADE;
DROP SEQUENCE IF EXISTS locationid_seq;
CREATE SEQUENCE locationid_seq;
CREATE TABLE location
(
    locationid integer PRIMARY KEY DEFAULT nextval('locationid_seq'),
    accountid integer NOT NULL REFERENCES account ON DELETE CASCADE ON UPDATE CASCADE,
    addressid integer NOT NULL REFERENCES address ON UPDATE CASCADE, -- ON DELETE stay on default NO ACTION
    name text,
    radius integer,
    active boolean,
    notify_email boolean NOT NULL DEFAULT false,
    notify_cell boolean NOT NULL DEFAULT false,
    CONSTRAINT unique_location UNIQUE ( accountid, addressid )
);

DROP TABLE IF EXISTS eventtype CASCADE;
DROP SEQUENCE IF EXISTS eventtypeid_seq;
CREATE SEQUENCE eventtypeid_seq;
CREATE TABLE eventtype
(
    eventtypeid integer PRIMARY KEY DEFAULT nextval('eventtypeid_seq'),
    type text NOT NULL,
    relevant_transportation_types text[] NOT NULL DEFAULT '{ "car" }',
    CONSTRAINT unique_eventtype UNIQUE ( type )
);

DROP TABLE IF EXISTS route_eventtype CASCADE;
DROP SEQUENCE IF EXISTS route_eventtypeid_seq;
CREATE SEQUENCE route_eventtypeid_seq;
CREATE TABLE route_eventtype
(
    route_eventtypeid integer PRIMARY KEY DEFAULT nextval('route_eventtypeid_seq'),
    routeid integer NOT NULL REFERENCES route ON DELETE CASCADE ON UPDATE CASCADE,
    eventtypeid integer NOT NULL REFERENCES eventtype ON DELETE CASCADE ON UPDATE CASCADE
);

DROP TABLE IF EXISTS location_eventtype CASCADE;
DROP SEQUENCE IF EXISTS location_eventtypeid_seq;
CREATE SEQUENCE location_eventtypeid_seq;
CREATE TABLE location_eventtype
(
    location_eventtypeid integer PRIMARY KEY DEFAULT nextval('location_eventtypeid_seq'),
    locationid integer NOT NULL REFERENCES location ON DELETE CASCADE ON UPDATE CASCADE,
    eventtypeid integer NOT NULL REFERENCES eventtype ON DELETE CASCADE ON UPDATE CASCADE
);

---------------------------------
------------ MAILBOX ------------
---------------------------------

-- tables for the implementation of a 'mailbox' for events

-- events relevent for a location (formally point of interest)
DROP TABLE IF EXISTS location_event CASCADE;
DROP SEQUENCE IF EXISTS location_eventid_seq;
CREATE SEQUENCE location_eventid_seq;
CREATE TABLE location_event
(
    location_eventid integer PRIMARY KEY DEFAULT nextval('location_eventid_seq'),
    locationid integer NOT NULL REFERENCES location ON UPDATE CASCADE ON DELETE CASCADE,
    eventid text NOT NULL, -- think foreign key, but then to MongoDB
    deleted boolean NOT NULL DEFAULT false,
    CONSTRAINT unique_location_event UNIQUE (locationid, eventid)
);

-- events that are relevent for a certain route
DROP TABLE IF EXISTS route_event CASCADE;
DROP SEQUENCE IF EXISTS route_eventid_seq;
CREATE SEQUENCE route_eventid_seq;
CREATE TABLE route_event
(
    route_eventid integer PRIMARY KEY DEFAULT nextval('route_eventid_seq'),
    routeid integer NOT NULL REFERENCES route ON UPDATE CASCADE ON DELETE CASCADE,
    eventid text NOT NULL, -- think foreign key, but then to MongoDB
    deleted boolean NOT NULL DEFAULT false,
    CONSTRAINT unique_route_event UNIQUE (routeid, eventid)
);

--------------------------------------------
--------- ADDRESS CLEANUP TRIGGERS ---------
--------------------------------------------

-- This trigger will clear up all unused address after deleting a location. In order to do this, it check if there
-- remains a record in travel or location itself that points to the address of the deleted location.

CREATE OR REPLACE FUNCTION clear_addresses_on_location_del() RETURNS trigger AS
$BODY$
    BEGIN
        IF NOT EXISTS ( SELECT * FROM travel WHERE travel.startpoint=OLD.addressid OR travel.endpoint=OLD.addressid)
        AND NOT EXISTS (SELECT * FROM location WHERE location.addressid=OLD.addressid) THEN
            DELETE FROM address WHERE address.addressid=OLD.addressid;
        END IF;
        RETURN NULL;
    END;
$BODY$
LANGUAGE plpgsql;

DROP TRIGGER IF EXISTS location_del ON location;
CREATE TRIGGER location_del
    AFTER DELETE OR UPDATE
    ON location
    FOR EACH ROW
    EXECUTE PROCEDURE clear_addresses_on_location_del();


-- This trigger will clean up all unused address after deleting an travel. In order to do this, it checks if there
-- remains a record in location or travel itself that points to the address of the deleted travel

CREATE OR REPLACE FUNCTION clear_addresses_on_travel_del() RETURNS trigger AS
$BODY$
    BEGIN
        -- check if startpoint is still used.
        IF NOT EXISTS ( SELECT * FROM travel WHERE OLD.startpoint=travel.startpoint OR OLD.startpoint=travel.endpoint)
        AND NOT EXISTS (SELECT * FROM location WHERE OLD.startpoint=location.addressid) THEN
            DELETE FROM address WHERE address.addressid=OLD.startpoint;
        END IF;
        -- check if endpoint is still used.
        IF NOT EXISTS ( SELECT * FROM travel WHERE OLD.endpoint=travel.startpoint OR OLD.endpoint=travel.endpoint)
        AND NOT EXISTS (SELECT * FROM location WHERE OLD.endpoint=location.addressid) THEN
            DELETE FROM address WHERE address.addressid=OLD.endpoint;
        END IF;
        RETURN NULL;
    END;
$BODY$
LANGUAGE plpgsql;

DROP TRIGGER IF EXISTS travel_del ON travel;
CREATE TRIGGER travel_del
    AFTER DELETE OR UPDATE
    ON travel
    FOR EACH ROW
    EXECUTE PROCEDURE clear_addresses_on_travel_del();

-------------------------------------------
--------- STREET CLEANUP TRIGGERS ---------
-------------------------------------------

-- This trigger will clean up all unused streets after deleting an address. In order to to do this, it checks if there
-- remains a record in address itself that points to the street of the deleted address

CREATE OR REPLACE FUNCTION clear_street_on_address_del() RETURNS trigger AS
$BODY$
    BEGIN
        IF NOT EXISTS (SELECT * FROM address WHERE OLD.streetid=address.streetid) THEN
            DELETE FROM street WHERE street.streetid=OLD.streetid;
        END IF;
        RETURN NULL;
    END;
$BODY$
LANGUAGE plpgsql;

DROP TRIGGER IF EXISTS address_del ON address;
CREATE TRIGGER address_del
    AFTER DELETE OR UPDATE
    ON address
    FOR EACH ROW
    EXECUTE PROCEDURE clear_street_on_address_del();
