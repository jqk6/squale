package com.airfrance.squalecommon.enterpriselayer.businessobject.result.rulechecking;

/**
 * R�sultat de transgression de Macker
 * 
 * @hibernate.subclass discriminator-value="MackerTransgression"
 */
public class MackerTransgressionBO
    extends RuleCheckingTransgressionBO
{
    // Vide. Cette classe sert juste � avoir un nom de subclass diff�rent
    // dans la base.
}
