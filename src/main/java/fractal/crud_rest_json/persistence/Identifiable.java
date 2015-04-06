package fractal.crud_rest_json.persistence;

import java.util.Optional;

public interface Identifiable<K> {

	Optional<K> getId();

	void setId(K key);

}
