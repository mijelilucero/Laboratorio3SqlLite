package umg.principal.formularios.productos;

import umg.principal.DaseDatos.Service.ProductoService;
import umg.principal.DaseDatos.model.Producto;
import umg.principal.reportes.PdfReport;

import javax.swing.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.util.List;

public class frmGenerarReporte extends JFrame{

    private JPanel JfrmGenerarReporte;
    private JComboBox comboBoxCondiciones;
    private JButton btnReporteCondicionado;
    private JButton btnReporteGeneral;
    private JComboBox comboBoxPaises;


    public void mostrarFrmGenerarReporte(){
        JFrame frame = new JFrame("Generador de reportes");
        frame.setContentPane(new frmGenerarReporte().JfrmGenerarReporte);
        frame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        frame.pack();
        frame.setResizable(false);
        frame.setSize(670, 400);
        frame.setLocationRelativeTo(null);
        frame.setVisible(true);
    }


    public frmGenerarReporte() {

        comboBoxCondiciones.addItem("Existencia menores a 20 unidades");
        comboBoxCondiciones.addItem("Origen de un país especifico");
        comboBoxCondiciones.addItem("Precios mayores a 2000");
        comboBoxCondiciones.addItem("Agrupado por país y ordenado por precio, de mayor a menor");
        comboBoxCondiciones.setSelectedIndex(-1);

        comboBoxPaises.enable(false);
        comboBoxPaises.addItem("Alemania");
        comboBoxPaises.addItem("China");
        comboBoxPaises.addItem("Corea del Sur");
        comboBoxPaises.addItem("Estados Unidos");
        comboBoxPaises.addItem("Francia");
        comboBoxPaises.addItem("Japón");
        comboBoxPaises.addItem("México");
        comboBoxPaises.addItem("Países Bajos");
        comboBoxPaises.addItem("Reino Unido");
        comboBoxPaises.addItem("Suiza");
        comboBoxPaises.setSelectedIndex(-1);


        btnReporteGeneral.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                try{
                    List<Producto> prod = new ProductoService().obtenerTodosLosProductos();
                    new PdfReport().generateProductReport(prod, "C:\\tmp\\reporte.pdf");

                    JOptionPane.showMessageDialog(null, "Reporte generado en C:\\tmp\\reporte.pdf");
                }
                catch (Exception ex) {
                    System.out.println("Error: " + ex.getMessage());
                }
            }
        });


        btnReporteCondicionado.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                if(comboBoxCondiciones.getSelectedIndex() != -1){
                    if(!comboBoxCondiciones.getSelectedItem().toString().equals("Agrupado por país y ordenado por precio, de mayor a menor")){
                        String condicionSQL=null;
                        String nombrePDF=null;

                        if(comboBoxCondiciones.getSelectedItem().toString().equals("Existencia menores a 20 unidades")){
                            condicionSQL="existencia < 20";
                            nombrePDF="reporteCondicionExistencia";
                        }else if(comboBoxCondiciones.getSelectedItem().toString().equals("Origen de un país especifico")){
                            condicionSQL="origen = '" + comboBoxPaises.getSelectedItem().toString() + "'";
                            nombrePDF="reporteCondicionOrigen";
                        }else if(comboBoxCondiciones.getSelectedItem().toString().equals("Precios mayores a 2000")){
                            condicionSQL="precio > 2000";
                            nombrePDF="reporteCondicionPrecio";
                        }

                        try{
                            List<Producto> prod = new ProductoService().obtenerTodosConCondicional(condicionSQL);
                            new PdfReport().generateProductReport(prod, "C:\\tmp\\"+ nombrePDF + ".pdf");

                            JOptionPane.showMessageDialog(null, "Reporte generado en C:\\tmp\\"+ nombrePDF + ".pdf");
                        }
                        catch (Exception ex) {
                            System.out.println("Error: " + ex.getMessage());
                        }
                    }else{
                        try{
                            List<Producto> prod = new ProductoService().obtenerTodosOrdenadosPorPrecio();
                            new PdfReport().generateProductReport(prod, "C:\\tmp\\reporteCondicionOrdenPrecios.pdf");

                            JOptionPane.showMessageDialog(null, "Reporte generado en C:\\tmp\\reporteCondicionOrdenPrecios.pdf");
                        }
                        catch (Exception ex) {
                            System.out.println("Error: " + ex.getMessage());
                        }
                    }
                }else{
                    JOptionPane.showMessageDialog(null,"Selecciona una condición para generar el reporte.");
                }
            }
        });


        comboBoxCondiciones.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                if(comboBoxCondiciones.getSelectedItem().toString().equals("Origen de un país especifico")) {
                    comboBoxPaises.enable(true);
                }else{
                    comboBoxPaises.enable(false);
                    comboBoxPaises.setSelectedIndex(-1);
                }
            }
        });
    }
}
