package mail.the.news.service.provider;

/**
 * Builder which provide a session object
 */
public interface SessionBuilder<T> {
	public T buildSession();
}
