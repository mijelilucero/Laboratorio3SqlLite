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
    private static BaseColor naranja = new BaseColor(255, 82, 27);
    private static BaseColor naranjaFuerte = new BaseColor(204, 48, 0);
    private static BaseColor amarillo = new BaseColor(255, 195, 0);
    private static BaseColor azul = new BaseColor(59, 148, 186);
    private static BaseColor apricot = new BaseColor(249, 204, 180);
    private static BaseColor mistyRose = new BaseColor(254, 233, 225);


    private static final Font TITLE_FONT = new Font(Font.FontFamily.HELVETICA, 24, Font.BOLD, azul);
    private static final Font HEADER_FONT = new Font(Font.FontFamily.HELVETICA, 14, Font.BOLD, BaseColor.WHITE);
    private static final Font HEADERGROUP_FONT = new Font(Font.FontFamily.HELVETICA, 12, Font.BOLD);
    private static final Font NORMALBOLD_FONT = new Font(Font.FontFamily.HELVETICA, 10, Font.BOLD);
    private static final Font NORMAL_FONT = new Font(Font.FontFamily.HELVETICA, 10, Font.NORMAL);

    public void generateProductReport(List<Producto> productos, String outputPath) throws DocumentException, IOException {
        Document document = new Document(PageSize.LETTER);
        PdfWriter.getInstance(document, new FileOutputStream(outputPath));
        document.open();

        try{
            addTitle(document);
            addProductTable(document, productos);
            addTextQR(document);
            addQrCode(document, "https://github.com/mijelilucero/Laboratorio3SqlLite");
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
        Paragraph title = new Paragraph("REPORTE DE PRODUCTOS", TITLE_FONT);
        title.setAlignment(Element.ALIGN_CENTER);
        document.add(title);
        document.add(Chunk.NEWLINE);
    }

    private void addTextQR(Document document) throws DocumentException {
        Paragraph textQR = new Paragraph("Código QR que redirige al repositorio en GitHub:", NORMAL_FONT);
        textQR.setAlignment(Element.ALIGN_CENTER);
        document.add(Chunk.NEWLINE);
        document.add(Chunk.NEWLINE);
        document.add(Chunk.NEWLINE);
        document.add(Chunk.NEWLINE);
        document.add(textQR);
    }

    private void addProductTable(Document document, List<Producto> productos) throws DocumentException {
        PdfPTable table = new PdfPTable(6); // 6 columnas para id, descripción, origen, precio, existencia y total
        table.setWidthPercentage(100);
        addTableHeader(table);
      //  addRows(table, productos);
        addRowsGroup(table, productos);
        document.add(table);
    }

    private void addTableHeader(PdfPTable table) {
       // Este código utiliza la clase Stream de Java para crear un flujo de datos con los títulos de las columnas de una tabla PDF. Luego,
        // para cada título de columna, se crea una celda de encabezado en la tabla con ciertas propiedades (color de fondo, ancho del borde, y texto).


        Stream.of("ID", "Descripción", "Origen", "Precio", "Existencia", "Total")
                .forEach(columnTitle -> {
                    PdfPCell header = new PdfPCell();
                    header.setBackgroundColor(naranja);
                    header.setBorderWidth(1);
                    header.setBorderColor(naranjaFuerte);
                    header.setPhrase(new Phrase(columnTitle, HEADER_FONT));
                    header.setHorizontalAlignment(Element.ALIGN_CENTER);
                    header.setVerticalAlignment(Element.ALIGN_MIDDLE);
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
                cell.setBackgroundColor(amarillo);
                table.addCell(cell);
            } else {
                table.addCell(new Phrase(producto.getOrigen(), NORMAL_FONT));
            }
            table.addCell(new Phrase(String.format("Q. %.2f", producto.getPrecio()), NORMAL_FONT));
            table.addCell(new Phrase(String.valueOf(producto.getExistencia()), NORMAL_FONT));
            table.addCell(new Phrase(String.valueOf((producto.getPrecio()*producto.getExistencia())), NORMAL_FONT));
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
                    cell.setBackgroundColor(amarillo);
                    table.addCell(cell);
                } else {
                    table.addCell(new Phrase(producto.getOrigen(), NORMAL_FONT));
                }
                table.addCell(new Phrase(String.format("Q. %.2f", producto.getPrecio()), NORMAL_FONT));
                table.addCell(new Phrase(String.valueOf(producto.getExistencia()), NORMAL_FONT));
                table.addCell(new Phrase(String.valueOf((producto.getPrecio()*producto.getExistencia())), NORMAL_FONT));
            } else {
                agrupado = producto.getOrigen();
                try{
                    //add blank line into the table
                    table.addCell(new Phrase("Grupo:"+producto.getOrigen(), HEADERGROUP_FONT));
                    table.addCell(new Phrase());
                    table.addCell(new Phrase());
                    table.addCell(new Phrase());
                    table.addCell(new Phrase());
                    table.addCell(new Phrase());
                }   catch (Exception e) {
                    System.out.println("Error al agrupar el PDF: " + e.getMessage());
                }
            }
        }
    }



    private void addRowsGroup(PdfPTable table, List<Producto> productos) {

        String currentOrigen = null;
        double groupTotalPrecio = 0.0;
        int groupTotalExistencia = 0;
        double groupTotalTotal= 0.0;

        double totalGeneralPrecio = 0.0;
        int totalGeneralExistencia = 0;
        double totalGeneralTotal= 0.0;

        for (Producto producto : productos) {
            if (currentOrigen == null) {
                // Primer grupo
                currentOrigen = producto.getOrigen();

                // Agregar fila de grupo
                PdfPCell groupCell = new PdfPCell(new Phrase("Grupo: " + currentOrigen, HEADERGROUP_FONT));
                groupCell.setColspan(6);
                groupCell.setBackgroundColor(apricot);
                table.addCell(groupCell);
            } else if (!producto.getOrigen().equals(currentOrigen)) {
                // El grupo ha cambiado, imprimir totales del grupo anterior
                PdfPCell totalCellLabel = new PdfPCell(new Phrase("Total Grupo " + currentOrigen, NORMALBOLD_FONT));
                totalCellLabel.setColspan(3);
                totalCellLabel.setBackgroundColor(mistyRose);
                table.addCell(totalCellLabel);

                PdfPCell preciogroupCell= new PdfPCell(new Phrase(String.format("Q. %.2f", groupTotalPrecio), NORMALBOLD_FONT));
                preciogroupCell.setBackgroundColor(mistyRose);
                table.addCell(preciogroupCell);

                PdfPCell existenciagroupCell= new PdfPCell(new Phrase(String.valueOf(groupTotalExistencia), NORMALBOLD_FONT));
                existenciagroupCell.setBackgroundColor(mistyRose);
                table.addCell(existenciagroupCell);

                PdfPCell totalgroupCell= new PdfPCell(new Phrase(String.format("Q. %.2f", groupTotalTotal), NORMALBOLD_FONT));
                totalgroupCell.setBackgroundColor(mistyRose);
                table.addCell(totalgroupCell);

                //Acumular totales generales
                totalGeneralPrecio += groupTotalPrecio;
                totalGeneralExistencia += groupTotalExistencia;
                totalGeneralTotal += groupTotalTotal;

                //Agregar división
                PdfPCell afterTotalCellLabel = new PdfPCell(new Phrase(" ",NORMAL_FONT));
                afterTotalCellLabel.setColspan(6);
                table.addCell(afterTotalCellLabel);

                // Reiniciar totales
                groupTotalPrecio = 0.0;
                groupTotalExistencia = 0;
                groupTotalTotal = 0.0;

                // Actualizar el origen actual al nuevo grupo
                currentOrigen = producto.getOrigen();

                // Agregar fila de nuevo grupo
                PdfPCell groupCell = new PdfPCell(new Phrase("Grupo: " + currentOrigen, HEADERGROUP_FONT));
                groupCell.setColspan(6);
                groupCell.setBackgroundColor(apricot);
                table.addCell(groupCell);
            }

            // Agregar fila del producto
            table.addCell(new Phrase(String.valueOf(producto.getIdProducto()), NORMAL_FONT));
            table.addCell(new Phrase(producto.getDescripcion(), NORMAL_FONT));

            if (producto.getOrigen().equals("China")) {
                PdfPCell cell = new PdfPCell(new Phrase(producto.getOrigen(), NORMAL_FONT));
                cell.setBackgroundColor(amarillo);
                table.addCell(cell);
            } else {
                table.addCell(new Phrase(producto.getOrigen(), NORMAL_FONT));
            }


            table.addCell(new Phrase(String.format("Q. %.2f", producto.getPrecio()), NORMAL_FONT));
            table.addCell(new Phrase(String.valueOf(producto.getExistencia()), NORMAL_FONT));
            table.addCell(new Phrase(String.format("Q. %.2f", (producto.getPrecio()*producto.getExistencia())), NORMAL_FONT));

            // Acumular totales
            groupTotalPrecio += producto.getPrecio();
            groupTotalExistencia += producto.getExistencia();
            groupTotalTotal += producto.getPrecio()*producto.getExistencia();
        }

        // Imprimir totales para el último grupo
        if (currentOrigen != null) {
            PdfPCell totalCellLabel = new PdfPCell(new Phrase("Total Grupo " + currentOrigen, NORMALBOLD_FONT));
            totalCellLabel.setColspan(3);
            totalCellLabel.setBackgroundColor(mistyRose);
            table.addCell(totalCellLabel);

            PdfPCell preciogroupCell= new PdfPCell(new Phrase(String.format("Q. %.2f", groupTotalPrecio), NORMALBOLD_FONT));
            preciogroupCell.setBackgroundColor(mistyRose);
            table.addCell(preciogroupCell);

            PdfPCell existenciagroupCell= new PdfPCell(new Phrase(String.valueOf(groupTotalExistencia), NORMALBOLD_FONT));
            existenciagroupCell.setBackgroundColor(mistyRose);
            table.addCell(existenciagroupCell);

            PdfPCell totalgroupCell= new PdfPCell(new Phrase(String.format("Q. %.2f", groupTotalTotal), NORMALBOLD_FONT));
            totalgroupCell.setBackgroundColor(mistyRose);
            table.addCell(totalgroupCell);


            totalGeneralPrecio += groupTotalPrecio;
            totalGeneralExistencia += groupTotalExistencia;
            totalGeneralTotal += groupTotalTotal;

            //Agregar división
            PdfPCell afterTotalCellLabel = new PdfPCell(new Phrase(" ",NORMAL_FONT));
            afterTotalCellLabel.setColspan(6);
            table.addCell(afterTotalCellLabel);
        }

        PdfPCell totalCellLabel = new PdfPCell(new Phrase("TOTALES GENERALES:", HEADER_FONT));
        totalCellLabel.setColspan(3);
        totalCellLabel.setBackgroundColor(naranja);
        table.addCell(totalCellLabel);

        PdfPCell precioGeneralCell= new PdfPCell(new Phrase(String.format("Q. %.2f", totalGeneralPrecio), HEADER_FONT));
        precioGeneralCell.setBackgroundColor(naranja);
        table.addCell(precioGeneralCell);

        PdfPCell existenciaGeneralCell= new PdfPCell(new Phrase(String.valueOf(totalGeneralExistencia), HEADER_FONT));
        existenciaGeneralCell.setBackgroundColor(naranja);
        table.addCell(existenciaGeneralCell);

        PdfPCell totalGeneralCell= new PdfPCell(new Phrase(String.format("Q. %.2f", totalGeneralTotal), HEADER_FONT));
        totalGeneralCell.setBackgroundColor(naranja);
        table.addCell(totalGeneralCell);
    }

}