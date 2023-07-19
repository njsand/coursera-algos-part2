import edu.princeton.cs.algs4.Digraph;
import edu.princeton.cs.algs4.StdIn;
import edu.princeton.cs.algs4.StdOut;
import edu.princeton.cs.algs4.In;
import edu.princeton.cs.algs4.Queue;

import java.util.Arrays;
import java.util.Collections;

/**
 * SAP (Shortest Ancestral Path) of two nodes in a DAG.
 */ 
public class SAP {

    private final Digraph g;
    private final int[] vmark;
    private final int[] wmark;

    // Queues that guide the BFS.  We run two 
    private Queue<Step> vnodes;
    private Queue<Step> wnodes;

    private Step shortest;

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
        bfs(Collections.singletonList(v), Collections.singletonList(w));
        return shortest.length == Integer.MAX_VALUE ? -1 : shortest.length;        
    }

    // a common ancestor of v and w that participates in a shortest ancestral path; -1 if no such path
    public int ancestor(int v, int w) {
        bfs(Collections.singletonList(v), Collections.singletonList(w));
        return shortest.vertex;
    }

    // length of shortest ancestral path between any vertex in v and any vertex in w; -1 if no such path
    public int length(Iterable<Integer> v, Iterable<Integer> w) {
        bfs(v, w);
        return shortest.length == Integer.MAX_VALUE ? -1 : shortest.length;
    }

    // a common ancestor that participates in shortest ancestral path; -1 if no such path
    public int ancestor(Iterable<Integer> v, Iterable<Integer> w) {
        bfs(v, w);
        return shortest.vertex;
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

        shortest = new Step(-1, Integer.MAX_VALUE);

        Step result = null;

        for (Integer vertex: v) {
            markVertexSafe(vmark, vertex, 0);
            vnodes.enqueue(new Step(vertex, 0));
        }

        for (Integer vertex: w) {
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

    private void markVertexSafe(int[] marks, Integer i, int value) {
        if (i == null)
            throw new IllegalArgumentException("Vertice must not be null");

        if (i < 0 || i >= marks.length)
            throw new IllegalArgumentException("Vertice out of bounds.");

        marks[i] = value;
    }
    
    private void bfs(Iterable<Integer> v, Iterable<Integer> w) {
        Step init = initQueues(v, w);

        if (init != null) {
            shortest = init;
            return;
        }
        
        while (!vnodes.isEmpty() || !wnodes.isEmpty()) {
            runSearchStep(vnodes, vmark, wmark);
            runSearchStep(wnodes, wmark, vmark);
        }
    }

    // Run one step of the BFS - take a node off the queue and enqueue its
    // adjacent vertices if they're not marked.
    private void runSearchStep(Queue<Step> s, int[] mark, int[] othermark) {
        if (s.isEmpty())
            return;

        Step step = s.dequeue();
            
        for (int adj : g.adj(step.vertex)) {
            if (mark[adj] == -1) {
                mark[adj] = step.length+1;
                s.enqueue(new Step(adj, step.length+1));

                if (othermark[adj] != -1) {
                    int newLength = mark[adj] + othermark[adj];
                    if (newLength < shortest.length) {
                        shortest = new Step(adj, newLength);
                    }
                }
            }
        }            
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

