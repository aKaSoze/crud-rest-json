package fractal.crud_rest_json.rest;

import java.util.Optional;

public class ObjectReference {
	public final Class<?>			objectClass;
	public final Optional<String>	key;

	public ObjectReference(Class<?> objectClass) {
		this.objectClass = objectClass;
		this.key = Optional.empty();
	}

	public ObjectReference(Class<?> objectClass, String key) {
		this.objectClass = objectClass;
		this.key = Optional.of(key);
	}
}
