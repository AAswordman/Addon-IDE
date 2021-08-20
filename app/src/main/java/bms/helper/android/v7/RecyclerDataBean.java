package bms.helper.android.v7;

public class RecyclerDataBean {

    private String Index;
	private String text="";

    public String getIndex () {
        return Index;
    }
	public void setText(String p1){
		text=p1;
	}
    public void setIndex (String index) {
        Index = index;
    }

    public RecyclerDataBean (String index) {
        Index = index;
    }

    public RecyclerDataBean () {
    }
	public String getText(){
		return text;
	}
    @Override
    public String toString () {
        return "RecyclerDataBean{" +
			"Index='" + Index + '\'' +
			'}';
    }
}
