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
@Table(name = "NEAK_PUPHAX_SZAKVKODOK")
public class PUPHAXSzakvkodok implements Serializable {
	private static final long serialVersionUID = 962915527428593903L;

	@Id
	private Long idSzakvkodok;
	
    private Integer kod;
	
    private String leiras;
	
    private Integer megfelel;

	private Long kihirdetesId;
	
}