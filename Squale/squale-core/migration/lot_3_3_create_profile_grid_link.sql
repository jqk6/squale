-- On cr�e la table
create table Profiles_Grids (
	GridId number(19,0) not null,
	ProfileId number(19,0) not null
);
    
-- avec sa cl� primaire
alter table Profiles_Grids add constraints pk_ProfilesGrids primary key (GridId, ProfileId) using index tablespace squale_ind;
-- sa s�quence
create sequence ProfilesGrids_sequence;
-- les cl�s �trang�res
alter table Profiles_Grids add constraint FK_profilesGrids_grid foreign key (GridId) references QualityGrid;
alter table Profiles_Grids add constraint FK_profilesGrids_profile foreign key (ProfileId) references Tasks_User;
