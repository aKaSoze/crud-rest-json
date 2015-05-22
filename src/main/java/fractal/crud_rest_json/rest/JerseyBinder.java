package fractal.crud_rest_json.rest;

import java.lang.reflect.ParameterizedType;
import java.lang.reflect.Type;
import java.util.Optional;
import java.util.UUID;

import org.glassfish.hk2.api.TypeLiteral;
import org.glassfish.hk2.utilities.binding.AbstractBinder;

import com.google.gson.Gson;
import com.google.gson.GsonBuilder;
import com.google.gson.JsonDeserializationContext;
import com.google.gson.JsonDeserializer;
import com.google.gson.JsonElement;
import com.google.gson.JsonParseException;
import com.google.gson.JsonSerializationContext;
import com.google.gson.JsonSerializer;

import fractal.crud_rest_json.persistence.Decorator;
import fractal.crud_rest_json.persistence.Identifiable;
import fractal.crud_rest_json.persistence.IdentifiableJson;
import fractal.crud_rest_json.persistence.InMemoryRepository;
import fractal.crud_rest_json.persistence.Repository;
import fractal.crud_rest_json.persistence.RepositoryLocator;

public class JerseyBinder extends AbstractBinder {

	@Override
	protected void configure() {
		bind(new GsonBuilder().registerTypeAdapter(Optional.class, new OptionalTypeAdapter<>()).create()).to(Gson.class);

		TypeLiteral<Repository<IdentifiableJson, UUID>> jsonRepoType = new TypeLiteral<Repository<IdentifiableJson, UUID>>() {
		};
		bind(new InMemoryRepository<IdentifiableJson, UUID>()).to(jsonRepoType);
		bind(new Decorator<Identifiable<UUID>, UUID>(() -> UUID.randomUUID())).to(new TypeLiteral<Decorator<Identifiable<UUID>, UUID>>() {
		});
		bind(new RepositoryLocator()).to(RepositoryLocator.class);
	}

	public static class OptionalTypeAdapter<T> implements JsonSerializer<Optional<T>>, JsonDeserializer<Optional<T>> {

		@Override
		public Optional<T> deserialize(JsonElement json, Type typeOfT, JsonDeserializationContext context) throws JsonParseException {
			return Optional.ofNullable(context.deserialize(json, ((ParameterizedType) typeOfT).getActualTypeArguments()[0]));
		}

		@Override
		public JsonElement serialize(Optional<T> src, Type typeOfSrc, JsonSerializationContext context) {
			return context.serialize(src.orElse(null));
		}
	}

}
