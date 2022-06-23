package application;

import com.itextpdf.text.Chunk;
import com.itextpdf.text.Document;
import com.itextpdf.text.Element;
import com.itextpdf.text.Font;
import com.itextpdf.text.FontFactory;
import com.itextpdf.text.Paragraph;
import com.itextpdf.text.pdf.BaseFont;
import com.itextpdf.text.pdf.PdfContentByte;
import com.itextpdf.text.pdf.PdfPTable;
import com.itextpdf.text.pdf.PdfPageEventHelper;
import com.itextpdf.text.pdf.PdfWriter;

public class PrintPiedPages extends PdfPageEventHelper {

	static String compte_bancaire;

	public void onStartPage(PdfWriter writer, Document document) {
		/*
		 * int num_page = document.getPageNumber();
		 * 
		 * if(num_page == 1) {
		 * 
		 * ColumnText.showTextAligned(writer.getDirectContent(), Element.ALIGN_CENTER,
		 * new Phrase("Top Left"), 30, 810, 0);
		 * 
		 * System.out.println("aaaaaaaaaaaaaaaaaaaaaaaaa "+num_page);
		 * 
		 * com.itextpdf.text.Image imgSoc; try { imgSoc =
		 * com.itextpdf.text.Image.getInstance(
		 * "C:\\Application_Facturation\\Images\\img\\Logos\\entete_piedPage\\en_tete_page.jpg"
		 * ); //imgSoc.scaleToFit(570,1500); imgSoc.scaleToFit(100,200);
		 * imgSoc.setAbsolutePosition(200,780);
		 * 
		 * writer.add(imgSoc); document.add(imgSoc);
		 * 
		 * } catch (Exception e1) { // TODO Auto-generated catch block
		 * e1.printStackTrace(); System.out.println((e1.getMessage())); }
		 * 
		 * //ColumnText.showTextAligned(writer.getDirectContent(), Element.ALIGN_CENTER,
		 * new Phrase("Top Right"), 550, 800, 0); }
		 */
	}

	public void onEndPage(PdfWriter writer, Document document) {
		com.itextpdf.text.Image imgSoc;
		try {
			imgSoc = com.itextpdf.text.Image
					.getInstance("C:\\application_ordreMission\\images\\pied de pages\\pied_page_BNP.jpg");
			imgSoc.scaleToFit(580, 1500);
			imgSoc.setAbsolutePosition(7, 0);
			document.add(imgSoc);

		} catch (Exception e1) {
			System.out.println(e1.getMessage());
		}
	}

	public static PdfPTable getHeaderTable(int x, int y) {
		// Police (Facultatif)
		FontFactory.registerDirectories();
		Font fontArial = FontFactory.getFont("Arial", BaseFont.IDENTITY_H, 12);
		Font fontBGrisSmall = new Font(fontArial);
		fontBGrisSmall.setSize(7);
		fontBGrisSmall.setColor(150, 150, 150);

		PdfPTable table = new PdfPTable(1);
		table.setTotalWidth(100);
		table.setLockedWidth(true);
		table.getDefaultCell().setFixedHeight(20);
		table.getDefaultCell().setBorder(0);
		table.getDefaultCell().setHorizontalAlignment(Element.ALIGN_LEFT);
		Paragraph paragraphPage = new Paragraph(new Chunk(String.format("Page %d / %d", x, y), fontBGrisSmall));
		table.addCell(paragraphPage);
		return table;
	}

	public static void PageNumber(PdfWriter writer, Document document) {
		for (int i = 0; i < document.getPageNumber(); ++i) {

			PdfContentByte overContent = writer.getDirectContent();
			overContent.saveState();
			overContent.beginText();
			// overContent.setFontAndSize(font, 10.0f);
			overContent.setTextMatrix(100, 100); // x,y position souhaitée
			overContent.showText((i + 1 + "/" + writer.getCurrentPageNumber()).toString());

			overContent.endText();
			overContent.restoreState();
		}
		writer.close();

	}
}