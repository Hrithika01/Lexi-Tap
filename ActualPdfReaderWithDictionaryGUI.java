import java.awt.*;
import java.awt.event.*;
import javax.swing.*;
import org.apache.http.HttpEntity;
import org.apache.http.client.methods.CloseableHttpResponse;
import org.apache.http.client.methods.HttpGet;
import org.apache.http.impl.client.CloseableHttpClient;
import org.apache.http.impl.client.HttpClients;
import org.apache.http.util.EntityUtils;
import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import org.apache.pdfbox.pdmodel.PDDocument;
import org.apache.pdfbox.text.PDFTextStripper;

import java.io.File;
import java.io.IOException;

public class PdfReaderWithDictionaryGUI extends JFrame {
    private JTextArea textArea;
    private JButton fetchButton;
    private static final String DICTIONARY_API_URL = "https://api.dictionaryapi.dev/api/v2/entries/en/";

    public PdfReaderWithDictionaryGUI() {  
        setTitle("PDF Reader with Dictionary");
        setSize(600, 400);
        setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);

        textArea = new JTextArea();
        JScrollPane scrollPane = new JScrollPane(textArea);

        fetchButton = new JButton("Fetch Meaning");
        fetchButton.addActionListener(e -> {
            String selectedWord = getSelectedWord();
            if (!selectedWord.isEmpty()) {
                String definitions = fetchDictionaryInfo(selectedWord);
                displayResult("Word: " + selectedWord + "\nDefinitions:\n" + definitions);
            } else {
                displayResult("No word selected. Please select a word.");
            }
        });

        setLayout(new BorderLayout());
        add(scrollPane, BorderLayout.CENTER);
        add(fetchButton, BorderLayout.SOUTH);

        extractTextFromPdf("D:\\javaproject\\JAVA Lecture Notes.pdf");

        setVisible(true);
    }

    private void extractTextFromPdf(String pdfFilePath) {
        try {
            PDDocument document = PDDocument.load(new File(pdfFilePath));
            PDFTextStripper stripper = new PDFTextStripper();
            String pdfText = stripper.getText(document);
            document.close();
            textArea.setText(pdfText);
        } catch (IOException e) {
            JOptionPane.showMessageDialog(this, "Error loading PDF: " + e.getMessage(), "Error", JOptionPane.ERROR_MESSAGE);
        }
    }

    private String getSelectedWord() {
        int start = textArea.getSelectionStart();
        int end = textArea.getSelectionEnd();
        return (start != end) ? textArea.getText().substring(start, end).trim() : "";
    }

    private void displayResult(String result) {  // Removed return type
        JOptionPane.showMessageDialog(this, result, "Meaning", JOptionPane.INFORMATION_MESSAGE);
    }

    private String makeApiRequest(String word) {
        String endpoint = DICTIONARY_API_URL + word.toLowerCase();
        try (CloseableHttpClient httpClient = HttpClients.createDefault()) {
            HttpGet httpGet = new HttpGet(endpoint);
            try (CloseableHttpResponse response = httpClient.execute(httpGet)) {
                HttpEntity entity = response.getEntity();
                return entity != null ? EntityUtils.toString(entity) : null;
            }
        } catch (Exception e) {
            return "API request failed: " + e.getMessage();
        }
    }

    private String fetchDictionaryInfo(String word) {
        try {
            String jsonResponse = makeApiRequest(word);
            if (jsonResponse == null) return "No response from server.";
            return extractDefinitionFromJson(jsonResponse);
        } catch (Exception e) {
            return "Error fetching information: " + e.getMessage();
        }
    }

    private String extractDefinitionFromJson(String jsonResponse) {
        StringBuilder definitionsBuilder = new StringBuilder();
        try {
            JSONArray jsonArray = new JSONArray(jsonResponse);
            if (jsonArray.length() > 0) {
                JSONObject firstResult = jsonArray.getJSONObject(0);
                if (firstResult.has("meanings")) {
                    JSONArray meanings = firstResult.getJSONArray("meanings");
                    for (int i = 0; i < meanings.length(); i++) {
                        JSONObject meaning = meanings.getJSONObject(i);
                        if (meaning.has("definitions")) {
                            JSONArray definitions = meaning.getJSONArray("definitions");
                            if (definitions.length() > 0) {
                                definitionsBuilder.append((i + 1) + ". ");
                                definitionsBuilder.append(definitions.getJSONObject(0).getString("definition")).append("\n");
                            }
                        }
                    }
                }
            }
        } catch (JSONException e) {
            return "Invalid JSON response.";
        }
        return definitionsBuilder.toString().trim().isEmpty() ? "No definitions found." : definitionsBuilder.toString();
    }

    public static void main(String[] args) {
        SwingUtilities.invokeLater(PdfReaderWithDictionaryGUI::new);
    }
}
