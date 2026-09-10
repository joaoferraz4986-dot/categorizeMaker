package com.makernav.categorize.service;

import java.awt.Color;
import java.io.ByteArrayInputStream;
import java.io.ByteArrayOutputStream;

import org.springframework.data.domain.Page;
import org.springframework.stereotype.Service;

import com.lowagie.text.Document;
import com.lowagie.text.DocumentException;
import com.lowagie.text.Element;
import com.lowagie.text.Font;
import com.lowagie.text.FontFactory;
import com.lowagie.text.PageSize;
import com.lowagie.text.Paragraph;
import com.lowagie.text.Phrase;
import com.lowagie.text.pdf.PdfPCell;
import com.lowagie.text.pdf.PdfPTable;
import com.lowagie.text.pdf.PdfWriter;
import com.makernav.categorize.dto.ItemResponseDTO;

@Service
public class ItemPdfService {

    private static final Color PRETO = Color.BLACK;
    private static final Color BRANCO = Color.WHITE;
    private static final Color CINZA_TEXTO = new Color(80, 80, 80);
    private static final Color CINZA_LINHA = new Color(185, 185, 185);
    private static final Color CINZA_ZEBRADO = new Color(245, 245, 245);

    public ByteArrayInputStream gerarPdfItens(Page<ItemResponseDTO> itens) {
        Document document = new Document(PageSize.A4, 36, 36, 40, 40);
        ByteArrayOutputStream out = new ByteArrayOutputStream();

        try {
            PdfWriter.getInstance(document, out);
            document.open();

            Font fontTitulo = FontFactory.getFont(FontFactory.HELVETICA_BOLD, 18, PRETO);
            Paragraph title = new Paragraph("RELATÓRIO DE ITENS - LABORATÓRIO", fontTitulo);
            title.setAlignment(Element.ALIGN_LEFT);
            title.setSpacingAfter(4);
            document.add(title);

            Font fontSub = FontFactory.getFont(FontFactory.HELVETICA, 10, CINZA_TEXTO);
            Paragraph sub = new Paragraph("CategorizeMaker System • Inventário de Materiais", fontSub);
            sub.setSpacingAfter(20);
            document.add(sub);

            PdfPTable table = new PdfPTable(4);
            table.setWidthPercentage(100);
            table.setWidths(new float[] { 3.5f, 4.5f, 2f, 2.5f });

            addHeader(table, "CATEGORIA");
            addHeader(table, "NOME DO ITEM");
            addHeader(table, "QTD");
            addHeader(table, "ESTADO");

            boolean zebrado = false;
            for (ItemResponseDTO item : itens) {
                Color background = zebrado ? CINZA_ZEBRADO : BRANCO;

                addBodyCell(table, item.categoria() != null ? item.categoria().name() : "-", background, false);
                addBodyCell(table, item.nome(), background, true);
                addBodyCell(table, String.valueOf(item.quantidade()), background, false);
                addBodyCell(table, item.estado() != null ? item.estado().name() : "-", background, false);

                zebrado = !zebrado;
            }

            document.add(table);
            document.close();
        } catch (DocumentException e) {
            throw new RuntimeException("Erro ao gerar PDF de itens", e);
        }

        return new ByteArrayInputStream(out.toByteArray());
    }

    private void addHeader(PdfPTable table, String text) {
        Font font = FontFactory.getFont(FontFactory.HELVETICA_BOLD, 9, BRANCO);
        PdfPCell cell = new PdfPCell(new Phrase(text, font));
        cell.setBackgroundColor(PRETO);
        cell.setBorderColor(PRETO);
        cell.setPaddingTop(10);
        cell.setPaddingBottom(10);
        cell.setPaddingLeft(8);
        cell.setPaddingRight(8);
        table.addCell(cell);
    }

    private void addBodyCell(PdfPTable table, String text, Color backgroundColor, boolean bold) {
        Font font = FontFactory.getFont(
                bold ? FontFactory.HELVETICA_BOLD : FontFactory.HELVETICA,
                9,
                PRETO);

        PdfPCell cell = new PdfPCell(new Phrase(text, font));
        cell.setBackgroundColor(backgroundColor);
        cell.setBorderColor(CINZA_LINHA);
        cell.setPaddingTop(9);
        cell.setPaddingBottom(9);
        cell.setPaddingLeft(8);
        cell.setPaddingRight(8);
        table.addCell(cell);
    }
}
