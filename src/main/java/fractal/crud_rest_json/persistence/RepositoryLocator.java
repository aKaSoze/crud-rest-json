package fractal.crud_rest_json.persistence;

import fractal.crud_rest_json.rest.Either;

public class RepositoryLocator extends Locator {

	public <T extends Identifiable<K>, K> Repository<T, K> locate(Class<T> clazz) {
		return locate(clazz, () -> new InMemoryRepository<>());
	}

	public <T extends Identifiable<K>, K> Repository<T, K> locate(String extraType) {
		return locate(extraType, () -> new InMemoryRepository<>());
	}

	public <K> Repository<? extends Identifiable<K>, K> locate(Either<Class<? extends Identifiable<K>>, String> type) {
		return type.unify(clazz -> locate(clazz), typeAsString -> locate(typeAsString));
	}
}
