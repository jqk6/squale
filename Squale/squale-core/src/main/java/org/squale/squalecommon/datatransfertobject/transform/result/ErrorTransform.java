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
 * Cr�� le 12 juil. 05
 *
 * Pour changer le mod�le de ce fichier g�n�r�, allez � :
 * Fen�tre&gt;Pr�f�rences&gt;Java&gt;G�n�ration de code&gt;Code et commentaires
 */
package org.squale.squalecommon.datatransfertobject.transform.result;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.List;

import org.squale.squalecommon.datatransfertobject.result.ErrorDTO;
import org.squale.squalecommon.enterpriselayer.businessobject.component.AuditBO;
import org.squale.squalecommon.enterpriselayer.businessobject.component.ProjectBO;
import org.squale.squalecommon.enterpriselayer.businessobject.result.ErrorBO;

/**
 * @author M400841 Pour changer le mod�le de ce commentaire de type g�n�r�, allez � :
 *         Fen�tre&gt;Pr�f�rences&gt;Java&gt;G�n�ration de code&gt;Code et commentaires
 */
public class ErrorTransform
    implements Serializable
{

    /**
     * Constructeur prive
     */
    private ErrorTransform()
    {
    }

    /**
     * Copie compl�te de la DTO � la BO DTO -> BO pour un Error
     * 
     * @param pErrorDTO ErrorDTO � transformer
     * @return ErrorBO
     */
    public static ErrorBO dto2Bo( ErrorDTO pErrorDTO )
    {

        // Initialisation du retour et la tache
        ErrorBO errorBO = new ErrorBO();
        errorBO.setTaskName( pErrorDTO.getTaskName() );

        // Initialisation de l'audit
        AuditBO auditBO = new AuditBO();
        auditBO.setId( pErrorDTO.getAuditId() );

        // Initialisation du sous-projet
        ProjectBO projectBO = new ProjectBO();
        projectBO.setId( pErrorDTO.getProjectId() );

        errorBO.setAudit( auditBO );
        errorBO.setProject( projectBO );
        errorBO.setLevel( pErrorDTO.getLevel() );

        return errorBO;

    }

    /**
     * Est utilis� pour remonter une collection d'erreurs aux AC BO -> DTO pour un Error
     * 
     * @param pErrorBO ErrorBO
     * @return ErrorDTO
     */
    public static ErrorDTO bo2Dto( ErrorBO pErrorBO )
    {

        // Initialisation du retour
        ErrorDTO errorDTO = new ErrorDTO();

        errorDTO.setAuditId( pErrorBO.getAudit().getId() );
        errorDTO.setLevel( pErrorBO.getLevel() );
        errorDTO.setMessageKey( pErrorBO.getMessage() );
        errorDTO.setTaskName( pErrorBO.getTaskName() );
        errorDTO.setProjectId( pErrorBO.getProject().getId() );

        return errorDTO;

    }

    /**
     * Transforms a list of ErrorBO in ErroDTO
     * 
     * @param pErrorsBO list of ErrorBO
     * @return list of ErrorDTO
     */
    public static List bo2Dto( List pErrorsBO )
    {
        List errorsDTO = new ArrayList();
        for ( int i = 0; i < pErrorsBO.size(); i++ )
        {
            errorsDTO.add( bo2Dto( (ErrorBO) pErrorsBO.get( i ) ) );
        }
        return errorsDTO;
    }

}
