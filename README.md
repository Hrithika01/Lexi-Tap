# **Lexi-Tap: PDF Reader with Dictionary Lookup**

Lexi-Tap is a Java-based **PDF reader** that allows users to extract text from a PDF file and look up the meaning of selected words using an **online dictionary API**. It provides an interactive GUI to read PDFs and instantly fetch word definitions.

## **Features**
- Load and display text from a PDF file  
- Select a word and fetch its meaning from **DictionaryAPI**  
- User-friendly GUI with interactive word selection  
- Error handling for API failures and invalid selections  

## **Technologies Used**
- **Java (Swing, AWT)** – GUI Development  
- **Apache PDFBox** – Extract text from PDFs  
- **Apache HttpClient** – HTTP requests to fetch dictionary meanings  
- **Dictionary API** – Fetch word definitions  

## **Usage**
1. Open **Lexi-Tap** – The GUI will display the extracted text from the PDF.  
2. Select a word by highlighting it in the text area.  
3. Click **"Fetch Meaning"** to get the word’s definition.  
4. The definition will be displayed in a popup window.  

## **Error Handling**
- **Invalid Selection** – If no word is selected, a message appears.  
- **API Failure** – If the API request fails, an error message is displayed.  
- **File Not Found** – If the PDF is missing or unreadable, an error message is shown.  

## **Future Enhancements**
- Support for multiple definitions and example sentences  
- Word pronunciation and phonetics  
- Support for other languages  

