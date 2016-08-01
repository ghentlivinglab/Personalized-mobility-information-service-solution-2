--
-- PostgreSQL database dump
--

-- Dumped from database version 9.4.6
-- Dumped by pg_dump version 9.5.0

-- Started on 2016-03-10 14:38:14

SET statement_timeout = 0;
SET lock_timeout = 0;
SET client_encoding = 'UTF8';
SET standard_conforming_strings = on;
SET check_function_bodies = false;
SET client_min_messages = warning;
SET row_security = off;

--
-- TOC entry 190 (class 3079 OID 11861)
-- Name: plpgsql; Type: EXTENSION; Schema: -; Owner: -
--

CREATE EXTENSION IF NOT EXISTS plpgsql WITH SCHEMA pg_catalog;


--
-- TOC entry 2133 (class 0 OID 0)
-- Dependencies: 190
-- Name: EXTENSION plpgsql; Type: COMMENT; Schema: -; Owner: -
--

COMMENT ON EXTENSION plpgsql IS 'PL/pgSQL procedural language';


SET search_path = public, pg_catalog;

--
-- TOC entry 204 (class 1255 OID 19267)
-- Name: route_address_del(); Type: FUNCTION; Schema: public; Owner: -
--

CREATE FUNCTION route_address_del() RETURNS trigger
    LANGUAGE plpgsql
    AS $$
BEGIN

IF NOT EXISTS (select * FROM user_address WHERE user_address.addressid = OLD.startpoint)
AND NOT EXISTS (SELECT * FROM route WHERE route.startpoint = OLD.startpoint OR route.endpoint = OLD.startpoint ) THEN
	DELETE FROM address WHERE address.addressid = OLD.startpoint;
END IF;
IF NOT EXISTS (select * FROM user_address WHERE user_address.addressid = OLD.endpoint) 
AND NOT EXISTS (SELECT * FROM route WHERE route.startpoint = OLD.endpoint OR route.endpoint = OLD.endpoint ) THEN
	DELETE FROM address WHERE address.addressid = OLD.endpoint;
END IF;
RETURN NULL;
END;
$$;


--
-- TOC entry 191 (class 1255 OID 19268)
-- Name: route_eventtype_del(); Type: FUNCTION; Schema: public; Owner: -
--

CREATE FUNCTION route_eventtype_del() RETURNS trigger
    LANGUAGE plpgsql
    AS $$
BEGIN
DELETE FROM eventtype WHERE eventtype.eventtypeid = OLD.eventtypeid;
RETURN NULL;
END;
$$;


--
-- TOC entry 205 (class 1255 OID 19269)
-- Name: travel_route_del(); Type: FUNCTION; Schema: public; Owner: -
--

CREATE FUNCTION travel_route_del() RETURNS trigger
    LANGUAGE plpgsql
    AS $$
BEGIN
DELETE FROM route WHERE route.routeid = OLD.routeid;
RETURN NULL;
END;
$$;


--
-- TOC entry 206 (class 1255 OID 19270)
-- Name: user_address_del(); Type: FUNCTION; Schema: public; Owner: -
--

CREATE FUNCTION user_address_del() RETURNS trigger
    LANGUAGE plpgsql
    AS $$
BEGIN
IF NOT EXISTS (SELECT * FROM route WHERE route.startpoint = OLD.addressid OR route.endpoint = OLD.addressid) 
AND NOT EXISTS (SELECT * FROM user_address WHERE user_address.addressid = OLD.addressid) THEN
	DELETE FROM address WHERE address.addressid = OLD.addressid;
END IF;
RETURN NULL;
END;
$$;


--
-- TOC entry 207 (class 1255 OID 19271)
-- Name: user_travel_del(); Type: FUNCTION; Schema: public; Owner: -
--

CREATE FUNCTION user_travel_del() RETURNS trigger
    LANGUAGE plpgsql
    AS $$
