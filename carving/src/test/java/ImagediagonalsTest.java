public class ImagediagonalsTest extends AbstractVerifyImage {

	@Override
	public String getFilename() {
		return "diagonals.png";
	}


	@Override
	public double getExpectedEnergy(boolean horizontal) {
		return horizontal ? 6372.339191 : 8246.198844;

	}

	@Override
	public int[] getExpectedSeam(boolean horizontal) {
		return horizontal ? new int[]{ 0,1,1,1,1,1,1,1,0 } : new int[]{ 0,1,1,1,1,1,1,1,1,1,1,0 };

	}
}
