/*
 * Cr�� le 29 juil. 05, par m400832.
 */
package com.airfrance.squalix.tools.compiling.java.beans;

/**
 * @author m400832
 * @version 1.0
 */
public class JXMLProject
    extends JProject
{

    /**
     * Target du fichier ANT � lancer.
     */
    private String mTarget = "";

    /**
     * Constructeur par d�faut.
     */
    public JXMLProject()
    {
    }

    /**
     * Getter.
     * 
     * @return la target du fichier ANT � lancer.
     */
    public String getTarget()
    {
        return mTarget;
    }

    /**
     * Setter.
     * 
     * @param pTarget Target du fichier ANT � lancer.
     */
    public void setTarget( String pTarget )
    {
        mTarget = pTarget;
    }

}
