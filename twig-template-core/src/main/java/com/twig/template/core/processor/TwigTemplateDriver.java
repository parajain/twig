package com.twig.template.core.processor;

import com.twig.template.core.exceptions.TwigTemplateException;
import com.twig.template.core.util.FileProcess;

public class TwigTemplateDriver {

	private InputProcessor inputProcessor;

	public TwigTemplateDriver() {
		inputProcessor = new InputProcessor();
	}

	public TwigTemplateDriver(String template, boolean isFile) {
		if (isFile) {
			String templateDefinations = FileProcess.readFile(template);
			inputProcessor = new InputProcessor(templateDefinations);
		} else
			inputProcessor = new InputProcessor(template);
	}

	public TwigTemplateDriver(String templateDefinations) {
		inputProcessor = new InputProcessor(templateDefinations);
	}

	public String getNLGFromJsonString(String jsonStr) throws TwigTemplateException {
		return inputProcessor.getNlg(jsonStr);
	}
}