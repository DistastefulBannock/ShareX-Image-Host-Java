/**
 * 
 */
package best.yiff.host.model;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.Table;
import javax.persistence.Transient;

import org.springframework.beans.factory.annotation.Value;
import org.springframework.security.core.token.Sha512DigestUtils;

/**
 * @author DistastefulBannock
 *
 */
@Entity
@Table(name = "INVITES")
public class ModelInvite {
	
	/**
	 * 
	 */
	public ModelInvite() {
		
	}
	
	@Value(value = "${invite.salt}")
	@Transient
	private transient String inviteSalt;
	
	/**
	 * Creates a new invite for a user
	 * @param owner The person who owns the invite
	 */
	public ModelInvite(ModelAccount owner) {
		this.owner = owner.getId();
		this.invite = Sha512DigestUtils.shaHex(inviteSalt + "-" + owner.getId() + "-" + owner.getUsername() + "-" + System.currentTimeMillis() + "-" + System.nanoTime());
	}

	@Id
	@GeneratedValue
	@Column(name = "id", unique = true)
	private long id;

	@Column(name = "owner")
	private long owner;
	
	@Column(name = "invite", unique = true)
	private String invite;
	
	@Column(name = "redeemed")
	private boolean redeemed;
	
	@Column(name = "redeemer")
	private long redeemer;
	
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
	 * @return the ownerId
	 */
	public long getOwner() {
		return owner;
	}

	/**
	 * @param owner the owner to set
	 */
	public void setOwner(long owner) {
		this.owner = owner;
	}
	
	/**
	 * @return the invite
	 */
	public String getInvite() {
		return invite;
	}
	
	/**
	 * @return the redeemed
	 */
	public boolean isRedeemed() {
		return redeemed;
	}
	
	/**
	 * @param redeemed the redeemed to set
	 */
	public void setRedeemed(boolean redeemed) {
		this.redeemed = redeemed;
	}
	
	/**
	 * @return the redeemer
	 */
	public long getRedeemer() {
		return redeemer;
	}
	
	/**
	 * @param redeemer the redeemer to set
	 */
	public void setRedeemer(long redeemer) {
		this.redeemer = redeemer;
	}
	
	@Override
	public String toString() {
		return "ModelInvite [id=" + id + ", owner=" + owner + ", invite=" + invite + ", redeemed=" + redeemed + ", redeemer="
				+ redeemer + "]";
	}
	
}
