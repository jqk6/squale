/*
 * Cr�� le 24 janv. 07
 *
 * Personnalisation des liens URL du graphe de type Bubble
 */
package com.airfrance.squaleweb.util.graph;

import org.jfree.chart.urls.XYURLGenerator;
import org.jfree.data.xy.XYDataset;

/**
 * @author 6370258 Classe de g�n�ration de liens sp�cifiques � chaque point du graphe de type Bubble
 */
public class BubbleUrlGenerator
    extends AbstractURLGenerator
    implements XYURLGenerator
{

    /**
     * Composants du projet (i.e les m�thodes)
     */
    private long[] tbComponentId;

    /**
     * Constructeur par d�faut
     */
    public BubbleUrlGenerator()
    {
    }

    /**
     * Constructeur
     * 
     * @param pProjectId Id du projet
     * @param pAuditId Id de l'audit courant
     * @param pPreviousAuditId l'id de l'audit pr�c�dent
     * @param pComponentId composants du projet � repr�senter sur le graphe
     */
    public BubbleUrlGenerator( String pProjectId, String pAuditId, String pPreviousAuditId, long[] pComponentId )
    {
        // Initialisation des attributs
        mProjectId = pProjectId;
        mCurrentAuditId = pAuditId;
        mPreviousAuditId = pPreviousAuditId;
        tbComponentId = new long[pComponentId.length];
        System.arraycopy( pComponentId, 0, tbComponentId, 0, pComponentId.length );

    }

    /**
     * G�n�ration du lien URL
     * 
     * @param pDataset le DataSet du graphe
     * @param pSeries la s�rie du graphe
     * @param pItem le point du graphe
     * @return url l'url de la portion du Scatterplott
     */
    public String generateURL( XYDataset pDataset, int pSeries, int pItem )
    {
        String url =
            "project_component.do?action=component&projectId=" + mProjectId + "&currentAuditId=" + mCurrentAuditId
                + "&component=" + tbComponentId[pItem];
        // On ne rajout l'audit pr�c�dent � l'url que si il est valide
        if ( mPreviousAuditId != null && !"-1".equals( mPreviousAuditId ) )
        {
            url += "&previousAuditId=" + mPreviousAuditId;
        }
        return url;
    }

}
