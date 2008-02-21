create table UserAccess (
	UserAccessId number(19,0) not null,
    accessDate date not null,
    matricule varchar2(1024) not null,
    ApplicationId number(19,0),
    AccessIndex number(10,0)
);

-- Cl� �trang�re
alter table UserAccess add constraint FKUserAccessApplication foreign key (ApplicationId) references Component;

-- Ajoute la PK dans le bon tablespace
alter table UserAccess add constraints pk_UserAccess primary key (UserAccessId) using index tablespace squale_ind;

-- Index sur la cl� �trang�re
create index UserAccess_Component on UserAccess(ApplicationId) tablespace squale_ind;

-- S�quence pour la table
create sequence userAccess_sequence;