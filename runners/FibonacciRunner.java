import java.lang.reflect.Method;
import java.util.Random;

import org.nauxiancc.executor.Result;
import org.nauxiancc.interfaces.Runner;

public class FibonacciRunner extends Runner {

	@Override
	public Result[] getResults(Class<?> classToTest) {
		try {
			final Method method = classToTest.getMethod("fibonacci", int.class);
			final Random ran = new Random();
			final Result[] results = new Result[10];
			for (int i = 0; i < 10; i++) {
				final int num = ran.nextInt(30);
				results[i] = new Result(method.invoke(classToTest.newInstance(), num), fibonacci(num), num);
			}
			return results;
		} catch (Exception e) {
			return new Result[] {};
		}
	}
	
	public FibonacciRunner() {
		cache.add(0);
		cache.add(1);
	}
	
	private List<Integer> cache = new ArrayList<Integer>();

	public int fibonacci(int n) {
		if (cache.length() > n) {
			return n < 0 ? 0 : cache.get(n);
		}
		int ret = fibonacci(n - 1) + fibonacci(n - 2);
		if (cache.length() == n) cache.add(ret);
		return ret;
	}
}
