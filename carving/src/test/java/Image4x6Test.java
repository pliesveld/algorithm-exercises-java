public class Image4x6Test extends AbstractVerifyImage {

	@Override
	public String getFilename() {
		return "4x6.png";
	}


	@Override
	public double getExpectedEnergy(boolean horizontal) {
		return horizontal ? 2346.424595 : 2706.370116;

	}

	@Override
	public int[] getExpectedSeam(boolean horizontal) {
		return horizontal ? new int[]{ 1,2,1,0 } : new int[]{ 1,2,1,1,2,1 };

	}
}
