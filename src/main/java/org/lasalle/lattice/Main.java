package org.lasalle.lattice;

import com.mxgraph.swing.mxGraphComponent;
import com.mxgraph.view.mxGraph;
import de.linearbits.jhpl.Lattice;
import guru.nidi.graphviz.attribute.Font;
import guru.nidi.graphviz.attribute.Rank;
import guru.nidi.graphviz.engine.Format;
import guru.nidi.graphviz.engine.Graphviz;
import guru.nidi.graphviz.model.Graph;
import guru.nidi.graphviz.model.MutableNode;

import javax.swing.*;
import java.io.File;
import java.io.IOException;
import java.lang.reflect.InvocationTargetException;
import java.lang.reflect.Method;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Iterator;
import java.util.List;
import java.util.Stack;
import java.util.function.BiConsumer;
import java.util.stream.Collectors;
import java.util.stream.Stream;

import static guru.nidi.graphviz.attribute.Rank.RankDir.TOP_TO_BOTTOM;
import static guru.nidi.graphviz.model.Factory.graph;
import static guru.nidi.graphviz.model.Factory.mutNode;

public class Main extends JPanel {


    private static final long serialVersionUID = -2707712944901661771L;

    public Main() {
        super(true);
    }

    private void buildGRaph() {
        String[][] elements = new String[][]{
                {"0", "1", "2", "3"},
                {"0", "1", "2", "3"},
                {"0", "1", "2", "3"},
                {"0", "1", "2", "3"},
        };

        Lattice<String, Integer> lattice = new Lattice<>(elements);

        mxGraph graph = new mxGraph();

        graph.getModel().beginUpdate();

        try {
            Graphviz.fromGraph(this.buildGraph(lattice))
                    .render(Format.PNG)
                    .toFile(new File("examples/ex2.png"));
        } catch (IOException e) {
            e.printStackTrace();
        } finally {
            graph.getModel().endUpdate();
        }

        mxGraphComponent graphComponent = new mxGraphComponent(graph);

    }

    private <T, R> Graph buildGraph(Lattice<T, R> lattice) {
        List<MutableNode> nodes = new ArrayList<>();

        this.visit(lattice, (vnode, sucs) -> {
            T[] value = lattice.space().toSource(vnode);

            MutableNode node = mutNode(Arrays.toString(value))
                    .addLink(sucs.parallel().map(lattice.space()::toSource)
                            .map(Arrays::toString)
                            .collect(Collectors.toList())
                            .toArray(new String[]{})
                    );

            nodes.add(node);
        });

        return graph("example1").directed()
                .graphAttr().with(Rank.dir(TOP_TO_BOTTOM))
                .nodeAttr().with(Font.name("arial"))
                .linkAttr().with("class", "link-class")
                .with(nodes);
    }

    private <T, R> void visit(Lattice<T, R> lattice, BiConsumer<int[], Stream<int[]>> node) {

        Stack<int[]> queue = new Stack<>();

        Iterator<int[]> ret = null;

        try {
            Method m = Lattice.class.getDeclaredMethod("listAllNodes");
            m.setAccessible(true);
            ret = (Iterator<int[]>) m.invoke(lattice);
        } catch (NoSuchMethodException e) {
            e.printStackTrace();
        } catch (IllegalAccessException e) {
            e.printStackTrace();
        } catch (InvocationTargetException e) {
            e.printStackTrace();
        }

        /*Map<int[], Stream<int[]>> values = IntStream.range(0, lattice.numLevels()).parallel().mapToObj(i -> this.listAllNodes(lattice, i)).flatMap(i -> {
            List<int[]> nodes = new ArrayList<>();

            i.forEachRemaining(nodes::add);

            return nodes.stream();
        }).collect(Collectors.toMap(k -> k, k -> {
            List<int[]> nodes = new ArrayList<>();

            Iterator<int[]> i = lattice.nodes().listSuccessors(k);

            i.forEachRemaining(nodes::add);

            return nodes.stream()
        }));*/


        ret.forEachRemaining(n -> {
            List<int[]> successorsNodes = new ArrayList<>();

            lattice.nodes().listSuccessors(n).forEachRemaining(successorsNodes::add);

            node.accept(n, successorsNodes.stream());
        });

    }

    private Iterator<int[]> listAllNodes(Lattice lattice, int level) {
        try {
            Method m = Lattice.class.getDeclaredMethod("listAllNodes", int.class);
            m.setAccessible(true);
            return (Iterator<int[]>) m.invoke(lattice, level);
        } catch (NoSuchMethodException e) {
            e.printStackTrace();
        } catch (IllegalAccessException e) {
            e.printStackTrace();
        } catch (InvocationTargetException e) {
            e.printStackTrace();
        }

        return null;
    }

    public static void main(String[] args) {
        Main frame = new Main();

        frame.setSize(400, 320);
        frame.setVisible(true);

        frame.buildGRaph();
    }


}
