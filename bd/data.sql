drop table Signalement;
drop table historique;
drop table Locations;
drop table Techniciens;
drop table DVDs;
drop table Abonne;
drop table Client;

create table DVDs (
    idD number(3),
	title varchar2(100),
	genre varchar2(50),
	releaseYear number(4),
	producer varchar2(50),
    actors varchar2(500),
    summary varchar2(500),
    urlImage varchar2(50),
    dispoloc number(1),
	constraint DVDs_C primary key (idD)
);

create table Abonne (
	idC number(3),
	email varchar(50),
	numCB varchar(20),
	mdp varchar(20),
    credit number(20),
	constraint Abonne_C primary key (idC)
);

create table Client (
	idC number(3),
	email varchar(50),
	numCB varchar(20),
	constraint Clients_C primary key (idC)
);


create table Techniciens (
	idT number(3),
	mdp varchar2(20),
	constraint Techniciens_C primary key (idT)
);




create table Locations (
    idLocation number(10),
    idDvd number(3),
	idClient number(3),
	idAbonne number(3),
	dateLocation date,
	constraint Locations_C primary key (idLocation),
	constraint Locations_C1 foreign key (idDvd) references DVDs (idD),
    constraint Locations_C2 foreign key (idAbonne) references Abonne (idC),
    constraint Locations_C3 foreign key (idClient) references Client (idC)
);

create table Historique (
    idLocation number(10),
    idDvd number(3),
	idAbonne number(3),
	dateLocation date,
	dateRetour date,
	constraint Historique_C primary key (idLocation),
	constraint Historique_C1 foreign key (idDvd) references DVDs (idD),
    constraint Historique_C2 foreign key (idAbonne) references Abonne (idC)
);

create table Signalement (
        idLocation number(3),
        signalement varchar2(500),
        montant number(5),
	constraint Signalement_C foreign key (idLocation) references Historique (idLocation)
);



insert into Techniciens values (123,'azerty');
insert into Techniciens values (111,'wxcvbn');