BEGIN
DELETE FROM travel WHERE travel.travelid = OLD.travelid;
RETURN NULL;
END;
$$;


SET default_tablespace = '';

SET default_with_oids = false;

--
-- TOC entry 172 (class 1259 OID 19272)
-- Name: address; Type: TABLE; Schema: public; Owner: -
--

CREATE TABLE address (
    addressid integer NOT NULL,
    street text NOT NULL,
    housenumber text NOT NULL,
    cityid integer NOT NULL,
    latitude numeric(11,8) NOT NULL,
    longitude numeric(11,8) NOT NULL
);


--
-- TOC entry 173 (class 1259 OID 19278)
-- Name: addressid_seq; Type: SEQUENCE; Schema: public; Owner: -
--

CREATE SEQUENCE addressid_seq
    START WITH 1
    INCREMENT BY 1
    NO MINVALUE
    NO MAXVALUE
    CACHE 1;


--
-- TOC entry 2134 (class 0 OID 0)
-- Dependencies: 173
-- Name: addressid_seq; Type: SEQUENCE OWNED BY; Schema: public; Owner: -
--

ALTER SEQUENCE addressid_seq OWNED BY address.addressid;


--
-- TOC entry 174 (class 1259 OID 19280)
-- Name: city; Type: TABLE; Schema: public; Owner: -
--

CREATE TABLE city (
    cityid integer NOT NULL,
    city text NOT NULL,
    postal_code text NOT NULL,
    country text NOT NULL
);


--
-- TOC entry 175 (class 1259 OID 19286)
-- Name: cityid_seq; Type: SEQUENCE; Schema: public; Owner: -
--

CREATE SEQUENCE cityid_seq
    START WITH 1
    INCREMENT BY 1
    NO MINVALUE
    NO MAXVALUE
    CACHE 1;


--
-- TOC entry 2135 (class 0 OID 0)
-- Dependencies: 175
-- Name: cityid_seq; Type: SEQUENCE OWNED BY; Schema: public; Owner: -
--

ALTER SEQUENCE cityid_seq OWNED BY city.cityid;


--
-- TOC entry 176 (class 1259 OID 19288)
-- Name: eventtype; Type: TABLE; Schema: public; Owner: -
--

CREATE TABLE eventtype (
    eventtypeid integer NOT NULL,
    type text NOT NULL,
    subtype text,
    relevant_transport_types text[] NOT NULL
);


--
-- TOC entry 177 (class 1259 OID 19294)
-- Name: eventtypeid_seq; Type: SEQUENCE; Schema: public; Owner: -
--

CREATE SEQUENCE eventtypeid_seq
    START WITH 1
    INCREMENT BY 1
    NO MINVALUE
    NO MAXVALUE
    CACHE 1;


--
-- TOC entry 2136 (class 0 OID 0)
-- Dependencies: 177
-- Name: eventtypeid_seq; Type: SEQUENCE OWNED BY; Schema: public; Owner: -
--

ALTER SEQUENCE eventtypeid_seq OWNED BY eventtype.eventtypeid;


--
-- TOC entry 178 (class 1259 OID 19296)
-- Name: route; Type: TABLE; Schema: public; Owner: -
--

CREATE TABLE route (
    routeid integer NOT NULL,
    startpoint integer NOT NULL,
    endpoint integer NOT NULL,
    transportation_types text[],
    waypoints numeric(11,8)[]
);


--
-- TOC entry 179 (class 1259 OID 19302)
-- Name: route_eventtype; Type: TABLE; Schema: public; Owner: -
--

CREATE TABLE route_eventtype (
    routeid integer NOT NULL,
    eventtypeid integer NOT NULL
);


--
-- TOC entry 180 (class 1259 OID 19305)
-- Name: routeid_seq; Type: SEQUENCE; Schema: public; Owner: -
--

CREATE SEQUENCE routeid_seq
    START WITH 1
    INCREMENT BY 1
    NO MINVALUE
    NO MAXVALUE
    CACHE 1;


