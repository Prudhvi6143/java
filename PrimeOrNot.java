import java.util.Scanner;
class PrimeOrNot{
	public static void main(String args[])
          {
		int n,i,count=0;
		Scanner sc = new Scanner(System.in);
		n =sc.nextInt();
		for(i=1; i<=n; i++)
		{
			if(n%i==0){
				count++;
                              }
		}
		 if (count == 2)
            	  System.out.println("Prime");
       		 else
           	  System.out.println("Not Prime");
	}
}