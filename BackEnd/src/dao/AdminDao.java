package dao;

import socket.InfoFromAdmin;
import socket.InfoToFront;

/**
 * @author Kevin Sun
 */
public interface AdminDao {
	/**
	 * Directly perform sql statements and return success or result set as HTML
	 * @author Kevin Sun
	 * @param infoFromAdmin
	 * @return
	 */
	String PerformSQL(InfoFromAdmin infoFromAdmin);

	/**
	 * Admin Edit Billboard
	 * @author Kevin Sun
	 * @param infoFromAdmin
	 * @return
	 */
	InfoToFront ChangeBillboard(InfoFromAdmin infoFromAdmin);
}
