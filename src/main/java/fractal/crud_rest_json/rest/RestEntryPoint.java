package fractal.crud_rest_json.rest;

import java.util.HashSet;
import java.util.LinkedList;
import java.util.List;
import java.util.Objects;
import java.util.Optional;
import java.util.Set;

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
import fractal.crud_rest_json.persistence.IdentifiableJson;
import fractal.crud_rest_json.persistence.Repository;
import fractal.crud_rest_json.persistence.RepositoryLocator;
import fractal.crud_rest_json.rest.Either.Right;

@Path("{subResources:.*}")
public class RestEntryPoint {

	private static final Set<String>						beanPackages	= new HashSet<>();

	private final UriInfo									uriInfo;

	private final Gson										gson;

	private final RepositoryLocator							repositoryLocator;

	private final Decorator<Identifiable<String>, String>	decorator;

	static {
		beanPackages.add("");
	}

	@Inject
	public RestEntryPoint(UriInfo uriInfo, Gson gson, RepositoryLocator repositoryLocator, Decorator<Identifiable<String>, String> decorator) {
		this.uriInfo = uriInfo;
		this.gson = gson;
		this.repositoryLocator = Objects.requireNonNull(repositoryLocator);
		this.decorator = Objects.requireNonNull(decorator);

	}

	@GET
	@Produces(MediaType.APPLICATION_JSON)
	public String get() {
		ObjectReference objectReference = getLastObjectFromPath();
		Repository<IdentifiableJson, String> jsonRepo = repositoryLocator.getRepository(objectReference.entityType.asRight().get());

		if (objectReference.key.isPresent()) {
			return objectReference.key.map(key -> objectReference.entityType.map(clazz -> jsonRepo.get(key), typeAsString -> getIdentifiable(typeAsString, key)).unify(gson::toJson)).get().toString();
		} else {
			return gson.toJson(objectReference.entityType.map(clazz -> jsonRepo.getAll(), typeName -> jsonRepo.getAll()));
		}
	}

	@POST
	public Response post(String jsonString) {
		JsonObject jsonObject = gson.fromJson(jsonString, JsonElement.class).getAsJsonObject();
		ObjectReference objectReference = getLastObjectFromPath();

		IdentifiableJson identifiableJson = objectReference.entityType.unify(clazz -> save(clazz, jsonObject), typeName -> save(typeName, jsonObject));

		return Response.created(uriInfo.getAbsolutePathBuilder().path(identifiableJson.getName()).build()).build();
	}

	@PUT
	public String put(String jsonObject) {
		return uriInfo.getPath();
	}

	@DELETE
	public String delete() {
		return uriInfo.getPath();
	}

	private IdentifiableJson save(Class<?> clazz, JsonObject jsonObject) {
		return null;
	}

	private JsonObject getIdentifiable(String typeAsString, String key) {
		Repository<IdentifiableJson, String> repo = repositoryLocator.getRepository(typeAsString);
		Optional<IdentifiableJson> valueByName = repo.yield(json -> json.getName().equals(key));
		if (valueByName.isPresent()) {
			return valueByName.get().getJsonObject();
		} else {
			return repo.get(key).get().getJsonObject();
		}
	}

	private IdentifiableJson save(String typeName, JsonObject jsonObject) {
		IdentifiableJson identifiableJson = new IdentifiableJson(jsonObject);
		decorator.decorate(identifiableJson);
		Repository<IdentifiableJson, String> jsonRepo = repositoryLocator.getRepository(typeName);
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
				objectPath.add(new ObjectReference(toSimpleClassName(tokens[i]), tokens[i + 1]));
			} else {
				objectPath.add(new ObjectReference(toSimpleClassName(tokens[i])));
			}
		}

		return objectPath;
	}

	private Either<Class<?>, String> toSimpleClassName(String token) {
		String simpleClassName = token.substring(0, 1).toUpperCase() + token.substring(1).toLowerCase();

		// Class.forName("");
		return new Right<>(simpleClassName);
	}

	public static void main(String[] args) {
		RestEntryPoint entryPoint = new RestEntryPoint(null, new Gson(), null, new Decorator<>(() -> ""));
		System.out.println(entryPoint.get());
	}

}
