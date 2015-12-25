import org.junit.runner.RunWith;
import org.junit.runners.Suite;
import org.junit.runners.Suite.SuiteClasses;

@RunWith(Suite.class)
@SuiteClasses({ Image10x10Test.class, Image10x12Test.class, Image12x10Test.class, Image1x1Test.class,
		Image1x8Test.class, Image3x4Test.class, Image3x7Test.class, Image4x6Test.class, Image5x6Test.class,
		Image6x5Test.class, Image7x10Test.class, Image7x3Test.class, Image8x1Test.class })
public class BasicImageUnitTests {

}
