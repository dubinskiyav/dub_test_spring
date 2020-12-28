CREATE TABLE measure (
    measure_id integer NOT NULL,
    measure_name varchar(100) NOT NULL,
    PRIMARY KEY (measure_id)
);

ALTER TABLE measure
    ADD UNIQUE (measure_name);


COMMENT ON TABLE measure
    IS 'Мера измерения';
COMMENT ON COLUMN measure.measure_id
    IS 'Мера измерения ИД';
COMMENT ON COLUMN measure.measure_name
    IS 'Наименование';

CREATE TABLE measureunit (
    measureunit_id integer NOT NULL,
    measure_id integer NOT NULL,
    measureunit_name varchar(100) NOT NULL,
    measureunit_shortname varchar(20),
    PRIMARY KEY (measureunit_id)
);

ALTER TABLE measureunit
    ADD UNIQUE (measureunit_name);

CREATE INDEX ON measureunit
    (measure_id);


COMMENT ON TABLE measureunit
    IS 'Единица измерения';
COMMENT ON COLUMN measureunit.measureunit_id
    IS 'ид';
COMMENT ON COLUMN measureunit.measure_id
    IS 'Единица измерения Ссылка';
COMMENT ON COLUMN measureunit.measureunit_name
    IS 'Наименование';
COMMENT ON COLUMN measureunit.measureunit_shortname
    IS 'Сокращение';

CREATE TABLE mainmeasureunit (
    mainmeasureunit_id integer NOT NULL,
    PRIMARY KEY (mainmeasureunit_id)
);


COMMENT ON TABLE mainmeasureunit
    IS 'Главная единица для меры';
COMMENT ON COLUMN mainmeasureunit.mainmeasureunit_id
    IS 'Единица измерения Ссылка';

CREATE TABLE measureunitrecalc (
    measureunitrecalc_id integer NOT NULL,
    mainmeasureunit_id integer NOT NULL,
    measureunit_id integer NOT NULL,
    measureunitrecalc_conversionfactor real NOT NULL,
    PRIMARY KEY (measureunitrecalc_id)
);

ALTER TABLE measureunitrecalc
    ADD UNIQUE (mainmeasureunit_id, measureunit_id);


COMMENT ON TABLE measureunitrecalc
    IS 'Пересчет единиц измерения';
COMMENT ON COLUMN measureunitrecalc.measureunitrecalc_id
    IS 'Пересчет единиц измерения ИД';
COMMENT ON COLUMN measureunitrecalc.mainmeasureunit_id
    IS 'Главная единица измерения Ссылка';
COMMENT ON COLUMN measureunitrecalc.measureunit_id
    IS 'Единица измерения Ссылка';
COMMENT ON COLUMN measureunitrecalc.measureunitrecalc_conversionfactor
    IS 'Коэффициент пересчета (сколько главных единиц в единице)';

ALTER TABLE measureunit ADD CONSTRAINT FK_measureunit__measure_id FOREIGN KEY (measure_id) REFERENCES measure(measure_id);
ALTER TABLE measureunitrecalc ADD CONSTRAINT FK_measureunitrecalc__mainmeasureunit_id FOREIGN KEY (mainmeasureunit_id) REFERENCES mainmeasureunit(mainmeasureunit_id);
ALTER TABLE measureunitrecalc ADD CONSTRAINT FK_measureunitrecalc__measureunit_id FOREIGN KEY (measureunit_id) REFERENCES measureunit(measureunit_id);