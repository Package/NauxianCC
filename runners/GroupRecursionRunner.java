import java.lang.reflect.Method;
import java.util.Arrays;
import java.util.Random;

import org.powerbat.executor.Result;
import org.powerbat.interfaces.Manifest;
import org.powerbat.interfaces.Runner;

@Manifest(category = "Recursive", instructions = "Return true if any combination of the numbers can reach the target.\nStart will always be 0 to begin with.\nRemember that this is recursive.", version = 1.0d, className = "GroupRecursion", level = 5, method = "boolean group(int start, int target, int[] nums)")
public class GroupRecursionRunner extends Runner {

	@Override
	public Result[] getResults(Class<?> clazz) {
		try {
			final Method method = clazz.getMethod("group", int.class, int.class,
					int[].class);
			final Random random = new Random();
			final Result[] results = new Result[10];
			for (int i = 0; i < 10; i++) {
				final int[] nums = new int[3 + random.nextInt(4)];
				for (int j = 0; j < nums.length; j++) {
					nums[j] = random.nextInt(13);
				}
				final int target = 3 + random.nextInt(22);
				results[i] = new Result(method.invoke(clazz.newInstance(), 0,
						target, nums), groupSum(0, target, nums), "0, "
						+ target + ", " + Arrays.toString(nums));

			}
			return results;
		} catch (Exception e) {
			return new Result[]{};
		}
	}

	private boolean groupSum(int start, int target, int[] nums) {
		if (start == nums.length)
			return target == 0;
		if (groupSum(start + 1, target - nums[start], nums))
			return true;
		return groupSum(start + 1, target, nums);
	}
}