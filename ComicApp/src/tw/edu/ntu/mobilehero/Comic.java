package tw.edu.ntu.mobilehero;

import java.util.ArrayList;

public class Comic {	
	String identifier;
	ArrayList<ServerPanel> panels;
	int views;
	String inviter;
		
	public Comic(String identifier, 
			String blobKey1,
			String blobKey2,
			String blobKey3,
			String blobKey4,
			String creator1,
			String creator2,
			String creator3,
			String creator4,
			int views) {
		this.identifier = identifier;
		panels = new ArrayList<ServerPanel>();
        panels.add(0, new ServerPanel(blobKey1, creator1));
		panels.add(1, new ServerPanel(blobKey2, creator2));
		panels.add(2, new ServerPanel(blobKey3, creator3));
		panels.add(3, new ServerPanel(blobKey4, creator4));
		this.views = views;
	}
	
	public void setPanel(int order, String url, String creator) {
		panels.get(order).setUrl(url);
		panels.get(order).setCreator(creator);
	}
	
	public void setInviter(String inviter) {
		this.inviter = inviter;
	}
	
	public String getIdentifier() {
		return identifier;
	}
	
	public ServerPanel getPanel(int order) {
		return panels.get(order - 1);
	}
	
	public int getViews() {
		return views;
	}
	
	public String getInviter() {
		return inviter;
	}
}
