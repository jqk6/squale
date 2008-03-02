//Source file: D:\\cc_views\\squale_v0_0_act_M400843\\squale\\src\\squaleCommon\\src\\com\\airfrance\\squalecommon\\enterpriselayer\\businessobject\\component\\PackageBO.java

package com.airfrance.squalecommon.enterpriselayer.businessobject.component;

import java.util.Collection;

import com.airfrance.squalecommon.enterpriselayer.businessobject.UnexpectedRelationException;

/**
 * Repr�sente un package Java ou un namespace C++
 * 
 * @author m400842
 * @hibernate.subclass lazy="true" discriminator-value="Package"
 */
public class PackageBO
    extends AbstractComplexComponentBO
{

    /**
     * Instancie un nouveau composant.
     * 
     * @param pName Nom du composant.
     * @roseuid 42AFF06C033D
     */
    public PackageBO( final String pName )
    {
        super();
        setName( pName );
    }

    /**
     * Constructeur par d�faut.
     * 
     * @roseuid 42CBA5D60359
     */
    public PackageBO()
    {
        super();
    }

    /**
     * Constructeur complet.
     * 
     * @param pName nom du composant
     * @param pChildren les enfants
     * @param pParent Composant parent
     * @throws UnexpectedRelationException si la relation ne peut etre ajout�
     * @roseuid 42CBA5D60369
     */
    public PackageBO( String pName, Collection pChildren, AbstractComplexComponentBO pParent )
        throws UnexpectedRelationException
    {
        super( pName, pChildren, pParent );
    }

    /**
     * {@inheritDoc}
     * 
     * @see com.airfrance.squalecommon.enterpriselayer.businessobject.component.AbstractComponentBO#accept(com.airfrance.squalecommon.enterpriselayer.businessobject.component.ComponentVisitor,
     *      java.lang.Object)
     */
    public Object accept( ComponentVisitor pVisitor, Object pArgument )
    {
        return pVisitor.visit( this, pArgument );
    }
}
