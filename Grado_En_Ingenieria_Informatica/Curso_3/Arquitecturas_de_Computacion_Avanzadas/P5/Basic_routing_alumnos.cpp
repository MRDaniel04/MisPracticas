/*
 *    		Hiperion simulator
 *
 *    Copyright (C) 2020 Francisco J. Andújar and Javier Cano-Cano.
 *    This file is part of the Hiperion simulator.
 *
 *  Hiperion simulator is free software: you can redistribute it and/or modify
 *  it under the terms of the GNU General Public License as published by
 *  the Free Software Foundation, either version 3 of the License, or
 *  (at your option) any later version.
 *
 *  Hiperion simulator is distributed in the hope that it will be useful,
 *  but WITHOUT ANY WARRANTY; without even the implied warranty of
 *  MERCHANTABILITY or FITNESS FOR A PARTICULAR PURPOSE.  See the
 *  GNU General Public License for more details.
 *
 *  You should have received a copy of the GNU General Public License
 *  along with Hiperion simulator.  If not, see <http://www.gnu.org/licenses/>.
 */


#include "configuration.h"
#include "globals.h"
#include "routing.h"
#include <math.h>
#include "routers.h"
#include "messages.h"
#include "cells.h"
#include "topology.h"
#include "utils.h"
#include "BitMask.h"
#include "buffers.h"

//Funciona solo en toros y fat-trees

int inline maxCreditsPerVC() {
    return BASIC_INFO.qSizexPort[0] / BASIC_INFO.packet_size;
}
//Funciona en todas las topologias.

int inline maxCreditsPerVC(PORT p) {
    return BASIC_INFO.qSizexPort[p] / BASIC_INFO.packet_size;
}

CELL_ID inline getHeadFlit(NODE_ID sw, PORT inport, TC itc, VC ivc) {
    CELL_ID cId;
    fnGetFirstCellID(ROUTER[sw].input_buffers + inport, vqsID[inport][itc][ivc], &cId);
    return cId;
}

MSG_ID inline getHeadMessage(NODE_ID sw, PORT inport, TC itc, VC ivc) {
    CELL_ID cId;
    if (fnGetFirstCellID(ROUTER[sw].input_buffers + inport, vqsID[inport][itc][ivc], &cId))
        return CELL[cId].mId;
    return 0;
}

VC inline getNumGlobalAdapVCs() {
    return CONFIGURATION.neighbourhood_ports * BASIC_INFO.num_adaptive_vcs;
}

VC inline getPortfromGlobalAdapVC(VC global_vc) {
    return global_vc / BASIC_INFO.num_adaptive_vcs;
}

PORT inline getVCfromGlobalAdapVC(VC global_vc) {
    return (global_vc % BASIC_INFO.num_adaptive_vcs) +BASIC_INFO.first_adap_vc;
}


int acum = 0;

/*PRACTICA: el propósito de la función es examinar las opciones adaptativas y una vez que encuentre una, selecciona un puerto de salida y un canal virtual.
Implementamos la política que permita obtener el canal adaptativo que este más vacío (+ créditos = + vacío)*/
int fnSelectionPolicyBASIC_alumnos(NODE_ID sw, PORT inport, TC itc, VC ivc, BitMask64 adaptive_mask, PORT* outport, VC* adapt_vc) {
    VC total_global_adaptive_vcs = getNumGlobalAdapVCs(); //PRACTICA: Obtiene el número de canales adaptativos globales (puertos de salida * número de canales adaptativos por puerto de salida)
    if (total_global_adaptive_vcs == 0) return FALSE; //PRACTICA: No hay canales adaptativos disponibles = retorna FALSE

    //PRACTICA: Inicializamos las variables que nos ayudarán a encontrar el canal adaptativo
    PORT best_port = maxPorts;         //PRACTICA: almacena el puerto de salida
    VC   best_vc = BASIC_INFO.first_adap_vc + BASIC_INFO.num_adaptive_vcs;  //PRACTICA: almacena el canal virtual
    int  max_found_credits = -1;       //PRACTICA: almacena el número de créditos encontrados (se inicia en -1 para que siempre entre al bucle(mejor elegir un canal lleno que uno no encontrado))
    VC   best_global_vc_idx_for_rr_update = ROUTER[sw].arbiters.BASIC[inport].last_adaptive_vc_selected_rr; //PRACTICA: almacena el índice del canal adaptativo para actualizar el puntero

    //PRACTICA: Iteramos sobre cada canal adaptativo global
    for (VC i = 0; i < total_global_adaptive_vcs; i++) {
        VC current_global_vc_idx = (ROUTER[sw].arbiters.BASIC[inport].last_adaptive_vc_selected_rr + i + 1) % total_global_adaptive_vcs; //PRACTCIA: calculod del canal actual
        
        PORT current_port = getPortfromGlobalAdapVC(current_global_vc_idx); //PRACTICA: convertimos el índice del canal adaptativo global a un puerto de salida
        VC current_vc = getVCfromGlobalAdapVC(current_global_vc_idx); //PRACTICA: convertimos el índice del canal adaptativo global a un canal virtual
        
        //PRACTICA: comprobamos si el puerto está permitido
        if (get_bit64(adaptive_mask, current_port)) {
            int credits = GET_CREDITS(sw, current_port, itc, current_vc); //PRACTICA: obtenemos créditos (número de flits) del canal adaptativo

            if (credits > max_found_credits) {  //PRACTICA: si el número de créditos es mayor que el máximo encontrado, el canal encontrado es mejor que el anterior
                max_found_credits = credits;
                best_port = current_port;
                best_vc = current_vc;
                best_global_vc_idx_for_rr_update = current_global_vc_idx;
            }
        }
    }

    //PRACTICA: Si se ha encontrado un canal adaptativo, se asigna el puerto de salida y el canal virtual
    if (max_found_credits > 0) { 
        *outport = best_port;
        *adapt_vc = best_vc;
        ROUTER[sw].arbiters.BASIC[inport].last_adaptive_vc_selected_rr = best_global_vc_idx_for_rr_update;
        return TRUE;
    }
    
    return FALSE;
}

