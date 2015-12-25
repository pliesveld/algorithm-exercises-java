import org.junit.runner.RunWith;
import org.junit.runners.Suite;
import org.junit.runners.Suite.SuiteClasses;

@RunWith(Suite.class)
@SuiteClasses({ ImagechameleonTest.class, ImagediagonalsTest.class, ImageHJoceanTest.class,
		ImageHJoceanTransposedTest.class, ImagestripesTest.class })
public class AdvancedImageUnitTests {

}
