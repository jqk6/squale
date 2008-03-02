package com.airfrance.project4mccabetest;

/**
 * Classe de test permettant de tester l'ex�cution de la t�che McCabe
 * sous Unix.
 * 
 * Cette interface ne fait et ne sert � rien d'autre qu'un h�ritage.
 * 
 * @author M400842
 */
public interface MyNihilistInterface {

    /**
     * M�thode � surcharger pour ne rien faire.
     * 
     * @param pObject l'objet ne servant � rien.
     * @return la valeur boole�nne du vide.
     */
    abstract boolean doNothing(final Object pObject);

}
