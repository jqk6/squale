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
package org.squale.squalecommon.enterpriselayer.businessobject.result.rsm;

import org.squale.squalecommon.enterpriselayer.businessobject.result.IntegerMetricBO;
import org.squale.squalecommon.enterpriselayer.businessobject.result.StringMetricBO;

/**
 * @hibernate.subclass discriminator-value="RSMMethodMetrics"
 */
public class RSMMethodMetricsBO
    extends RSMMetricsBO
{

    /**
     * les commentaires
     */
    private final static String COMMENTS = "comments";

    /**
     * Le nombre de lignes de codes
     */
    private final static String SLOC = "sloc";

    /**
     * Le nom du fichier associ�
     */
    private final static String FILENAME = "fileName";

    /**
     * M�thode d'acc�s � la m�trique commentaires
     * 
     * @return la valeur sur le nombre de lignes de commentaires
     */
    public Integer getComments()
    {
        return (Integer) ( (IntegerMetricBO) getMetrics().get( COMMENTS ) ).getValue();
    }

    /**
     * Change la valeur de la m�trique commentaires
     * 
     * @param pComments la nouvelle valeur du nombre de commentaires
     */
    public void setComments( Integer pComments )
    {
        ( (IntegerMetricBO) getMetrics().get( COMMENTS ) ).setValue( pComments );
    }

    /**
     * Change la valeur de la m�trique nom de fichier
     * 
     * @param pFileName la nouvelle valeur du nom du fichier
     */
    public void setFileName( String pFileName )
    {
        ( (StringMetricBO) getMetrics().get( FILENAME ) ).setValue( pFileName );
    }

    /**
     * M�thode d'acc�s � la m�trique nom du fichier
     * 
     * @return le nom du fichier
     */
    public String getFileName()
    {
        return (String) ( (StringMetricBO) getMetrics().get( FILENAME ) ).getValue();
    }

    /**
     * M�thode d'acc�s � la m�trique SLOC
     * 
     * @return la valeur du sloc
     */
    public Integer getSloc()
    {
        return (Integer) ( (IntegerMetricBO) getMetrics().get( SLOC ) ).getValue();
    }

    /**
     * Change la valeur de la m�trique SLOC
     * 
     * @param pSLOC la nouvelle valeur du SLOC
     */
    public void setSloc( Integer pSLOC )
    {
        ( (IntegerMetricBO) getMetrics().get( SLOC ) ).setValue( pSLOC );
    }

    /**
     * Constructeur par d�faut.
     */
    public RSMMethodMetricsBO()
    {
        super();
        getMetrics().put( SLOC, new IntegerMetricBO() );
        getMetrics().put( COMMENTS, new IntegerMetricBO() );
        getMetrics().put( FILENAME, new StringMetricBO() );
    }

}
