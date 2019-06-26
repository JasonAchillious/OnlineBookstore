package controller;

import java.lang.reflect.Method;

import service.DAOFactory;
import service.impl.DAOFactoryImpl;
import socket.InfoFromFront;
import socket.InfoToFront;

/**
 * @author Jason Zhao, Kevin Sun
 * <p>
 * Edit on 2019.5.23 Kevin Sun.
 */
public class UserReflectionController extends AbstractController {

	@Override
	public String methodController(String infoFromFront) {
		InfoFromFront info = this.fromJson(infoFromFront);

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
		boolean setSuccessFalse = false;
		try {
			infoToFront = method.invoke(baseDao, info);
		} catch (Exception e) {
			e.printStackTrace();
			setSuccessFalse = true;
		}
		InfoToFront front;

		if (infoToFront instanceof InfoToFront) {
			front = (InfoToFront) infoToFront;
			front.setType(method.getName());
			if (setSuccessFalse)
				front.setSuccess(false);
		}
		else {
			System.err.println("Invoke method did not return correct result");
			return null;
		}
		return this.toJson(front);
	}
}
