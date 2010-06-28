--#############################################
--# Modifications for #257

alter table FactorRef
	drop foreign key FK2854B0A4A4AEA807;
	
alter table FactorRef
	drop foreign key FK2854B0A497209814;
	
alter table SqualeReference
	drop foreign key FK32FD7E08499FD217;

alter table FactorRef 
    add constraint FK2854B0A4A4AEA807 
    foreign key (ReferencielId) 
    references SqualeReference (ReferencielId)
    on delete cascade;

alter table FactorRef  
    add constraint FK2854B0A497209814 
    foreign key (Rule) 
    references QualityRule (QualityRuleId)
    on delete cascade;
    
alter table SqualeReference  
    add constraint FK32FD7E08499FD217 
    foreign key (QualityGrid) 
    references QualityGrid (QualityGridId)
    on delete cascade;