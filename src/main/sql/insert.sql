-- PHONE_TYPE

INSERT INTO public.phone_type(name)
VALUES ('Mobile'),
       ('Home'),
       ('Work');


-- PHONE

INSERT INTO public.phone(telephone_number, model, phone_type_id)
VALUES ('88005553535', 'IPhone', 1),
       ('2345566', NULL, 2),
       ('88503558595', 'Samsung', 1),
       ('2341556', NULL, 3),
       ('88563551191', 'IPhone', 1);


-- COUNTRY

INSERT INTO public.country(name)
VALUES ('Russia'),
       ('USA'),
       ('Germany'),
       ('Italy'),
       ('South Africa');


-- CITY

INSERT INTO public.city(name, country_id)
VALUES ('Voronezh', 1),
       ('Moscow', 1),
       ('New York',2),
       ('Berlin', 3),
       ('Leipzig', 3);

-- ADDRESS

INSERT INTO public.address(city_id, street, building)
VALUES (1, 'Lenina', '5a'),
       (1, 'Lenina', '20'),
       (2, NULL, NULL),
       (2, 'Street11', '166/B'),
       (3, 'Street5', '10');


-- CONTACT

INSERT INTO public.contact(first_name, middle_name, last_name, company, email, address_id, note, phone_id)
VALUES ('Ivan', 'Inanovich', 'Ivanov', 'DataArt', 'iviviv@mail.ru', 1, 'Some description', 1),
       ('Petr', 'Alekseevich', 'Petrov', 'MAGNIT', 'ppptr@mail.ru', 2, 'Some description2', 2),
       ('Bob', NULL, 'Ekler', 'Apple', 'boooob@gmail.com', 3, NULL, 3),
       ('Jon', 'Inanovich', 'Robinson', 'IBM', 'jooro@mail.com', 4, NULL, 4),
       ('Erick', NULL, NULL, 'Amazon', 'amzn@icloud.com', 5, 'Some description', 5);


-- USERS

INSERT INTO public.users(username, password, email, telephone_number)
VALUES ('user', '1111', NULL, '87004556677'),
       ('user2', '12345', 'user2@bk.ru', NULL);


-- USER_CONTACT

INSERT INTO public.user_contact(user_id, contact_id)
VALUES (1, 1),
       (1, 2),
       (1, 3),
       (1, 4),
       (2, 3),
       (2, 4),
       (2, 5);

