/**
 * 
 */
package best.yiff.host.repo;

import java.util.List;

import org.springframework.data.jpa.repository.JpaRepository;

import best.yiff.host.model.ModelDomain;

/**
 * @author DistastefulBannock
 *
 */
public interface RepoDomains extends JpaRepository<ModelDomain, Long> {
	
//	List<String> findDomainBy();
	
}
