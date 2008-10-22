package com.airfrance.squalix.tools.cpd;


/**
 * Classe r�alisant la d�tection du copier-coller en Cobol.
 */
public class CobolCpdProcessing
    extends AbstractCpdProcessing
{
    /** Seuil de d�tection de copier-coller */
    private static final int COBOL_THRESHOLD = 100;

    /**
     * {@inheritDoc}
     * 
     * @see com.airfrance.squalix.tools.cpd.AbstractCpdTask#getTokenThreshold()
     */
    @Override
    protected int getTokenThreshold()
    {
        return COBOL_THRESHOLD;
    }

    /**
     * {@inheritDoc}
     * 
     * @see com.airfrance.squalix.tools.cpd.AbstractCpdProcessing#getExtension()
     */
    @Override
    protected String[] getExtensions()
    {
        return new String[] { ".cob", ".txt" };
    }

}
