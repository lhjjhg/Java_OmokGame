package Test;

import javax.swing.*;
import javax.swing.border.EmptyBorder;
import java.awt.*;
import java.awt.event.*;
import java.io.*;
import java.util.*;
import java.util.List;
import java.io.*;
import java.io.InputStreamReader;
import java.net.HttpURLConnection;
import java.net.URL;
import java.net.URLEncoder;
import javax.xml.parsers.DocumentBuilderFactory;
import javax.xml.parsers.DocumentBuilder;
import org.w3c.dom.Document;
import org.w3c.dom.NodeList;
import org.w3c.dom.Element;
import java.text.SimpleDateFormat;
import java.util.Calendar;

public class WeatherFrameTest extends JFrame {
	
	/*  Operator operator; -- 다 완성 후 코드 집어 넣기 
	 * (WeatherFrame 메서드 매개 변수 집어넣기
	 * 메인 함수 삭제 */
	
	
	private JPanel contentPane;
	
    private JComboBox<String> cityBox;
    private JComboBox<String> districtBox;
    
    private JLabel weatherLabel;
    private JLabel  weatherImageLabel;
    
    // 시와 구를 저장할 리스트
    private Map<String, List<String>> locationMap;
    private Map<String, String[]> coordMap; // 좌표 저장하는 맵

    public WeatherFrameTest() {
    	
    	setTitle("WeatherFrame");
		setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
		setBounds(100, 100, 650, 500);
		contentPane = new JPanel();
		contentPane.setBorder(new EmptyBorder(5, 5, 5, 5));
		
		setContentPane(contentPane);
		contentPane.setLayout(null);

        locationMap = new HashMap<>();
        coordMap = new HashMap<>();
        loadLocations(); // CSV 파일로부터 시, 구 정보와 좌표를 불러옴

        JPanel cityP = new JPanel();
		cityP.setBounds(10, 62, 232, 99);
		contentPane.add(cityP);
		cityP.setLayout(null);
        
        cityBox = new JComboBox<>(locationMap.keySet().toArray(new String[0]));
        districtBox = new JComboBox<>();
        
        JLabel lblNewLabel = new JLabel("시 선택");
		lblNewLabel.setBounds(10, 10, 50, 15);
		cityP.add(lblNewLabel);
		
		cityP.add(cityBox);
		cityBox.setBounds(70, 6, 100, 23);
		
		JPanel districtP = new JPanel();
		districtP.setBounds(10, 171, 232, 99);
		contentPane.add(districtP);
		districtP.setLayout(null);
        
		JLabel lblNewLabel_1 = new JLabel("구 선택");
		lblNewLabel_1.setBounds(10, 10, 40, 15);
		districtP.add(lblNewLabel_1);
		
        districtP.add(districtBox);
        districtBox.setBounds(70, 6, 100, 23);
        
        // 오른쪽 패널 (날씨 정보 및 버튼)
        JPanel rightPanel = new JPanel();
        rightPanel.setBounds(252, 60, 500, 500); // 위치와 크기 설정
        contentPane.add(rightPanel);
        rightPanel.setLayout(null);
        
        // 날씨 조회 버튼
        JButton searchWeatherBtn = new JButton("날씨 조회");
        searchWeatherBtn.setBounds(81, 0, 104, 30); // 버튼 위치와 크기 설정
        rightPanel.add(searchWeatherBtn);
       
        //날씨 조회 버튼 이벤트 
        searchWeatherBtn.addActionListener(new ActionListener() {
            public void actionPerformed(ActionEvent e) {
                String selectedCity = (String) cityBox.getSelectedItem();
                String selectedDistrict = (String) districtBox.getSelectedItem();
                if (selectedCity != null && selectedDistrict != null) {
                    showWeather(selectedCity, selectedDistrict);
                }
            }
        });

     // 날씨 정보 표시할 레이블
        weatherLabel = new JLabel("날씨 정보: ");
        weatherLabel.setBounds(0, 10, 400, 100); // 레이블 위치와 크기 설정
        rightPanel.add(weatherLabel);
        
       
        // 시 콤보박스에 ActionListener 추가
        cityBox.addActionListener(new ActionListener() {
            public void actionPerformed(ActionEvent e) {
                String selectedCity = (String) cityBox.getSelectedItem();
                updateDistrictBox(selectedCity);
            }
        });
        
       // 날씨 이미지 레이블 추가
        weatherImageLabel = new JLabel();
        weatherImageLabel.setBounds(0, 100, 500, 250); // 위치 및 크기 설정
        rightPanel.add(weatherImageLabel);

        setVisible(true);
    }

