<!-- JSP d'initialisation de la connexion LDAP -->

<!-- code � appeler d�s l'entr�e dans l'application :
- r�cup�ration de la configuration de connectionLdap
- Si LDAP connect� au serveur HTTP 
	R�cup�ration du matricule pass� dans la requ�te ou simulation WTE
- Sinon
	Simulation de la popup d'authentification
	R�cup�ration du matricule pass� dans la requ�te
- Appel de l'action de traitement de la connection
-->
<!DOCTYPE HTML PUBLIC "-//W3C//DTD HTML 4.01 Transitional//EN">
<HTML>
<HEAD>
<META http-equiv="Content-Type" content="text/html; charset=ISO-8859-1">
<TITLE>index.jsp</TITLE>
</HEAD>
<BODY>

<jsp:forward page="/login.do"></jsp:forward>
</BODY>
</HTML>
