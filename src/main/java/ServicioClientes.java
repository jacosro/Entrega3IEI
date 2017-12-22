import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;
import java.util.Date;

public class ServicioClientes {
	public Cliente buscarCliente(int codCliente) {
		Cliente encontrado = null;
		Log.write("Abriendo conexion en servicio cliente");
		Connection conn = Conexion.abrirConexion();
		Log.write("Conexion es null? " + (conn == null));
		if (conn != null) {
			Log.write("Entrado");
			String SQL = "SELECT * FROM clientes where idClientes = ?;";
			try {
				PreparedStatement statement = conn.prepareStatement(SQL);
				statement.setInt(1, codCliente);
				ResultSet result = statement.executeQuery();
				if (result.next())
					encontrado = new Cliente(result.getInt(1), result.getString(2), result.getString(3), result.getDate(4),
							result.getString(5), result.getString(6), result.getString(7));
				else
					encontrado = null;
				Conexion.cerrarConexion();
				return (encontrado);
			} catch (SQLException e) {
				Log.write(e);
			}
		}
		Conexion.cerrarConexion();
		return encontrado;
	}

	// A�ade un cliente y devuelve la clave autogenerada del mismo
	public int insertarCliente(String nombre, String direccion, String Email, java.util.Date FechaAlta, String TarjetaCredito,
			String Emisor) {
		int clave = 0;
		Connection conn = Conexion.abrirConexion();
		Log.write("Conexion en insertarcliente: " + conn);
		if (conn != null) {
			String SQL = "INSERT INTO clientes (Nombre, Direccion, FechaAlta, NumTarjeta, Emisor, Correoelectronico) VALUES (?,?,?,?,?,?);";
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
				Log.write(e);
			} finally {
				Conexion.cerrarConexion();
			}
		}
		return clave;
	}
}
