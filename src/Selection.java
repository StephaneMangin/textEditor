/**
 * @(#) Selection.java
 */

public class Selection
{
	
	private Integer start;
	
	private Integer length;
	
	private String content;

	public Selection(Integer start, Integer length, String content) {
		setStart(start);
		setLength(length);
		setContent(content);
	}

	public String getContent() {
		return content;
	}

	public void setContent(String content) {
		this.content = content;
	}
	
	public Integer getStart() {
		return start;
	}

	public void setStart(Integer start) {
		this.start = start;
	}

	public Integer getLength() {
		return length;
	}

	public void setLength(Integer length) {
		this.length = length;
	}

	public void jump() {
		start = start + content.length();
		reset();
	}

	public void reset() {
		length = 0;
		content = null;
	}
	
	public String toString() {
		return start + "->" + length + "='" + content + "'";
		
	}
}
