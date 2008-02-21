package com.airfrance.squalix.tools.pmd;

import java.util.ArrayList;
import java.util.Collection;
import java.util.HashMap;
import java.util.Iterator;
import java.util.Map;

import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;

import net.sourceforge.pmd.Report;
import net.sourceforge.pmd.RuleViolation;
import net.sourceforge.pmd.Report.ProcessingError;

import com.airfrance.jraf.commons.exception.JrafDaoException;
import com.airfrance.jraf.spi.persistence.ISession;
import com.airfrance.squalecommon.daolayer.result.MeasureDAOImpl;
import com.airfrance.squalecommon.enterpriselayer.businessobject.component.AuditBO;
import com.airfrance.squalecommon.enterpriselayer.businessobject.component.ProjectBO;
import com.airfrance.squalecommon.enterpriselayer.businessobject.result.IntegerMetricBO;
import com.airfrance.squalecommon.enterpriselayer.businessobject.result.rulechecking.JavaPmdTransgressionBO;
import com.airfrance.squalecommon.enterpriselayer.businessobject.result.rulechecking.JspPmdTransgressionBO;
import com.airfrance.squalecommon.enterpriselayer.businessobject.result.rulechecking.RuleCheckingTransgressionBO;
import com.airfrance.squalecommon.enterpriselayer.businessobject.result.rulechecking.RuleCheckingTransgressionItemBO;
import com.airfrance.squalecommon.enterpriselayer.businessobject.rulechecking.RuleBO;
import com.airfrance.squalecommon.enterpriselayer.businessobject.rulechecking.pmd.PmdRuleSetBO;

/**
 * Persistance des donn�es de Pmd
 * Les donn�es sont stock�es dans une ruleset dynamique, ce ruleset contient une r�gle
 * par langage
 */
public class PmdPersistor {
    /**
     * Logger
     */
    private static final Log LOGGER = LogFactory.getLog(PmdPersistor.class);

    /** Transgression */
    private RuleCheckingTransgressionBO mTransgression;
    /** RuleSet */
    private PmdRuleSetBO mRuleSet;

    /**
     * Constructeur
     * @param pProjectBO projet
     * @param pAuditBO audit
     * @param pRuleSet ruleset
     * @param pLanguage langage
     * @throws PmdFactoryException si langage inconnu
    */
    public PmdPersistor(ProjectBO pProjectBO, AuditBO pAuditBO, PmdRuleSetBO pRuleSet, String pLanguage) throws PmdFactoryException {
        mRuleSet = pRuleSet;
        mTransgression = createTransgression(pProjectBO, pAuditBO, pLanguage);
    }

    /**
     * Cr�ation de la transgression
     * @param pProjectBO projet
     * @param pAuditBO audit
     * @param pLanguage langage
     * @return transgression
     * @throws PmdFactoryException si langage inconnu
     */
    protected RuleCheckingTransgressionBO createTransgression(ProjectBO pProjectBO, AuditBO pAuditBO, String pLanguage) throws PmdFactoryException {
        // Cr�ation de la transgression
        RuleCheckingTransgressionBO transgression;
        if (pLanguage.equals("java")) {
            transgression = new JavaPmdTransgressionBO();
        } else if (pLanguage.equals("jsp")) {
            transgression = new JspPmdTransgressionBO();
        } else {
            throw new PmdFactoryException(PmdMessages.getString("exception.unknown.language", pLanguage));
        }
        transgression.setAudit(pAuditBO);
        transgression.setComponent(pProjectBO);
        transgression.setRuleSet(mRuleSet);
        transgression.setTaskName("PmdTask");
        return transgression;
    }

