import java.lang.reflect.Method;
import java.util.Arrays;
import java.util.Random;

import org.powerbat.executor.Result;
import org.powerbat.interfaces.Manifest;
import org.powerbat.interfaces.Runner;

@Manifest(category = "Logic", instructions = "Return the array that has a larger sum out of the 2.\n{1, 3, 2}, {5, 3, 2} -> {5, 3, 2}\n{1, 5, 6}, {4, 8, 0} -> {1, 5, 6}", version = 1.0d, className = "LargeArray", level = 1, method = "int[] largerArray(int[] a, int[] b)")
public class LargeArrayRunner extends Runner {

	@Override
	public Result[] getResults(Class<?> clazz) {
		try {
			final Method method = clazz.getMethod("largerArray", int[].class,
					int[].class);
			final Result[] results = new Result[10];
			final Random random = new Random();
			for (int i = 0; i < results.length; i++) {
				final int count = random.nextInt(5);
				final int[] a = new int[count];
				for (int j = 0; j < a.length; j++) {
					a[j] = random.nextInt(50);
				}
				final int[] b = new int[count];
				for (int j = 0; j < b.length; j++) {
					b[j] = random.nextInt(50);
				}
				results[i] = new Result(Arrays.toString((int[]) method.invoke(
						clazz.newInstance(), a, b)), Arrays.toString(larger(a,
						b)), Arrays.toString(a), Arrays.toString(b));
			}
			return results;
		} catch (Exception e) {
			return new Result[]{};
		}
	}

	private int[] larger(int[] a, int[] b) {
		int sum = 0;
		for (int i = 0; i < Math.max(a.length, b.length); i++) {
			sum += a[i] - b[i];
		}
		return sum > 0 ? a : b;
	}
}