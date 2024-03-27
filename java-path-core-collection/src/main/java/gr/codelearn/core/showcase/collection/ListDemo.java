package gr.codelearn.core.showcase.collection;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.Comparator;
import java.util.List;

public class ListDemo {
    private static final String LINE_DELIMITER = "-------------------------------------------------";
    private static final Logger logger = LoggerFactory.getLogger(ListDemo.class);

    public static void main(String[] args) {
        createLists();
        otherListActions();
    }

    private static void createLists() {
        logger.info("### LIST CREATE ACTIONS ###");

        List<String> list = Arrays.asList("Lars", "Simon");
        list.set(0, "Johny");

        logger.info("My list: {}", list);

        List<String> anotherList = new ArrayList<>();
        anotherList.add("Lars");
        anotherList.add("Simon");

        logger.info(LINE_DELIMITER);
        anotherList.forEach(logger::info);

        List<String> listCopy1 = List.copyOf(anotherList);
        List<String> listCopy2 = List.of("copy1", "copy2");
    }

    private static void otherListActions() {
        logger.info("");
        logger.info("### LIST OTHER ACTIONS ###");

        logger.info("Sorting with natural order");
        List<String> l1 = createList();
        l1.sort(null);
        l1.forEach(logger::info);

        logger.info("Sorting with custom order");
        List<String> l2 = createList();
        l2.sort(Comparator.comparing(String::length));
        l2.forEach(logger::info);

        logger.info(LINE_DELIMITER);
        logger.info("Demonstration of removeIf");
        List<String> l3 = createList2();
        l3.removeIf(s -> s.toLowerCase().contains("x"));
        l3.forEach(logger::info);

    }

    private static List<String> createList() {
        return Arrays.asList("Windows", "Linux", "iOS", "Android", "Mac OS X");
    }

    private static List<String> createList2() {
        List<String> list = new ArrayList<>();
        list.addAll(createList());
        return list;
    }
}
