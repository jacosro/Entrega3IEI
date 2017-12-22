import java.io.File;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.OutputStream;
import java.io.PrintWriter;
import java.util.Calendar;

public class Log {

	private static final File FILE = new File("C:\\Users\\Administrador\\Desktop\\log.txt");
	
	private static String getTime() {
		Calendar calendar = Calendar.getInstance();
		int h = calendar.get(Calendar.HOUR);
		int m = calendar.get(Calendar.MINUTE);
		int s = calendar.get(Calendar.SECOND);
		String hh = h < 10 ? "0" + h : String.valueOf(h);
		String mm = m < 10 ? "0" + m : String.valueOf(m);
		String ss = s < 10 ? "0" + s : String.valueOf(s);
		return "[" + hh + ":" + mm + ":" + ss + "] ";
	}
	
	private static String getTrace() {
		StackTraceElement[] ste = Thread.currentThread().getStackTrace();
		return ste[3].getClassName() + "::" + ste[3].getMethodName() + " line " + ste[3].getLineNumber();
	}
	
	
	public static void write(String string) {
		try {
			PrintWriter pw = new PrintWriter(new FileOutputStream(FILE, true));
			pw.println(getTime() + string + " at " + getTrace());
			pw.flush();
			pw.close();
		} catch (FileNotFoundException e) {
			e.printStackTrace();
		}
	}
}
