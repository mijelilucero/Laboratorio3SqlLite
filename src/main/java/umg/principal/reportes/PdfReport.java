package umg.principal.reportes;

import com.google.zxing.BarcodeFormat;
import com.google.zxing.MultiFormatWriter;
import com.google.zxing.client.j2se.MatrixToImageWriter;
import com.google.zxing.common.BitMatrix;
import com.itextpdf.text.*;
import com.itextpdf.text.pdf.*;

import umg.principal.DaseDatos.model.Producto;

import java.io.ByteArrayOutputStream;
import java.io.FileOutputStream;
import java.io.IOException;
import java.util.List;
import java.util.stream.Stream;

public class PdfReport {
    private static final Font TITLE_FONT = new Font(Font.FontFamily.COURIER, 18, Font.BOLDITALIC);
    private static final Font HEADER_FONT = new Font(Font.FontFamily.HELVETICA, 12, Font.BOLD);
    private static final Font NORMAL_FONT = new Font(Font.FontFamily.HELVETICA, 10, Font.NORMAL);

    public void generateProductReport(List<Producto> productos, String outputPath) throws DocumentException, IOException {
        Document document = new Document(PageSize.LETTER);
        PdfWriter.getInstance(document, new FileOutputStream(outputPath));
        document.open();

        try{
            addTitle(document);
            addProductTable(document, productos);
            addQrCode(document, "https://www.google.com");
            document.close();
        } catch (Exception e) {
            System.out.println("Error al generar el PDF: " + e.getMessage());
        }

    }

    private void addQrCode(Document document, String qrCodeData) throws DocumentException, Exception {
        // Generar el código QR
        BitMatrix bitMatrix = new MultiFormatWriter().encode(qrCodeData, BarcodeFormat.QR_CODE, 200, 200);
        ByteArrayOutputStream pngOutputStream = new ByteArrayOutputStream();
        MatrixToImageWriter.writeToStream(bitMatrix, "PNG", pngOutputStream);

        // Convertir el código QR a imagen
        Image qrImage = Image.getInstance(pngOutputStream.toByteArray());
        qrImage.setAlignment(Element.ALIGN_CENTER);
        document.add(qrImage);
    }

    private void addTitle(Document document) throws DocumentException {
        Paragraph title = new Paragraph("Reporte de mi Primer PDF", TITLE_FONT);
        title.setAlignment(Element.ALIGN_CENTER);
        document.add(title);
        document.add(Chunk.NEWLINE);
    }

    private void addProductTable(Document document, List<Producto> productos) throws DocumentException {
        PdfPTable table = new PdfPTable(5); // 4 columnas para id, descripción, origen y precio
        table.setWidthPercentage(100);
        addTableHeader(table);
      //  addRows(table, productos);
        addRowsGroup(table, productos);
        document.add(table);
    }

    private void addTableHeader(PdfPTable table) {
       // Este código utiliza la clase Stream de Java para crear un flujo de datos con los títulos de las columnas de una tabla PDF. Luego,
        // para cada título de columna, se crea una celda de encabezado en la tabla con ciertas propiedades (color de fondo, ancho del borde, y texto).



        Stream.of("ID", "Descripción", "Origen", "Precio", "Existencia")
                .forEach(columnTitle -> {
                    PdfPCell header = new PdfPCell();
                    header.setBackgroundColor(BaseColor.LIGHT_GRAY);
                    header.setBorderWidth(2);
                    header.setPhrase(new Phrase(columnTitle, HEADER_FONT));
                    table.addCell(header);
                });

        //Stream.of("ID", "Descripción", "Origen", "Precio"):
        //Crea un flujo (Stream) de datos con los elementos "ID", "Descripción", "Origen", y "Precio".


//        Este código utiliza la clase Stream de Java para crear un flujo de datos con los títulos de las columnas de una tabla PDF. Luego, para cada título de columna, se crea una celda de encabezado en la tabla con ciertas propiedades (color de fondo, ancho del borde, y texto).
//                Explicación del Código
//        Stream.of("ID", "Descripción", "Origen", "Precio"):
//        Crea un flujo (Stream) de datos con los elementos "ID", "Descripción", "Origen", y "Precio".
//.forEach(columnTitle -> { ... }):
//        Para cada elemento en el flujo (en este caso, cada título de columna), ejecuta el bloque de código dentro de las llaves { ... }.
//        Dentro del bloque forEach:
//        PdfPCell header = new PdfPCell();: Crea una nueva celda para la tabla PDF.
//        header.setBackgroundColor(BaseColor.LIGHT_GRAY);: Establece el color de fondo de la celda a gris claro.
//                header.setBorderWidth(2);: Establece el ancho del borde de la celda a 2 puntos.
//                header.setPhrase(new Phrase(columnTitle, HEADER_FONT));: Establece el texto de la celda con el título de la columna y la fuente de encabezado.
//                table.addCell(header);: Añade la celda a la tabla.
//¿Qué es Stream.of?
//                Stream.of es un método estático de la clase Stream en Java que se utiliza para crear un flujo (Stream) a partir de una secuencia de elementos. En este caso, se está utilizando para crear un flujo de cadenas de texto ("ID", "Descripción", "Origen", "Precio").
//        Código

    } //Fin de addTableHeader

