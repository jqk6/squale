package com.airfrance.project4mccabetest;

import java.net.URL;


/**
 * Classe de test permettant de tester l'ex�cution de la t�che McCabe
 * sous Unix.
 * 
 * @author M400842
 */
public class Index {
    /**
     * 
     * @param pArgs arguments
     */
    public static void main(String[] pArgs) {
        URL url = Index.class.getClassLoader().getResource("com/airfrance/project4mccabetest/toread.txt");
        MyFileReader reader = new MyFileReader();
        reader.readFile(url);
    }
}
