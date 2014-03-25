import java.util.*;
import java.awt.*;
import java.awt.event.*;
/*
	domain restrictions?
	calculus functions
*/

public class Calculator extends Panel implements KeyListener, MouseListener
{
	Scanner s = new Scanner(System.in);
	double xMin, xMax, yMin, yMax, xScale, yScale;
	Memory memory;

	Frame frame;

	public Calculator()
	{
		memory = new Memory();

		xMin = yMin = -10;
		xMax = yMax = 10;
		xScale = yScale = 1;

		frame = new Frame();
		frame.setTitle("TC-95");
		frame.setLocation(50,100); // X,Y (ints)
		frame.setSize(600,600); // X,Y (ints)
		frame.setResizable(false);
		frame.setVisible(true);
		frame.add(this);

		addKeyListener(this);
		addMouseListener(this);

		// run program
		while(true) menu();
	}

	public void menu()
	{
		System.out.println("What would you like to do?");
		System.out.println("(1) Evaluate");
		System.out.println("(2) Set/edit displayed functions");
		System.out.println("(3) Clear displayed functions");
		System.out.println("(4) Edit display settings");
		System.out.println("(5) Store values in memory");
		System.out.println("(0) Exit");

		int selection = s.nextInt();
		s.nextLine();

		switch (selection)
		{
			case 1:
				evaluate();
				break;
			case 2:
				editFunctions();
				repaint();
				break;
			case 3:
				clearFunctions();
				repaint();
				break;
			case 4:
				editDisplaySettings();
				repaint();
				break;
			case 5:
				editMemory();
				repaint();
				break;
			case 0:
				System.exit(0);
				break;
		}

		System.out.println("\n=========================================================");
	}

	public void evaluate()
	{
		System.out.println("\nWould you like to use any of the displayed functions?");
		char ans = 'N';

		String temp = s.nextLine().toUpperCase();
		if (temp.length() > 0) ans = temp.charAt(0);

		if (ans == 'Y')
		{
			for (int i = 0; i < 10; i++)
			{
				if (memory.y[i] != null && memory.y[i].toString != null) System.out.println("Y"+(i+1)+" = "+memory.y[i].toString);
				else System.out.println("Y"+(i+1)+" = ");
			}

			System.out.println("Select a function. (1-10)");
			try
			{
				int choice = Integer.parseInt(s.nextLine()) - 1;

				if (memory.y[choice] != null && memory.y[choice].valid)
				{
					System.out.println("\nFor what value of X would you like to evaluate this function?");

					double x;

					try
					{
						String foo = s.nextLine();
						if (foo.length() < 1) x = 0;
						else
						{
							Function newFun = new Function(foo);
							x = newFun.evaluate(1);
						}
					}
					catch (Exception e)
					{
						x = 0;
					}

					System.out.println("\nY"+(choice+1)+"("+x+") = " + memory.y[choice].evaluate(x));
				}
			}
			catch (Exception e)
			{
				System.err.println("Invalid selection.");
			}
		}

		else
		{
			System.out.print("\nPlease define the function: Y = ");
			String funny = s.nextLine();
			Function fun = new Function(funny);
			System.out.println();

			if (fun != null && funny.length() > 0 && fun.valid)
			{
				System.out.println("\nFor what value of X would you like to evaluate this function?");
				double x;

				try
				{
					String foo = s.nextLine();
					if (foo.length() < 1) x = 0;
					else
					{
						Function newFun = new Function(foo);
						x = newFun.evaluate(1);
					}
				}
				catch (Exception e)
				{
					x = 0;
				}

				System.out.println("\nY("+x+") = " + fun.evaluate(x));
			}
		}
	}

	public void editFunctions()
	{
		System.out.println();

		for (int i = 0; i < 10; i++)
		{
			if (memory.y[i] != null && memory.y[i].toString != null) System.out.println("Y"+(i+1)+" = "+memory.y[i].toString);
			else System.out.println("Y"+(i+1)+" = ");
		}

		System.out.println("Select a function to edit. (1-10)");
		try
		{
			int choice = Integer.parseInt(s.nextLine()) - 1;


			System.out.print("\nPlease define the function: Y"+(choice+1)+" = ");
			memory.y[choice] = new Function(s.nextLine());
			System.out.println();
		}
		catch (Exception e)
		{
			System.err.println("Invalid selection.");
		}
	}

