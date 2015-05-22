package fractal.crud_rest_json.persistence;

import java.util.Date;

public class Decorator<T extends Identifiable<K>, K> {

	private final KeyGenerator<K>	keyGenerator;

	public Decorator(KeyGenerator<K> keyGenerator) {
		this.keyGenerator = keyGenerator;
	}

	public void decorate(T t) {
		Date now = new Date();
		if (t.getId() == null || !t.getId().isPresent()) {
			t.setId(keyGenerator.generate());
			t.setCreated(now);
		}
		t.setLastUpdated(now);
	}

}
