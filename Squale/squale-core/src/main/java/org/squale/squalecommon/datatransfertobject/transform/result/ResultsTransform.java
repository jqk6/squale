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
import java.util.Collection;
import java.util.Iterator;
import java.util.List;

import org.squale.squalecommon.datatransfertobject.result.ResultsDTO;
import org.squale.squalecommon.enterpriselayer.businessobject.result.MarkBO;
import org.squale.squalecommon.enterpriselayer.businessobject.result.MeasureBO;
import org.squale.squalecommon.enterpriselayer.businessobject.result.QualityResultBO;

/**
 * @author M400841 Pour changer le mod�le de ce commentaire de type g�n�r�, allez � :
 *         Fen�tre&gt;Pr�f�rences&gt;Java&gt;G�n�ration de code&gt;Code et commentaires
 */
public class ResultsTransform
    implements Serializable
{

    /**
     * Constructeur prive
     */
    private ResultsTransform()
    {
    }

    /**
     * DTO -> BO pour un Results
     * 
     * @param pResultsDTO ResultsDTO � transformer
     * @return ResultsBO
     * @deprecated non utilis� car les r�sultats ne seront pas modifi�s par la facade
     */
    /* TODO BEF --> deprecated method to suppress
    public static List dto2Bo( ResultsDTO pResultsDTO )
    {

        // Initialisation
        List list = null; // retour de la methode

        pResultsDTO = null; // traitement du parametre

        // Non impl�ment� car les r�sultats ne seront pas modifi�s par la facade 

        return list;
    }*/

    /**
     * Permet de convertir la liste d'objets metiers en valeurs
     * 
     * @param pResultsBO liste des valeurs a mettre en valeurs dans la HashMap de ResultsDTO
     * @return ResultsDTO avec HashMap modifi�
     */
    public static List bo2Dto( Collection pResultsBO )
    {

        // Initialisation
        List resultsDTO = new ArrayList(); // retour de la methode

        Iterator resultsIterator = pResultsBO.iterator();
        Object currentObject = null;
        Object addedObject = null;
        // Pour chaque r�sultat de la liste
        while ( resultsIterator.hasNext() )
        {

            currentObject = resultsIterator.next();
            // On regarde son type et on le transforme
            if ( currentObject instanceof MarkBO )
            {
                addedObject = bo2DtoMark( (MarkBO) currentObject );
            }
            else if ( currentObject instanceof MeasureBO )
            {
                addedObject = bo2DtoMeasure( (MeasureBO) currentObject );
            }
            else if ( currentObject instanceof QualityResultBO )
            {
                addedObject = bo2DtoQualityResult( (QualityResultBO) currentObject );
            }
            else
            {
                addedObject = currentObject;
            }

            resultsDTO.add( addedObject );
        }

        return resultsDTO;

    }

    /**
     * Transforme unQualityResultBO en un objet Float correspondant � l'attribut MeanMark
     * 
     * @param pResultBO QualityResultBO
     * @return flottant sous forme d'objet
     */
    private static Float bo2DtoQualityResult( QualityResultBO pResultBO )
    {

        Float mark = null;
        float f = pResultBO.getMeanMark();
        if ( f != -1.0f )
        {
            mark = new Float( f );
        }
        return mark;

    }

    /**
     * @param pResultsBO liste a raiter
     * @return null
     * @deprecated utiliser le MeasureTransform
     */
   
    private static Float bo2DtoMeasure( MeasureBO pResultsBO )
    {
        
        MeasureBO newResultsBO = pResultsBO;

        // Initialisation
        Float value = null; // Initialisation du retour

        newResultsBO = null; // traitement du parametre

        // Non impl�ment� car externalis� dans MeasureTransform 

        return value;

    }

    /**
     * Transforme un MarkBO en un Float correspondant � l'attribut "value"
     * 
     * @param pResultBO MarkBO associe
     * @return Float
     */
    private static Float bo2DtoMark( MarkBO pResultBO )
    {
        Float value = new Float( pResultBO.getValue() );
        return value;
    }

}
