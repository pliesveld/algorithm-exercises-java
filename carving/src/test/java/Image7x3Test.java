public class Image7x3Test extends AbstractVerifyImage {

	@Override
	public String getFilename() {
		return "7x3.png";
	}


	@Override
	public double getExpectedEnergy(boolean horizontal) {
		return horizontal ? 3282.484416 : 2218.952050;

	}

	@Override
	public int[] getExpectedSeam(boolean horizontal) {
		return horizontal ? new int[]{ 0,1,1,1,1,1,0 } : new int[]{ 2,3,2 };

	}
}
