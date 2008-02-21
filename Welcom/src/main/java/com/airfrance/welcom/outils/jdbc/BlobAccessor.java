/*
 * Cr�� le 20 oct. 04
 *
 * Pour changer le mod�le de ce fichier g�n�r�, allez � :
 * Fen�tre&gt;Pr�f�rences&gt;Java&gt;G�n�ration de code&gt;Code et commentaires
 */
package com.airfrance.welcom.outils.jdbc;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.io.OutputStream;
import java.io.OutputStreamWriter;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.zip.GZIPInputStream;
import java.util.zip.GZIPOutputStream;

import javax.servlet.ServletException;

import oracle.sql.BLOB;

import com.airfrance.welcom.outils.WelcomConfigurator;

/**
 * Classe d'acces a un blob dans une base de donn�e
 * @author R�my Bouquet
 *
 */
public class BlobAccessor {

    /**
     * Taille d'un bloc de donn�es
     */
    private final static int CHUNK_SIZE = 2048;
    /**
    * Taille d'un gros bloc de donn�es
    */
    private final static int HEAVY_CHUNK_SIZE = 65535;

    /**
     * D�termine si le flux est zipp�.
     */
    private boolean zipped = false;
    /**
    * D�termine l'ensemble de caract�res � utiliser pout l'encodage des caract�res
    */
    private String charSet = "";

    /**
     * Constructeur
     *
     * @param pzipped : d�termine si les donn�e sont zipp�es avant d'�tre mises en base.
     */
    public BlobAccessor(final boolean pzipped) {
        charSet = WelcomConfigurator.getMessage(WelcomConfigurator.ENCODING_CHARSET);
        zipped = pzipped;
    }

    /**
     * Ecrit le blob en base
     * @param statement : le statement qui repr�sente la comande � ex�cuter.
     * @param column : la colonne du blob dans la table.
     * @param data : un tableau d'octets contenant les donn�es � ins�rer.(limit� � 64 Ko)
     * @throws ServletException servlet exception
     * @throws SQLException sql exception
     * @throws IOException io exception
     */
    public void write(final WStatement statement, final String column, final byte data[]) throws ServletException, SQLException, IOException {
        if (statement.getSQL().toLowerCase().indexOf("for update") < 0) {
            statement.add("for update");
        }
        final ResultSet rs = statement.executeQuery();

        if ((rs != null) && rs.next()) {

            final BLOB blob = (BLOB) rs.getBlob(column);

            if (blob == null) {
                throw new SQLException("le Blob n'est pas initialis� en Base de donn�e.");
            }

            if (zipped) {
                final GZIPOutputStream gzipout = new GZIPOutputStream(blob.getBinaryOutputStream());
                gzipout.write(data, 0, data.length);
                gzipout.flush();
                gzipout.close();
            } else {
                final OutputStream out = blob.getBinaryOutputStream();
                out.write(data, 0, data.length);
                out.flush();
                out.close();
            }

            rs.close();
        } else {
            throw new SQLException("Blob introuvable");
        }
    }

    /**
     *
     * �crit des donn�es dans le blob
     *
     * @param statement : le statement qui repr�sente la comande � ex�cuter.
     * @param column : la colonne du blob dans la table.
     * @param data : un tableau d'octets contenant les donn�es � ins�rer.(limit� � 64 Ko)
     * @throws ServletException servlet exception
     * @throws SQLException sql exception
     * @throws IOException io exception
     */
    public void write(final WStatement statement, final String column, final String data) throws ServletException, SQLException, IOException {
        if (statement.getSQL().toLowerCase().indexOf("for update") < 0) {
            statement.add("for update");
        }

        final ResultSet rs = statement.executeQuery();

        if ((rs != null) && rs.next()) {
            final oracle.sql.BLOB blob = (oracle.sql.BLOB) rs.getBlob(column);

            if (blob == null) {
                throw new SQLException("le Blob n'est pas initialis� en Base de donn�e.");
            }

            final java.io.OutputStream os = blob.getBinaryOutputStream();

            if (zipped) {
                final GZIPOutputStream gzipout = new GZIPOutputStream(os);
                final OutputStreamWriter out = new OutputStreamWriter(gzipout, charSet);
                out.write(data);
                out.flush();
                out.close();
            } else {
                final OutputStreamWriter out = new OutputStreamWriter(os, charSet);
                out.write(data);
                out.flush();
                out.close();
            }

            rs.close();
        } else {
            throw new SQLException("Blob introuvable");
        }
    }

