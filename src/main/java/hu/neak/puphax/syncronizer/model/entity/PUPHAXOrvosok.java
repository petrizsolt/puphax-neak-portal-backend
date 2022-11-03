package hu.neak.puphax.syncronizer.model.entity;

import java.io.Serializable;
import java.time.LocalDateTime;

import javax.persistence.Entity;
import javax.persistence.Id;
import javax.persistence.Table;

import com.fasterxml.jackson.databind.PropertyNamingStrategies;
import com.fasterxml.jackson.databind.annotation.JsonNaming;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@JsonNaming(PropertyNamingStrategies.UpperSnakeCaseStrategy.class)
@Data
@NoArgsConstructor
@AllArgsConstructor
@Entity
@Table(name = "NEAK_PUPHAX_ORVOSOK")
public class PUPHAXOrvosok implements Serializable {
	private static final long serialVersionUID = -2858848891017658632L;

	@Id
	private Long eesztAzon;
	
	private String pecsetkod;
	
	private Long szakvId;

    private LocalDateTime kezdet;
    
    private LocalDateTime vege;
   
	private Long kihirdetesId;
}