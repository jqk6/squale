/*
	Selection d'un onglet en retrouvant le <a>...
	pour permettre un lien --> pour la charte v3
*/
function F_MyOngletSelectionner( pBoite, pOnglet)
{
  var onglets = document.getElementById(pBoite);
  // L'expression r�guli�re pour le onclick
  var innerHTML = new RegExp(".*F_OngletSelectionner3\\( '" + pBoite + "', '" + pOnglet + "', this \\).*", "m");
  var linkOn;
  var linkOff;
  // On r�cup�re les onglets
  for( var i = 0; i < onglets.childNodes.length; i++ ) {
   		if ( onglets.childNodes[ i ].tagName == "DIV" && onglets.childNodes[ i ].className=="onglet") {
   			onglets = onglets.childNodes[ i ];
   			break;
   		}
  }
  // on met � on l'onglet d�sir� et � off les autres.
  for( var i = 0; i < onglets.childNodes.length; i++ ) {
  	if ( onglets.childNodes[ i ].tagName == "A") {
   			var currentHTML = "" + onglets.childNodes[ i ].getAttribute("onclick");
   			linkOff = onglets.childNodes[ i ];
   			if(linkOff.className == "on") {
   				linkOff.className=""; // on indique que l'onglet n'est plus s�lectionn�
   			}
   			if(currentHTML.search(innerHTML) != -1) {
   				linkOn = onglets.childNodes[ i ];
   				linkOn.className="on"; // on indique que l'onglet est s�lectionn�
   			} 
   		}
   	}
   	// M�thode de Welcom
   	F_OngletSelectionner3(pBoite, pOnglet, linkOn);
}
/*
	Modifie la valeur de la variable d'id varId par newValue
	Cette m�thode est utile en particulier pour conserver le nom de l'onglet
	s�lectionn�.
*/
function changeVarValue(varId, newValue) {
	document.getElementById(varId).value=newValue;
}
