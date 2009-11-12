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
package org.squale.squalix.tools.compiling.java.compiler.impl;

import java.util.List;

import org.squale.squalecommon.enterpriselayer.businessobject.component.parameters.MapParameterBO;
import org.squale.squalix.tools.compiling.java.adapter.JComponentAdapter;
import org.squale.squalix.tools.compiling.java.compiler.eclipse.EclipseCompiler;

/**
 * Classe d'impl�mentation pour le compilater Eclipse
 */
public class EclipseCompilerImpl
    extends JComponentAdapter
{

    /**
     * Compilateur pour Eclipse.
     */
    private EclipseCompiler eclipseCompiler;

    /**
     * M�thode de lancement de la compilation.
     * 
     * @throws Exception exception.
     */
    public void execute()
        throws Exception
    {
        eclipseCompiler.runCompilation();
        // On v�rifie les variables
        eclipseCompiler.checkOutputVariables();
        // On modifie les erreurs
        setErrors( eclipseCompiler.getErrors() );
    }

    /**
     * Constructeur par d�faut.
     * 
     * @param pProjectList liste des projets WSAD � compiler.
     * @param pViewPath le view path
     * @param eclipseParams eclipse parameters
     */
    public EclipseCompilerImpl( List pProjectList, String pViewPath, MapParameterBO eclipseParams )
    {
        eclipseCompiler = new EclipseCompiler( pProjectList, pViewPath, eclipseParams );
    }

    /**
     * @return le compileur
     */
    public EclipseCompiler getEclipseCompiler()
    {
        return eclipseCompiler;
    }

}
