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

                if (linea.isEmpty() || linea.startsWith("#")) {
                    continue;
                }

                String[] partes = linea.split(",");

                if (partes.length == 1 && nuevoGrafo == null) {
                    int cantidadVertices = Integer.parseInt(partes[0].trim());
                    nuevoGrafo = new Grafo(cantidadVertices);
                } 

                else if (partes.length == 2) {
                    if (nuevoGrafo == null) {
                        throw new IllegalArgumentException(
                            "El archivo debe indicar la cantidad de vértices en la primera línea válida."
                        );
                    }
                    int origen = Integer.parseInt(partes[0].trim());
                    int destino = Integer.parseInt(partes[1].trim());

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