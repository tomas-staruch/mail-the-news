package mail.the.news.domain;

import javax.persistence.Column;
import javax.persistence.MappedSuperclass;
import javax.validation.constraints.NotNull;
import javax.validation.constraints.Size;

import com.fasterxml.jackson.annotation.JsonIgnore;
import com.fasterxml.jackson.annotation.JsonProperty;

@MappedSuperclass
abstract class SecurePersistentEntity extends PersistentEntity {
	
	@NotNull(message="error.password.notnull")
	@Size(min=4,max=255,message="error.password.size")
	@Column(nullable=false)
	private String password;
	
	SecurePersistentEntity() { }
	
	protected SecurePersistentEntity(String password) { 
		this.setPassword(password);
	}

	@JsonProperty
	public void setPassword(String password) {
		this.password = password;
	}

	@JsonIgnore
	public String getPassword() {
		return this.password;
	}
}
