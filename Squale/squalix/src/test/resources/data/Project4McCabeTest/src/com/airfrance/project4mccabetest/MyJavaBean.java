package com.airfrance.project4mccabetest;

import sun.security.provider.MD5;

/**
 * Classe de test permettant de tester l'ex�cution de la t�che McCabe
 * sous Unix.
 * 
 * @author M400842
 */
public class MyJavaBean {

    /**
     * Nombre de lignes dans le fichier lu
     */
    private int mLineNumber;
    
    /**
     * Nom du fichier lu
     */
    private String mFileName = null;
    
    /**
     * Dur�e de lecture en ms.
     */
    private long mReadingTime;
    
    
    /**
     * @return le nom du fichier lu.
     */
    public String getFileName() {
        return mFileName;
    }

    /**
     * @return le nombre de lignes lues.
     */
    public int getLineNumber() {
        return mLineNumber;
    }

    /**
     * @return la dur�e de lecture.
     */
    public long getReadingTime() {
        return mReadingTime;
    }

    /**
     * @param pFileName le nom du fichier lu.
     */
    public void setFileName(String pFileName) {
        mFileName = pFileName;
    }

    /**
     * @param pLineNumber le nombre de lignes lues.
     */
    public void setLineNumber(int pLineNumber) {
        mLineNumber = pLineNumber;
    }

    /**
     * @param pReadingTime la dur�e de lecture.
     */
    public void setReadingTime(long pReadingTime) {
        mReadingTime = pReadingTime;
    }
    
    /**
     * @return l'objet sous forme de cha�ne.
     */
    public String toString() {
        String string = System.getProperty("line.separator");
        string += "Nom du fichier : " + mFileName + System.getProperty("line.separator");
        string += "Nombre de lignes lues : " + mLineNumber + System.getProperty("line.separator");
        string += "Dur�e de lecture : " + mReadingTime + " ms." + System.getProperty("line.separator");
        return string;
    }


}
