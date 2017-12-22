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
				
				Log.write(articulos.toString());
				
				for (Integer[] i : articulos) {
					String SQLArticulos = "SELECT * FROM articulos WHERE idArticulos=?;";
					PreparedStatement statementArticulos = conn.prepareStatement(SQLArticulos);
					statementArticulos.setInt(1, i[0]);
					ResultSet resultArticulos = statementArticulos.executeQuery();

					if (resultArticulos.next()) {
						int stockArticulo = resultArticulos.getInt(4);
						int reservadoArticulo = resultArticulos.getInt(5);
						
						Log.write(String.valueOf(stockArticulo - reservadoArticulo));

						if ((stockArticulo - reservadoArticulo) >= i[1]) {
							Log.write("OK");
							String SQLReserva = "UPDATE articulos SET Reservado=" + (reservadoArticulo + i[1]) + " WHERE idArticulos=?;";
							PreparedStatement statementReserva = conn.prepareStatement(SQLReserva);
							statementReserva.setInt(1, i[0]);
							statementReserva.executeUpdate();
						} else {
							Log.write("NO OK");
							String SQLReserva = "UPDATE lineapedidos SET Cantidad=" + (i[1]*-1) + " WHERE Articulos_idArticulos=?;";
							PreparedStatement statementReserva = conn.prepareStatement(SQLReserva);
							statementReserva.setInt(1, i[0]);
							statementReserva.executeUpdate();
						}
					} // fin if externo

				} // fin while
				
				Log.write("Tó bien");

			} catch (SQLException e) {
				Log.write(e);
			}

			Conexion.cerrarConexion();
		}
	}
}
