package test;

import static org.junit.jupiter.api.Assertions.*;

import org.junit.Test;

import control.ExperimentWriter;

public class ExperimentWriterTest {
	
	@Test
	public void testConvert() {
		String in = "\"test, a";
		String out = ExperimentWriter.toCSVDelimited(in);
		String expected = "\"\"\"test, a\",";
		assertEquals(expected,out,"Input not properly delimited");
	}
	
	@Test
	public void testConvertNoQuotes() {
		String in = "test, a";
		String out = ExperimentWriter.toCSVDelimited(in);
		String expected = "\"test, a\",";
		assertEquals(expected,out,"Input not properly delimited");
	}
	
	@Test
	public void testConvertNoCommas() {
		String in = "\"test a";
		String out = ExperimentWriter.toCSVDelimited(in);
		String expected = "\"\"\"test a\",";
		assertEquals(expected,out,"Input not properly delimited");
	}
	
	@Test
	public void testConvertNoCommaNoQuote() {
		String in = "test a";
		String out = ExperimentWriter.toCSVDelimited(in);
		String expected = in+',';
		assertEquals(expected,out,"Input without commas or quotes had content changed");
	}
}
