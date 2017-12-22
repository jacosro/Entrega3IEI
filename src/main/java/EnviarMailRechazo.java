import javax.mail.PasswordAuthentication;
import java.util.Properties;

import javax.mail.Message;
import javax.mail.MessagingException;
import javax.mail.Session;
import javax.mail.Transport;
import javax.mail.internet.InternetAddress;
import javax.mail.internet.MimeMessage;

import org.activiti.engine.delegate.DelegateTask;
import org.activiti.engine.delegate.TaskListener;

public class EnviarMailRechazo implements TaskListener {
	private static final long serialVersionUID = 1L;

	@Override
	public void notify(DelegateTask delegado) {
		final String username = "soquetesTim@gmail.com";
		final String password = "SocketesTeam21122017";
		String email = (String) delegado.getExecution().getVariable("IDCorreoElectronico");
		String motivo = (String) delegado.getExecution().getVariable("IDCuerpo");
		
		Properties props = new Properties();
		props.put("mail.smtp.auth", "true");
		props.put("mail.smtp.starttls.enable", "true");
		props.put("mail.smtp.host", "smtp.gmail.com");
		props.put("mail.smtp.port", "587");
        props.put("mail.smtp.user", username);
        
		Session session = Session.getDefaultInstance(props,  new javax.mail.Authenticator() {
			protected PasswordAuthentication getPasswordAuthentication() {
				return new PasswordAuthentication(username, password);
			}
		});
		
		String mensaje = "Su pedido ha sido rechazado por el siguiente motivo: \n\t" + motivo + "\n\nSentimos las molestias.";
		try {
			Message message = new MimeMessage(session);
			message.setFrom(new InternetAddress(username));
			message.setRecipients(Message.RecipientType.TO, InternetAddress.parse(email));
			message.setReplyTo(InternetAddress.parse(username, false));
			message.setText(mensaje);
			Transport.send(message);
		} catch (MessagingException e) {
			Log.write(e);
			throw new RuntimeException(e);
		} catch (Exception e) {
			Log.write(e);
		}
	}
}
