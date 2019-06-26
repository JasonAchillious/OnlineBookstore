package dao;

import java.sql.SQLException;

import socket.InfoFromFront;
import socket.InfoToFront;

public interface BillboardDao {

	/** Get the book's id of a billboard.
	 * @author Niu Lei, Kevin Sun Edit at May 21th
	 * @param infoFromFront
	 * @return
	 * @throws SQLException
	 */
	InfoToFront GetBookListBooks(InfoFromFront infoFromFront) throws SQLException;

	/** Get the details of a billboard.
	 * @author Niu Lei
	 * @param infoFromFront
	 * @return
	 * @throws SQLException
	 */
	InfoToFront GetTitleDescription(InfoFromFront infoFromFront) throws SQLException;
}
