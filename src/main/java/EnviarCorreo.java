import javax.mail.PasswordAuthentication;
import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.Properties;

import javax.mail.Message;
import javax.mail.MessagingException;
import javax.mail.Session;
import javax.mail.Transport;
import javax.mail.internet.InternetAddress;
import javax.mail.internet.MimeMessage;

import org.activiti.engine.delegate.DelegateTask;
import org.activiti.engine.delegate.TaskListener;

/**
 * No se muestran los imports
 */
public class EnviarCorreo implements TaskListener {
	private static final long serialVersionUID = 1L;

	@Override
	public void notify(DelegateTask delegado) {
		String mailBody = "Estos son los articulos que estan preparados para enviarle: \n";
		boolean retraso = false;

		Connection conn = Conexion.abrirConexion();
		if (conn != null) {
			String SQLPedidos = "SELECT * FROM lineapedidos;";

			try {
				PreparedStatement statementPedidos = conn.prepareStatement(SQLPedidos);
				ResultSet resultPedidos = statementPedidos.executeQuery();

				while(resultPedidos.next()) {
					int lineapedido = resultPedidos.getInt("Articulos_idArticulos");
					int cantidadPedido = resultPedidos.getInt("Cantidad");

					if(cantidadPedido > 0) {
						mailBody += "\tArticulo con ID: " + lineapedido + "\tCantidad: " + cantidadPedido;
					} else {
						retraso = true;
						mailBody += "\tArticulo con ID: " + lineapedido + "\tCantidad: " + cantidadPedido + "(De momento no disponemos de suficiente stock)";
					}

				}// fin while

				if(retraso) {
					mailBody += "\n\nDebido a que no disponemos de algunos articulos, la entrega se va a "+
									"retrasar un poco para poder llevarle los productos en una sola entrega." +
									"\n Disculpe las molestias ;(";
				}

			} catch (SQLException e) {
				e.printStackTrace();
			}
			Conexion.cerrarConexion();

			System.out.println("Inicio de enviÌ�o de correo");
	
			String email = (String) delegado.getExecution().getVariable("IDCorreoelectronico");
			String asunto = (String) delegado.getExecution().getVariable("IDAsunto");
			String cuerpo = (String) delegado.getExecution().getVariable("IDCuerpo");
			int tiempoEntrega = Integer.parseInt((String) delegado.getExecution().getVariable("IDTiempoEntrega"));
		
			final String username = "soquetesTim@gmail.com";
			final String password = "SocketesTeam21122017";
			
			Properties props = new Properties();
			props.put("mail.smtp.auth", "true");
			props.put("mail.smtp.starttls.enable", "true");
			props.put("mail.smtp.host", "smtp.gmail.com");
			props.put("mail.smtp.port", "587");
	        props.put("mail.smtp.user", username);
			Session session = Session.getDefaultInstance(props, null);
			
			try {
				Message message = new MimeMessage(session);
				message.setFrom(new InternetAddress(username));
				message.setRecipients(Message.RecipientType.TO, InternetAddress.parse(email));
				message.setReplyTo(InternetAddress.parse(username, false));
				message.setText("En un plazo de " + tiempoEntrega + " le enviaremos los siguientes articulos:" + "\n" + mailBody);
				Transport.send(message);
			} catch (MessagingException e) {
				System.out.println("ExcepcioÌ�n detectada" + e);
				throw new RuntimeException(e);
			}
		}
	}
}
