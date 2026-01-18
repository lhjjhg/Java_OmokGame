package Gui;

import java.awt.*; 
import java.awt.event.*; 
import javax.swing.*; 
import javax.swing.border.EmptyBorder;
import java.io.*;
import java.util.*;
import java.util.List;
import java.net.HttpURLConnection;
import java.net.URL;
import javax.xml.parsers.*;
import org.xml.sax.InputSource;
import org.w3c.dom.*;
import java.text.SimpleDateFormat;
import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;

public class WeatherFrame extends JFrame {
	
	Operator o = null;
	
    private static final long serialVersionUID = 1L;

    /* Panel */
    private JPanel mainPanel;
    private JPanel weatherPanel;
    /* Label */
    private JLabel cityL;
    private JLabel districtL;
    private JLabel regionL;
    private JLabel weatherinfoL;
    private JLabel weatherImageL;
    private JLabel dateL;
    private JLabel weatherL;
    private JLabel temperatureL;
    private JLabel precipitationL;
    private JLabel humidityL;
    /* Button */
    private JButton checkBtn;
    private JButton exitBtn;
    /* ComboBox */
    private JComboBox<String> cityBox;
    private JComboBox<String> districtBox;
    /* 시와 구를 저장할 리스트 */
    private Map<String, List<String>> locationMap;
    private Map<String, int[]> coordMap = new HashMap<>(); // 좌표 저장하는 맵

    /**
     * Launch the application.
     */
    public static void main(String[] args) {
        EventQueue.invokeLater(new Runnable() {
            public void run() {
                try {
                	Operator opt = new Operator();
                    WeatherFrame frame = new WeatherFrame(opt);
                    frame.setVisible(true);
                } catch (Exception e) {
                    e.printStackTrace();
                }
            }
        });
    }

