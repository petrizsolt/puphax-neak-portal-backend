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
@Table(name = "NEAK_PUPHAX_ATCKONYV")
public class PUPHAXAtckonyv implements Serializable {
	private static final long serialVersionUID = -9053175238341742913L;

	@Id
    private String atc;
    
    private String angol;

    private String hatoanyag;
    
    private Long kihirdetesId;
    
    private String megnev;
}