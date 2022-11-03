package hu.neak.puphax.syncronizer.controller;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.CrossOrigin;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import hu.neak.puphax.syncronizer.neak.service.PortalPuphax;

@RestController
@CrossOrigin
@RequestMapping("/rest/publish")
public class PublishController {
	private final PortalPuphax portalService;
	
	
	@Autowired
	public PublishController(PortalPuphax portalService) {
		this.portalService = portalService;
	}



	@GetMapping("/actual/version")
	public ResponseEntity<String> publishVersion() {
		return ResponseEntity.ok(portalService.publishVersion());
	}
}
