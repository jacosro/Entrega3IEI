import org.activiti.engine.delegate.DelegateTask;
import org.activiti.engine.delegate.TaskListener;

public class AltaCliente implements TaskListener {
	private static final long serialVersionUID = 1L;

	@Override
	public void notify(DelegateTask tareaDelegada) {
		// TODO Auto-generated method stub
		// acceso a las variables introducidas en el formulario.
		ServicioClientes servicio = new ServicioClientes();
		String Nombre = (String) tareaDelegada.getExecution().getVariable("IDNombre");
		String direccion = (String) tareaDelegada.getExecution().getVariable("IDDireccion");
		String email = (String) tareaDelegada.getExecution().getVariable("IDCorreoElectronico");
		java.util.Date fechaAlta = (java.util.Date) tareaDelegada.getExecution().getVariable("IDFechaAlta");
		String NumTarjeta = (String) tareaDelegada.getExecution().getVariable("IDTarjeta"); // acceso al tipo enumerado
		String emisor = (String) tareaDelegada.getExecution().getVariable("IDEmisor");
		int idcliente = servicio.insertarCliente(Nombre, direccion, email, fechaAlta, NumTarjeta, emisor);
		tareaDelegada.getExecution().setVariable("IDCliente", idcliente);
	}
}