DROP TABLE BRAND IF EXISTS;
CREATE TABLE BRAND (
  ID bigint NOT NULL,
  BRAND varchar(255),
  PRIMARY KEY (ID)
);

DROP TABLE CURRENCY IF EXISTS;
CREATE TABLE CURRENCY(
  ID bigint NOT NULL,
  CURR varchar(255),
  PRIMARY KEY (ID)
);

DROP TABLE PRODUCT IF EXISTS;
CREATE TABLE PRODUCT(
  ID bigint NOT NULL,
  ITEM varchar(255),
  PRIMARY KEY (ID)
);

DROP TABLE PRICES IF EXISTS;
CREATE TABLE PRICES (
  ID bigint NOT NULL,
  BRAND_ID bigint NOT NULL,
  START_DATE TIMESTAMP,
  END_DATE TIMESTAMP,
  PRICE_LIST int,
  PRODUCT_ID bigint,
  PRIORITY int,
  PRICE float,
  CURR varchar(255),
  PRIMARY KEY (ID),
  FOREIGN KEY (BRAND_ID) REFERENCES BRAND(ID),
  FOREIGN KEY (PRODUCT_ID) REFERENCES PRODUCT(ID)
);










