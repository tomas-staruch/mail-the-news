package mail.the.news.domain;

import javax.persistence.DiscriminatorColumn;
import javax.persistence.Entity;
import javax.persistence.FetchType;
import javax.persistence.Inheritance;
import javax.persistence.InheritanceType;
import javax.persistence.JoinColumn;
import javax.persistence.ManyToOne;
import javax.persistence.Table;

@Entity
@Inheritance(strategy=InheritanceType.SINGLE_TABLE)
@DiscriminatorColumn(name="config_type")
@Table(name="email_service_configurations")
public abstract class EmailServiceConfiguration extends PersistentEntity {
	
	@ManyToOne(optional=false, fetch=FetchType.LAZY)
	@JoinColumn(name="user_id")
	private User user;
	
	EmailServiceConfiguration() { }

	public User getUser() {
		return this.user;
	}

	public void setUser(User user) {
		this.user = user;
	}
}
