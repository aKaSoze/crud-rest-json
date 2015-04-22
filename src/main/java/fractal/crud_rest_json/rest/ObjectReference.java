package fractal.crud_rest_json.rest;

import java.util.Optional;

import fractal.crud_rest_json.rest.Either.Left;
import fractal.crud_rest_json.rest.Either.Right;

public class ObjectReference {
	public final Either<Class<?>, String>	entityType;
	public final Optional<String>			key;

	public ObjectReference(Either<Class<?>, String> entityType) {
		this.entityType = entityType;
		this.key = Optional.empty();
	}

	public ObjectReference(Either<Class<?>, String> entityType, String key) {
		this.entityType = entityType;
		this.key = Optional.of(key);
	}

	public ObjectReference(Class<?> objectClass) {
		this(new Left<>(objectClass));
	}

	public ObjectReference(Class<?> objectClass, String key) {
		this(new Left<>(objectClass), key);
	}

	public ObjectReference(String objectClassName) {
		this(new Right<>(objectClassName));
	}

	public ObjectReference(String objectClassName, String key) {
		this(new Right<>(objectClassName), key);
	}

	public String getName() {
		return entityType.unify(clazz -> clazz.getSimpleName(), className -> className);
	}

	@Override
	public String toString() {
		return "ObjectReference [entityType=" + entityType + ", key=" + key + "]";
	}

}
