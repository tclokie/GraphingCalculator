import java.util.*;

public class Function
{
	String toString;
	private String realString;
	public boolean valid;
	TokenQueue input;
	TokenQueue output;

	Memory memory;

	public Function(String inputString)
	{
		valid = true;
		toString = inputString;
		realString = "("+inputString.toLowerCase()+")";
		realString = realString.replace(" ", "");
		realString = realString.replace("(-", "(0-");
		realString = realString.replace("(+", "(");

		tokenize();
		shuntingYard();
	}

	public Function(TokenQueue q)
	{
		valid = true;
		output = q;
	}

	public double evaluate (double x)
	{
//		System.out.println(output.toString());

		double y = 0; // the answer
		TokenStack expr = new TokenStack(); // the temporary stack for this evaluation; tokens to be returned to Output

		Token temp;

		while (!output.empty())
		{
			temp = output.poll();
			expr.push(temp);
			input.offer(temp);
		}
		while (!input.empty())
		{
			output.offer(input.poll());
		}

		try
		{
			y = calculate(expr, x);
		}
		catch (Exception e)
		{
			System.err.println("Could not evaluate function.");
			valid = false;
		}

//		System.out.println(output.toString());

		return y;
	}

	private double calculate (TokenStack expr, double x)
	{
		double y = 0;

		Token t = expr.pop();

		if (expr.empty())
		{
			if (!t.variable) y = t.value; // If it's a number, return the number
			else
			{
				y = useVariable(x, t.text);
			}
		}

		else if (t.value == Token.FUNCTION) // function
		{
//			if (t.text != null && t.text.equals("deriv")) y = derivative(expr, x);

//			else
			{
				double a;

				if (expr.peek().type == Token.OPERATOR)
				{
					a = calculate(expr, x);
				}
				else // it's a number
				{
					Token temp = expr.pop();
					if (temp.variable)
					{
						a = useVariable(x,temp.text);
					}
					else a = temp.value;
				}

				if (t.text.equals("sin")) y = Math.sin(a);
				else if (t.text.equals("cos")) y = Math.cos(a);
				else if (t.text.equals("tan")) y = Math.tan(a);
				else if (t.text.equals("log")) y = Math.log10(a);
				else if (t.text.equals("ln")) y = Math.log(a);
				else if (t.text.equals("arcsin")) y = Math.asin(a);
				else if (t.text.equals("arccos")) y = Math.acos(a);
				else if (t.text.equals("arctan")) y = Math.atan(a);
				else if (t.text.equals("sqrt")) y = Math.sqrt(a);
				else if (t.text.equals("abs")) y = Math.abs(a);
				else if (t.text.equals("int")) y = (double)((int)a);
				else if (t.text.equals("floor")) y = Math.floor(a);
				else if (t.text.equals("sgn")) y = Math.signum(a);
				else if (t.text.equals("cot")) y = Math.cos(a) / Math.sin(a);
				else if (t.text.equals("csc")) y = 1/Math.sin(a);
				else if (t.text.equals("sec")) y = 1/Math.cos(a);
				else if (t.text.equals("arccot")) y = Math.atan(1/a);
				else if (t.text.equals("arccsc")) y = Math.asin(1/a);
				else if (t.text.equals("arcsec")) y = Math.acos(1/a);
			}
		}

		else // operator
		{
			double a, b;

			if (expr.peek().type == Token.OPERATOR) // b
			{
				b = calculate(expr, x);
			}
			else
			{
				Token tempB = expr.pop();
				if (tempB.variable)
				{
					b = useVariable (x, tempB.text);
				}
				else b = tempB.value;
			}

			if (expr.peek().type == Token.OPERATOR) // a
			{
				a = calculate(expr, x);
			}
			else
			{
				Token tempA = expr.pop();
				if (tempA.variable)
				{
					a = useVariable(x, tempA.text);
				}
				else a = tempA.value;
			}

			if (t.text.equals("+"))	y = a + b;
			else if (t.text.equals("-")) y = a - b;
			else if (t.text.equals("*")) y = a * b;
			else if (t.text.equals("/")) y = a / b;
			else if (t.text.equals("^")) y = Math.pow(a, b);
		}

		return y;
	}

