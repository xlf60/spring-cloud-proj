use employee;
CREATE TABLE employee (
            id int(11) NOT NULL,
            activityType varchar(255) NOT NULL,
            employee_id int(11) NOT NULL,
            startTime date DEFAULT NULL,
            endTime date DEFAULT NULL,
            active varchar(255) DEFAULT NULL,
            PRIMARY KEY (id)
);