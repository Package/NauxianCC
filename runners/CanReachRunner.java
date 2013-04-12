import java.lang.reflect.Method;
import java.util.Random;

import org.powerbat.executor.Result;
import org.powerbat.interfaces.Manifest;
import org.powerbat.interfaces.Runner;

@Manifest(category = "Logic", instructions = "Return true if you can reach the goal (Without going over) by adding large sticks (worth 5) and small sticks (worth 1). \nYou only have a number of each of these given in the parameters.", version = 1.0, className = "CanReach", level = 3, method = "boolean canReach(int small, int large, int goal)")
public class CanReachRunner extends Runner {

	@Override
	public Result[] getResults(Class<?> clazz) {
		try {
			final Method method = clazz.getMethod("canReach", int.class, int.class, int.class);
			final Random ran = new Random();
			final Result[] results = new Result[10];
			for (int i = 0; i < 10; i++) {
				final int small = ran.nextInt(15) + 5;
				final int large = ran.nextInt(5) + 5;
				final int goal = (small + large * 5) + 2 + ran.nextInt(6);
				results[i] = new Result(method.invoke(clazz.newInstance(), small, large, goal), (goal > small + large * 5) ? false
						: goal % 5 <= small, small, large, goal);
			}
			return results;
		} catch (Exception e) {
			return new Result[] {};
		}
	}
}
