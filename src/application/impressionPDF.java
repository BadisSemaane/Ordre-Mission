package application;

import java.awt.Desktop;
import java.io.File;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

import com.itextpdf.text.BaseColor;
import com.itextpdf.text.Chunk;
import com.itextpdf.text.Document;
import com.itextpdf.text.DocumentException;
import com.itextpdf.text.Element;
import com.itextpdf.text.Font;
import com.itextpdf.text.FontFactory;
import com.itextpdf.text.Image;
import com.itextpdf.text.PageSize;
import com.itextpdf.text.Paragraph;
import com.itextpdf.text.Phrase;
import com.itextpdf.text.pdf.BaseFont;
import com.itextpdf.text.pdf.PdfPCell;
import com.itextpdf.text.pdf.PdfPTable;
import com.itextpdf.text.pdf.PdfWriter;

public class impressionPDF {

	static Font font = FontFactory.getFont("c://windows//fonts//calibri.ttf", BaseFont.IDENTITY_H, BaseFont.EMBEDDED,
			0.9f, Font.NORMAL, BaseColor.BLACK);

	static BaseFont baseFont = font.getBaseFont();
	Font font2 = new Font(baseFont, 11f, Font.BOLD);

	private static Font subFont = new Font(baseFont, 21, Font.BOLD);
	private static Font simpleUndelined = new Font(baseFont, 12, Font.UNDERLINE);

	private static Font simplel_12_Bold = new Font(baseFont, 14, Font.BOLD);
	private static Font simplel_10_notBold = new Font(baseFont, 12, Font.NORMAL);

	static String nom_pdf = "", FILE_name = "";
	static String type_de_devis; // TVA => R_GARANTIE ou r_garantie_tva
	static ordreMission ordreMission;
	public static String pathSiganture;

	/////////////////////////////////////// Fontions

	public static void imprimer_OrdreMission() {

		//////////////////// création du PDF
		try {
			String ref = ordreMission.getReference().replace('/', '-');
			nom_pdf = "OM " + ref + ordreMission.getClient() + " " + ordreMission.getSite();
			FILE_name = "C:\\application_ordreMission\\ordre missions\\" + nom_pdf + ".pdf";

			File f = new File(FILE_name);
			int cmp = 1;

			/*
			 * f.delete(); if (!f.delete()) {
			 * System.out.println("bbbbbbbbbbbbbbbbbbbbbbbb"); }
			 */
			while (f.isFile()) {
				if (cmp != 1) {
					FILE_name = FILE_name.substring(0, FILE_name.length() - 16);
				} else {
					FILE_name = FILE_name.substring(0, FILE_name.length() - 4);
				}
				cmp++;
				// FILE_name.replaceAll(".pdf", " ");

				FILE_name += "-Version (" + cmp + ")";
				FILE_name += ".pdf";
				f = new File(FILE_name);
			}
			Document document = new Document(PageSize.A4, 40f, 40f, 55f, 55f);

			PdfWriter writer = PdfWriter.getInstance(document, new FileOutputStream(FILE_name));

			if (document != null && document.isOpen()) {
				document.close();
			}
			// Fermeture du pdfWriter
			if (writer != null && !writer.isCloseStream()) {
				writer.close();
			}

			PrintPiedPages PrintPiedPages_event = new PrintPiedPages();
			writer.setPageEvent(PrintPiedPages_event);

			document.open();
			writer.open();

			addContent(document, writer);

			document.close();
			Desktop.getDesktop().open(new File("C:\\application_ordreMission\\ordre missions\\"));
			Desktop.getDesktop().open(new File(FILE_name));
		} catch (Exception e) {
			e.printStackTrace();
		}
	}

	///////////////////////////////////////////////////////////

	private static void addEmptyLine(Paragraph paragraph, int number) {
		for (int i = 0; i < number; i++) {
			paragraph.add(new Paragraph(" "));
		}
	}

	private static String addEmptySpace(String contenue, int number) {
		String spaces = " ";
		for (int i = 0; i < number; i++) {
			spaces += " ";
		}
		contenue = spaces + contenue;
		return contenue;
	}
	///////////////////////////////////////////////////////////

