package com.twig.template.core.exceptions;

public class TwigTemplateException extends Exception {

	private static final long serialVersionUID = 1L;
	
	public TwigTemplateException() {
	}

	public TwigTemplateException(String message) {
		super(message);
	}

	public TwigTemplateException(Throwable cause) {
		super(cause);
	}

	public TwigTemplateException(String message, Throwable cause) {
		super(message, cause);
	}

	public TwigTemplateException(String message, Throwable cause,
			boolean enableSuppression, boolean writableStackTrace) {
		super(message, cause, enableSuppression, writableStackTrace);
	}
}
