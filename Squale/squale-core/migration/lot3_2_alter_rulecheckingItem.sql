-- Ajout des deux colonnes n�cessaires � l'export IDE
-- Nom du fichier
alter table RuleCheckingTransgressionItem add Path varchar2(3000);
-- Num�ro de ligne
alter table RuleCheckingTransgressionItem add Line number(10,0) default 0 not null;