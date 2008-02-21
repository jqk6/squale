package com.airfrance.squalix.tools.external.bugtracking.qc;

import com.airfrance.squalecommon.util.messages.BaseMessages;

public class BugTrackingQCMessages extends BaseMessages {
  /** Instance de BugTrackingMessages */ 
    static private BugTrackingQCMessages mMessages = new BugTrackingQCMessages();
    
    
    /**
     * Constructeur par d�faut
     */
    private BugTrackingQCMessages () {
        super("com.airfrance.squalix.tools.external.bugtracking.bugtrackingqc");
    }
    
    
    /**
     * Retourne la chaine de caract�re identifi�e par la cl�
     * @param pKey le nom de la cl�
     * @return la chaine de caract�re
     */
    public static String getString(String pKey) {
        return mMessages.getBundleString(pKey);
    }

}
