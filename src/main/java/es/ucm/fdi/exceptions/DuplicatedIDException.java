package es.ucm.fdi.exceptions;

import java.lang.Throwable;

public class DuplicatedIDException extends RuntimeException {

	static final long serialVersionUID = 1001L;
	
	public DuplicatedIDException(String message, Throwable e) {
		super(message, e);
	}

	public DuplicatedIDException(String message) {
		super(message);
	}
}
