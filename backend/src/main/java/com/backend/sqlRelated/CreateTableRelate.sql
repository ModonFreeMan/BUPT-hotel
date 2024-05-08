create table customers
(
    customerId     char(18)    not null
        primary key,
    contactNumber  varchar(30) null,
    customerName   varchar(30) null,
    customerGender varchar(30) null
);

create table detailedbills
(
    serviceId  varchar(255) not null
        primary key,
    endTem     double       null,
    fee        double       null,
    rate       double       null,
    roomId     varchar(30)  null,
    speedLevel int          null,
    startTem   double       null,
    startTime  datetime     null,
    requestTime datetime    null,
    serviceLength varchar(30)  null
);

create table rooms
(
    roomId         varchar(10) not null
        primary key,
    roomType       varchar(50) null,
    checkinDate    date        null,
    customerId     varchar(30) null,
    customerGender varchar(10) null,
    checkinStatus  tinyint(1)  null
);

create table statistics
(
    date            date     null,
    detailedBillSum int      null,
    dispatchSum     int      null,
    requestLength   int      null,
    roomId          int      null,
    speedChangeSum  int      null,
    switchSum       int      null,
    temChangeSum    int      null,
    totalFee        int      null
);

create table totalbills
(
    serviceId    varchar(100) not null
        primary key,
    customerId   varchar(30)  null,
    customerName varchar(255) null,
    totalFee     double       null,
    acFee        double       null,
    days         int          null,
    roomFee      double       null,
    roomId       varchar(20)  null,
    roomType     varchar(255) null comment '房间类型'
);

