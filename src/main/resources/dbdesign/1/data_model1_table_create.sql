CREATE TABLE public.measure (
    measure_id integer NOT NULL,
    measure_name varchar(100) NOT NULL,
    PRIMARY KEY (measure_id)
);

ALTER TABLE public.measure
    ADD UNIQUE (measure_name);


COMMENT ON TABLE public.measure
    IS 'Мера измерения';
COMMENT ON COLUMN public.measure.measure_id
    IS 'Мера измерения ИД';
COMMENT ON COLUMN public.measure.measure_name
    IS 'Наименование';

CREATE TABLE public.measureunit (
    measureunit_id integer NOT NULL,
    measure_id integer NOT NULL,
    measureunit_name varchar(100) NOT NULL,
    measureunit_shortname varchar(20),
    PRIMARY KEY (measureunit_id)
);

ALTER TABLE public.measureunit
    ADD UNIQUE (measureunit_name);

CREATE INDEX ON public.measureunit
    (measure_id);


ALTER TABLE public.measureunit ADD CONSTRAINT FK_measureunit__measure_id 
  FOREIGN KEY (measure_id) REFERENCES public.measure(measure_id);
