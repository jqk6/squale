package com.airfrance.squalecommon.datatransfertobject.result;

import com.airfrance.squalecommon.datatransfertobject.component.ComponentDTO;

/**
 * Repr�sente l'�volution des pratiques entre deux audits
 */
public class PracticeEvolutionDTO {
    
    /* Les constantes pour le filtre */
    
    /** L'index pour le tableau des filtres */
    public final static int ONLY_UP_OR_DOWN_ID = 0;
    /** L'index pour le tableau des filtres */
    public final static int THRESHOLD_ID = 1;
    /** L'index pour le tableau des filtres */
    public final static int ONLY_PRACTICES_ID = 2;
    
    /** Indique que le filtre se porte sur les composants qui se sont am�lior�s */
    public final static String ONLY_DOWN = "onlyDown";
    /** Indique que le filtre se porte sur les composants qui se sont d�grad�s */
    public final static String ONLY_UP = "onlyUp";
    /** Indique que le filtre se porte sur les composants qui ont �t� supprim�s */
    public final static String DELETED = "deleted";

    /** Le composant concern� */
    private ComponentDTO mComponent;
    
    /** La pratique concern�e */
    private QualityResultDTO mPractice;
    
    /** La note de l'audit de r�f�rence */
    private Float mMark;
    
    /** La note de l'audit de comparaison */
    private Float mPreviousMark;
    
    
    /**
     * @return le composant
     */
    public ComponentDTO getComponent() {
        return mComponent;
    }

    /**
     * @return la note courante
     */
    public Float getMark() {
        return mMark;
    }

    /**
     * @return la note pr�c�dente
     */
    public Float getPreviousMark() {
        return mPreviousMark;
    }

    /**
     * @return la pratique
     */
    public QualityResultDTO getPractice() {
        return mPractice;
    }

    /**
     * @param pComponent le composant
     */
    public void setComponent(ComponentDTO pComponent) {
        mComponent = pComponent;
    }

    /**
     * @param pMark la note courante
     */
    public void setMark(Float pMark) {
        mMark = pMark;
    }

    /**
     * @param pPreviousMark la note pr�c�dente
     */
    public void setPreviousMark(Float pPreviousMark) {
        mPreviousMark = pPreviousMark;
    }

    /**
     * @param pPractice la pratique concern�e
     */
    public void setPractice(QualityResultDTO pPractice) {
        mPractice = pPractice;
    }

}
