import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.List;

import org.activiti.engine.delegate.DelegateExecution;
import org.activiti.engine.delegate.DelegateTask;
import org.activiti.engine.delegate.JavaDelegate;
import org.activiti.engine.delegate.TaskListener;

public class ComprobarStockYReservar implements JavaDelegate {

	@Override
	public void execute(DelegateExecution ejecucion) {
		Connection conn = Conexion.abrirConexion();
		if (conn != null) {
			String SQLPedidos = "SELECT * FROM lineapedidos;";

			try {
				PreparedStatement statementPedidos = conn.prepareStatement(SQLPedidos);
				ResultSet resultPedidos = statementPedidos.executeQuery();

				List<Integer[]> articulos = new ArrayList<Integer[]>();
				
				while (resultPedidos.next()) {
					int idArticulo = resultPedidos.getInt("Articulos_idArticulos");
					int cantidadPedido = resultPedidos.getInt("Cantidad");
					
					articulos.add(new Integer[] { idArticulo, cantidadPedido });
				}
				
				for (Integer[] i : articulos) {
					String SQLArticulos = "SELECT * FROM articulos WHERE idArticulos=?;";
					PreparedStatement statementArticulos = conn.prepareStatement(SQLArticulos);
					statementArticulos.setInt(1, i[0]);
					ResultSet resultArticulos = statementPedidos.executeQuery();

					if (resultArticulos.next()) {
						int stockArticulo = resultArticulos.getInt(3);
						int reservadoArticulo = resultArticulos.getInt(4);

						if ((stockArticulo - reservadoArticulo) >= i[1]) {
							String SQLReserva = "UPDATE articulos SET Reservado=" + (reservadoArticulo + i[1]) + " WHERE idArticulos=?;";
							PreparedStatement statementReserva = conn.prepareStatement(SQLArticulos);
							statementReserva.setInt(1, i[0]);
							statementReserva.executeQuery();
						} else {
							String SQLReserva = "UPDATE lineapedidos SET Cantidad=" + -1 + " WHERE Articulos_idArticulos=?;";
							PreparedStatement statementReserva = conn.prepareStatement(SQLArticulos);
							statementReserva.setInt(1, i[0]);
							statementReserva.executeQuery();
						}
					} // fin if externo

				} // fin while

			} catch (SQLException e) {
				Log.write(e);
			}

			Conexion.cerrarConexion();
		}
	}
}