	private static Document createTable(Document doc) throws DocumentException {

		PdfPTable table = new PdfPTable(2);
		table.setWidthPercentage(100);
		// table = new Table(new float[] { 1, 3 });
		table.setTotalWidth(new float[] { 20, 80 });

		// 1) employés
		PdfPCell c1 = new PdfPCell(new Phrase("Nom et prénom :", simplel_12_Bold));
		c1.setHorizontalAlignment(Element.ALIGN_LEFT);
		c1.setVerticalAlignment((Element.ALIGN_TOP));
		c1.setBorderColor(BaseColor.WHITE);
		table.addCell(c1);

		// 1.2 : Liste des employés
		PdfPTable table_employes = new PdfPTable(2);
		table_employes.setWidthPercentage(100);
		table_employes.setTotalWidth(new float[] { 30, 70 });

		for (employe emp : ordreMission.getListEmployes()) {
			List<String> infos_emp = Arrays.asList(emp.getNom(), emp.getPrenom());
			for (String info : infos_emp) {
				c1 = new PdfPCell(new Phrase(info, simplel_10_notBold));
				c1.setHorizontalAlignment(Element.ALIGN_LEFT);
				c1.setVerticalAlignment((Element.ALIGN_MIDDLE));
				c1.setFixedHeight(20);
				c1.setBorderColor(BaseColor.WHITE);
				table_employes.addCell(c1);
			}
		}
		c1 = new PdfPCell(table_employes);
		c1.setHorizontalAlignment(Element.ALIGN_LEFT);
		c1.setVerticalAlignment((Element.ALIGN_MIDDLE));
		c1.setBorderColor(BaseColor.WHITE);
		table.addCell(c1);

		// 2) Fonction :
		c1 = new PdfPCell(new Phrase("Fonction :", simplel_12_Bold));
		c1.setHorizontalAlignment(Element.ALIGN_LEFT);
		c1.setVerticalAlignment((Element.ALIGN_MIDDLE));
		c1.setFixedHeight(30);
		c1.setBorderColor(BaseColor.WHITE);
		table.addCell(c1);

		ArrayList<String> list_functions = new ArrayList<String>();
		String liste_functions = "";
		for(employe emp : ordreMission.getListEmployes()) {
			if( ! list_functions.contains(emp.poste)) {
				list_functions.add(emp.poste);
				liste_functions+= " / "+emp.poste;
			}
		}
		liste_functions = liste_functions.substring(3);
		c1 = new PdfPCell(new Phrase(liste_functions, simplel_10_notBold));
		c1.setHorizontalAlignment(Element.ALIGN_LEFT);
		c1.setVerticalAlignment((Element.ALIGN_MIDDLE));
		c1.setBorderColor(BaseColor.WHITE);
		table.addCell(c1);

		// 1) Mission
		c1 = new PdfPCell(new Phrase("Mission :", simplel_12_Bold));
		c1.setHorizontalAlignment(Element.ALIGN_LEFT);
		c1.setVerticalAlignment((Element.ALIGN_MIDDLE));
		c1.setFixedHeight(30);
		c1.setBorderColor(BaseColor.WHITE);
		table.addCell(c1);

		c1 = new PdfPCell(new Phrase(ordreMission.getMission(), simplel_10_notBold));
		c1.setHorizontalAlignment(Element.ALIGN_LEFT);
		c1.setVerticalAlignment((Element.ALIGN_MIDDLE));
		c1.setBorderColor(BaseColor.WHITE);
		table.addCell(c1);

		// 2) Destination
		c1 = new PdfPCell(new Phrase("Destiantion :", simplel_12_Bold));
		c1.setHorizontalAlignment(Element.ALIGN_LEFT);
		c1.setVerticalAlignment((Element.ALIGN_MIDDLE));
		c1.setFixedHeight(30);
		c1.setBorderColor(BaseColor.WHITE);
		table.addCell(c1);

		c1 = new PdfPCell(new Phrase(ordreMission.getDestinatio(), simplel_10_notBold));
		c1.setHorizontalAlignment(Element.ALIGN_LEFT);
		c1.setVerticalAlignment((Element.ALIGN_MIDDLE));
		c1.setFixedHeight(30);
		c1.setBorderColor(BaseColor.WHITE);
		table.addCell(c1);

		// 3) Date depart
		c1 = new PdfPCell(new Phrase("Date départ :", simplel_12_Bold));
		c1.setHorizontalAlignment(Element.ALIGN_LEFT);
		c1.setVerticalAlignment((Element.ALIGN_MIDDLE));
		c1.setFixedHeight(30);
		c1.setBorderColor(BaseColor.WHITE);
		table.addCell(c1);

		c1 = new PdfPCell(new Phrase(ordreMission.getDate_dep(), simplel_10_notBold));
		c1.setHorizontalAlignment(Element.ALIGN_LEFT);
		c1.setVerticalAlignment((Element.ALIGN_MIDDLE));
		c1.setFixedHeight(30);
		c1.setBorderColor(BaseColor.WHITE);
		table.addCell(c1);

		// 4) Date retour
		c1 = new PdfPCell(new Phrase("Date retour :", simplel_12_Bold));
		c1.setHorizontalAlignment(Element.ALIGN_LEFT);
		c1.setVerticalAlignment((Element.ALIGN_MIDDLE));
		c1.setFixedHeight(30);
		c1.setBorderColor(BaseColor.WHITE);
		table.addCell(c1);

		c1 = new PdfPCell(new Phrase(ordreMission.getDate_ret(), simplel_10_notBold));
		c1.setHorizontalAlignment(Element.ALIGN_LEFT);
		c1.setVerticalAlignment((Element.ALIGN_MIDDLE));
		c1.setFixedHeight(30);
		c1.setBorderColor(BaseColor.WHITE);
		table.addCell(c1);

		// 5) Véhicules
		c1 = new PdfPCell(new Phrase("Véhicule :", simplel_12_Bold));
		c1.setHorizontalAlignment(Element.ALIGN_LEFT);
		c1.setVerticalAlignment((Element.ALIGN_MIDDLE));
		c1.setBorderColor(BaseColor.WHITE);
		table.addCell(c1);

		// 5) Véhicules
		c1 = new PdfPCell(new Phrase(
				ordreMission.getVehicule().getMarque() + " "+  ordreMission.getVehicule().getNom() + " imatriculé : " + ordreMission.getVehicule().getMatricule(),
				simplel_10_notBold));
		c1.setHorizontalAlignment(Element.ALIGN_LEFT);
		c1.setBorderColor(BaseColor.WHITE);
		table.addCell(c1);

		doc.add(table);
		return doc;
	}

