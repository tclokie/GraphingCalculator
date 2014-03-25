public class TokenStack
{
	static final int SIZE = 100;

	// instance fields
	private Token[] stack;
	private int top = -1; // 0 is the first element

	// constructor
	public TokenStack()
	{
		stack = new Token[SIZE];
	}

	public TokenStack (int cap)			// this constructor creates a stack of a user-defined capacity.
	{
		stack = new Token[cap] ;
	}

	// interface (public methods)
	public boolean empty()
	{
		return top == -1;
	}

	public Token pop()
	{
		Token re = null;
		if (top != -1)
		{
			re = stack[top];
			stack[top] = null;
			top--;
		}
		return re;
	}

	public Token peek()
	{
		if (top != -1) return stack[top];
		else return null;
	}

	public Token push (Token input)
	{
		Token re = null;

		if (input != null && !full())
		{
			top++;
			stack[top] = input;
			re = input;
		}

		return re;
	}

	private boolean full()
	{
		return top >= SIZE-1;
	}

	public String toString()
	{
		String str = "[";
		for(int k = 0; k <= top; k++)
		{
			str += stack[k].text;
			if (k < top) str += ", " ;
		}
		return str + "]";
	}
}