package textadventure;

import org.w3c.dom.Element;

public class Quest {
	public enum Status {AVAILABLE,STARTED,COMPLETED}
	private String name;
	private String title;
	private String description;
	public String getName() {
		return name;
	}
	public String getDescription() {
		return description;
	}
	public String perform(String method) {
		if(method.equals("start")) {
			if(Game.getGame().getPlayerQuestStatus(this) == null || Game.getGame().getPlayerQuestStatus(this) == Quest.Status.AVAILABLE) {
				Game.getGame().setPlayerQuestStatus(this,Quest.Status.STARTED);
				return "Started quest: "+title+"\n";
			}
		}
		return null;
	}
	public static Quest Read(Element node) {
		Quest q = new Quest();
		q.name = node.getAttribute("name");
		q.title = node.getAttribute("title");
		return q;
	}
	public String getTitle() {
		return title;
	}
}
