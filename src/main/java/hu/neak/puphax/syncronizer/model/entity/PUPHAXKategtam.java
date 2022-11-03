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
@Table(name = "NEAK_PUPHAX_KATEGTAM")
public class PUPHAXKategtam implements Serializable {
	private static final long serialVersionUID = 5964236382220060414L;
	
	@Id
	private Long idKategtam;
	private String kategoria;
	private String tamtechn;
	private Integer kgyirhato;
	private Integer minEletkor;
	private Integer maxEletkor;
	private Integer nem;
	private Double tamszaz;
	private Integer fixId;
	private Double refntk;
	private Long ntam;
	private Double btam;
	private Double terdij;
	private Long ntktd;
	private Long mihaid;
	private Double mihacel;
	private Long mihastat;
	private String kihi;
	private String felme;
	private Long tamalapId;
}