import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;

import org.activiti.engine.delegate.DelegateTask;
import org.activiti.engine.delegate.TaskListener;

public class AltaCliente implements TaskListener {
	private static final long serialVersionUID = 1L;

	@Override
	public void notify(DelegateTask tareaDelegada) {
		ServicioClientes servicio = new ServicioClientes();
		String Nombre = (String) tareaDelegada.getExecution().getVariable("IDNombre");
		String direccion = (String) tareaDelegada.getExecution().getVariable("IDDireccion");
		String email = (String) tareaDelegada.getExecution().getVariable("IDCorreoElectronico");
		java.util.Date fechaAlta = (java.util.Date) tareaDelegada.getExecution().getVariable("IDFechaAlta");
		String NumTarjeta = (String) tareaDelegada.getExecution().getVariable("IDTarjeta"); 
		String emisor = (String) tareaDelegada.getExecution().getVariable("IDEmisor");
			
		int idcliente = servicio.insertarCliente(Nombre, direccion, email, fechaAlta, NumTarjeta, emisor);
		tareaDelegada.getExecution().setVariable("IDCliente", idcliente);
	}
}