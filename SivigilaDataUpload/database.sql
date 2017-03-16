drop table if exists weekdata;
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
	year_data date not null,
	amount int not null,
	foreign key(id_town) references town(id),
	primary key(id_town,year_data)
	

);
create table if not exists climatic_p(
	id date not null,
	start_date date not null,
	end_date date not null,
	type TEXT,
	name TEXT,
	primary key(id)
);

select * from town where id_department = '11'
select count(*) from weekdata