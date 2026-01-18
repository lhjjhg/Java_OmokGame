package Gui;

import org.xml.sax.InputSource;
import org.w3c.dom.Document;
import org.w3c.dom.NodeList;
import javax.xml.parsers.*;
import javax.swing.*;
import javax.swing.table.DefaultTableModel;
import java.awt.*;
import java.io.*;
import java.net.*;

public class PostalCode extends JFrame {
   
	private JTextField queryField;
    private JTable resultTable; // JTable 사용
    private JTextField postalField;
    private JTextField addressField;

    // 공공 데이터 API 인증키 및 URL
    private static final String PUBLIC_DATA_API_KEY = "ZpWqU%2BsA8dnGr7XVV%2BwzPJU9%2FvcbY6GE4aahTWlcA8Kzs9SWDHYMwyU2aCMhEiBtw7X02PNwJ5Gx1yO39qeOJw%3D%3D"; // 실제 API 키 입력
    private static final String PUBLIC_DATA_API_URL = "http://openapi.epost.go.kr/postal/retrieveNewAdressAreaCdSearchAllService/retrieveNewAdressAreaCdSearchAllService/getNewAddressListAreaCdSearchAll";

    public PostalCode(JTextField postalField, JTextField addressField) {
        this.postalField = postalField;
        this.addressField = addressField;

        setTitle("주소 검색");
        setSize(450, 300);
        setDefaultCloseOperation(DISPOSE_ON_CLOSE);
        setLocationRelativeTo(null);
        setLayout(new BorderLayout());
        setResizable(false);

        queryField = new JTextField();
        JButton searchButton = new JButton("검색");
        searchButton.addActionListener(e -> searchAddress());

        // 엔터 키로 검색 기능 추가
        queryField.addActionListener(e -> searchAddress());

        JPanel inputPanel = new JPanel();
        inputPanel.setLayout(new BorderLayout());
        inputPanel.add(queryField, BorderLayout.CENTER);
        inputPanel.add(searchButton, BorderLayout.EAST);

        // JTable 설정
        String[] columnNames = {"우편번호", "주소"};
        DefaultTableModel model = new DefaultTableModel(columnNames, 0);
        resultTable = new JTable(model);
        resultTable.setSelectionMode(ListSelectionModel.SINGLE_SELECTION);
        resultTable.getSelectionModel().addListSelectionListener(event -> {
            if (!event.getValueIsAdjusting()) {
                int selectedRow = resultTable.getSelectedRow();
                if (selectedRow != -1) {
                    String postalCode = (String) resultTable.getValueAt(selectedRow, 0);
                    String address = (String) resultTable.getValueAt(selectedRow, 1);
                    postalField.setText(postalCode);
                    addressField.setText(address);
                    dispose(); // 창 닫기
                }
            }
        });

        // 열 너비 설정 (3:7 비율)
        resultTable.getColumnModel().getColumn(0).setPreferredWidth(100); // 우편번호 열
        resultTable.getColumnModel().getColumn(1).setPreferredWidth(350); // 주소 열

        add(inputPanel, BorderLayout.NORTH);
        add(new JScrollPane(resultTable), BorderLayout.CENTER);
    }

    private void searchAddress() {
        String query = queryField.getText();
        if (query.isEmpty()) {
            JOptionPane.showMessageDialog(this, "주소를 입력하세요.");
            return;
        }

        try {
            // 쿼리 문자열을 인코딩
            String encodedQuery = URLEncoder.encode(query, "UTF-8");
            String apiUrl = PUBLIC_DATA_API_URL + "?serviceKey=" + PUBLIC_DATA_API_KEY + "&srchwrd=" + encodedQuery + "&countPerPage=10&currentPage=1";

            HttpURLConnection conn = (HttpURLConnection) new URL(apiUrl).openConnection();
            conn.setRequestMethod("GET");
            conn.setRequestProperty("Content-type", "application/xml");

            // 응답 코드 확인
            int responseCode = conn.getResponseCode();
            if (responseCode != HttpURLConnection.HTTP_OK) {
                JOptionPane.showMessageDialog(this, "API 호출 오류: " + responseCode + " - " + conn.getResponseMessage());
                return;
            }

            BufferedReader br = new BufferedReader(new InputStreamReader(conn.getInputStream()));
            StringBuilder response = new StringBuilder();
            String line;

            while ((line = br.readLine()) != null) {
                response.append(line);
            }
            br.close();

            // XML 응답 처리
            DocumentBuilderFactory factory = DocumentBuilderFactory.newInstance();
            DocumentBuilder builder = factory.newDocumentBuilder();
            Document document = builder.parse(new InputSource(new StringReader(response.toString())));

            // XML에서 필요한 데이터 추출
            NodeList zipNoList = document.getElementsByTagName("zipNo");
            NodeList lnmAdresList = document.getElementsByTagName("lnmAdres"); 

            DefaultTableModel model = (DefaultTableModel) resultTable.getModel();
            model.setRowCount(0); 

            if (zipNoList.getLength() > 0 && lnmAdresList.getLength() > 0) {
                for (int i = 0; i < zipNoList.getLength(); i++) {
                    String postalCode = zipNoList.item(i).getTextContent().trim();
                    String address = lnmAdresList.item(i).getTextContent().trim(); 
                    model.addRow(new Object[]{postalCode, address}); 
                }
            } else {
                JOptionPane.showMessageDialog(this, "도로명 주소를 찾을 수 없습니다.");
            }
        } catch (Exception e) {
            e.printStackTrace(); 
            JOptionPane.showMessageDialog(this, "주소 검색 중 오류가 발생했습니다: " + e.getMessage());
        }
    }
}
