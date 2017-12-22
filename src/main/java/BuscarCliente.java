import org.activiti.engine.delegate.DelegateExecution;
import org.activiti.engine.delegate.JavaDelegate;

public class BuscarCliente implements JavaDelegate {

  @Override
  public void execute(DelegateExecution ejecucion) throws Exception { // TODO Auto-generated method stub
		long codCliente = (Long) ejecucion.getVariable("IDCliente");
		System.out.println("Buscando cliente " + codCliente);
		ServicioClientes servicio = new ServicioClientes();
		Cliente cliente = servicio.buscarCliente((int) codCliente);
		boolean encontrado = cliente != null;
		// a�adir el resultado al motor.
		System.out.println("Encontrado " + encontrado); 
		ejecucion.setVariable("encontrado", encontrado); 
		if (encontrado) {
			ejecucion.setVariable("IDCorreoElectronico", cliente.getEmail());
			ejecucion.setVariable("IDNombre", cliente.getNombre());
			ejecucion.setVariable("IDDireccion", cliente.getDireccion());
			ejecucion.setVariable("IDFechaAlta", cliente.getFechaAlta());
			ejecucion.setVariable("IDTarjeta", cliente.getTarjetaCredito());
			ejecucion.setVariable("IDEmisor", cliente.getEmisor());			
		}
	}
}