package gr.codelearn.core.showcase.collection;

import com.thedeanda.lorem.Lorem;
import com.thedeanda.lorem.LoremIpsum;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.util.Arrays;

public class ArrayDemo {
    private static final String LINE_DELIMITER = "-------------------------------------------------";
    private static final Logger logger = LoggerFactory.getLogger(ArrayDemo.class);
    private static final Lorem generator = LoremIpsum.getInstance();

    public static void main(String[] args) {
        integerArrayActions();
    }

    private static void integerArrayActions() {
        logger.info("");
        logger.info("### INTEGER ARRAY ACTIONS ###");

        int[] intArray = new int[]{1, 2, 3, 4, 5, 6, 7, 8, 9, 10};

        int[] intArrayTheNewWay = {1, 2, 3, 4, 5, 6, 7, 8, 9, 10};

        var loggingContent = new StringBuilder();
        for (int i = 0; i < intArrayTheNewWay.length; i++) {
            loggingContent.append(intArrayTheNewWay[i]).append(" ");
        }
        logger.info("Classic looping: {}", loggingContent);
        logger.info(LINE_DELIMITER);

        var loggingContent2 = new StringBuilder();
        for (int i : intArrayTheNewWay) {
            loggingContent2.append(i).append(" ");
        }
        logger.info("Enhanced for looping: {}", loggingContent2);
        logger.info(LINE_DELIMITER);

        int[][] intArray2D = {{1, 3, 5, 7, 9, 11}, {2, 4, 6, 8, 10, 13}, {1, 1, 2, 3, 5, 8, 13, 21}};

        var loggingContent3 = new StringBuilder();
        for (int i = 0; i < intArray2D.length; i++) {
            for (int j = 0; j < intArray2D[i].length; j++) {
                loggingContent3.append(intArray2D[i][j]).append(" ");
            }
            loggingContent3.append(", ");
        }
        logger.info("Looping 2D array: {}", loggingContent3);

        var fillableArray = new int[10];

        logger.info(LINE_DELIMITER);
        var loggingContent4 = new StringBuilder();
        Arrays.fill(fillableArray, 123);
        for (int i : fillableArray) {
            loggingContent4.append(i).append(" ");
        }
        logger.info("Logging fillable array: {}", loggingContent4);
    }


}
