public class ImagestripesTest extends AbstractVerifyImage {

	@Override
	public String getFilename() {
		return "stripes.png";
	}


	@Override
	public double getExpectedEnergy(boolean horizontal) {
		return horizontal ? 5091.710692 : 6416.729559;

	}

	@Override
	public int[] getExpectedSeam(boolean horizontal) {
		return horizontal ? new int[]{ 0,1,1,1,1,1,1,1,0 } : new int[]{ 0,1,1,1,1,1,1,1,1,1,1,0 };

	}
}
