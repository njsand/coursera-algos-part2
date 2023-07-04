import java.util.TreeMap;
import java.util.Map;
import java.util.ArrayList;

import edu.princeton.cs.algs4.Digraph;
import edu.princeton.cs.algs4.In;

public class WordNet {

    Map<String, ArrayList<Integer>> nouns = new TreeMap<>();
    Digraph graph;

    // constructor takes the name of the two input files
    public WordNet(String synsets, String hypernyms) {

        In in = new In(synsets);

        int id = 0;

        while (!in.isEmpty()) {
            String line = in.readLine();

            String[] fields = line.split(",");
            id = Integer.parseInt(fields[0]);
            String[] words = fields[1].split(" ");

            for (String w: words) {
                installWord(w, id);
            }
        }

        graph = new Digraph(id + 1);

        in = new In(hypernyms);

        while (!in.isEmpty()) {
            String line = in.readLine();
            String[] fields = line.split(",");

            int synsetId = Integer.parseInt(fields[0]);

            for (int i = 1; i < fields.length; i++) {
                graph.addEdge(synsetId, Integer.parseInt(fields[i]));
            }
        }
    }

    void installWord(String word, int id) {
        ArrayList<Integer> values = nouns.get(word);

        if (values == null) {
            values = new ArrayList<Integer>();
        }
        
        values.add(id);
        nouns.put(word, values);
    }

    // returns all WordNet nouns
    public Iterable<String> nouns() {
        return nouns.keySet();
    }

    // is the word a WordNet noun?
    public boolean isNoun(String word) {
        return nouns.get(word) != null;
    }

    // distance between nounA and nounB (defined below)
    public int distance(String nounA, String nounB) {
        return 0;
    }

    // a synset (second field of synsets.txt) that is the common ancestor of nounA and nounB
    // in a shortest ancestral path (defined below)
    public String sap(String nounA, String nounB) {
        return null;
    }

    // do unit testing of this class
    public static void main(String[] args) {
        WordNet net = new WordNet(args[0], args[1]);

        String[] words = {"a", "b", "z"};
        for (String w: words) {
            System.out.println(String.format("net.isNoun(%s): %b", w, net.isNoun(w)));
        }
    }
}
