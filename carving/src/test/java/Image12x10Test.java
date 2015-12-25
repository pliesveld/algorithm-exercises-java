public class Image12x10Test extends AbstractVerifyImage {

	@Override
	public String getFilename() {
		return "12x10.png";
	}


	@Override
	public double getExpectedEnergy(boolean horizontal) {
		return horizontal ? 3878.866388 : 3311.007347;

	}

	@Override
	public int[] getExpectedSeam(boolean horizontal) {
		return horizontal ? new int[]{ 7,8,7,8,7,6,5,6,6,5,4,3 } : new int[]{ 6,7,7,6,6,7,7,7,8,7 };

	}
}