--
-- TOC entry 2137 (class 0 OID 0)
-- Dependencies: 180
-- Name: routeid_seq; Type: SEQUENCE OWNED BY; Schema: public; Owner: -
--

ALTER SEQUENCE routeid_seq OWNED BY route.routeid;


--
-- TOC entry 181 (class 1259 OID 19307)
-- Name: travel; Type: TABLE; Schema: public; Owner: -
--

CREATE TABLE travel (
    travelid integer NOT NULL,
    date date,
    is_arrival_time boolean NOT NULL,
    mon boolean NOT NULL,
    tue boolean NOT NULL,
    wed boolean NOT NULL,
    thu boolean NOT NULL,
    fri boolean NOT NULL,
    sat boolean NOT NULL,
    sun boolean NOT NULL,
    time_interval time without time zone[] NOT NULL,
    name text
);


--
-- TOC entry 182 (class 1259 OID 19313)
-- Name: travel_route; Type: TABLE; Schema: public; Owner: -
--

CREATE TABLE travel_route (
    travelid integer NOT NULL,
    routeid integer NOT NULL
);


--
-- TOC entry 183 (class 1259 OID 19316)
-- Name: travelid_seq; Type: SEQUENCE; Schema: public; Owner: -
--

CREATE SEQUENCE travelid_seq
    START WITH 1
    INCREMENT BY 1
    NO MINVALUE
    NO MAXVALUE
    CACHE 1;


--
-- TOC entry 2138 (class 0 OID 0)
-- Dependencies: 183
-- Name: travelid_seq; Type: SEQUENCE OWNED BY; Schema: public; Owner: -
--

ALTER SEQUENCE travelid_seq OWNED BY travel.travelid;


--
-- TOC entry 184 (class 1259 OID 19318)
-- Name: user_address; Type: TABLE; Schema: public; Owner: -
--

CREATE TABLE user_address (
    user_address_id integer NOT NULL,
    userid integer NOT NULL,
    addressid integer NOT NULL,
    name text NOT NULL,
    is_home_address boolean NOT NULL,
    active boolean NOT NULL,
    radius numeric(8,2) NOT NULL
);


--
-- TOC entry 189 (class 1259 OID 19447)
-- Name: user_event; Type: TABLE; Schema: public; Owner: -
--

CREATE TABLE user_event (
    userid integer NOT NULL,
    eventid integer NOT NULL
);


--
-- TOC entry 185 (class 1259 OID 19324)
-- Name: user_travel; Type: TABLE; Schema: public; Owner: -
--

CREATE TABLE user_travel (
    userid integer NOT NULL,
    travelid integer NOT NULL
);


--
-- TOC entry 186 (class 1259 OID 19327)
-- Name: useraddressid_seq; Type: SEQUENCE; Schema: public; Owner: -
--

CREATE SEQUENCE useraddressid_seq
    START WITH 1
    INCREMENT BY 1
    NO MINVALUE
    NO MAXVALUE
    CACHE 1;


--
-- TOC entry 2139 (class 0 OID 0)
-- Dependencies: 186
-- Name: useraddressid_seq; Type: SEQUENCE OWNED BY; Schema: public; Owner: -
--

ALTER SEQUENCE useraddressid_seq OWNED BY user_address.user_address_id;


--
-- TOC entry 187 (class 1259 OID 19329)
-- Name: users; Type: TABLE; Schema: public; Owner: -
--

CREATE TABLE users (
    userid integer NOT NULL,
    email text NOT NULL,
    first_name text NOT NULL,
    last_name text NOT NULL,
    password text NOT NULL,
    cell_number text,
    gender character(1),
    mute_notifications boolean NOT NULL
);


--
-- TOC entry 188 (class 1259 OID 19335)
-- Name: userid_seq; Type: SEQUENCE; Schema: public; Owner: -
--

CREATE SEQUENCE userid_seq
    START WITH 1
    INCREMENT BY 1
    NO MINVALUE
    NO MAXVALUE
    CACHE 1;


