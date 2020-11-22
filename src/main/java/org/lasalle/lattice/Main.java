package org.lasalle.lattice;

import guru.nidi.graphviz.engine.Format;
import org.lasalle.lattice.representation.CompleteRepresentation;
import org.lasalle.lattice.representation.DescendentRepresentation;
import org.lasalle.lattice.representation.HasseRepresentation;
import org.lasalle.lattice.representation.IdealRepresentation;
import org.thegalactic.lattice.Lattice;
import org.thegalactic.lattice.LatticeFactory;

import java.io.File;
import java.io.IOException;
import java.util.stream.Stream;


public class Main  {

    private void buildRepresentations() {

        Stream.of(new DescendentRepresentation(), new HasseRepresentation(), new CompleteRepresentation(), new IdealRepresentation()).forEach(it -> {
            Stream.of(LatticeFactory.random(10), LatticeFactory.booleanAlgebra(3), LatticeFactory.permutationLattice(4))
                    .forEach(la -> {
                        try {
                            it.represent(la)
                                    .render(Format.PNG)
                                    .toFile(new File("examples/" + it.representationName() + "-" + la.hashCode() + ".png"));
                        } catch (IOException e) {
                            e.printStackTrace();
                        }
                    });
        });

    }

    public static void main(String[] args) {
        Main frame = new Main();

        frame.buildRepresentations();
    }


}
