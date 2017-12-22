import com.mysql.jdbc.Statement;
import com.mysql.jdbc.exceptions.jdbc4.MySQLIntegrityConstraintViolationException;

import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.time.LocalDate;
import java.util.Calendar;
import java.sql.Connection;
import java.sql.Date;

public class ServicioLineaPedidos {
	
 public int insertarLineaPedido(int idCliente, int idArticulo, int cantidad) {
		Connection connection = Conexion.abrirConexion();
		// Comprobar si existe la cabecera de pedido
		String buscarCabecera = "SELECT * FROM cabecerapedidos WHERE Clientes_idClientes = ?;";
		int res = -1;
		try {
			PreparedStatement ps = connection.prepareStatement(buscarCabecera);
			ps.setInt(1, idCliente);
			ResultSet rs = ps.executeQuery();
			
			int idCabecera = -1;
			if (rs.next()) {
				// Si existe, asociar la lineapedido a esa cabecera
				idCabecera = rs.getInt("idCabeceraPedidos");
			} else {
				// Si no, crear la cabeceraPedido
				String insertarCabecera = "INSERT INTO cabecerapedidos (FechaPedido, Clientes_idClientes) VALUES (?,?);";
				PreparedStatement preparedStatement = connection.prepareStatement(insertarCabecera, Statement.RETURN_GENERATED_KEYS);
				preparedStatement.setDate(1, new Date(Calendar.getInstance().getTimeInMillis()));
				preparedStatement.setInt(2, idCliente);
				preparedStatement.executeUpdate();
				ResultSet claves = preparedStatement.getGeneratedKeys();
				claves.next();
				idCabecera = claves.getInt(1);
			}
			
			String insertarLineaPedidos = "INSERT INTO lineapedidos (Cantidad, CabeceraPedidos_idCabeceraPedidos, Articulos_idArticulos) VALUES (?,?,?);";
			PreparedStatement preparedStatement = connection.prepareStatement(insertarLineaPedidos, Statement.RETURN_GENERATED_KEYS);
			preparedStatement.setInt(1, cantidad);
			preparedStatement.setInt(2, idCabecera);
			preparedStatement.setInt(3, idArticulo);
			preparedStatement.executeUpdate();
			ResultSet claves = preparedStatement.getGeneratedKeys();
			claves.next();
			res = claves.getInt(1);
		} catch (MySQLIntegrityConstraintViolationException e) {
			Log.write("Se intenta añadir un artículo que no existe");
		} catch (SQLException e) {
			Log.write(e);
		}
		
		Conexion.cerrarConexion();
		return res;
	}
}