--
-- TOC entry 2140 (class 0 OID 0)
-- Dependencies: 188
-- Name: userid_seq; Type: SEQUENCE OWNED BY; Schema: public; Owner: -
--

ALTER SEQUENCE userid_seq OWNED BY users.userid;


--
-- TOC entry 1949 (class 2604 OID 19337)
-- Name: addressid; Type: DEFAULT; Schema: public; Owner: -
--

ALTER TABLE ONLY address ALTER COLUMN addressid SET DEFAULT nextval('addressid_seq'::regclass);


--
-- TOC entry 1950 (class 2604 OID 19338)
-- Name: cityid; Type: DEFAULT; Schema: public; Owner: -
--

ALTER TABLE ONLY city ALTER COLUMN cityid SET DEFAULT nextval('cityid_seq'::regclass);


--
-- TOC entry 1951 (class 2604 OID 19339)
-- Name: eventtypeid; Type: DEFAULT; Schema: public; Owner: -
--

ALTER TABLE ONLY eventtype ALTER COLUMN eventtypeid SET DEFAULT nextval('eventtypeid_seq'::regclass);


--
-- TOC entry 1952 (class 2604 OID 19340)
-- Name: routeid; Type: DEFAULT; Schema: public; Owner: -
--

ALTER TABLE ONLY route ALTER COLUMN routeid SET DEFAULT nextval('routeid_seq'::regclass);


--
-- TOC entry 1953 (class 2604 OID 19341)
-- Name: travelid; Type: DEFAULT; Schema: public; Owner: -
--

ALTER TABLE ONLY travel ALTER COLUMN travelid SET DEFAULT nextval('travelid_seq'::regclass);


--
-- TOC entry 1954 (class 2604 OID 19342)
-- Name: user_address_id; Type: DEFAULT; Schema: public; Owner: -
--

ALTER TABLE ONLY user_address ALTER COLUMN user_address_id SET DEFAULT nextval('useraddressid_seq'::regclass);


--
-- TOC entry 1955 (class 2604 OID 19343)
-- Name: userid; Type: DEFAULT; Schema: public; Owner: -
--

ALTER TABLE ONLY users ALTER COLUMN userid SET DEFAULT nextval('userid_seq'::regclass);


--
-- TOC entry 1957 (class 2606 OID 19345)
-- Name: address_pk; Type: CONSTRAINT; Schema: public; Owner: -
--

ALTER TABLE ONLY address
    ADD CONSTRAINT address_pk PRIMARY KEY (addressid);


--
-- TOC entry 1962 (class 2606 OID 19347)
-- Name: city_pk; Type: CONSTRAINT; Schema: public; Owner: -
--

ALTER TABLE ONLY city
    ADD CONSTRAINT city_pk PRIMARY KEY (cityid);


--
-- TOC entry 1967 (class 2606 OID 19349)
-- Name: eventtype_pk; Type: CONSTRAINT; Schema: public; Owner: -
--

ALTER TABLE ONLY eventtype
    ADD CONSTRAINT eventtype_pk PRIMARY KEY (eventtypeid);


--
-- TOC entry 1976 (class 2606 OID 19351)
-- Name: r_et_pk; Type: CONSTRAINT; Schema: public; Owner: -
--

ALTER TABLE ONLY route_eventtype
    ADD CONSTRAINT r_et_pk PRIMARY KEY (routeid, eventtypeid);


--
-- TOC entry 1972 (class 2606 OID 19353)
-- Name: route_pk; Type: CONSTRAINT; Schema: public; Owner: -
--

ALTER TABLE ONLY route
    ADD CONSTRAINT route_pk PRIMARY KEY (routeid);


--
-- TOC entry 1978 (class 2606 OID 19355)
-- Name: travel_pk; Type: CONSTRAINT; Schema: public; Owner: -
--