    // CSV 파일에서 시와 구를 로드하는 메소드
    private void loadLocations() {
        try (BufferedReader br = new BufferedReader(new FileReader("src/main/java/Test/weather.csv"))) {
            String line;
            br.readLine(); // 첫 번째 행 무시 
            while ((line = br.readLine()) != null) {
                String[] values = line.split(",");
                String city = values[0];
                String district = values[1];
                String x = values[2]; 
                String y = values[3]; // csv파일 x,y 좌표 

             // 시, 구 정보 저장
                locationMap.putIfAbsent(city, new ArrayList<>());
                List<String> districts = locationMap.get(city);
                if (!districts.contains(district)) {
                    districts.add(district); 
                }
               // 좌표 저장
                coordMap.put(city + "_" + district, new String[]{x, y});
            }
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    // 구 선택 박스를 업데이트하는 메소드
    private void updateDistrictBox(String selectedCity) {
        districtBox.removeAllItems();
        List<String> districts = locationMap.get(selectedCity);
        if (districts != null) {
            for (String district : districts) {
                districtBox.addItem(district);
            }
        }
    }

	    // 선택된 지역의 날씨를 조회하여 표시하는 메소드
	    private void showWeather(String city, String district) {
	        String[] coord = coordMap.get(city + "_" + district);
	        if (coord != null) {
	            String nx = coord[0]; // x 좌표
	            String ny = coord[1]; // y 좌표
	            String xmlData = getWeatherFromAPI(nx, ny);
	         // 현재 날짜 정보를 가져옴
	            Calendar calendar = Calendar.getInstance();
	            SimpleDateFormat sdfDate = new SimpleDateFormat("yyyy-MM-dd");
	            String currentDate = sdfDate.format(calendar.getTime()); // 현재 날짜
	            String weatherInfo = parseXML(xmlData,currentDate); // XML 데이터 파싱
	            weatherLabel.setText("날씨 정보: " + weatherInfo);
	            updateWeatherImage(weatherInfo); // 날씨에 따른 이미지 설정 
	        } else {
	            weatherLabel.setText("날씨 정보를 찾을 수 없습니다.");
	            weatherImageLabel.setIcon(null); 
	        }
	    }

 // 기상청 API 호출하여 날씨 정보를 가져오는 메소드
    private String getWeatherFromAPI(String nx, String ny) {
        StringBuilder result = new StringBuilder();
        try {
            String apiUrl = "http://apis.data.go.kr/1360000/VilageFcstInfoService_2.0/getUltraSrtNcst";
            String serviceKey = "L81WkM1Ssv3FtJaKsBXdI2iUaXuZf3jfgX%2FvjGdQrcjf4Ea007rT%2FC138K%2BXykCeyi2Xu%2FHYct%2BIHqrikJ5OcA%3D%3D";

            // 현재 날짜와 시간 가져오기
            Calendar calendar = Calendar.getInstance();
            SimpleDateFormat sdfDate = new SimpleDateFormat("yyyyMMdd");
            SimpleDateFormat sdfTime = new SimpleDateFormat("HHmm");

            String baseDate = sdfDate.format(calendar.getTime()); // 오늘 날짜
            String baseTime = sdfTime.format(calendar.getTime()); // 현재 시간

            // 시간 보정 (가장 최근 시간으로 설정: 기상청은 매 정각 데이터를 제공)
            int currentHour = Integer.parseInt(baseTime.substring(0, 2));
            int currentMinute = Integer.parseInt(baseTime.substring(2, 4));

            if (currentMinute < 40) {
                // 현재 분이 40분 이전이면 한 시간 전의 데이터를 사용해야 함
                currentHour -= 1;
            }
            if (currentHour < 10) {
                baseTime = "0" + currentHour + "00"; // 한 자리수 시간 처리 (ex: 0900)
            } else {
                baseTime = currentHour + "00"; // 정각 처리
            }

            // URL 빌드
            StringBuilder urlBuilder = new StringBuilder(apiUrl);
            urlBuilder.append("?" + URLEncoder.encode("ServiceKey", "UTF-8") + "=" + serviceKey);
            urlBuilder.append("&" + URLEncoder.encode("nx", "UTF-8") + "=" + URLEncoder.encode(nx, "UTF-8")); // 경도
            urlBuilder.append("&" + URLEncoder.encode("ny", "UTF-8") + "=" + URLEncoder.encode(ny, "UTF-8")); // 위도
            urlBuilder.append("&" + URLEncoder.encode("base_date", "UTF-8") + "=" + URLEncoder.encode(baseDate, "UTF-8")); // 현재 날짜
            urlBuilder.append("&" + URLEncoder.encode("base_time", "UTF-8") + "=" + URLEncoder.encode(baseTime, "UTF-8")); // 정각 시간
            urlBuilder.append("&" + URLEncoder.encode("dataType", "UTF-8") + "=" + URLEncoder.encode("xml", "UTF-8")); // XML 형식

            // URL 연결
            URL url = new URL(urlBuilder.toString());
            HttpURLConnection conn = (HttpURLConnection) url.openConnection();
            conn.setRequestMethod("GET");
            conn.setRequestProperty("Content-type", "application/json");
            System.out.println("Response code: " + conn.getResponseCode());

            BufferedReader rd;
            if (conn.getResponseCode() >= 200 && conn.getResponseCode() <= 300) {
                rd = new BufferedReader(new InputStreamReader(conn.getInputStream()));
            } else {
                rd = new BufferedReader(new InputStreamReader(conn.getErrorStream()));
            }

            String line;
            while ((line = rd.readLine()) != null) {
                result.append(line);
            }
            rd.close();
            conn.disconnect();
        } catch (Exception e) {
            e.printStackTrace();
        }
        return result.toString();
    }

    
    public String parseXML(String xmlData, String currentDate) {
        StringBuilder weatherInfo = new StringBuilder(); // StringBuilder 사용으로 성능 개선
        try {
            // XML 파싱
            DocumentBuilderFactory factory = DocumentBuilderFactory.newInstance();
            DocumentBuilder builder = factory.newDocumentBuilder();
            Document document = builder.parse(new java.io.ByteArrayInputStream(xmlData.getBytes()));

            // XML의 root element 확인
            document.getDocumentElement().normalize();

            // item 노드 리스트 추출
            NodeList nList = document.getElementsByTagName("item");

            String skyCondition = "알 수 없음"; // 기본값으로 설정
            String temperature = "";
            String precipitation = ""; // 강수량
            String windSpeed = ""; // 풍속
            String humidity = ""; // 습도

            for (int i = 0; i < nList.getLength(); i++) {
                Element element = (Element) nList.item(i);

                // 카테고리와 값 추출
                String category = element.getElementsByTagName("category").item(0).getTextContent();
                String fcstValue = element.getElementsByTagName("obsrValue").item(0).getTextContent();

                // 디버깅 출력
                System.out.println("Category: " + category + ", Value: " + fcstValue); 

                switch (category) {
                    case "SKY": // 하늘 상태
                        switch (fcstValue) {
                            case "1":
                                skyCondition = "맑음";
                                break;
                            case "2":
                                skyCondition = "비";
                                break;
                            case "3":
                                skyCondition = "구름많음";
                                break;
                            case "4":
                                skyCondition = "흐림";
                                break;
                            default:
                                skyCondition = "알 수 없음"; // 예상치 못한 값 처리
                                break;
                        }
                        break;
                    case "PTY": // 강수 형태
                        if ("0".equals(fcstValue)) {
                            skyCondition = "맑음"; 
                        } else if ("1".equals(fcstValue)) {
                            skyCondition = "비"; 
                        } else if ("2".equals(fcstValue)) {
                            skyCondition = "눈"; 
                        }
                        break;
                    case "T1H": // 기온 정보
                        temperature = "기온: " + fcstValue + "℃";
                        break;
                    case "RN1": // 강수량
                        precipitation = "강수량: " + fcstValue + "mm";
                        break;
                    case "WSD": // 풍속 (삭제)
                        break;
                    case "REH": // 습도
                        humidity = "습도: " + fcstValue + "%";
                        break;
                }
            }

            // 최종 정보 조합 (현재 날짜 추가)
            weatherInfo.append(currentDate).append(", ") // 현재 날짜 추가
                        .append(skyCondition).append(", ") 
                        .append(temperature).append(", ")   
                        .append(precipitation).append(", ") 
                        .append(humidity);                 

            // GUI 업데이트
            String finalWeatherInfo = weatherInfo.toString();
            System.out.println(finalWeatherInfo); 
            weatherLabel.setText(finalWeatherInfo); 

        } catch (Exception e) {
            e.printStackTrace();
        }
        return weatherInfo.toString();
    }
    
 // 날씨에 따라 이미지를 업데이트하는 메소드
    private void updateWeatherImage(String weatherInfo) {
        String skyCondition = weatherInfo.split(", ")[0]; // 첫 번째 줄에서 하늘 상태 가져오기
        String imagePath = ""; // 이미지 경로를 저장할 변수

        // 하늘 상태에 따라 이미지 경로 설정
        switch (skyCondition) {
            case "맑음":
                imagePath = "C:\\Users\\lhjjh\\eclipse-workspace\\OmokProject\\src\\main\\java\\Image\\sunny.jpg"; // 맑음 이미지 경로
                break;
            case "비":
                imagePath = "C:\\Users\\lhjjh\\eclipse-workspace\\OmokProject\\src\\main\\java\\Image\\rain.jpg"; // 비 이미지 경로
                break;
            case "구름많음":
                imagePath = "C:\\Users\\lhjjh\\eclipse-workspace\\OmokProject\\src\\main\\java\\Image\\cloudy.jpg"; // 구름많음 이미지 경로
                break;
            case "흐림":
                imagePath = "C:\\Users\\lhjjh\\eclipse-workspace\\OmokProject\\src\\main\\java\\Image\\overcast.jpg"; // 흐림 이미지 경로
                break;
            default:
                imagePath = ""; // 알 수 없는 상태의 경우 이미지 초기화
                break;
        }

        // 이미지 설정
        if (!imagePath.isEmpty()) {
            ImageIcon icon = new ImageIcon(imagePath);
            weatherImageLabel.setIcon(icon);
        } else {
            weatherImageLabel.setIcon(null); // 이미지 초기화
        }
    }
    
    public static void main(String[] args) {
        new WeatherFrameTest();
    }
}
