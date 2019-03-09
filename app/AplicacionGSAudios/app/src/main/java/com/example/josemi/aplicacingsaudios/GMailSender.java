/**
 * @author: Sergio Chico Carrancio, modificado por: José Miguel Ramírez Sanz
 * @version: 1.0
 */

package com.example.josemi.aplicacingsaudios;

import android.util.Log;
import java.io.ByteArrayInputStream;
import java.io.File;
import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;
import java.security.Provider;
import java.security.Security;
import java.util.Properties;
import javax.activation.DataHandler;
import javax.activation.DataSource;
import javax.activation.FileDataSource;
import javax.mail.Message;
import javax.mail.Multipart;
import javax.mail.PasswordAuthentication;
import javax.mail.Session;
import javax.mail.Transport;
import javax.mail.internet.InternetAddress;
import javax.mail.internet.MimeBodyPart;
import javax.mail.internet.MimeMessage;
import javax.mail.internet.MimeMultipart;

/**
 * Clase que configura el smtp de google y permite enviar correos usándolo.
 */
public class GMailSender extends javax.mail.Authenticator {
    private String mailhost = "smtp.gmail.com"; //Host del servicio mail
    private String user; //Usuario que manda el mensaje
    private String password; //Contraseña de la cuenta de usuario
    private Session session; //Sesión

    /**
     * Clase que habilita XOAUTH2 SASL para poder utilizar el smtp de google.
     */
    public static final class Oauth2Provider extends Provider {
        public Oauth2Provider() {
            super("Google OAuth2 Provider", 1.0, "Provides the XOAUTH2 SASL Mechanism");
            put("SaslClientFactory.XOAUTH2", "com.somedomain.oauth2.OAuth2SaslClientFactory");
        }
    }

    static {
        Security.addProvider(new Oauth2Provider());
    }

    /**
     * Configura los parámetros de acceso para el smtp de google para un usuario y contraseña.
     *
     * @param user usuario que manda el correo
     * @param password contraseña
     */
    public GMailSender(String user, String password) {
        this.user = user;
        this.password = password;

        //Propiedades del correo
        Properties props = new Properties();
        props.setProperty("mail.transport.protocol", "smtp");
        props.setProperty("mail.host", mailhost);
        props.put("mail.smtp.auth", "true");
        props.put("mail.smtp.port", "465");
        props.put("mail.smtp.socketFactory.port", "465");
        props.put("mail.smtp.socketFactory.class",
                "javax.net.ssl.SSLSocketFactory");
        props.put("mail.smtp.socketFactory.fallback", "false");
        props.setProperty("mail.smtp.quitwait", "false");

        session = Session.getDefaultInstance(props, this);
    }

    /**
     * Autentifica la contraseña
     * @return password authentication
     */
    protected PasswordAuthentication getPasswordAuthentication() {
        return new PasswordAuthentication(user, password); //Autentificación de la contraseña
    }


    /**
     * Envía un email con un asunto, cuerpo, dirección de correo de origen, direcciones de correo de destino y
     * fichero adjunto.
     * Es importante no llamar a este método desde el hilo principal de Android, ya que se produce una excepción
     * si se realiza una conexión a internet desde este hilo.
     *
     * @param subject asunto del correo
     * @param body cuerpo de texto del correo
     * @param sender quiem lo envia
     * @param recipients quien recibe
     * @param attach adjuntos, en nuestro caso el comprimido
     */
    public synchronized void sendMail(String subject, String body, String sender, String recipients, File attach) {
        try {
            MimeMessage message = new MimeMessage(session);
            DataHandler handler = new DataHandler(new ByteArrayDataSource(body.getBytes(), "text/plain"));
            message.setSender(new InternetAddress(sender));
            message.setSubject(subject);
            message.setDataHandler(handler);

            Multipart multipart = new MimeMultipart();

            MimeBodyPart attachmentBodyPart = new MimeBodyPart();
            DataSource source = new FileDataSource(attach);
            attachmentBodyPart.setDataHandler(new DataHandler(source));
            attachmentBodyPart.setFileName(attach.getName());

            multipart.addBodyPart(attachmentBodyPart);

            message.setContent(multipart);

            if (recipients.indexOf(',') > 0)
                message.setRecipients(Message.RecipientType.TO, InternetAddress.parse(recipients));
            else
                message.setRecipient(Message.RecipientType.TO, new InternetAddress(recipients));
            Transport.send(message);
        } catch (Exception e) {
            Log.e("SendMail", e.getMessage(), e);
        }
    }

    /**
     * Clase que almacena un array de Bytes implementando javax.Datasource, para poder adjuntar
     * esta secuencia de bytes en un correo.
     */
    public class ByteArrayDataSource implements DataSource {
        private byte[] data;
        private String type;


        public ByteArrayDataSource(byte[] data, String type) {
            super();
            this.data = data;
            this.type = type;
        }

        public ByteArrayDataSource(byte[] data) {
            super();
            this.data = data;
        }

        public void setType(String type) {
            this.type = type;
        }

        public String getContentType() {
            if (type == null)
                return "application/octet-stream";
            else
                return type;
        }

        public InputStream getInputStream() throws IOException {
            return new ByteArrayInputStream(data);
        }

        public String getName() {
            return "ByteArrayDataSource";
        }

        public OutputStream getOutputStream() throws IOException {
            throw new IOException("Not Supported");
        }
    }
}
