/**
 * Copyright (C) 2008-2010, Squale Project - http://www.squale.org
 *
 * This file is part of Squale.
 *
 * Squale is free software: you can redistribute it and/or modify
 * it under the terms of the GNU Lesser General Public License as
 * published by the Free Software Foundation, either version 3 of the
 * License, or any later version.
 *
 * Squale is distributed in the hope that it will be useful,
 * but WITHOUT ANY WARRANTY; without even the implied warranty of
 * MERCHANTABILITY or FITNESS FOR A PARTICULAR PURPOSE.  See the
 * GNU General Public License for more details.
 *
 * You should have received a copy of the GNU Lesser General Public License
 * along with Squale.  If not, see <http://www.gnu.org/licenses/>.
 */
package org.squale.squalix.util.parser;

import org.squale.squalecommon.enterpriselayer.businessobject.component.ClassBO;
import org.squale.squalecommon.enterpriselayer.businessobject.component.MethodBO;

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
