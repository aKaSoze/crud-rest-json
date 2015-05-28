package fractal.crud_rest_json.persistence;

import java.util.HashMap;
import java.util.Map;
import java.util.function.Supplier;

public class Locator {

	private final Map<Object, Object>	repos	= new HashMap<>();

	@SuppressWarnings("unchecked")
	protected <T> T locate(Object key, Supplier<T> supplier) {
		if (!repos.containsKey(key)) {
			repos.put(key, supplier.get());
		}
		return (T) repos.get(key);
	}

}
