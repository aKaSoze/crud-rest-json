package fractal.crud_rest_json.persistence;

import java.util.HashMap;
import java.util.Map;
import java.util.Objects;

public class ValidationException extends RuntimeException {

	private static final long			serialVersionUID	= 1L;

	private final Map<String, String>	issues				= new HashMap<>();

	public void addIssue(String fieldName, String issueDescription) {
		issues.put(Objects.requireNonNull(fieldName), Objects.requireNonNull(issueDescription));
	}

	public Boolean hasIssues() {
		return !issues.isEmpty();
	}

	@Override
	public String toString() {
		return issues.toString();
	}

}
