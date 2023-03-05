package org.wickedsource.docxstamper;

import org.docx4j.openpackaging.exceptions.Docx4JException;
import org.docx4j.openpackaging.packages.WordprocessingMLPackage;
import org.docx4j.wml.P;
import org.junit.jupiter.api.Test;
import org.wickedsource.docxstamper.util.ParagraphWrapper;

import java.io.IOException;
import java.io.InputStream;

import static org.junit.jupiter.api.Assertions.assertEquals;

public class CustomExpressionFunctionTest extends AbstractDocx4jTest {
	@Test
	public void test() throws Docx4JException, IOException {
		Name context = new Name("Homer Simpson");
		InputStream template = getClass().getResourceAsStream("CustomExpressionFunction.docx");
		DocxStamperConfiguration config = new DocxStamperConfiguration()
				.exposeInterfaceToExpressionLanguage(UppercaseFunction.class, new UppercaseFunctionImpl());
		WordprocessingMLPackage document = stampAndLoad(template, context, config);
		P nameParagraph = (P) document.getMainDocumentPart().getContent().get(2);
		assertEquals(
				"In this paragraph, a custom expression function is used to uppercase a String: HOMER SIMPSON.",
				new ParagraphWrapper(nameParagraph).getText());
		P commentedParagraph = (P) document.getMainDocumentPart().getContent().get(3);
		assertEquals(
				"To test that custom functions work together with comment expressions, we toggle visibility of this paragraph with a comment expression.",
				new ParagraphWrapper(commentedParagraph).getText());
	}

	public interface UppercaseFunction {
		String toUppercase(String string);
	}

	public record Name(String name) {
	}

	public static class UppercaseFunctionImpl implements UppercaseFunction {
		@Override
		public String toUppercase(String string) {
			return string.toUpperCase();
		}
	}
}
