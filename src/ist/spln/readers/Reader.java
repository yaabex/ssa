package ist.spln.readers;


import java.io.IOException;
import java.util.List;

public interface Reader {
    public final static String configLocation = "./resources/config.xml";

    public void read() throws IOException;
    public List getTextList();
}
