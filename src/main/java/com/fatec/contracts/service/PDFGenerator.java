package com.fatec.contracts.service;

import com.fatec.contracts.exceptions.FillFieldOnPDFException;
import org.apache.pdfbox.pdmodel.PDDocument;
import org.apache.pdfbox.pdmodel.interactive.form.PDAcroForm;
import org.apache.pdfbox.pdmodel.interactive.form.PDTextField;
import org.springframework.stereotype.Service;

import java.io.File;
import java.io.IOException;
import java.util.HashMap;
import java.util.Map;

@Service
public class PDFGenerator {

    private PDDocument document;
    private PDAcroForm pdfForm;


    public void setDocument(String filePath) {
        try {
            this.document = PDDocument.load(new File(filePath));
        } catch (IOException e) {
            throw new IllegalArgumentException("O arquivo " + filePath + " não foi encontrado.");
        }
        this.pdfForm = document.getDocumentCatalog().getAcroForm();
    }

    public void fillFields(Map<String, String> fieldValues) {
        fieldValues.forEach((field, value) -> {
            PDTextField fieldObject = (PDTextField) this.pdfForm.getField(field);
            try {
                System.out.println(field + ": " + value);
                fieldObject.setValue(value);
            } catch (IOException ex) {
                throw new FillFieldOnPDFException("Não foi possível atribuir o valor " + value + " ao campo " + field + " do formulário");
            }
        });
    }

    public void saveDocument(String targetPath) throws IOException {
        document.save(targetPath);
    }

    public void closeDocument() throws IOException {
        document.close();
    }

    public PDDocument getDocument() {
        return document;
    }

}