import core.Crawler;
import org.junit.Test;

import java.io.FileReader;
import java.io.IOException;

/**
 * <p>
 * TODO
 * </p>
 *
 * @author violet
 * @version 1.0
 * @since 2020/1/31 15:54
 */
public class CrawlerTest {


    @Test
    public void init() throws IOException {
        Crawler crawler = new Crawler();
        crawler.initQueue("https://www.jd.com");
//        crawler.enqueue("https://a.jd.com/");

        crawler.doProcess();
//        crawler.enqueue("www.bilibili.com");
//        System.out.println(crawler.dequeue());
//        System.out.println(crawler.dequeue());
//        System.out.println(crawler.dequeue());
    }

    @Test
    public void persistence() throws IOException {
        Crawler crawler = new Crawler();
        crawler.enqueue("www.jd.com");
        crawler.enqueue("www.google.com");
        crawler.enqueue("www.huya.com");

        crawler.doProcess();
        crawler.persistence();

    }

    @Test
    public void testSaveUrl() throws IOException {
        FileReader reader = new FileReader("./data/doc_raw.bin");
        char[] buf = new char[102400];
        reader.read(buf);
        Crawler crawler = new Crawler();
        crawler.saveDoc("https://www.baidu.com", new String(buf));
    }

}
