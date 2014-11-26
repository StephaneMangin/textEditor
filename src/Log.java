import java.io.IOException;
import java.sql.Time;
import java.text.DateFormat;
import java.util.logging.ConsoleHandler;
import java.util.logging.FileHandler;
import java.util.logging.Formatter;
import java.util.logging.Handler;
import java.util.logging.Level;
import java.util.logging.Logger;	 
import java.util.Date;
import java.util.logging.LogRecord;


public class Log {

	private Logger log;
	private Object obj;

	 
	public class MyFormatter extends Formatter {
	 
	    @Override
	    public String format(LogRecord record) {
	        return new Date(record.getMillis()) + " "
	        		+ record.getSourceClassName() + "::"
	                + record.getMessage()+"\n";
	    }
	 
	}
	protected Log(Object obj) {
		this.obj = obj;
		log = Logger.getLogger("ACO_TP." + getClassName());
		Handler fh = null;
		Handler ch = new ConsoleHandler();
		try {
			fh = new FileHandler("ACO_TP.log");
		} catch (SecurityException | IOException e) {
			e.printStackTrace();
		}
		fh.setFormatter(new MyFormatter());
		ch.setFormatter(new MyFormatter());
		log.setLevel(Level.FINE);
		log.addHandler(ch);
		log.addHandler(fh);
	}
	
	private String getClassName() {
		return obj.getClass().getName();
	}

	private String getMethodName() {
		return Thread.currentThread().getStackTrace()[4].getMethodName().toString();
	}
	
	public void log(Level level, String msg) {
		System.out.println(level.getName() + " " + msg);
		log.logp(level, getClassName(), getMethodName(), msg);
	}
	
	public void severe(String msg) {
		log(Level.SEVERE, msg);
	}

	public void warning(String msg) {
		log(Level.WARNING, msg);
	}

	public void info(String msg) {
		log(Level.INFO, msg);
	}

	public void config(String msg) {
		log(Level.CONFIG, msg);
	}

	public void fine(String msg) {
		log(Level.FINE, msg);
	}

	public void finer(String msg) {
		log(Level.FINER, msg);
	}

	public void finest(String msg) {
		log(Level.FINEST, msg);
	}
}
