import edu.princeton.cs.algs4.Digraph;
import edu.princeton.cs.algs4.StdIn;
import edu.princeton.cs.algs4.StdOut;
import edu.princeton.cs.algs4.In;
import edu.princeton.cs.algs4.Stack;

public class SAP {

    private Digraph g;
    
    // constructor takes a digraph (not necessarily a DAG)
    public SAP(Digraph G) {
        if (G == null)
            throw new IllegalArgumentException("Graph must be non-null");

        g = G;
    }

    // length of shortest ancestral path between v and w; -1 if no such path
    public int length(int v, int w) {
        Vertex result = bfs(v, w);
        
        return result == null ? result.length : -1;
    }

    // a common ancestor of v and w that participates in a shortest ancestral path; -1 if no such path
    public int ancestor(int v, int w) {
        Vertex result = bfs(v, w);
        
        return result == null ? result.vertex : -1;
    }

    private static class Vertex {
        int vertex, length;
        
        Vertex(int vertex, int length) {
            this.vertex = vertex;
            this.length = length;
        }
    }

    private Vertex bfs(int v, int w) {
        // validateVertices(v, w);
        
        boolean[] vmark = new boolean[g.V()];
        boolean[] wmark = new boolean[g.V()];

        Stack<Vertex> vnodes = new Stack<>();
        Stack<Vertex> wnodes = new Stack<>();

        vmark[v] = true;
        vnodes.push(new Vertex(v, 0));

        if (vmark[w])
            return new Vertex(w, 0);

        wmark[w] = true;
        wnodes.push(new Vertex(w, 0));

        while (!vnodes.isEmpty() || !wnodes.isEmpty()) {
            if (!vnodes.isEmpty()) {
                Vertex vl = vnodes.pop();

                for (int adj : g.adj(vl.vertex)) {
                    Vertex result = checkPush(adj, vl.length, vnodes, vmark, wmark);
                    if (result != null)
                        return result;
                }
            }

            if (!wnodes.isEmpty()) {
                Vertex wl = wnodes.pop();

                for (int adj : g.adj(wl.vertex)) {
                    Vertex result = checkPush(adj, wl.length, wnodes, wmark, vmark);
                    if (result != null)
                        return result;
                }
            }
        }

        return null;
    }

    // Return non-null if an ancestor was found.
    private Vertex checkPush(int v, int length, Stack<Vertex> s, boolean[] mark, boolean[] othermark) {
        if (othermark[v])
            return new Vertex(v, length+1);           // Ancestor found.

        if (!mark[v]) {
            mark[v] = true;
            s.push(new Vertex(v, length+1));
        }

        return null;
    }

    // length of shortest ancestral path between any vertex in v and any vertex in w; -1 if no such path
    public int length(Iterable<Integer> v, Iterable<Integer> w) {
        return 0;               // TODO
    }

    // a common ancestor that participates in shortest ancestral path; -1 if no such path
    public int ancestor(Iterable<Integer> v, Iterable<Integer> w) {
        return 0;               // TODO
    }

    public static void main(String[] args) {
        In in = new In(args[0]);
        Digraph G = new Digraph(in);
        SAP sap = new SAP(G);
        while (!StdIn.isEmpty()) {
            int v = StdIn.readInt();
            int w = StdIn.readInt();
            int length   = sap.length(v, w);
            int ancestor = sap.ancestor(v, w);
            StdOut.printf("length = %d, ancestor = %d\n", length, ancestor);
        }
    }    

}

