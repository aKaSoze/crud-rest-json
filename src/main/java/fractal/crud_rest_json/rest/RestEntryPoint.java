package fractal.crud_rest_json.rest;

import java.util.HashSet;
import java.util.LinkedList;
import java.util.List;
import java.util.Objects;
import java.util.Optional;
import java.util.Set;
import java.util.UUID;

import javax.inject.Inject;
import javax.ws.rs.DELETE;
import javax.ws.rs.GET;
import javax.ws.rs.POST;
import javax.ws.rs.PUT;
import javax.ws.rs.Path;
import javax.ws.rs.Produces;
import javax.ws.rs.core.MediaType;
import javax.ws.rs.core.Response;
import javax.ws.rs.core.UriInfo;

import com.google.gson.Gson;
import com.google.gson.JsonElement;
import com.google.gson.JsonObject;

import fractal.crud_rest_json.persistence.Decorator;
import fractal.crud_rest_json.persistence.Identifiable;
import fractal.crud_rest_json.persistence.IdentifiableBean;
import fractal.crud_rest_json.persistence.IdentifiableJson;
import fractal.crud_rest_json.persistence.Repository;
import fractal.crud_rest_json.persistence.RepositoryLocator;
import fractal.crud_rest_json.rest.Either.Left;
import fractal.crud_rest_json.rest.Either.Right;

@Path("{subResources:.*}")
public class RestEntryPoint {

	private static final Set<String>					beanPackages	= new HashSet<>();

	private final UriInfo								uriInfo;

	private final Gson									gson;

	private final RepositoryLocator						repositoryLocator;

	private final Decorator<Identifiable<UUID>, UUID>	decorator;

	static {
		beanPackages.add("fractal.crud_rest_json.persistence.beans");
	}

	@Inject
	public RestEntryPoint(UriInfo uriInfo, Gson gson, RepositoryLocator repositoryLocator, Decorator<Identifiable<UUID>, UUID> decorator) {
		this.uriInfo = Objects.requireNonNull(uriInfo);
		this.gson = Objects.requireNonNull(gson);
		this.repositoryLocator = Objects.requireNonNull(repositoryLocator);
		this.decorator = Objects.requireNonNull(decorator);
	}

	@GET
	@Produces(MediaType.APPLICATION_JSON)
	public String get() {
		ObjectReference objectReference = getLastObjectFromPath();

		if (objectReference.key.isPresent()) {
			return objectReference.key.map(key -> objectReference.entityType.map(clazz -> getIdentifiable(clazz, key), typeAsString -> getIdentifiable(typeAsString, key)).unify(gson::toJson)).get().toString();
		} else {
			return gson.toJson(objectReference.entityType.map(clazz -> repositoryLocator.locate(clazz).getAll(), typeName -> repositoryLocator.locate(typeName).getAll()));
		}
	}

	@POST
	public Response post(String jsonString) {
		JsonObject jsonObject = gson.fromJson(jsonString, JsonElement.class).getAsJsonObject();
		ObjectReference objectReference = getLastObjectFromPath();
		Identifiable<?> identifiable = objectReference.entityType.unify(clazz -> save(clazz, jsonObject), typeName -> save(typeName, jsonObject));
		return Response.created(uriInfo.getAbsolutePathBuilder().path(identifiable.getName()).build()).build();
	}

	@PUT
	public String put(String jsonObject) {
		return uriInfo.getPath();
	}

	@DELETE
	public String delete() {
		return uriInfo.getPath();
	}

	private JsonObject getIdentifiable(String typeAsString, String key) {
		Repository<IdentifiableJson, UUID> repo = repositoryLocator.locate(typeAsString);
		return repo.yield(json -> json.getName().equals(key)).orElse(repo.get(UUID.fromString(key)).get()).getJsonObject();
	}

	private IdentifiableBean getIdentifiable(Class<? extends IdentifiableBean> clazz, String key) {
		Repository<? extends IdentifiableBean, UUID> repo = repositoryLocator.locate(clazz);
		Optional<? extends IdentifiableBean> valueByName = repo.yield(json -> json.getName().equals(key));
		if (valueByName.isPresent()) {
			return valueByName.get();
		} else {
			return repo.get(UUID.fromString(key)).get();
		}
	}

	private <T extends IdentifiableBean> IdentifiableBean save(Class<T> clazz, JsonObject jsonObject) {
		T bean = gson.fromJson(jsonObject, clazz);
		decorator.decorate(bean);
		repositoryLocator.locate(clazz).save(bean);

		return bean;
	}

	private IdentifiableJson save(String typeName, JsonObject jsonObject) {
		IdentifiableJson identifiableJson = new IdentifiableJson(jsonObject);
		decorator.decorate(identifiableJson);
		Repository<IdentifiableJson, UUID> jsonRepo = repositoryLocator.locate(typeName);
		jsonRepo.save(identifiableJson);
		return identifiableJson;
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
				// TODO Auto-generated catch block
				e.printStackTrace();
			}
		}
		return Optional.empty();
	}

	public static void main(String[] args) {
		RestEntryPoint entryPoint = new RestEntryPoint(null, new Gson(), null, new Decorator<>(() -> null));
		System.out.println(entryPoint.get());
	}

}
