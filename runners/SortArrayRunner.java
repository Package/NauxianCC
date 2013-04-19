import java.lang.reflect.Method;
import java.util.Arrays;
import java.util.Random;

import org.nauxiancc.executor.Result;
import org.nauxiancc.interfaces.Runner;

public class SortArrayRunner extends Runner {

	@Override
	public Result[] getResults(Class<?> classToTest) {
		try {
			final Method method = classToTest.getMethod("sort", int[].class);
			final Result[] results = new Result[10];
            final Random ran = new Random();
			for (int i = 0; i < 10; i++) {
                final int[] nums = new int[ran.nextInt(10)];
                for(int j = 0; j < nums.length; j++){
                    nums[j] = ran.nextInt(20) - ran.nextInt(10);
                }
				results[i] = new Result(Arrays.toString((int[]) method.invoke(classToTest.newInstance(), nums)), Arrays.toString(sort(nums)), Arrays.toString(nums));
			}
			return results;
		} catch (Exception e) {
			return new Result[]{};
		}
	}

	private int[] sort(int[] nums){
        Arrays.sort(nums);
        return nums;
	}
}