	private double useVariable(double x, String str)
	{
		if (str.equals("x")) return x;
		else if (str.equals("a")) return memory.a;
		else if (str.equals("b")) return memory.b;
		else if (str.equals("c")) return memory.c;
		else if (str.equals("d")) return memory.d;
		else if (str.equals("y1") && memory.y[0] != null && memory.y[0].valid) return memory.y[0].evaluate(x);
		else if (str.equals("y2") && memory.y[1] != null && memory.y[1].valid) return memory.y[1].evaluate(x);
		else if (str.equals("y3") && memory.y[2] != null && memory.y[2].valid) return memory.y[2].evaluate(x);
		else if (str.equals("y4") && memory.y[3] != null && memory.y[3].valid) return memory.y[3].evaluate(x);
		else if (str.equals("y5") && memory.y[4] != null && memory.y[4].valid) return memory.y[4].evaluate(x);
		else if (str.equals("y6") && memory.y[5] != null && memory.y[5].valid) return memory.y[5].evaluate(x);
		else if (str.equals("y7") && memory.y[6] != null && memory.y[6].valid) return memory.y[6].evaluate(x);
		else if (str.equals("y8") && memory.y[7] != null && memory.y[7].valid) return memory.y[7].evaluate(x);
		else if (str.equals("y9") && memory.y[8] != null && memory.y[8].valid) return memory.y[8].evaluate(x);
		else if (str.equals("y10") && memory.y[9] != null && memory.y[9].valid) return memory.y[9].evaluate(x);
		else return 0;
	}

	private void tokenize()
	{
		StringTokenizer st = new StringTokenizer(realString, "+-*^/()" , true);
		input = new TokenQueue();
		Token token;

		while (st.hasMoreElements() && valid)
		{
			token = new Token (st.nextToken());
			if (token.error) valid = false;
			else input.offer(token);
		}
	}

	private void shuntingYard()
	{
		output = new TokenQueue();
		TokenStack operators = new TokenStack();

		// parses "input" using algorithm

		while (!input.empty())
		{
			if (input.peek().type == Token.NUMBER)
			{
				output.offer(input.poll());
			}

			else if (input.peek().text.equals("("))
			{
				operators.push(input.poll());
			}

			else if (input.peek().text.equals(")"))
			{
				input.poll(); // remove the ")"

				boolean go = true;
				Token temp;

				while (!operators.empty() && go)
				{
					temp = operators.pop();

					if (temp.text.equals("("))
					{
						go = false;
						if (!operators.empty() && operators.peek().type == Token.OPERATOR && operators.peek().value == Token.FUNCTION)
						{
							output.offer(operators.pop()); // put the function into the queue, but not the "("
						}
					}

					else
					{
						output.offer(temp);
					}
				}
			}

			else // it is an operator/function
			{
				while (!operators.empty() && input.peek().value <= operators.peek().value)
				{
					output.offer(operators.pop());
				}

				operators.push(input.poll());
			}
		}

		while (!operators.empty()) // empty the remaining operators from the stack
		{
			output.offer(operators.pop());
		}
	}
}
/*	private double derivative (TokenStack expr, double x)
	{
		double dx = 1E-7;
		double slope = Double.NaN;

		double a, b, c;								 // a = y(x-dx), b = y(x), c = y(x+dx)
		a = b = c = calculate(expr, x);

		if (!Double.isNaN(a) && !Double.isNaN(b) && !Double.isNaN(c))
		{
			slope = (c - a) / (2 * dx);
		}
		return slope;
	}
*/

/*	public Function differentiate ()
	{
		TokenStack expr = new TokenStack();
		Token temp;

		while (!output.empty()) {
			temp = output.poll();
			expr.push(temp);
			input.offer(temp);
		}
		while (!input.empty()) {
			output.offer(input.poll());
		}

		TokenQueue derivative = findDerivative(expr);

		Function prime = new Function(derivative);
		return prime;
	}

	private TokenQueue findDerivative(TokenStack expr)
	{
		TokenQueue derivative = new TokenQueue();

		Token t = expr.pop();

		if (expr.empty())
		{
			Token d;

			if (t.text.equals("x")) d = new Token ("1"); // If it's a number, return the number
			else d = new Token ("1");

			derivative.offer(d);
		}

		else if (t.value == Token.FUNCTION) // function
		{
			// derivative = rest of stack, f', (rest of stack)', *
		}

		else // operator
		{
			// parse a, then parse b

			if (t.text.equals("+"))
			{
				// derivative = b', a', +;
			}
			else if (t.text.equals("-"))
			{
				// derivative = b', a', -;									// WATCH THE ORDER
			}
			else if (t.text.equals("*"))
			{
				// derivative = b', a, *, b, a', *, +;
			}
			else if (t.text.equals("/"))
			{
				// derivative = b', a, *, b, a', *, -, a, 2, ^ /;			// WATCH THE ORDER
			}
			else if (t.text.equals("^"))
			{
				// derivative = b, a, ^, a', b, abs, ln, *, a, b, /, +, *;	// WATCH THE ORDER
			}
		}

		return derivative;
	}
*/