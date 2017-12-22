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
			int idCabecera = -1;
			
			try {
				PreparedStatement statementPedidos = conn.prepareStatement(SQLPedidos);
				ResultSet resultPedidos = statementPedidos.executeQuery();

				List<Integer> idArticulos = new ArrayList<Integer>();
				
				while(resultPedidos.next()) {
					idArticulos.add(resultPedidos.getInt("Articulos_idArticulos"));
					idCabecera = (resultPedidos.getInt("CabeceraPedidos_idCabeceraPedidos"));
					Log.write(String.valueOf(idCabecera));
				}

				for (Integer id : idArticulos) {
					String SQLArticulos = "SELECT * FROM articulos WHERE idArticulos=?;";
					PreparedStatement statementArticulos = conn.prepareStatement(SQLArticulos);
					statementArticulos.setInt(1, id);
					ResultSet resultArticulos = statementPedidos.executeQuery();
					
					if (!resultArticulos.next()) {
						String SQLBorrarPedido = "DELETE FROM lineapedidos WHERE Articulos_idArticulos=?;";
						PreparedStatement statementBorrar = conn.prepareStatement(SQLBorrarPedido);
						statementBorrar.setInt(1, id);
						statementBorrar.executeUpdate();
					}					
				}
			} catch (SQLException e) {
				Log.write(e);
			}

			try {
				PreparedStatement statementPedidos = conn.prepareStatement(SQLPedidos);
				ResultSet resultPedidosOtravez = statementPedidos.executeQuery();

				if (resultPedidosOtravez.next()) {
					valido = true;
				} else {
					String SQLBorrarCabecera = "DELETE FROM cabecerapedidos WHERE idCabeceraPedidos = ?;";
					PreparedStatement statementBorrar = conn.prepareStatement(SQLBorrarCabecera);
					statementBorrar.setInt(1, idCabecera);
					statementBorrar.executeUpdate();				
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
