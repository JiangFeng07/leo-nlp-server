package com.leo.nlp.structure;

import org.junit.Test;

import java.util.Iterator;

/**
 * Created by lionel on 17/8/24.
 */
public class ListDGraphTest {
    @Test
    public void test() {
        DGraph<String> dGraph = new ListDGraph<String>();
        dGraph.add("A");
        dGraph.add("B");
        dGraph.add("C");
        dGraph.add("D");
        dGraph.add("E");
        dGraph.add("F");
        dGraph.add("G");
        dGraph.add("H");
        dGraph.add("I");

        dGraph.add(new DGraph.Edge<String>("A", "B"));
        dGraph.add(new DGraph.Edge<String>("A", "C"));
        dGraph.add(new DGraph.Edge<String>("A", "D"));
        dGraph.add(new DGraph.Edge<String>("B", "E"));
        dGraph.add(new DGraph.Edge<String>("E", "F"));
        dGraph.add(new DGraph.Edge<String>("C", "F"));
        dGraph.add(new DGraph.Edge<String>("D", "G"));
        dGraph.add(new DGraph.Edge<String>("D", "H"));
        dGraph.add(new DGraph.Edge<String>("G", "H"));
        dGraph.add(new DGraph.Edge<String>("G", "I"));

        Iterator<String> iterator = dGraph.iterator(DGraph.ITERATOR_TYPE_BFS, "A");
        while (iterator.hasNext()) {
            String str = iterator.next();
            System.out.print(str + "->");
        }

        System.out.println();
        iterator = dGraph.iterator(DGraph.ITERATOR_TYPE_DFS, "A");
        while (iterator.hasNext()) {
            String str = iterator.next();
            System.out.print(str + "->");
        }
    }
}
