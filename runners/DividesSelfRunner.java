import java.lang.reflect.Method;
import java.util.Random;

import org.powerbat.executor.Result;
import org.powerbat.interfaces.Runner;

public class DividesSelfRunner extends Runner {

	@Override
	public Result[] getResults(Class<?> clazz) {
		try {
			final Method method = clazz.getMethod("canDivide", int.class);
			final Random ran = new Random();
			final Result[] results = new Result[10];
			for (int i = 0; i < results.length; i++) {
				int selected = ran.nextInt(501) + ran.nextInt(500);
				results[i] = new Result(method.invoke(clazz.newInstance(), selected), canDivide(selected), selected);
			}
			return results;
		} catch (Exception e) {
			return new Result[]{};
		}
	}

	private boolean canDivide(int n) {
		if (n < 100)
			if (n % 10 == 0)
				return n % (n / 10) == 0;
			else
				return n % (n / 10) == 0 && n % (n % 10) == 0;
		else if (n % 10 == 0)
			return false;
		return n % (n / 100) == 0 && n % ((n / 10) % 10) == 0 && n % (n % 10) == 0;
	}
}
