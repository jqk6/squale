package com.airfrance.squalecommon.enterpriselayer.businessobject.stats;

import com.airfrance.squalecommon.enterpriselayer.businessobject.config.ServeurBO;

import java.io.Serializable;
import java.util.Calendar;
import java.util.Date;

/**
 * La table regroupant les stats pour les indicateurs qui ne concernent pas le profil
 * 
 * @hibernate.class table="Stats_squale_dict" mutable="true"
 */
public class SiteStatsDICTBO
    implements Serializable
{

    /**
     * @param pServeurId l'id du site
     * @param pNbAppli le nombre d'appli pour ce site
     * @param pNbAppliExec le nombre d'appli avec un audit ex�cut� pour ce site
     * @param pNbAppliSuccess le nombre d'appli avec un audit r�ussi pour ce site
     * @param pNbAppliWithoutAudits le nombre d'applis sans audits
     * @param pNbAppliToValidate le nombre d'applis � valider
     * @param pFactorsAccepted le nombre de facteurs accept�s
     * @param pFactorsReserved le nombre de facteurs accept� avec r�serves
     * @param pFactorsRefused le nombre de facteurs refus�s
     * @param pAuditsFailed le nombre d'audits en �chec
     * @param pAuditsSuccessful le nombre d'audits r�ussis
     * @param pAuditsPartial le nombre d'audits partiels
     * @param pAuditsNotAttempted le nombre d'audit programm�s
     * @param pRoi le roi
     */
    public SiteStatsDICTBO( long pServeurId, int pNbAppli, int pNbAppliExec, int pNbAppliSuccess,
                            int pNbAppliWithoutAudits, int pNbAppliToValidate, int pFactorsAccepted,
                            int pFactorsReserved, int pFactorsRefused, int pAuditsFailed, int pAuditsSuccessful,
                            int pAuditsPartial, int pAuditsNotAttempted, double pRoi )
    {
        // initialisation l'id � la valeur unsaved_value
        // pour permettre � hibernate de prendre la valeur de la s�quence
        id = -1;
        today = Calendar.getInstance().getTime();
        mServeurBO = new ServeurBO();
        mServeurBO.setServeurId( pServeurId );
        nbTotalAppli = pNbAppli;
        nbTotalAppliWithAudit = pNbAppliExec;
        nbTotalAppliWithSuccesfulAudit = pNbAppliSuccess;
        nbTotalAppliWithoutAudit = pNbAppliWithoutAudits;
        nbAppliToValidate = pNbAppliToValidate;
        nbOfAcceptedFactors = pFactorsAccepted;
        nbOfAcceptedWithReservesFactors = pFactorsReserved;
        nbOfRefusedFactors = pFactorsRefused;
        nbFailedAudits = pAuditsFailed;
        nbSuccessfulAudits = pAuditsSuccessful;
        nbPartialAudits = pAuditsPartial;
        nbNotAttemptedAudit = pAuditsNotAttempted;
        roi = pRoi;

    }

    /** Constructeur vide */
    public SiteStatsDICTBO()
    {
        // initialisation l'id � la valeur unsaved_value
        // pour permettre � hibernate de prendre la valeur de la s�quence
        id = -1;
    }

    /** La date de calcul effective des stats */
    private Date today;

    /** le nombre total d'application */
    private int nbTotalAppli;

    /** le nombre total d'application avec que des audits en �chec */
    private int nbTotalAppliWithAudit;

    /** le nombre total d'application avec au moins audit r�ussi */
    private int nbTotalAppliWithSuccesfulAudit;

    /** le nombre total d'application sans audit (ni r�ussi ni en �chec) */
    private int nbTotalAppliWithoutAudit;

    /** le nombre total d'application � valider */
    private int nbAppliToValidate;

    /** le nombre de facteurs accept�s */
    private int nbOfAcceptedFactors;

    /** le nombre de facteurs accept�s avec r�serves */
    private int nbOfAcceptedWithReservesFactors;

    /** le nombre de facteurs refus�s */
    private int nbOfRefusedFactors;

    /** le nombre d'audits ayant �chou� */
    private int nbFailedAudits;

    /** le nombre d'audits ayant r�ussi */
    private int nbSuccessfulAudits;

    /** le nombre d'audits ayant r�ussi partiellement */
    private int nbPartialAudits;

    /** le nombre d'audits programm� */
    private int nbNotAttemptedAudit;

    /** le roi pour ce site */
    private double roi;

    /** le nom du site */
    private ServeurBO mServeurBO;

    /** l'identifiant en base */
    private long id;

    /**
     * @return l'id de l'objet
     * @hibernate.id generator-class="native" type="long" column="Id" unsaved-value="-1" length="19"
     * @hibernate.generator-param name="sequence" value="stats_sequence"
     */
    public long getId()
    {
        return id;
    }

    /**
     * @return le nombre de facteurs accept�s
     * @hibernate.property name="nbOfAcceptedFactors" column="NB_FACTEURS_ACCEPTES" type="integer" not-null="true"
     *                     cascade="all"
     */
    public int getNbOfAcceptedFactors()
    {
        return nbOfAcceptedFactors;
    }

    /**
     * @return le nombre de facteurs accept�s avec r�serves
     * @hibernate.property name="nbOfAcceptedWithReservesFactors" column="NB_FACTEURS_ACCEPTES_RESERVES" type="integer"
     *                     not-null="true" cascade="all"
     */
    public int getNbOfAcceptedWithReservesFactors()
    {
        return nbOfAcceptedWithReservesFactors;
    }

    /**
     * @return le nombre de facteurs refus�s
     * @hibernate.property name="nbOfRefusedFactors" column="NB_FACTEURS_REFUSES" type="integer" not-null="true"
     *                     cascade="all"
     */
    public int getNbOfRefusedFactors()
    {
        return nbOfRefusedFactors;
    }

    /**
     * @return le nombre d'applications
     * @hibernate.property name="nbTotalAppli" column="NB_TOTAL_APPLI" type="integer" not-null="true" cascade="all"
     */
    public int getNbTotalAppli()
    {
        return nbTotalAppli;
    }

    /**
     * @return le nombre d'applications
     * @hibernate.property name="nbTotalAppliWithAudit" column="NB_APPLI_AVEC_AUDIT_EXECUTE" type="integer"
     *                     not-null="true" cascade="all"
     */
    public int getNbTotalAppliWithAudit()
    {
        return nbTotalAppliWithAudit;
    }

    /**
     * @return le nombre d'applications
     * @hibernate.property name="nbTotalAppliWithSuccesfulAudit" column="NB_APPLI_AVEC_AUDIT_REUSSI" type="integer"
     *                     not-null="true" cascade="all"
     */
    public int getNbTotalAppliWithSuccesfulAudit()
    {
        return nbTotalAppliWithSuccesfulAudit;
    }

    /**
     * @return la date du jour
     * @hibernate.property name="today" column="Date_calcul" type="timestamp" not-null="true" cascade="all"
     */
    public Date getToday()
    {
        return today;
    }

    /**
     * @return le retour d'investissement pour ce site
     * @hibernate.property name="roi" column="ROI_EN_MHI" type="double" not-null="true" cascade="all"
     */
    public double getRoi()
    {
        return roi;
    }

    /**
     * @return le nombre d'appli � valider
     * @hibernate.property name="applisToValidate" column="NB_APPLI_A_VALIDER" type="integer" not-null="true"
     *                     cascade="all"
     */
    public int getNbAppliToValidate()
    {
        return nbAppliToValidate;
    }

    /**
     * @return le nombre d'audits ayant �chou�s
     * @hibernate.property name="failedAudits" column="NB_AUDITS_ECHECS" type="integer" not-null="true" cascade="all"
     */
    public int getNbFailedAudits()
    {
        return nbFailedAudits;
    }

    /**
     * @return le nombre d'audits programm�s
     * @hibernate.property name="notAttemptedAudits" column="NB_AUDITS_PROGRAMME" type="integer" not-null="true"
     *                     cascade="all"
     */
    public int getNbNotAttemptedAudit()
    {
        return nbNotAttemptedAudit;
    }

    /**
     * @return le nombre d'audits partiels
     * @hibernate.property name="partialAudits" column="NB_AUDITS_PARTIELS" type="integer" not-null="true" cascade="all"
     */
    public int getNbPartialAudits()
    {
        return nbPartialAudits;
    }

    /**
     * @return le nombre d'audits r�ussis
     * @hibernate.property name="successfulAudits" column="NB_AUDITS_REUSSIS" type="integer" not-null="true"
     *                     cascade="all"
     */
    public int getNbSuccessfulAudits()
    {
        return nbSuccessfulAudits;
    }

    /**
     * @return le nombre d'applications sans audits
     * @hibernate.property name="applisWithoutAudits" column="NB_APPLI_AVEC_AUCUN_AUDIT" type="integer" not-null="true"
     *                     cascade="all"
     */
    public int getNbTotalAppliWithoutAudit()
    {
        return nbTotalAppliWithoutAudit;
    }

    /**
     * @param newValue le nouveau nombre de facteurs accept�s
     */
    public void setNbOfAcceptedFactors( int newValue )
    {
        nbOfAcceptedFactors = newValue;
    }

    /**
     * @param newValue le nouveau nombre de facteurs accept�s avec r�serves
     */
    public void setNbOfAcceptedWithReservesFactors( int newValue )
    {
        nbOfAcceptedWithReservesFactors = newValue;
    }

    /**
     * @param newValue le nouveau nombre de facteurs refus�s
     */
    public void setNbOfRefusedFactors( int newValue )
    {
        nbOfRefusedFactors = newValue;
    }

    /**
     * @param newValue le nouveau nombre d'appli
     */
    public void setNbTotalAppli( int newValue )
    {
        nbTotalAppli = newValue;
    }

    /**
     * @param newValue le nouveau nombre d'appli avec un audit ex�cut�
     */
    public void setNbTotalAppliWithAudit( int newValue )
    {
        nbTotalAppliWithAudit = newValue;
    }

    /**
     * @param newValue le nouveau nombre d'appli avec un audit r�ussi
     */
    public void setNbTotalAppliWithSuccesfulAudit( int newValue )
    {
        nbTotalAppliWithSuccesfulAudit = newValue;
    }

    /**
     * @param date la nouvelle date du jour
     */
    public void setToday( Date date )
    {
        today = date;
    }

    /**
     * @param newRoi le nouveau roi
     */
    public void setRoi( int newRoi )
    {
        roi = newRoi;
    }

    /**
     * @param newId le nouvel id
     */
    public void setId( long newId )
    {
        id = newId;
    }

    /**
     * @param newRoi la nouvelle valeur du roi
     */
    public void setRoi( double newRoi )
    {
        roi = newRoi;
    }

    /**
     * @param pNbApplisToValidate le nombre d'applications � valider
     */
    public void setNbAppliToValidate( int pNbApplisToValidate )
    {
        nbAppliToValidate = pNbApplisToValidate;
    }

    /**
     * @param pFailedAudits le nombre d'audits ayant �chou�s
     */
    public void setNbFailedAudits( int pFailedAudits )
    {
        nbFailedAudits = pFailedAudits;
    }

    /**
     * @param pNumberNotAttemptedAudits le nombre d'audits programm�s
     */
    public void setNbNotAttemptedAudit( int pNumberNotAttemptedAudits )
    {
        nbNotAttemptedAudit = pNumberNotAttemptedAudits;
    }

    /**
     * @param pPartialAudits le nombre d'audits partiels
     */
    public void setNbPartialAudits( int pPartialAudits )
    {
        nbPartialAudits = pPartialAudits;
    }

    /**
     * @param pAuditsSuccess le nombre d'audits r�ussis
     */
    public void setNbSuccessfulAudits( int pAuditsSuccess )
    {
        nbSuccessfulAudits = pAuditsSuccess;
    }

    /**
     * @param pApplisWithoutAudit le nombre d'applis sans audits
     */
    public void setNbTotalAppliWithoutAudit( int pApplisWithoutAudit )
    {
        nbTotalAppliWithoutAudit = pApplisWithoutAudit;
    }

    /**
     * @hibernate.many-to-one name="serveurBO"
     *                        type="com.airfrance.squalecommon.enterpriselayer.businessobject.config.ServeurBO"
     *                        column="Serveur" not-null="false" lazy="true" update="true" insert="true"
     * @return le serveur
     */
    public ServeurBO getServeurBO()
    {
        return mServeurBO;
    }

    /**
     * @param pServeurBO le serveur
     */
    public void setServeurBO( ServeurBO pServeurBO )
    {
        mServeurBO = pServeurBO;
    }

}
