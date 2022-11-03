package hu.neak.puphax.syncronizer.utils;

import java.util.Collection;
import java.util.List;
import java.util.stream.Collectors;

import hu.neak.puphax.syncronizer.model.entity.PUPHAXBnokodok;
import hu.neak.puphax.syncronizer.model.entity.PUPHAXKategtam;
import hu.neak.puphax.syncronizer.model.entity.PUPHAXSzakvkodok;
import org.modelmapper.Conditions;
import org.modelmapper.ModelMapper;
import org.modelmapper.PropertyMap;
import org.modelmapper.convention.MatchingStrategies;

import com.oracle.xmlns.orawsv.puphax.puphaxws.OBJKATEGTAMIntType;
import com.oracle.xmlns.orawsv.puphax.puphaxws.OBJKODTABLAIntType;

import hu.neak.puphax.syncronizer.mapper.converter.GregorianToLocalDate;
import hu.neak.puphax.syncronizer.mapper.converter.GregorianToLocalDateTime;
import hu.neak.puphax.syncronizer.mapper.converter.HandleBigDecimalDoubleNulls;
import hu.neak.puphax.syncronizer.mapper.converter.HandleBigDecimalIntegerNulls;
import hu.neak.puphax.syncronizer.mapper.converter.HandleBigDecimalLongNulls;
import hu.neak.puphax.syncronizer.mapper.converter.HandleBigDecimalNulls;
import hu.neak.puphax.syncronizer.mapper.converter.HandleStringNulls;
import hu.neak.puphax.syncronizer.mapper.propertymap.AtcKonyvProperty;
import hu.neak.puphax.syncronizer.mapper.propertymap.BnokodokProperty;
import hu.neak.puphax.syncronizer.mapper.propertymap.IsoBookProperty;
import hu.neak.puphax.syncronizer.mapper.propertymap.KategtamProperty;
import hu.neak.puphax.syncronizer.mapper.propertymap.KihirdetesProperty;
import hu.neak.puphax.syncronizer.mapper.propertymap.NicheProperty;
import hu.neak.puphax.syncronizer.mapper.propertymap.OrvosokProperty;
import hu.neak.puphax.syncronizer.mapper.propertymap.SzakvkodokProperty;
import hu.neak.puphax.syncronizer.mapper.propertymap.TamalapProperty;
import hu.neak.puphax.syncronizer.mapper.propertymap.TermekProperty;
import lombok.experimental.UtilityClass;

@UtilityClass
public class Mapper {
	private static final ModelMapper modelMapper;

	/**
	 * Model mapper property setting are specified in the following block. Default
	 * property matching strategy is set to Strict see {@link MatchingStrategies}
	 * Custom mappings are added using {@link ModelMapper#addMappings(PropertyMap)}
	 */
	static {
		modelMapper = new ModelMapper();
		modelMapper.getConfiguration().setMatchingStrategy(MatchingStrategies.STRICT).setAmbiguityIgnored(true)
				.setSkipNullEnabled(true).setPropertyCondition(Conditions.isNotNull());
		modelMapper.addConverter(new GregorianToLocalDate());
		modelMapper.addConverter(new GregorianToLocalDateTime());
		modelMapper.addConverter(new HandleBigDecimalNulls());
		modelMapper.addConverter(new HandleBigDecimalIntegerNulls());
		modelMapper.addConverter(new HandleBigDecimalDoubleNulls());
		modelMapper.addConverter(new HandleBigDecimalLongNulls());
		modelMapper.addConverter(new HandleStringNulls());
		modelMapper.addMappings(new TamalapProperty());
		modelMapper.addMappings(new TermekProperty());
		modelMapper.addMappings(new KategtamProperty());
		modelMapper.addMappings(new BnokodokProperty());
		modelMapper.addMappings(new SzakvkodokProperty());
		modelMapper.addMappings(new OrvosokProperty());
		modelMapper.addMappings(new AtcKonyvProperty());
		modelMapper.addMappings(new IsoBookProperty());
		modelMapper.addMappings(new NicheProperty());
		modelMapper.addMappings(new KihirdetesProperty());
	}

	/**
	 * <p>
	 * Note: outClass object must have default constructor with no arguments
	 * </p>
	 *
	 * @param <D>      type of result object.
	 * @param <T>      type of source object to map from.
	 * @param entity   entity that needs to be mapped.
	 * @param outClass class of result object.
	 * @return new object of <code>outClass</code> type.
	 */
	public static <D, T> D map(final T entity, Class<D> outClass) {
		try {
			return modelMapper.map(entity, outClass);
		} catch (Exception e) {
			System.err.println("ModelMapper mapping error, obj: " + entity);
			throw new RuntimeException(e.getMessage());
		}
	}

	/**
	 * <p>
	 * Note: outClass object must have default constructor with no arguments
	 * </p>
	 *
	 * @param entityList list of entities that needs to be mapped
	 * @param outCLass   class of result list element
	 * @param <D>        type of objects in result list
	 * @param <T>        type of entity in <code>entityList</code>
	 * @return list of mapped object with <code><D></code> type.
	 */
	public static <D, T> List<D> mapAll(final Collection<T> entityList, Class<D> outCLass) {
		return entityList.stream().map(entity -> map(entity, outCLass)).collect(Collectors.toList());
	}

	/**
	 * Maps {@code source} to {@code destination}.
	 *
	 * @param source      object to map from
	 * @param destination object to map to
	 */
	public static <S, D> D map(final S source, D destination) {
		modelMapper.map(source, destination);
		return destination;
	}

	public static PUPHAXKategtam mapKategtam(OBJKATEGTAMIntType neakKategtam, Long tamalapId) {
		PUPHAXKategtam kategtam = map(neakKategtam, PUPHAXKategtam.class);
		kategtam.setTamalapId(tamalapId);
		return kategtam;
	}

	public static PUPHAXBnokodok mapBnokodok(OBJKODTABLAIntType neakBno) {
		return map(neakBno, PUPHAXBnokodok.class);
	}

	public static PUPHAXSzakvkodok mapAllSzakvkodok(OBJKODTABLAIntType neakSzakvkodok) {
		return map(neakSzakvkodok, PUPHAXSzakvkodok.class);
	}
}
