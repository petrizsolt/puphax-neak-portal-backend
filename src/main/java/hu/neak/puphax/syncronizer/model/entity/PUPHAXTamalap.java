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
@Table(name = "NEAK_PUPHAX_TAMALAP")
public class PUPHAXTamalap implements Serializable {
	private static final long serialVersionUID = 1504354053124000333L;
	
	@Id
	private Long idTamalap;
	private LocalDateTime ervKezd;
	private LocalDateTime ervVege;
	private Double termar;
	private Double nkar;
	private Long fan;
	private Double fab;
	private Long maxfab;
	private Long afa;
	private Double ntk;
	private Double egysegar;
	private Long besorolas;
	private Long prasTermek;
	private Long kestTerm;
	private Long kgykeret;
	private String kulonl100;
	private Long kihirdetesId;
	private Long nicheId;
	private Long termekId;
}