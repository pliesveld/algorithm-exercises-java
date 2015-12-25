public class Image5x6Test extends AbstractVerifyImage {

	@Override
	public String getFilename() {
		return "5x6.png";
	}


	@Override
	public double getExpectedEnergy(boolean horizontal) {
		return horizontal ? 2583.198933 : 2769.528866;

	}

	@Override
	public int[] getExpectedSeam(boolean horizontal) {
		return horizontal ? new int[]{ 2,3,2,3,2 } : new int[]{ 1,2,2,3,2,1 };

	}
}
