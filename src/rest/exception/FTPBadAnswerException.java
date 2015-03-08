package rest.exception;

/**
 * @author cachera - falez
 * Exception throwed when the FTP Server return an error
 */
public class FTPBadAnswerException extends Exception {

	private static final long serialVersionUID = 1L;
	private int code;

	public FTPBadAnswerException(int code) {
		this.code = code;
	}
	
	public int getCode() {
		return this.code;
	}

}
