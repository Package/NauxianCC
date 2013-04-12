import java.lang.reflect.Method;
import java.util.Arrays;
import java.util.Random;

import org.powerbat.executor.Result;
import org.powerbat.interfaces.Manifest;
import org.powerbat.interfaces.Runner;

@Manifest(category = "Logic", instructions = "Given 3 ints, return true if the difference between small and medium is the same as the difference between medium and large.\nThough a problem does arise, which of a, b and c is the largest? The smallest?", version = 1.0, className = "EvenlySpaced", level = 3, method = "boolean spaced(int a, int b, int c)")
public class EvenlySpacedRunner extends Runner {

	@Override
	public Result[] getResults(Class<?> classToTest) {
		try {
			final Method method = classToTest.getMethod("spaced", int.class, int.class, int.class);
			final Random ran = new Random();
			final Result[] results = new Result[10];
			for (int i = 0; i < 10; i++) {
				final int a = ran.nextInt(10), b = a + (-ran.nextInt(2) + ran.nextInt(4)), c = b + (-ran.nextInt(2) + ran.nextInt(4));
				results[i] = new Result(method.invoke(classToTest.newInstance(), a, b, c), spaced(a, b, c), Arrays.toString(new int[] { a, b, c }));
			}
			return results;
		} catch (Exception e) {
			return new Result[] {};

		}
	}

	private boolean spaced(int a, int b, int c) {
		int diff1 = 0;
		int diff2 = 0;
		int diff3 = 0;

		if (a == b && a == c) {
			return true;
		}

		if (a == b || b == c || a == c) {
			return false;
		}
		diff1 = Math.abs(a - b);
		diff2 = Math.abs(a - c);
		diff3 = Math.abs(b - c);

		if (diff1 == diff2) {
			return true;
		}
		if (diff1 == diff3) {
			return true;
		}
		if (diff2 == diff3) {
			return true;
		}

		return false;
	}
}