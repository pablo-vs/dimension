package es.ucm.fdi.exceptions;

import java.lang.Throwable;

public class NotFoundException extends RuntimeException {

	static final long serialVersionUID = 1000L;
	
	public NotFoundException(String message, Throwable e) {
		super(message, e);
	}

	public NotFoundException(String message) {
		super(message);
	}
}
