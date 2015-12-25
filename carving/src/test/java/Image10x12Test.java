public class Image10x12Test extends AbstractVerifyImage {

	@Override
	public String getFilename() {
		return "10x12.png";
	}


	@Override
	public double getExpectedEnergy(boolean horizontal) {
		return horizontal ? 3380.304184 : 3599.030114;

	}

	@Override
	public int[] getExpectedSeam(boolean horizontal) {
		return horizontal ? new int[]{ 8,9,10,10,10,9,10,10,9,8 } : new int[]{ 5,6,7,8,7,7,6,7,6,5,6,5 };

	}
}
