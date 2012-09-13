package tw.edu.ntu.mobilehero;

public class ServerPanel {
	final static String SERVER_URL = "http://linzy-hellogae.appspot.com";
	
	private String url, creator;		
	
	public ServerPanel() {}
	
	public ServerPanel(String blobKey, String creator) {
		this.url = SERVER_URL + "/serve?blob-key=" + blobKey;
		this.creator = creator;
	}
	
	public void setUrl(String url) {
		this.url = url;
	}
	
	public void setCreator(String creator) {
		this.creator = creator;
	}
	
	public String getUrl() {
		return url;
	}
	
	public String getCreator() {
		return creator;
	}
}
