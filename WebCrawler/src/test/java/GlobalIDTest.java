import org.junit.Test;
import util.SnowFlake;

/**
 * <p>
 * TODO
 * </p>
 *
 * @author violet
 * @version 1.0
 * @since 2020/2/1 17:13
 */
public class GlobalIDTest {

    @Test
    public void test() {
        int i = 100;
        while (i-- > 0) {
            System.out.println(SnowFlake.nextId());
        }
    }
}
