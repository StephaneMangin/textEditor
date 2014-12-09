/**
 * 
 */
package com.textEditor.core;

import static org.junit.Assert.*;

import org.junit.Before;
import org.junit.Test;

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

	@Test
	public void testUndo() {
		position.setStart(3);
		position.setEnd(3);
		core.insert(position);
		assertTrue(core.isUndo());
		core.undo();
		assertEquals("abcdefgh", core.getBuffer());
	}

	@Test
	public void testRedo() {
		position.setStart(3);
		position.setEnd(3);
		core.insert(position);
		assertEquals("abcabcdefghdefgh", core.getBuffer());
		position.setStart(0);
		position.setEnd(1);
		core.delete(position);
		assertEquals("bcabcdefghdefgh", core.getBuffer());
		assertTrue(core.isUndo());
		core.undo();
		assertEquals("abcabcdefghdefgh", core.getBuffer());
		assertTrue(core.isUndo());
		core.undo();
		assertEquals("abcdefgh", core.getBuffer());
		assertTrue(core.isRedo());
		core.redo();
		assertEquals("abcabcdefghdefgh", core.getBuffer());
	}

	@Test
	public void testInsert() {
		position.setStart(3);
		position.setEnd(3);
		core.insert(position);
		assertEquals("abcabcdefghdefgh", core.getBuffer());
		assertEquals("123456", position.getPrevContent());
	}
	
	public void testDelete() {
		position.setStart(0);
		position.setEnd(3);
		core.delete(position);
		assertEquals("defgh", core.getBuffer());
		assertEquals("abc", position.getPrevContent());
	}

	@Test
	public void testCopy() {
		position.setEnd(5);
		core.copy(position);
		assertEquals("abcdefghabcdefgh", core.getBuffer());
		assertEquals("123456", position.getPrevContent());
	}

	@Test
	public void testCut() {
		position.setEnd(5);
		core.cut(position);     
		assertEquals("fgh", core.getBuffer());
		assertEquals("123456", position.getPrevContent());
	}

	@Test
	public void testPaste() {
		position.setEnd(5);
		core.cut(position);
		assertEquals("abcde", core.getClipboard());
		assertEquals("123456", position.getPrevContent());
		position.setStart(2);
		position.setEnd(2);
		core.paste(position);
		assertEquals("fgabcdeh", core.getBuffer().toString());
		assertEquals("abcdefgh", position.getPrevContent());
	}

//	@Test
//	public void testCurrentPosition() {
//		fail("Not yet implemented");
//	}
	
}