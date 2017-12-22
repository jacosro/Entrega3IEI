import java.sql.Date;

public class Cliente {
	private int id;
	private String nombre;
	private String direccion;
	private Date fechaAlta;
	private String email;
	private String tarjetaCredito;
	private String emisor;
	
	public Cliente(int id, String nombre, String direccion, Date fechaAlta, String tarjetaCredito, String emisor, String email) {
		super();
		this.id = id;
		this.nombre = nombre;
		this.direccion = direccion;
		this.fechaAlta = fechaAlta;
		this.email = email;
		this.tarjetaCredito = tarjetaCredito;
		this.emisor = emisor;
	}
	
	
	public String getDireccion() {
		return direccion;
	}


	public void setDireccion(String direccion) {
		this.direccion = direccion;
	}


	public int getId() {
		return id;
	}
	public void setId(int id) {
		this.id = id;
	}
	public String getNombre() {
		return nombre;
	}
	public void setNombre(String nombre) {
		this.nombre = nombre;
	}
	public Date getFechaAlta() {
		return fechaAlta;
	}
	public void setFechaAlta(Date fechaAlta) {
		this.fechaAlta = fechaAlta;
	}
	public String getEmail() {
		return email;
	}
	public void setEmail(String email) {
		this.email = email;
	}
	public String getTarjetaCredito() {
		return tarjetaCredito;
	}
	public void setTarjetaCredito(String tarjetaCredito) {
		this.tarjetaCredito = tarjetaCredito;
	}
	public String getEmisor() {
		return emisor;
	}
	public void setEmisor(String emisor) {
		this.emisor = emisor;
	}
}
