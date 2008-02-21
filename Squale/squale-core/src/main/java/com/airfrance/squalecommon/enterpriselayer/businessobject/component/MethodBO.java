package com.airfrance.squalecommon.enterpriselayer.businessobject.component;

import com.airfrance.squalecommon.enterpriselayer.businessobject.UnexpectedRelationException;


/**
 * Repr�sente une m�thode au sens Java et C++
 * 
 * @hibernate.subclass
 * lazy="true"
 * discriminator-value="Method"
 */
public class MethodBO extends AbstractComponentBO {

    /**
     * Chemin complet du fichier � partir du projet
     */
    private String mLongFileName;

    /**
     * Le num�ro de ligne de la m�thode dans le fichier
     */
    private int mStartLine;
    
    /**
     * Instancie un nouveau composant.
     * @param pName Nom du composant.
     */
    public MethodBO(final String pName) {
        super();
        setName(pName);
    }
    
    /**
     * Constructeur par d�faut.
     */
    public MethodBO() {
        super();
    }
    
    /**
     * Constructeur complet.
     * @param pName nom du composant
     * @param pParent Composant parent
     * @throws UnexpectedRelationException si la relation ne peut etre ajout�e
     */
    public MethodBO(String pName, AbstractComplexComponentBO pParent) throws UnexpectedRelationException {
        super(pName, pParent);
    }

    /**
     * @return le chemin complet du fichier au projet
     * 
     * @hibernate.property 
     * name="longFileName" 
     * column="LongFileName" 
     * type="string" 
     * length="2048"
     * not-null="false" 
     * unique="false"
     */
    public String getLongFileName() {
        return mLongFileName;
    }

    /**
     * @return le num�ro de ligne de la m�thode dans le fichier
     * 
     * @hibernate.property 
     * name="startLine" 
     * column="StartLine" 
     * type="integer" 
     * length="10"
     * not-null="false" 
     * unique="false"
     */
    public int getStartLine() {
        return mStartLine;
    }

    /**
     * @param pLongFileName le nouveau chemin
     */
    public void setLongFileName(String pLongFileName) {
        mLongFileName = pLongFileName;
    }

    /**
     * @param pLine le num�ro de ligne de la m�thode dans le fichier
     */
    public void setStartLine(int pLine) {
        mStartLine = pLine;
    }

    /** 
     * {@inheritDoc}
     * @see com.airfrance.squalecommon.enterpriselayer.businessobject.component.AbstractComponentBO#accept(com.airfrance.squalecommon.enterpriselayer.businessobject.component.ComponentVisitor, java.lang.Object)
     */
    public Object accept(ComponentVisitor pVisitor, Object pArgument) {
        return pVisitor.visit(this, pArgument);
    }

}
