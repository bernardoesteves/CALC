package calc.exceptions;

public class CalcException extends Exception {

	private static final long serialVersionUID = -7609177080514770993L;

	public CalcException(String mensagem, Exception e) {
		super(mensagem, e);
	}

}