    /**
     *
     * �crit des donn�es dans le blob
     *
     * @param statement : le statement qui repr�sente la comande � ex�cuter.
     * @param column : la colonne du blob dans la table.
     * @param data : une InputStream contenant les donn�es � ins�rer dans le blob
      * @throws ServletException servlet exception
     * @throws SQLException sql exception
     * @throws IOException io exception
    */
    public void write(final WStatement statement, final String column, final InputStream data) throws ServletException, SQLException, IOException {
        if (statement.getSQL().toLowerCase().indexOf("for update") < 0) {
            statement.add("for update");
        }

        final ResultSet rs = statement.executeQuery();
        int nbBytes = 0;
        final byte buf[] = new byte[CHUNK_SIZE];

        if ((rs != null) && rs.next()) {
            final oracle.sql.BLOB blob = (oracle.sql.BLOB) rs.getBlob(column);

            if (blob == null) {
                throw new SQLException("le Blob n'est pas initialis� en Base de donn�e.");
            }

            final java.io.OutputStream os = blob.getBinaryOutputStream();

            if (zipped) {
                final GZIPOutputStream gzipout = new GZIPOutputStream(os);
                nbBytes = data.read(buf);

                while (nbBytes != -1) {
                    gzipout.write(buf, 0, nbBytes);
                    nbBytes = data.read(buf);
                }

                gzipout.flush();
                gzipout.close();
            } else {
                nbBytes = data.read(buf);

                while (nbBytes != -1) {
                    os.write(buf, 0, nbBytes);
                    nbBytes = data.read(buf);
                }

                os.flush();
                os.close();
            }

            rs.close();
        } else {
            throw new ServletException("Blob introuvable");
        }
    }

    /**
     * lit des donn�es dans le blob
     *
     * @param rs : java.sql.ResultSet comprenant la colonne du blob
     * @param column : nom de la colone du blob.
     * @return retourne un tableau d'octets repr�sentant les donn�es du blob (taille limit�e � 64Ko).
      * @throws ServletException servlet exception
     * @throws SQLException sql exception
     * @throws IOException io exception
    */
    public byte[] getBytes(final ResultSet rs, final String column) throws ServletException, SQLException, IOException {
        byte b[] = null;

        if (rs != null) {
            final BLOB monBlob = (BLOB) rs.getBlob(column);

            if (monBlob == null) {
                return null;
            }

            final java.io.InputStream is = monBlob.getBinaryStream();

            if (zipped) {
                b = new byte[HEAVY_CHUNK_SIZE];

                final GZIPInputStream gzipin = new GZIPInputStream(is);
                gzipin.read(b);
                gzipin.close();
            } else {
                b = new byte[new Long(monBlob.length()).intValue()];
                is.read(b);
                is.close();
            }
        } else {
            throw new ServletException("Blob introuvable");
        }

        return b;
    }

    /**
     * @param rs : java.sql.ResultSet comprenant la colonne du blob
     * @param column : nom de la colone du blob.
     * @return retourne les don�es du blob sous forme de chaine de caract�res.
     * @throws ServletException servlet exception
     * @throws SQLException sql exception
     * @throws IOException io exception
    */
    public String getString(final ResultSet rs, final String column) throws ServletException, SQLException, IOException {
        String result = "";

        if (rs != null) {
            final BLOB monBlob = (BLOB) rs.getBlob(column);

            if (monBlob == null) {
                return "";
            }

            final java.io.InputStream is = monBlob.getBinaryStream();

            if (zipped) {
                final GZIPInputStream gzipin = new GZIPInputStream(is);
                final BufferedReader br = new BufferedReader(new InputStreamReader(gzipin, charSet));
                String buf = br.readLine();

                while (buf != null) {
                    result += buf;
                    buf = br.readLine();
                }

                br.close();
            } else {
                final BufferedReader br = new BufferedReader(new InputStreamReader(is, charSet));
                String buf = br.readLine();

                while (buf != null) {
                    result += buf;
                    buf = br.readLine();
                }

                br.close();
            }
        } else {
            throw new ServletException("Blob introuvable");
        }

        return result;
    }

    /**
     * ATTENTION. Si le param�tre zipped du BlobAccessor est � true cette methode retourne un GZIPInputStream
     * pour lire les donn�es "d�zip�es" il faut le tanstyper avant la lecture.
     *
     * @param rs : java.sql.ResultSet comprenant la colonne du blob
     * @param column : nom de la colone du blob.
     * @return retourne une InputStream contenant les donn�es du Blob.
     * @throws ServletException servlet exception
     * @throws SQLException sql exception
     * @throws IOException io exception
    */
    public InputStream getInputStream(final ResultSet rs, final String column) throws ServletException, SQLException, IOException {
        if ((rs != null) && rs.next()) {
            final BLOB monBlob = (BLOB) rs.getBlob(column);

            if (!zipped) {
                return monBlob.getBinaryStream();
            }

            return new GZIPInputStream(monBlob.getBinaryStream());
        } else {
            throw new ServletException("Blob introuvable, Attention le rs.next() est effectu� par le BlobAccessor");
        }
    }

    /**
     * @return charSet
     */
    public String getCharSet() {
        return charSet;
    }

    /**
     * @return charSet
     */
    public boolean isZipped() {
        return zipped;
    }

    /**
     * @param string charSet
     */
    public void setCharSet(final String string) {
        charSet = string;
    }

    /**
     * @param b zipped
     */
    public void setZipped(final boolean b) {
        zipped = b;
    }
}