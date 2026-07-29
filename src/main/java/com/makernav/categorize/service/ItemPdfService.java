package com.makernav.categorize.service;

import com.lowagie.text.*;
import com.lowagie.text.pdf.PdfPCell;
import com.lowagie.text.pdf.PdfPTable;
import com.lowagie.text.pdf.PdfWriter;
import com.makernav.categorize.model.Item;
import org.springframework.stereotype.Service;

import java.awt.Color;
import java.io.ByteArrayInputStream;
import java.io.ByteArrayOutputStream;
import java.util.List;

@Service
public class ItemPdfService {

    public ByteArrayInputStream gerarPdfItens(List<Item> itens) {
        Document document = new Document(PageSize.A4);
        ByteArrayOutputStream out = new ByteArrayOutputStream();

        try {
            PdfWriter.getInstance(document, out);
            document.open();

            // Título
            Font fontTitulo = FontFactory.getFont(FontFactory.HELVETICA_BOLD, 16);
            Paragraph title = new Paragraph("Relatório de Itens - Laboratório", fontTitulo);
            title.setAlignment(Element.ALIGN_CENTER);
            document.add(title);
            document.add(new Paragraph(" ")); 

            // Tabela
            PdfPTable table = new PdfPTable(4);
            table.setWidthPercentage(100);
            table.setWidths(new float[] { 3, 4, 2, 2 });

            addHeader(table, "Categoria");
            addHeader(table, "Nome");
            addHeader(table, "Qtd");
            addHeader(table, "Estado");

            for (Item item : itens) {
                table.addCell(item.getCategoria() != null ? item.getCategoria().name() : "-");
                table.addCell(item.getNome());
                table.addCell(String.valueOf(item.getQuantidade()));
                table.addCell(item.getEstado() != null ? item.getEstado().name() : "-");
            }

            document.add(table);
            document.close();

        } catch (DocumentException e) {
            throw new RuntimeException("Erro ao gerar PDF de itens", e);
        }

        return new ByteArrayInputStream(out.toByteArray());
    }

    private void addHeader(PdfPTable table, String text) {
        PdfPCell cell = new PdfPCell(new Phrase(text, FontFactory.getFont(FontFactory.HELVETICA_BOLD, 10)));
        cell.setBackgroundColor(new Color(230, 230, 230));
        cell.setPadding(6);
        table.addCell(cell);
    }
}