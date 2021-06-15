package com.spring.demo.application.report;

import java.awt.Color;
import java.io.IOException;
import java.io.OutputStream;
import java.util.List;

import javax.servlet.http.HttpServletResponse;

import com.lowagie.text.Document;
import com.lowagie.text.DocumentException;
import com.lowagie.text.Font;
import com.lowagie.text.FontFactory;
import com.lowagie.text.PageSize;
import com.lowagie.text.Paragraph;
import com.lowagie.text.Phrase;
import com.lowagie.text.pdf.PdfPCell;
import com.lowagie.text.pdf.PdfPTable;
import com.lowagie.text.pdf.PdfWriter;
import com.spring.demo.application.entities.Contact;

public class ContactsPdfReport {
	
	List<Contact> contactList;

	public ContactsPdfReport(List<Contact> contactList) {
		super();
		this.contactList = contactList;
	}
	
	private void tableHeader(PdfPTable table) {
		
		PdfPCell cell = new PdfPCell();
		cell.setBackgroundColor(Color.BLACK);
		cell.setPadding(5);
		
		Font font = FontFactory.getFont(FontFactory.HELVETICA);
		font.setColor(Color.WHITE);
		
		cell.setPhrase(new Phrase("ID", font));
        table.addCell(cell);
         
        cell.setPhrase(new Phrase("Name", font));
        table.addCell(cell);
         
        cell.setPhrase(new Phrase("Phone No.", font));
        table.addCell(cell);
         
        cell.setPhrase(new Phrase("Email", font));
        table.addCell(cell);
         
        cell.setPhrase(new Phrase("User", font));
        table.addCell(cell); 
	}
	
	private void tableData(PdfPTable table) {
		
		for (Contact contact : contactList) {
			table.addCell(String.valueOf(contact.getcId()));
			table.addCell(contact.getName());
			table.addCell(contact.getPhone());
			table.addCell(contact.getEmail());
			table.addCell(contact.getUser().getName());
		}
	}
	
	public void export(HttpServletResponse response) throws DocumentException, IOException {
		
		Document document = new Document(PageSize.A4);
		PdfWriter.getInstance(document, response.getOutputStream());
		
		document.open();
		Font font = FontFactory.getFont(FontFactory.HELVETICA_BOLD);
		font.setColor(Color.BLACK);
		font.setSize(18);
		
		Paragraph p = new Paragraph("List of Contacts", font);
		p.setAlignment(Paragraph.ALIGN_CENTER);
		
		document.add(p);
		
		PdfPTable table = new PdfPTable(5);
		table.setWidthPercentage(100f);
		table.setWidths(new float[] {1f, 3.5f, 3.5f, 4f, 3f});
		table.setSpacingBefore(10);
		
		tableHeader(table);
		tableData(table);
		document.add(table);
		document.close();
	}

}
