package kofpgraphdrawer.model;

import java.io.*;
import java.util.ArrayList;
import java.util.Iterator;
import java.util.LinkedList;
import kofpgraphdrawer.view.View;



/*
**
**
pensavo di aggiungere una variabile booleana per togliere i metodi che ho aggiunto per lavorare con nodi e archi "importati"
ho modificato i vari cast che avevi fatto, in quanto ora le due arraylist sono tipizzate
*/
        
public class Graph /*implements GraphInterface*/{
	//FileManager file;

	protected ArrayList<Node> nodeList;
	protected ArrayList<Edge> edgeList;
        
        protected ArrayList<Edge> kfpEdgeList;
        
        //uso una linkedList per avere le stringhe del file letto
        protected LinkedList<String> listOfStringByFile = null;
	
	//Variabili che contano il numero di archi e il numero di punti all'interno del grafo. 
	
	protected int edges=0;
	protected int nodes=0;
        protected boolean isAFanPlanarGraph = false;
        //diventa true se la funzione trova che non c'è rappresentazione antifan
        protected static Graph graph = null;
	

    public Graph(/*String string*/){
       
		//file = new FileManager(fileHandler.getPathFile());
                
                
		nodeList = new ArrayList<>(0);
		edgeList = new ArrayList<>(0);

               	/*if(file.getStatus()){
			edges=0;
			nodes=0;
		}else{
			edges=nodeList.size();
			nodes=edgeList.size();
		}*/
    }
    
    
    public ArrayList<Node> getNodeList(){
        return this.nodeList;
    }
    
    public ArrayList<Edge> getEdgeList(){
        return this.edgeList;
    }
    //TO-DO: sostituire 0,0 con valori ricevuti cliccando sulla circonferenza
   /* public boolean addNode(){
    	return addNode(new Node(0, 0,"1" , "node " + (nodeList.size()+1)));
        //non va bene che cambia il label 
    }*/
    
    //per import da file, con id e label "fissati"
    public boolean addNodeImported(String ID, String label){
        return addNode(new Node(0,0,ID, label));
    }
    
    public boolean addNode(Node n){
    	nodes++;
    	n.setVectorIndex(nodeList.size());
    	return nodeList.add(n);
    }
    
    public boolean delNode(Node n){
    	nodes--;
    	int index=n.getVectorIndex();
    	for (int i=index+1; i<nodeList.size(); i++){
    		//DA TESTARE NON NE SONO CERTO, CAST MOLTO FORZATO
    		//Questa ciclo serve per scalare l'indice a tutti i nodi che seguono quello da eliminare
    		Node tempNode = (Node)nodeList.get(i);
    		tempNode.setVectorIndex(i-1);
    	}
    	if (nodeList.remove(n.getVectorIndex())==n)
    		return true;
    	else
    		return false;
    }

    public boolean addEdge(Edge e){
    	edges++;
    	e.setVectorIndex(edgeList.size()+1);
    	return edgeList.add(e);
    }
    

    public boolean delEdge(Edge e){
    	/*edges--;
        int index=e.getVectorIndex();
        for (int i=index; i<edgeList.size(); i++){
            //Questa ciclo serve per scalare l'indice a tutti i nodi che seguono quello da eliminare
            Edge tempEdge = edgeList.get(i);
            tempEdge.setVectorIndex(i-1);
        }
        if (edgeList.remove(e.getVectorIndex())==e)
            return true;
        else
            return false;
        */
        String s = e.getLabel();
        Iterator<Edge> it = edgeList.iterator();
            while (it.hasNext()) {
                Edge o = it.next();
                if (o.getLabel().equals(s)){
                it.remove();
                }
            }
            return true;
        //this.edgeList.remove(e);
    }
    
    
    //funziona
    public void stringFromFile(){
        try{
            //boolean importingStatus = false;
            this.listOfStringByFile = new LinkedList<>();
            String auxiliaryBuffer = null;
            BufferedReader importFile = View.getInstance().getGMLFileHandler().getOpenedFile();   
            while((auxiliaryBuffer=importFile.readLine())!=null){
                if(!auxiliaryBuffer.isEmpty())//se la stringa ausiliaria non contiene quei caratteri, allora salva dentro la linkedList
                    this.listOfStringByFile.add(auxiliaryBuffer.trim());
            }
                
        }
        catch(FileNotFoundException fnfe){
        }
        catch(IOException ioe){
        }
        catch(NullPointerException npe){
            View.getInstance().getInfoPanel().setTextOfLogArea("error during import, file not imported!");
        }
           //System.out.println("lettura file avvenuta correttamente");
    }
       
        /*
        l'intento è questo; nel metodo NodeFromFile io richiamo il metodo per riempire la lista con le righe del file
        scorro tale lista, in cerca del tag "node"
        se lo trovo, e nella posizione successiva trovo "["
            A questo punto, se in i+3 trovo ID e in i+4 label
            salvo le etichette
            creo il nodo e lo salvo nella nodeList!
            Altrimenti, ho ID in i+4 e in i+3 label
        */
         
