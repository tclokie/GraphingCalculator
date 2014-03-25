public class Token
{
	boolean type;
	boolean variable;
	double value;
	String text;
	boolean error = false;

	static final boolean NUMBER = true;
	static final boolean OPERATOR = false;
	static final int FUNCTION = 4;

	public Token(String s)
	{
		text = s.toLowerCase();

		if (s.equals("+") || s.equals("-"))
		{
			type = OPERATOR;
			value = 1;
		}

		else if (s.equals("*") || s.equals("/"))
		{
			type = OPERATOR;
			value = 2;

		}

		else if (s.equals("(") || s.equals(")"))
		{
			type = OPERATOR;
			value = 0;
		}

		else if (s.equals("^"))
		{
			type = OPERATOR;
			value = 3;
		}

		else if (s.equals("log") || s.equals("ln") || s.equals("sqrt") || s.equals("abs") || s.equals("int") || s.equals("floor") || s.equals("sgn")
		|| s.equals("sin") || s.equals("cos") || s.equals("tan") ||  s.equals("cot") || s.equals("csc") || s.equals("sec")
		|| s.equals("arcsin") || s.equals("arccos") || s.equals("arctan") || s.equals("arccot") || s.equals("arccsc") || s.equals("arcsec"))
		{
			type = OPERATOR;
			value = 4;
		}

		else if (s.equals("pi"))
		{
			type = NUMBER;
			value = Math.PI;
			variable = false;
		}

		else if (s.equals("e"))
		{
			type = NUMBER;
			value = Math.E;
			variable = false;
		}

		else if (text.equals("a") || text.equals("b") || text.equals("c") || text.equals("d") || text.equals("x") ||
				text.equals("y1") || text.equals("y2") || text.equals("y3") || text.equals("y4") || text.equals("y5") ||
				text.equals("y6") || text.equals("y7") || text.equals("y8") || text.equals("y9") || text.equals("y10") )
		{
			type = NUMBER;
			variable = true;
		}

		else // it's a number
		{
			try // Tries to parse it as a double
			{
				value = Double.parseDouble(s);
				type = NUMBER;
				variable = false;
			}
			catch (NumberFormatException e)
			{
				System.err.println("Parsing error.");
				text = null;
				error = true;
			}
		}
	}

	public Token (Token t) // alternate constructor: duplicates given token
	{
		type = t.type;
		value = t.value;
		variable = t.variable;
		text = t.text;
	}
}