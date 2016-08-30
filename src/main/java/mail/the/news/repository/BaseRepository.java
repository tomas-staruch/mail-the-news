package mail.the.news.repository;

import java.util.Set;

public interface BaseRepository<T> {

    public Set<T> findByEmail(String email);

	public T findById(String email, Long id);
}
