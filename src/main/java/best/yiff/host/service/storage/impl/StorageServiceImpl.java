/**
 * 
 */
package best.yiff.host.service.storage.impl;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.web.multipart.MultipartFile;

import com.amazonaws.services.s3.AmazonS3;

import best.yiff.host.service.storage.StorageService;
import best.yiff.host.service.storage.StorageServiceException;

/**
 * @author DistastefulBannock
 *
 */
@Service
public class StorageServiceImpl implements StorageService {
	
	@Autowired
	private AmazonS3 amazonS3;
	
	@Override
	public String store(MultipartFile uploaded) throws StorageServiceException {
		// TODO Auto-generated method stub
		return null;
	}

	@Override
	public String load(String token) throws StorageServiceException {
		// TODO Auto-generated method stub
		return null;
	}

}
