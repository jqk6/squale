package com.airfrance.squaleweb.applicationlayer.formbean.component.parameters;

/**
 * Formulaire pour Macker avec le profil J2EE Cette class sert juste � r�cup�rer le bon nom de la t�che
 */
public class J2EEMackerForm
    extends MackerForm
{

    /**
     * @see com.airfrance.squaleweb.applicationlayer.formbean.component.parameters.AbstractParameterForm#getNameInSession()
     *      {@inheritDoc}
     */
    public String getNameInSession()
    {
        return "j2eeMackerForm";
    }

    /**
     * @see com.airfrance.squaleweb.applicationlayer.formbean.component.parameters.AbstractParameterForm#getTaskName()
     *      {@inheritDoc}
     */
    public String getTaskName()
    {
        return "J2eeMackerTask";
    }

}
