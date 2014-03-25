public class Memory
{
	public static Function[] y;
	public static Function[] derivatives;

	public static double a;
	public static double b;
	public static double c;
	public static double d;

	public Memory ()
	{
		y = new Function[10];
		derivatives = new Function[10];
		a = 0;
		b = 0;
		c = 0;
		d = 0;
	}
}