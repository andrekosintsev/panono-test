SET statement_timeout = 0;
SET lock_timeout = 0;
SET idle_in_transaction_session_timeout = 0;
SET client_encoding = 'UTF8';
SET standard_conforming_strings = on;
SET check_function_bodies = false;
SET client_min_messages = warning;
SET row_security = off;

DROP TABLE IF EXISTS  panono.operations;
DROP TABLE IF EXISTS  panono.shapes;

DROP SCHEMA IF EXISTS  panono;

CREATE SCHEMA panono;

SET search_path = panono;

ALTER SCHEMA panono OWNER TO postgres;



CREATE TABLE shapes (
    id character varying(40),
    coordinates character varying(255)
);

ALTER TABLE shapes OWNER TO postgres;



CREATE TABLE operations (
    id character varying(40),
    op_type character varying(20),
    op_param character varying(255)
);


ALTER TABLE operations OWNER TO postgres;
