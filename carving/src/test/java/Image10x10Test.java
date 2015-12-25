public class Image10x10Test extends AbstractVerifyImage {

	@Override
	public String getFilename() {
		return "10x10.png";
	}


	@Override
	public double getExpectedEnergy(boolean horizontal) {
		return horizontal ? 3260.892911 : 3348.051236;

	}

	@Override
	public int[] getExpectedSeam(boolean horizontal) {
		return horizontal ? new int[]{ 0,1,2,3,3,3,3,2,1,0 } : new int[]{ 6,7,7,7,7,7,8,8,7,6 };

	}
}
