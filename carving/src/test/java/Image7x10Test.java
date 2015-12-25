public class Image7x10Test extends AbstractVerifyImage {

	@Override
	public String getFilename() {
		return "7x10.png";
	}


	@Override
	public double getExpectedEnergy(boolean horizontal) {
		return horizontal ? 2898.313922 : 3443.197820;

	}

	@Override
	public int[] getExpectedSeam(boolean horizontal) {
		return horizontal ? new int[]{ 6,7,7,7,8,8,7 } : new int[]{ 2,3,4,3,4,3,3,2,2,1 };

	}
}
