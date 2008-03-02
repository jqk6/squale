package com.airfrance.squalix.tools.ruleschecking;

/**
 * Traitement d'un rapport checkstyle Cette interface permet de r�aliser le traitement des anomalies report�es dans un
 * rapport checkstyle. Toutes les informations disponibles dans le rapport sont pass�es en param�tre
 */
public interface ReportHandler
{
    /**
     * @param pFileName fichier
     * @param pLine ligne
     * @param pColumn colonne
     * @param pSeverity s�v�rit�
     * @param pMessage message
     * @param pSource source
     */
    public void processError( String pFileName, String pLine, String pColumn, String pSeverity, String pMessage,
                              String pSource );
}
