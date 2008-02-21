/*
 * Cr�� le 24 janv. 07
 *
 * Personnalisation des liens URL du Pie Chart
 */
package com.airfrance.squaleweb.util.graph;

import java.util.HashMap;
import java.util.Map;

import org.jfree.chart.urls.PieURLGenerator;
import org.jfree.data.general.PieDataset;

/**
 * @author 6370258
 * Classe de g�n�ration de liens sp�cifiques � chaque portion du Pie Chart
 */
public class PieChartUrlGenerator extends AbstractURLGenerator implements PieURLGenerator {
    /**
     * Map des projets libell� et Id
     */
    private Map urlMap;
    /**
     * Constructeur par d�faut
     */     
    public PieChartUrlGenerator() {
    }
        
    /**
     * Constructeur
     * @param pUrlMap Map des projets
     * @param pCurrentAuditId l'id de l'audit courant
     * @param pPreviousAuditId l'id de l'audit pr�c�dent
     */
    public PieChartUrlGenerator(Map pUrlMap,String pCurrentAuditId,String pPreviousAuditId) {
        urlMap = new HashMap();
        urlMap.putAll(pUrlMap);
        mCurrentAuditId = pCurrentAuditId;
        mPreviousAuditId = pPreviousAuditId;
    }
     
    /**
     * G�n�ration du lien URL
     * @param pDataset le DataSet du graphe
     * @param pKey le libell� de la portion du graphe
     * @param pIndex la s�rie (inutilis�e)
     * @return url l'url de la portion du Pie Chart
     */     
    public String generateURL(PieDataset pDataset, Comparable pKey, int pIndex) {
        String key = (String) pKey;
        String url = "project.do?action=select&projectId=" + (Long) urlMap.get(key) 
        +"&currentAuditId=" + mCurrentAuditId;
        if(mPreviousAuditId != null){
            url += "&previousAuditId=" + mPreviousAuditId;
        }
        return url;
    }

}
