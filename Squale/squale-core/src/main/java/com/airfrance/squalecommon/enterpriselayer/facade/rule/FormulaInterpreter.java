package com.airfrance.squalecommon.enterpriselayer.facade.rule;

import java.lang.reflect.InvocationTargetException;
import java.util.HashMap;
import java.util.Iterator;

import org.apache.commons.beanutils.BeanUtils;
import org.python.core.PyException;
import org.python.core.PyFloat;
import org.python.core.PyInteger;
import org.python.core.PyNone;
import org.python.core.PyObject;
import org.python.util.PythonInterpreter;

import com.airfrance.squalecommon.enterpriselayer.businessobject.result.MeasureBO;
import com.airfrance.squalecommon.enterpriselayer.businessobject.rule.AbstractFormulaBO;

/**
 * Interpr�te de formule
 */
public class FormulaInterpreter {
    /** Derni�re formule compil�e � des fins d'optimisation*/
    private long mLastFormulaComputed;

    /** Interpreteur python */
    private PythonInterpreter mInterpreter;

    /**
     * Obtention d'un interpr�teur Jython
     * @return interpr�teur
     */
    public PythonInterpreter getInterpreter() {
        if (mInterpreter == null) {
            mInterpreter = new PythonInterpreter();
            mInterpreter.exec("from math import *\n");
        }
        return mInterpreter;
    }

    /**
     * V�rification de la syntaxe d'une formule
     * @param pFormula formule
     * @throws FormulaException si erreur
     */
    public void checkSyntax(AbstractFormulaBO pFormula) throws FormulaException {
        FormulaConverter converter = new FormulaConverter();
        String result = converter.convertFormula(pFormula);
        PythonInterpreter inter = getInterpreter();
        try {
            inter.exec(result);
        } catch (PyException e) {
            throw new FormulaException(e);
        }
        // Test des param�tres utilis�s
        MeasureBOFactory factory = new MeasureBOFactory();
        MeasureBO[] measures = new MeasureBO[pFormula.getMeasureKinds().size()];
        Iterator measureKinds = pFormula.getMeasureKinds().iterator();
        int i = 0;
        while (measureKinds.hasNext()) {
            measures[i] = factory.createMeasure(pFormula.getComponentLevel(), (String) measureKinds.next());
            i++;
        }
        checkParameters(pFormula, measures);
    }

    /**
     * Evaluation d'une formule
     * @param pFormula formule
     * @param pMeasures mesures
     * @throws FormulaException si erreur
     * @return note attribu�e
     */
    public Number evaluate(AbstractFormulaBO pFormula, MeasureBO[] pMeasures) throws FormulaException {
        Number result = null;
        try {
            result = evaluateWithoutAdapt(pFormula, pMeasures);
            // Ajustement du r�sultat
            if (null != result) {
                double value = result.doubleValue();
                // Ajustement de la valeur dans l'intervale autoris�
                final int minValue = 0;
                final int maxValue = 3;
                if (value < minValue) {
                    value = minValue;
                } else if (value > maxValue) {
                    value = maxValue;
                }
                // Conversion de la valeur en type
                result = new Double(value);
            }
        } catch (Throwable t) {
            // Renvoi d'une exception si la couche Jython rencontre une erreur
            throw new FormulaException(t);
        }
        return result;
    }

    /**
     * @param pFormula la formule
     * @param pMeasures mesures
     * @return le r�sultat une fois la mesure appliqu�e
     * @throws FormulaException si erreur
     */
    public Number evaluateWithoutAdapt(AbstractFormulaBO pFormula, MeasureBO[] pMeasures) throws FormulaException {
        Number result = null;
        try {
            // Calcul
            PyObject r = calculate(pFormula, pMeasures);
            // Conversion du r�sultat
            result = convertResult(r);
        } catch (Throwable t) {
            // Renvoi d'une exception si la couche Jython rencontre une erreur
            throw new FormulaException(t);
        }
        return result;
    }

    /**
     * @param pFormula la formule
     * @param pMeasures mesures
     * @return le r�sulat en phyton une fois la formule appliqu�e
     * @throws FormulaException si erreur
     */
    private PyObject calculate(AbstractFormulaBO pFormula, MeasureBO[] pMeasures) throws FormulaException {
        // Obtention d'un interpr�te Python
        PythonInterpreter inter = getInterpreter();
        PyObject result = null;
        try {
            // Compilation de la formule correspondate
            compileFormula(pFormula);
            // Optimisation du traitement des cha�nes
            StringBuffer function = new StringBuffer();
            function.append(FormulaConverter.FUNCTION_PREFIX);
            long formulaId = pFormula.getId();
            if (formulaId < 0) {
                formulaId = 0;
            }
            function.append(formulaId);
            function.append('(');
            // Traitement de chacun des param�tres
            for (int i = 0; i < pMeasures.length; i++) {
                // Chaque param�tre suit une r�gle de nommage
                String param = "measure" + i;
                inter.set(param, pMeasures[i]);
                // Cas de plusieurs param�tres
                if (i > 0) {
                    function.append(',');
                }
                function.append(param);
            }
            function.append(')');
            // Appel de la fonction
            result = inter.eval(function.toString());
        } catch (Throwable t) {
            // Renvoi d'une exception si la couche Jython rencontre une erreur
            throw new FormulaException(t);
        }
        return result;
    }

