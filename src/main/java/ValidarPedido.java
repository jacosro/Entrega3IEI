import java.sql.ResultSet;
import java.sql.SQLException;

import org.activiti.engine.delegate.DelegateExecution;
import org.activiti.engine.delegate.DelegateTask;
import org.activiti.engine.delegate.JavaDelegate;
import org.activiti.engine.delegate.TaskListener;

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
				PreparedStatement statementPedidos = conn.prepareStatement(SQLPedidos);
				ResultSet resultPedidos = statementPedidos.executeQuery();

				while(resultPedidos.next()) {
					int lineapedido = resultPedidos.getInt("Articulos_idArticulos");

					String SQLArticulos = "SELECT * FROM articulos WHERE idArticulos=?;";
					PreparedStatement statementArticulos = conn.prepareStatement(SQLArticulos);
					statementArticulos.setInt(1, lineapedido);
					ResultSet resultArticulos = statementPedidos.executeQuery();

					if (!resultArticulos.next()) {
						String SQLBorrarPedido = "DELETE FROM lineaspedido WHERE Articulos_idArticulos=?;";
						PreparedStatement statementBorrar = conn.prepareStatement(SQLBorrarPedido);
						statementArticulos.setInt(1, lineapedido);
						statementArticulos.executeQuery();
					}
				}

			} catch (SQLException e) {
				e.printStackTrace();
			}

			try {
				PreparedStatement statementPedidos = conn.prepareStatement(SQLPedidos);
				ResultSet resultPedidos = statementPedidos.executeQuery();

				if (resultPedidos.next()) {
					valido = true;
				}

				Conexion.cerrarConexion();

			} catch (SQLException e) {
				e.printStackTrace();
			}

		}
		Conexion.cerrarConexion();

		ejecucion.setVariable("pedidoValido", valido);
	}
}
