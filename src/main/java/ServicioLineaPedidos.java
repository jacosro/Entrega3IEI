import com.mysql.jdbc.Statement;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.time.LocalDate;
import java.util.Calendar;
import java.sql.Connection;
import java.sql.Date;

public class ServicioLineaPedidos {
	
 public int insertarLineaPedido(int idCliente, int idArticulo, int cantidad) throws SQLException {
		Connection connection = Conexion.abrirConexion();
		// Comprobar si existe la cabecera de pedido
		String buscarCabecera = "SELECT * FROM cabecerapedidos WHERE Clientes_idClientes = ?;";
		PreparedStatement ps = connection.prepareStatement(buscarCabecera);
		ps.setInt(1, idCliente);
		ResultSet rs = ps.executeQuery();

		int idCabecera = -1;
		if (rs.next()) {
			// Si existe, asociar la lineapedido a esa cabecera
			idCabecera = rs.getInt("idCabeceraPedidos");
		} else {
			// Si no, crear la cabeceraPedido
			String insertarCabecera = "INSERT INTO cabecerapedidos (FechaPedido, Clientes_idClientes) VALUES (?, ?);";
			PreparedStatement preparedStatement = connection.prepareStatement(insertarCabecera, Statement.RETURN_GENERATED_KEYS);
			preparedStatement.setInt(1, idCliente);
			preparedStatement.setDate(2, new Date(Calendar.getInstance().getTimeInMillis()));
			preparedStatement.executeUpdate(insertarCabecera);
			ResultSet claves = preparedStatement.getGeneratedKeys();
			claves.next();
			idCabecera = claves.getInt(1);
		}
		
		String insertarLineaPedidos = "INSERT INTO lineapedidos (Cantidad, CabeceraPedidos_idCabeceraPedidos, Articulos_idArticulos) VALUES (?,?,?);";
		PreparedStatement preparedStatement = connection.prepareStatement(insertarLineaPedidos, Statement.RETURN_GENERATED_KEYS);
		preparedStatement.setInt(1, cantidad);
		preparedStatement.setInt(2, idCabecera);
		preparedStatement.setInt(3, idArticulo);
		preparedStatement.executeUpdate(insertarLineaPedidos);
		ResultSet claves = preparedStatement.getGeneratedKeys();
		claves.next();
		int res = claves.getInt(1);
		
		Conexion.cerrarConexion();
		return res;
	}
}
