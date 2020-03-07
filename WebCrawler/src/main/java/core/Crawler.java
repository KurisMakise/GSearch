package core;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.http.converter.StringHttpMessageConverter;
import org.springframework.stereotype.Service;
import org.springframework.web.client.RestTemplate;
import util.BloomFilter;
import util.SnowFlake;

import java.io.*;
import java.nio.charset.StandardCharsets;
import java.util.BitSet;
import java.util.Scanner;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

/**
 * <p>
 * TODO
 * </p>
 *
 * @author violet
 * @version 1.0
 * @since 2020/1/31 15:12
 */
@Service
public class Crawler {
    private static final String LINK_FILE_NAME = "./data/links.bin";
    private static final String BLOOM_FILTER_FILE_NAME = "./data/bloom_filter.bin";
    private static final String DOC_ID_FILE_NAME = "./data/doc_id.bin";
    private static final String DOC_RAW_FILE_NAME = "./data/doc_raw.bin";

    public Crawler() throws IOException {
        linksOutputStream = new FileOutputStream(LINK_FILE_NAME, true);

        linksInputStream = new FileInputStream(LINK_FILE_NAME);
        linksInputStream.skip(position);
        scanner = new Scanner(linksInputStream);


        docRawOutputStream = new FileOutputStream(DOC_RAW_FILE_NAME);
        docIdOutputStream = new FileOutputStream(DOC_ID_FILE_NAME);

        restore();
    }

    /**
     * 恢复布隆过滤器记录
     */
    private void restore() throws IOException {
        File file = new File(BLOOM_FILTER_FILE_NAME);
        bloomFilter = new BloomFilter();
        if (!file.exists()) {
            return;
        }
        ObjectInputStream objectInputStream = new ObjectInputStream(new FileInputStream(BLOOM_FILTER_FILE_NAME));
        try {
            bloomFilter.setBitSets((BitSet) objectInputStream.readObject());
        } catch (ClassNotFoundException e) {
            e.printStackTrace();
        }
        objectInputStream.close();
    }

    private long position;

    /**
     * 保存连接
     */
    private InputStream linksInputStream;
    private OutputStream linksOutputStream;
    private Scanner scanner;

    /**
     * 保存原始网页
     */
    private OutputStream docRawOutputStream;

    private OutputStream docIdOutputStream;

    private BloomFilter bloomFilter;

    @Autowired
    private RestTemplate restTemplate;

    public void doProcess() throws IOException {
        if (restTemplate == null) {
            restTemplate = new RestTemplate();
            restTemplate.getMessageConverters().set(1, new StringHttpMessageConverter(StandardCharsets.UTF_8));
        }
        String url = dequeue();
        while (url != null) {
            try {
                if (!url.contains("http")) {
                    url = "http:" + url;
                }
                if (bloomFilter.contains(url)) {
                    url = dequeue();
                    continue;
                }
                ResponseEntity<String> responseEntity = restTemplate.getForEntity(url, String.class);
                if (responseEntity.getStatusCode() == HttpStatus.OK) {
                    saveDoc(url, responseEntity.getBody());
                }
            } catch (Exception e) {
                e.printStackTrace();
            }

            url = dequeue();
        }
        linksInputStream.close();
        linksOutputStream.close();
    }

    public void saveDoc(String url, String doc) throws IOException {
        saveUrl(doc);
        long id = SnowFlake.nextId();

        String index = id + "\t" + url + "\r\n";
        docIdOutputStream.write(index.getBytes(StandardCharsets.UTF_8));

        String content = id + "\t" + doc.getBytes(StandardCharsets.UTF_8).length + "\t" + doc + "\r\n\r\n";
        docRawOutputStream.write(content.getBytes(StandardCharsets.UTF_8));
    }

    /**
     * 解析页面所有的url，并加入队列
     *
     * @param doc
     */
    private static Pattern compile = Pattern.compile("href\\s*=\\s*['\"](.*?)['\"]");
    private void saveUrl(String doc) throws IOException {
        Matcher matcher1 = compile.matcher(doc);
        while (matcher1.find()) {
            enqueue(matcher1.group().replace("href", "").replace("=", "").replaceAll("\"", ""));
        }
    }

//    private void test()throws IOException {
//        Pattern pattern = Pattern.compile("<[Aa]\\s+(.*?\\s+)*?href\\s*=\\s*(['\"]).+?\\2(\\s+.*?\\s*)*?>.+?</[Aa]>");
//        Matcher matcher = pattern.matcher("doc");
//        while (matcher.find()) {
//            Pattern compile = Pattern.compile("href\\s*=\\s*['\"](.*?)['\"]");
//            Matcher matcher1 = compile.matcher(matcher.group());
//            while (matcher1.find()) {
//                enqueue(matcher1.group().replace("href", "").replace("=", "").replaceAll("\"", ""));
//            }
//        }
//    }

    public void persistence() throws IOException {

        OutputStream outputStream = new FileOutputStream(BLOOM_FILTER_FILE_NAME);

        ObjectOutputStream objectOutputStream = new ObjectOutputStream(outputStream);


        objectOutputStream.writeObject(bloomFilter.getBitSets());

        objectOutputStream.close();
    }


    public void enqueue(String url) throws IOException {
        bloomFilter.add(url);
        initQueue(url);
    }

    public void initQueue(String url) throws IOException {
        linksOutputStream.write((url + "\r\n").getBytes(StandardCharsets.UTF_8));
    }


    public String dequeue() throws IOException {
        String url = scanner.next();

        position += url.length();
        return url;
    }
}
