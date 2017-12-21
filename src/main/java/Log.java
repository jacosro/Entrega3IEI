import java.io.File;
import java.io.FileNotFoundException;
import java.io.PrintWriter;
import java.util.Calendar;

public class Log {

	private static final File FILE = new File("log.txt");
	
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
	
	
	public static void write(String string) {
		try {
			PrintWriter pw = new PrintWriter(FILE);
			pw.println(getTime() + string);
			pw.flush();
			pw.close();
		} catch (FileNotFoundException e) {
			e.printStackTrace();
		}
	}
}
