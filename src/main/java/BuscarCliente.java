import org.activiti.engine.delegate.DelegateExecution;
import org.activiti.engine.delegate.JavaDelegate;

public class BuscarCliente implements JavaDelegate {

  @Override
  public void execute(DelegateExecution ejecucion) throws Exception { // TODO Auto-generated method stub
		int codCliente = Integer.parseInt(((String) ejecucion.getVariable("IDCliente")));
		System.out.println("Buscando cliente " + codCliente);
		ServicioClientes servicio = new ServicioClientes();
		boolean encontrado = servicio.buscarCliente(codCliente);
		// a�adir el resultado al motor.
		System.out.println("Encontrado " + encontrado); 
		ejecucion.setVariable("encontrado", encontrado); 
	}
}