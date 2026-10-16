package modelo;

import java.io.FileInputStream;
import java.util.Scanner;
import grafo.Grafo;

public class LectorArchivos {

    public static Grafo cargarGrafo(String rutaArchivo) throws Exception {
        FileInputStream fis = new FileInputStream(rutaArchivo);
        Scanner scanner = new Scanner(fis);
        Grafo nuevoGrafo = null;
        int numeroLinea = 0;

        try {
            while (scanner.hasNextLine()) {
                numeroLinea++;
                String linea = scanner.nextLine().trim();

                // Ignorar líneas vacías o comentarios que empiecen con #
                if (linea.isEmpty() || linea.startsWith("#")) {
                    continue;
                }

                String[] partes = linea.split(",");

                // Si tiene 1 solo valor y aún no creamos el grafo, es la cantidad de vértices
                if (partes.length == 1 && nuevoGrafo == null) {
                    int cantidadVertices = Integer.parseInt(partes[0].trim());
                    nuevoGrafo = new Grafo(cantidadVertices);
                } 
                // Si tiene 2 valores, es una arista (origen, destino)
                else if (partes.length == 2) {
                    if (nuevoGrafo == null) {
                        throw new IllegalArgumentException(
                            "El archivo debe indicar la cantidad de vértices en la primera línea válida."
                        );
                    }
                    int origen = Integer.parseInt(partes[0].trim());
                    int destino = Integer.parseInt(partes[1].trim());

                    // Evitamos que tire excepción si en el txt pusieron dos veces la misma arista
                    if (!nuevoGrafo.existeArista(origen, destino) && origen != destino) {
                        nuevoGrafo.agregarArista(origen, destino);
                    }
                } else {
                    throw new IllegalArgumentException("Formato inválido en la línea " + numeroLinea + ": " + linea);
                }
            }
        } finally {
            scanner.close();
        }

        if (nuevoGrafo == null) {
            throw new IllegalArgumentException("El archivo está vacío o no contiene un grafo válido.");
        }

        return nuevoGrafo;
    }
}