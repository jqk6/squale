package com.airfrance.squalecommon.datatransfertobject.export;

import java.util.Collection;

import com.airfrance.squalecommon.datatransfertobject.result.QualityResultDTO;
import com.airfrance.squalecommon.datatransfertobject.result.ResultsDTO;

/**
 * Repr�sente une pratique � corriger pour le plan d'action
 */
public class ActionPlanDTO {

    /** le r�sultat qualit� */
    private QualityResultDTO mQualityResultDTO;

    /** les r�sultats des composants */
    private ResultsDTO mResultsDTO;

    /** les transgressions */
    private Collection mTransgressions;
    
    /** Le nombre total de correction a effectuer */
    private int mNbCorrections;

    /**
     * Constructeur par d�faut
     */
    public ActionPlanDTO() {
        this(null, null, null);
        mNbCorrections = 0;
    }

    /**
     * Constructeur pour un plan d'action synth�tique
     * @param pQualityResult le r�sultat qualit�
     * @param pNbCorrections le total des corrections
     */
    public ActionPlanDTO(QualityResultDTO pQualityResult, int pNbCorrections) {
        this(pQualityResult, null, null);
        mNbCorrections = pNbCorrections;
    }

    /**
     * Constructeur pour un plan d'action d�taill�
     * @param pQualityResult le r�sultat qualit�
     * @param pResultsDTO les r�sultats des composants (peut �tre nul)
     * @param pTransgressions les transgressions (peut �tre nul)
     */
    public ActionPlanDTO(QualityResultDTO pQualityResult, ResultsDTO pResultsDTO, Collection pTransgressions) {
        mQualityResultDTO = pQualityResult;
        mResultsDTO = pResultsDTO;
        mTransgressions = pTransgressions;
    }

    /**
     * @return le r�sultat qualit�
     */
    public QualityResultDTO getQualityResultDTO() {
        return mQualityResultDTO;
    }

    /**
     * @return les r�sultats des composants
     */
    public ResultsDTO getResultsDTO() {
        return mResultsDTO;
    }

    /**
     * @return les transgressions
     */
    public Collection getTransgressions() {
        return mTransgressions;
    }

    /**
     * @param pResultDTO le r�sultat qualit�
     */
    public void setQualityResultDTO(QualityResultDTO pResultDTO) {
        mQualityResultDTO = pResultDTO;
    }

    /**
     * @param pResultsDTO les r�sultats des composants
     */
    public void setResultsDTO(ResultsDTO pResultsDTO) {
        mResultsDTO = pResultsDTO;
    }

    /**
     * @param pCollection les transgressions
     */
    public void setTransgressions(Collection pCollection) {
        mTransgressions = pCollection;
    }

    /**
     * @return le nombre total de correction a effectuer
     */
    public int getNbCorrections() {
        return mNbCorrections;
    }

    /**
     * @param pTotal le nombre total de correction a effectuer
     */
    public void setNbCorrections(int pTotal) {
        mNbCorrections = pTotal;
    }

}
