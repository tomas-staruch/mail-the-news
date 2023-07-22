package mail.the.news.domain;

import jakarta.persistence.Column;
import jakarta.persistence.MappedSuperclass;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Size;

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
