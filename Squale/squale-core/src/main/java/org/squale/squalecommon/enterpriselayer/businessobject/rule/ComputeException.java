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
/*
 * Cr�� le 5 ao�t 05
 *
 * Pour changer le mod�le de ce fichier g�n�r�, allez � :
 * Fen�tre&gt;Pr�f�rences&gt;Java&gt;G�n�ration de code&gt;Code et commentaires
 */
package org.squale.squalecommon.enterpriselayer.businessobject.rule;

import org.apache.commons.lang.exception.NestableException;

/**
 * @author M400843
 */
public class ComputeException
    extends NestableException
{
    /**
     * Construit une ComputeException avec le message sp�cifi�
     * 
     * @param pMessage Le message
     */
    public ComputeException( String pMessage )
    {
        super( pMessage );
    }

    /**
     * Construit une ComputeException avec la cause sp�cifi�e
     * 
     * @param pException La cause de l'exception
     */
    public ComputeException( Throwable pException )
    {
        super( pException );
    }
}
