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
		final String password = "socketesTeam21122017";
		System.out.println("Inicio de enviÌ�o de correo");

		String email = (String) delegado.getExecution().getVariable("IDCorreoElectronico");
		String cuerpo = (String) delegado.getExecution().getVariable("IDCuerpo");

		Properties props = new Properties();
		props.put("mail.smtp.auth", "true");
		props.put("mail.smtp.starttls.enable", "true");
		props.put("mail.smtp.host", "smtp.gmail.com");
		props.put("mail.smtp.port", "587");
		Session session = Session.getInstance(props, new javax.mail.Authenticator() {
			protected PasswordAuthentication getPasswordAuthentication() {
				return new PasswordAuthentication(username, password);
			}
		});
		
		try {
			Message message = new MimeMessage(session);
			message.setFrom(new InternetAddress("jsanchezdiaz@gmail.com"));
			message.setRecipients(Message.RecipientType.TO, InternetAddress.parse(email)); 
			message.setText("PEDIDO RECHAZADO\n" + cuerpo);
			Transport.send(message);
		} catch (MessagingException e) {
			System.out.println("ExcepcioÌ�n detectada" + e);
			throw new RuntimeException(e);
		}

	}
}
