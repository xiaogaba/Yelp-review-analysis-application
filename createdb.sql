create table business(
	bid varchar(25) not null,
	address varchar(110),
	Sun_close int,
	Sun_open int,
	Mon_close int,
	Mon_open int,
	Tue_close int,
	Tue_open int,
	Wed_close int,
	Wed_open int,
	Thu_close int,
	Thu_open int,
	Fri_close int,
	Fri_open int,
	Sat_close int,
	Sat_open int,
	open varchar(8),
	city varchar(25),
	review_count int,
	name varchar(70),
	state varchar(3),
	rate number,
	primary key(bid)
);
CREATE TABLE MainCategory (
    bid     VARCHAR(100),
    mainCategory   VARCHAR(150),
    PRIMARY KEY (bid, mainCategory),
    FOREIGN KEY (bid) REFERENCES Business
);

CREATE TABLE SubCategory (
    bid     VARCHAR(100),
    subCategory     VARCHAR(150),
    PRIMARY KEY (bid, subCategory),
    FOREIGN KEY (bid) REFERENCES Business
);


create table attribute(
	bid varchar(25) not null,
	attribute varchar(50),
 	PRIMARY KEY (bid, attribute),
    	FOREIGN KEY (bid) REFERENCES business
);


create table yelp_user(
	user_id varchar(25) not null,
	name varchar(40),
	primary key(user_id)
);

create table review(
	rid varchar(25) not null,
	reviewDate varchar(15),
	stars int,
	text Clob,
	funny int,
	useful int,
	cool int,
	user_id varchar(25) not null,
	bid varchar(25) not null,
	primary key(rid),
	FOREIGN KEY (bid) REFERENCES business,
    	FOREIGN KEY (user_id) REFERENCES yelp_user
);

create table yelp_checkin(
	bid varchar(25) not null,
	checkin int,
	primary key(bid),
	foreign key(bid) references business
);

	
create index mainCategory on MainCategory(mainCategory);
create index subCategory on SubCategory(subCategory);
create index attribute on attribute(attribute);
create index name on yelp_user(name);
create index checkin on yelp_checkin(checkin);
create index bid on review(bid);







	

	