package com.airfrance.squaleweb.util;

import java.lang.reflect.Method;
import java.text.DateFormat;
import java.text.DecimalFormat;
import java.text.NumberFormat;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.Collection;
import java.util.Date;
import java.util.Iterator;
import java.util.LinkedList;
import java.util.List;
import java.util.Locale;

import javax.servlet.http.HttpServletRequest;

import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;

import com.airfrance.squalecommon.enterpriselayer.businessobject.component.AuditBO;
import com.airfrance.squalecommon.enterpriselayer.businessobject.result.ErrorBO;
import com.airfrance.squalecommon.enterpriselayer.businessobject.result.MarkBO;
import com.airfrance.squalecommon.util.ConstantRulesChecking;
import com.airfrance.squaleweb.resources.WebMessages;

/**
 * Fournit des fonctionnalit�s utiles � plusieurs actions.
 * 
 * @author M400842
 */
public class SqualeWebActionUtils
{
    /**
     * Logger
     */
    private static Log log = LogFactory.getLog( SqualeWebActionUtils.class );

    /**
     * Images en fonction de la note 0..3
     */
    public static final String[] IMG =
        { "images/pictos/bad.png", "images/pictos/good.png", "images/pictos/best.png",
            "images/pictos/super.png", "images/pictos/na.gif" };

    /**
     * Constante "-" pour l'affichage
     */
    public static final String DASH = "-";

    /**
     * d�fini une valeur de seuil significatif pour une variation de la tendance de constante � un peux mieux Tant que
     * la variation de la note est en dessous de ce seuil, on consid�re qu'il n'y a pas eu d'�volution
     */
    private final static float BETTER = 0.05f;

    /**
     * d�fini une valeur de seuil significatif pour une variation de la tendance de constante � beaucoup mieux
     */
    private final static float MUCH_BETTER = 0.3f;

    /**
     * @param currentMark la note courante
     * @param predecessorMark la note pr�c�dente
     * @return l'image repr�sentant l'�volution entre currentMark et predecessorMark
     */
    public static String getImageForTrend( String currentMark, String predecessorMark )
    {
        String result = "images/pictos/na.gif";
        // Dans ce cas, c'est simple: il n'y a pas eu d'�volution
        // la note pr�c�dente peut etre null ou initialis�e � la chaine vide (pour le form)
        if ( isValidMark( currentMark ) && isValidMark( predecessorMark ) )
        {
            float diff =
                Float.parseFloat( currentMark.replace( ',', '.' ) )
                    - Float.parseFloat( predecessorMark.replace( ',', '.' ) );
            if ( Math.abs( diff ) < BETTER )
            {
                // l'�volution n'est pas significative, fl�che constante
                result = "images/pictos/ar_blueAF.gif";
            }
            else if ( Math.abs( diff ) < MUCH_BETTER )
            {
                // changement peu significatif
                if ( diff < 0 )
                { // l�g�re d�gradation
                    result = "images/pictos/ar_blueAF_RD.gif";
                }
                else
                { // l�g�re am�lioration
                    result = "images/pictos/ar_blueAF_RU.gif";
                }
            }
            else
            {
                if ( diff < 0 )
                { // d�gradation significative
                    result = "images/pictos/ar_blueAF_D.gif";
                }
                else
                { // am�lioration significative
                    result = "images/pictos/ar_blueAF_U.gif";
                }
            }
        }
        return result;
    }

    /**
     * Affiche l'image.
     * 
     * @param pNote la note.
     * @param pRequest la requ�te
     * @return le chemin de l'image
     */
    public static String generatePictoWithTooltip( String pNote, HttpServletRequest pRequest )
    {
        // On r�cup�re l'index en fonction de la note tronqu�e.
        int imgIndex = generatePicto( pNote );
        String pictureHelp = WebMessages.getString( pRequest, "project.results.mark.status_" + imgIndex );
        if ( imgIndex != IMG.length - 1 )
        {
            int imgIndexRound = Integer.parseInt( formatFloat( pNote ).substring( 0, 1 ) );
            if ( imgIndexRound > imgIndex )
            {
                pictureHelp +=
                    " (" + WebMessages.getString( pRequest, "project.results.mark.nearly.status_" + imgIndexRound )
                        + ")";
            }
        }
        // on remplace ' par \' pour le javascript
        return "<img src=\"" + IMG[imgIndex] + "\"title=\"" + pictureHelp.replaceAll( "'", "\\\\'" )
            + "\" border=\"0\" />";
    }

