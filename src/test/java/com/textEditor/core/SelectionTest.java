package com.textEditor.core;

import static org.junit.Assert.*;
import net.sf.json.JSONObject;

import org.junit.After;
import org.junit.Before;
import org.junit.Test;

public class SelectionTest {

	protected Selection position;
	protected Integer start = 0;
	protected Integer end = 0;
	protected String content = "abcdefgh";
	protected String prevContent = "123456";
	
	
	@Before
	public void setUp() throws Exception {
		position = new Selection(start, end, content, prevContent);
	}

	@After
	public void tearDown() throws Exception {
		position = new Selection(0, 0, "abcdefgh", "123456");
	}

	@Test
	public void testInit() {
		assertEquals((long) 0, (long)position.getStart());
		assertEquals((long)0, (long)position.getEnd());
		assertEquals("abcdefgh", position.getContent());
		assertEquals("123456", position.getPrevContent());
	}

	@Test
	public void testSet() {
		position.setStart(1);
		assertEquals((long) 1, (long)position.getStart());
		position.setEnd(1);
		assertEquals((long) 1, (long)position.getEnd());
		position.setContent("abcdefgh");
		assertEquals("abcdefgh", position.getContent());
		position.setPrevContent("123456");
		assertEquals("123456", position.getPrevContent());
	}
	
	@Test
	public void testPreviousContent() {
		position.previous();
		assertEquals("123456", position.getContent());
		assertEquals("", position.getPrevContent());
	}
	
	@Test
	public void testJson() {
		JSONObject json = new JSONObject();
		json.accumulate("start", 10);
		json.accumulate("end", 15);
		json.accumulate("content", "zyx");
		json.accumulate("prevContent", "321");
		position.set(json);
		assertEquals((long) 10, (long)position.getStart());
		assertEquals((long) 15, (long)position.getEnd());
		assertEquals("zyx", position.getContent());
		assertEquals("321", position.getPrevContent());
		assertEquals(json, position.toJson());
		
	}

}
