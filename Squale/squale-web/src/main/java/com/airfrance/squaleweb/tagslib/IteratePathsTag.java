package com.airfrance.squaleweb.tagslib;

import javax.servlet.jsp.JspException;
import javax.servlet.jsp.tagext.TagSupport;

import org.apache.struts.util.RequestUtils;
import org.apache.struts.util.ResponseUtils;

import com.airfrance.squaleweb.util.SqualeWebActionUtils;
import com.airfrance.welcom.taglib.field.FieldTag;

/**
 */
public class IteratePathsTag
    extends TagSupport
{

    /** le nom permettant de d�finir la variable utilis�e dans le tag */
    private String name;

    /** Cl� pour identifier */
    private String key;

    /** permet de savoir lequel des tableaux de String on veut r�cup�rer */
    private String property;

    /** Le droit d'acc�s aux champs */
    private boolean disabled;

    /** Le caract�re obligatoire du champ */
    private String isRequired = "false";

    /**
     * @see javax.servlet.jsp.tagext.TagSupport#doEndTag() {@inheritDoc} M�thode de lancement du tag
     */
    public int doStartTag()
        throws JspException
    {
        // Publie
        String[] elementsTab = (String[]) RequestUtils.lookup( pageContext, name, property, null );
        FieldTag field = new FieldTag();
        field.setSize( "60" );
        if ( elementsTab != null )
        {
            // On supprime les cha�nes vides du tableau
            elementsTab = SqualeWebActionUtils.cleanValues( elementsTab );
        }
        // cas particulier, aucun champ n'a �t� rempli
        if ( elementsTab == null || elementsTab.length == 0 )
        {
            elementsTab = new String[] { "" };
        }
        // charge la page dans le tag a ex�cuter
        field.setPageContext( pageContext );
        // premier champ obligatoire
        field.setIsRequired( isRequired );
        field.setKey( key );
        field.setProperty( property );
        field.setDisabled( disabled );
        field.setStyleClassLabel( "td1" );
        String result = "";
        for ( int i = 0; i < elementsTab.length; i++ )
        {
            if ( i == 1 )
            {
                field.setIsRequired( "false" );
            }
            // positionne la valeur
            field.setValue( elementsTab[i] );
            ResponseUtils.write( pageContext, "<tr class=\"fondClair\">" );
            // lance le tag de welcom
            field.doStartTag();
            field.doEndTag();
            ResponseUtils.write( pageContext, "</tr>" );
        }
        return SKIP_BODY;
    }

    /**
     * @see javax.servlet.jsp.tagext.TagSupport#doEndTag() {@inheritDoc} M�thode de lancement du tag
     */
    public int doEndTag()
        throws JspException
    {
        return EVAL_PAGE;
    }

    /**
     * @return le nom utilis� dans le tag
     */
    public String getName()
    {
        return name;
    }

    /**
     * @param newUsedName le nouveau nom
     */
    public void setName( String newUsedName )
    {
        name = newUsedName;
    }

    /**
     * @return la cl�
     */
    public String getKey()
    {
        return key;
    }

    /**
     * @return la propri�t�
     */
    public String getProperty()
    {
        return property;
    }

    /**
     * @param newKey la nouvelle cl�
     */
    public void setKey( String newKey )
    {
        key = newKey;
    }

    /**
     * @param newProperty la nouvelle propri�t�
     */
    public void setProperty( String newProperty )
    {
        property = newProperty;
    }

    /**
     * @return le caract�re obligatoire du champ
     */
    public String getIsRequired()
    {
        return isRequired;
    }

    /**
     * @param newRequirement le nouveau caract�re obligatoire du champ
     */
    public void setIsRequired( String newRequirement )
    {
        isRequired = newRequirement;
    }

    /**
     * @return si le champ est readonly
     */
    public boolean isDisabled()
    {
        return disabled;
    }

    /**
     * @param pDisabled indique si le champ est readonly
     */
    public void setDisabled( boolean pDisabled )
    {
        disabled = pDisabled;
    }
}