    /**
     * R�cup�re le pictogramme associ� � une note
     * 
     * @param pNote la note.
     * @return le chemin de l'image
     */
    public static int generatePicto( String pNote )
    {
        // initialisation � l'image Na par d�faut
        int i = IMG.length - 1;
        // V�rification simpliste de la note pour �viter des exceptions
        // lors de sa conversion
        Float currentMark;
        if ( isValidMark( pNote ) )
        {
            i = Integer.parseInt( truncFloat( pNote ).substring( 0, 1 ) );
            i = ( i > IMG.length - 1 ? IMG.length - 1 : i );
        }
        return i;
    }

    /**
     * @param aFloat la note sous forme de String � tronquer
     * @return la note sous sa forme flottante en la tronquant � un chiffre apr�s la virgule
     */
    private static String truncFloat( String aFloat )
    {
        final int toTrunc = 10;
        String result = DASH;
        float mark = stringToFloat( aFloat );
        if ( mark != MarkBO.NOT_NOTED_VALUE )
        {
            // on tronque et garde que un chiffre apr�s la virgule.
            mark = mark * toTrunc;
            mark = (int) mark;
            result = "" + mark / toTrunc;
        }
        return result;
    }

    /**
     * @param pNote La note de 0 � 3 ou non valide (i.e vide (?))
     * @return un boolean indiquant que la chaine est bien une note (non vide)
     */
    public static boolean isValidMark( String pNote )
    {
        return pNote != null && pNote.length() > 0 && Character.isDigit( pNote.charAt( 0 ) );
    }

    /**
     * @param aFloat le float � formatter
     * @return le float arrondi � un chiffre apr�s la virgule ou "-" si la chaine ne peut pas etre correctement format�e
     */
    public static String formatFloat( String aFloat )
    {
        String result = DASH;
        float mark = stringToFloat( aFloat );
        if ( mark != MarkBO.NOT_NOTED_VALUE )
        {
            // on ne garde que un chiffre apr�s la virgule.
            result = formatFloat( mark );
        }
        return result;
    }

    /**
     * @param aFloat la note sous forme de String
     * @return la note sous sa forme flottante en l'arrondissant � un chiffre apr�s la virgule
     */
    private static float stringToFloat( String aFloat )
    {
        float result = MarkBO.NOT_NOTED_VALUE;
        if ( isValidMark( aFloat ) )
        {
            // Remplacement du "." par une ","
            result = Float.parseFloat( aFloat.replace( ',', '.' ) );
        }
        return result;
    }

    /**
     * Factorisation de code, aucun test n'est fait
     * 
     * @param pFloat le float � formater
     * @return la chaine associ�e au float
     */
    private static String formatFloat( float pFloat )
    {
        NumberFormat nf = NumberFormat.getInstance();
        nf.setMaximumFractionDigits( 1 );
        nf.setMinimumFractionDigits( 1 );
        String result = nf.format( pFloat );
        return result;
    }

    /**
     * @param aFloat le nombre � formatter
     * @return le float formatt� en String avec un chiffre apr�s la virgule
     */
    public static String formatFloat( Float aFloat )
    {
        String result = DASH;
        if ( aFloat != null && aFloat.floatValue() != MarkBO.NOT_NOTED_VALUE )
        {
            result = formatFloat( aFloat.floatValue() );
        }
        return result;
    }

