//Source file: D:\\cc_views\\squale_v0_0_act\\squale\\src\\squalix\\src\\com\\airfrance\\squalix\\tools\\compiling\\java\\compiler\\CompilerAdapterWSADImpl.java

package com.airfrance.squalix.tools.compiling.java.compiler.impl;

import java.util.List;

import com.airfrance.squalix.tools.compiling.java.adapter.JComponentAdapter;
import com.airfrance.squalix.tools.compiling.java.compiler.wsad.JWSADCompiler;


/**
 * Impl�mentation de l'adapteur pour le compilateur WSAD 5.x.
 * @author m400832 (by rose)
 * @version 1.0
 */
public class JWSADCompilerImpl extends JComponentAdapter {
    
    /**
     * Compilateur pour WSAD.
     */
    private JWSADCompiler mWSADCompiler;
    
    /**
     * M�thode de lancement de la compilation.
     * @throws Exception exception.
     */
    public void execute() throws Exception {
        mWSADCompiler.runCompilation();
    }
    
    /**
     * Constructeur par d�faut.
     * @param pProjectList liste des projets WSAD � compiler.
     */
    public JWSADCompilerImpl(List pProjectList) {
        mWSADCompiler = new JWSADCompiler(pProjectList);
    }
}
