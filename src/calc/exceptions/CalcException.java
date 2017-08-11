package calc.exceptions;

public class CalcException extends Exception {


	public CalcException(String mensagem, Exception e) {
		super(mensagem, e);
	}

}
