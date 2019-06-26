/**
 * 
 */
package socket;

/**
 * @author Kevin Sun
 *
 */
public class InfoFromAdmin {

	private int UserId = -1;
	private String Type;

	private String SQL;

	private Integer BillboardId;
	private Integer ChangeType;
	private Integer AlteredBookId;
	private String AlteredText;

	public int getUserId() {
		return UserId;
	}

	public void setUserId(int userId) {
		UserId = userId;
	}

	public String getType() {
		return Type;
	}

	public void setType(String type) {
		Type = type;
	}

	public String getSQL() {
		return SQL;
	}

	public void setSQL(String sQL) {
		SQL = sQL;
	}

	public Integer getBillboardId() {
		return BillboardId;
	}

	public void setBillboardId(Integer billboardId) {
		BillboardId = billboardId;
	}

	public Integer getChangeType() {
		return ChangeType;
	}

	public void setChangeType(Integer changeType) {
		ChangeType = changeType;
	}

	public Integer getAlteredBookId() {
		return AlteredBookId;
	}

	public void setAlteredBookId(Integer alteredBookId) {
		AlteredBookId = alteredBookId;
	}

	public String getAlteredText() {
		return AlteredText;
	}

	public void setAlteredText(String alteredText) {
		AlteredText = alteredText;
	}

}
