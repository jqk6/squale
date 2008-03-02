//Source file: D:\\cc_views\\squale_v0_0_act\\squale\\src\\squalix\\src\\com\\airfrance\\squalix\\util\\csv\\CSVBeanInstantiator.java

package com.airfrance.squalix.util.csv;

import java.lang.reflect.Method;

import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;

/**
 * Permet d'instancier des objets, et de manipuler les appels de m�thodes sur ceux-ci.
 * 
 * @author m400842
 * @version 1.0
 */
public class CSVBeanInstanciator
{

    /**
     * Logger.
     */
    private static final Log LOGGER = LogFactory.getLog( CSVBeanInstanciator.class );

    /**
     * Instancie un objet de la classe identifi�e par la param�tre, en utilisant le constructeur sans param�tre.
     * 
     * @param pClassName nom de la classe pleinement qualifi�.
     * @return une instance de CSVBean
     * @exception Exception si un probl�me appara�t.
     * @roseuid 42942CA70075
     */
    public Object instanciate( String pClassName )
        throws Exception
    {
        Class objClass = Class.forName( pClassName );
        Object bean = objClass.newInstance();
        return bean;
    }

    /**
     * Invoque la m�thode de l'objet <code>pBean</code> d�sign�e par <code>pMethod</code> en passant en param�tre
     * une instance de la classe <code>pType</code> contenant la valeur <code>pValue.<br>
     * La classe <code>pType</code> doit poss�der un constructeur prenant en param�tre 
     * une <code>String</code>.<br>
     * Si un probl�me survient, le bean est retourn� tel que re�u.
     * 
     * @param pBean Le bean � modifier.
     * @param pMethod Le champ.
     * @param pValue Le type objet du champ.
     * @return le bean modifi�.
     * @throws Exception si un probl�me d'affectation appara�t
     * @roseuid 42942D2D0077
     */
    public Object setValue( final Object pBean, final Method pMethod, final String pValue )
        throws Exception
    {
        Object value = getValue( pMethod.getParameterTypes()[0], pValue );
        if ( null != value )
        {
            Object values[] = { value };
            pMethod.invoke( pBean, values );
        }
        return pBean;
    }

    /**
     * Cr�e une instance de <code>pObjClass</code> avec la valeur initiale <code>pValue</code>.<br>
     * La classe <code>pType</code> doit poss�der un constructeur prenant en param�tre une <code>String</code>.
     * 
     * @param pObjClass la classe de l'objet � instancier
     * @param pValue la valeur initiale
     * @return un objet initialis� � pValue
     * @roseuid 42B2BC3500E0
     */
    private Object getValue( final Class pObjClass, final String pValue )
    {
        Class[] types = { String.class };
        Object[] values = { pValue };
        Object field = null;
        if ( null != pValue )
        {
            try
            {
                field = pObjClass.getConstructor( types ).newInstance( values );
            }
            catch ( Exception e )
            {
                // Les exceptions attrap�es ici n'ont rien de grave,
                // elles sont dues � une diff�rence entre le type attendu et le type
                // r�el du param�tre...
                LOGGER.debug( e, e );
                field = null;
            }
        }
        return field;
    }

    /**
     * Constructeur par d�faut.
     * 
     * @roseuid 42CE6C6C012C
     */
    public CSVBeanInstanciator()
    {
    }
}
