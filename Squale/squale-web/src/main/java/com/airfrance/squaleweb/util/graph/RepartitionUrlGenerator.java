package com.airfrance.squaleweb.util.graph;

import org.jfree.chart.urls.CategoryURLGenerator;
import org.jfree.data.category.CategoryDataset;

/**
 */
public class RepartitionUrlGenerator extends AbstractRepartitionUrlGenerator implements CategoryURLGenerator {

    /**
     * Constructeur, initialise les champs communs � tout le graphe 
     * @param pProjectId l'id du projet 
     * @param pCurrentAuditId l'id de l'audit courant
     * @param pPreviousAuditId l'id de l'audit pr�c�dent
     * @param pPracticeId l'id de la pratique
     * @param pFactorParentId l'id du facteur parent
     * @param pNbSeries le nombre de s�ries que contient le graph
     */
    public RepartitionUrlGenerator(String pProjectId, String pCurrentAuditId,String pPreviousAuditId, String pPracticeId, String pFactorParentId, int pNbSeries) {
        mProjectId = pProjectId;
        mCurrentAuditId = pCurrentAuditId;
        mPreviousAuditId = pPreviousAuditId;
        practiceId = pPracticeId;
        factorParent = pFactorParentId;
        nbSeries = pNbSeries;
    }

    /** 
     * Impl�mente la g�n�ration d'url
     * @param pDataSet les donne�s
     * @param pSerieIndex l'index de la s�rie sur lequel on va d�finir l'url
     * @param pCategory la cat�gorie
     * @see org.jfree.chart.urls.CategoryURLGenerator#generateURL(org.jfree.data.category.CategoryDataset, int, int)
     * @return l'url
     */
    public String generateURL(CategoryDataset pDataSet, int pSerieIndex, int pCategory) {
        return super.generateURL(pDataSet, pSerieIndex, pCategory);
    }
}
