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
@Table(name = "NEAK_PUPHAX_BNOHOZZAR")
public class PUPHAXBnohozzar implements Serializable {
	private static final long serialVersionUID = -1059699459581711099L;

	@Id
	private Long azonosito;
	private Long eupontId;
	private Long bnoId;
	private Long kihirdetesId;
	private String genkey;
}