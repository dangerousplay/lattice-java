package org.lasalle.lattice.representation;

import guru.nidi.graphviz.engine.Graphviz;
import org.thegalactic.lattice.ConceptLattice;
import org.thegalactic.lattice.Lattice;

public class IdealRepresentation<T,R> implements GraphRepresentation<T,R> {
    @Override
    public Graphviz represent(Lattice<T,R> lattice) {
        return Graphviz.fromString(this.latticeAsDot(ConceptLattice.idealLattice(lattice)));
    }
}
