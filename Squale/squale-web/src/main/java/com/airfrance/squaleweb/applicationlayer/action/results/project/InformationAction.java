package com.airfrance.squaleweb.applicationlayer.action.results.project;

import java.util.ArrayList;
import java.util.Collection;
import java.util.Iterator;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.apache.struts.action.ActionErrors;
import org.apache.struts.action.ActionForm;
import org.apache.struts.action.ActionForward;
import org.apache.struts.action.ActionMapping;

import com.airfrance.jraf.helper.AccessDelegateHelper;
import com.airfrance.jraf.spi.accessdelegate.IApplicationComponent;
import com.airfrance.squalecommon.datatransfertobject.rule.AbstractFormulaDTO;
import com.airfrance.squalecommon.datatransfertobject.rule.ConditionFormulaDTO;
import com.airfrance.squalecommon.datatransfertobject.rule.PracticeRuleDTO;
import com.airfrance.squalecommon.datatransfertobject.rule.QualityRuleDTO;
import com.airfrance.squalecommon.datatransfertobject.rule.SimpleFormulaDTO;
import com.airfrance.squaleweb.applicationlayer.action.accessRights.DefaultAction;
import com.airfrance.squaleweb.applicationlayer.formbean.information.PracticeInformationForm;
import com.airfrance.squaleweb.resources.WebMessages;

/**
 */
public class InformationAction extends DefaultAction {

    /**  
     * r�cup�re les informations li�es � un facteur/crit�re/pratique
     * @param pMapping le mapping.
     * @param pForm le formulaire � lire.
     * @param pRequest la requ�te HTTP.
     * @param pResponse la r�ponse de la servlet.
     * @return l'action � r�aliser.
     */
    public ActionForward retrieveInformation(ActionMapping pMapping, ActionForm pForm, HttpServletRequest pRequest, HttpServletResponse pResponse) {
        ActionForward forward = null;
        ActionErrors errors = new ActionErrors();
        try {
            forward = pMapping.findForward("information");
            // la cl� de l'�l�ment sur lequel on veut des d�tails
            String elementKey = pRequest.getParameter("elementName");
            // l'id de l'�l�ment (pour r�cup�rer la formule)
            String ruleId = pRequest.getParameter("ruleId");
            // r�cup�re les informations li�es a l'�l�ment
            IApplicationComponent ac = AccessDelegateHelper.getInstance("QualityGrid");
            QualityRuleDTO rule = new QualityRuleDTO();
            rule.setId(new Long(ruleId).longValue());
            Object[] param = new Object[] { rule, new Boolean(true)};
            Object[] ruleAndTre = (Object[]) ac.execute("getQualityRuleAndUsedTres", param);
            QualityRuleDTO ruleResult = (QualityRuleDTO) ruleAndTre[0];
            Object[] triggerAndFormula = formatFormula(pRequest, ruleResult);
            String trigger = (String) triggerAndFormula[0];
            String key = ruleResult.getHelpKey();
            if (key != null && !key.equals("")) {
                elementKey = ruleResult.getHelpKey();
            }
            String description = WebMessages.getString(pRequest, elementKey + ".description");
            String correction = WebMessages.getString(pRequest, elementKey + ".correction");
            ((PracticeInformationForm) pForm).setTrigger(trigger);
            // Suivant le type de la formule, on a r�cup�r� une formule simple
            // ou une formule avec des conditions
            Object formula = triggerAndFormula[1];
            if (formula instanceof String) {
                ((PracticeInformationForm) pForm).setFormula((String) formula);
            }
            if (formula instanceof String[]) {
                ((PracticeInformationForm) pForm).setFormulaCondition((String[]) formula);
            }
            ((PracticeInformationForm) pForm).setDescription(description);
            ((PracticeInformationForm) pForm).setCorrection(correction);
            ((PracticeInformationForm) pForm).setName(WebMessages.getString(pRequest, elementKey));
            ((PracticeInformationForm) pForm).setUsedTres((Collection)ruleAndTre[1]);
        } catch (Exception e) {
            // Traitement factoris� des exceptions
            handleException(e, errors, pRequest);
        }
        if (!errors.isEmpty()) {
            // Sauvegarde des messages
            saveMessages(pRequest, errors);
            // Routage vers la page d'erreur
            forward = pMapping.findForward("total_failure");
        }
        return forward;
    }

    /**
     * @param ruleDto la r�gle
     * @param pRequest la requete
     * @return un tableau de deux Strings et une liste : le trigger, la formule et les m�triques
     * utilis�es dans la formule
     * format�e pour l'affichage
     */
    public Object[] formatFormula(HttpServletRequest pRequest, QualityRuleDTO ruleDto) {
        final int ITEMS = 3;
        Object[] result = new Object[ITEMS];
        // initialisation par d�faut � des messages
        // indiquant que le trigger et la formule n'ont pas �t� d�finies.
        result[0] = WebMessages.getString(pRequest, "qualityRule.trigger.undefined");
        result[1] = WebMessages.getString(pRequest, "qualityRule.formula.undefined");
        result[2] = new ArrayList();
        // code d�fensif: normalement on est forc�ment sur une practiceRuleDTO
        // si on n'est pas sur une practice alors il n'y a pas d'aide
        if (ruleDto instanceof PracticeRuleDTO) {
            // On r�cup�re la formule et on l'adapte pour l'affichage
            // 2 cas :
            //          * SimpleFormula --> r�cup�ration simple de la formule
            //          * ConditionFormula --> r�cup�ration des conditions
            AbstractFormulaDTO formula = ((PracticeRuleDTO) ruleDto).getFormula();
            if (formula != null) {
            	result[2] = formula.getMeasureKinds();
                String trigger = formula.getTriggerCondition();
                if (trigger != null) {
                    result[0] = trigger;
                }
                if (formula instanceof SimpleFormulaDTO) {
                    result[1] = ((SimpleFormulaDTO) formula).getFormula();
                }
                if (formula instanceof ConditionFormulaDTO) {
                    Collection coll = ((ConditionFormulaDTO) formula).getMarkConditions();
                    String[] conditions = new String[coll.size()];
                    Iterator it = coll.iterator();
                    String formulaResult = "";
                    // 3 conditions : 
                    //   * index = 0 --> accept�
                    //   * index = 1 --> accept� avec r�serve
                    //   * index = 2 --> refus�
                    String accepted = WebMessages.getString(pRequest, "mark.less.1");
                    String reserves = WebMessages.getString(pRequest, "mark.less.2");
                    String refused = WebMessages.getString(pRequest, "mark.less.3");
                    int counter = 0;
                    String rule = "";
                    while (it.hasNext()) {
                        switch (counter) {
                            case 0 :
                                rule = accepted;
                                break;
                            case 1 :
                                rule = reserves;
                                break;
                            case 2 :
                                rule = refused;
                                break;
                            default :
                                break;
                        }
                        formulaResult = rule + ": " + it.next();
                        conditions[counter] = formulaResult;
                        counter++;

                    }
                    result[1] = conditions;
                }
            }
        }
        return result;
    }

}
