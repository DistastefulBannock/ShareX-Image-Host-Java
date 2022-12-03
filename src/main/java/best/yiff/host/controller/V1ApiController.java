package best.yiff.host.controller;

import java.util.Optional;

import org.apache.tika.Tika;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.multipart.MultipartFile;

import best.yiff.host.model.ModelAccount;
import best.yiff.host.repo.RepoAccounts;
import best.yiff.host.service.storage.StorageService;

/**
 * @author DistastefulBannock
 *
 */
@Controller
@RequestMapping(value = "/api/v1")
public class V1ApiController {
	
}
