package stac.discriminer.test;

public class test1 {
	public static int n = 10;
	public static void main(String[] args) {
		// TODO Auto-generated method stub
		for(int i = 0; i < n; i++)
			simpleClass(i);
		test2 t2 = new test2();
		for(int i = 0; i < n; i++)
			t2.simpleClass_2(i);
	}
	public static int simpleClass(int n)
	{
		int sum = 0;
		for(int i = 0; i < n ; i++)
			sum += i;
		return sum;
	}
}
