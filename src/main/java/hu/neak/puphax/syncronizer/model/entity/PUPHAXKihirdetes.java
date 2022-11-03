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
@Table(name = "NEAK_PUPHAX_KIHIRDETES")
public class PUPHAXKihirdetes implements Serializable, Comparable<PUPHAXKihirdetes> {
	private static final long serialVersionUID = -4061098968144969969L;
	
	private Long idKihirdetes;
	
	@Id
	private LocalDateTime ervDatum;
	private LocalDateTime keszDatum;
	private Integer munkaver;
	private String status;
	private LocalDateTime published;
	
	
	@Override
	public int compareTo(PUPHAXKihirdetes o) {
		if(o.getErvDatum() == null || this.ervDatum == null) {
			return 0;
		}
		return o.ervDatum.compareTo(this.ervDatum);
	}
}