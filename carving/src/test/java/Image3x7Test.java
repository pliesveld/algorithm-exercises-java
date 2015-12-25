public class Image3x7Test extends AbstractVerifyImage {

	@Override
	public String getFilename() {
		return "3x7.png";
	}


	@Override
	public double getExpectedEnergy(boolean horizontal) {
		return horizontal ? 2236.167314 : 3386.629883;

	}

	@Override
	public int[] getExpectedSeam(boolean horizontal) {
		return horizontal ? new int[]{ 1,2,1 } : new int[]{ 0,1,1,1,1,1,0 };

	}
}
