
package com.alibaba.cloud.ai.toolcalling.regex;

import org.junit.jupiter.api.Test;
import static org.junit.jupiter.api.Assertions.*;

import java.util.ArrayList;
import java.util.List;
import java.util.regex.Pattern;

/**
 * Test cases for {@link RegexService}
 *
 * @author zhangshenghang
 */
public class RegexServiceTest {

	private final RegexService regexService = new RegexService();

	@Test
	public void testApplyWithValidPattern() {
		// Test basic regex pattern matching
		String content = "Hello 123 World 456";
		Pattern pattern = Pattern.compile("\\d+");
		RegexService.RegexRequest request = new RegexService.RegexRequest(content, pattern, 0);

		@SuppressWarnings("unchecked")
		List<String> result = (List<String>) regexService.apply(request);

		assertEquals(2, result.size());
		assertEquals("123", result.get(0));
		assertEquals("456", result.get(1));
	}

	@Test
	public void testApplyWithGroups() {
		// Test group extraction
		String content = "name: John, age: 30";
		Pattern pattern = Pattern.compile("name: (\\w+), age: (\\d+)");
		RegexService.RegexRequest request = new RegexService.RegexRequest(content, pattern, 1);

		@SuppressWarnings("unchecked")
		List<String> result = (List<String>) regexService.apply(request);

		assertEquals(1, result.size());
		assertEquals("John", result.get(0));
	}

	@Test
	public void testApplyWithNoMatches() {
		// Test case with no matches
		String content = "Hello World";
		Pattern pattern = Pattern.compile("\\d+");
		RegexService.RegexRequest request = new RegexService.RegexRequest(content, pattern, 0);

		@SuppressWarnings("unchecked")
		List<String> result = (List<String>) regexService.apply(request);

		assertTrue(result.isEmpty());
	}

	@Test
	public void testFindAllWithNullPattern() {
		// Test with null Pattern
		List<String> result = RegexUtils.findAll(null, "content", 0, new ArrayList<>());
		assertNull(result);
	}

	@Test
	public void testFindAllWithNullContent() {
		// Test with null Content
		Pattern pattern = Pattern.compile("\\w+");
		List<String> result = RegexUtils.findAll(pattern, null, 0, new ArrayList<>());
		assertNull(result);
	}

	@Test
	public void testFindAllWithMultipleMatches() {
		// Test multiple matches
		String content = "abc123def456ghi789";
		Pattern pattern = Pattern.compile("([a-z]+)(\\d+)");
		List<String> result = RegexUtils.findAll(pattern, content, 1, new ArrayList<>());

		assertEquals(3, result.size());
		assertEquals("abc", result.get(0));
		assertEquals("def", result.get(1));
		assertEquals("ghi", result.get(2));
	}

}
