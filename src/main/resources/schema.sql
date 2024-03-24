create table if not exists customers (
    customer_id serial primary key,
    first_name varchar(50) not null,
    last_name varchar(50) not null,
    email varchar(50) not null unique,
    password text not null,
    phone_number varchar(15) not null,
    created_at date
);

create table if not exists vehicles (
    vehicle_id serial primary key,
    brand varchar(50) not null,
    model varchar(80) not null,
    year_of_manufacture int,
    registration_number varchar(30) unique not null,
    status varchar(20) not null check (status in ('available', 'rented', 'in service')),
    price_per_day decimal(10, 2) not null,
    mileage int,
    last_service_date date
);

create table if not exists rentals (
    rental_id serial primary key,
    customer_id int not null,
    vehicle_id int not null,
    start_date date not null,
    end_date date not null,
    status varchar(50) not null check (status in ('active', 'completed')),
    created_at date,
    notes text,
    foreign key (customer_id) references customers(customer_id),
    foreign key (vehicle_id) references vehicles(vehicle_id)
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
