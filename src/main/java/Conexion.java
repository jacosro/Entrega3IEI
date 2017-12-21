import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.SQLException;

public class Conexion {
	private static Connection conexion = null;

	public static Connection abrirConexion() {
		try {
			Class.forName("com.mysql.jdbc.Driver");
		} catch (ClassNotFoundException e) {
			// TODO Auto-generated catch block e.printStackTrace();
		}
		try {
			conexion = DriverManager.getConnection("jdbc:mysql://localhost:3306/Entrega3Procesos", "root", "root");
		} catch (SQLException e) {
			// TODO Auto-generated catch block
			Log.write(e.toString());
		}
		return conexion;
	}

	public static void cerrarConexion() {
		if (conexion != null) {
			try {
				// cerrar la BD
				conexion.close();
			} catch (SQLException e) {
				System.out.println("Error al cerrar la conexión con la BD");
			}
		}
	}
}