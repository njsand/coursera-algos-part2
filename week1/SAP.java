import edu.princeton.cs.algs4.Digraph;
import edu.princeton.cs.algs4.StdIn;
import edu.princeton.cs.algs4.StdOut;
import edu.princeton.cs.algs4.In;
import edu.princeton.cs.algs4.Queue;

import java.util.Arrays;

/**
 * SAP (Shortest Ancestral Path) of two nodes in a DAG.
 *
 * The algorithm here is to run two independent breadth-first-searches, one step at a time until they collide.  The
 * first search starts at vertex {@code v} and the second at vertex {@code w}.

 * When either search tries to visit a node that the other search has already visited, the node the searches collide on
 * is the shortest common ancestor.
 */ 
public class SAP {

    private Digraph g;
    private int[] vmark;
    private int[] wmark;

    // Queues that guide the BFS.  We run two 
    private Queue<Step> vnodes;
    private Queue<Step> wnodes;
    
    // constructor takes a digraph (not necessarily a DAG)
    public SAP(Digraph G) {
        if (G == null)
            throw new IllegalArgumentException("Graph must be non-null");

        g = new Digraph(G);
        vmark = new int[g.V()];
        wmark = new int[g.V()];
    }

    // length of shortest ancestral path between v and w; -1 if no such path
    public int length(int v, int w) {
        return bfs(Arrays.asList(v), Arrays.asList(w)).length;
    }

    // a common ancestor of v and w that participates in a shortest ancestral path; -1 if no such path
    public int ancestor(int v, int w) {
        return bfs(Arrays.asList(v), Arrays.asList(w)).vertex;
    }

    // length of shortest ancestral path between any vertex in v and any vertex in w; -1 if no such path
    public int length(Iterable<Integer> v, Iterable<Integer> w) {
        return bfs(v, w).length;
    }

    // a common ancestor that participates in shortest ancestral path; -1 if no such path
    public int ancestor(Iterable<Integer> v, Iterable<Integer> w) {
        return bfs(v, w).vertex;
    }
    
    private static class Step {
        int vertex, length;
        
        Step(int vertex, int length) {
            this.vertex = vertex;
            this.length = length;
        }
    }

    private Step initQueues(Iterable<Integer> v, Iterable<Integer> w) {
        if (v == null || w == null)
            throw new IllegalArgumentException("Vertice set must not be null");

        Arrays.fill(vmark, -1);
        Arrays.fill(wmark, -1);

        vnodes = new Queue<>();
        wnodes = new Queue<>();

        Step result = null;

        for (int vertex: v) {
            markVertexSafe(vmark, vertex, 0);
            vnodes.enqueue(new Step(vertex, 0));
        }

        for (int vertex: w) {
            markVertexSafe(wmark, vertex, 0);
            wnodes.enqueue(new Step(vertex, 0));

            // This checks for overlap in the starting sets.  If so, we've already found our ancestor.
            if (vmark[vertex] != -1) {
                result = new Step(vertex, 0);
                break;
            }
        }

        return result;
    }

    private void markVertexSafe(int[] marks, int v, int value) {
        if (v < 0 || v >= marks.length)
            throw new IllegalArgumentException("Vertice out of bounds.");

        marks[v] = value;
    }
    
    private Step bfs(Iterable<Integer> v, Iterable<Integer> w) {
        Step init = initQueues(v, w);

        if (init != null)
            return init;
        
        int shortestVertex = -1;
        int length = Integer.MAX_VALUE;
        
        while (!vnodes.isEmpty() || !wnodes.isEmpty()) {
            Step result = doSearchStep(vnodes, vmark, wmark);
            Step result2 = null;

            if (result == null)
                result2 = doSearchStep(wnodes, wmark, vmark);

            Step found = result != null ? result : result2;
            if (found != null) {
                int ancestor = found.vertex;

                // Total path length is the sum of each search's path.
                return new Step(ancestor, vmark[ancestor] + wmark[ancestor]);
            }
        }

        return new Step(-1, -1); // No ancestor found.
    }

    // Run one step of the BFS - take a node off the queue and enqueue its
    // adjacent vertices if they're not marked.
    //
    // Return a {@code Step} if the ancestor is found.
    private Step doSearchStep(Queue<Step> s, int[] mark, int[] othermark) {
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

