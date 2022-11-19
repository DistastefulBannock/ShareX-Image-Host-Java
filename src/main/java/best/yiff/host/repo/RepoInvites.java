/**
 * 
 */
package best.yiff.host.repo;

import java.util.ArrayList;

import org.springframework.data.jpa.repository.JpaRepository;

import best.yiff.host.model.ModelInvite;

/**
 * @author DistastefulBannock
 *
 */
public interface RepoInvites extends JpaRepository<ModelInvite, Long> {
	
	/**
	 * Finds and returns the invites for a specific user
	 * @param ownerId The id of the person you're checking
	 * @return An arraylist of their invites
	 */
	ArrayList<ModelInvite> findByOwner(long owner);
	
	/**
	 * Finds an invite
	 * @param invite The invite to find
	 * @return The invite, or null if none is found
	 */
	ModelInvite findByInvite(String invite);
	
}
