package com.airfrance.squalecommon.enterpriselayer.businessobject.result.rulechecking;

/**
 * R�sultat de transgression de Checkstyle
 * 
 * @hibernate.subclass discriminator-value="CheckstyleTransgression"
 */
public class CheckstyleTransgressionBO
    extends RuleCheckingTransgressionBO
{
    // Vide. Cette classe sert juste � avoir un nom de subclass diff�rent
    // dans la base.
}
