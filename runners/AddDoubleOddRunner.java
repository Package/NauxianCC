import java.lang.reflect.Method;
import java.util.ArrayList;
import java.util.Random;

import org.powerbat.executor.Result;
import org.powerbat.interfaces.Manifest;
import org.powerbat.interfaces.Runner;

@Manifest(category = "Logic", instructions = "Add parameters x and y, but if x is odd, return double the sum. \nUse the \"addOdd\" method to perform this.", version = 1.0, className = "AddDoubleOdd", method = "int addOdd(int x, int y)", level = 1)
public class AddDoubleOddRunner extends Runner {

	private final Random random = new Random();

	@Override
	public Result[] getResults(Class<?> c) {
		try {
			final Object instance = c.newInstance();
			final Method m = c.getMethod("addOdd", int.class, int.class);
			final int count = 10;
			final ArrayList<Result> results = new ArrayList<Result>();
			for (int i = 0; i < count; i++) {
				final int x = -3000 + random.nextInt(6000);
				final int y = -3000 + random.nextInt(6000);
				final int correct = (x + y) * (x % 2 == 0 ? 1 : 2);
				final int answer = (Integer) m.invoke(instance, x, y);

				results.add(new Result(answer, correct, x, y));
			}
			return results.toArray(new Result[results.size()]);
		} catch (Exception e) {
			e.printStackTrace();
		}
		return new Result[] {};
	}
}
