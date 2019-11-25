package com.example.donggukalertapp.DataModel;

import java.util.HashMap;

public class NoticeUrl {
    public static HashMap<String, String> NoticeUrlHashMap = new HashMap<>();

    public static String getUrl(String key){
        NoticeUrlHashMap.put("nomal", "https://www.dongguk.edu/mbs/kr/jsp/board/list.jsp?boardId=3646&id=kr_010802000000"); // 일반
        NoticeUrlHashMap.put("bachelor", "https://www.dongguk.edu/mbs/kr/jsp/board/list.jsp?boardId=3638&id=kr_010801000000"); // 학사
        NoticeUrlHashMap.put("scholar_ship", "https://www.dongguk.edu/mbs/kr/jsp/board/list.jsp?boardId=3662&id=kr_010804000000"); // 장학
        NoticeUrlHashMap.put("admission", "https://www.dongguk.edu/mbs/kr/jsp/board/list.jsp?boardId=3654&id=kr_010803000000"); // 입시
        NoticeUrlHashMap.put("international", "https://www.dongguk.edu/mbs/kr/jsp/board/list.jsp?boardId=9457435&id=kr_010807000000"); // 국제
        NoticeUrlHashMap.put("event", "https://www.dongguk.edu/mbs/kr/jsp/board/list.jsp?boardId=3654&id=kr_010803000000"); // 행사

        return NoticeUrlHashMap.get(key);
    }
}
