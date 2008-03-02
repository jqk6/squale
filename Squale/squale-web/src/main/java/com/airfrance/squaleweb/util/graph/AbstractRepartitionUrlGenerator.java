package com.airfrance.squaleweb.util.graph;

import com.airfrance.squalecommon.enterpriselayer.businessobject.result.PracticeResultBO;

/**
 */
public class AbstractRepartitionUrlGenerator
    extends AbstractURLGenerator
{

    /**
     * l'id de la pratique
     */
    protected String practiceId;

    /**
     * l'id du crit�re parent
     */
    protected String factorParent;

    /**
     * Le nombre de batonnets, varie en fonction de la formule associ�e � la pratique
     */
    protected int nbSeries;

    /**
     * Impl�mente la g�n�ration d'url
     * 
     * @param pDataSet les donne�s
     * @param pSerieIndex l'index de la s�rie sur lequel on va d�finir l'url
     * @param pCategory la cat�gorie
     * @see org.jfree.chart.urls.CategoryURLGenerator#generateURL(org.jfree.data.category.CategoryDataset, int, int)
     * @return l'url
     */
    public String generateURL( Object pDataSet, int pSerieIndex, int pCategory )
    {
        // D�finit le min et le max de l'intervalle de note
        String url = "";
        final double coeff = 10;
        double minMark =
            ( (int) ( coeff * ( (double) pSerieIndex ) / ( (double) nbSeries / (double) PracticeResultBO.EXCELLENT ) ) )
                / coeff;
        double maxMark =
            Math.round( (float) coeff * ( minMark + ( (double) PracticeResultBO.EXCELLENT / nbSeries ) ) ) / coeff;
        url =
            "mark.do?action=mark&projectId=" + mProjectId + "&currentAuditId=" + mCurrentAuditId + "&previousAuditId="
                + mPreviousAuditId + "&tre=" + practiceId + "&minMark=" + minMark + "&maxMark=" + maxMark;
        if ( factorParent != null && !factorParent.equals( "" ) )
        {
            url += "&factorParent=" + factorParent;
        }
        return url;
    }

}
