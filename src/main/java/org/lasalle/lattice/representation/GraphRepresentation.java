package org.lasalle.lattice.representation;

import guru.nidi.graphviz.engine.Graphviz;
import org.thegalactic.lattice.ConceptLattice;
import org.thegalactic.lattice.Lattice;
import org.thegalactic.lattice.io.ConceptLatticeSerializerDot;

import java.io.BufferedWriter;
import java.io.IOException;
import java.io.StringWriter;

public interface GraphRepresentation<T,R> {

    Graphviz represent(Lattice<T,R> lattice);

    default String representationName() {
        return this.getClass().getSimpleName();
    }

    default String latticeAsDot(ConceptLattice lattice) {

        try (StringWriter writer = new StringWriter();
             BufferedWriter bf = new BufferedWriter(writer)) {
            ConceptLatticeSerializerDot.getInstance().write(lattice, bf);

            bf.flush();

            return writer.toString();
        } catch (IOException e) {
            e.printStackTrace();
        }

        return null;
    }

}
