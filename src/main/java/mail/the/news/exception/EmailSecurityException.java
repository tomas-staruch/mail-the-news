package mail.the.news.exception;

public class EmailSecurityException extends RuntimeException {

	private static final long serialVersionUID = 1523834682439934111L;

	public EmailSecurityException(String msg) {
		super(msg);
	}
	
	public EmailSecurityException(Exception e) {
		super(e);
	}
	
	public EmailSecurityException(String msg, Exception e) {
		super(msg, e);
	}
}