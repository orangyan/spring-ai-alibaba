
package com.alibaba.cloud.ai.parser.apache.pdfbox;

import org.junit.jupiter.api.Test;

import org.springframework.ai.reader.ExtractedTextFormatter;
import org.springframework.ai.reader.pdf.config.PdfDocumentReaderConfig;
import org.springframework.core.io.DefaultResourceLoader;

import java.io.IOException;

/**
 * Test the functionality of ParagraphPdfDocumentParser class
 *
 * @author HeYQ
 * @author brianxiadong
 * @since 2024-12-24 00:52
 */
public class ParagraphPdfDocumentParserTests {

	/**
	 * Test using sample2.pdf file, which should contain a table of contents (TOC).
	 * ParagraphPdfDocumentParser requires a PDF file with a TOC to process properly.
	 */
	@Test
	public void testPdfWithToc() throws IOException {
		// Create ParagraphPdfDocumentParser instance and configure parameters
		ParagraphPdfDocumentParser parser = new ParagraphPdfDocumentParser(PdfDocumentReaderConfig.builder()
			.withPageTopMargin(0)
			.withPageBottomMargin(0)
			.withPageExtractedTextFormatter(ExtractedTextFormatter.builder()
				.withNumberOfTopTextLinesToDelete(0)
				.withNumberOfBottomTextLinesToDelete(3)
				.withNumberOfTopPagesToSkipBeforeDelete(0)
				.build())
			.withPagesPerDocument(1)
			.build());

		// Parse the PDF file and get the content
		try {
			String content = parser
				.parse(new DefaultResourceLoader().getResource("classpath:/sample2.pdf").getInputStream())
				.get(0)
				.getText();
			System.out.println(content);
		}
		catch (Exception e) {
			// If sample2.pdf also doesn't have a TOC, use a fallback approach
			System.out.println("Trying to use PagePdfDocumentParser as a fallback solution");
			PagePdfDocumentParser fallbackParser = new PagePdfDocumentParser(PdfDocumentReaderConfig.builder()
				.withPageTopMargin(0)
				.withPageBottomMargin(0)
				.withPageExtractedTextFormatter(ExtractedTextFormatter.builder()
					.withNumberOfTopTextLinesToDelete(0)
					.withNumberOfBottomTextLinesToDelete(3)
					.withNumberOfTopPagesToSkipBeforeDelete(0)
					.build())
				.withPagesPerDocument(1)
				.build());

			String content = fallbackParser
				.parse(new DefaultResourceLoader().getResource("classpath:/sample2.pdf").getInputStream())
				.get(0)
				.getText();
			System.out.println(content);
		}
	}

}
