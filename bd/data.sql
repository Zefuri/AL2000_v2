drop table Signalement ;
drop table Historique ;
drop table Locations ;
drop table Clients ;
drop table Techniciens ;
drop table DVDs ;


create table Clients (
	idC number(3),
	numCB varchar2(20),
	email varchar2(20),
	mdp varchar2(20),
        credit number(3),
	constraint Clients_C primary key (idC)
);


create table Techniciens (
	idT number(3),
	mdp varchar2(20),
	constraint Techniciens_C primary key (idT)
);

create table DVDs (
        idD number(3),
	title varchar2(100),
	genre varchar2(50),
	releaseYear number(4),
	producer varchar2(50),
        actors varchar2(500),
        summary varchar2(500),
        urlImage varchar2(50),
	constraint DVDs_C primary key (idD)
);


create table Locations (
        idLocation number(3),
        idDvd number(3),
	idClient number(3),
	dateLocation date,
	constraint Locations_C primary key (idLocation),
	constraint Locations_C1 foreign key (idDvd) references DVDs (idD),
        constraint Locations_C2 foreign key (idClient) references Clients (idC)
);

create table Historique (
        idLocation number(3),
        idDvd number(3),
	idClient number(3),
	dateLocation date,
	dateRetour date,
	constraint Historique_C primary key (idLocation),
	constraint Historique_C1 foreign key (idDvd) references DVDs (idD),
        constraint Historique_C2 foreign key (idClient) references Clients (idC)
);

create table Signalement (
        idLocation number(3),
        signalement varchar2(500),
	constraint Signalement_C foreign key (idLocation) references Historique (idLocation)
);



insert into Techniciens values (123,'azerty');
insert into Techniciens values (111,'wxcvbn');