ALTER TABLE ONLY travel
    ADD CONSTRAINT travel_pk PRIMARY KEY (travelid);


--
-- TOC entry 1982 (class 2606 OID 19357)
-- Name: travel_route_pk; Type: CONSTRAINT; Schema: public; Owner: -
--

ALTER TABLE ONLY travel_route
    ADD CONSTRAINT travel_route_pk PRIMARY KEY (travelid, routeid);


--
-- TOC entry 1991 (class 2606 OID 19359)
-- Name: u_t_pk; Type: CONSTRAINT; Schema: public; Owner: -
--

ALTER TABLE ONLY user_travel
    ADD CONSTRAINT u_t_pk PRIMARY KEY (userid, travelid);


--
-- TOC entry 1985 (class 2606 OID 19361)
-- Name: ua_unique; Type: CONSTRAINT; Schema: public; Owner: -
--

ALTER TABLE ONLY user_address
    ADD CONSTRAINT ua_unique UNIQUE (userid, addressid);


--
-- TOC entry 1960 (class 2606 OID 19363)
-- Name: unique_address; Type: CONSTRAINT; Schema: public; Owner: -
--

ALTER TABLE ONLY address
    ADD CONSTRAINT unique_address UNIQUE (street, housenumber, cityid);


--
-- TOC entry 1965 (class 2606 OID 19365)
-- Name: unique_city; Type: CONSTRAINT; Schema: public; Owner: -
--

ALTER TABLE ONLY city
    ADD CONSTRAINT unique_city UNIQUE (city, country, postal_code);


--
-- TOC entry 1994 (class 2606 OID 19367)
-- Name: unique_email; Type: CONSTRAINT; Schema: public; Owner: -
--

ALTER TABLE ONLY users
    ADD CONSTRAINT unique_email UNIQUE (email);


--
-- TOC entry 1970 (class 2606 OID 19369)
-- Name: unique_eventtype; Type: CONSTRAINT; Schema: public; Owner: -
--

ALTER TABLE ONLY eventtype
    ADD CONSTRAINT unique_eventtype UNIQUE (type, subtype);


--
-- TOC entry 1987 (class 2606 OID 19371)
-- Name: user_address_pk; Type: CONSTRAINT; Schema: public; Owner: -
--

ALTER TABLE ONLY user_address
    ADD CONSTRAINT user_address_pk PRIMARY KEY (user_address_id);


--
-- TOC entry 1996 (class 2606 OID 19373)
-- Name: user_pk; Type: CONSTRAINT; Schema: public; Owner: -
--

ALTER TABLE ONLY users
    ADD CONSTRAINT user_pk PRIMARY KEY (userid);


--
-- TOC entry 1999 (class 2606 OID 19451)
-- Name: userid_eventid_pk; Type: CONSTRAINT; Schema: public; Owner: -
--

ALTER TABLE ONLY user_event
    ADD CONSTRAINT userid_eventid_pk PRIMARY KEY (userid, eventid);


--
-- TOC entry 1958 (class 1259 OID 19374)
-- Name: addresspk_index; Type: INDEX; Schema: public; Owner: -
--

CREATE INDEX addresspk_index ON address USING btree (addressid);


--
-- TOC entry 1963 (class 1259 OID 19375)
-- Name: citypk_index; Type: INDEX; Schema: public; Owner: -
--

CREATE INDEX citypk_index ON city USING btree (cityid);


--
-- TOC entry 1992 (class 1259 OID 19376)
-- Name: email_index; Type: INDEX; Schema: public; Owner: -
--

CREATE INDEX email_index ON users USING btree (email);


--
-- TOC entry 1968 (class 1259 OID 19377)
-- Name: eventtypepk_index; Type: INDEX; Schema: public; Owner: -
--

CREATE INDEX eventtypepk_index ON eventtype USING btree (eventtypeid);


--
-- TOC entry 1983 (class 1259 OID 19378)
-- Name: fki_address_fk; Type: INDEX; Schema: public; Owner: -
--

