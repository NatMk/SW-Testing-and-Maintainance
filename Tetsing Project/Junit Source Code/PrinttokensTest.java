package JUnit4Printtokens2;

import org.junit.After;
import org.junit.Assert;
import org.junit.Before;
import org.junit.Test;

import static org.junit.Assert.*;

import java.io.*;

public class PrinttokensTest {
	private Printtokens2 obj;
	private File testFile;
	private String nameFile = "aa.tmp";
	private String[] content = new String[]{"first2\n", "second\n", "123"};
	private final ByteArrayOutputStream outContent = new ByteArrayOutputStream();
	private final ByteArrayOutputStream errContent = new ByteArrayOutputStream();
	@Before
	public void setUp() {
		obj = new Printtokens2();
		testFile = new File(nameFile);
		System.setOut(new PrintStream(outContent));
		System.setErr(new PrintStream(errContent));
	}
	@After
	public void shutDown() {
		System.setOut(System.out);
		System.setErr(System.err);
	}

	@Test
	public void test_open_character_stream() {
		try (BufferedReader bw = obj.open_character_stream(nameFile)) {
			Assert.assertNotNull(bw);
			assertEquals(bw.readLine(), "@edwe2236725");
		} catch (IOException e) {
			fail(e.getMessage());
		}
		BufferedReader bw = obj.open_character_stream("undef");
		Assert.assertNull(bw);
		assertEquals("The file undef doesn't exists\n", outContent.toString());
	}
	@Test
	public void test_open_token_stream() {
		try (BufferedReader bw = obj.open_token_stream(nameFile)) {
			Assert.assertNotNull(bw);
			assertEquals(bw.readLine(), "@edwe2236725");
		} catch (IOException e) {
			fail(e.getMessage());
		}
		BufferedReader bw = obj.open_token_stream("undef");
		Assert.assertNull(bw);
		assertEquals("The file undef doesn't exists\n", outContent.toString());
	}
	@Test
	public void test_is_spec_symbol() {
		char[] chars = new char[]{'(', ')', '[', ']', '\'', '`', ','};
		for (char c : chars) {
			Assert.assertTrue(Printtokens2.is_spec_symbol(c));
		}
		Assert.assertFalse(Printtokens2.is_spec_symbol('c'));
	}
	@Test
	public void test_print_spec_symbol() {
		Printtokens2.print_spec_symbol("(");
		assertEquals("lparen.\n", outContent.toString());
		outContent.reset();
		Printtokens2.print_spec_symbol(")");
		assertEquals("rparen.\n", outContent.toString());
		outContent.reset();
		Printtokens2.print_spec_symbol("[");
		assertEquals("lsquare.\n", outContent.toString());
		outContent.reset();
		Printtokens2.print_spec_symbol("]");
		assertEquals("rsquare.\n", outContent.toString());
		outContent.reset();
		Printtokens2.print_spec_symbol("'");
		assertEquals("quote.\n", outContent.toString());
		outContent.reset();
		Printtokens2.print_spec_symbol("`");
		assertEquals("bquote.\n", outContent.toString());
		outContent.reset();
		Printtokens2.print_spec_symbol("`");
		assertEquals("bquote.\n", outContent.toString());
		outContent.reset();
		Printtokens2.print_spec_symbol("a");
		assertEquals("", outContent.toString());
		Printtokens2.print_spec_symbol("1");
		assertEquals("", outContent.toString());
	}
	@Test
	public void test_is_identifier() {
		assertTrue(Printtokens2.is_identifier("asd"));
		assertTrue(Printtokens2.is_identifier("a1sd"));
		assertTrue(Printtokens2.is_identifier("as2d"));
		assertTrue(Printtokens2.is_identifier("wer566666" + "ddf"));
		assertFalse(Printtokens2.is_identifier("1qwqwq"));
		assertFalse(Printtokens2.is_identifier("a_"));
		assertFalse(Printtokens2.is_identifier(String.valueOf(17)));
		assertFalse(Printtokens2.is_identifier("*"));
	}
	@Test
	public void test_is_str_constant() {
		assertTrue(Printtokens2.is_str_constant("\"asd\""));
		assertTrue(Printtokens2.is_str_constant("\"a1sd\""));
		assertTrue(Printtokens2.is_str_constant("\"as2d\""));
		assertFalse(Printtokens2.is_str_constant("\"wer56666\"6" + "ddf"));
		assertFalse(Printtokens2.is_str_constant("\"1qwqw\"q"));
		assertFalse(Printtokens2.is_str_constant("\"a_"));
		assertFalse(Printtokens2.is_str_constant(String.valueOf(17)));
		assertTrue(Printtokens2.is_str_constant("\"*\""));
	}
	@Test
	public void test_is_num_constant() {
		try {
		assertTrue(Printtokens2.is_num_constant("15"));
		assertFalse(Printtokens2.is_num_constant("a1sd"));
		assertFalse(Printtokens2.is_num_constant("\"as2d"));
		assertFalse(Printtokens2.is_num_constant("\"wer56666\"6" + "ddf"));
		assertFalse(Printtokens2.is_num_constant("\"1qwqw\"q"));
		assertFalse(Printtokens2.is_num_constant("\"a_"));
		assertTrue(Printtokens2.is_num_constant(String.valueOf(17)));
		assertFalse(Printtokens2.is_num_constant("\"*\""));
		} catch (Exception e) {
			fail("Fault detected: " + e);
		}
	}
	@Test
	public void test_is_char_constant() {
		assertTrue(Printtokens2.is_char_constant("#a"));
		assertFalse(Printtokens2.is_char_constant("a1sd"));
		assertFalse(Printtokens2.is_char_constant("#as2d"));
		assertFalse(Printtokens2.is_char_constant("\"wer56666\"6" + "ddf"));
		assertFalse(Printtokens2.is_char_constant("#1qwqw\"q"));
		assertFalse(Printtokens2.is_char_constant("#a_"));
		assertTrue(Printtokens2.is_char_constant("#g"));
	}
	@Test
	public void test_is_keyword() {
		String[] chars = new String[]{"and", "or", "if", "xor", "lambda", "=>"};
		for (String c : chars) {
			Assert.assertTrue(Printtokens2.is_keyword(c));
		}
		Assert.assertFalse(Printtokens2.is_keyword("dd"));
	}
	@Test
	public void test_is_comment() {
		assertTrue(Printtokens2.is_comment(";#a"));
		assertFalse(Printtokens2.is_comment("a1sd"));
		assertFalse(Printtokens2.is_comment("#as2d"));
		assertTrue(Printtokens2.is_comment(";\"wer56666\"6" + "ddf"));
		assertFalse(Printtokens2.is_comment("#1qwqw\"q"));
		assertTrue(Printtokens2.is_comment(";a_"));
		assertTrue(Printtokens2.is_comment(";g"));
	}
	@Test
	public void test_print_token() {
		obj.print_token("@edwe2236725");
		assertEquals("error,\"@edwe2236725\".\n", outContent.toString());
		outContent.reset();
		obj.print_token("xor");
		assertEquals("keyword,\"xor\".\n", outContent.toString());
		outContent.reset();
		obj.print_token("(");
		assertEquals("lparen.\n", outContent.toString());
		outContent.reset();
		obj.print_token("ret3");
		assertEquals("identifier,\"ret3\".\n", outContent.toString());
		outContent.reset();
		obj.print_token("45");
		assertEquals("numeric,45.\n", outContent.toString());
		outContent.reset();
		obj.print_token("\"lparen.\\n\"");
		assertEquals("string,\"lparen.\\n\".\n", outContent.toString());
		outContent.reset();
		obj.print_token("#a");
		assertEquals("character,\"a\".\n", outContent.toString());
		outContent.reset();
	}
	@Test
	public void test_token_type() {
		assertEquals(0, obj.token_type("@edwe2236725"));
		assertEquals(obj.token_type("xor"), 1);
		assertEquals(obj.token_type("("), 2);
		assertEquals(obj.token_type("ret3"), 3);
		assertEquals(obj.token_type("45"), 41);
		assertEquals(obj.token_type("\"lparen.\\n\""), 42);
		assertEquals(obj.token_type("#a"), 43);
		assertEquals(obj.token_type(";a"), 5);
	}
	@Test
	public void test_is_token_end() {
		assertTrue(obj.is_token_end(0, '\n'));
		assertTrue(obj.is_token_end(1, '\r'));
		assertTrue(obj.is_token_end(-1, -1));
		assertTrue(obj.is_token_end(1, '"'));
		assertTrue(obj.is_token_end(2, '\t'));
		assertTrue(obj.is_token_end(1, 59));
		assertTrue(obj.is_token_end(1, ' '));
		assertTrue(obj.is_token_end(3, '('));
		assertFalse(obj.is_token_end(1, 't'));
		assertFalse(obj.is_token_end(1, '('));
	}
	@Test
	public void test_get_token() {

		StringReader sr = new StringReader("first");
		BufferedReader bfr = new BufferedReader(sr);	
		StringBuilder sb = new StringBuilder();
		sb.append("first");
		assertEquals(sb.toString(), obj.get_token(bfr).toString());

		sr = new StringReader("");
		bfr = new BufferedReader(sr);	
		sb.setLength(0);
		assertEquals(null,obj.get_token(bfr));

		sr = new StringReader(" ");
		bfr = new BufferedReader(sr);	
		assertEquals(null, obj.get_token(bfr));

		sr = new StringReader("\n");
		bfr = new BufferedReader(sr);	
		assertEquals(null, obj.get_token(bfr));

		sr = new StringReader("\r");
		bfr = new BufferedReader(sr);	
		assertEquals(null, obj.get_token(bfr));

		sr = new StringReader(" b");
		bfr = new BufferedReader(sr);	
		sb.append("b");
		assertEquals(sb.toString(), obj.get_token(bfr).toString());

		sr = new StringReader("b");
		bfr = new BufferedReader(sr);	
		sb.setLength(0);
		sb.append("b");
		assertEquals(sb.toString(), obj.get_token(bfr).toString());

		sr = new StringReader(",");
		bfr = new BufferedReader(sr);	
		sb.setLength(0);
		sb.append(",");
		assertEquals(sb.toString(), obj.get_token(bfr).toString());

		sr = new StringReader("\"b\"");
		bfr = new BufferedReader(sr);	
		sb.setLength(0);
		sb.append("\"b\"");
		assertEquals(sb.toString(), obj.get_token(bfr).toString());

		sr = new StringReader(";comment");
		bfr = new BufferedReader(sr);	
		sb.setLength(0);
		sb.append(";comment");
		assertEquals(sb.toString(), obj.get_token(bfr).toString());

		sr = new StringReader("first,");
		bfr = new BufferedReader(sr);	
		sb.setLength(0);
		sb.append("first");
		assertEquals(sb.toString(), obj.get_token(bfr).toString());

		sr = new StringReader("first;");
		bfr = new BufferedReader(sr);	
		sb.setLength(0);
		sb.append("first");
		assertEquals(sb.toString(), obj.get_token(bfr).toString());
	}

}
