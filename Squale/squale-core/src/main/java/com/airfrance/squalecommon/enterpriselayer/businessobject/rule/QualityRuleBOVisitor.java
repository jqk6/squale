package com.airfrance.squalecommon.enterpriselayer.businessobject.rule;

/**
 * Visiteur des r�gles qualit� Ce design pattern permet d'externaliser des traitements sur les facteurs, crit�res et
 * pratiques
 */
public interface QualityRuleBOVisitor
{
    /**
     * @param pFactorRule facteur
     * @param pArgument argument
     * @return objet
     */
    public Object visit( FactorRuleBO pFactorRule, Object pArgument );

    /**
     * @param pCriteriumRule crit�re
     * @param pArgument argument
     * @return objet
     */
    public Object visit( CriteriumRuleBO pCriteriumRule, Object pArgument );

    /**
     * @param pPracticeRule pratique
     * @param pArgument argument
     * @return objet
     */
    public Object visit( PracticeRuleBO pPracticeRule, Object pArgument );
}