    /**
     * Create the frame.
     */
    public WeatherFrame(Operator _o) {
    	
    	o = _o;
    	
        setTitle("오늘의 날씨");
        setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        setBounds(100, 100, 450, 450);
        mainPanel = new JPanel();
        mainPanel.setBackground(SystemColor.inactiveCaptionBorder);
        mainPanel.setBorder(new EmptyBorder(5, 5, 5, 5));

        weatherPanel = new JPanel();
        weatherPanel.setBackground(SystemColor.inactiveCaptionBorder);
        weatherPanel.setBounds(171, 39, 246, 295);
        mainPanel.add(weatherPanel);
        weatherPanel.setLayout(null);

        setContentPane(mainPanel);
        mainPanel.setLayout(null);

        locationMap = new HashMap<>();
        coordMap = new HashMap<>();
        loadLocations(); // CSV 파일로부터 시, 구 정보와 좌표를 불러옴

        regionL = new JLabel("지역");
        regionL.setBounds(67, 50, 28, 15);
        mainPanel.add(regionL);

        cityL = new JLabel("시 선택");
        cityL.setBounds(36, 88, 65, 15);
        mainPanel.add(cityL);

        districtL = new JLabel("구 선택");
        districtL.setBounds(36, 188, 65, 15);
        mainPanel.add(districtL);

        weatherinfoL = new JLabel("날씨 정보");
        weatherinfoL.setBounds(87, 10, 63, 15);
        weatherPanel.add(weatherinfoL);

        weatherImageL = new JLabel("");
        weatherImageL.setBounds(56, 30, 143, 113);
        weatherPanel.add(weatherImageL);

        dateL = new JLabel("");
        dateL.setBounds(56, 145, 180, 15);
        weatherPanel.add(dateL);

        weatherL = new JLabel("");
        weatherL.setBounds(56, 170, 180, 15);
        weatherPanel.add(weatherL);

        temperatureL = new JLabel("");
        temperatureL.setBounds(56, 195, 180, 15);
        weatherPanel.add(temperatureL);

        precipitationL = new JLabel("");
        precipitationL.setBounds(56, 220, 180, 15);
        weatherPanel.add(precipitationL);

        humidityL = new JLabel("");
        humidityL.setBounds(56, 245, 180, 15);
        weatherPanel.add(humidityL);

        checkBtn = new JButton("날씨 조회");
        checkBtn.setBackground(new Color(255, 255, 255));
        checkBtn.setBounds(36, 344, 105, 30);
        mainPanel.add(checkBtn);

        exitBtn = new JButton("닫기");
        exitBtn.setBackground(new Color(255, 255, 255));
        exitBtn.setBounds(293, 344, 105, 30);
        mainPanel.add(exitBtn);

        cityBox = new JComboBox<>(locationMap.keySet().toArray(new String[0]));
        cityBox.setBackground(SystemColor.textHighlightText);
        cityBox.setBounds(36, 107, 90, 30);
        mainPanel.add(cityBox);

        districtBox = new JComboBox<>();
        districtBox.setBackground(SystemColor.textHighlightText);
        districtBox.setBounds(36, 208, 90, 30);
        mainPanel.add(districtBox);

        /* addActionListener */
        cityBox.addActionListener(e -> updateDistricts());

        checkBtn.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                String selectedCity = (String) cityBox.getSelectedItem();
                String selectedDistrict = (String) districtBox.getSelectedItem();
                if (selectedCity != null && selectedDistrict != null) {
                    showWeather(selectedCity, selectedDistrict);
                }
            }
        });

        exitBtn.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {dispose();}
        });

    }

    /* CSV 파일에서 시와 구를 로드하는 메소드 */
    private void loadLocations() {
        try (BufferedReader br = new BufferedReader(new FileReader("src/main/java/Gui/weather.csv"))) {
            String line;
            br.readLine(); // 첫 번째 행 무시
            while ((line = br.readLine()) != null) {
                String[] values = line.split(",");
                String city = values[0];
                String district = values[1];
                int x = Integer.parseInt(values[2]);
                int y = Integer.parseInt(values[3]);

                // 시, 구 정보 저장
                locationMap.putIfAbsent(city, new ArrayList<>());
                List<String> districts = locationMap.get(city);
                if (!districts.contains(district)) {
                    districts.add(district);
                }
                // 좌표 저장
                coordMap.put(city + "_" + district, new int[] { x, y });
            }
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    /* 시 선택 - 구 선택 박스 업데이트 메서드 */
    private void updateDistricts() {
        String selectedCity = (String) cityBox.getSelectedItem();
        if (selectedCity != null) {
            districtBox.removeAllItems();
            List<String> districts = locationMap.get(selectedCity);
            if (districts != null) {
                for (String district : districts) {
                    districtBox.addItem(district);
                }
            }
        }
    }
    /* 지역에 따른 날씨 정보 업데이트 메서드 */
    private void showWeather(String city, String district) {
        int[] coord = coordMap.get(city + "_" + district);
        if (coord != null) {
            int nx = coord[0];
            int ny = coord[1]; 
            String xmlData = getWeatherFromAPI(nx, ny);

            // 현재 날짜
            Calendar calendar = Calendar.getInstance();
            SimpleDateFormat sdfDate = new SimpleDateFormat("yyyy-MM-dd");
            String currentDate = sdfDate.format(calendar.getTime()); // 현재 날짜

            // XML 데이터 파싱
            String weatherInfo = parseXML(xmlData, currentDate);

            // 날씨 정보 GUI 업데이트
            String[] weatherDetails = weatherInfo.split(", "); 
            if (weatherDetails.length >= 5) {
                dateL.setText("날짜: " + weatherDetails[0]); // 현재 날짜
                weatherL.setText("날씨 상태: " + weatherDetails[1]); // 날씨 상태
                temperatureL.setText(weatherDetails[2]); // 기온
                precipitationL.setText(weatherDetails[3]); // 강수량
                humidityL.setText(weatherDetails[4]); // 습도
                updateWeatherImage(weatherDetails[1]); // 날씨 상태 매개변수로 전달하여 이미지 메서드 
            } else {
                showErrorAndSetNull();
            }
        } else {
            showErrorAndSetNull();
        }
    }

    /* 기상청 API 호출하여 날씨 정보를 가져오는 메소드 */
    private String getWeatherFromAPI(int nx, int ny) {
        StringBuilder result = new StringBuilder();
        try {
            String apiUrl = "http://apis.data.go.kr/1360000/VilageFcstInfoService_2.0/getUltraSrtNcst";
            String serviceKey = "L81WkM1Ssv3FtJaKsBXdI2iUaXuZf3jfgX%2FvjGdQrcjf4Ea007rT%2FC138K%2BXykCeyi2Xu%2FHYct%2BIHqrikJ5OcA%3D%3D";

            // 현재 날짜 가져오기
            Calendar calendar = Calendar.getInstance();
            SimpleDateFormat sdfDate = new SimpleDateFormat("yyyyMMdd");

            LocalDateTime t = LocalDateTime.now().minusMinutes(30);

            @SuppressWarnings("deprecation")
            URL url = new URL(
                    apiUrl // 초단기예보조회 API
                            + "?ServiceKey=" + serviceKey // 서비스키
                            + "&pageNo=1" // 페이지번호 Default: 1
                            + "&numOfRows=60" // 한 페이지 결과 수 (10개 카테고리값 * 6시간)
                            + "&dataType=XML" // 요청자료형식(XML/JSON) Default: XML
                            + "&base_date=" + t.format(DateTimeFormatter.ofPattern("yyyyMMdd"))  // 발표 날짜
                            + "&base_time=" + t.format(DateTimeFormatter.ofPattern("HHmm")) // 발표 시각
                            + "&nx=" + nx // 예보지점의 X 좌표값
                            + "&ny=" + ny // 예보지점의 Y 좌표값
            );

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
    /* xml 파싱 메서드 */
    public String parseXML(String xmlData, String currentDate) {
        StringBuilder weatherInfo = new StringBuilder();
        try {
            // XML 파싱
            DocumentBuilderFactory factory = DocumentBuilderFactory.newInstance();
            DocumentBuilder builder = factory.newDocumentBuilder();
            Document document = builder.parse(new InputSource(new StringReader(xmlData)));

            // XML의 root element 확인
            document.getDocumentElement().normalize();

            // item 노드 리스트 추출
            NodeList nList = document.getElementsByTagName("item");

            String skyCondition = "알 수 없음"; 
            String temperature = "";
            String precipitation = "";
            String humidity = ""; 

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
                                skyCondition = "알 수 없음"; 
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
                    case "REH": // 습도
                        humidity = "습도: " + fcstValue + "%";
                        break;
                }
            }
            weatherInfo.append(currentDate).append(", ")
                    .append(skyCondition).append(", ")
                    .append(temperature).append(", ")
                    .append(precipitation).append(", ")
                    .append(humidity);

            // 디버깅 출력 
            String finalWeatherInfo = weatherInfo.toString();
            System.out.println(finalWeatherInfo);

        } catch (Exception e) {
            e.printStackTrace();
        }
        return weatherInfo.toString();
    }

    // 날씨에 따른 이미지를 업데이트하는 메소드
    private void updateWeatherImage(String weatherCondition) {
        String imagePath;
        switch (weatherCondition) {
            case "맑음":
                imagePath = "src/main/java/Image/sun.png"; // 맑음 이미지 경로
                break;
            case "구름많음":
                imagePath = "src/main/java/Image/cloudy.png"; // 구름많음 이미지 경로
                break;
            case "흐림":
                imagePath = "src/main/java/Image/cloudy.png"; // 흐림 이미지 경로
                break;
            case "비":
                imagePath = "src/main/java/Image/rain.png"; // 비 이미지 경로
                break;
            case "눈":
                imagePath = "src/main/java/Image/snow.png"; // 눈 이미지 경로
                break;
            default:
                imagePath = "path/to/default_image.png"; // 기본 이미지 경로
                break;
        }
        // 이미지 아이콘 설정
        weatherImageL.setIcon(
                new ImageIcon(new ImageIcon(imagePath).getImage().getScaledInstance(122, 90, Image.SCALE_SMOOTH)));
    }

    //오류 메세지
    private void showErrorAndSetNull() {
        JOptionPane.showMessageDialog(this, "날씨 정보를 불러오는 데 실패했습니다.", "에러", JOptionPane.ERROR_MESSAGE);
        
        dateL.setText("날짜: null");
        weatherL.setText("날씨 상태: null");
        temperatureL.setText("기온: null");
        precipitationL.setText("강수량: null");
        humidityL.setText("습도: null");
        weatherImageL.setIcon(null);
    }

}
