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
import org.squale.squalecommon.enterpriselayer.businessobject.result.MetricBO;

/**
 * @hibernate.subclass discriminator-value="RSMClassMetrics"
 */
public class RSMClassMetricsBO
    extends RSMMetricsBO
{

    /**
     * Le nombre de lignes de codes
     */
    private final static String SLOC = "sloc";

    /**
     * Les commentaires
     */
    private final static String COMMENTS = "comments";

    /**
     * Le nombre de donn�es publiques
     */
    private final static String PUBLIC_DATA = "public";

    /**
     * Le nombre de donn�es prot�g�es
     */
    private final static String PROTECTED_DATA = "protectedData";

    /**
     * Le nombre de donn�es priv�es
     */
    private final static String PRIVATE_DATA = "private";

    /**
     * Constructeur par d�faut.
     */
    public RSMClassMetricsBO()
    {
        // super();
        getMetrics().put( SLOC, new IntegerMetricBO() );
        getMetrics().put( COMMENTS, new IntegerMetricBO() );
        getMetrics().put( PUBLIC_DATA, new IntegerMetricBO() );
        getMetrics().put( PROTECTED_DATA, new IntegerMetricBO() );
        getMetrics().put( PRIVATE_DATA, new IntegerMetricBO() );
    }

    /**
     * M�thode d'acc�s � la m�trique SLOC
     * 
     * @return la valeur du sloc
     */
    public Integer getSloc()
    {
        return (Integer) ( (MetricBO) getMetrics().get( SLOC ) ).getValue();
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
     * M�thode d'acc�s � la m�trique SLOC
     * 
     * @return la valeur du nombre de donn�es publiques
     */
    public Integer getPublicData()
    {
        return (Integer) ( (MetricBO) getMetrics().get( PUBLIC_DATA ) ).getValue();
    }

    /**
     * Change la valeur de la m�trique donn�es publiques
     * 
     * @param pPublic la nouvelle valeur du nombre de donn�es publiques
     */
    public void setPublicData( Integer pPublic )
    {
        ( (IntegerMetricBO) getMetrics().get( PUBLIC_DATA ) ).setValue( pPublic );
    }

    /**
     * M�thode d'acc�s � la m�trique SLOC
     * 
     * @return la valeur du nombre de donn�es publiques
     */
    public Integer getProtectedData()
    {
        return (Integer) ( (MetricBO) getMetrics().get( PROTECTED_DATA ) ).getValue();
    }

    /**
     * Change la valeur de la m�trique donn�es prot�g�es
     * 
     * @param pProtected la nouvelle valeur du nombre de donn�es prot�g�es
     */
    public void setProtectedData( Integer pProtected )
    {
        ( (IntegerMetricBO) getMetrics().get( PROTECTED_DATA ) ).setValue( pProtected );
    }

    /**
     * M�thode d'acc�s � la m�trique nombre de donn�es priv�es
     * 
     * @return la valeur du nombre de donn�es priv�es
     */
    public Integer getPrivateData()
    {
        return (Integer) ( (MetricBO) getMetrics().get( PRIVATE_DATA ) ).getValue();
    }

    /**
     * Change la valeur de la m�trique nombre de donn�es priv�es
     * 
     * @param pPrivateData la nouvelle valeur du nombre de donn�es priv�es
     */
    public void setPrivateData( Integer pPrivateData )
    {
        ( (IntegerMetricBO) getMetrics().get( PRIVATE_DATA ) ).setValue( pPrivateData );
    }

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

}
