/**
 * Copyright (C) 2008-2010, Squale Project - http://www.squale.org
 *
 * This file is part of Squale.
 *
 * Squale is free software: you can redistribute it and/or modify
 * it under the terms of the GNU Lesser General Public License as
 * published by the Free Software Foundation, either version 3 of the
 * License, or any later version.
 *
 * Squale is distributed in the hope that it will be useful,
 * but WITHOUT ANY WARRANTY; without even the implied warranty of
 * MERCHANTABILITY or FITNESS FOR A PARTICULAR PURPOSE.  See the
 * GNU General Public License for more details.
 *
 * You should have received a copy of the GNU Lesser General Public License
 * along with Squale.  If not, see <http://www.gnu.org/licenses/>.
 */
package org.squale.squalecommon.util.mail.javamail;

import java.util.Collection;
import java.util.Iterator;
import java.util.Properties;

import javax.mail.Message;
import javax.mail.MessagingException;
import javax.mail.Session;
import javax.mail.Transport;
import javax.mail.internet.AddressException;
import javax.mail.internet.InternetAddress;
import javax.mail.internet.MimeMessage;

import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;

import org.squale.jraf.commons.exception.JrafDaoException;
import org.squale.jraf.helper.PersistenceHelper;
import org.squale.jraf.spi.persistence.ISession;
import org.squale.squalecommon.daolayer.config.AdminParamsDAOImpl;
import org.squale.squalecommon.enterpriselayer.businessobject.config.AdminParamsBO;
import org.squale.squalecommon.util.mail.IMailerProvider;
import org.squale.squalecommon.util.mail.MailException;
import org.squale.squalecommon.util.mail.MailMessages;

/**
 * Mail provider, implementation for javaMail
 */
public class JavaMailProviderImpl
    implements IMailerProvider
{

    /** logger */
    private static final Log LOG = LogFactory.getLog( JavaMailProviderImpl.class );

    /**
     * SMTP server to use for sent mail.
     */
    private static String smtpServer;

    /**
     * Default sender.
     */
    private static String senderAddress;

    /**
     * SMTP authentication needed ?
     */
    private static boolean isSmtpAuthenticationNeeded;

    /**
     * SMTP authentication : user name
     */
    private static String username;

    /**
     * SMTP authentication : password
     */
    private static String password;

    /**
     * Constructor
     */
    protected JavaMailProviderImpl()
    {
    }

    /**
     * This method send mail
     * 
     * @param sender Sender Address
     * @param recipients List of recipient address
     * @param subject Subject of the mail
     * @param content Content of the mail
     * @throws MailException Exception happened the send of the mail
     */
    public void sendMail( String sender, String[] recipients, String subject, String content )
        throws MailException
    {

        // Initialization of the mail provider
        init();

        // if there is mail configuration information
        if ( smtpServer != null && ( senderAddress != null || sender != null ) )
        {

            // Properties used for sending mails
            Properties props = System.getProperties();
            props.put( "mail.smtp.host", smtpServer );

            Session session = smtpAuthent( props );

            // Header message definition
            MimeMessage message = new MimeMessage( session );
            try
            {
                // If a sender argument is not null we use it
                if ( sender != null )
                {
                    message.setFrom( new InternetAddress( sender ) );
                }
                // If the sender argument is null
                else
                {
                    message.setFrom( new InternetAddress( senderAddress ) );
                }

                // We send to each recipient
                for ( int i = 0; i <= recipients.length - 1; i++ )
                {
                    String mailAddress = (String) recipients[i];
                    LOG.debug( "Recipent : " + recipients[i] );
                    message.addRecipient( Message.RecipientType.BCC, new InternetAddress( mailAddress ) );
                }

                // Body message definition
                message.setSubject( subject );
                message.setText( content, "iso-8859-1" );

                // Send of the mail
                Transport.send( message );

            }
            catch ( AddressException e )
            {
                throw new MailException( e );
            }
            catch ( MessagingException e )
            {
                throw new MailException( e );
            }
        }
        else
        {
            String message = MailMessages.getString( "mail.exception.noConfig" );
            throw new MailException( message );
        }

    }

    /**
     * Initialize the mail provider
     */
    public static void init()
    {
        try
        {

            AdminParamsDAOImpl dao = AdminParamsDAOImpl.getInstance();
            AdminParamsBO bo;
            ISession session = PersistenceHelper.getPersistenceProvider().getSession();
            Collection<AdminParamsBO> adminParamsBOCollection = dao.findByKeyLike( session, AdminParamsBO.MAIL );
            Iterator<AdminParamsBO> adminParamsIterator = adminParamsBOCollection.iterator();
            while ( adminParamsIterator.hasNext() )
            {
                bo = adminParamsIterator.next();
                if ( bo.getParamKey().equals( AdminParamsBO.MAIL_SMTP_SERVER ) )
                {
                    smtpServer = bo.getParamValue();
                }
                else if ( bo.getParamKey().equals( AdminParamsBO.MAIL_SENDER_ADDRESS ) )
                {
                    senderAddress = bo.getParamValue();
                }
                else if ( bo.getParamKey().equals( AdminParamsBO.MAIL_SMTP_AUTHENT_NEEDED ) )
                {
                    if ( bo.getParamValue().equals( true ) )
                    {
                        isSmtpAuthenticationNeeded = true;
                    }
                    else
                    {
                        isSmtpAuthenticationNeeded = false;
                    }
                }
                else if ( bo.getParamKey().equals( AdminParamsBO.MAIL_SMTP_USERNAME ) )
                {
                    username = bo.getParamValue();
                }
                else if ( bo.getParamKey().equals( AdminParamsBO.MAIL_SMTP_PASSWORD ) )
                {
                    password = bo.getParamValue();
                }
            }

        }

        catch ( JrafDaoException e )
        {
            LOG.error( MailMessages.getString( "mail.exception.initialization" ) );
            LOG.error( e.getMessage() );
        }
    }

    /**
     * This method return a session according to we use the SMTP authentication or not
     * 
     * @param props the properties for sending a mail
     * @return the session for sending the mail
     */
    private Session smtpAuthent( Properties props )
    {
        Session session = null;
        if ( isSmtpAuthenticationNeeded )
        {
            SmtpAuthenticator authent = new SmtpAuthenticator( username, password );
            props.put( "mail.smtp.auth", "true" );
            session = Session.getDefaultInstance( props, authent );
        }
        else
        {
            // Session recovering
            session = Session.getDefaultInstance( props, null );
        }
        return session;
    }

}
