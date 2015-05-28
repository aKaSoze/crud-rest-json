package fractal.crud_rest_json.persistence;

import java.util.Objects;

public class ValidatorLocator extends Locator {

	private final RepositoryLocator	repositoryLocator;

	public ValidatorLocator(RepositoryLocator repositoryLocator) {
		this.repositoryLocator = Objects.requireNonNull(repositoryLocator);
	}

	public <T extends Identifiable<K>, K> Validator<T, K> locate(Class<T> clazz) {
		return locate(clazz, () -> new Validator<>(repositoryLocator.locate(clazz)));
	}

}
