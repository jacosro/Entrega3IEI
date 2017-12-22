import java.sql.SQLException;

import org.activiti.engine.delegate.DelegateExecution;
import org.activiti.engine.delegate.DelegateTask;
import org.activiti.engine.delegate.TaskListener;

public class IntroducirCodigoArticulo implements TaskListener {
	
	@Override
	public void notify(DelegateTask delegate) {
		DelegateExecution execution = delegate.getExecution();
		int idArticulo = Integer.parseInt(String.valueOf(execution.getVariable("IDCodigo")));
		int cantidad = Integer.parseInt(String.valueOf(execution.getVariable("IDCantidad")));
		int idCliente = Integer.parseInt(String.valueOf(execution.getVariable("IDCliente")));
		ServicioLineaPedidos slp = new ServicioLineaPedidos();
		int idLineaPedido = slp.insertarLineaPedido(idCliente, idArticulo, cantidad);
		execution.setVariable("IDLineaPedido", idLineaPedido);
	}
	

}
