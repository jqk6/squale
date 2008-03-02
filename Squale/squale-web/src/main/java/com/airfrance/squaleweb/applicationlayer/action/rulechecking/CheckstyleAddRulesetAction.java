package com.airfrance.squaleweb.applicationlayer.action.rulechecking;

/**
 * Action pour permettre � squale de parser le fichier de configuration checkstyle
 * 
 * @author henix
 */
public class CheckstyleAddRulesetAction
    extends AbstractAddRuleSetAction
{

    /**
     * @see com.airfrance.squaleweb.applicationlayer.action.rulechecking.AbstractAddRuleSetAction#getAccessComponentName()
     * @return le nom utilis�
     */
    protected String getAccessComponentName()
    {
        return "CheckstyleAdmin";
    }
}
