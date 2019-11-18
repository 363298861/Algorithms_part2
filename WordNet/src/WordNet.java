
import edu.princeton.cs.algs4.Digraph;
import edu.princeton.cs.algs4.DirectedCycle;
import edu.princeton.cs.algs4.In;
import java.util.ArrayList;
import java.util.TreeMap;


/******************************************************************************
 *  Author:  Huang Zhiyuan
 *  Start Date: 8th Jan, 2018
 *  End Date:
 *
 *  What do I learn after I finish:
 ******************************************************************************/

public class WordNet {
    /*private final Digraph wordNet;
    private final String[] word;

    // constructor takes the name of the two input files
    public WordNet(String synsets, String hypernyms) {
        int V = 0;
        In in1 = new In(synsets);
        In in2 = new In(hypernyms);
        while(in2.hasNextLine()){
            String s = in2.readLine();
            String[] fields = s.split(",");
            for(int i = 0; i < fields.length; i++){
                if(V < Integer.parseInt(fields[i]))
                    V = Integer.parseInt(fields[i]);
            }
        }
        wordNet = new Digraph(V + 1);
        In in3 = new In(hypernyms);
        while (in3.hasNextLine()) {
            String s = in3.readLine();
            String[] fields = s.split(",");
            for(int i = 1; i < fields.length; i++){
                wordNet.addEdge(Integer.parseInt(fields[0]), Integer.parseInt(fields[i]));
            }
        }
        word = new String[V + 1];
        int n = 0;
        while (in1.hasNextLine()) {
            String s = in1.readLine();
            String[] l = s.split(",");
            word[n++] = l[1];
        }
    }

    // returns all WordNet nouns
    public Iterable<String> nouns()
    {
        Stack<String> s = new Stack<>();
        for(int i = 0; i < word.length; i++)
            s.push(word[i]);
        return s;
    }

    // is the word a WordNet noun?
    public boolean isNoun(String word)
    {
        for(int i = 0; i < this.word.length; i++) {
            if (this.word[i].contains(word))//this.word[i].equals(word))
                return true;
        }
        return false;
    }

    // distance between nounA and nounB (defined below)
    public int distance(String nounA, String nounB)
    {
        SAP sap = new SAP(wordNet);

        for(int i = 0; i < word.length; i++){
            if(word[i].contains(nounA)){
                for(int j = 0; j < word.length; j++){
                    if(word[j].contains(nounB))
                        return sap.length(i, j);
                }
            }
        }
        return -1;
    }

    // a synset (second field of synsets.txt) that is the common ancestor of nounA and nounB
    // in a shortest ancestral path (defined below)
    public String sap(String nounA, String nounB)
    {
        SAP sap = new SAP(wordNet);

        String anc = "";
        for(int i = 0; i < word.length; i++){
            if(word[i].contains(nounA)){
                for(int j = 0; j < word.length; j++){
                    if(word[j].contains(nounB))
                        anc = word[sap.ancestor(i, j)];
                }
            }
        }
        return anc;
    }*/

    private Digraph net;
    private TreeMap<String, ArrayList<Integer>> map;
    private ArrayList<String> list;
    private int V;
    private SAP sap;

    // constructor takes the name of the two input files
    public WordNet(String synsets, String hypernyms) {
        if (synsets == null || hypernyms == null)
            throw new NullPointerException();

        initSynsets(synsets);
        initHypernyms(hypernyms);
        // is a DAG
        DirectedCycle cycle = new DirectedCycle(net);
        if (cycle.hasCycle())
            throw new IllegalArgumentException("have cycle");
        // is one root
        for (int i = 0, count = 0; i < V; i++) {
            if (net.outdegree(i) == 0) {
                if (count == 0) count++;
                else throw new IllegalArgumentException("too many root");
            }
        }
        // init sap
        sap = new SAP(net);
    }

    private void initSynsets(String synsets) {
        V = 0;
        list = new ArrayList<>();
        map = new TreeMap<>();

        In in = new In(synsets);
        String[] fields, keySet;
        String key;
        int val;
        ArrayList<Integer> temp;
        for (String line = in.readLine(); line != null; line = in.readLine(), V++) {
            fields = line.split(",");
            keySet = fields[1].split(" ");
            val = Integer.parseInt(fields[0]);
            for (int j = 0; j < keySet.length; j++) {
                key = keySet[j];
//                map.put(key, val);
                if (map.containsKey(key)) {
                    temp = map.get(key);
                    if (temp.add(val))
                        map.put(key, temp);
                }
                else {
                    temp = new ArrayList<>();
                    if (temp.add(val))
                        map.put(key, temp);
                }

            }
            list.add(fields[1]);
        }
    }

    private void initHypernyms(String hypernyms) {
        net = new Digraph(V);
        In in = new In(hypernyms);
        String[] fields;
        for (String line = in.readLine(); line != null; line = in.readLine()) {
            fields = line.split(",");
            for (int i = 1, k = Integer.parseInt(fields[0]); i < fields.length; i++)
                net.addEdge(k, Integer.parseInt(fields[i]));
        }
    }

    // returns all WordNet nouns
    public Iterable<String> nouns() {
        return map.keySet();
    }

    // is the word a WordNet noun?
    public boolean isNoun(String word) {
        if (word == null)
            throw new NullPointerException();
        return map.containsKey(word);
    }

    // distance between nounA and nounB (defined below)
    public int distance(String nounA, String nounB) {
        if (!isNoun(nounA) || !isNoun(nounB))
            throw new IllegalArgumentException();
        return helper(nounA, nounB)[0];
    }

    // a synset (second field of synsets.txt) that is the common ancestor of nounA and nounB
    // in a shortest ancestral path (defined below)
    public String sap(String nounA, String nounB) {
        if (!isNoun(nounA) || !isNoun(nounB))
            throw new IllegalArgumentException();
        return list.get(helper(nounA, nounB)[1]);
    }

    // helper func for get distance, sap
    private int[] helper(String nounA, String nounB) {
        int length = Integer.MAX_VALUE, temp = 0, ancestor = 0;
        for (int val1 : map.get(nounA)) {
            for (int val2 : map.get(nounB)) {
                temp = sap.length(val1, val2);

                if (temp < length) {
                    length = temp;
                    ancestor = sap.ancestor(val1, val2);
                }
            }
        }
        return new int[] {length, ancestor};
    }

    // do unit testing of this class
    public static void main(String[] args) {
        WordNet w = new WordNet(args[0], args[1]);

        //System.out.println(w.isNoun("1870s"));
        //System.out.println(w.isNoun("koala koala_bear kangaroo_bear native_bear Phascolarctos_cinereus"));
        //System.out.println(w.isNoun("readership"));
        System.out.println(w.distance("readership", "koala koala_bear kangaroo_bear native_bear Phascolarctos_cinereus"));

        //System.out.println(w.sap("readership", "koala koala_bear kangaroo_bear native_bear Phascolarctos_cinereus"));

        //java-algs4 WordNet synsets.txt hypernyms.txt
    }
}
