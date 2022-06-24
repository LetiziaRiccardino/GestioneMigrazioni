package it.polito.tdp.borders.model;

import java.util.HashMap;
import java.util.Map;
import java.util.PriorityQueue;

import org.jgrapht.Graph;
import org.jgrapht.Graphs;
import org.jgrapht.graph.DefaultEdge;

public class Simulatore {
	
	//Coda degli eventi
	private PriorityQueue<Event> queue;
	
	
	//Parametri di simulazione
	private int nInizialeMigranti;
	private Country nazioneIniziale;
	
	
	//Output della simulazione
	private int nPassi; //T
	private Map<Country, Integer> persone;//per ogni nazione, quanti migranti sono stanziali in quella nazione
	            //al posto di questa mappa potevo usare una lista: List<CountryAndNumber> personeStanziali;
	
	
	//Stato del mondo smulato
	private Graph<Country, DefaultEdge> grafo;
	//Map persone Country-> Integer
	
	public Simulatore(Graph<Country, DefaultEdge> grafo) {
		super();
		this.grafo = grafo;
	}
	
	public void init(Country partenza, int migranti) {
		this.nazioneIniziale= partenza;
		this.nInizialeMigranti= migranti;
		this.queue= new PriorityQueue();
		
		this.persone= new HashMap<Country, Integer>();//la mappa riparte da zero, non mi porto dietro i residui della simulazione precedente
		for(Country c: this.grafo.vertexSet()) {
			this.persone.put(c, 0);
		}
		
		this.queue.add(new Event(1, this.nazioneIniziale, this.nInizialeMigranti));
	}
	
	public void run() {
		while(!this.queue.isEmpty()) {
			Event e= this.queue.poll();
			processEvent(e);
		}
	}

	private void processEvent(Event e) {
		int stanziali= e.getPersone()/2; //la metà diventano stanziali subito. La divisione intera approssima per difetto
		int migranti= e.getPersone() - stanziali;
		int confinanti= this.grafo.degreeOf(e.getNazione());
		int gruppoMigranti=migranti/confinanti; // ho creato i gruppi attraverso la divisione e approssimando per difetto
		stanziali += migranti%confinanti; //% è il resto
		
		this.persone.put(e.getNazione(), persone.get(e.getNazione())+ stanziali);
		this.nPassi= e.getTime();
		
		if(gruppoMigranti!=0) {
		for(Country vicino: Graphs.neighborListOf(this.grafo, e.getNazione())) {
			this.queue.add(new Event(e.getTime()+1, vicino, gruppoMigranti));
		}
		}
		
		
	}

	public int getnPassi() {
		return nPassi;
	}
	public Map<Country, Integer> getPersone() {
		return persone;
	}
	
	
	
}