        //funziona
        public boolean nodeFromFile(){
            this.stringFromFile();
            String auxiliaryId;
            String auxiliaryLabel;
            boolean result=true;
            for(int i=0; i<this.listOfStringByFile.size(); i++){
                auxiliaryId = null;
                auxiliaryLabel = null;
                if(this.listOfStringByFile.get(i).contains("node")){
                    i++;
                    
                    while(!this.listOfStringByFile.get(i).contains("]")){
                        if(this.listOfStringByFile.get(i).toLowerCase().contains("id"))
                            auxiliaryId = listOfStringByFile.get(i).substring(3);
                        else if(this.listOfStringByFile.get(i).toLowerCase().contains("label")){
                            String tempString = listOfStringByFile.get(i);
                            int firstInd = tempString.indexOf("\"");
                            int secondInd = tempString.indexOf("\"", firstInd + 1);
                            auxiliaryLabel = tempString.substring(firstInd+1, secondInd);
                        }
                            i++;
                    }
                    //In assenza di label, ne assegna una vuota
                    if(auxiliaryLabel == null)
                        auxiliaryLabel=auxiliaryId;
                    if(auxiliaryId != null)
                        result=this.addNodeImported(auxiliaryId, auxiliaryLabel);
                    
                } 
            }
            
            return result;
            
        }
        
                
        //funziona
        public boolean edgeFromFile(){
            stringFromFile();
            
            boolean result=true;
            
            Node auxiliaryNodeA = null;
            Node auxiliaryNodeB = null;
            String auxiliaryLabel = null;
            
            for(int i =0; i< this.listOfStringByFile.size(); i++){
                
                if(this.listOfStringByFile.get(i).contains("edge")){
                    i++;
                    while(!this.listOfStringByFile.get(i).contains("]")){
                        
                        if(this.listOfStringByFile.get(i).toLowerCase().contains("source")){     
                            auxiliaryLabel = this.listOfStringByFile.get(i).substring(7);
                            for(int j =0; j<nodeList.size(); j++){
                                if(nodeList.get(j).getID().equals(auxiliaryLabel))
                                    auxiliaryNodeA = nodeList.get(j);
                            }   
                        }
                        
                        if(this.listOfStringByFile.get(i).toLowerCase().contains("target")){
                            auxiliaryLabel = this.listOfStringByFile.get(i).substring(7);
                            for(int j=0; j<nodeList.size(); j++){
                                if(nodeList.get(j).getID().equals(auxiliaryLabel))
                                    auxiliaryNodeB = nodeList.get(j);
                            }
                        }
                        i++;
                    }  
                    
                    result=this.addEdge(new Edge(auxiliaryNodeA,auxiliaryNodeB)); 
                }
            }
            return result;
        }
               
	
        //forse non serve, guarda GMLFileHandler
        public boolean saveToFile(String filePath){
            boolean ris = false;
            PrintWriter printWriter = null;
            try{
                printWriter= new PrintWriter(
                        new BufferedWriter(
                                new OutputStreamWriter(new FileOutputStream(filePath),"UTF-8")),true);
                printWriter.print(this.graphToString());
                ris = true;

            }
            catch(UnsupportedEncodingException | FileNotFoundException uee){
                //View.getInstance().addTextToLogArea("Error during saving, graph not saved!");
                ris = false;
            }
            finally{
                try{
                    printWriter.close();
                    }catch(NullPointerException npe){
                        View.getInstance().getInfoPanel().setTextOfLogArea("Error during saving,\n graph not saved!");
                    } 
                }
                
            return ris;
	}
        
        
        public boolean isFanPlanar(int k){
		
		//Lista che contiene gli archi indipendenti
		ArrayList<Edge> indEdgeList;
		//arco temporaneo, serve dopo
		Edge tempEdge;
                Edge temp1Edge;
                Edge temp2Edge;
		//arco di riferimento: il primo da fissare, per poi lavorare
		Edge refEdge;
		//Variabile che conta gli archi rimossi. Utile perchè se ne rimuove 0, allora sono tutti indipendenti
		int count;
		//Variabile che conta gli archi indipendenti
		int independentEdges=0;
                //Variabile per arrestare l'analisi
                boolean toContinue=true;
		//Variabile che conta le intersezioni tra archi indipendenti
		int intersections=0;
		//Variabile risultato
		boolean ris=true;

		//Ciclo che analizzando fissando il primo arco, quello che nell'algoritmo viene definito arco di riferimento
		for (int i=0; i<this.edgeList.size(); i++){
                    //Suppongo tutti gli archi indipendenti, e poi rimuovo quelli che non lo sono
                    indEdgeList = (ArrayList<Edge>) this.edgeList.clone();
                    //arco di riferimento fissato, archi rimossi imposti uguali a 0
                    refEdge = this.edgeList.get(i);
                    count=0;
                    toContinue=true;
	
                    while(toContinue){
                        //Escludo tutti gli archi che partono da uno dei due nodi di quello fissato (tranno l'arco di riferimento, ovviamente)
                        for (int j=0; j<indEdgeList.size(); j++){
                            //Mi accerto di non lavorare sull'arco di riferimento
                            if (indEdgeList.get(j)!=this.edgeList.get(i)){
                                tempEdge = indEdgeList.get(j);
                                if(tempEdge.getNode1() == refEdge.getNode1() || tempEdge.getNode1() == refEdge.getNode2() || tempEdge.getNode2() == refEdge.getNode1() || tempEdge.getNode2() == refEdge.getNode2()){
                                    indEdgeList.remove(tempEdge);
                                    count++;
                                }
                            }
                        }
                      
                        if (count==0){
                            toContinue = false;
                        }
                        count=0;
                    }
                   
                    
                    //Ripeto lo stesso procedimento per tutti le possibili coppie di archi rimanenti
                    for (int j=0; j<indEdgeList.size(); j++){
                        temp1Edge = indEdgeList.get(j);
                        count=0;
                        toContinue=true;
                        while(toContinue){
                            for (int n=0; n<indEdgeList.size(); n++)
                                //Mi accerto di non lavorare sullo stesso arco
                                if (j!=n){
                                    temp2Edge = indEdgeList.get(n);
                                    if(temp1Edge.getNode1() == temp2Edge.getNode1() || temp1Edge.getNode1() == temp2Edge.getNode2() || temp1Edge.getNode2() == temp2Edge.getNode1() || temp1Edge.getNode2() == temp2Edge.getNode2()){
                                        indEdgeList.remove(temp2Edge);
                                        count++;
                                    }
                                }
                            if (count==0){
                                toContinue = false;
                            }
                            count=0;
                        }
                    }
			
                    /*if(indEdgeList.size()>independentEdges){
                        independentEdges = indEdgeList.size();
                    }//Fine dell'analisi per l'arco fissato*/

                    /*
                    //Conta il numero di intersezioni tra gli archi indipendenti
                    for (int l=0; l<indEdgeList.size()-1; l++){
			for (int m=l+1; m<indEdgeList.size(); m++){
                            if(indEdgeList.get(l).cross(indEdgeList.get(m))){
				intersections++;
                            }
                        }
                    }*/
                                      
                    for (int l=0; l<indEdgeList.size(); l++){
                        if (indEdgeList.get(l).cross(refEdge) && indEdgeList.get(l)!=refEdge){
                            intersections++;
                        }
                    }
                    
                    if(intersections>k){
                    	ris=false;
                        this.kfpEdgeList = (ArrayList<Edge>) indEdgeList.clone();
			break;
                    }else{
			intersections=0;
                    }
		}
		return ris;
	}
        
