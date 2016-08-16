package mail.the.news.service.exception;

/**
 * Wrapper for exceptions
 */
public class EmailServiceException extends Exception {

	private static final long serialVersionUID = 1523834682439934111L;

	public EmailServiceException(String msg) {
		super(msg);
	}
	
	public EmailServiceException(Exception e) {
		super(e);
	}
	
	public EmailServiceException(String msg, Exception e) {
		super(msg, e);
	}
}
