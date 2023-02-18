package firstpackage;

import com.devexperts.logging.Logging;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

public class FirstTest {

    public static final Logging log = Logging.getLogging(FirstTest.class);
    public static final Logger log2 = LoggerFactory.getLogger(FirstTest.class.getName());

    public static void main(String[] args) {
        FirstTest ft = new FirstTest();
        final String letterA = ft.returnLetterA();
        System.out.println(letterA);

        ft.produceAInfoLog();
    }

    void produceAInfoLog() {
        log.info("|||||||||||||| - From 1st Logger");
        log2.info("|||||||||||||| - From 2nd Logger");
    }

    String returnLetterA() {
        return "A";
    }
}
