public class Image3x4Test extends AbstractVerifyImage {

	@Override
	public String getFilename() {
		return "3x4.png";
	}


	@Override
	public double getExpectedEnergy(boolean horizontal) {
		return horizontal ? 2228.087702 : 2456.615600;

	}

	@Override
	public int[] getExpectedSeam(boolean horizontal) {
		return horizontal ? new int[]{ 1,2,1 } : new int[]{ 0,1,1,0 };

	}
}
