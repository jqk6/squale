-- Ajout de la colonne pr�cisant si l'utilisateur est abonn� ou non aux mails automatiques
alter table UserBO add unsubscribed number(1) default 0 not null;