import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;

import org.activiti.engine.delegate.DelegateTask;
import org.activiti.engine.delegate.TaskListener;

public class ComprobarStockYReservar implements TaskListener {

	@Override
	public void notify(DelegateTask tareaDelegada) {
		Connection conn = Conexion.abrirConexion();
		if (conn != null) {
			String SQLPedidos = "SELECT * FROM lineapedidos;";

			try {
				PreparedStatement statementPedidos = conn.prepareStatement(SQLPedidos);
				ResultSet resultPedidos = statementPedidos.executeQuery();

				while (resultPedidos.next()) {
					int idArticulo = resultPedidos.getInt("Articulos_idArticulos");
					int cantidadPedido = resultPedidos.getInt("Cantidad");

					String SQLArticulos = "SELECT * FROM articulos WHERE idArticulos=?;";
					PreparedStatement statementArticulos = conn.prepareStatement(SQLArticulos);
					statementArticulos.setInt(1, idArticulo);
					ResultSet resultArticulos = statementPedidos.executeQuery();

					if (resultArticulos.next()) {
						int stockArticulo = resultArticulos.getInt("Stock");
						int reservadoArticulo = resultArticulos.getInt("Reservado");

						if ((stockArticulo - reservadoArticulo) >= cantidadPedido) {
							String SQLReserva = "UPDATE articulos SET Reservado=? WHERE idArticulos=?;";
							PreparedStatement statementReserva = conn.prepareStatement(SQLArticulos);
							statementReserva.setInt(1, reservadoArticulo + cantidadPedido);
							statementReserva.setInt(2, idArticulo);
							statementReserva.executeQuery();
						} else {
							String SQLReserva = "UPDATE lineapedidos SET Cantidad=? WHERE Articulos_idArticulos=?;";
							PreparedStatement statementReserva = conn.prepareStatement(SQLArticulos);
							statementReserva.setInt(1, -1);
							statementReserva.setInt(2, idArticulo);
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
