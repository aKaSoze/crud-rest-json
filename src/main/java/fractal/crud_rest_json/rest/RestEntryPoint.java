package fractal.crud_rest_json.rest;

import java.util.HashSet;
import java.util.LinkedList;
import java.util.List;
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

import fractal.crud_rest_json.persistence.IdentifiableJsonObject;
import fractal.crud_rest_json.persistence.Repository;
import fractal.crud_rest_json.rest.Either.Right;

@Path("{subResources:.*}")
public class RestEntryPoint {

	private static final Set<String>					beanPackages	= new HashSet<>();

	private UriInfo										uriInfo;

	private Gson										gson;

	private Repository<JsonObject, String>	jsonRepo;

	static {
		beanPackages.add("");
	}

	@Inject
	public RestEntryPoint(UriInfo uriInfo, Gson gson, Repository<IdentifiableJsonObject, String> jsonRepo) {
		this.uriInfo = uriInfo;
		this.gson = gson;
		this.jsonRepo = jsonRepo;
	}

	@GET
	@Produces(MediaType.APPLICATION_JSON)
	public String get() {
		ObjectReference objectReference = getLastObjectFromPath();

		if (objectReference.key.isPresent()) {
			return gson.toJson(objectReference.entityType.map(clazz -> jsonRepo.get(objectReference.key.get()), entityType -> jsonRepo.get(objectReference.key.get())));
		} else {
			return gson.toJson(objectReference.entityType.map(clazz -> jsonRepo.getAll(), typeName -> jsonRepo.filter(jsonObject -> jsonObject.getType().equals(typeName))));
		}
	}

	@POST
	public Response post(String jsonString) {
		ObjectReference objectReference = getLastObjectFromPath();
		JsonObject jsonObject = gson.fromJson(jsonString, JsonElement.class).getAsJsonObject();
		
		IdentifiableJsonObject identifiableJsonObject = objectReference.entityType.unify(clazz -> save(objectReference.key.get(), clazz, jsonObject),
				typeName -> save(objectReference.key.get(), typeName, jsonObject));

		return Response.created(uriInfo.getAbsolutePathBuilder().path(identifiableJsonObject.getName()).build()).build();
	}

	@PUT
	public String put(String jsonObject) {
		return uriInfo.getPath();
	}

	@DELETE
	public String delete() {
		return uriInfo.getPath();
	}

	private IdentifiableJsonObject save(String name, Class<?> clazz, JsonObject jsonObject) {
		return null;
	}

	private IdentifiableJsonObject save(String name, String typeName, JsonObject jsonObject) {
		IdentifiableJsonObject identifiableJsonObject = new IdentifiableJsonObject(typeName, name, jsonObject);
		jsonRepo.save(identifiableJsonObject);
		return identifiableJsonObject;
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
		RestEntryPoint entryPoint = new RestEntryPoint(null, new Gson(), null);
		System.out.println(entryPoint.get());
	}

}
