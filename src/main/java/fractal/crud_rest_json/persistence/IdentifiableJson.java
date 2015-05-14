package fractal.crud_rest_json.persistence;

import java.text.DateFormat;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.Objects;
import java.util.Optional;

import com.google.gson.JsonObject;

public class IdentifiableJson implements Identifiable<String> {

	private final DateFormat	dateFormat	= new SimpleDateFormat();

	private JsonObject			jsonObject;

	public JsonObject getJsonObject() {
		return jsonObject;
	}

	public void setJsonObject(JsonObject jsonObject) {
		this.jsonObject = jsonObject;
	}

	@Override
	public Optional<String> getId() {
		return Optional.ofNullable(jsonObject.get("id").getAsString());
	}

	@Override
	public void setId(String key) {
		jsonObject.addProperty("id", key);
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
		jsonObject.addProperty("updated", dateFormat.format(Objects.requireNonNull(updated)));
	}

	@Override
	public Optional<Date> getLastUpdated() {
		return Optional.ofNullable(jsonObject.get("updated").getAsString()).map(updated -> {
			try {
				return dateFormat.parse(updated);
			} catch (ParseException e) {
				throw new RuntimeException(e);
			}
		});
	}

}
