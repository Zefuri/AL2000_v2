drop table Signalement ;
drop table Historique ;
drop table Locations ;
drop table Clients ;
drop table Abonnes;
drop table Techniciens ;
drop table DVDs ;


create table Clients (
	numCB varchar2(20),
	email varchar2(20),
	constraint Clients_C primary key (numCB, email)
);

create table Abonnes (
	idA number(3),
	email varchar2(20),
	mdp varchar2(20),
    credit number,
	constraint Abonnes_C primary key (idA)
);


create table Techniciens (
	idT number(3),
	mdp varchar2(20),
	constraint Techniciens_C primary key (idT)
);

create table DVDs (
        idD number(3),
	title varchar2(50),
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

insert into Clients values (001,'AX40 400 344 433','boukrisw@gmail.com','azertyuiop',60);

insert into DVDs values (001,'Avatar','Science-fiction',2009,'James Cameron','Sam Worthington,Zoe Saldana,Sigourney Weaver,Stephen Lang,Michelle Rodríguez','Malgre sa paralysie, Jake Sully, un ancien marine immobilisé dans un fauteuil roulant, est reste un combattant au plus profond de son etre. Il est recruté pour se rendre a des annees-lumiere de la Terre, sur Pandora, ou de puissants groupes industriels exploitent un minerai rarissime destine à resoudre la crise energetique sur Terre.', '../Avatar.png');

insert into Locations values (001,001,001,TO_DATE('13/11/2020', 'DD/MM/YYYY'));
 
