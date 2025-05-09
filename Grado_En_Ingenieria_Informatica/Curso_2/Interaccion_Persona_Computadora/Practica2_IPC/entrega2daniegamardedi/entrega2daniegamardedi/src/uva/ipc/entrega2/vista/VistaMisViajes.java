/*
 * Click nbfs://nbhost/FileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/GUIForms/JFrame.java to edit this template
 */
package uva.ipc.entrega2.vista;

import uva.ipc.entrega2.modelo.Billete;
import java.awt.Color;
import java.time.LocalDate;
import java.time.LocalTime;
import java.util.List;
import javax.swing.DefaultListModel;

/**
 * @author mardedi
 * @author daniega
 */

public class VistaMisViajes extends javax.swing.JFrame {
    private ControladorMisViajes miControlador;
    private DefaultListModel listModelAntiguos = new DefaultListModel();
    private DefaultListModel listModelFuturos = new DefaultListModel();
    /**
     * Creates new form MisViajesVista
     */
    public VistaMisViajes() {
        initComponents();
        this.miControlador = new ControladorMisViajes(this);
        cargarBilletes(); // Aquí se carga visualmente la lista
    }

    /**
    * Carga los billetes en las listas de billetes antiguos y futuros en la vista.
    * Los billetes antiguos son aquellos cuya fecha es anterior a la fecha actual o cuya fecha es igual a la actual y la hora es anterior.
    * Los billetes futuros son aquellos cuya fecha es posterior a la fecha actual o cuya fecha es igual a la actual y la hora es posterior.
    */

    public void cargarBilletes() {
        LocalDate fechaActual = LocalDate.now();
        LocalTime horaActual = LocalTime.now();
        listModelAntiguos.clear();
        listModelFuturos.clear();
        List<Billete> billetes = miControlador.obtenerBilletes();
        for (Billete billete : billetes) {
            int comparacionFecha = billete.getFecha().compareTo(fechaActual);
            if (comparacionFecha < 0) {
                listModelAntiguos.addElement(billete.toString());
            } else if (comparacionFecha > 0) {
                listModelFuturos.addElement(billete.toString());
            } else {
                int comparacionHora = billete.getHora().compareTo(horaActual);
                if (comparacionHora <= 0) {
                    listModelAntiguos.addElement(billete.toString());
                } else if (comparacionHora > 0) {
                    listModelFuturos.addElement(billete.toString());
                } 
            }
        }
        billetesAntiguosList.setModel(listModelAntiguos);
        billetesNuevosList.setModel(listModelFuturos);
    }

