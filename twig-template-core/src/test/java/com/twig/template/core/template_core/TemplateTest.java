package com.twig.template.core.template_core;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertTrue;

import org.junit.Test;

import com.twig.template.core.exceptions.TwigTemplateException;
import com.twig.template.core.processor.InputProcessor;
import com.twig.template.core.util.FileProcess;

public class TemplateTest {
	
	private static final String TEST_TEMPLATE_FILE = "resources/testfiles/test-templates.json";
	
	@Test
	public void testDiscountTemplateWithLoop() throws TwigTemplateException{
		String expected = "You have to pay 500$. But you are eligible for the following discounts: \n 1)For discount type 1 you will get 5 discount\n 2)For discount type 2 you will get 25 % discount.";
		String jsonInput = FileProcess.readFile("resources/testfiles/discount-template-input.json");
		String templateDef = FileProcess.readFile(TEST_TEMPLATE_FILE);

		InputProcessor processor = new InputProcessor(templateDef);
		String nlg = processor.getNlg(jsonInput);
		System.out.println("Got:" + nlg);
		//assertTrue(expected.equals(nlg));
	}
	
	@Test
	public void testResultTemplateWithMap() throws TwigTemplateException {
		String jsonInput = FileProcess.readFile("resources/testfiles/test_result_template_input.json");
		String templateDef = FileProcess.readFile(TEST_TEMPLATE_FILE);
		InputProcessor processor = new InputProcessor(templateDef);
		String nlg = processor.getNlg(jsonInput);
		System.out.println("Got:" + nlg);

	}

}
