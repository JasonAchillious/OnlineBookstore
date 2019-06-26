/**
 * 
 */
package controller;

import java.lang.reflect.Method;

import service.DAOFactory;
import service.impl.DAOFactoryImpl;
import socket.InfoFromAdmin;
import socket.InfoToFront;

/**
 * @author Kevin Sun
 *
 */
public class AdminReflectionController extends AbstractController {

	@Override
	public String methodController(String infoFromFront) {
		InfoFromAdmin info = this.fromAdminJson(infoFromFront);

		DAOFactory factory = new DAOFactoryImpl();

		String type = info.getType();
		// According to the method, find the corresponding object. baseDao <- son of BaseDao.
		Object baseDao = factory.getBaseDao(type);

		Method method = null;
		Method[] methods = baseDao.getClass().getMethods();
		for (int i = 0; i < methods.length; i++) {
			if (methods[i].getName().equalsIgnoreCase(type))
				method = methods[i];
		}

		Object infoToFront = null;
		try {
			infoToFront = method.invoke(baseDao, info);
		} catch (Exception e) {
			e.printStackTrace();
		}
		String front = null;
		InfoToFront infoFront = null;

		if (infoToFront instanceof String) {
			front = (String) infoToFront;
		}
		else if (infoToFront instanceof InfoToFront) {
			infoFront = (InfoToFront) infoToFront;
		}
		else {
			System.err.println("Invoke method did not return correct result");
			return null;
		}
		return front != null ? front : toJson(infoFront);
	}

}
