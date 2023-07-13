import edu.princeton.cs.algs4.Digraph;
import edu.princeton.cs.algs4.StdIn;
import edu.princeton.cs.algs4.StdOut;
import edu.princeton.cs.algs4.In;
import edu.princeton.cs.algs4.Queue;

import java.util.Arrays;

/**
 * SAP (Shortest Ancestral Path).
 */ 
public class SAP {

    private Digraph g;
    private int[] vmark;
    private int[] wmark;
    
    // constructor takes a digraph (not necessarily a DAG)
    public SAP(Digraph G) {
        if (G == null)
            throw new IllegalArgumentException("Graph must be non-null");

        g = G;
        vmark = new int[g.V()];
        wmark = new int[g.V()];
    }

    // length of shortest ancestral path between v and w; -1 if no such path
    public int length(int v, int w) {
        Step result = bfs(v, w);
        
        return result != null ? result.length : -1;
    }

    // a common ancestor of v and w that participates in a shortest ancestral path; -1 if no such path
    public int ancestor(int v, int w) {
        Step result = bfs(v, w);
        
        return result != null ? result.vertex : -1;
    }

    private static class Step {
        int vertex, length;
        
        Step(int vertex, int length) {
            this.vertex = vertex;
            this.length = length;
        }
    }

    // The algorithm here is to run two independent breadth-first-searches, one
    // step at a time until they collide: one search tries to visit a node that
    // the other search has already visited.
    //
    // The node they collide on is the shortest common ancestor.
    private Step bfs(int v, int w) {
        // -1 in the mark arrays means "not visited yet".  A non-negative number
        // means it was visited with that many steps.
        Arrays.fill(vmark, -1);
        Arrays.fill(wmark, -1);

        Queue<Step> vnodes = new Queue<>();
        Queue<Step> wnodes = new Queue<>();

        if (v == w)
            return new Step(v, 0);

        vmark[v] = 0;
        vnodes.enqueue(new Step(v, 0));

        wmark[w] = 0;
        wnodes.enqueue(new Step(w, 0));

        while (!vnodes.isEmpty() || !wnodes.isEmpty()) {
            Step result = step(vnodes, vmark, wmark);
            Step result2 = null;

            if (result == null)
                result2 = step(wnodes, wmark, vmark);

            Step found = result != null ? result : result2;
            if (found != null) {
                int ancestor = found.vertex;
                
                return new Step(ancestor, vmark[ancestor] + wmark[ancestor]);
            }
        }

        return null;
    }

    // Run one step of the BFS - take a node of the queue and enqueue its
    // adjacent vertices if they're not searched yet.
    //
    // Return a {@code Step} if the ancestor is found.
    private Step step(Queue<Step> s, int[] mark, int[] othermark) {
        if (!s.isEmpty()) {
            Step step = s.dequeue();
            
            for (int adj : g.adj(step.vertex)) {
                if (mark[adj] == -1) {
                    mark[adj] = step.length+1;
                    s.enqueue(new Step(adj, step.length+1));
                }

                if (othermark[adj] != -1) {
                    return new Step(adj, step.length+1);           // Ancestor found.
                }
            }            
        }

        return null;
    }

    // length of shortest ancestral path between any vertex in v and any vertex in w; -1 if no such path
    public int length(Iterable<Integer> v, Iterable<Integer> w) {
        // TODO
        return 0;
    }

    // a common ancestor that participates in shortest ancestral path; -1 if no such path
    public int ancestor(Iterable<Integer> v, Iterable<Integer> w) {
        // TODO
        return 0;
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

