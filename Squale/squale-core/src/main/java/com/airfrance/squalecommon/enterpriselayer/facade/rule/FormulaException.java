package com.airfrance.squalecommon.enterpriselayer.facade.rule;

/**
 * Exception de formule Cette exception est lev�e pendant la conversion d'une formule ou son �valuation
 */
public class FormulaException
    extends Exception
{

    /**
     * @param pString texte
     */
    public FormulaException( String pString )
    {
        super( pString );
    }

    /**
     * @param pString texte
     * @param e exception
     */
    public FormulaException( String pString, Throwable e )
    {
        super( pString, e );
    }

    /**
     * @param e exception
     */
    public FormulaException( Throwable e )
    {
        super( e );
    }

}
