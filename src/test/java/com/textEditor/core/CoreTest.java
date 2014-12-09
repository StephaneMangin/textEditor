/**
 * 
 */
package com.textEditor.core;

import static org.junit.Assert.*;

import org.junit.After;
import org.junit.AfterClass;
import org.junit.Before;
import org.junit.BeforeClass;
import org.junit.Test;

/**
 * @author blacknight
 *
 */
public class CoreTest {

	
	protected Core core;
	protected Selection position;
	/**
	 * @throws java.lang.Exception
	 */
	@Before
	public void setUp() throws Exception {
		core = new Core();
		position = new Selection();
		position.setContent("abcdefgh");
		position.setPrevContent("123456");
		core.insert(position);
	}

	/**
	 * @throws java.lang.Exception
	 */
	@After
	public void tearDown() throws Exception {
		core.reset();
		position = new Selection();
		position.setContent("abcdefgh");
		position.setPrevContent("123456");
		core.insert(position);
	}

//	@Test
//	public void testRecording() {
//		fail("Not yet implemented");
//	}
//
//	@Test
//	public void testPlaying() {
//		fail("Not yet implemented");
//	}
//
//	@Test
//	public void testStop() {
//		fail("Not yet implemented");
//	}
//
//	@Test
//	public void testUndo() {
//		fail("Not yet implemented");
//	}
//
//	@Test
//	public void testRedo() {
//		fail("Not yet implemented");
//	}
//
//	@Test
//	public void testInsert() {
//		fail("Not yet implemented");
//	}
//
//	@Test
//	public void testReplace() {
//		fail("Not yet implemented");
//	}
//
//	@Test
//	public void testDelete() {
//		fail("Not yet implemented");
//	}

	@Test
	public void testCopy() {
	position.setEnd(5);
	core.copy(position);
	assertEquals("fgh", core.getBuffer());
	}

	@Test
	public void testCut() {
		position.setEnd(5);
		core.cut(position);
		assertEquals("fgh", core.getBuffer());
	}

	@Test
	public void testPaste() {
		position.setEnd(5);
		core.cut(position);
		System.out.println(core.getClipboard());
		assertEquals("abcde", core.getClipboard());
		position.setStart(2);
		core.paste(position);
		assertEquals("abcdefgabcdefgh", core.getBuffer().toString());
	}

//	@Test
//	public void testCurrentPosition() {
//		fail("Not yet implemented");
//	}
	
}