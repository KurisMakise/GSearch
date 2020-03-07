import bean.Doc;

import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.IOException;
import java.io.InputStream;

/**
 * <p>
 * TODO
 * </p>
 *
 * @author violet
 * @version 1.0
 * @since 2020/2/2 15:20
 */
public class Analysis {

    public Analysis() throws FileNotFoundException {
        docIns = new FileInputStream(DOC_RAW_FILE_NAME);
    }

    public void doProcess() throws IOException {
        while (docIns.available() > 0) {
            String content = getDoc().getContent();
            removeJsCss(content);
            removeHtml(content);
        }
    }

    private String removeJsCss(String content) {

        return content;
    }

    private String removeHtml(String content) {
        return content;
    }

    private static final String DOC_RAW_FILE_NAME = "./../WebCrawler/data/doc_raw.bin";

    private InputStream docIns;

    private Doc getDoc() throws IOException {
        Doc doc = new Doc();
        int size;
        long id;
        StringBuilder sb = new StringBuilder();
        char separator = '\t';

        char read;
        while ((read = (char) docIns.read()) != separator) {
            sb.append(read);
        }
        id = Long.parseLong(sb.toString());

        sb.delete(0, sb.length());
        while ((read = (char) docIns.read()) != separator) {
            sb.append(read);
        }
        size = Integer.parseInt(sb.toString());

        byte[] buf = new byte[size];
        docIns.read(buf);

        //跳过分隔符 /r/n/r/n
        docIns.skip(4);

        doc.setContent(new String(buf));
        doc.setId(id);
        doc.setSize(size);
        return doc;
    }
}
