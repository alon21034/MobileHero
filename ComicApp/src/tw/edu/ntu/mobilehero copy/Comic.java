package tw.edu.ntu.mobilehero;

public class Comic {	
    private String[] mUrl = {"","","",""};
    private String[] mCreater = {"","","",""};
    private String id;
    public String getId() {
        return id;
    }
    public void setId(String id) {
        this.id = id;
    }
    public String getUrl(int n) {
        return mUrl[n];
    }
    public void setUrl(int n, String str) {
        mUrl[n] = str;
    }
    public String getCreater(int n) {
        return mCreater[n];
    }
    public void setCreator(int n, String str) {
        mCreater[n] = str;
    }
}
