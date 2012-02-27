package textadventure;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.Iterator;
import java.util.Map.Entry;
import java.util.Set;

import org.w3c.dom.Element;
import org.w3c.dom.Node;
import org.w3c.dom.NodeList;

public class Conversation {
	public String name;
	public String In;
	public String Out;
	public HashMap<String,String> options;
	public ArrayList<ConversationAction> actions;
	public NPC npc;
	public Conversation() {
		options = new HashMap<String,String>();
		actions = new ArrayList<ConversationAction>();
	}

	public String doTalk(String string) {
		//TODO: Complex Conversation Actions
		String conversation = "Player:"+In+"\n"+"NPC:"+Out+"\n";
		
		Iterator<ConversationAction> ia = actions.iterator();
		if(ia.hasNext()) {
			while(ia.hasNext()) {
				ConversationAction a = ia.next();
				conversation+=a.doAction();
			}
		}
		
		
		Set<Entry<String,String>> set = options.entrySet();
		Iterator<Entry<String,String>> io = set.iterator();
		if(io.hasNext()) {
			conversation+="\n+Options:\n";
			while(io.hasNext()) {
				Entry<String,String> option = io.next();
				conversation+=option.getKey()+") "+option.getValue()+"\n";
			}
		}
		NPC.lastConversation = this;
		return conversation;
	}

	public static Conversation Read(Element node) {
		Conversation c = new Conversation();
		c.name = node.getAttribute("name");
		c.In = node.getAttribute("In");
		c.Out = node.getAttribute("Out");
		NodeList nodes = node.getChildNodes();
        for(int o = 0; o < nodes.getLength(); o++) {
        	Node oNode = nodes.item(o);
        	if (oNode.getNodeType() == Node.ELEMENT_NODE) {
        		Element eElement = (Element) oNode;
        		if(eElement.getTagName().equals("option")) {
        			String key = eElement.getAttribute("value");
        			String value = eElement.getAttribute("text");
        			c.options.put(key, value);
        		}
        		if(eElement.getTagName().equals("action")) {
        			ConversationAction a = ConversationAction.Read(eElement);
        			c.actions.add(a);
        		}
        	}
        }
		return c;
	}

	public boolean hasOption(String string) {
		return options.containsKey(string);
	}

}
