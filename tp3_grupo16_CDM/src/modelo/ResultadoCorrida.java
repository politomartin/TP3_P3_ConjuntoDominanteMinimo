package modelo;

import java.util.Set;

public class ResultadoCorrida {
    private final String nombre;
    private final Set<Integer> cdm;
    private final double tiempoMs;

    public ResultadoCorrida(String nombre, Set<Integer> cdm, double tiempoMs) {
        this.nombre = nombre;
        this.cdm = cdm;
        this.tiempoMs = tiempoMs;
    }

    public String getNombre() { return nombre; }
    public Set<Integer> getCdm() { return cdm; }
    public double getTiempoMs() { return tiempoMs; }
}