    /**
     * Donne une valeur � un attribut de l'objet.<br>
     * La classe de l'objet doit avoir le setter associ� au nom de l'attribut : setMonAttribut et doit prendre une
     * String en param�tre.
     * 
     * @param pObject l'objet � remplir.
     * @param pSetterName le nom du setter de l'attribut.
     * @param pValue la nouvelle valeur de l'attribut.
     */
    public static void setValue( final Object pObject, final String pSetterName, final String pValue )
    {
        Class[] paramsType = { String.class };
        try
        {
            // Obtention de la m�thode
            Method setter = pObject.getClass().getMethod( pSetterName, paramsType );
            Object[] params = { null };
            // V�rification du type de cha�ne
            // TODO voir si le test sur un nombre est pertinent - redondance entre la regexp et le parseInt
            if ( null != pValue
                && ( ( pValue.matches( "-?[0-9]*" ) && Integer.parseInt( pValue ) != -1 ) || !pValue.matches( "-?[0-9]*" ) ) )
            {
                params[0] = pValue;
            }
            setter.invoke( pObject, params );
        }
        catch ( Exception e )
        {
            log.error( e, e );
        }
    }

    /**
     * Remplit le bean pass� en param�tre avec les valeurs donn�es.
     * 
     * @param pBean le bean � remplir.
     * @param pAttributes la liste ordonn�e des attributs (String).
     * @param pValues la liste ordonn�e des valeurs (String).
     * @param pMessages pr�cise si les nom des attributs sont des cl�s � r�soudre, ils sont alors suffix�s de
     *            ".attribute".
     */
    public static void fullFillBean( final Object pBean, final List pAttributes, final List pValues,
                                     final boolean pMessages )
    {
        String attributeName = null;
        String setterName = null;
        for ( int i = 0; null != pBean && i < pAttributes.size(); i++ )
        {
            if ( pMessages )
            {
                attributeName = WebMessages.getString( (String) pAttributes.get( i ) + ".attribute" );
            }
            else
            {
                attributeName = (String) pAttributes.get( i );
            }
            setterName = "set" + attributeName.substring( 0, 1 ).toUpperCase() + attributeName.substring( 1 );
            setValue( pBean, setterName, (String) pValues.get( i ) );
        }
    }

    /**
     * Retourne une liste h�t�rog�ne sous la forme d'une liste de String.
     * 
     * @param pList la liste � transformer.
     * @return une liste de String.
     */
    public static List getAsStringsList( final List pList )
    {
        LinkedList result = null;
        if ( null != pList )
        {
            result = new LinkedList();
            Iterator it = pList.iterator();
            Object value = null;
            // Parcours de la collection et conversion de chaque valeur
            while ( it.hasNext() )
            {
                value = it.next();
                if ( null != value )
                {
                    // Conversion sp�cifique pour un nombre flottant
                    if ( value.getClass().equals( Float.class ) )
                    {
                        result.addLast( formatFloat( (Float) value ) );
                    }
                    else
                    {
                        result.addLast( value.toString() );
                    }
                }
                else
                {
                    // On conserve la valeur null initiale
                    result.addLast( null );
                }
            }
        }
        return result;
    }

    /**
     * Equivalent de la fonction sprintf du C.<br>
     * Ne fonctionne que pour les %s, puisque les param�tres sont des String.
     * 
     * @param pString la chaine � compl�ter.
     * @param pValue la liste des valeurs � affecter.
     * @return la cha�ne compl�t�e.
     */
    public static String sprintf( final String pString, final String[] pValue )
    {
        String result = pString;
        for ( int i = 0; i < pValue.length; i++ )
        {
            result.replaceFirst( "%s", pValue[i] );
        }
        return result;
    }

    /**
     * Retourne une chaine formattant la date.
     * 
     * @param pDate la date � formatter.
     * @param lang langue d'affichage
     * @return une chaine repr�santant le date.
     */
    public static String getFormattedDate( Date pDate, Locale lang )
    {
        DateFormat df = DateFormat.getDateInstance( DateFormat.LONG, lang );
        return df.format( pDate );
    }

