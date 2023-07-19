import edu.princeton.cs.algs4.In;
import edu.princeton.cs.algs4.StdOut;

public class Outcast {

    private WordNet net;

    // constructor takes a WordNet object
    public Outcast(WordNet wordnet) {
        net = wordnet;
    }
     
    // given an array of WordNet nouns, return an outcast
    public String outcast(String[] nouns) {
        int max = sumDist(nouns[0], nouns);
        String outcast = nouns[0];

        for (int i = 1; i < nouns.length; i++) {
            int dist = sumDist(nouns[i], nouns);

            if (dist > max) {
                max = dist;
                outcast = nouns[i];
            }
        }

        return outcast;
    }

    private int sumDist(String sample, String[] nouns) {
        int sum = 0;
        
        for (String noun : nouns) {
            sum += net.distance(sample, noun);
        }

        return sum;
    }
    
    public static void main(String[] args) {
        WordNet wordnet = new WordNet(args[0], args[1]);
        Outcast outcast = new Outcast(wordnet);
        for (int t = 2; t < args.length; t++) {
            In in = new In(args[t]);
            String[] nouns = in.readAllStrings();
            StdOut.println(args[t] + ": " + outcast.outcast(nouns));
        }
    }
}

