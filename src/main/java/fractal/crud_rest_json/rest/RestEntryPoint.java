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
import javax.ws.rs.core.UriInfo;

import com.google.gson.Gson;
import com.google.gson.JsonObject;

import fractal.crud_rest_json.rest.Either.Right;

@Path("{subResources:.*}")
public class RestEntryPoint {

	private static final Set<String>	beanPackages	= new HashSet<>();

	private UriInfo						uriInfo;

	private Gson						gson;

	@Inject
	public RestEntryPoint(UriInfo uriInfo, Gson gson) {
		this.uriInfo = uriInfo;
		this.gson = gson;
	}

	static {
		beanPackages.add("");
	}

	@GET
	@Produces(MediaType.APPLICATION_JSON)
	public String get() {
		JsonObject jsonObject = new JsonObject();
		jsonObject.addProperty("path", uriInfo.getPath());
		for (ObjectReference objectReference : getObjectPath()) {
			jsonObject.addProperty(objectReference.getName(), objectReference.key.orElse("All"));
		}

		return gson.toJson(jsonObject);
	}

	@POST
	public String post(String jsonObject) {
		return uriInfo.getPath();
	}

	@PUT
	public String put(String jsonObject) {
		return uriInfo.getPath();
	}

	@DELETE
	public String delete() {
		return uriInfo.getPath();
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
		RestEntryPoint entryPoint = new RestEntryPoint(null, new Gson());
		System.out.println(entryPoint.get());
	}

}
