package umg.principal.formularios.productos;

import javax.swing.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;

public class frmGenerarReporte {

    private JPanel JfrmGenerarReporte;
    private JComboBox comboBoxCondiciones;
    private JButton btnReporteCondicionado;
    private JButton btnReporteGeneral;


    public static void main(String[] args) {
        JFrame frame = new JFrame("frmGenerarReporte");
        frame.setContentPane(new frmGenerarReporte().JfrmGenerarReporte);
        frame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        frame.pack();
        frame.setResizable(false);
        frame.setSize(550, 400);
        frame.setLocationRelativeTo(null);
        frame.setVisible(true);
    }


    public frmGenerarReporte() {

        comboBoxCondiciones.addItem("Existencia menores a 20 unidades");
        comboBoxCondiciones.addItem("Origen de un país especifico");
        comboBoxCondiciones.addItem("Precios mayores a 2000");
        comboBoxCondiciones.addItem("Agrupado por país y ordenado por precio, de mayor a menor");

        btnReporteGeneral.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {

            }
        });


        btnReporteCondicionado.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {

            }
        });
    }
}
