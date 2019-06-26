package controller;

import com.google.gson.Gson;
import com.google.gson.GsonBuilder;
import com.google.gson.JsonSyntaxException;

import socket.InfoFromAdmin;
import socket.InfoFromFront;
import socket.InfoToFront;

/**
 * @author Kevin Sun
 * <p>
 * Edit on 2019.5.20 Kevin Sun.
 */
public abstract class AbstractController {
	private Gson gson = new GsonBuilder().setPrettyPrinting().create();

	protected InfoFromFront fromJson(String info) {
		InfoFromFront infoFromFront;
		try {
			infoFromFront = gson.fromJson(info, InfoFromFront.class);
		} catch (JsonSyntaxException e) {
			e.printStackTrace();
			return null;
		}
		return infoFromFront;
	}

	protected InfoFromAdmin fromAdminJson(String info) {
		InfoFromAdmin infoFromFront;
		try {
			infoFromFront = gson.fromJson(info, InfoFromAdmin.class);
		} catch (JsonSyntaxException e) {
			e.printStackTrace();
			return null;
		}
		return infoFromFront;
	}

	protected String toJson(InfoToFront info) {
		String json = gson.toJson(info);
		return json;
	}

	/**
	 * Controls the info to the correct method
	 * @param infoFromFront
	 * @return The <code>InfoToFront</code>'s instance's Json string
	 * @throws Exception
	 */
	public abstract String methodController(String infoFromFront);

}
