package mail.the.news.domain;

import java.util.Date;

import jakarta.persistence.Column;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.persistence.MappedSuperclass;
import jakarta.persistence.PrePersist;
import jakarta.persistence.PreUpdate;
import jakarta.persistence.TableGenerator;
import jakarta.persistence.Temporal;
import jakarta.persistence.TemporalType;

@MappedSuperclass
abstract class PersistentEntity {
	@Id
	@GeneratedValue(strategy=GenerationType.TABLE, generator="primary_table_generator")
	@TableGenerator(name="primary_table_generator", table="sequences")
	private Long id;
	
    @Temporal(TemporalType.TIMESTAMP)
    @Column(nullable = false)
    private Date created;

    @Temporal(TemporalType.TIMESTAMP)
    @Column(nullable = true)
    private Date updated;
    
    public Long getId() {
		return id;
	}
    
    public Date getCreated() {
		return created;
	}
    
    public Date getUpdated() {
		return updated;
	}
    
    @PrePersist
    protected void onCreate() {
      created = new Date();
    }

    @PreUpdate
    protected void onUpdate() {
      updated = new Date();
    }
}