        public ArrayList<Edge> getKfpEdgeList(){
            return this.kfpEdgeList;
        }
        
        //Metodo che effettua una clicque su tutti i nodi passati come argomento
        public boolean Clique(Node[] n){
            boolean ris = true;
            for (int i=0; i<n.length-1; i++){
                for (int j=i+1; j<n.length; j++){
                    if(!this.addEdge(new Edge(n[i], n[j]))){
                        ris=false;
                        //Si potrebbe avvisare l'utente dicendo l'arco non creato
                    }
                }
            }
            return ris;
        }
            
        //Metodo che effettua una clicque su tutti i nodi del grafo
        public boolean Clique(){
            boolean ris = true;
            boolean toAdd;
            for (int i=0; i<nodeList.size(); i++){
                for (int j=i+1; j<nodeList.size(); j++){
                    //Devo accertarmi che l'arco che voglio inserire non sia già presente
                    toAdd=true;
                    for(int k=0; k<edgeList.size(); k++){
                        if((edgeList.get(k).getNode1()==nodeList.get(i) && edgeList.get(k).getNode2()==nodeList.get(j)) || (edgeList.get(k).getNode1()==nodeList.get(j) && edgeList.get(k).getNode2()==nodeList.get(i))){
                            toAdd=false;
                        }
                    }
                    if(toAdd == true)
                        this.addEdge(new Edge(nodeList.get(i), nodeList.get(j)));
                }
            }
            return ris;
        }
        
        
        public String graphToString(){
    	String result = "graph\r\n[\r\n";
    	for (int i=0; i<nodeList.size(); i++){
    		Node tempNode = nodeList.get(i);
    		result+="\tnode\r\n\t[\r\n" + "\t\tid " + tempNode.getID() + "\r\n\t\tlabel " + "\"" + tempNode.getLabel() + "\"\r\n\t]\r\n";
    	}

    	for (int i=0; i<edgeList.size(); i++){
    		Edge tempEdge = edgeList.get(i);
    		result+="\tedge\r\n\t[\r\n" + "\t\tsource " + tempEdge.getNode1().getID() + "\r\n\t\ttarget " + tempEdge.getNode2().getID() + "\r\n\t\tlabel " + "\"" + tempEdge.getLabel() + "\"\r\n\t]\r\n";
    	}
    	result += "]";
    	return result;
        }
        
}