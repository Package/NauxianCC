import java.lang.reflect.Method;
import java.util.ArrayList;
import java.util.Random;

import org.powerbat.executor.Result;
import org.powerbat.interfaces.Manifest;
import org.powerbat.interfaces.Runner;

@Manifest(category = "Logic", instructions = "Return the sum of a and b.", version = 1.0, className = "SimpleAddition", level = 1, method = "int add(int a, int b)")
public class SimpleAdditionRunner extends Runner {
	
	@Override
	public Result[] getResults(Class<?> c) {
		try{
			final Random random = new Random();
			final Object instance = c.newInstance();
			final Method m = c.getMethod("add", int.class, int.class);

			final int count = 10;
			final ArrayList<Result> results = new ArrayList<Result>();
			for(int i = 0; i < count; i++){//just iterate ten times
				final int x = -3000 + random.nextInt(6000); //random between -3000 and 5999
				final int y = -3000 + random.nextInt(6000);
				final int correct = x + y;
				final int answer = (Integer) m.invoke(instance, x, y);
				
				results.add(new Result(answer, correct, x, y));
			}
			return results.toArray(new Result[results.size()]);
		}catch(Exception e){
		}
		return new Result[]{};
	}

}