    /**
     * Retourne la date d'aujourd'hui selon le format d�fini par la cl�.
     * 
     * @param lang langue d'affichage
     * @param formatKey la cl� du fichier de properties d�finissant le format de la date
     * @return une chaine repr�santant le date.
     */
    public static String getTodayDate( Locale lang, String formatKey )
    {
        Calendar today = Calendar.getInstance();
        return getFormattedDate( lang, today.getTime(), formatKey );
    }

    /**
     * Retourne la date format�e selon le format d�fini par la cl�.
     * 
     * @param lang langue d'affichage
     * @param pDate la date � formater
     * @param formatKey la cl� du fichier de properties d�finissant le format de la date
     * @return une chaine repr�santant le date.
     */
    public static String getFormattedDate( Locale lang, Date pDate, String formatKey )
    {
        SimpleDateFormat sdf = new SimpleDateFormat( WebMessages.getString( lang, formatKey ), lang );
        return sdf.format( pDate );
    }

    /**
     * Suffixe toutes les valeurs du tableau.
     * 
     * @param pArray le tableau � transformer.
     * @param pSuffix la suffixe � ajouter.
     * @param pKey pr�cise si les valeurs suffix�s sont des cl�s, auquel cas elles sont remplac�es par les valeurs
     *            associ�es.
     * @return un tableau de la m�me taille avec les valeurs suffix�s.
     */
    public static String[] suffixString( final String[] pArray, final String pSuffix, boolean pKey )
    {
        String[] result = new String[pArray.length];
        for ( int i = 0; i < pArray.length; i++ )
        {
            if ( pKey )
            {
                result[i] = WebMessages.getString( pArray[i] + pSuffix );
            }
            else
            {
                result[i] = pArray[i] + pSuffix;
            }
        }
        return result;
    }

    /**
     * Obtention de l'image associ�e � un niveau d'erreur
     * 
     * @param pErrorLevel le niveau d'erreur
     * @return image correspondante au niveau d'erreur
     */
    public static String getImageForErrorLevel( String pErrorLevel )
    {
        String result;
        if ( pErrorLevel.equals( ErrorBO.CRITICITY_FATAL ) )
        {
            result = "images/pictos/error.png";
        }
        else if ( pErrorLevel.equals( ErrorBO.CRITICITY_WARNING ) )
        {
            result = "images/pictos/warning.png";
        }
        else
        {
            result = "images/pictos/info.png";
        }
        return result;
    }

    /**
     * Obtention de l'image associ�e � la s�v�rit� de la r�gle
     * 
     * @param pSeverity la s�v�rit�
     * @return image correspondante � la s�v�rit�
     */
    public static String getImageForRuleSeverity( String pSeverity )
    {
        String result;
        if ( pSeverity.equals( ConstantRulesChecking.ERROR_LABEL ) )
        {
            result = "images/pictos/error.png";
        }
        else if ( pSeverity.equals( ConstantRulesChecking.WARNING_LABEL ) )
        {
            result = "images/pictos/warning.png";
        }
        else
        {
            result = "images/pictos/info.png";
        }
        return result;
    }

    /**
     * Retourne le contenu de pValues sans les valeurs vides.
     * 
     * @param pValues la tableau des valeurs � nettoyer
     * @return un tableau de String propre
     */
    public static String[] cleanValues( final String[] pValues )
    {
        String[] result = null;
        // Nettoyage des noms
        ArrayList temp = new ArrayList();
        for ( int i = 0; i < pValues.length; i++ )
        {
            if ( pValues[i].trim().length() > 0 )
            {
                temp.add( pValues[i] );
            }
        }
        String[] type = new String[0];
        result = (String[]) temp.toArray( type );
        return result;
    }

