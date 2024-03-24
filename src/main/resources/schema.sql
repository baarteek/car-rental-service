create table if not exists Customers (
    customerID serial primary key,
    firstName varchar(50) not null,
    lastName varchar(50) not null,
    email varchar(50) not null unique,
    password text not null,
    phoneNumber varchar(15) not null,
    createdAt timestamp without time zone default(now() at time zone 'utc')
);

create table if not exists Vehicles (
    vehicleID serial primary key,
    brand varchar(50) not null,
    model varchar(80) not null,
    yearOfManufacture int,
    registrationNumber varchar(30) unique not null,
    status varchar(20) not null check (status in ('available', 'rented', 'in service')),
    pricePerDay decimal(10, 2) not null,
    mileage int,
    lastServiceDate date
);

create table if not exists Rentals (
    rentalID serial primary key,
    customerID int not null,
    vehicleID int not null,
    startDate date not null,
    endDate date not null,
    status varchar(50) not null check (status in ('active', 'completed')),
    createdAt timestamp without time zone default(now() at time zone 'utc'),
    notes text,
    foreign key (customerID) references Customers(customerID),
    foreign key (vehicleID) references Vehicles(vehicleID)
);

create table if not exists Payments (
    paymentID serial primary key,
    rentalID int not null,
    amount decimal(10, 2) not null,
    paymentDate date not null,
    paymentMethod varchar(50) not null,
    foreign key (rentalID) references Rentals(rentalID)
);

create table if not exists Reviews (
    reviewID serial primary key,
    rentalID int not null,
    rating int check(
        rating >= 1
        and rating <= 5
    ),
    comment text,
    foreign key (rentalID) references Rentals(rentalID)
);