	/////////////////////////////////////////////////////////

	public static void addContent(Document document, PdfWriter writer) throws DocumentException, FileNotFoundException {

		com.itextpdf.text.Image imgSoc;
		try {
			imgSoc = com.itextpdf.text.Image
					.getInstance("C:\\application_ordreMission\\images\\pied de pages\\en_tete_page.jpg");

			imgSoc.scaleToFit(570, 1500);
			imgSoc.setAbsolutePosition(15, 765);

			document.add(imgSoc);

		} catch (Exception e1) {
			System.out.println("Erreur imgSoc :" + e1.getMessage());
		}

		Paragraph Para = new Paragraph();
		addEmptyLine(Para, 2);
		document.add(Para);

		Para = new Paragraph();

		Chunk ch = new Chunk("Réf/  ", simplel_12_Bold);
		Para.add(ch);
		ch = new Chunk(ordreMission.getReference(), simplel_12_Bold);
		Para.add(ch);
		ch = new Chunk(addEmptySpace("Alger le, " + ordreMission.getDate_etabli(), 90), simplel_12_Bold);
		Para.add(ch);

		addEmptyLine(Para, 5);
		document.add(Para);

		ch = new Chunk("ORDRE DE MISSION", subFont);
		Para = new Paragraph();
		Para.add(ch);
		Para.setAlignment(Element.ALIGN_CENTER);

		addEmptyLine(Para, 5);

		document.add(Para);

		/*
		 * Para = new Paragraph(); Chunk ch = new
		 * Chunk(addEmptySpace("ORDRE DE MISSION N° " + ordreMission.getReference(),
		 * 22), subFont); Para.add(ch);
		 * 
		 * Para.add(ch); document.add(Para); Para = new Paragraph(); addEmptyLine(Para,
		 * 1); document.add(Para);
		 * 
		 * Para = new Paragraph(); addEmptyLine(Para, 1); document.add(Para);
		 */
		document = createTable(document);

////////////////sevise comercial
		Para = new Paragraph();
		addEmptyLine(Para, 2);
		Chunk space = new Chunk(addEmptySpace("", 115));
		Chunk val2 = new Chunk("Signature et Cachet", simpleUndelined);
		Para.add(space);
		Para.add(val2);
		document.add(Para);

		if (pathSiganture != null) {
			document.add(space);
			String path = pathSiganture.substring(6);

			File f = new File(path);
			if (f.isFile()) {
				com.itextpdf.text.Image image;
				try {
					image = com.itextpdf.text.Image.getInstance(path);
					image.scaleAbsolute(170, 90);
					image.setAlignment(Image.ALIGN_RIGHT);
					document.add(image);
				} catch (Exception e) {
					System.out.println("erreur image signature");
				}
			}
		}
	}

	public static String eliminer_espace(String prix) {
		String prix2 = "";
		for (int i = 0; i < prix.length(); i++) {
			char c = prix.charAt(i);
			if (c == '0' || c == '1' || c == '2' || c == '3' || c == '4' || c == '5' || c == '6' || c == '7' || c == '8'
					|| c == '9' || c == ',' || c == '.' || c == '-' || c == '+') {
				prix2 += c;
			}
		}
		prix = prix2.replaceAll(",", ".");
		return prix;
	}
}
