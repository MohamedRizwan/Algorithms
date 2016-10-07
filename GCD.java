package Assignment;

public class GCD {
	public static void main(String args[]) // entry point from OS
	{
		int a = 25, b = 35;
		int sign_a = 1, sign_b = 1;
		int[] ans = new int[3];

		
		/* Check for input of two zeros, and print error */

		if ((a == 0) && (b == 0)) {
			System.out.println("Both inputs are Zero");
			System.exit(1);
		}

		/*
		 * If a and/or b is negative, then track this information for later
		 * output, because the extendedEuclid function expects nonnegative
		 * input.
		 */
		if (a < 0) {
			sign_a = -1;
			a = Math.abs(a);
		}
		if (b < 0) {
			sign_b = -1;
			b = Math.abs(b);
		}

		ans = ExtendedEuclid(a, b);
		System.out.println("gcd(" + a * sign_a + "," + b * sign_b + ") = " + ans[0]);
		System.out.print(ans[0] + " = (" + sign_a * ans[1] + ") (" + sign_a * a + ")");
		System.out.println(" + (" + sign_b * ans[2] + ") (" + sign_b * b + ")");

	}

	public static int[] ExtendedEuclid(int a, int b)
	{
		int[] ans = new int[3];
		int q;

		if (b == 0) { /* If b = 0, then done */
			ans[0] = a;
			ans[1] = 1;
			ans[2] = 0;
		} else { /* Otherwise, make a recursive function call */
			q = a / b;
			ans = ExtendedEuclid(b, a % b);
			int temp = ans[1] - ans[2] * q;
			ans[1] = ans[2];
			ans[2] = temp;
		}
		return ans;
	}
}
