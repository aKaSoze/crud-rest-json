package fractal.crud_rest_json.persistence;

import java.util.HashMap;
import java.util.Map;

public class RepositoryLocator {

	private final Map<Class<?>, Repository<?, ?>>	repos			= new HashMap<>();

	private final Map<String, Repository<?, ?>>		genericRepos	= new HashMap<>();

	@SuppressWarnings("unchecked")
	public <T extends Identifiable<K>, K> Repository<T, K> getRepository(Class<T> clazz) {
		if (!repos.containsKey(clazz)) {
			repos.put(clazz, new InMemoryRepository<>());
		}
		return (Repository<T, K>) repos.get(clazz);
	}

	@SuppressWarnings("unchecked")
	public <T extends Identifiable<K>, K> Repository<T, K> getRepository(String extraType) {
		if (!genericRepos.containsKey(extraType)) {
			genericRepos.put(extraType, new InMemoryRepository<>());
		}
		return (Repository<T, K>) genericRepos.get(extraType);
	}

}
