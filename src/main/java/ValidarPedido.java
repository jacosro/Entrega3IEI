import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;

import org.activiti.engine.delegate.DelegateExecution;
import org.activiti.engine.delegate.DelegateTask;
import org.activiti.engine.delegate.JavaDelegate;
import org.activiti.engine.delegate.TaskListener;

import java.util.List;

import java.sql.Connection;
import java.sql.PreparedStatement;

public class ValidarPedido implements JavaDelegate {

	@Override
	public void execute(DelegateExecution ejecucion) {
		boolean valido = false;

		Connection conn = Conexion.abrirConexion();
		if (conn != null) {
			String SQLPedidos = "SELECT * FROM lineapedidos;";

			try {
				PreparedStatement statementPedidos = conn.prepareStatement(SQLPedidos, ResultSet.HOLD_CURSORS_OVER_COMMIT);
				ResultSet resultPedidos = statementPedidos.executeQuery();

				List<Integer> idArticulos = new ArrayList<Integer>();
				
				while(resultPedidos.next()) {
					idArticulos.add(resultPedidos.getInt("Articulos_idArticulos"));
				}

				for (Integer id : idArticulos) {
					String SQLArticulos = "SELECT * FROM articulos WHERE idArticulos=?;";
					PreparedStatement statementArticulos = conn.prepareStatement(SQLArticulos);
					statementArticulos.setInt(1, id);
					ResultSet resultArticulos = statementPedidos.executeQuery();
					
					if (!resultArticulos.next()) {
						String SQLBorrarPedido = "DELETE FROM lineaspedido WHERE Articulos_idArticulos=?;";
						PreparedStatement statementBorrar = conn.prepareStatement(SQLBorrarPedido);
						statementArticulos.setInt(1, id);
						statementArticulos.executeQuery();
					}					
				}
			} catch (SQLException e) {
				Log.write(e);
			}

			try {
				PreparedStatement statementPedidos = conn.prepareStatement(SQLPedidos);
				ResultSet resultPedidos = statementPedidos.executeQuery();

				if (resultPedidos.next()) {
					valido = true;
				}

				Conexion.cerrarConexion();

			} catch (SQLException e) {
				Log.write(e);
			}

		}
		Conexion.cerrarConexion();

		ejecucion.setVariable("pedidoValido", valido);
	}
}
