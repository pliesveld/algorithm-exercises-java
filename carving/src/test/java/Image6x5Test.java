public class Image6x5Test extends AbstractVerifyImage {

	@Override
	public String getFilename() {
		return "6x5.png";
	}


	@Override
	public double getExpectedEnergy(boolean horizontal) {
		return horizontal ? 2530.681960 : 2414.973496;

	}

	@Override
	public int[] getExpectedSeam(boolean horizontal) {
		return horizontal ? new int[]{ 1,2,1,2,1,0 } : new int[]{ 3,4,3,2,1 };

	}
}
