package fractal.crud_rest_json.persistence;

import java.util.Objects;

import com.google.gson.JsonObject;

public class IdentifiableJsonObject extends TrackedIdentifiable {

	public IdentifiableJsonObject(String name, JsonObject jsonObject) {
		super(name);
		setJsonObject(jsonObject);
	}

	private JsonObject	jsonObject;

	public JsonObject getJsonObject() {
		return jsonObject;
	}

	public void setJsonObject(JsonObject jsonObject) {
		Objects.requireNonNull(jsonObject);
		this.jsonObject = jsonObject;
	}

}
