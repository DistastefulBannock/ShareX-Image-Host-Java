/**
 * 
 */
package best.yiff.host.model;

import java.util.ArrayList;
import java.util.Arrays;
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
@Table(name = "USERS")
public class ModelAccount {
	
	public ModelAccount() {
		
	}
	
	/**
	 * @param username The username for the account
	 * @param email The email for the account
	 * @param password The password for the account
	 * @param banned Whether or not the account is banned
	 */
	public ModelAccount(String username, String email, String password, boolean banned) {
		this.username = username;
		this.email = email;
		this.password = password;
		this.banned = banned;
		this.roles = new ArrayList<>(Arrays.asList("ROLE_USER"));
		this.domains = new ArrayList<>(Arrays.asList("yiff.best"));
	}

	@Id
	@GeneratedValue
	@Column(name = "id", unique = true)
	private long id;

	@Column(name = "username", unique = true)
	private String username;

	@Column(name = "email", unique = true)
	private String email;

	@Column(name = "password")
	private String password;
	
	@Column(name = "uploadKey", unique = true)
	private String uploadKey;
	
	@Column(name = "banned")
	private boolean banned;
	
	@Column(name = "disabled")
	private boolean disabled;
	
	@Column(name = "roles")
	private ArrayList<String> roles;
	
	@Column(name = "invitedBy")
	private long invitedBy;
	
	@Column(name = "inviteCount")
	private long inviteCount;
	
	@Column(name = "lastActive")
	private long lastActive;
	
	@Column(name = "domains")
	private ArrayList<String> domains;
	
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
	 * @return the username
	 */
	public String getUsername() {
		return username;
	}

	/**
	 * @param username the username to set
	 */
	public void setUsername(String username) {
		this.username = username;
	}

	/**
	 * @return the email
	 */
	public String getEmail() {
		return email;
	}

	/**
	 * @param email the email to set
	 */
	public void setEmail(String email) {
		this.email = email;
	}

	/**
	 * @return the password
	 */
	public String getPassword() {
		return password;
	}

	/**
	 * @param password the password to set
	 */
	public void setPassword(String password) {
		this.password = password;
	}
	
	/**
	 * @return the uploadKey
	 */
	public String getUploadKey() {
		return uploadKey;
	}
	
	/**
	 * @param uploadKey the uploadKey to set
	 */
	public void setUploadKey(String uploadKey) {
		this.uploadKey = uploadKey;
	}

	/**
	 * @return the banned
	 */
	public boolean isBanned() {
		return banned;
	}

	/**
	 * @param banned the banned to set
	 */
	public void setBanned(boolean banned) {
		this.banned = banned;
	}

	/**
	 * @return the disabled
	 */
	public boolean isDisabled() {
		return disabled;
	}

	/**
	 * @param disabled the disabled to set
	 */
	public void setDisabled(boolean disabled) {
		this.disabled = disabled;
	}

	/**
	 * @return the roles
	 */
	public ArrayList<String> getRoles() {
		return roles;
	}

	/**
	 * @param roles the roles to set
	 */
	public void setRoles(ArrayList<String> roles) {
		this.roles = roles;
	}
	
	/**
	 * @return the invitedBy
	 */
	public long getInvitedBy() {
		return invitedBy;
	}
	
	/**
	 * @param invitedBy the invitedBy to set
	 */
	public void setInvitedBy(long invitedBy) {
		this.invitedBy = invitedBy;
	}
	
	/**
	 * @return the inviteCount
	 */
	public long getInviteCount() {
		return inviteCount;
	}
	
	/**
	 * @param inviteCount the inviteCount to set
	 */
	public void setInviteCount(long inviteCount) {
		this.inviteCount = inviteCount;
	}
	
	/**
	 * @return the lastActive
	 */
	public long getLastActive() {
		return lastActive;
	}
	
	/**
	 * @param lastActive the lastActive to set
	 */
	public void setLastActive(long lastActive) {
		this.lastActive = lastActive;
	}
	
	/**
	 * @return the domains
	 */
	public ArrayList<String> getDomains() {
		return domains;
	}
	
	/**
	 * @param domains the domains to set
	 */
	public void setDomains(ArrayList<String> domains) {
		this.domains = domains;
	}

	@Override
	public String toString() {
		return "ModelAccount [id=" + id + ", username=" + username + ", email=" + email + ", password=********"
				+ ", banned=" + banned + ", disabled=" + disabled + ", roles=" + roles + ", invitedBy=" + invitedBy
				+ ", inviteCount=" + inviteCount + ", lastActive=" + lastActive + "]";
	}
	
}
