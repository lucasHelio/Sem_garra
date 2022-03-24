import java.io.FileReader;
import java.io.IOException;
import java.util.Scanner;

public class AlgGrafos
{
    public static void main(String[] args) throws IOException {

        Digraph g1 = new Digraph();


        Scanner scanner = new Scanner(new FileReader("myfiles/grafo.txt")).useDelimiter("\\n");

        String linha;
        String[] valores;

        while (scanner.hasNextLine()){
            linha = scanner.nextLine();
            valores = linha.split(" ");

            for(int i=0; i<valores.length; i++){
                if(!valores[i].equals("=") && i==0){
                    g1.add_vertex(Integer.parseInt(valores[i]));

                }
                else if(!valores[i].equals("=")){

                    g1.add_vertex(Integer.parseInt(valores[i]));
                    g1.add_edge(Integer.parseInt(valores[0]), Integer.parseInt(valores[i]));


                }

            }

        }


        g1.identifica_semGarra();
    }
}

