package dao;

import java.sql.SQLException;

import socket.InfoFromFront;
import socket.InfoToFront;

public interface DanmuDao {
	/** get the content of a barrage by its id
	 * @author Niu Lei
	 * @param infoFromFront
	 * @return
	 * @throws SQLException
	 */
	InfoToFront GetDanmuContent(InfoFromFront infoFromFront) throws SQLException;

	/** get the Danmus' ids that a user created.
	 * @author Niu Lei
	 * @param infoFromFront
	 * @return
	 * @throws SQLException
	 */
	InfoToFront GetMyDanmus(InfoFromFront infoFromFront) throws SQLException;

	/** get the IDs of a book's Danmu
	 * @author Niu Lei
	 * @param infoFromFront
	 * @return
	 * @throws SQLException
	 */
	InfoToFront GetDanmuOfBook(InfoFromFront infoFromFront) throws SQLException;

	/** get full content of a Danmu
	 * @author Niu Lei
	 * @param infoFromFront
	 * @return
	 * @throws SQLException
	 */
	InfoToFront GetFullDanmuContent(InfoFromFront infoFromFront) throws SQLException;

	/** delete the danmu or update danmu by danmuId.
	 * @author Niu Lei
	 * @param infoFromFront
	 * @return
	 * @throws SQLException
	 */
	InfoToFront ChangeDanmu(InfoFromFront infoFromFront) throws SQLException;

	/** Create / send danmu.
	 * @author Niu Lei
	 * @param infoFromFront
	 * @return
	 * @throws SQLException
	 */
	InfoToFront CreateDanmu(InfoFromFront infoFromFront) throws SQLException;
}
