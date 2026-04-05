import java.util.Scanner;
class AddTwoNumbers
  {
     public static void main(String args[])
      {
        int num1,num2,sum;
        Scanner p = new Scanner(System.in);
	System.out.println("Enter First No: ");
	num1= p.nextInt();
	System.out.println("Enter second No: ");
	num2= p.nextInt();
	sum = num1+num2;
	System.out.println("The Sum is: "+sum);
      }
  }