    /**
     * Compilation d'une formule
     * Une optimisation est faite pour �viter de recompiler plusieurs fois
     * la m�me formule
     * @param pFormula formule � compiler
     */
    private void compileFormula(AbstractFormulaBO pFormula) {
        if (mLastFormulaComputed != pFormula.getId()) {
            FormulaConverter converter = new FormulaConverter();
            String formula = converter.convertFormula(pFormula);
            PythonInterpreter inter = getInterpreter();
            inter.exec(formula);
            mLastFormulaComputed = pFormula.getId();
        }
    }

    /**
     * Conversion d'un r�sultat Python
     * La conversion s'op�re sur les objets de type PyFloat ou PyInteger,
     * un autre objet g�n�re une erreur
     * @param pResult objet � convertir
     * @return valeur correspondante ou null si la conversion ne peut se faire
     * @throws FormulaException si erreur dans le type
     */
    public Double convertResult(PyObject pResult) throws FormulaException {
        Double result;
        // Les fonctions peuvent renvoyer null comme type PYTHON
        // on ne traite donc que les objets de type num�rique
        // Attention en Jython, isNumbeType renvoie toujours true
        if (pResult.isNumberType()) {
            double value;
            // Traitement du cas entier
            if (pResult instanceof PyInteger) {
                value = ((PyInteger) pResult).getValue();
            } else if (pResult instanceof PyFloat) {
                value = ((PyFloat) pResult).getValue();
            } else {
                // Le type attendue n'est pas num�rique
                throw new FormulaException(RuleMessages.getString("result.badtype", new Object[] { pResult }));
            }
            // Conversion de la valeur en type
            result = new Double(value);
        } else if (pResult instanceof PyNone) {
            // Un r�sultat vide est g�n�r� dans le cas d'un trigger non activ� par exemple
            result = null;
        } else {
            // Pour le cas o� isNumberType renvoie false ce qui est peu probable
            // pour la version actuelle
            // Le type attendue n'est pas num�rique
            throw new FormulaException(RuleMessages.getString("result.badtype", new Object[] { pResult }));
        }
        return result;
    }

    /**
     * V�rification des param�tres
     * @param pFormula formule
     * @param pMeasures mesure
     * @throws FormulaException si erreur
     * @return true si les param�tres sont corrects
     */
    private boolean checkParameters(AbstractFormulaBO pFormula, MeasureBO[] pMeasures) throws FormulaException {
        boolean result = true;
        // Construction d'une map avec les types de mesure et les mesures
        HashMap map = new HashMap();
        Iterator measureKinds = pFormula.getMeasureKinds().iterator();
        int i = 0;
        // Parcours des mesures
        while (measureKinds.hasNext()) {
            map.put(measureKinds.next(), pMeasures[i]);
            i++;
        }
        // R�cup�ration des attributs utilis�s par la formule
        ParameterExtraction extracter = new ParameterExtraction();
        Iterator attributes = extracter.getFormulaParameters(pFormula).iterator();
        // On v�rifie que les param�tres utilis�s dans la formule correspondent bien
        // � des m�triques existantes
        while (attributes.hasNext()) {
            FormulaParameter parameter = (FormulaParameter) attributes.next();
            Object measure = map.get(parameter.getMeasureKind());
            // Si la mesure n'existe pas on l�ve une exception
            if (measure == null) {
                // La m�trique est inconnue, on indique ce qui manque
                throw new FormulaException(RuleMessages.getString("measure.unknown", new Object[] { parameter.getMeasureKind()}));
            } else {
                // On v�rifie que la mesure contient bien une propri�t� avec ce nom
                if (getProperty(measure, parameter.getMeasureAttribute()) == null) {
                    result = false;
                }
            }
        }
        return result;
    }

    /**
     * Obtention d'une propri�t�
     * @param pMeasure mesure
     * @param pAttribute attribut
     * @return propri�t� si elle existe
     * @throws FormulaException si erreur
     */
    private String getProperty(Object pMeasure, String pAttribute) throws FormulaException {
        String value = null;
        // On pourrait aussi intercepter le type Exception et factoriser
        // le message d'erreur
        try {
            // On essaye d'obtenir la propri�t� correspondante
            value = BeanUtils.getProperty(pMeasure, pAttribute);
        } catch (IllegalAccessException e) {
            // Renvoi d'une exception encapul�e
            throw new FormulaException(RuleMessages.getString("parameter.unknown", new Object[] { pAttribute }), e);
        } catch (InvocationTargetException e) {
            // Renvoi d'une exception encapul�e
            throw new FormulaException(RuleMessages.getString("parameter.unknown", new Object[] { pAttribute }), e);
        } catch (NoSuchMethodException e) {
            // Renvoi d'une exception encapul�e
            throw new FormulaException(RuleMessages.getString("parameter.unknown", new Object[] { pAttribute }), e);
        }
        return value;
    }
}
