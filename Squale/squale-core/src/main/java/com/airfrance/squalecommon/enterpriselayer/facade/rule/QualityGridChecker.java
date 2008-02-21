package com.airfrance.squalecommon.enterpriselayer.facade.rule;

import java.util.Iterator;

import com.airfrance.squalecommon.enterpriselayer.businessobject.rule.CriteriumRuleBO;
import com.airfrance.squalecommon.enterpriselayer.businessobject.rule.FactorRuleBO;
import com.airfrance.squalecommon.enterpriselayer.businessobject.rule.PracticeRuleBO;
import com.airfrance.squalecommon.enterpriselayer.businessobject.rule.QualityGridBO;
import com.airfrance.squalecommon.enterpriselayer.businessobject.rule.QualityRuleBOVisitor;

/**
 * V�rification d'une grille qualit�
 * La v�rification se limite au test de compilation des
 * formules et fonctions rattach�es � une grille
 */
public class QualityGridChecker implements QualityRuleBOVisitor {
    // D�sactivation du cache python pour utilisation dans environnement
    // serveur J2EE dans lequel on n'a pas la main sur le r�pertoire
    // utilis� par jython pour la gestion du cache des packages
    static {
        System.setProperty("python.cachedir.skip","true");
    }

    /** Interpr�teur de formule */
    private FormulaInterpreter mInterpreter;
    
    /**
     * Constructeur
     */
    public QualityGridChecker() {
        mInterpreter = new FormulaInterpreter();
    }
    
    /**
     * V�rification d'une grille
     * @param pGrid grille
     * @param pError erreurs sur cette grille
     */
    public void checkGrid(QualityGridBO pGrid, StringBuffer pError) {
        Iterator factors = pGrid.getFactors().iterator();
        while (factors.hasNext()) {
            ((FactorRuleBO)factors.next()).accept(this, pError);
        }
    }
    
    /** (non-Javadoc)
     * @see com.airfrance.squalecommon.enterpriselayer.businessobject.rule.QualityRuleBOVisitor#visit(com.airfrance.squalecommon.enterpriselayer.businessobject.rule.FactorRuleBO, java.lang.Object)
     */
    public Object visit(FactorRuleBO pFactorRule, Object pArgument) {
        Iterator criteria = pFactorRule.getCriteria().keySet().iterator();
        while (criteria.hasNext()) {
            ((CriteriumRuleBO)criteria.next()).accept(this, pArgument);
        }
        return null;
    }

    /** (non-Javadoc)
     * @see com.airfrance.squalecommon.enterpriselayer.businessobject.rule.QualityRuleBOVisitor#visit(com.airfrance.squalecommon.enterpriselayer.businessobject.rule.CriteriumRuleBO, java.lang.Object)
     */
    public Object visit(CriteriumRuleBO pCriteriumRule, Object pArgument) {
        Iterator practices = pCriteriumRule.getPractices().keySet().iterator();
        while (practices.hasNext()) {
            ((PracticeRuleBO)practices.next()).accept(this, pArgument);
        }
        return null;
    }

    /** (non-Javadoc)
     * @see com.airfrance.squalecommon.enterpriselayer.businessobject.rule.QualityRuleBOVisitor#visit(com.airfrance.squalecommon.enterpriselayer.businessobject.rule.PracticeRuleBO, java.lang.Object)
     */
    public Object visit(PracticeRuleBO pPracticeRule, Object pArgument) {
        StringBuffer errorBuffer = (StringBuffer) pArgument;
        if (pPracticeRule.getFormula()!=null) {
            FormulaInterpreter interpreter = new FormulaInterpreter();
            WeightFunctionInterpreter functionInter = new WeightFunctionInterpreter(interpreter.getInterpreter());
            try {
                // V�rification de la syntaxe de la fonction de pond�ration
                functionInter.checkSyntax(pPracticeRule.getWeightFunction());
            } catch(WeightFunctionException wfe) {
                // Si une erreur se produit sur l'�valuation de la fonction
                // on note celle-ci dans le rapport des erreurs
                Object[] args = new Object[] {pPracticeRule.getWeightFunction(), pPracticeRule.getName()};
                errorBuffer.append(RuleMessages.getString("function.error", args));
                errorBuffer.append('\n');
                errorBuffer.append(wfe.getMessage());
            }
            try {
                // V�rification de la syntaxe de la formule
                // un exception est lev�e si elle est incorrecte
                interpreter.checkSyntax(pPracticeRule.getFormula());
            } catch (FormulaException e) {
                // Si une erreur se produit sur le calcul d'une formule
                // on note celle-ci dans le rapport des erreurs
                errorBuffer.append(RuleMessages.getString("formula.error", new Object[] { pPracticeRule.getName()}));
                errorBuffer.append('\n');
                errorBuffer.append(e.getMessage());
            }

        }
        return null;
    }

}
