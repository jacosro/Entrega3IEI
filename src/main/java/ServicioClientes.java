import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;
import java.util.Date;

public class ServicioClientes {
	public boolean buscarCliente(int codCliente) {
		boolean encontrado = false;
		Connection conn = Conexion.abrirConexion();
		if (conn != null) {
			String SQL = "SELECT idClientes FROM clientes where idClientes = ?";
			try {
				PreparedStatement statement = conn.prepareStatement(SQL);
				statement.setInt(1, codCliente);
				ResultSet result = statement.executeQuery();
				if (result.next())
					encontrado = true;
				else
					encontrado = false;
				Conexion.cerrarConexion();
				return (encontrado);
			} catch (SQLException e) {
				Log.write(e.toString());
			}
		}
		Conexion.cerrarConexion();
		return false;
	}

	// A�ade un cliente y devuelve la clave autogenerada del mismo
	public int insertarCliente(String nombre, String direccion, String Email, java.util.Date FechaAlta, String TarjetaCredito,
			String Emisor) {
		int clave = 0;
		Connection conn = Conexion.abrirConexion();
		if (conn != null) {
			String SQL = "INSERT INTO clientes (Nombre, Direccion, FechaAlta, NumTarjeta, Emisor, Correoelectronico) VALUES (?,?,?,?,?,?)";
			try {
				PreparedStatement statement = conn.prepareStatement(SQL, Statement.RETURN_GENERATED_KEYS);
				statement.setString(1, nombre);
				statement.setString(2, direccion);
				// convertir java.util.Date a java.sql.Date
				java.sql.Date sqlDate = new java.sql.Date(FechaAlta.getTime());
				statement.setDate(3, sqlDate);
				statement.setString(4, TarjetaCredito);
				statement.setString(5, Emisor);
				statement.setString(6, Email);
				statement.executeUpdate();
				ResultSet claves = statement.getGeneratedKeys();
				claves.next();
				clave = claves.getInt(1);
				Log.write("Cliente insertado");
				return clave;
			} catch (SQLException e) {
				Log.write(e.toString());
			} finally {
				Conexion.cerrarConexion();
			}
		}
		return clave;
	}
}
