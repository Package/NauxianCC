import java.lang.reflect.Method;
import java.util.ArrayList;
import java.util.Random;

import org.powerbat.executor.Result;
import org.powerbat.interfaces.Manifest;
import org.powerbat.interfaces.Runner;

@Manifest(category = "Logic", instructions = "Given parameters boolean lowGas and boolean lowOil, using the getWarning method, return 0 if you have no issues, 1 if you have lowOil, 2 if you have lowGas, and 3 if you have both.", version = 1.0, className = "CarTrouble", level = 1, method = "int getWarning(boolean lowOil, boolean lowGas)")
public class CarTroubleRunner extends Runner {

	@Override
	public Result[] getResults(Class<?> classToTest) {
		try {
			final Random random = new Random();
			final Object instance = classToTest.newInstance();
			final Method method = classToTest.getMethod("getWarning", boolean.class, boolean.class);
			final ArrayList<Result> results = new ArrayList<Result>();

			final ArrayList<boolean[]> sets = new ArrayList<boolean[]>();
			sets.add(new boolean[] { true, true });
			sets.add(new boolean[] { true, false });
			sets.add(new boolean[] { false, true });
			sets.add(new boolean[] { false, false });
			while (!sets.isEmpty()) {
				int j = random.nextInt(sets.size());
				boolean[] b = sets.remove(j);
				results.add(new Result((b[0] ? 1 : 0) + (b[1] ? 2 : 0), method.invoke(instance, b[0], b[1]), b[0], b[1]));
			}
			return results.toArray(new Result[4]);
		} catch (Exception e) {
			return new Result[] {};
		}

	}
}