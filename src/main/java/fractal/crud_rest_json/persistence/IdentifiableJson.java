package fractal.crud_rest_json.persistence;

import java.text.DateFormat;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.Objects;
import java.util.Optional;
import java.util.UUID;

import com.google.gson.JsonObject;

public class IdentifiableJson implements Identifiable<UUID> {

	private final DateFormat	dateFormat	= new SimpleDateFormat();

	private JsonObject			jsonObject;

	public IdentifiableJson(JsonObject jsonObject) {
		setJsonObject(jsonObject);
	}

	public JsonObject getJsonObject() {
		return jsonObject;
	}

	public void setJsonObject(JsonObject jsonObject) {
		this.jsonObject = Objects.requireNonNull(jsonObject);
	}

	@Override
	public Optional<UUID> getId() {
		return Optional.ofNullable(jsonObject.get("id")).map(json -> UUID.fromString(json.getAsString()));
	}

	@Override
	public void setId(UUID key) {
		Objects.requireNonNull(key);
		jsonObject.addProperty("id", key.toString());
	}

	@Override
	public String getName() {
		return jsonObject.get("name").getAsString();
	}

	@Override
	public void setName(String name) {
		jsonObject.addProperty("name", Objects.requireNonNull(name));
	}

	@Override
	public Date getCreated() {
		try {
			return dateFormat.parse(jsonObject.get("created").getAsString());
		} catch (ParseException e) {
			throw new RuntimeException(e);
		}
	}

	@Override
	public void setCreated(Date created) {
		jsonObject.addProperty("created", dateFormat.format(Objects.requireNonNull(created)));
	}

	@Override
	public void setLastUpdated(Date updated) {
		jsonObject.addProperty("lastUpdated", dateFormat.format(Objects.requireNonNull(updated)));
	}

	@Override
	public Optional<Date> getLastUpdated() {
		return Optional.ofNullable(jsonObject.get("lastUpdated").getAsString()).map(updated -> {
			try {
				return dateFormat.parse(updated);
			} catch (ParseException e) {
				throw new RuntimeException(e);
			}
		});
	}
}
