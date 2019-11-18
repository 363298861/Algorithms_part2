import edu.princeton.cs.algs4.BreadthFirstDirectedPaths;
import edu.princeton.cs.algs4.Digraph;

/******************************************************************************
 *  Author:  Huang Zhiyuan
 *  Start Date: 8th Jan, 2018
 *  End Date:
 *
 *  What do I learn after I finish:
 ******************************************************************************/

public class SAP {

    /*private final Digraph d;


    // constructor takes a digraph (not necessarily a DAG)
    public SAP(Digraph G) { d = new Digraph(G); }

    // length of shortest ancestral path between v and w; -1 if no such path
    public int length(int v, int w) {
        if(v > d.V() || w > d.V())
            throw new IllegalArgumentException();
        if(ancestor(v, w) == -1)
            return -1;
        else{
            BreadthFirstDirectedPaths path1 = new BreadthFirstDirectedPaths(d, v);
            BreadthFirstDirectedPaths path2 = new BreadthFirstDirectedPaths(d, w);
            return path1.distTo(ancestor(v, w)) + path2.distTo(ancestor(v, w));
        }
    }

    // a common ancestor of v and w that participates in a shortest ancestral path; -1 if no such path
    public int ancestor(int v, int w) {
        if(v > d.V() || w > d.V())
            throw new IllegalArgumentException();
        BreadthFirstDirectedPaths path1 = new BreadthFirstDirectedPaths(d, v);
        BreadthFirstDirectedPaths path2 = new BreadthFirstDirectedPaths(d, w);

        Topological top = new Topological(d);
        if(top.hasOrder()){
            if(path1.hasPathTo(w))
                return w;
            else if (path2.hasPathTo(v))
                return v;
            else{
                int rank;
                int x;
                System.out.println(top.rank(v) + " rank: v");
                System.out.println(top.rank(w) + " rank: w");
                if(top.rank(v) > top.rank(w)){
                    x = v;
                    rank = top.rank(v) + 1;
                }else{
                    x = w;
                    rank = top.rank(w) + 1;
                }
                while(!(path1.hasPathTo(x) && path2.hasPathTo(x))){
                    for(int i = 0; i < d.V(); i++){
                        if(top.rank(i) == rank) {
                            x = i;
                            break;
                        }
                    }
                    rank++;
                }
                return x;
            }
        }else{
            KosarajuSharirSCC k = new KosarajuSharirSCC(d);
            int vrank = k.id(v);
            int wrank = k.id(w);
            int ancestor = -1;
            for(int i = 0; i < d.V(); i ++){
                if(k.id(i) <= vrank && k.id(i) <= wrank){
                    if(path1.hasPathTo(i) && path2.hasPathTo(i)){
                        if(ancestor == -1)
                            ancestor = i;
                        if(path1.distTo(i) + path2.distTo(i) < path1.distTo(ancestor) + path2.distTo(ancestor))
                            ancestor = i;
                    }
                }
            }
            return ancestor;
        }
    }


    // length of shortest ancestral path between any vertex in v and any vertex in w; -1 if no such path
    public int length(Iterable<Integer> v, Iterable<Integer> w) {
        validate(v, w);
        int leng = 0;
        for(int i : v){
            for(int j : w){
                leng = length(i, j); break;
            }break;
        }
        for(int i : v){
            for(int j : w){
                if(length(i, j) <= leng){
                    leng = length(i, j);
                    //vertex = ancestor(i, j);
                }
            }
        }
        return leng;
    }

    // a common ancestor that participates in shortest ancestral path; -1 if no such path
    public int ancestor(Iterable<Integer> v, Iterable<Integer> w) {
        validate(v, w);
        int vertex = 0;
        int leng = 0;
        for(int i : v){
            for(int j : w){
                leng = length(i, j); break;
            }break;
        }
        for(int i : v){
            for(int j : w){
                if(length(i, j) <= leng){
                    leng = length(i, j);
                    vertex = ancestor(i, j);
                }
            }
        }
        return vertex;
    }

    private void validate(Iterable<Integer> v, Iterable<Integer> w){
        if(v == null || w == null)
            throw new IllegalArgumentException();
        for(Integer i : v){
            if (i == null || i > d.V())
                throw new IllegalArgumentException();
        }
        for(Integer i : w){
            if (i == null || i > d.V())
                throw new IllegalArgumentException();
        }
    }*/

    private Digraph graph;

    public SAP(Digraph G){
        graph = new Digraph(G);
    }

    private int[] helper(BreadthFirstDirectedPaths bfs1, BreadthFirstDirectedPaths bfs2){
        int length = Integer.MAX_VALUE;
        int ancestor = 0;
        for(int i = 0, tem = 0; i < graph.V(); i++){
            if(bfs1.hasPathTo(i) && bfs2.hasPathTo(i)){
                tem = bfs1.distTo(i) + bfs2.distTo(i);
                if(tem < length){
                    length = tem;
                    ancestor = i;
                }
            }
        }
        return new int[] {length, ancestor};
    }

    public int length(int v, int w){
        if (v < 0 || v > graph.V() || w < 0 || w > graph.V())
            throw new IllegalArgumentException();
        BreadthFirstDirectedPaths bfs1 = new BreadthFirstDirectedPaths(graph, v);
        BreadthFirstDirectedPaths bfs2 = new BreadthFirstDirectedPaths(graph, w);
        return modify(helper(bfs1, bfs2))[0];
    }

    public int ancestor(int v, int w) {
        if (v < 0 || v > graph.V() || w < 0 || w > graph.V())
            throw new IllegalArgumentException();
        BreadthFirstDirectedPaths bfs1 = new BreadthFirstDirectedPaths(graph, v);
        BreadthFirstDirectedPaths bfs2 = new BreadthFirstDirectedPaths(graph, w);
        return modify(helper(bfs1, bfs2))[1];
    }

