package Exception;

@SuppressWarnings("serial")
public class RestrictionException extends Exception {
	
	public RestrictionException(String message) {
	    super(message);
	}
	@Override
	public String getMessage() {
	    String message = super.getMessage();
	    return message.replace("Exception.RestrictionException: ", "");
	}
}