    private void addRows(PdfPTable table, List<Producto> productos) {
        for (Producto producto : productos) {
            table.addCell(new Phrase(String.valueOf(producto.getIdProducto()), NORMAL_FONT));
            table.addCell(new Phrase(producto.getDescripcion(), NORMAL_FONT));

            if (producto.getOrigen().equals("China")) {
                PdfPCell cell = new PdfPCell(new Phrase(producto.getOrigen(), NORMAL_FONT));
                cell.setBackgroundColor(BaseColor.GREEN);
                table.addCell(cell);
            } else {
                table.addCell(new Phrase(producto.getOrigen(), NORMAL_FONT));
            }
            table.addCell(new Phrase(String.valueOf(producto.getPrecio()), NORMAL_FONT));
            table.addCell(new Phrase(String.valueOf(producto.getExistencia()), NORMAL_FONT));

            //table.addCell(new Phrase(String.format("Q%.2f", producto.getPrecio()), NORMAL_FONT));
        }
    }


    private void addRowsGroupOld(PdfPTable table, List<Producto> productos) {
        String agrupado ="";

        for (Producto producto : productos) {

            if (agrupado.equals(producto.getOrigen())) { //Si el origen es igual al anterior
                table.addCell(new Phrase(String.valueOf(producto.getIdProducto()), NORMAL_FONT));
                table.addCell(new Phrase(producto.getDescripcion(), NORMAL_FONT));

                if (producto.getOrigen().equals("China")) {
                    PdfPCell cell = new PdfPCell(new Phrase(producto.getOrigen(), NORMAL_FONT));
                    cell.setBackgroundColor(BaseColor.GREEN);
                    table.addCell(cell);
                } else {
                    table.addCell(new Phrase(producto.getOrigen(), NORMAL_FONT));
                }
                table.addCell(new Phrase(String.valueOf(producto.getPrecio()), NORMAL_FONT));
                table.addCell(new Phrase(String.valueOf(producto.getExistencia()), NORMAL_FONT));

            } else {
                agrupado = producto.getOrigen();
                try{
                    //add blank line into the table
                    table.addCell(new Phrase("Grupo:"+producto.getOrigen(), NORMAL_FONT));
                    table.addCell(new Phrase());
                    table.addCell(new Phrase());
                    table.addCell(new Phrase());
                    table.addCell(new Phrase());
                }   catch (Exception e) {
                    System.out.println("Error al agrupar el PDF: " + e.getMessage());
                }



            }
            //table.addCell(new Phrase(String.format("Q%.2f", producto.getPrecio()), NORMAL_FONT));
        }
    }



    private void addRowsGroup(PdfPTable table, List<Producto> productos) {
        String currentOrigen = null;
        double groupTotalPrecio = 0.0;
        int groupTotalExistencia = 0;

        for (Producto producto : productos) {
            if (currentOrigen == null) {
                // Primer grupo
                currentOrigen = producto.getOrigen();

                // Agregar fila de grupo
                PdfPCell groupCell = new PdfPCell(new Phrase("Grupo: " + currentOrigen, NORMAL_FONT));
                groupCell.setColspan(5);
                table.addCell(groupCell);
            } else if (!producto.getOrigen().equals(currentOrigen)) {
                // El grupo ha cambiado, imprimir totales del grupo anterior
                PdfPCell totalCellLabel = new PdfPCell(new Phrase("Total Grupo " + currentOrigen, NORMAL_FONT));
                totalCellLabel.setColspan(3);
                table.addCell(totalCellLabel);

                table.addCell(new Phrase(String.valueOf(groupTotalPrecio), NORMAL_FONT));
                table.addCell(new Phrase(String.valueOf(groupTotalExistencia), NORMAL_FONT));

                // Reiniciar totales
                groupTotalPrecio = 0.0;
                groupTotalExistencia = 0;

                // Actualizar el origen actual al nuevo grupo
                currentOrigen = producto.getOrigen();

                // Agregar fila de nuevo grupo
                PdfPCell groupCell = new PdfPCell(new Phrase("Grupo: " + currentOrigen, NORMAL_FONT));
                groupCell.setColspan(5);
                table.addCell(groupCell);
            }

            // Agregar fila del producto
            table.addCell(new Phrase(String.valueOf(producto.getIdProducto()), NORMAL_FONT));
            table.addCell(new Phrase(producto.getDescripcion(), NORMAL_FONT));

            if (producto.getOrigen().equals("China")) {
                PdfPCell cell = new PdfPCell(new Phrase(producto.getOrigen(), NORMAL_FONT));
                cell.setBackgroundColor(BaseColor.GREEN);
                table.addCell(cell);
            } else {
                table.addCell(new Phrase(producto.getOrigen(), NORMAL_FONT));
            }
            table.addCell(new Phrase(String.valueOf(producto.getPrecio()), NORMAL_FONT));
            table.addCell(new Phrase(String.valueOf(producto.getExistencia()), NORMAL_FONT));

            // Acumular totales
            groupTotalPrecio += producto.getPrecio();
            groupTotalExistencia += producto.getExistencia();
        }

        // Imprimir totales para el último grupo
        if (currentOrigen != null) {
            PdfPCell totalCellLabel = new PdfPCell(new Phrase("Total Grupo " + currentOrigen, NORMAL_FONT));
            totalCellLabel.setColspan(3);
            table.addCell(totalCellLabel);

            table.addCell(new Phrase(String.valueOf(groupTotalPrecio), NORMAL_FONT));
            table.addCell(new Phrase(String.valueOf(groupTotalExistencia), NORMAL_FONT));
        }
    }

}