/**
 * 
 */
package best.yiff.host.model;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.Id;
import javax.persistence.Table;

/**
 * @author DistastefulBannock
 *
 */
@Entity
@Table(name = "DOMAINS")
public class ModelDomain {
	
	public ModelDomain() {}
	
	@Id
	@GeneratedValue
	@Column(name = "id", unique = true)
	private long id;
	
	@Column(name = "domain", unique = true)
	private String domain;
	
	@Column(name = "wildcard")
	private boolean wildcard;
	
	/**
	 * @return the id
	 */
	public long getId() {
		return id;
	}
	
	/**
	 * @param id the id to set
	 */
	public void setId(long id) {
		this.id = id;
	}
	
	/**
	 * @return the domain
	 */
	public String getDomain() {
		return domain;
	}
	
	/**
	 * @param domain the domain to set
	 */
	public void setDomain(String domain) {
		this.domain = domain;
	}
	
	/**
	 * @return the wildcard
	 */
	public boolean isWildcard() {
		return wildcard;
	}
	
	/**
	 * @param wildcard the wildcard to set
	 */
	public void setWildcard(boolean wildcard) {
		this.wildcard = wildcard;
	}
	
}
