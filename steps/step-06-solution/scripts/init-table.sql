-- Table init script --

DROP TABLE IF EXISTS customers;

CREATE TABLE customers (
  id BIGINT NOT NULL,
  first_name CHARACTER VARYING(255) NOT NULL,
  last_name CHARACTER VARYING(255) NOT NULL,
  age INTEGER NOT NULL,
  email CHARACTER VARYING(255) UNIQUE NOT NULL,
  gender CHARACTER VARYING(255) NOT NULL
);

ALTER TABLE customers ADD PRIMARY KEY (id);

INSERT INTO customers(id, first_name, last_name, age, email, gender) VALUES
(1, 'Yuko', 'Birden', 20, 'Yuko.Birden@sfeir.com', 'FEMALE'),
(2, 'Vertie', 'Pick', 20, 'Vertie.Pick@sfeir.com', 'MALE'),
(3, 'Desire', 'Cavalier', 20, 'Desire.Cavalier@sfeir.com', 'FEMALE'),
(4, 'Teisha', 'Teeter', 20, 'Teisha.Teeter@sfeir.com', 'FEMALE'),
(5, 'Venessa', 'Martello', 20, 'Venessa.Martello@sfeir.com', 'FEMALE'),
(6, 'Connie', 'Trawick', 20, 'Connie.Trawick@sfeir.com', 'MALE'),
(7, 'Cari', 'Dennie', 20, 'Cari.Dennie@sfeir.com', 'MALE'),
(8, 'Kazuko', 'Caylor', 20, 'Kazuko.Caylor@sfeir.com', 'MALE'),
(9, 'Mercy', 'Wenner', 20, 'Mercy.Wenner@sfeir.com', 'MALE'),
(10, 'Bev', 'Teter', 20, 'Bev.Teter@sfeir.com', 'FEMALE'),
(11, 'Letitia', 'Lee', 20, 'Letitia.Lee@sfeir.com', 'FEMALE'),
(12, 'Syreeta', 'Vrieze', 20, 'Syreeta.Vrieze@sfeir.com', 'FEMALE'),
(13, 'Kristeen', 'Tao', 20, 'Kristeen.Tao@sfeir.com', 'FEMALE'),
(14, 'Rudolf', 'Plowman', 20, 'Rudolf.Plowman@sfeir.com', 'MALE'),
(15, 'Sharon', 'Arias', 20, 'Sharon.Arias@sfeir.com', 'FEMALE'),
(16, 'Frieda', 'Massi', 20, 'Frieda.Massi@sfeir.com', 'FEMALE'),
(17, 'Susann', 'Spriggs', 20, 'Susann.Spriggs@sfeir.com', 'FEMALE'),
(18, 'Asha', 'Galyon', 20, 'Asha.Galyon@sfeir.com', 'MALE'),
(19, 'Karl', 'Spagnoli', 20, 'Karl.Spagnoli@sfeir.com', 'MALE'),
(20, 'Lyndsay', 'Woodson', 20, 'Lyndsay.Woodson@sfeir.com', 'FEMALE');
