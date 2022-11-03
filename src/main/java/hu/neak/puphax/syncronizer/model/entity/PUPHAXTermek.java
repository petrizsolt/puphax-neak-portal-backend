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
@Table(name = "NEAK_PUPHAX_TERMEK")
public class PUPHAXTermek implements Serializable {
	private static final long serialVersionUID = 5849913517663024352L;

	@Id
	private Long idTermek;
	private Long parentId;
	private LocalDateTime ervKezd;
	private LocalDateTime ervVege;
	private String termekkod;
	private Long kozhid;
	private String ttt;
	private String tk;
	private String tktorles;
	private LocalDateTime tktorlesdat;
	private String eankod;
	private Long brandId;
	private String nev;
	private String kisznev;
	private String atc;
	private String iso;
	private String hatoanyag;
	private String adagmod;
	private String gyforma;
	private String rendelhet;
	private Integer egyenId;
	private Integer helyettesith;
	private String potencia;
	private Double ohatoMenny;
	private Double hatoMenny;
	private String hatoEgys;
	private Double kiszMenny;
	private String kiszEgys;
	private Double dddMenny;
	private String dddEgys;
	private Double dddFaktor;
	private Double dot;
	private Long adagMenny;
	private String adagEgys;
	private Integer egyedi;
	private Integer oldalisag;
	private Integer tobblgar;
	private String patika;
	private Long dobazon;
	private String keresztjelzes;
	private Long forgengtId;
	private Long forgalmazId;
	private Integer forgalomban;
	private Long kihirdetesId;
}