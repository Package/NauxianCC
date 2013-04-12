import java.lang.reflect.Method;
import java.util.Arrays;
import java.util.Random;

import org.powerbat.executor.Result;
import org.powerbat.interfaces.Manifest;
import org.powerbat.interfaces.Runner;

@Manifest(category = "Logic", instructions = "Return true if the array has three of the same element in a row.\n{1, 4, 5, 5, 5, 8} -> true\n{1, 4, 5, 6, 5, 5, 8} -> false", version = 1.0d, className = "HasTriple", level = 2, method = "boolean hasTriple(int[] nums)")
public class HasTripleRunner extends Runner {

	@Override
	public Result[] getResults(Class<?> clazz) {
		try {
			final Method method = clazz.getMethod("hasTriple", int[].class);
			final Result[] results = new Result[10];
			final Random random = new Random();
			for (int i = 0; i < results.length; i++) {
				final int[] nums = new int[7];
				for (int k = 0; k < 7;) {
					final int val = random.nextInt(10);
					final int max = random.nextInt(3);
					for (int j = 0; j < max && k < 7; j++) {
						nums[k] = val;
						k++;
					}
				}
				results[i] = new Result(
						method.invoke(clazz.newInstance(), nums),
						hasTriple(nums), Arrays.toString(nums));
			}
			return results;
		} catch (Exception e) {
			return new Result[]{};
		}
	}

	private boolean hasTriple(int[] nums) {
		for (int i = 0; i < nums.length - 2; i++) {
			if (nums[i + 1] == nums[i] && nums[i + 2] == nums[i])
				return true;
		}
		return false;
	}

}