/*PRACTICA: Esta función encamina un paquete al conmutador destino*/
void fnRouterBASIC_dor_alumnos(NODE_ID sw, PORT iport, TC itc, VC ivc, CELL_ID cId, PORT *output_port, VC *requested_vc, BitMask64* output_mask) {
    // Let's obtain the destination of the cell
    MSG_ID mId = CELL[cId].mId;
    NODE_ID dstNode = MESSAGE[mId].dstNode;
    NODE_ID dst_router = fnTop_nic_to_switch(dstNode);
    PORT xy_port = maxPorts;
    VC vc_low = 0;
    VC vc_high = 1;


    if (dst_router!=sw){
        BitMask64 adaptive_mask = 0;
        for (int dim = 0; dim < CONFIGURATION.numDimensions; dim++) {
            int dim_dest = ROUTER[dst_router].m_id->getPos(dim);
            int dim_current = ROUTER[sw].m_id->getPos(dim);

            if (dim_dest != dim_current) {
                // PRACTICA: Seleccionamos el VC determinista (LOW/HIGH)
                if (dim_dest > dim_current) {
                    *requested_vc = vc_high;
                } else {
                    *requested_vc = vc_low;
                }

                // PRACTICA: Determinamos el puerto físico de salida que equivaldría al camino más corto en el toro
                torus_port_direction physical_path_dir;
                int k_nodes_in_dim = CONFIGURATION.sizeDimensions[dim];
                int diff = dim_dest - dim_current; //PRACTICA: Vemos la diferencia entre el destino y el origen en la dimensión actual
                int forward_dist, backward_dist;

                //PRACTICA: Con la diferencia, calcumlamos la distancia hasta el destino en ambas direcciones (delante y detrás)
                if (diff > 0) { 
                    forward_dist = diff;
                    backward_dist = k_nodes_in_dim - diff; // Distancia dando la vuelta por detrás (ej: 2 -> 5 en un toro de 8 es 3 por delante, 8-3=5 por detrás)
                } else { // diff < 0. Destino está "detrás" en coordenadas (ej: 5 -> 2)
                    forward_dist = k_nodes_in_dim + diff; // Distancia dando la vuelta por delante (ej: 5 -> 2 en toro de 8, diff=-3, 8-3=5 por delante)
                    backward_dist = -diff; // Distancia directa hacia atrás
                }

                //PRACTICA: Determinamos la dirección física dependiendo de las distancias calculadas
                if (forward_dist < backward_dist) {
                    physical_path_dir = positive_direction;
                } else if (backward_dist < forward_dist) {
                    physical_path_dir = negative_direction;
                } else {
                    physical_path_dir = tie_direction; // Empate en distancia física
                }

                //PRACTICA: establecemos el valor de los puertos de salida posibles para la dimensión actual
                PORT positive_dim_port = (PORT)(2 * dim + 1);
                PORT negative_dim_port = (PORT)(2 * dim);

                //PRACTICA: Determinamos el puerto de salida para que en caso de que sea determinista, que no se entre al if(CONFIGURATION.adaptive_packets_allowed)
                if (physical_path_dir == positive_direction) {
                    xy_port = positive_dim_port;
                } else if (physical_path_dir == negative_direction) {
                    xy_port = negative_dim_port;
                } else { // tie_direction (para el puerto físico determinista)
                    xy_port = positive_dim_port; // Desempate determinista: preferir positivo
                }

                *output_port = xy_port;

                //PRACTICA: establecemos el puerto de salida y la máscara, dependiendo de la dirección física del camino
                if(CONFIGURATION.adaptive_packets_allowed){
                    if (physical_path_dir == positive_direction) {
                        set_bit64(&adaptive_mask, positive_dim_port);
                    } else if (physical_path_dir == negative_direction) {
                        set_bit64(&adaptive_mask, negative_dim_port);
                    } else { // tie_direction (para el puerto físico)
                        set_bit64(&adaptive_mask, positive_dim_port);
                        set_bit64(&adaptive_mask, negative_dim_port);
                    }
                    *output_mask = adaptive_mask;
                }else{
                    *output_mask=0;
                }

                return;
                
            }
        }
    } else {
        //The packets has arrived at its destination
        // let's get the nic associated to the noder
        *output_port = fnTop_nic_to_port(dstNode);
        *requested_vc = ivc;
        *output_mask = 0;
    }
    
}
