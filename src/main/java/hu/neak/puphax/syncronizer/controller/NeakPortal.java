package hu.neak.puphax.syncronizer.controller;

import hu.neak.puphax.syncronizer.model.dto.PuphaxTables;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.CrossOrigin;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import hu.neak.puphax.syncronizer.neak.service.PortalPuphax;

@RestController
@CrossOrigin
@RequestMapping("/rest/tor/nyilvantartas")
public class NeakPortal {
	private final PortalPuphax portalPuphax;

	@Autowired
	public NeakPortal(PortalPuphax portalPuphax) {
		this.portalPuphax = portalPuphax;
	}
	
	@GetMapping("/PUPHAX/{tableName}")
	public ResponseEntity<?> getTable(
			@PathVariable("tableName") PuphaxTables table,
			@RequestParam Integer page, @RequestParam Integer size,
			@RequestParam(required = false) String field, 
			@RequestParam(required = false) String value) {
		return portalPuphax.getTableWithPage(table, page, size, field, value);
	}
	
	
	@GetMapping("/PUPHAX/{tableName}/size")
	public ResponseEntity<Long> getTableSize(
			@PathVariable("tableName") PuphaxTables table,
			@RequestParam(required = false) String field, 
			@RequestParam(required = false) String value){
		return ResponseEntity.ok(portalPuphax.getTableSize(table, field, value));
	}
}
