public class TokenQueue
{
	static final int SIZE = 100;

	// instance fields
	private Token[] queue;
	private int head = 0; // Next out
	private int tail = 0; // Next in

	// constructor
	public TokenQueue()
	{
		queue = new Token[SIZE];
	}

	public TokenQueue(int cap)
	{
		queue = new Token[cap];

	}

	// interface (public methods)
	public boolean empty()
	{
		return (queue[head] == null);
	}

	private boolean full()
	{
		return (queue[tail] != null);
	}

	public Token poll()
	{
		Token re = null;
		if (!empty())
		{
			re = queue[head];
			queue[head] = null;
			head++;
			if (head >= queue.length) head -= queue.length;
		}
		return re;
	}

	public Token remove()
	{
		Token re = queue[head];
		queue[head] = null;
		head++;
		if (head >= queue.length) head -= queue.length;
		return re;
	}

	public Token peek()
	{
		if (!empty()) return queue[head];
		else return null;
	}

	public Token element()
	{
		return queue[head];
	}

	public Token offer (Token input)
	{
		Token re = null;

		if (input != null && !full())
		{
			queue[tail] = input;
			re = input;
			tail++;
			if (tail >= queue.length) tail -= queue.length;
		}

		return re;
	}

	public String toString()
	{
		String temp = "[";
		if (!empty())
		{
			for (int i = head; i != tail ; i = (i + 1) % queue.length)
			{
				if (i != head) temp += ", ";
				temp += queue[i].text;
			}
		}
		return temp + "]";
	}
}