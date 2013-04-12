import java.lang.reflect.Method;
import java.util.Random;

import org.powerbat.executor.Result;
import org.powerbat.interfaces.Manifest;
import org.powerbat.interfaces.Runner;

@Manifest(category = "Recursive", instructions = "Return the nth fibonacci number...\nRecursively.", version = 1.0, className = "Fibonacci", level = 3, method = "int fibonacci(int n)")
public class FibonacciRunner extends Runner {

	@Override
	public Result[] getResults(Class<?> classToTest) {
		try {
			final Method method = classToTest.getMethod("fibonacci", int.class);
			final Random ran = new Random();
			final Result[] results = new Result[10];
			for (int i = 0; i < 10; i++) {
				final int num = ran.nextInt(30);
				results[i] = new Result(method.invoke(
						classToTest.newInstance(), num), fibonacci(num), num);
			}
			return results;
		} catch (Exception e) {
			return new Result[] {};
		}
	}

	public int fibonacci(int n) {
		if (n == 1 || n == 0) {
			return n;
		}
		return fibonacci(n - 1) + fibonacci(n - 2);

	}
}