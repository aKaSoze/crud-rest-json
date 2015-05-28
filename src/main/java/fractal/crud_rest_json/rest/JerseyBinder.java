package fractal.crud_rest_json.rest;

import java.lang.reflect.ParameterizedType;
import java.lang.reflect.Type;
import java.util.HashSet;
import java.util.LinkedList;
import java.util.List;
import java.util.Optional;
import java.util.Set;
import java.util.UUID;

import javax.inject.Inject;
import javax.ws.rs.core.UriInfo;

import org.glassfish.hk2.api.Factory;
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
import fractal.crud_rest_json.persistence.IdentifiableBean;
import fractal.crud_rest_json.persistence.IdentifiableJson;
import fractal.crud_rest_json.persistence.InMemoryRepository;
import fractal.crud_rest_json.persistence.Repository;
import fractal.crud_rest_json.persistence.RepositoryLocator;
import fractal.crud_rest_json.persistence.ValidatorLocator;
import fractal.crud_rest_json.rest.Either.Left;
import fractal.crud_rest_json.rest.Either.Right;

public class JerseyBinder extends AbstractBinder {

	@Override
	protected void configure() {
		bind(new GsonBuilder().registerTypeAdapter(Optional.class, new OptionalTypeAdapter<>()).registerTypeAdapter(IdentifiableJson.class, new JsonTypeAdapter()).create()).to(Gson.class);

		TypeLiteral<Repository<IdentifiableJson, UUID>> jsonRepoType = new TypeLiteral<Repository<IdentifiableJson, UUID>>() {
		};
		bind(new InMemoryRepository<IdentifiableJson, UUID>()).to(jsonRepoType);
		bind(new Decorator<Identifiable<UUID>, UUID>(() -> UUID.randomUUID())).to(new TypeLiteral<Decorator<Identifiable<UUID>, UUID>>() {
		});

		RepositoryLocator repositoryLocator = new RepositoryLocator();
		bind(repositoryLocator).to(RepositoryLocator.class);
		bind(new ValidatorLocator(repositoryLocator)).to(ValidatorLocator.class);
		bindFactory(ObjectReferenceFactory.class).to(ObjectReference.class);
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

	public static class JsonTypeAdapter implements JsonSerializer<IdentifiableJson>, JsonDeserializer<IdentifiableJson> {

		@Override
		public IdentifiableJson deserialize(JsonElement json, Type typeOfT, JsonDeserializationContext context) throws JsonParseException {
			return new IdentifiableJson(context.deserialize(json, ((ParameterizedType) typeOfT).getActualTypeArguments()[0]));
		}

		@Override
		public JsonElement serialize(IdentifiableJson src, Type typeOfSrc, JsonSerializationContext context) {
			return context.serialize(src.getJsonObject());
		}
	}

	public static class ObjectReferenceFactory implements Factory<ObjectReference> {

		private final UriInfo				uriInfo;

		private static final Set<String>	beanPackages	= new HashSet<>();

		static {
			beanPackages.add("fractal.crud_rest_json.persistence.beans");
		}

		@Inject
		public ObjectReferenceFactory(UriInfo uriInfo) {
			this.uriInfo = uriInfo;
		}

		@Override
		public ObjectReference provide() {
			return getLastObjectFromPath();
		}

		@Override
		public void dispose(ObjectReference t) {
		}

		private ObjectReference getLastObjectFromPath() {
			List<ObjectReference> objectPath = getObjectPath();
			return objectPath.get(objectPath.size() - 1);
		}

		private List<ObjectReference> getObjectPath() {
			List<ObjectReference> objectPath = new LinkedList<>();
			String[] tokens = uriInfo.getPath().split("/");
			for (int i = 0; i < tokens.length; i += 2) {
				if (i + 1 < tokens.length) {
					objectPath.add(new ObjectReference(toClass(tokens[i]), tokens[i + 1]));
				} else {
					objectPath.add(new ObjectReference(toClass(tokens[i])));
				}
			}

			return objectPath;
		}

		private Either<Class<? extends IdentifiableBean>, String> toClass(String token) {
			String simpleClassName = token.substring(0, 1).toUpperCase() + token.substring(1).toLowerCase();
			return getClassBySimpleName(simpleClassName).map(clazz -> {
				Either<Class<? extends IdentifiableBean>, String> either = new Left<>(clazz);
				return either;
			}).orElse(new Right<>(simpleClassName));
		}

		@SuppressWarnings("unchecked")
		private Optional<Class<? extends IdentifiableBean>> getClassBySimpleName(String simpleClassName) {
			for (String pkg : beanPackages) {
				try {
					return Optional.of((Class<? extends IdentifiableBean>) Class.forName(pkg + "." + simpleClassName));
				} catch (ClassNotFoundException e) {
				}
			}
			return Optional.empty();
		}
	}

}
