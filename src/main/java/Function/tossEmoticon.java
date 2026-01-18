package Function;

public class tossEmoticon {
    // 이모티콘 코드 정의
    public static final String ANGRY_CODE = "(분노)";
    public static final String LOVE_CODE = "(사랑)";
    public static final String HAPPY_CODE = "(행복)";
    public static final String SAD_CODE = "(슬픔)";
    public static final String CURIOUS_CODE = "(무표정)";
    public static final String SURPRISE_CODE = "(놀람)";
    
    // 이모티콘 이미지 경로 정의
    public static final String ANGRY_PATH = "/Image/분노.png";
    public static final String LOVE_PATH = "/Image/사랑.png";
    public static final String HAPPY_PATH = "/Image/행복.png";
    public static final String SAD_PATH = "/Image/슬픔.png";
    public static final String CURIOUS_PATH = "/Image/무표정.png";
    public static final String SURPRISE_PATH = "/Image/놀람.png";
    
    // 이모티콘 코드에 따라 이미지 경로 반환
    public static  String tossEmo(String emoticonCode) {
        switch (emoticonCode) {
            case ANGRY_CODE: return ANGRY_PATH;
            case LOVE_CODE: return LOVE_PATH;
            case HAPPY_CODE: return HAPPY_PATH;
            case SAD_CODE: return SAD_PATH;
            case CURIOUS_CODE: return CURIOUS_PATH;
            case SURPRISE_CODE: return SURPRISE_PATH;
            default:
                System.out.println("Invalid emoticon code: " + emoticonCode);
                return null;
        }
    }
}
