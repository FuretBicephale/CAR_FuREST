package rest.exception;

public class FTPBadAnswerException extends Exception {

	private static final long serialVersionUID = 1L;
	private int code;
	
	public FTPBadAnswerException(int code) {
		this.code = code;
	}

}
