package bms.helper.http;

public class SendException extends Exception{
    private String message="";
	private String cause="";
	public SendException(String cause,String mes){
		super();
		this.message=mes;
		this.cause=cause;
	}
	@Override
	public String getMessage() {
		return this.message;
	}

	@Override
	public Throwable getCause() {
		return super.getCause();
	}
	@Override
	public String toString() {
		return "Cause:"+cause+"\nMessage:"+message;
	}
}
