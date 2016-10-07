package Assignment;

public class ExtendedEuclid {
	
	public static void gcd_extended(int a, int b){
		long x = 0, y = 1, lastx = 1, lasty = 0, temp;
        while (b != 0)
        {
            long q = a / b;
            int r = a % b;
 
            a = b;
            b = r;
 
            temp = x;
            x = lastx - q * x;
            lastx = temp;
 
            temp = y;
            y = lasty - q * y;
            lasty = temp;            
        }
        System.out.println("Roots  x : "+ lastx +" y :"+ lasty);
    }	    

	public static void main(String[] args) {
		int a = 120, b = 23;
	    System.out.println("GCD("+a+", "+b+"): ");
	    gcd_extended(a,b);
	    a = 10; b = 35;
	    System.out.println("GCD("+a+", "+b+"): ");
	    gcd_extended(a,b);
	    a = 31; b = 2;
	    System.out.println("GCD("+a+", "+b+"): ");
	    gcd_extended(a,b);

	}

}
