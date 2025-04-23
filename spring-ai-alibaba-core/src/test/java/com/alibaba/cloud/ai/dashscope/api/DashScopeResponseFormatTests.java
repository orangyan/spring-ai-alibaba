
package com.alibaba.cloud.ai.dashscope.api;

import static org.junit.jupiter.api.Assertions.*;

import org.junit.jupiter.api.Test;

/**
 * Tests for DashScopeResponseFormat class functionality
 *
 * @author yuluo
 * @author <a href="mailto:yuluo08290126@gmail.com">yuluo</a>
 * @author brianxiadong
 * @since 1.0.0-M2
 */
class DashScopeResponseFormatTests {

	@Test
	void testTextType() {
		// Test creating a response format with TEXT type
		DashScopeResponseFormat format = new DashScopeResponseFormat(DashScopeResponseFormat.Type.TEXT);

		// Verify properties
		assertNotNull(format, "Response format should be created");
		assertEquals(DashScopeResponseFormat.Type.TEXT, format.getType(), "Type should be TEXT");
		assertEquals("{\"type\":\"text\"}", format.toString(), "String representation should match");
	}

	@Test
	void testJsonObjectType() {
		// Test creating a response format with JSON_OBJECT type
		DashScopeResponseFormat format = new DashScopeResponseFormat(DashScopeResponseFormat.Type.JSON_OBJECT);

		// Verify properties
		assertNotNull(format, "Response format should be created");
		assertEquals(DashScopeResponseFormat.Type.JSON_OBJECT, format.getType(), "Type should be JSON_OBJECT");
		assertEquals("{\"type\":\"json_object\"}", format.toString(), "String representation should match");
	}

	@Test
	void testBuilder() {
		// Test using the builder to create a response format
		DashScopeResponseFormat textFormat = DashScopeResponseFormat.builder()
			.type(DashScopeResponseFormat.Type.TEXT)
			.build();

		DashScopeResponseFormat jsonFormat = DashScopeResponseFormat.builder()
			.type(DashScopeResponseFormat.Type.JSON_OBJECT)
			.build();

		// Verify properties
		assertEquals(DashScopeResponseFormat.Type.TEXT, textFormat.getType(), "Text format type should be TEXT");
		assertEquals(DashScopeResponseFormat.Type.JSON_OBJECT, jsonFormat.getType(),
				"JSON format type should be JSON_OBJECT");
	}

	@Test
	void testEqualsAndHashCode() {
		// Test equals and hashCode methods
		DashScopeResponseFormat format1 = new DashScopeResponseFormat(DashScopeResponseFormat.Type.TEXT);
		DashScopeResponseFormat format2 = new DashScopeResponseFormat(DashScopeResponseFormat.Type.TEXT);
		DashScopeResponseFormat format3 = new DashScopeResponseFormat(DashScopeResponseFormat.Type.JSON_OBJECT);

		// Test equals
		assertTrue(format1.equals(format2), "Equal formats should be equal");
		assertFalse(format1.equals(format3), "Different formats should not be equal");
		assertFalse(format1.equals(null), "Format should not equal null");
		assertFalse(format1.equals("string"), "Format should not equal different type");

		// Test hashCode
		assertEquals(format1.hashCode(), format2.hashCode(), "Equal formats should have same hash code");
		assertNotEquals(format1.hashCode(), format3.hashCode(), "Different formats should have different hash codes");
	}

	@Test
	void testSetType() {
		// Test setting the type after creation
		DashScopeResponseFormat format = new DashScopeResponseFormat(DashScopeResponseFormat.Type.TEXT);
		format.setType(DashScopeResponseFormat.Type.JSON_OBJECT);

		// Verify type was changed
		assertEquals(DashScopeResponseFormat.Type.JSON_OBJECT, format.getType(),
				"Type should be changed to JSON_OBJECT");
	}

}
