package gr.codelearn.core.showcase.stream;

import com.thedeanda.lorem.Lorem;
import com.thedeanda.lorem.LoremIpsum;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.util.regex.Pattern;
import java.util.stream.Stream;

public class StreamDemo {
    private static final String LINE_DELIMITER = "---------------------------------------";
    private static final Logger logger = LoggerFactory.getLogger(StreamDemo.class);
    private static final Lorem generator = LoremIpsum.getInstance();

    public static void main(String[] args) {
        streamActions();
    }

    private static void streamActions() {
        logger.info("STREAM CREATE ACTIONS");

        Stream<String> emptyStream = Stream.empty();

        Stream<String> simpleStringStream = createSampleStringStream();
//        simpleStringStream.forEach(logger::info);

        simpleStringStream.sorted()
                .map(String::toUpperCase)
                .filter(x -> x.toLowerCase().contains("x"))
                .forEach(logger::info);
    }

    private static Stream<String> createSampleStringStream() {
        String generatedWords= generator.getWords(30);
        return Pattern.compile(" ").splitAsStream(generatedWords);
    }
}
