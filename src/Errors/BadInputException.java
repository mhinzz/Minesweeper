package Errors;

public class BadInputException extends Error {
	public BadInputException(String errorMessage) {
		super(errorMessage);
	}
}
