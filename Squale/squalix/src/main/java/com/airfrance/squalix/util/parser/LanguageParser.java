package com.airfrance.squalix.util.parser;

import com.airfrance.squalecommon.enterpriselayer.businessobject.component.ClassBO;
import com.airfrance.squalecommon.enterpriselayer.businessobject.component.MethodBO;

/**
 * Parse les noms enti�remement qualifi� et les remplace par les objets correspondants.
 */
public interface LanguageParser
{

    /* ################ D�composition et transformation en objet correspondant ################ */

    /**
     * D�compose la m�thode pour construire l'objet MethodBO avec ses parents.
     * 
     * @param pAbsoluteMethodName le nom absolu de la m�thode
     * @param pFileName le nom absolu du fichier � partir du projet
     * @return la m�thode correspondant aux param�tres
     */
    public MethodBO getMethod( String pAbsoluteMethodName, String pFileName );

    /**
     * D�compose la classe pour construire l'objet ClassBO avec ses parents.
     * 
     * @param pAbsoluteClassName le nom enti�rement qualifi� d'une classe
     * @return la classe sous forme de ClassBO
     */
    public ClassBO getClass( String pAbsoluteClassName );

    /**
     * Retourne la cha�ne pAbsoluteName avant le dernier s�parateur ou null si il n'y a pas de s�parateur.
     * 
     * @param pAbsoluteName le nom absolu du fils
     * @return le nom absolu du parent
     */
    public String getParentName( String pAbsoluteName );
}