    /**
     * This method is called from within the constructor to initialize the form.
     * WARNING: Do NOT modify this code. The content of this method is always
     * regenerated by the Form Editor.
     */
    @SuppressWarnings("unchecked")
    // <editor-fold defaultstate="collapsed" desc="Generated Code">//GEN-BEGIN:initComponents
    private void initComponents() {

        buttonGroup1 = new javax.swing.ButtonGroup();
        jPanel1 = new javax.swing.JPanel();
        jPanel2 = new javax.swing.JPanel();
        jPanel4 = new javax.swing.JPanel();
        jPanel6 = new javax.swing.JPanel();
        jPanel5 = new javax.swing.JPanel();
        jScrollPane3 = new javax.swing.JScrollPane();
        billetesAntiguosList = new javax.swing.JList<>();
        jPanel7 = new javax.swing.JPanel();
        jLabel4 = new javax.swing.JLabel();
        jPanel9 = new javax.swing.JPanel();
        jPanel8 = new javax.swing.JPanel();
        jScrollPane4 = new javax.swing.JScrollPane();
        billetesNuevosList = new javax.swing.JList<>();
        jPanel11 = new javax.swing.JPanel();
        jLabel5 = new javax.swing.JLabel();
        jPanel18 = new javax.swing.JPanel();
        jPanel19 = new javax.swing.JPanel();
        jPanel12 = new javax.swing.JPanel();
        DevolverRadioButton = new javax.swing.JRadioButton();
        jPanel13 = new javax.swing.JPanel();
        EditarRadioButton = new javax.swing.JRadioButton();
        jPanel14 = new javax.swing.JPanel();
        jPanel15 = new javax.swing.JPanel();
        jPanel16 = new javax.swing.JPanel();
        confirmarButton = new javax.swing.JButton();
        jPanel17 = new javax.swing.JPanel();
        inicioButton = new javax.swing.JButton();
        jPanel10 = new javax.swing.JPanel();
        MensajeErrorLabel = new javax.swing.JLabel();
        jPanel3 = new javax.swing.JPanel();
        jLabel1 = new javax.swing.JLabel();

        setDefaultCloseOperation(javax.swing.WindowConstants.EXIT_ON_CLOSE);
        setMinimumSize(new java.awt.Dimension(650, 550));

        jPanel1.setLayout(new java.awt.BorderLayout());

        jPanel2.setBorder(javax.swing.BorderFactory.createEmptyBorder(10, 10, 10, 10));
        jPanel2.setLayout(new java.awt.BorderLayout());

        jPanel4.setLayout(new java.awt.GridLayout(3, 0));

        jPanel6.setBorder(javax.swing.BorderFactory.createEmptyBorder(1, 1, 10, 1));
        jPanel6.setLayout(new java.awt.BorderLayout());

        jPanel5.setLayout(new java.awt.BorderLayout());

        billetesAntiguosList.addListSelectionListener(new javax.swing.event.ListSelectionListener() {
            public void valueChanged(javax.swing.event.ListSelectionEvent evt) {
                billetesAntiguosListValueChanged(evt);
            }
        });
        jScrollPane3.setViewportView(billetesAntiguosList);

        jPanel5.add(jScrollPane3, java.awt.BorderLayout.CENTER);

        jPanel6.add(jPanel5, java.awt.BorderLayout.CENTER);

        jPanel7.setLayout(new java.awt.BorderLayout());

        jLabel4.setText("BILLETES PASADOS");
        jPanel7.add(jLabel4, java.awt.BorderLayout.CENTER);

        jPanel6.add(jPanel7, java.awt.BorderLayout.PAGE_START);

        jPanel4.add(jPanel6);

        jPanel9.setBorder(javax.swing.BorderFactory.createEmptyBorder(10, 1, 1, 1));
        jPanel9.setLayout(new java.awt.BorderLayout());

        jPanel8.setLayout(new java.awt.BorderLayout());

        jScrollPane4.setViewportView(billetesNuevosList);

        jPanel8.add(jScrollPane4, java.awt.BorderLayout.CENTER);

        jPanel9.add(jPanel8, java.awt.BorderLayout.CENTER);

        jPanel11.setLayout(new java.awt.BorderLayout());

        jLabel5.setText("BILLETES FUTUROS");
        jPanel11.add(jLabel5, java.awt.BorderLayout.CENTER);

        jPanel9.add(jPanel11, java.awt.BorderLayout.PAGE_START);

        jPanel4.add(jPanel9);

        jPanel18.setBorder(javax.swing.BorderFactory.createEmptyBorder(10, 1, 1, 1));
        jPanel18.setLayout(new java.awt.BorderLayout());

        jPanel19.setLayout(new java.awt.GridLayout(1, 3));

        jPanel12.setLayout(new java.awt.BorderLayout());

        buttonGroup1.add(DevolverRadioButton);
        DevolverRadioButton.setText("DEVOLVER");
        jPanel12.add(DevolverRadioButton, java.awt.BorderLayout.CENTER);

        jPanel19.add(jPanel12);

        jPanel13.setLayout(new java.awt.BorderLayout());

        buttonGroup1.add(EditarRadioButton);
        EditarRadioButton.setText("EDITAR");
        jPanel13.add(EditarRadioButton, java.awt.BorderLayout.CENTER);

        jPanel19.add(jPanel13);

        jPanel14.setBorder(javax.swing.BorderFactory.createEmptyBorder(20, 1, 20, 1));
        jPanel14.setLayout(new java.awt.BorderLayout());

        jPanel15.setLayout(new java.awt.GridLayout(2, 0));

        jPanel16.setBorder(javax.swing.BorderFactory.createEmptyBorder(1, 1, 10, 1));
        jPanel16.setLayout(new java.awt.BorderLayout());

        confirmarButton.setText("CONFIRMAR");
        confirmarButton.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                confirmarButtonActionPerformed(evt);
            }
        });
        jPanel16.add(confirmarButton, java.awt.BorderLayout.CENTER);

        jPanel15.add(jPanel16);

        jPanel17.setBorder(javax.swing.BorderFactory.createEmptyBorder(10, 1, 1, 1));
        jPanel17.setLayout(new java.awt.BorderLayout());

        inicioButton.setText("INICIO");
        inicioButton.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                inicioButtonActionPerformed(evt);
            }
        });
        jPanel17.add(inicioButton, java.awt.BorderLayout.CENTER);

        jPanel15.add(jPanel17);

        jPanel14.add(jPanel15, java.awt.BorderLayout.CENTER);

        jPanel19.add(jPanel14);

        jPanel18.add(jPanel19, java.awt.BorderLayout.CENTER);

        jPanel10.setLayout(new java.awt.BorderLayout());

        MensajeErrorLabel.setHorizontalAlignment(javax.swing.SwingConstants.CENTER);
        MensajeErrorLabel.setText("-");
        jPanel10.add(MensajeErrorLabel, java.awt.BorderLayout.PAGE_START);

        jPanel18.add(jPanel10, java.awt.BorderLayout.PAGE_START);

        jPanel4.add(jPanel18);

        jPanel2.add(jPanel4, java.awt.BorderLayout.CENTER);

        jPanel1.add(jPanel2, java.awt.BorderLayout.CENTER);

        jPanel3.setLayout(new java.awt.BorderLayout());

        jLabel1.setFont(new java.awt.Font("Segoe UI", 1, 24)); // NOI18N
        jLabel1.setHorizontalAlignment(javax.swing.SwingConstants.CENTER);
        jLabel1.setText("MIS VIAJES");
        jPanel3.add(jLabel1, java.awt.BorderLayout.CENTER);

        jPanel1.add(jPanel3, java.awt.BorderLayout.PAGE_START);

        getContentPane().add(jPanel1, java.awt.BorderLayout.CENTER);

        pack();
    }// </editor-fold>//GEN-END:initComponents

    /**
    * Procesa el evento generado por el botón de inicio.
    */
    
    private void inicioButtonActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_inicioButtonActionPerformed
        miControlador.procesarEventoInicioButton();
    }//GEN-LAST:event_inicioButtonActionPerformed

    /**
    * Obtiene el billete seleccionado en la lista de billetes nuevos
    * Procesa el evento generado por el botón de confirmar.
    */
    
    private void confirmarButtonActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_confirmarButtonActionPerformed
              
        String devolverBillete = (String)billetesNuevosList.getSelectedValue();
        miControlador.procesarEventoConfirmarButton(devolverBillete);
    }//GEN-LAST:event_confirmarButtonActionPerformed

    /**
    * Procesa el evento generado por el cambio de selección en la lista de billetes antiguos.
    */
    
    private void billetesAntiguosListValueChanged(javax.swing.event.ListSelectionEvent evt) {//GEN-FIRST:event_billetesAntiguosListValueChanged
        miControlador.procesaEventoDeselectListas();
    }//GEN-LAST:event_billetesAntiguosListValueChanged

    /**
    * Verifica si hay algún elemento seleccionado en la lista de billetes antiguos.
    * @return true si hay algún elemento seleccionado, de lo contrario, false.
    */
    
    public boolean getList1selected(){
        return !billetesAntiguosList.isSelectionEmpty();
    }
    
    /**
    * Verifica si hay algún elemento seleccionado en la lista de billetes nuevos.
    * @return true si hay algún elemento seleccionado, de lo contrario, false.
    */
    
    public boolean getList2selected(){
        return !billetesNuevosList.isSelectionEmpty();
    }
    
    /**
    * Deselecciona todos los elementos en la lista de billetes antiguos.
    */
    
    public void deselectList1(){
        billetesAntiguosList.clearSelection();
    }
    
    /**
    * Obtiene el estado de selección del botón "Devolver".
    * @return true si el botón "Devolver" está seleccionado, de lo contrario, false.
    */
    
    public boolean getDevolverBillete(){
        return DevolverRadioButton.isSelected();
    }
    
    /**
    * Obtiene el estado de selección del botón "Editar".
    * @return true si el botón "Editar" está seleccionado, de lo contrario, false.
    */
    
    public boolean getEditarBillete(){
        return EditarRadioButton.isSelected();
    }
    
    /**
    * Muestra un mensaje de error con el texto especificado y el color dado.
    * @param mensaje El mensaje de error a mostrar.
    * @param color El color del texto del mensaje de error.
    */
    
    public void cambioMensajeError(String mensaje,Color color){
        MensajeErrorLabel.setVisible(true);
        MensajeErrorLabel.setText(mensaje);
        MensajeErrorLabel.setForeground(color);
    }
    
    /**
    * Obtiene el índice del elemento seleccionado en la lista de billetes nuevos.
    * @return El índice del elemento seleccionado.
    */
    
    public int getSelectedIndex(){
        return billetesNuevosList.getSelectedIndex();
    }
    
    /**
    * Elimina la selección en la lista de billetes nuevos en el índice especificado.
    * @param i El índice del elemento a deseleccionar.
    */
    
    public void removeSelection(int i){
        System.out.println("size"+billetesNuevosList.getModel().getSize());
        System.out.println("index"+i);
        DefaultListModel<String> model = (DefaultListModel<String>) billetesNuevosList.getModel();
        model.remove(i); 
    }
    
    /**
    * Deselecciona todos los botones de radio en el grupo de botones "buttonGroup1".
    */
    
    public void deselectRadioButtons(){
        buttonGroup1.clearSelection();
    }
    
    // Variables declaration - do not modify//GEN-BEGIN:variables
    private javax.swing.JRadioButton DevolverRadioButton;
    private javax.swing.JRadioButton EditarRadioButton;
    private javax.swing.JLabel MensajeErrorLabel;
    private javax.swing.JList<String> billetesAntiguosList;
    private javax.swing.JList<String> billetesNuevosList;
    private javax.swing.ButtonGroup buttonGroup1;
    private javax.swing.JButton confirmarButton;
    private javax.swing.JButton inicioButton;
    private javax.swing.JLabel jLabel1;
    private javax.swing.JLabel jLabel4;
    private javax.swing.JLabel jLabel5;
    private javax.swing.JPanel jPanel1;
    private javax.swing.JPanel jPanel10;
    private javax.swing.JPanel jPanel11;
    private javax.swing.JPanel jPanel12;
    private javax.swing.JPanel jPanel13;
    private javax.swing.JPanel jPanel14;
    private javax.swing.JPanel jPanel15;
    private javax.swing.JPanel jPanel16;
    private javax.swing.JPanel jPanel17;
    private javax.swing.JPanel jPanel18;
    private javax.swing.JPanel jPanel19;
    private javax.swing.JPanel jPanel2;
    private javax.swing.JPanel jPanel3;
    private javax.swing.JPanel jPanel4;
    private javax.swing.JPanel jPanel5;
    private javax.swing.JPanel jPanel6;
    private javax.swing.JPanel jPanel7;
    private javax.swing.JPanel jPanel8;
    private javax.swing.JPanel jPanel9;
    private javax.swing.JScrollPane jScrollPane3;
    private javax.swing.JScrollPane jScrollPane4;
    // End of variables declaration//GEN-END:variables
}
