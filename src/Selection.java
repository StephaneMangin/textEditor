/**
 * @(#) Selection.java
 */

public class Selection {
	
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
	}
	
	public Integer getStart() {
		return start;
	}

	public void setStart(Integer start) {
		this.start = start;
	}

	public Integer getEnd() {
		return end;
	}

	public void setEnd(Integer end) {
		this.end = end;
	}

	public void jump() {
		start = start + str.length();
		reset();
	}

	public void reset() {
		end = 0;
		str = "";
	}
	
	public String toString() {
		return start + "->" + end + "='" + str + "'";
		
	}
}
