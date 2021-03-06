package com.dp1wms.tabu;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.Collections;
import java.util.Random;

public class Tabu {

    private int[][] distancias;
    private int[] caminoInicial;
    private long duracion;
    private long iteracion;
    private long iteracionSinMejora;
    private long[] distanciaPorIter;

    public Tabu(){
        //prueba
        this.distancias = //Distance matrix, 5x5, used to represent distances
                new int[][]{{0, 1, 3, 4, 5},
                        {1, 0, 1, 4, 8},
                        {3, 1, 0, 5, 1},
                        {4, 4, 5, 0, 2},
                        {5, 8, 1, 2, 0}};


        this.caminoInicial = new int[]{0, 1, 2, 3, 4, 0};
        this.duracion = 0;
    }


    public Tabu(int[][] distancias, int[] caminoInicial){
        this.distancias = distancias;
        this.caminoInicial = caminoInicial;
        this.duracion = 0;
        this.iteracion = 0;
        this.iteracionSinMejora = 0;
    }

    /**
     * Funcion objetivo: suma de distancias
     */
    public long funcionObjetivo(int[] solucion){
        long valor = 0;

        for(int i = 0 ; i < solucion.length-1; i++){
            valor += this.distancias[solucion[i]][solucion[i+1]];
        }
        return valor;
    }

    public int[] generarCamino(long numIteraciones, long numIteracionesSinMejora,
                               int listaTabuTamahno, int listaTabuPermanencia,
                               long tiempoMaximo){
        int[] solucionActual =  new int[this.caminoInicial.length];  //inicializar solucion
        this.distanciaPorIter = new long[(int)numIteraciones]; //
        System.arraycopy(this.caminoInicial, 0, solucionActual, 0, this.caminoInicial.length);

        ListaTabu listaTabu = new ListaTabu(listaTabuTamahno, listaTabuPermanencia);

        int[] mejorSol = new int[solucionActual.length]; //la mejor solucion hasta el momento
        System.arraycopy(solucionActual, 0, mejorSol, 0, mejorSol.length);
        long mejorCosto = this.funcionObjetivo(mejorSol);

        this.iteracion = 0;
        this.iteracionSinMejora = 0;

        long time_start = System.currentTimeMillis();
        while(condicionDeParada(numIteraciones, numIteracionesSinMejora, tiempoMaximo)){// iterar segun parametro - condicion de parada
            solucionActual = this.obtenerMejorVecino(listaTabu, this.distancias, solucionActual);

            long costoActual = this.funcionObjetivo(solucionActual);

            if (costoActual < mejorCosto) {
                System.arraycopy(solucionActual, 0, mejorSol, 0, mejorSol.length);
                mejorCosto = costoActual;
                this.iteracionSinMejora = 0;
            } else {
                //System.out.printf("Costos (actual - mejor): %d - %d\n", costoActual, mejorCosto);
                this.iteracionSinMejora += 1;
            }
            this.distanciaPorIter[(int)iteracion] = mejorCosto;
            iteracion++;
            this.duracion = ( System.currentTimeMillis() - time_start );
        }

        return mejorSol;
    }

    private boolean condicionDeParada(long iteracionMax,
                                      long iteracionSinMejoraMax,
                                      long tiempoMax){

        return this.iteracion < iteracionMax && this.iteracionSinMejora < iteracionSinMejoraMax && this.duracion < tiempoMax;
    }

    private int[] obtenerMejorVecino(ListaTabu listaTabu,
                                     int[][] distancias,
                                     int[] solInicial) {

        int[] mejorSol = new int[solInicial.length]; //la mejor solucion local
        System.arraycopy(solInicial, 0, mejorSol, 0, mejorSol.length);
        long mejorCosto = this.funcionObjetivo(solInicial);
        int nodo1 = 0;
        int nodo2 = 0;
        boolean primerVecino = true;

        for (int i = 1; i < mejorSol.length - 1; i++) {
            for (int j = i+1; j < mejorSol.length - 1; j++) {
                if (i == j) {
                    continue;
                }

                int[] mejorSolActual = new int[mejorSol.length]; //mejor solucion actual
                System.arraycopy(mejorSol, 0, mejorSolActual, 0, mejorSolActual.length);

                mejorSolActual = twoOPT_swap(i,j,solInicial);
                // calcular el nuevo mejor costo
                long mejorCostoActual = this.funcionObjetivo(mejorSolActual);

                //si se encontro un mejor movimiento, guardar
                if ((mejorCostoActual < mejorCosto || primerVecino) && !listaTabu.contieneMovimiento(i,j)) {
                    primerVecino = false;
                    nodo1 = i;
                    nodo2 = j;
                    System.arraycopy(mejorSolActual, 0, mejorSol, 0, mejorSolActual.length);
                    mejorCosto = mejorCostoActual;
                }
            }
        }

        if (nodo1 != 0) {
            listaTabu.decrementarListaTabu();
            listaTabu.agregarNodo(nodo1, nodo2);
        }

        return mejorSol;
    }


    // intercambiar dos nodos
    private int[] intercambiarNodos(int nodo1, int nodo2, int[] solucion) {
        //int[] sol_aux = new int[solucion.length];
        //System.arraycopy(solucion,0,sol_aux,0,solucion.length);
        int temp = solucion[nodo1];
        solucion[nodo1] = solucion[nodo2];
        solucion[nodo2] = temp;
        return solucion;
    }

    private int[] twoOPT_swap(int nodo1, int nodo2, int[] solucion){
        int [] nuevaSol = new int [solucion.length];
        //1. take route[0] to route[i-1] and add them in order to new_route
        for (int i = 0; i < nodo1 ; i++) {
            nuevaSol[i] = solucion[i];
        }
        //2. take route[i] to route[k] and add them in reverse order to new_route
        for (int i = 0; i <= nodo2 - nodo1; i++) {
            nuevaSol[nodo1 + i] = solucion[nodo2 - i];
        }
        //3. take route[k+1] to end and add them in order to new_route
        for(int i = nodo2 +1; i < solucion.length; i++){
            nuevaSol[i] = solucion[i];
        }
        return nuevaSol;
    }

    public long getDuracion(){
        return this.duracion;
    }

    public long getIteracion(){
        return this.iteracion;
    }

    public long[] getDistanciaPorIter(){
        return this.distanciaPorIter;
    }

    /**
     * Unir dos o más arrays
     */
    private int[] joinArray(int[]... arrays) {
        int length = 0;
        for (int[] array : arrays) {
            length += array.length;
        }

        final int[] result = new int[length];

        int offset = 0;
        for (int[] array : arrays) {
            System.arraycopy(array, 0, result, offset, array.length);
            offset += array.length;
        }

        return result;
    }

    /**
     * Reverse an array
     */
    private void reverse(int[] array){
        for(int i = 0; i < array.length / 2; i++)
        {
            int temp = array[i];
            array[i] = array[array.length - i - 1];
            array[array.length - i - 1] = temp;
        }
    }

    private int[] subArray(int[] array, int from, int to){
        return Arrays.copyOfRange(array, from, to);
    }



}