	public void clearFunctions()
	{
		System.out.println();

		for (int i = 0; i < 10; i++)
		{
			if (memory.y[i] != null && memory.y[i].toString != null) System.out.println("Y"+(i+1)+" = "+memory.y[i].toString);
			else System.out.println("Y"+(i+1)+" = ");
		}

		System.out.println("Select a function to clear. (1-10)");
		try
		{
			int choice = Integer.parseInt(s.nextLine()) - 1;

			memory.y[choice] = null;
			System.out.println();
		}
		catch (Exception e)
		{
			System.err.println("Invalid selection.");
		}
	}

	public void paint (Graphics g)
	{
		g.setColor(Color.BLACK);
		double last, current;

		// axes
		int xAxis = (int)(yMax*600/(yMax-yMin)); // y-value of x-axis
		int yAxis = (int)(-xMin*600/(xMax-xMin)); // x-value of y-axis
		g.drawLine(0, xAxis, 600, xAxis);
		g.drawLine(yAxis, 0, yAxis, 600);

		// tick marks
		for (double i = xScale; i <= xMax; i += xScale) g.drawLine((int)(yAxis+i*600/(xMax-xMin)),
									(int)(xAxis-1), (int)(yAxis+i*600/(xMax-xMin)), (int)(xAxis+1));
		for (double i = -xScale; i >= xMin; i -= xScale) g.drawLine((int)(yAxis+i*600/(xMax-xMin)),
									(int)(xAxis-1), (int)(yAxis+i*600/(xMax-xMin)), (int)(xAxis+1));
		for (double i = yScale; i <= yMax; i += yScale) g.drawLine((int)(yAxis-1), (int)(xAxis-i*600/(yMax-yMin)),
									(int)(yAxis+1), (int)(xAxis-i*600/(yMax-yMin)));
		for (double i = -yScale; i >= yMin; i -= yScale) g.drawLine((int)(yAxis-1), (int)(xAxis-i*600/(yMax-yMin)),
									(int)(yAxis+1), (int)(xAxis-i*600/(yMax-yMin)));

		// functions
		g.setColor(Color.RED);

		for (int i = 0; i < memory.y.length; i++)
		{
			if (memory.y[i] != null && memory.y[i].valid)
			{
				last = xAxis - memory.y[i].evaluate((-yAxis)*(xMax-xMin)/600) * 600/(yMax-yMin);

				for (int a = 0; a < 600; a++)
				{

					// draw line from two adjacent points, IF POSSIBLE
					current = xAxis - memory.y[i].evaluate((a+1 - yAxis)*(xMax-xMin)/600) * 600/(yMax-yMin);

					if (!Double.isNaN(last) && !Double.isNaN(current) && !Double.isInfinite(last) && !Double.isInfinite(current))
						g.drawLine(a, (int)(last+0.5), a+1, (int)(current+0.5));

					//g.drawLine(a, (int)(last+0.5), a, (int)(last+0.5));

					last = current;

			//		try { Thread.sleep(1); } catch(InterruptedException ex) { Thread.currentThread().interrupt(); }
				}
			}
		}
	}

