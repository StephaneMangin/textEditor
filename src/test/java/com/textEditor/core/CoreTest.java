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
		position = new Selection(0, 0, "abcdefgh", "");
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
		assertTrue(core.isUndo());
		core.undo();
		assertEquals("", core.getBuffer());
	}

	@Test
	public void testRedo() {
		assertEquals("abcdefgh", core.getBuffer());
		assertTrue(core.isUndo());
		core.undo();
		assertEquals("", core.getBuffer());
		assertEquals("abcdefgh", position.getContent());
		assertEquals("", position.getPrevContent());
		assertTrue(core.isRedo());
		core.redo();
		assertEquals("abcdefgh", core.getBuffer());
		assertEquals("abcdefgh", position.getContent());
		assertEquals("", position.getPrevContent());
	}

	@Test
	public void testInsert() {
		assertEquals("abcdefgh", core.getBuffer());
		position.setStart(3);
		position.setEnd(3);
		core.insert(position);
		assertEquals("abcabcdefghdefgh", core.getBuffer());
		assertEquals("", position.getPrevContent());
	}

	@Test
	public void testDelete() {
		assertEquals("abcdefgh", core.getBuffer());
		position.setStart(0);
		position.setEnd(3);
		core.delete(position);
		assertEquals("defgh", core.getBuffer());
		assertEquals("abc", position.getPrevContent());
	}

	@Test
	public void testCopy() {
		assertEquals("abcdefgh", core.getBuffer());
		position.setEnd(5);
		core.copy(position);
		assertEquals("abcdefgh", core.getBuffer());
		assertEquals("", position.getPrevContent());
	}

	@Test
	public void testCut() {
		assertEquals("abcdefgh", core.getBuffer());
		position.setEnd(5);
		core.cut(position);     
		assertEquals("fgh", core.getBuffer());
		assertEquals("abcde", position.getPrevContent());
	}

	@Test
	public void testPaste() {
		assertEquals("abcdefgh", core.getBuffer());
		position.setEnd(5);
		core.cut(position);
		assertEquals("fgh", core.getBuffer());
		assertEquals("abcde", core.getClipboard());
		assertEquals("abcdefgh", position.getContent());
		assertEquals("abcde", position.getPrevContent());
		position.setStart(2);
		position.setEnd(2);
		core.paste(position);
		assertEquals("fgabcdeh", core.getBuffer().toString());
		assertEquals("", position.getPrevContent());
	}

	@Test
	public void testCurrentPosition() {
		assertEquals("abcdefgh", core.getBuffer());
		assertEquals((int) 8, core.getCurrentPosition());
	}
	
}