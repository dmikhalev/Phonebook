
-- PHONE_TYPE

CREATE TABLE public.phone_type
(
  id bigint NOT NULL GENERATED ALWAYS AS IDENTITY ( INCREMENT 1 START 1 MINVALUE 1 MAXVALUE 9223372036854775807 CACHE 1 ),
  name character varying(100) COLLATE pg_catalog."default" NOT NULL,
  CONSTRAINT phone_type_pkey PRIMARY KEY (id),
  CONSTRAINT unique_phone_type_name UNIQUE (name)
);


-- PHONE

CREATE TABLE public.phone
(
  id bigint NOT NULL GENERATED ALWAYS AS IDENTITY ( INCREMENT 1 START 1 MINVALUE 1 MAXVALUE 9223372036854775807 CACHE 1 ),
  telephone_number character varying(100) COLLATE pg_catalog."default" NOT NULL,
  model character varying(100) COLLATE pg_catalog."default",
  phone_type_id bigint,
  CONSTRAINT phone_pkey PRIMARY KEY (id),
  CONSTRAINT fk_phone_type FOREIGN KEY (phone_type_id)
    REFERENCES public.phone_type (id) MATCH SIMPLE
    ON UPDATE NO ACTION
    ON DELETE NO ACTION
);


-- COUNTRY

CREATE TABLE public.country
(
  id bigint NOT NULL GENERATED ALWAYS AS IDENTITY ( INCREMENT 1 START 1 MINVALUE 1 MAXVALUE 9223372036854775807 CACHE 1 ),
  name character varying(100) COLLATE pg_catalog."default" NOT NULL,
  CONSTRAINT country_pkey PRIMARY KEY (id),
  CONSTRAINT unique_country_name UNIQUE (name)
);


-- CITY

CREATE TABLE public.city
(
  id bigint NOT NULL GENERATED ALWAYS AS IDENTITY ( INCREMENT 1 START 1 MINVALUE 1 MAXVALUE 9223372036854775807 CACHE 1 ),
  name character varying(100) COLLATE pg_catalog."default" NOT NULL,
  country_id bigint NOT NULL,
  CONSTRAINT city_pkey PRIMARY KEY (id),
  CONSTRAINT fk_country_id FOREIGN KEY (country_id)
    REFERENCES public.country (id) MATCH SIMPLE
    ON UPDATE NO ACTION
    ON DELETE NO ACTION
    NOT VALID
);

-- ADDRESS

CREATE TABLE public.address
(
  id bigint NOT NULL GENERATED ALWAYS AS IDENTITY ( INCREMENT 1 START 1 MINVALUE 1 MAXVALUE 9223372036854775807 CACHE 1 ),
  city_id bigint,
  street text COLLATE pg_catalog."default",
  building text COLLATE pg_catalog."default",
  CONSTRAINT address_pkey PRIMARY KEY (id),
  CONSTRAINT fk_city_id FOREIGN KEY (city_id)
    REFERENCES public.city (id) MATCH SIMPLE
    ON UPDATE NO ACTION
    ON DELETE NO ACTION
    NOT VALID
);


-- CONTACT

CREATE TABLE public.contact
(
  id bigint NOT NULL GENERATED ALWAYS AS IDENTITY ( INCREMENT 1 START 1 MINVALUE 1 MAXVALUE 9223372036854775807 CACHE 1 ),
  first_name character varying(100) COLLATE pg_catalog."default",
  middle_name character varying(100) COLLATE pg_catalog."default",
  last_name character varying(100) COLLATE pg_catalog."default",
  company text COLLATE pg_catalog."default",
  email character varying(100) COLLATE pg_catalog."default",
  address_id bigint,
  phone_id bigint NOT NULL,
  note text COLLATE pg_catalog."default",
  CONSTRAINT contact_pkey PRIMARY KEY (id),
  CONSTRAINT fk_address_id FOREIGN KEY (address_id)
    REFERENCES public.address (id) MATCH SIMPLE
    ON UPDATE NO ACTION
    ON DELETE NO ACTION
    NOT VALID,
  CONSTRAINT fk_phone_id FOREIGN KEY (phone_id)
    REFERENCES public.phone (id) MATCH SIMPLE
    ON UPDATE NO ACTION
    ON DELETE NO ACTION
    NOT VALID
);


-- USERS

CREATE TABLE public.users
(
  id bigint NOT NULL GENERATED ALWAYS AS IDENTITY ( INCREMENT 1 START 1 MINVALUE 1 MAXVALUE 9223372036854775807 CACHE 1 ),
  username character varying(100) COLLATE pg_catalog."default" NOT NULL,
  password character varying(100) COLLATE pg_catalog."default" NOT NULL,
  email character varying(100) COLLATE pg_catalog."default",
  telephone_number character varying(100) COLLATE pg_catalog."default",
  CONSTRAINT users_pkey PRIMARY KEY (id),
  CONSTRAINT unique_email UNIQUE (email),
  CONSTRAINT unique_telephone_number UNIQUE (telephone_number),
  CONSTRAINT unique_username UNIQUE (username)
);



-- USER_CONTACT

CREATE TABLE public.user_contact
(
  id bigint NOT NULL GENERATED ALWAYS AS IDENTITY ( INCREMENT 1 START 1 MINVALUE 1 MAXVALUE 9223372036854775807 CACHE 1 ),
  user_id bigint NOT NULL,
  contact_id bigint NOT NULL,
  CONSTRAINT fk_contact_id FOREIGN KEY (contact_id)
    REFERENCES public.contact (id) MATCH SIMPLE
    ON UPDATE NO ACTION
    ON DELETE NO ACTION
    NOT VALID,
  CONSTRAINT fk_user_id FOREIGN KEY (user_id)
    REFERENCES public.users (id) MATCH SIMPLE
    ON UPDATE NO ACTION
    ON DELETE NO ACTION
    NOT VALID
);
