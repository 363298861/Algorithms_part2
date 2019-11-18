import edu.princeton.cs.algs4.In;
import edu.princeton.cs.algs4.StdOut;

public class Outcast {
    private final WordNet outcast;

    public Outcast(WordNet wordnet) {
        outcast = wordnet;
    } // constructor takes a WordNet object

    public String outcast(String[] nouns) {
        int max = 0;
        String rtn = "";
        for(int i = 0; i < nouns.length; i++){
            int sum = 0;
            for(int j = 0; j < nouns.length; j++){
                sum += outcast.distance(nouns[i], nouns[j]);
            }
            System.out.println(sum);
            if(max < sum) {
                max = sum;
                rtn = nouns[i];
            }
        }
        return rtn;
    } // given an array of WordNet nouns, return an outcast

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