    /**
     * @param pRequest la requete http
     * @return le formatter de nombre correspondant � la locale
     */
    public static NumberFormat getNumberFormat( HttpServletRequest pRequest )
    {
        NumberFormat formatter = null;
        try
        {
            // En anglais par d�faut
            formatter = (DecimalFormat) DecimalFormat.getInstance();
            // Si c'est en francais , on n'affiche pas les "," pour s�parer les chiffres
            if ( pRequest.getLocale().getLanguage().equals( Locale.FRENCH.toString() ) )
            {
                ( (DecimalFormat) formatter ).setGroupingUsed( false );
            }
        }
        catch ( NumberFormatException nfe )
        {
            // en cas de probl�me rencontr�, on renvoie le formatter par d�faut
            formatter = NumberFormat.getInstance();
        }
        return formatter;
    }

    /**
     * @param pStrings la liste de String
     * @param pSeparator le s�parateur
     * @return la string repr�sentant le tableau dont les �lement sont s�par�s pas <code>separator</code>
     */
    public static String formatArray( Collection pStrings, String pSeparator )
    {
        String result = "";
        for ( Iterator it = pStrings.iterator(); it.hasNext(); )
        {
            result += (String) it.next();
            if ( it.hasNext() )
            {
                result += pSeparator;
            }
        }
        return result;
    }

    /**
     * Permet d'aller � la ligne dans un menu pour pouvoir afficher le nom en entier au lieu qu'il soit tronqu� car la
     * charte graphique d'Air France impose une taille fixe
     * 
     * @param pItem l'item a couper
     * @return l'item avec des '\n' tous les n caract�res
     */
    public static String formatStringForMenuItem( String pItem )
    {
        final int MAX_CHAR = 26; // Nombre de caract�res qu'on suppose affichable
        StringBuffer result = new StringBuffer( pItem );
        int nbInsertion = result.length() / MAX_CHAR;
        for ( int i = 1; i <= nbInsertion; i++ )
        {
            result.insert( i * MAX_CHAR, "... " ); // On d�cale de 3 caract�res
        }
        return result.toString();
    }

    /**
     * R�cup�re la cl� de configuration repr�sentant le nombre minimum d'applications que doit avoir le menu pour �tre
     * group�
     * 
     * @param request la requ�te
     * @return le nombre minimum d'applications que doit avoir le menu pour �tre group� (10 par d�faut)
     */
    public static int getApplicationMenuKey( HttpServletRequest request )
    {
        // On groupe les applications si on en a plus de 10 dans le menu
        final int MIN_APPLIS = 10;
        // On r�cup�re le nombre minimum d'applications que doit avoir le menu pour �tre group�
        String minApplisForGrouping = WebMessages.getString( request, "nbApplisForGrouping" );
        int minApplis;
        try
        {
            minApplis = Integer.parseInt( minApplisForGrouping );
        }
        catch ( NumberFormatException nfe )
        {
            // Si erreur de cl�, on met le nombre par d�faut
            minApplis = MIN_APPLIS;
        }
        return minApplis;
    }

    /**
     * @param pRequest la requ�te
     * @param pTres la liste des tres
     * @return la l�gende pr�cisant la signification des tres
     */
    public static String getLegendForTres( HttpServletRequest pRequest, List pTres )
    {
        String legend = "<ul>";
        for ( int i = 0; i < pTres.size(); i++ )
        {
            String tre = (String) pTres.get( i );
            String acronyme = tre;
            int lastDot = tre.lastIndexOf( "." );
            if ( lastDot > 0 )
            {
                acronyme = tre.substring( lastDot + 1 );
            }
            legend += "<li>" + acronyme + " = " + WebMessages.getString( pRequest, tre ) + "</li>";
        }
        return legend + "</ul>";
    }

    /**
     * @param pStatus le statut de l'audit
     * @return le type servant � la cl� repr�sentant le statut de l'audit.
     */
    public static String getAuditKind( int pStatus )
    {
        // TODO : Il faudrait exploiter le statut de l'audit
        // --> faire une passe sur la cl� "kind" pour la remplacer par
        // un int et r�cup�rer la string par les fichiers de properties
        String result = "terminated";
        if ( pStatus == AuditBO.FAILED )
        {
            result = "failed";
        }
        else if ( pStatus == AuditBO.PARTIAL )
        {
            result = "partial";
        }
        return result;
    }
}
