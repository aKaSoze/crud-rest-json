package fractal.crud_rest_json.persistence;

import java.util.Date;
import java.util.Optional;

public interface Identifiable<K> {

	Optional<K> getId();

	void setId(K key);

	String getName();

	void setName(String name);

	Date getCreated();
	
	void setCreated(Date date);

	void setLastUpdated(Date date);

	Optional<Date> getLastUpdated();

}
