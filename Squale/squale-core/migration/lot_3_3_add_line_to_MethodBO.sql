-- Num�ro de ligne pour les m�thodes afin de la r�cup�rer pour le plugin Eclipse
alter table Component add StartLine number(10,0) default 0;
commit;