    /**
     * Sauvegarde des r�sultats
     * @param pSession session
     * @param pReport rapport
     * @throws JrafDaoException si erreur
     */
    public void storeResults(ISession pSession, Report pReport) throws JrafDaoException {
        HashMap results = new HashMap();
        // On r�cup�re les erreurs de parsing et on les consid�re comme des transgressions
        // de la r�gle concernant le XHTML car toutes les JSPs doivent �tre conforme au XHTML
        // pour pouvoir �tre pars�e.
        Iterator itErrors = pReport.errors();
        while(itErrors.hasNext()) {
            ProcessingError error = (ProcessingError) itErrors.next();
            processTransgression(results, error);
        }
        // Parcours des violations
        Iterator itViolations = pReport.iterator();
        while (itViolations.hasNext()) {
            RuleViolation violation = (RuleViolation) itViolations.next();
            processTransgression(results, violation);
        }
        // Ecriture des violations dans la mesure
        Iterator itRules = mRuleSet.getRules().values().iterator();
        while (itRules.hasNext()) {
            RuleBO rule = (RuleBO) itRules.next();
            Collection details = (Collection) results.get(rule.getCode());
            // On r�cup�re le nombre de transgression qui existe d�j�
            int nbOcc = findNbOccWhereName(mTransgression.getMetrics(), rule.getCode());
            // Si le parsing n'a pas donn� de r�sultat, on place 0 comme
            // nombre de transgression
            if (details != null) {
                nbOcc += details.size();
                // On parcourt le d�tail des transgressions
                int cpt = RuleCheckingTransgressionBO.MAX_DETAILS;
                for (Iterator detailIt = details.iterator(); detailIt.hasNext() && cpt > 0; cpt--) {
                    RuleCheckingTransgressionItemBO item = (RuleCheckingTransgressionItemBO) detailIt.next();
                    item.setRule(rule);
                    mTransgression.getDetails().add(item);
                }
            }
            // On ajoute une m�trique de type Integer pour chaque r�gle transgress�e
            // avec 0 comme valeur par d�faut
            IntegerMetricBO metric = new IntegerMetricBO();
            metric.setName(rule.getCode());
            metric.setValue(nbOcc);
            metric.setMeasure(mTransgression);
            mTransgression.putMetric(metric);
        }
        // On parcourt les r�gles non trouv�es dans le ruleset
        Iterator ruleCodes = results.keySet().iterator();
        while (ruleCodes.hasNext()) {
            String ruleCode = (String) ruleCodes.next();
            // Chaque r�gle non trouv�e est signal�e comme
            // manquante
            if (false == mRuleSet.getRules().containsKey(ruleCode)) {
                LOGGER.warn(PmdMessages.getString("rule.ignored", ruleCode));
            }
        }
        // Sauvegarde des donn�es dans la base
        MeasureDAOImpl.getInstance().create(pSession, mTransgression);
    }

    /**
     * @param pDetails d�tails des transgression MAP<rulename,Collection<RuleCheckingTransgressionItemBO>>
     * @param pError l'erreur de parsing caus� par le XHTML
     */
    private void processTransgression(Map pDetails, ProcessingError pError) {
        Collection col = (Collection) pDetails.get(PmdRuleSetBO.XHTML_RULE_NAME);
        if (col == null) {
            col = new ArrayList();
            pDetails.put(PmdRuleSetBO.XHTML_RULE_NAME, col);
        }
        // On cr�e une transgression que l'on ajoute
        RuleCheckingTransgressionItemBO item = new RuleCheckingTransgressionItemBO();
        // Construction du message en fonction du nom de fichier et du message
        item.setComponentFile(pError.getFile());
        StringBuffer detail = new StringBuffer(RuleCheckingTransgressionItemBO.PATH_KEY);
        detail.append(" is not XHTML compliant");
        // Troncature du message si besoin
        item.setMessage(truncMessage(detail));
        col.add(item);
    }

    /**
     * 
     * @param pDetails d�tails des transgression MAP<rulename,Collection<RuleCheckingTransgressionItemBO>>
     * @param pViolation violation � traiter
     */
    protected void processTransgression(Map pDetails, RuleViolation pViolation) {
        Collection col = (Collection) pDetails.get(pViolation.getRule().getName());
        if (col == null) {
            col = new ArrayList();
            pDetails.put(pViolation.getRule().getName(), col);
        }
        // On cr�e une transgression que l'on ajoute
        RuleCheckingTransgressionItemBO item = new RuleCheckingTransgressionItemBO();
        // Construction du message en fonction du nom de fichier, num�ro de ligne et du message
        item.setComponentFile(pViolation.getFilename());
        StringBuffer detail = new StringBuffer(RuleCheckingTransgressionItemBO.PATH_KEY);
        item.setLine(pViolation.getBeginLine());
        detail.append(" line " + RuleCheckingTransgressionItemBO.LINE_KEY);
        if (pViolation.getBeginLine() != pViolation.getEndLine()) {
            detail.append(" to " + pViolation.getEndLine());
        }
        detail.append(": " + pViolation.getDescription());
        // Troncature du message si besoin
        item.setMessage(truncMessage(detail));
        col.add(item);
    }
    
    /**
     * @param pMessage le message � tronquer
     * @return le message tronqu� si il d�passe la limite
     */
    private String truncMessage(StringBuffer pMessage) {
        StringBuffer result = pMessage;
        // Taille maximale pour les d�tails
        final int MAX_LENGTH = 3000;
        // Troncature du message si besoin
        if (pMessage.length() > MAX_LENGTH) {
            pMessage.substring(MAX_LENGTH - 1);
        }
        return result.toString();
    }

    /**
     * @param pMetrics les m�triques
     * @param pCode le code de la r�gle � chercher
     * @return le nombre d'occurence de la r�gle
     */
    private int findNbOccWhereName(Map pMetrics, String pCode) {
        int nbOcc = 0;
        IntegerMetricBO metric = (IntegerMetricBO) pMetrics.get(pCode);
        if (metric != null) {
            nbOcc = ((Integer) metric.getValue()).intValue();
        }
        return nbOcc;
    }
}
