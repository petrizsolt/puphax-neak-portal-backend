package hu.neak.puphax.syncronizer.model.entity;

import java.io.Serializable;

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
@Table(name = "NEAK_PUPHAX_EUPONTOK")
public class PUPHAXEupontok implements Serializable {
	private static final long serialVersionUID = 9001674355040671382L;
	
	@Id
	private Long idEupontok;
	private Integer pontszam;
	private String perjelzes;
	private String feliras;
	private String megjegyzes;
	private Long kihirdetesId;
}