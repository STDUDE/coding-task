ALTER TABLE public.data_item
    ALTER COLUMN description SET NOT NULL;

ALTER TABLE public.data_item
    ALTER COLUMN name SET NOT NULL;

ALTER TABLE public.data_item
    ALTER COLUMN updated SET NOT NULL;