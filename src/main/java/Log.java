import java.io.File;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.OutputStream;
import java.io.PrintWriter;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Calendar;
import java.util.List;
import java.util.stream.Stream;

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
	
	public static void write(String string) {
		try {
			PrintWriter pw = new PrintWriter(new FileOutputStream(FILE, true));
			pw.println(getTime() + string);
			pw.flush();
			pw.close();
		} catch (FileNotFoundException e) {
			e.printStackTrace();
		}
	}
	
	public static void write(Exception e) {
		write(e.toString() + 
				System.lineSeparator() + 
				String.join(System.lineSeparator(), mapToString(e.getStackTrace())));
	}
	
	
	private static String[] mapToString(Object[] a) {
		List<String> res = new ArrayList<String>(a.length);
		for (Object o : a) {
			res.add(o.toString());
		}
		return res.toArray(new String[a.length]);
	}
}
