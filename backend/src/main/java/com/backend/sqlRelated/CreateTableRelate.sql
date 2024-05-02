create table TotalBills(
    serviceId varchar(100),
    customerId varchar(30),
    customerGender varchar(10),
    customerName varchar(255),
    contactNumber varchar(20),
    totalFee double,
    primary key(serviceId)
)



create table Rooms(
    roomId varchar(10),
    roomType varchar(50),
    checkinDate TIMESTAMP,
    customerId varchar(30),
    customerGender varchar(10),
    checkinStatus BIT,
    primary key (roomId)
)

