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
@Table(name = "NEAK_PUPHAX_ISOKONYV")
public class PUPHAXIsokonyv implements Serializable {
	private static final long serialVersionUID = 4766361344584112489L;

	@Id
    private String iso;
    private String megnevezes;
	private Integer oldalisag;
	private Integer ffno;
	private Integer ekmax;
	private Integer ekmin;
	private Integer kihirdetesId;
}