    private int[] modify(int[] a){
        if(a[0] == Integer.MAX_VALUE){
            a[0] = -1;
            a[1] = -1;
        }
        return a;
    }
    public int length(Iterable<Integer> v, Iterable<Integer> w) {
        if (v == null || w == null)
            throw new IllegalArgumentException();
        for(Integer i : v) {
            if (i == null)
                throw new IllegalArgumentException();
        }
        for(Integer i : w){
            if(i == null)
                throw new IllegalArgumentException();
        }
        BreadthFirstDirectedPaths bfs1 = new BreadthFirstDirectedPaths(graph, v);
        BreadthFirstDirectedPaths bfs2 = new BreadthFirstDirectedPaths(graph, w);
        return modify(helper(bfs1, bfs2))[0];
    }

    public int ancestor(Iterable<Integer> v, Iterable<Integer> w) {
        if (v == null || w == null)
            throw new IllegalArgumentException();
        for(Integer i : v) {
            if (i == null)
                throw new IllegalArgumentException();
        }
        for(Integer i : w){
            if(i == null)
                throw new IllegalArgumentException();
        }

        BreadthFirstDirectedPaths bfs1 = new BreadthFirstDirectedPaths(graph, v);
        BreadthFirstDirectedPaths bfs2 = new BreadthFirstDirectedPaths(graph, w);
        return modify(helper(bfs1, bfs2))[1];
    }

    // do unit testing of this class
    public static void main(String[] args) {
        Digraph di = new Digraph(25);
        di.addEdge(1, 0);
        di.addEdge(2, 0);
        di.addEdge(3, 1);
        di.addEdge(4, 1);
        di.addEdge(5, 2);
        di.addEdge(6, 2);
        di.addEdge(7, 3);
        di.addEdge(8, 3);
        di.addEdge(9, 3);
        di.addEdge(10, 5);
        di.addEdge(11, 5);
        di.addEdge(12, 5);
        di.addEdge(13, 7);
        di.addEdge(14, 7);
        di.addEdge(15, 9);
        di.addEdge(16, 9);
        di.addEdge(17, 10);
        di.addEdge(18, 10);
        di.addEdge(19, 12);
        di.addEdge(20, 12);
        di.addEdge(21, 16);
        di.addEdge(22, 16);
        di.addEdge(23, 20);
        di.addEdge(24, 20);
        SAP sap = new SAP(di);


        Digraph d2 = new Digraph(6);
        d2.addEdge(1,0);
        d2.addEdge(1, 2);
        d2.addEdge(2, 3);
        d2.addEdge(3, 4);
        d2.addEdge(4, 5);
        d2.addEdge(5, 0);
        SAP sap2 = new SAP(d2);
        System.out.println(sap2.ancestor(0, 3) + "ttt");
        System.out.println(sap2.length(0, 3));

        Digraph d3 = new Digraph(15);
        d3.addEdge(1,2);
        d3.addEdge(2,3);
        d3.addEdge(3,4);
        d3.addEdge(4,5);
        d3.addEdge(5,6);
        d3.addEdge(6,1);
        d3.addEdge(7,8);
        d3.addEdge(8,9);
        d3.addEdge(9,10);
        d3.addEdge(10,11);
        d3.addEdge(11,12);
        d3.addEdge(12,8);
        d3.addEdge(13,14);
        d3.addEdge(14,0);
        d3.addEdge(0,11);
        SAP sap3 = new SAP(d3);
        System.out.println(sap3.length(7, 9));
        System.out.println(sap3.ancestor(6, 1));

        Digraph d4 = new Digraph(10);
        d4.addEdge(1, 2);
        d4.addEdge(1, 7);
        d4.addEdge(2, 3);
        d4.addEdge(3, 4);
        d4.addEdge(4, 5);
        d4.addEdge(5, 6);
        d4.addEdge(7, 8);
        d4.addEdge(9, 0);
        d4.addEdge(8, 6);
        d4.addEdge(0, 8);
        SAP sap4 = new SAP(d4);
        System.out.println(sap4.length(6, 0) + "test");
        System.out.println(sap4.ancestor(6, 0));

        Digraph d5 = new Digraph(22);
        d5.addEdge(7, 8);
        d5.addEdge(8, 9);
        d5.addEdge(9, 10);
        d5.addEdge(8, 14);
        d5.addEdge(11, 12);
        d5.addEdge(12, 13);
        d5.addEdge(12, 19);
        d5.addEdge(13, 10);
        d5.addEdge(14, 15);
        d5.addEdge(15, 16);
        d5.addEdge(16, 17);
        d5.addEdge(17, 18);
        d5.addEdge(18, 12);
        d5.addEdge(19, 20);
        d5.addEdge(20, 21);
        d5.addEdge(21, 0);
        d5.addEdge(0, 9);
        SAP sap5 = new SAP(d5);
        System.out.println(sap5.length(18, 10));
        System.out.println(sap5.ancestor(18, 10));
        System.out.println("----------");

        Digraph d1 = new Digraph(13);
        d1.addEdge(7, 3);
        d1.addEdge(8, 3);
        d1.addEdge(3, 1);
        d1.addEdge(4, 1);
        d1.addEdge(5, 1);
        d1.addEdge(9, 5);
        d1.addEdge(10, 5);
        d1.addEdge(11, 10);
        d1.addEdge(12, 10);
        d1.addEdge(1, 0);
        d1.addEdge(2, 0);
        SAP sap6 = new SAP(d1);
        System.out.println(sap6.ancestor(11, 3));
        System.out.println(sap6.ancestor(10, 4));
        System.out.println(sap6.length(10, 4));


    }
}
