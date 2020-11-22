package org.lasalle.lattice.representation;

import guru.nidi.graphviz.attribute.Rank;
import guru.nidi.graphviz.engine.Graphviz;
import guru.nidi.graphviz.model.MutableNode;
import org.thegalactic.dgraph.Node;
import org.thegalactic.lattice.Lattice;

import java.util.ArrayList;
import java.util.List;
import java.util.function.BiConsumer;
import java.util.stream.Collectors;
import java.util.stream.Stream;

import static guru.nidi.graphviz.attribute.Rank.RankDir.TOP_TO_BOTTOM;
import static guru.nidi.graphviz.model.Factory.graph;
import static guru.nidi.graphviz.model.Factory.mutNode;

public class DescendentRepresentation<T,R> implements GraphRepresentation<T,R>  {

    @Override
    public Graphviz represent(Lattice<T, R> lattice) {
        List<MutableNode> nodes = new ArrayList<>();

        this.visitDescendent(lattice, (vnode, sucs) -> {
            T value = vnode.getContent();

            MutableNode node = mutNode(value.toString())
                    .addLink(sucs.parallel().map(it -> it.getContent().toString())
                            .collect(Collectors.toList())
                            .toArray(new String[]{})
                    );

            nodes.add(node);
        });

        return Graphviz.fromGraph(
                graph("example1").directed()
                .graphAttr().with(Rank.dir(TOP_TO_BOTTOM))
                .with(nodes)
        );
    }

    private void visitDescendent(Lattice<T, R> lattice, BiConsumer<Node<T>, Stream<Node<T>>> node) {
        lattice.getNodes().parallelStream().forEach(n -> {
            node.accept(n, lattice.getSuccessorNodes(n).stream());
        });
    }

}
