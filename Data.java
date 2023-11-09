package socket;

public class Data {
	private String request;
	//Answer, Error
	private String returnType = null;
	private String errorMessage =null;
	private int response;
	
	public void setResponse(int n) {
		this.response = n;
	}
	
	public void setErrorMessage(String s) {
		this.errorMessage = s;
	}
	
	public void setRequest(String s) {
		this.request = s;
	}
	public String getRequest() {
		if (this.request != null) return this.request;
		
		return "Not Exist Request";
	}
	
	public void setReturnType(String s) {
		this.returnType = s;
	}
	
	public String responseToClinet() {
		if (this.returnType.equals("Answer")) {
			if(this.errorMessage!=null) {
				return this.errorMessage;
			}
			return this.returnType + " : " + this.response;
		}
		 
		return this.returnType + " : "+ this.errorMessage;
	}
	
	@Override
	public String toString() {
		return this.request;
	}
}
