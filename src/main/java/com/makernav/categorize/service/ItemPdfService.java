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

    // Cores baseadas na paleta da sua referência
    private static final Color AZUL_PRIMARY = new Color(37, 99, 235);   // #2563eb
    private static final Color AZUL_CABECALHO = new Color(239, 246, 255); // #eff6ff
    private static final Color TEXTO_ESCURO = new Color(30, 41, 59);     // #1e293b
    private static final Color TEXTO_MUTED = new Color(100, 116, 139);   // #64748b
    private static final Color CINZA_ZEBRADO = new Color(248, 250, 252);  // #f8fafc
    private static final Color BORDA_TABELA = new Color(226, 232, 240);  // #e2e8f0

    public ByteArrayInputStream gerarPdfItens(List<Item> itens) {
        Document document = new Document(PageSize.A4, 36, 36, 40, 40);
        ByteArrayOutputStream out = new ByteArrayOutputStream();

        try {
            PdfWriter.getInstance(document, out);
            document.open();

            // título principal
            Font fontTitulo = FontFactory.getFont(FontFactory.HELVETICA_BOLD, 18, AZUL_PRIMARY);
            Paragraph title = new Paragraph("RELATÓRIO DE ITENS - LABORATÓRIO", fontTitulo);
            title.setAlignment(Element.ALIGN_LEFT);
            title.setSpacingAfter(4);
            document.add(title);

            // subtítulo
            Font fontSub = FontFactory.getFont(FontFactory.HELVETICA, 10, TEXTO_MUTED);
            Paragraph sub = new Paragraph("CategorizeMaker System • Inventário de Materiais", fontSub);
            sub.setSpacingAfter(20);
            document.add(sub);

            // tabela 4 colunas
            PdfPTable table = new PdfPTable(4);
            table.setWidthPercentage(100);
            table.setWidths(new float[] { 3.5f, 4.5f, 2f, 2.5f });

            // cabecalhos
            addHeader(table, "CATEGORIA");
            addHeader(table, "NOME DO ITEM");
            addHeader(table, "QTD");
            addHeader(table, "ESTADO");

            // linhas de dadps
            boolean zebrado = false;
            for (Item item : itens) {
                Color background = zebrado ? CINZA_ZEBRADO : Color.WHITE;

                addBodyCell(table, item.getCategoria() != null ? item.getCategoria().name() : "-", background, false);
                addBodyCell(table, item.getNome(), background, true); // Nome em negrito
                addBodyCell(table, String.valueOf(item.getQuantidade()), background, false);
                addBodyCell(table, item.getEstado() != null ? item.getEstado().name() : "-", background, false);

                zebrado = !zebrado; // Alterna a cor da linha
            }

            document.add(table);
            document.close();

        } catch (DocumentException e) {
            throw new RuntimeException("Erro ao gerar PDF de itens", e);
        }

        return new ByteArrayInputStream(out.toByteArray());
    }

    private void addHeader(PdfPTable table, String text) {
        Font font = FontFactory.getFont(FontFactory.HELVETICA_BOLD, 9, AZUL_PRIMARY);
        PdfPCell cell = new PdfPCell(new Phrase(text, font));
        cell.setBackgroundColor(AZUL_CABECALHO);
        cell.setBorderColor(BORDA_TABELA);
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
            TEXTO_ESCURO
        );

        PdfPCell cell = new PdfPCell(new Phrase(text, font));
        cell.setBackgroundColor(backgroundColor);
        cell.setBorderColor(BORDA_TABELA);
        // Espaçamento interno vertical dobrado pra dar respiro no texto
        cell.setPaddingTop(9);
        cell.setPaddingBottom(9);
        cell.setPaddingLeft(8);
        cell.setPaddingRight(8);
        table.addCell(cell);
    }
}