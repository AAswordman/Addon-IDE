package bms.helper.http;
import java.util.regex.Pattern;
import java.util.regex.Matcher;

public class UrlStringFactory {
	protected String url;
	public UrlStringFactory(String x){
		url=x;
	}
    public int GetParameterInt(String nr){
		String pattern = "(?<="+nr+"=)[0-9]*";
		Pattern r = Pattern.compile(pattern);
		Matcher m = r.matcher(url);
		if(m.find()){
			return Integer.parseInt(m.group(0));
		}
		return 0;
	}
	public String GetParameter(String nr){
		String pattern = "[?&]"+nr+"=([^&#]+)";
		Pattern r = Pattern.compile(pattern);
		Matcher m = r.matcher(url);
		if(m.find()) return m.group(1);
		return null;
	}
    public String Pattern(String a,String b){
		Pattern r = Pattern.compile("(?<="+a+").*?(?="+b+")");
		Matcher m = r.matcher(url);
		if(m.find()){
			return m.group(0);
		}
		return "";
	}
    public UrlStringFactory SetParameter(String a,String b){
		String Parameter=GetParameter(a);
		if(Parameter==null){
			if(url.indexOf("?")!=-1){
				url+="&"+a+"="+b;
			}else{
				url+="?"+a+"="+b;
			}
		}else{
			url=url.replace(Parameter,b);
		}
        return this;
	}
	public UrlStringFactory DeleteParameter(String a){
		String s=GetParameter(a);
		if(s!=null){
			url=url.replace("\\?"+a+"="+s+"&","?").
			replace("&"+a+"="+s,"");
		}
        return this;
	}
	@Override
	public String toString() {
		return url;
	}
	
}
