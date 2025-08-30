create table categories (
  id bigserial primary key,
  slug varchar(160) unique not null,
  name_tr varchar(160) not null,
  name_en varchar(160) not null,
  created_at timestamp not null default now()
);

create table products (
  id bigserial primary key,
  category_id bigint references categories(id),
  slug varchar(180) unique not null,
  title_tr varchar(200) not null,
  title_en varchar(200) not null,
  description_tr text,
  description_en text,
  price numeric(12,2) not null,
  currency varchar(8) not null default 'TRY',
  stock integer not null default 0,
  created_at timestamp not null default now(),
  updated_at timestamp not null default now()
);

create table product_images (
  id bigserial primary key,
  product_id bigint references products(id) on delete cascade,
  url text not null,
  alt text
);

create table product_variants (
  id bigserial primary key,
  product_id bigint references products(id) on delete cascade,
  color varchar(64),
  size varchar(64),
  sku varchar(64),
  extra_price numeric(12,2) not null default 0
);

create table coupons (
  id bigserial primary key,
  code varchar(64) unique not null,
  percent_off int,
  amount_off numeric(12,2),
  active boolean not null default true,
  valid_until date
);

create table order_drafts (
  id bigserial primary key,
  draft_code varchar(40) not null,
  whatsapp_phone varchar(32) not null,
  full_name varchar(160),
  email varchar(160),
  phone varchar(32),
  address text,
  shipping_pref varchar(80),
  note text,
  cart_json jsonb not null,
  subtotal numeric(12,2) not null,
  coupon_code varchar(64),
  utm jsonb,
  status varchar(40) not null default 'draft_whatsapp',
  created_at timestamp not null default now()
);

create index idx_products_category on products(category_id);
create index idx_order_drafts_created on order_drafts(created_at);

