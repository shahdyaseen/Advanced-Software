package com.example.Rental.Services;import com.example.Rental.models.Entity.Payment;
import com.itextpdf.text.*;
import com.itextpdf.text.Font;
import com.itextpdf.text.pdf.PdfPCell;
import com.itextpdf.text.pdf.PdfPTable;
import com.itextpdf.text.pdf.PdfWriter;
import org.springframework.stereotype.Service;
import com.itextpdf.text.Document;

import java.awt.*;
import java.io.ByteArrayOutputStream;
import java.io.FileOutputStream;
import java.io.IOException;
import java.math.BigDecimal;
import java.text.SimpleDateFormat;
import java.util.Date;

@Service
public class InvoiceService {

    public byte[] generateInvoice(Payment payment) throws DocumentException, IOException {
        Document document = new Document();
        ByteArrayOutputStream baos = new ByteArrayOutputStream();
        PdfWriter.getInstance(document, baos);

        document.open();
        addInvoiceHeader(document, payment);
        addInvoiceTable(document, payment);
        addInvoiceFooter(document, payment);

        document.close();
        return baos.toByteArray();
    }

    private void addInvoiceHeader(Document document, Payment payment) throws DocumentException {
        Font headerFont = FontFactory.getFont(FontFactory.HELVETICA_BOLD, 16);
        Paragraph header = new Paragraph("Invoice", headerFont);
        header.setAlignment(Element.ALIGN_CENTER);
        document.add(header);

        document.add(new Paragraph("Invoice Date: " + new SimpleDateFormat("yyyy-MM-dd").format(new Date())));
        document.add(new Paragraph("Payment ID: " + payment.getId()));
    }

    private void addInvoiceTable(Document document, Payment payment) throws DocumentException {
        PdfPTable table = new PdfPTable(2);
        table.setWidthPercentage(100);

        PdfPCell cell = new PdfPCell(new Phrase("Invoice Details"));
        cell.setColspan(2);
        cell.setHorizontalAlignment(Element.ALIGN_CENTER);
        table.addCell(cell);

        table.addCell("Payment Method:");
        table.addCell(payment.getPaymentMethod().name());

        table.addCell("Total Amount:");
        table.addCell("$" + payment.getAmount().toString());

        table.addCell("Status:");
        table.addCell(payment.getStatus().name());

        table.addCell("Delivery Fee:");
        table.addCell("$" + payment.getDeliveryFee().toString());

        document.add(table);
    }

    private void addInvoiceFooter(Document document, Payment payment) throws DocumentException {
        Paragraph footer = new Paragraph("Thank you for your payment!");
        footer.setAlignment(Element.ALIGN_CENTER);
        document.add(footer);
    }

    public void saveInvoiceToFile(byte[] invoiceBytes, String filePath) throws IOException {
        try (FileOutputStream fos = new FileOutputStream(filePath)) {
            fos.write(invoiceBytes);
        }
    }
}
