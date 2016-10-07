package Assignment;

import java.util.Vector;

public class PushRelabel {
	
	public static class Edge
	{
	    int flow;
	    int capacity;
	    int u, v;
	 
	    Edge(int flow, int capacity, int u, int v)
	    {
	        this.flow = flow;
	        this.capacity = capacity;
	        this.u = u;
	        this.v = v;
	    }
	}
	
	public static class Vertex
	{
	    int h;
	    int e_flow;
	 
	    Vertex(int h, int e_flow)
	    {
	        this.h = h;
	        this.e_flow = e_flow;
	    }
	}
	
	public static class Graph
	{
	    int V;    // Number of vertices
	    Vector<Vertex> ver = new Vector<>();
	    Vector<Edge> edge = new Vector<>();
	 
	    // Function to push excess flow from u
	    boolean push(int u)
	    {
	        // Traverse through all edges to find an adjacent (of u)
	        // to which flow can be pushed
	        for (int i = 0; i < edge.size(); i++)
	        {
	            // Checks u of current edge is same as given
	            // overflowing vertex
	            if (edge.get(i).u == u)
	            {
	                // if flow is equal to capacity then no push
	                // is possible
	                if (edge.get(i).flow == edge.get(i).capacity)
	                    continue;
	     
	                // Push is only possible if height of adjacent
	                // is smaller than height of overflowing vertex
	                if (ver.get(u).h > ver.get(edge.get(i).v).h)
	                {
	                    // Flow to be pushed is equal to minimum of
	                    // remaining flow on edge and excess flow.
	                    int flow = Math.min(edge.get(i).capacity - edge.get(i).flow,
	                                   ver.get(u).e_flow);
	     
	                    // Reduce excess flow for overflowing vertex
	                    ver.get(u).e_flow -= flow;
	     
	                    // Increase excess flow for adjacent
	                    ver.get(edge.get(i).v).e_flow += flow;
	     
	                    // Add residual flow (With capacity 0 and negative
	                    // flow)
	                    edge.get(i).flow += flow;
	     
	                    updateReverseEdgeFlow(i, flow);
	     
	                    return true;
	                }
	            }
	        }
	        return false;
	    }
	 
	    // Function to relabel a vertex u
	    void relabel(int u)
	    {
	        // Initialize minimum height of an adjacent
	        int mh = Integer.MAX_VALUE;
	     
	        // Find the adjacent with minimum height
	        for (int i = 0; i < edge.size(); i++)
	        {
	            if (edge.get(i).u == u)
	            {
	                // if flow is equal to capacity then no
	                // relabeling
	                if (edge.get(i).flow == edge.get(i).capacity)
	                    continue;
	     
	                // Update minimum height
	                if (ver.get(edge.get(i).v).h < mh)
	                {
	                    mh = ver.get(edge.get(i).v).h;
	     
	                    // updating height of u
	                    ver.get(u).h = mh + 1;
	                }
	            }
	        }
	    }
	 
	    // This function is called to initialize
	    // preflow
	    void preflow(int s)
	    {
	        // Making h of source Vertex equal to no. of vertices
	        // Height of other vertices is 0.
	    	ver.get(s).h = ver.size();
	     
	        //
	        for (int i = 0; i < edge.size(); i++)
	        {
	            // If current edge goes from source
	            if (edge.get(i).u == s)
	            {
	                // Flow is equal to capacity
	                edge.get(i).flow = edge.get(i).capacity;
	     
	                // Initialize excess flow for adjacent v
	                ver.get(edge.get(i).v).e_flow += edge.get(i).flow;
	                //ver[edge[i].v].e_flow += edge[i].flow;
	     
	                // Add an edge from v to s in residual graph with
	                // capacity equal to 0
	                edge.add(new Edge(-edge.get(i).flow, 0, edge.get(i).v, s));
	            }
	        }
	    }
	    
	 // returns index of overflowing Vertex
	    int overFlowVertex(Vector<Vertex> ver)
	    {
	        for (int i = 1; i < ver.size() - 1; i++)
	           if (ver.get(i).e_flow > 0)
	                return i;
	     
	        // -1 if no overflowing Vertex
	        return -1;
	    }
	 
	    // Function to reverse edge
	    void updateReverseEdgeFlow(int i, int flow)
	    {
	        int u = edge.get(i).v, v = edge.get(i).u;
	     
	        for (int j = 0; j < edge.size(); j++)
	        {
	            if (edge.get(j).v == v && edge.get(j).u == u)
	            {
	                edge.get(j).flow -= flow;
	                return;
	            }
	        }
	     
	        // adding reverse Edge in residual graph
	        Edge e = new Edge(0, flow, u, v);
	        edge.add(e);
	    }
	 
	    Graph(int V)
	    {
	        this.V = V;
	     
	        // all vertices are initialized with 0 height
	        // and 0 excess flow
	        for (int i = 0; i < V; i++)
	        	ver.add(new Vertex(0, 0));
	        
	    }
	 
	    // function to add an edge to graph
	    void addEdge(int u, int v, int capacity)
	    {
	        // flow is initialized with 0 for all edge
	        edge.add(new Edge(0, capacity, u, v));
	    }
	 
	    // returns maximum flow from s to t
	    int getMaxFlow(int s, int t)
	    {
	        preflow(s);
	     
	        // loop untill none of the Vertex is in overflow
	        while (overFlowVertex(ver) != -1)
	        {
	            int u = overFlowVertex(ver);
	            if (!push(u))
	                relabel(u);
	        }
	     
	        // ver.back() returns last Vertex, whose
	        // e_flow will be final maximum flow
	        return ver.lastElement().e_flow;
	    }
	}

	public static void main(String[] args) {
		int V = 6;
		Graph g=new Graph(V);
	    //Graph g(V);
	 
	    // Creating above shown flow network
	    g.addEdge(0, 1, 16);
	    g.addEdge(0, 2, 13);
	    g.addEdge(1, 2, 10);
	    g.addEdge(2, 1, 4);
	    g.addEdge(1, 3, 12);
	    g.addEdge(2, 4, 14);
	    g.addEdge(3, 2, 9);
	    g.addEdge(3, 5, 20);
	    g.addEdge(4, 3, 7);
	    g.addEdge(4, 5, 4);
	 
	    // Initialize source and sink
	    int s = 0, t = 5;
	 
	    System.out.println("Maximum flow is "+g.getMaxFlow(s, t));
	}

}
