create table if not exists users (
    user_id serial primary key,
    email varchar(200) not null unique,
    password text not null,
    role varchar(50) not null,
    created_at timestamp default CURRENT_TIMESTAMP
);

create table if not exists customers (
    customer_id serial primary key,
    user_id int,
    first_name varchar(50) not null,
    last_name varchar(50) not null,
    phone_number varchar(15) not null,
    foreign key (user_id) references users(user_id)
);

create table if not exists insurance (
    insurance_id serial primary key,
    name varchar(50) not null,
    description text not null,
    price_per_day decimal(10, 2) not null,
    coverage_start_date date not null,
    coverage_end_date date not null
);


create table if not exists vehicles (
    vehicle_id serial primary key,
    insurance_id int,
    brand varchar(50) not null,
    model varchar(80) not null,
    year_of_manufacture int,
    registration_number varchar(30) unique not null,
    status varchar(20) not null check (status in ('available', 'rented', 'in service')),
    price_per_day decimal(10, 2) not null,
    mileage int,
    last_service_date date,
    image_url VARCHAR(255),
    foreign key (insurance_id) references insurance(insurance_id)
);

CREATE TABLE IF NOT EXISTS vehicles (
    vehicle_id SERIAL PRIMARY KEY,
    insurance_id INT,
    brand VARCHAR(50) NOT NULL,
    model VARCHAR(80) NOT NULL,
    year_of_manufacture INT,
    registration_number VARCHAR(30) UNIQUE NOT NULL,
    status VARCHAR(20) NOT NULL CHECK (status IN ('available', 'rented', 'in service')),
    price_per_day DECIMAL(10, 2) NOT NULL,
    mileage INT,
    last_service_date DATE,
    image_url VARCHAR(255),
    seats INT,
    engine_capacity DECIMAL(3, 1),
    engine_power INT,
    fuel_consumption DECIMAL(3, 1),
    air_conditioning BOOLEAN,
    FOREIGN KEY (insurance_id) REFERENCES insurance(insurance_id)
);


create table if not exists payments (
    payment_id serial primary key,
    rental_id int not null,
    amount decimal(10, 2) not null,
    payment_date date not null,
    payment_method varchar(50) not null,
    foreign key (rental_id) references rentals(rental_id)
);

create table if not exists reviews (
    review_id serial primary key,
    rental_id int not null,
    rating int check (
        rating >= 1
        and rating <= 5
    ),
    comment text,
    foreign key (rental_id) references rentals(rental_id)
);