CREATE INDEX fki_address_fk ON user_address USING btree (addressid);


--
-- TOC entry 1974 (class 1259 OID 19379)
-- Name: fki_eventtype_fk; Type: INDEX; Schema: public; Owner: -
--

CREATE INDEX fki_eventtype_fk ON route_eventtype USING btree (eventtypeid);


--
-- TOC entry 1980 (class 1259 OID 19380)
-- Name: fki_route_fk; Type: INDEX; Schema: public; Owner: -
--

CREATE INDEX fki_route_fk ON travel_route USING btree (routeid);


--
-- TOC entry 1989 (class 1259 OID 19381)
-- Name: fki_travel_pk -> users; Type: INDEX; Schema: public; Owner: -
--

CREATE INDEX "fki_travel_pk -> users" ON user_travel USING btree (travelid);


--
-- TOC entry 1973 (class 1259 OID 19382)
-- Name: routepk_index; Type: INDEX; Schema: public; Owner: -
--

CREATE INDEX routepk_index ON route USING btree (routeid);


--
-- TOC entry 1979 (class 1259 OID 19383)
-- Name: travelpk_index; Type: INDEX; Schema: public; Owner: -
--

CREATE INDEX travelpk_index ON travel USING btree (travelid);


--
-- TOC entry 1988 (class 1259 OID 19384)
-- Name: useraddresspk_index; Type: INDEX; Schema: public; Owner: -
--

CREATE INDEX useraddresspk_index ON user_address USING btree (user_address_id);


--
-- TOC entry 1997 (class 1259 OID 19385)
-- Name: userpk_index; Type: INDEX; Schema: public; Owner: -
--

CREATE INDEX userpk_index ON users USING btree (userid);


--
-- TOC entry 2012 (class 2620 OID 19386)
-- Name: route_address_del; Type: TRIGGER; Schema: public; Owner: -
--

CREATE TRIGGER route_address_del AFTER DELETE ON route FOR EACH ROW EXECUTE PROCEDURE route_address_del();


--
-- TOC entry 2013 (class 2620 OID 19387)
-- Name: route_eventtype_del; Type: TRIGGER; Schema: public; Owner: -
--

CREATE TRIGGER route_eventtype_del AFTER DELETE ON route_eventtype FOR EACH ROW EXECUTE PROCEDURE route_eventtype_del();


--
-- TOC entry 2014 (class 2620 OID 19388)
-- Name: travel_route_del; Type: TRIGGER; Schema: public; Owner: -
--

CREATE TRIGGER travel_route_del AFTER DELETE ON travel_route FOR EACH ROW EXECUTE PROCEDURE travel_route_del();


--
-- TOC entry 2015 (class 2620 OID 19389)
-- Name: user_address_del; Type: TRIGGER; Schema: public; Owner: -
--

CREATE TRIGGER user_address_del AFTER DELETE ON user_address FOR EACH ROW EXECUTE PROCEDURE user_address_del();


--
-- TOC entry 2016 (class 2620 OID 19390)
-- Name: user_travel_del; Type: TRIGGER; Schema: public; Owner: -
--

CREATE TRIGGER user_travel_del AFTER DELETE ON user_travel FOR EACH ROW EXECUTE PROCEDURE user_travel_del();


--
-- TOC entry 2000 (class 2606 OID 19391)
-- Name: address_city_fk; Type: FK CONSTRAINT; Schema: public; Owner: -
--

ALTER TABLE ONLY address
    ADD CONSTRAINT address_city_fk FOREIGN KEY (cityid) REFERENCES city(cityid);


--
-- TOC entry 2007 (class 2606 OID 19396)
-- Name: address_fk; Type: FK CONSTRAINT; Schema: public; Owner: -
--

ALTER TABLE ONLY user_address
    ADD CONSTRAINT address_fk FOREIGN KEY (addressid) REFERENCES address(addressid) ON DELETE CASCADE;


