import java.util.Scanner;
class TwoArray
{
	public static void main(String args[])
	{
		int a[][]=new int[2][3];
		Scanner sc=new Scanner(System.in);
		System.out.print("Enter array elements");
		for(int i=0;i<a.length;i++)
		{
			for(int j=0;j<a[i].length;j++)
			{
				a[i][j]=sc.nextInt();
			}
		}
		System.out.print("elemets are");
		for(int i=0;i<a.length;i++){
			for(int j=0;j<a[i].length;j++){
				System.out.print(a[i][j]+" ");
				}
			}
		System.out.println(" ");
	}
}