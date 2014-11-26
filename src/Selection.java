import java.util.Observable;

/**
 * @(#) Selection.java
 */

public class Selection extends Observable {
	
	private Integer start;
	private Integer end;
	private String str;

	public Selection(Integer start, Integer end, String str) {
		setStart(start);
		setEnd(end);
		setContent(str);
	}

	public String getContent() {
		return str;
	}

	public void setContent(String content) {
		this.str = content;
		setChanged();
		notifyObservers();
	}
	
	public Integer getStart() {
		return start;
	}

	public void setStart(Integer start) {
		this.start = start;
		setChanged();
		notifyObservers();
	}

	public Integer getEnd() {
		return end;
	}

	public void setEnd(Integer end) {
		this.end = end;
		setChanged();
		notifyObservers();
	}

	public void jump() {
		start = start + str.length();
		reset();
		setChanged();
		notifyObservers();
	}

	public void reset() {
		end = 0;
		str = "";
		setChanged();
		notifyObservers();
	}
	
	public String toString() {
		return start + "->" + end + "='" + str + "'";
		
	}
}
