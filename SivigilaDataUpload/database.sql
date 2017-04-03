drop table if exists weekdata;
drop table if exists total_per_year;
drop table if exists total_per_month;
drop table if  exists town;
drop table if  exists department;
drop table if exists event;
drop table if exists climatic_p;
drop table if exists population;

PRAGMA foreign_keys = 1;
create table if not exists department(
	id TEXT primary key not null,
	name TEXT not null
);
create table if not exists town (
	id TEXT  not null,
	name TEXT not null,
	id_department TEXT no null,
	foreign key(id_department) references department(id),
	primary key(id,id_department)
);


create table if not exists event (
	id int primary key not null,
	name TEXT
);
create table if not exists weekdata (
	week int not null,
	id_event int  not null,
	id_town TEXT  not null,
	id_department TEXT not null,
	year_data int not null,
	amount int not null,
	primary key(week,id_event,id_town,id_department,year_data),
	foreign key(id_event) references event(id),
	foreign key(id_town,id_department) references town(id,id_department)
);

create table if not exists population (
	id_town  TEXT not null,
	id_department TEXT not null,
	year_data int not null,
	amount int not null,
	foreign key(id_town,id_department) references town(id,id_department)
	primary key(id_town,id_department,year_data)
	

);
create table if not exists climatic_p(
	id int not null,
	start_month string not null,
	end_month string not null,
	start_year int not null,
	end_year int not null,
	type TEXT,
	name TEXT,
	primary key(id)
);

create table if not exists total_per_year(
	id_event int  not null,
	id_town  TEXT not null,
	id_department TEXT not null,
	year_data int not null,
	amount int not null,
	primary key(id_event,id_town,id_department,year_data),
	foreign key(id_event) references event(id),
	foreign key(id_town,id_department) references town(id,id_department)
);

create table if not exists total_per_month(
	id_month int not null,
	id_event int  not null,
	id_town  TEXT not null,
	id_department TEXT not null,
	year_data int not null,
	amount int not null,
	primary key(id_month,id_event,id_town,id_department,year_data),
	foreign key(id_event) references event(id),
	foreign key(id_town,id_department) references town(id,id_department)
);


--select * from town where id_department = '11'
select count(*) from weekdata
