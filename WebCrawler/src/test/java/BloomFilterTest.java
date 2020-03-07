import org.junit.Test;
import util.BloomFilter;

/**
 * <p>
 * TODO
 * </p>
 *
 * @author violet
 * @version 1.0
 * @since 2020/2/1 15:56
 */
public class BloomFilterTest {

    @Test
    public void test(){
        BloomFilter bloomFilter = new BloomFilter();
        bloomFilter.add("http://www.baidu.com");
        bloomFilter.add("https://www.baidu.com");
        bloomFilter.add("http://www.jd.com");
        bloomFilter.add("http://www.huya.com");

        System.out.println(bloomFilter.contains("http://www.baidu.com"));
        System.out.println(bloomFilter.contains("https://www.baidu.com"));
        System.out.println(bloomFilter.contains("www.huya.com"));
        System.out.println(bloomFilter.contains("123256"));
    }
}
