package dao;

import java.sql.SQLException;

import socket.InfoFromFront;
import socket.InfoToFront;

public interface ReadlistDao {

	/** get the user's readlists' IDs without a certian book
	 * @author Niu Lei
	 * @param infoFromFront
	 * @return
	 * @throws SQLException
	 */
	public InfoToFront GetMyReadListsWithout(InfoFromFront infoFromFront) throws SQLException;

	/** get the user's created readlists' IDs
	 * @author Niu Lei
	 * @param infoFromFront
	 * @return
	 * @throws SQLException
	 */
	InfoToFront GetMyCreatedReadLists(InfoFromFront infoFromFront) throws SQLException;

	/** get the user's followed readlists' IDs
	 * @author Kevin Sun
	 * @param infoFromFront
	 * @return
	 * @throws SQLException
	 */
	InfoToFront GetMyFollowedReadLists(InfoFromFront infoFromFront) throws SQLException;

	/** user can edit their created read lists
	 * @author Kevin Sun
	 * @param infoFromFront
	 * @return
	 * @throws SQLException
	 */
	InfoToFront ChangeReadList(InfoFromFront infoFromFront) throws SQLException;

	/** user can create their own read list.
	 * @author Kevin Sun
	 * @param infoFromFront
	 * @return
	 * @throws SQLException
	 */
	InfoToFront CreateReadList(InfoFromFront infoFromFront) throws SQLException;

	// follow or cancel following a read list.
	InfoToFront FollowReadList(InfoFromFront infoFromFront) throws SQLException;
}
