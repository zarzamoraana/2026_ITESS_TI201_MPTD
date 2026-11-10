//EJECUTAR: javac MetodoModi.java
//COMPILAR: java MetodoModi.java

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.util.StringTokenizer;

public class MetodoModi {
    FastReader reader = new FastReader();

    static class FastReader {
        BufferedReader b;
        StringTokenizer s; 

        public FastReader() {
            b = new BufferedReader(new InputStreamReader(System.in));
        }

        String next() {
            while (s == null || !s.hasMoreElements()) {
                try {
                    s = new StringTokenizer(b.readLine());
                } catch (IOException e) {
                    e.printStackTrace(); 
                }
            }
            return s.nextToken();
        }

        int nextInt() { 
            return Integer.parseInt(next()); 
            
        }
    }

    int m, n; //m:filas (numero de origenes), n:columnas 
    int[][] costos, asignacion;
    int[] oferta, demanda;

    public void leerDatos() {
        System.out.println(" METODO DE MODI :3 ");

        System.out.print("Ingresa el numero de origenes (filas): ");
        m = reader.nextInt();

        System.out.print("Ingresa el numero de destinos (columnas): ");
        n = reader.nextInt();
        
        costos = new int[m][n];
        asignacion = new int[m][n];
        
        //Matriz de costos
        System.out.println("Ingresa la matriz de costos (" + m + " x " + n + "):");
        for (int i = 0; i < m; i++)
            for (int j = 0; j < n; j++)
                costos[i][j] = reader.nextInt();

        System.out.println("Ingresa las ofertas para cada origen (" + m + " valores):");
        oferta = new int[m];
        for (int i = 0; i < m; i++) oferta[i] = reader.nextInt();

        System.out.println("Ingrese las demandas para cada destino (" + n + " valores):");
        demanda = new int[n];
        for (int j = 0; j < n; j++) demanda[j] = reader.nextInt();
    }

    public void resuelve() {
        boolean[] filaEliminada = new boolean[m];
        boolean[] colEliminada = new boolean[n];

        while (true) {
            //verificar si la oferta y demanda se agotaron
            boolean fin = true;
            for (int o : oferta) if (o > 0) fin = false;
            for (int d : demanda) if (d > 0) fin = false;
            if (fin) break;

            int maxPenalizacion = -1;
            boolean esFila = true;
            int idxSel = -1;

            //calcular penalizaciones por fila
            for (int i = 0; i < m; i++) {
                if (filaEliminada[i]) continue;
                int pen = penalizacionFila(i, colEliminada);
                if (pen > maxPenalizacion) {
                    maxPenalizacion = pen;
                    esFila = true;
                    idxSel = i;
                }
            }

            // calcular penalizaciones por columna
            for (int j = 0; j < n; j++) {
                if (colEliminada[j]) continue;
                int pen = penalizacionColumna(j, filaEliminada);
                if (pen > maxPenalizacion) {
                    maxPenalizacion = pen;
                    esFila = false;
                    idxSel = j;
                }
            }

            // buscar celda con menor costo en la fila o columna seleccionada
            int mejorF = -1, mejorC = -1, minCosto = Integer.MAX_VALUE;

            if (esFila) {
                int i = idxSel;
                for (int j = 0; j < n; j++) {
                    if (!colEliminada[j] && costos[i][j] < minCosto) {
                        minCosto = costos[i][j];
                        mejorF = i;
                        mejorC = j;
                    }
                }
            } else {
                int j = idxSel;
                for (int i = 0; i < m; i++) {
                    if (!filaEliminada[i] && costos[i][j] < minCosto) {
                        minCosto = costos[i][j];
                        mejorF = i;
                        mejorC = j;
                    }
                }
            }

            // asignar y actualizar
            int cant = Math.min(oferta[mejorF], demanda[mejorC]);
            asignacion[mejorF][mejorC] += cant;
            oferta[mejorF] -= cant;
            demanda[mejorC] -= cant;

            //marcar eliminadas si llegan a 0
            if (oferta[mejorF] == 0) filaEliminada[mejorF] = true;
            if (demanda[mejorC] == 0) colEliminada[mejorC] = true;
        }

        //imprimir matriz y costo total
        int costoTotal = 0;
        for (int i = 0; i < m; i++) {
            for (int j = 0; j < n; j++) {
                System.out.print(asignacion[i][j] + "\t");
                costoTotal += asignacion[i][j] * costos[i][j];
            }
            System.out.println();
        }
        System.out.println("Costo Total: " + costoTotal);

        System.out.println("Costo Total: " + costoTotal);
        metodoMODI(); //Manda a llamar el metodo

    }

    private int penalizacionFila(int i, boolean[] colEliminada) {
        int m1 = Integer.MAX_VALUE, m2 = Integer.MAX_VALUE, count = 0;
        for (int j = 0; j < n; j++) {
            if (!colEliminada[j]) {
                count++;
                int c = costos[i][j];
                if (c < m1) { m2 = m1; m1 = c; }
                else if (c < m2) { m2 = c; }
            }
        }
        return count <= 1 ? m1 : m2 - m1;
    }

    private int penalizacionColumna(int j, boolean[] filaEliminada) {
        int m1 = Integer.MAX_VALUE, m2 = Integer.MAX_VALUE, count = 0;
        for (int i = 0; i < m; i++) {
            if (!filaEliminada[i]) {
                count++;
                int c = costos[i][j];
                if (c < m1) { m2 = m1; m1 = c; }
                else if (c < m2) { m2 = c; }
            }
        }
        return count <= 1 ? m1 : m2 - m1;
    }

    //DESDE AQUI EMPIEZA LO DEL METODO DE MODI :3

    //Metodo que evalua la optimidad (utilizando MODI)
    public void metodoMODI() {
        Integer[] u = new Integer[m];
        Integer[] v = new Integer[n];
        u[0] = 0; 

        boolean cambio = true;
        while (cambio) {
            cambio = false;
            for (int i = 0; i < m; i++) {
                for (int j = 0; j < n; j++) {
                    if (asignacion[i][j] > 0) { // Celda básica
                        if (u[i] != null && v[j] == null) {
                            v[j] = costos[i][j] - u[i];
                            cambio = true;
                        } else if (u[i] == null && v[j] != null) {
                            u[i] = costos[i][j] - v[j];
                            cambio = true;
                        }
                    }
                }
            }
        }

        System.out.println("\n--- MULTIPLICADORES MODI (u y v) ---");
        for (int i = 0; i < m; i++) System.out.println("u[" + i + "] = " + u[i]);
        for (int j = 0; j < n; j++) System.out.println("v[" + j + "] = " + v[j]);


       
        // Evaluación de celdas no básicas 
        boolean optimo = true;
        System.out.println("EVALUACION DE CELDAS NO BASICAS (DELTAS)");
        for (int i = 0; i < m; i++) {
            for (int j = 0; j < n; j++) {
                if (asignacion[i][j] == 0) {
                    int delta = costos[i][j] - (u[i] + v[j]);
                    System.out.println("Celda (" + i + "," + j + ") -> Delta = " + delta);
                    if (delta < 0) {
                        optimo = false;
                    }
                }
            }
        }

        System.out.println("\n--------------------------------------------------");
        if (optimo) {
            System.out.println("La solucion de Vogel es OPTIMA (Todos las deltas >= 0)");
        } else {
            System.out.println("La solucion NO es optima (Hay deltas negativos)");
        }
        System.out.println("--------------------------------------------------");
    }


    public static void main(String[] args) {
        MetodoModi id = new MetodoModi();
        id.leerDatos();
        id.resuelve();
    } 
}
