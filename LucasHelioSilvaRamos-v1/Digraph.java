import java.util.HashMap;
import java.util.Queue;
import java.util.LinkedList;

//import static jdk.vm.ci.aarch64.AArch64.v3;

public class Digraph {
	// vertex set
    private HashMap< Integer, Vertex> vertex_set;

    public Digraph() {
        vertex_set = new HashMap< Integer, Vertex>();
    }

    public void print() {
        System.out.printf("\n\nDados do Grafo, grau máximo de saída %d", this.max_degree());

        for( Vertex v : vertex_set.values())
            v.print();

        if( this.is_undirected() )
            System.out.println("\nNão direcionado: verdadeiro");
        else
            System.out.println("\nNão direcionado: falso");
    }
    
    public void add_vertex( int id ) {//adiciona vertice
        if ( id < 1 || this.vertex_set.get( id ) == null ) {
            Vertex v = new Vertex( id );
            vertex_set.put( v.id, v );
            System.out.println("adicionado o vertice: "+ id);
        }
        /*else
            System.out.println("Id inválido ou já utilizado!");//talvez retirar esse print*/
    }
    
    public void add_arc( Integer id1, Integer id2) {//adiciona aresta direcionada
        Vertex v1 = vertex_set.get( id1 );
        Vertex v2 = vertex_set.get( id2 );
        if ( v1 == null || v2 == null ) {
            System.out.println("Vértice inexistente!");
            return;
        }
        v1.add_neighbor( v2 );
    }

    public void add_edge( Integer id1, Integer id2) {//adiciona aresta
        Vertex v1 = vertex_set.get( id1 );
        Vertex v2 = vertex_set.get( id2 );
        if ( v1 == null || v2 == null ) {
            System.out.printf("Vértice inexistente!");
            return;
        }
        v1.add_neighbor( v2 );
        v2.add_neighbor( v1 );
        System.out.println("adicionado o vertice: "+id2+" como vizinho do vertice: "+id1);

// ou
//        add_arc( id1, id2 );
//        add_arc( id2, id1 );
    }
    
    public void del_vertex( int id ) {
        for( Vertex v1 : vertex_set.values()) {
			v1.nbhood.remove( id );
        }
        vertex_set.remove( id );
    }
    
    // maximum outdegree
    public int max_degree() {
        int max = -1;
        for( Vertex v1 : vertex_set.values()) {
            if( v1.degree() > max )
                max = v1.degree();
		}
        return max;
    }


    public int identifica_semGarra(){


        //--------------------------- SEÇÃO DE VERIFICAÇÃO ------------------------

        if(vertex_set.size()<4){
            System.out.println("\n\n----------- É um Grafo Sem Garra -----------");
            System.out.println("\nO Grafo nao possui vertices suficientes para ter uma Garra");
            return 1;
        }

        for(Vertex v1:vertex_set.values()){



            for(Vertex v2 : v1.nbhood.values()) { //pegamos os vizinhos de v1
                if (v1.id != v2.id) {

                    for (Vertex v3 : v1.nbhood.values())//pegamos o id dos vizinhos de v1
                    {
                        if ((v1.id != v3.id) && (v2.id != v3.id)) {

                            for (Vertex v4 : v1.nbhood.values()) {
                                if ((v1.id != v4.id) && (v2.id != v4.id) && (v3.id != v4.id)) {
                                    if(!((v3.nbhood.containsKey(v2.id)) || (v4.nbhood.containsKey(v2.id)) || (v4.nbhood.containsKey(v3.id)))){
                                        //-------------------- SEÇÃO DE MENSAGENS -----------------------------
                                        System.out.println("\n\n----------- Possui Garra -----------");
                                        System.out.println("             Vertice: " + v1.getId());
                                        System.out.println("             Vizinho: " + v2.getId());
                                        System.out.println("             Vizinho: " + v3.getId());
                                        System.out.println("             Vizinho: " + v4.getId());
                                        return 0;
                                    }
                                }
                            }
                        }
                    }
                }
            }
        }

        System.out.println("\n\n----------- É um Grafo Sem Garra -----------");
        return 1;//é sem garra
    }

/*
	public void compact() {
        // fazer
        int cont = 1;
        for( Vertex v : vertex_set.values()){
            if(cont != v.id){
                System.out.println("Vetor "+ v.id + " alterado para "+ cont);
                System.out.println("vizinho "+ v.nbhood.values() + " alterado para "+ cont);
                v.id = cont;
            }
            cont++;
        }
    }
*/

    // relabel the vertices from 1 to n
    public void compact() {
		int n = vertex_set.size();
		// ids utilizados de 1 a n
		int [ ] small = new int[n+1];
		Vertex [ ] big = new Vertex[n];



		int qbig = 0;
        for( Vertex v1 : vertex_set.values() ) {
			if( v1.id <= n )
				small[ v1.id ] = 1;
			else
				big[ qbig++ ] = v1;
		}



		int i = 1;
		for( int pairs = 0; pairs < qbig; i++ ) {
			if( small[ i ] == 0 )
				small[ pairs++ ] = i;
		}



		for( i = 0; i < qbig; i++) {
			int old_id = big[ i ].id;
			big[ i ].id = small[ i ];

			vertex_set.remove( old_id );
            vertex_set.put( big[ i ].id, big[ i ] );

			for( Vertex v1 : vertex_set.values() ) {
				if( v1.nbhood.get( old_id ) != null ) {
					v1.nbhood.remove( old_id );
					v1.nbhood.put( big[ i ].id, big[ i ] );
				}
			}
		}
    }

    public boolean is_undirected() {

        for( Vertex v1 : this.vertex_set.values()) {
            for( Vertex v2 : v1.nbhood.values()) {
                if (v2.nbhood.get( v1.id ) == null)
                    return false;
            }
        }
        return true;
    }

    public Digraph subjacent() {

        Digraph g2 = new Digraph();

        for( Vertex v11 : this.vertex_set.values()) {
            g2.add_vertex( v11.id );
        }

        for( Vertex v11 : this.vertex_set.values()) {
            for( Vertex v12 : v11.nbhood.values()) {
				g2.add_edge( v11.id, v12.id );

/*				g2.add_arc( v11.id, v12.id );
				g2.add_arc( v12.id, v11.id );*/

/*                Vertex v21 = g2.vertex_set.get( v11.id );
                Vertex v22 = g2.vertex_set.get( v12.id );
                v21.add_neighbor( v22 );
                v22.add_neighbor( v21 );*/
            }
        }
        return g2;
    }

    public boolean is_connected(int id1, int id2) {
        // encontre o subjacente
        // aplique no subjacente
        Vertex v1 = vertex_set.get(id1);

        if(v1.nbhood.containsKey(id2))
            return true;

        return false;

        // generalizar para encontrar as
        // componentes conexas do grafo subjacente
    }



}

