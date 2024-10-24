package umg.principal.formularios.productos;

import umg.principal.DaseDatos.Service.ProductoService;
import umg.principal.DaseDatos.model.Producto;

import javax.swing.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;

public class frmCRUDProductos extends JFrame{
    private JPanel JfrmProductos;
    private JButton btnSalir;
    private JTextField txtIdProducto;
    private JTextField txtDescripcion;
    private JComboBox comboBoxOrigen;
    private JTextField txtPrecio;
    private JTextField txtExistencia;
    private JButton btnLimpiar;
    private JButton btnGrabar;
    private JButton btnBuscar;
    private JButton btnGenerarReporte;

    public static void main(String[] args) {
        JFrame frame = new JFrame("Productos");
        frame.setContentPane(new frmCRUDProductos().JfrmProductos);
        frame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        frame.pack();
        frame.setResizable(false);
        frame.setSize(750, 500);
        frame.setLocationRelativeTo(null);
        frame.setVisible(true);
    }

    public frmCRUDProductos() {

        comboBoxOrigen.addItem("Alemania");
        comboBoxOrigen.addItem("China");
        comboBoxOrigen.addItem("Corea del Sur");
        comboBoxOrigen.addItem("Estados Unidos");
        comboBoxOrigen.addItem("Francia");
        comboBoxOrigen.addItem("Japón");
        comboBoxOrigen.addItem("México");
        comboBoxOrigen.addItem("Países Bajos");
        comboBoxOrigen.addItem("Reino Unido");
        comboBoxOrigen.addItem("Suiza");


        btnLimpiar.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                txtIdProducto.setText("");
                txtDescripcion.setText("");
                comboBoxOrigen.setSelectedIndex(0);
                txtPrecio.setText("");
                txtExistencia.setText("");
            }
        });


        btnGrabar.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                JOptionPane.showMessageDialog(null,
                        "Id Producto: " + txtIdProducto.getText() + "\n" +
                        "Descripcion: " + txtDescripcion.getText() + "\n" +
                        "Origen: " + comboBoxOrigen.getSelectedItem().toString() + "\n" +
                        "Precio: " + txtPrecio.getText() + "\n" +
                        "Existencia: " + txtExistencia.getText());

                Producto producto = new Producto();
                producto.setDescripcion(txtDescripcion.getText());
                producto.setOrigen(comboBoxOrigen.getSelectedItem().toString());
                producto.setPrecio(Double.parseDouble(txtPrecio.getText()));
                producto.setExistencia(Integer.parseInt(txtExistencia.getText()));

                try{
                    new ProductoService().crearProducto(producto);
                    JOptionPane.showMessageDialog(null, "Producto guardado correctamente");
                } catch (Exception ex){
                    JOptionPane.showMessageDialog(null, "Error al guardar el producto");
                }
            }
        });


        btnBuscar.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                int idProducto = txtIdProducto.getText().isEmpty() ? 0 : Integer.parseInt(txtIdProducto.getText());
                try{
                    Producto productoEncontrado = new ProductoService().obtenerProducto(idProducto);
                    if(productoEncontrado != null){
                        JOptionPane.showMessageDialog(null, "Producto encontrado: " + productoEncontrado.getDescripcion());

                        txtDescripcion.setText(productoEncontrado.getDescripcion());
                        comboBoxOrigen.setSelectedItem(productoEncontrado.getOrigen());
                        txtPrecio.setText(String.valueOf(productoEncontrado.getPrecio()));
                        txtExistencia.setText(String.valueOf(productoEncontrado.getExistencia()));
                    } else {
                        JOptionPane.showMessageDialog(null, "Producto no encontrado");
                    }
                } catch (Exception ex){
                    JOptionPane.showMessageDialog(null, "Error al buscar el producto");
                }
            }
        });


        btnSalir.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                System.exit(0);
            }
        });


        btnGenerarReporte.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                frmGenerarReporte formGeneradorDeReportes = new frmGenerarReporte();
                formGeneradorDeReportes.mostrarFrmGenerarReporte();
            }
        });
    }











}
