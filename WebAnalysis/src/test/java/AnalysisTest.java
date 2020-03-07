import org.junit.Test;

import java.io.FileNotFoundException;
import java.io.IOException;

/**
 * <p>
 * TODO
 * </p>
 *
 * @author violet
 * @version 1.0
 * @since 2020/2/2 15:30
 */
public class AnalysisTest {

    @Test
    public void doProcess() throws IOException {
        Analysis analysis = new Analysis();
        analysis.doProcess();
    }

    @Test
    public void test(){
        System.out.println((byte)'\t');
    }
}
