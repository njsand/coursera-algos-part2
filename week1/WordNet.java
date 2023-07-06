import java.util.TreeMap;
import java.util.Map;
import java.util.ArrayList;

import edu.princeton.cs.algs4.Digraph;
import edu.princeton.cs.algs4.In;

public class WordNet {

    // Maps words to the IDs of the synsets they belong to (and thus the
    // vertices of those synsets in the graph.)  A noun can belong to multiple
    // synsets.
    private final Map<String, ArrayList<Integer>> nouns = new TreeMap<>();
    private final Digraph graph;

    // constructor takes the name of the two input files
    //
    // TODO: Check for cycles?
    public WordNet(String synsets, String hypernyms) {
        // TODO: Throw execption if the input to the constructor does not
        // correspond to a rooted DAG.  Now how we gonna do that?
        
        if (synsets == null)
            throw new IllegalArgumentException("Synsets must be non-null");

        if (synsets == null)
            throw new IllegalArgumentException("Hypernyms must be non-null");
        
        graph = new Digraph(readSynsets(synsets));

        readHypernyms(hypernyms);
        
        checkRooted();
    }

    // Read the synsets file and add words to noun map.  First field is the id,
    // second field the nouns, and the third field (the gloss) we ignore.
    // Returns the number of synsets read.
    private int readSynsets(String filename) {
        In input = new In(filename);

        int id = 0;

        while (!input.isEmpty()) {
            String line = input.readLine();

            String[] fields = line.split(",");
            id = Integer.parseInt(fields[0]);
            String[] words = fields[1].split(" ");

            for (String w: words) {
                installWord(w, id);
            }
        }

        input.close();

        // We rely on the synset ids in the file being correct here, but that's
        // part of the contract.
        return id + 1;
    }

    // Read the hypernyms file and add edges to the graph.
    // First field is the synset id; all following fields are its hypernym
    // synset ids.
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

        return 0;
    }

    // a synset (second field of synsets.txt) that is the common ancestor of nounA and nounB
    // in a shortest ancestral path (defined below)
    public String sap(String nounA, String nounB) {
        validNoun("NounA", nounA);
        validNoun("NounB", nounB);
        
        return null;
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
            System.out.println(String.format("net.isNoun(%s): %b", w, net.isNoun(w)));
        }

        System.out.println("net.nouns():");
        for (String n: net.nouns()) {
            System.out.println(n);
        }
    }
}