	public void editDisplaySettings()
	{	// edit the panel variables
		System.out.println("\nWhich of the following would you like to edit?\n(1) XMin = "+xMin+"\n(2) XMax = "+xMax+"\n(3) XScale = "+xScale+"\n(4) YMin = "+yMin+"\n(5) YMax = "+yMax+"\n(6) YScale = "+yScale);
		int input = s.nextInt();
		s.nextLine();

		System.out.println("What would like to set it to?");
		Function newFun = new Function(s.nextLine());
		double temp = newFun.evaluate(1);

		switch (input)
		{
			case 1:
				if (temp < xMax) xMin = temp;
				else System.err.println("Nope.");
				break;
			case 2:
				if (temp > xMin) xMax = temp;
				else System.err.println("Nope.");
				break;
			case 3:
				if (temp != 0) xScale = Math.abs(temp);
				else System.err.println("Nope.");
				break;
			case 4:
				if (temp < yMax) yMin = temp;
				else System.err.println("Nope.");
				break;
			case 5:
				if (temp > yMin) yMax = temp;
				else System.err.println("Nope.");
				break;
			case 6:
				if (temp != 0) yScale = Math.abs(temp);
				else System.err.println("Nope.");
				break;
		}
	}

	public void editMemory ()
	{
		System.out.println("\n a = "+memory.a+"\n b = "+memory.b+"\n c = "+memory.c+"\n d = "+memory.d);
		System.out.println("Which would you like to edit?");
		String str = s.nextLine();

		if (str.length() > 0)
		{
			System.out.println("What would you like to set it to?");

			try
			{
				Function newFun = new Function(s.nextLine());
				double temp = newFun.evaluate(1);

				switch (str.charAt(0))
				{
					case 'a': memory.a = temp;
						break;
					case 'b': memory.b = temp;
						break;
					case 'c': memory.c = temp;
						break;
					case 'd': memory.d = temp;
						break;
				}
			}
			catch (Exception e)
			{
				System.out.println("Didn't work.");
			}
		}
	}

	public static void main (String[] args)
	{
		Calculator calc = new Calculator();
	}

	public void keyPressed (KeyEvent ke)
	{
		int keyCode = ke.getKeyCode();

		switch(keyCode)
		{
			case KeyEvent.VK_0:		// centers on the origin, resets the axes (though not the tick scale)
				xMin = yMin = -10;
				xMax = yMax = 10;
				repaint();
				break;
			case KeyEvent.VK_C:		// centers on the origin without changing the axes
				double temp = xMin;
				xMin = (xMin-xMax)/2;
				xMax = (xMax-temp)/2;
				temp = yMin;
				yMin = (yMin-yMax)/2;
				yMax = (yMax-temp)/2;
				repaint();
				break;
			case KeyEvent.VK_UP:
				yMin += yScale;
				yMax += yScale;
				repaint();
				break;
			case KeyEvent.VK_DOWN:
				yMin -= yScale;
				yMax -= yScale;
				repaint();
				break;
			case KeyEvent.VK_RIGHT:
				xMin += xScale;
				xMax += xScale;
				repaint();
				break;
			case KeyEvent.VK_LEFT:
				xMin -= xScale;
				xMax -= xScale;
				repaint();
				break;
			case KeyEvent.VK_PAGE_UP:
				if (xMax - xMin > 2*xScale && yMax - yMin > 2*yScale)
				{
					xMin += xScale;
					xMax -= xScale;
					yMin += yScale;
					yMax -= yScale;
					repaint();
				}
				else
				{
					System.err.println("Can't zoom in any farther.\n");
				}
				break;
			case KeyEvent.VK_PAGE_DOWN:
				xMin -= xScale;
				xMax += xScale;
				yMin -= yScale;
				yMax += yScale;
				repaint();
				break;
			case KeyEvent.VK_ESCAPE:
				System.exit(0);
				break;
		}
	}

	public void keyReleased (KeyEvent ke){}
	public void keyTyped (KeyEvent ke){}
	public void mouseClicked(MouseEvent me){}
	public void mouseEntered(MouseEvent me){}
	public void mouseExited(MouseEvent me){}
	public void mousePressed(MouseEvent me) // centers the point that is clicked.
	{
		if (frame.isActive())
		{
			Point p = me.getPoint();

			double dx = (p.x - 300)*(xMax-xMin)/600;
			double dy = (300 - p.y)*(yMax-yMin)/600;

			xMin += dx;
			xMax += dx;
			yMin += dy;
			yMax += dy;

			repaint();
		}
	}
	public void mouseReleased(MouseEvent me){}
}
