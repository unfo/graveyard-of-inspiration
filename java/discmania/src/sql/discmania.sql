create table discmania_user (
	login    VARCHAR(20) PRIMARY KEY NOT NULL,
	password VARCHAR(20) NOT NULL,
	name     VARCHAR(50) NOT NULL,
	address  VARCHAR(50),
	phone    VARCHAR(25),
	email    VARCHAR(50),
	type     INTEGER
);

create sequence disc_seq;

create table disc (
    id      INTEGER         PRIMARY KEY NOT NULL DEFAULT nextval('disc_seq'),
    artist  VARCHAR(50)     NOT NULL,
    name    VARCHAR(100)    NOT NULL,
    price   NUMERIC(10, 2)  NOT NULL DEFAULT 0,
    balance INTEGER         NOT NULL DEFAULT 0,
    removed CHAR(1)         DEFAULT '0'
);

create table track (
    disc_id INTEGER     REFERENCES disc,
    number  INTEGER     NOT NULL,
    name    VARCHAR(50) NOT NULL,
    PRIMARY KEY(disc_id, number)
);

create sequence order_seq;

create table discmania_order (
    id          INTEGER PRIMARY KEY DEFAULT nextval('order_seq'),
    arrived     TIMESTAMP,
    processed   TIMESTAMP,
    user_id     VARCHAR(20) NOT NULL REFERENCES discmania_user ON DELETE CASCADE
);

create table order_disc (
    disc_id     INTEGER NOT NULL REFERENCES disc,
    order_id    INTEGER NOT NULL REFERENCES discmania_order ON DELETE CASCADE,
    price       DECIMAL(10, 2),
    quantity    INTEGER NOT NULL DEFAULT 1
);