--
-- TOC entry 2001 (class 2606 OID 19401)
-- Name: begin_address; Type: FK CONSTRAINT; Schema: public; Owner: -
--

ALTER TABLE ONLY route
    ADD CONSTRAINT begin_address FOREIGN KEY (startpoint) REFERENCES address(addressid);


--
-- TOC entry 2002 (class 2606 OID 19406)
-- Name: end_address; Type: FK CONSTRAINT; Schema: public; Owner: -
--

ALTER TABLE ONLY route
    ADD CONSTRAINT end_address FOREIGN KEY (endpoint) REFERENCES address(addressid);


--
-- TOC entry 2003 (class 2606 OID 19411)
-- Name: eventtype_fk; Type: FK CONSTRAINT; Schema: public; Owner: -
--

ALTER TABLE ONLY route_eventtype
    ADD CONSTRAINT eventtype_fk FOREIGN KEY (eventtypeid) REFERENCES eventtype(eventtypeid) ON DELETE CASCADE;


--
-- TOC entry 2004 (class 2606 OID 19416)
-- Name: route_fk; Type: FK CONSTRAINT; Schema: public; Owner: -
--

ALTER TABLE ONLY route_eventtype
    ADD CONSTRAINT route_fk FOREIGN KEY (routeid) REFERENCES route(routeid) ON DELETE CASCADE;


--
-- TOC entry 2005 (class 2606 OID 19421)
-- Name: route_fk; Type: FK CONSTRAINT; Schema: public; Owner: -
--

ALTER TABLE ONLY travel_route
    ADD CONSTRAINT route_fk FOREIGN KEY (routeid) REFERENCES route(routeid) ON DELETE CASCADE;


--
-- TOC entry 2006 (class 2606 OID 19426)
-- Name: travel_fk; Type: FK CONSTRAINT; Schema: public; Owner: -
--

ALTER TABLE ONLY travel_route
    ADD CONSTRAINT travel_fk FOREIGN KEY (travelid) REFERENCES travel(travelid) ON DELETE CASCADE;


--
-- TOC entry 2009 (class 2606 OID 19431)
-- Name: travel_fk; Type: FK CONSTRAINT; Schema: public; Owner: -
--

ALTER TABLE ONLY user_travel
    ADD CONSTRAINT travel_fk FOREIGN KEY (travelid) REFERENCES travel(travelid) ON DELETE CASCADE;


--
-- TOC entry 2010 (class 2606 OID 19436)
-- Name: user_fk; Type: FK CONSTRAINT; Schema: public; Owner: -
--

ALTER TABLE ONLY user_travel
    ADD CONSTRAINT user_fk FOREIGN KEY (userid) REFERENCES users(userid) ON DELETE CASCADE;


--
-- TOC entry 2008 (class 2606 OID 19441)
-- Name: user_fk; Type: FK CONSTRAINT; Schema: public; Owner: -
--

ALTER TABLE ONLY user_address
    ADD CONSTRAINT user_fk FOREIGN KEY (userid) REFERENCES users(userid) ON DELETE CASCADE;


--
-- TOC entry 2011 (class 2606 OID 19452)
-- Name: user_fk; Type: FK CONSTRAINT; Schema: public; Owner: -
--

ALTER TABLE ONLY user_event
    ADD CONSTRAINT user_fk FOREIGN KEY (userid) REFERENCES users(userid) ON DELETE CASCADE;


--
-- TOC entry 2132 (class 0 OID 0)
-- Dependencies: 6
-- Name: public; Type: ACL; Schema: -; Owner: -
--

REVOKE ALL ON SCHEMA public FROM PUBLIC;
REVOKE ALL ON SCHEMA public FROM postgres;
GRANT ALL ON SCHEMA public TO postgres;
GRANT ALL ON SCHEMA public TO PUBLIC;


-- Completed on 2016-03-10 14:38:20

--
-- PostgreSQL database dump complete
--

