/**
 * 
 */
package entities;

/**
 * @author Kevin Sun
 */
public class ResultHtml {
	private String[] row;
	private boolean head;

	public ResultHtml(String... row) {
		this(false, row);
	}

	public ResultHtml(boolean head, String... row) {
		this.head = head;
		this.row = row;
	}

	public String toHtmlTableRow() {
		StringBuilder html = new StringBuilder();
		html.append("<tr>\n");
		String tdh = head ? "th" : "td";
		for (String s : row) {
			html.append("<" + tdh + ">" + s + "</" + tdh + ">");
		}
		html.append("\n");
		html.append("</tr>\n");
		return html.toString();
	}

	@Override
	public String toString() {
		return this.toHtmlTableRow().replaceAll("\\n", "");
	}
}
