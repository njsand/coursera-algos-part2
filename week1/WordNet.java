import java.util.TreeMap;
import java.util.Map;
import java.util.ArrayList;
import java.util.List;

import edu.princeton.cs.algs4.Digraph;
import edu.princeton.cs.algs4.In;
import edu.princeton.cs.algs4.StdOut;
import edu.princeton.cs.algs4.StdIn;
import edu.princeton.cs.algs4.DirectedCycle;

// https://coursera.cs.princeton.edu/algs4/assignments/wordnet/specification.php
public class WordNet {

    // Maps words to the IDs of the synsets they belong to (and thus the
    // vertices of those synsets in the graph.)  A noun can belong to multiple
    // synsets.
    private final Map<String, ArrayList<Integer>> nouns = new TreeMap<>();
    private final List<String> synsets = new ArrayList<>();

    private final Digraph graph;
    private final SAP sap;

    // constructor takes the name of the two input files
    public WordNet(String synsets, String hypernyms) {
        if (synsets == null)
            throw new IllegalArgumentException("Synsets must be non-null");

        if (hypernyms == null)
            throw new IllegalArgumentException("Hypernyms must be non-null");
        
        graph = new Digraph(readSynsets(synsets));
        readHypernyms(hypernyms);
        checkRooted();
        checkAcyclic();

        sap = new SAP(graph);
    }

    // Read the synsets file and add words to the noun map.
    //
    // The first field in the file is the synset id, the second field the list
    // of nouns (space separated), and the third field (the gloss) we ignore.
    // Returns the number of synsets read.
    private int readSynsets(String filename) {
        In input = new In(filename);

        int id = 0;

        while (!input.isEmpty()) {
            String line = input.readLine();

            String[] fields = line.split(",");
            id = Integer.parseInt(fields[0]);

            synsets.add(fields[1]);
            for (String w: fields[1].split(" ")) {
                installWord(w, id);
            }
        }

        input.close();

        // We rely on the synset ids in the file being correct here, but that's
        // part of the contract of the input file.
        return id + 1;
    }

    // Read the hypernyms file and add edges to the graph.
    // 
    // The first field is the synset id; all the following fields are its
    // hypernym synset ids.
    private void readHypernyms(String filename) {
        In input = new In(filename);

        while (!input.isEmpty()) {
            String[] fields = input.readLine().split(",");

            int synsetId = Integer.parseInt(fields[0]);

            for (int i = 1; i < fields.length; i++) {
                graph.addEdge(synsetId, Integer.parseInt(fields[i]));
            }
        }

        input.close();
    }
    
    // Check there is exactly one vertex with zero outgoing edges.  If there are
    // two or more, the DAG is not rooted.
    private void checkRooted() {
        int first = -1;

        for (int i = 0; i < graph.V(); i++) {
            if (graph.outdegree(i) == 0)
                if (first == -1)
                    first = i;
                else {
                    throw new IllegalArgumentException(
                        String.format("Graph is not rooted.  %d and %d are both roots.",
                                      first,
                                      i));
                }
        }
    }

    private void checkAcyclic() {
        DirectedCycle cycle = new DirectedCycle(graph);

        if (cycle.hasCycle())
            throw new IllegalArgumentException("Graph has a cycle.");
    }

    // Add an entry into the noun -> vertex id map.
    private void installWord(String word, int id) {
        ArrayList<Integer> values = nouns.get(word);

        if (values == null)
            values = new ArrayList<>();
        
        values.add(id);
        nouns.put(word, values);
    }

    // returns all WordNet nouns
    public Iterable<String> nouns() {
        return nouns.keySet();
    }

    // is the word a WordNet noun?
    public boolean isNoun(String word) {
        if (word == null)
            throw new IllegalArgumentException("Word must be non-null.");
        
        return nouns.get(word) != null;
    }

    // distance between nounA and nounB (defined below)
    public int distance(String nounA, String nounB) {
        validNoun("NounA", nounA);
        validNoun("NounB", nounB);

        Iterable<Integer> aVerts = nouns.get(nounA);
        Iterable<Integer> bVerts = nouns.get(nounB);

        return sap.length(aVerts, bVerts);
    }

    // a synset (second field of synsets.txt) that is the common ancestor of nounA and nounB
    // in a shortest ancestral path (defined below)
    public String sap(String nounA, String nounB) {
        validNoun("NounA", nounA);
        validNoun("NounB", nounB);

        Iterable<Integer> aVerts = nouns.get(nounA);
        Iterable<Integer> bVerts = nouns.get(nounB);

        return synsets.get(sap.ancestor(aVerts, bVerts));
    }

    private void validNoun(String label, String noun) {
        if (noun == null || !isNoun(noun))
            throw new IllegalArgumentException(label + " must be non-null and exist in the input");
    }

    // do unit testing of this class
    public static void main(String[] args) {
        WordNet net = new WordNet(args[0], args[1]);

        String[] words = {"a", "b", "z"};
        for (String w: words) {
            StdOut.printf("net.isNoun(%s): %b", w, net.isNoun(w));
        }

        while (!StdIn.isEmpty()) {
            String v = StdIn.readString();
            String w = StdIn.readString();
            // int length   = sap.length(v, w);
            // int ancestor = sap.ancestor(v, w);
            StdOut.printf("%s\n", net.sap(v, w));
        }
    }
}
