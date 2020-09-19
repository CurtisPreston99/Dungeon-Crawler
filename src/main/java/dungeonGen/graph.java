package dungeonGen;

import java.util.ArrayList;
import java.util.HashMap;

public class graph {
    ArrayList<String> nodes;

    // ArrayList<String> nodes;
    HashMap<String,ArrayList<String>> edges;

    public graph(){
        nodes=new ArrayList<>();
        edges=new HashMap<>();
    }

    public void addNode(String Name){
        nodes.add(Name);
        edges.put(Name, new ArrayList<String>());
    }

    public void addEdge(String FName,String TName){
        ArrayList<String> curEdge=edges.get(FName);
        if(!curEdge.contains(TName)){
            curEdge.add(TName);
        }
    }
    class edge{
        String From;
        String Too;
        int weight;
        public edge(String From,String Too,int weight){
            this.From=From;
            this.Too=Too;
            this.weight=weight;
        }

        @Override
        public boolean equals(Object obj) {
            edge e=(edge)obj;
            return e.From==this.From && e.Too==this.Too && e.weight==this.weight;
        }
    }
}
