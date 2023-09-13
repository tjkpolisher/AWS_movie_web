package com.example.demo.controller;

import org.springframework.beans.factory.annotation.Autowired;
import org.apache.hc.client5.http.classic.methods.*;
import org.apache.hc.client5.http.entity.*;
import org.apache.hc.client5.http.impl.classic.*;
import org.apache.hc.core5.http.io.entity.*;
import org.apache.hc.core5.http.message.*;
import org.json.JSONArray;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.ResponseBody;

import com.example.demo.repository.MovieRepository;

import com.example.demo.entity.Movie;

import java.util.*;
import java.nio.charset.Charset;

@Controller
public class MovieController {
    @Autowired
    // SQL 쿼리를 실행하는 객체 주입
    private MovieRepository repository;

    // URL이 box_office일 때 실행
    @RequestMapping(value="/box_office")
    @ResponseBody //JSON 문자열 리턴
    // box office 가장 최근 영화 5개와 각 영화와 가장 가까운 영화 3개씩 추천
    public String boxOffice() throws Exception {
        // box office 가장 최근 영화 5개 리턴
        List<Movie> boxOfficeList = repository.findBoxOffice();
        // 박스 오피스 최신 영화와 추천 영화가 함께 저장될 객체
        List<Map> allMovie = new ArrayList<Map>();
        
        //boxOffice에 저장된 객체의 수만큼 반복
        for (int i=0;i < boxOfficeList.size(); i++) {
            Map oneMovie = new HashMap(); // 영화 한 편과 해당 영화의 추천 영화가 저장될 객체
            Movie movie = boxOfficeList.get(i); // boxOffice에 저장된 영화 중 i번째 영화 리턴
            oneMovie.put("movie", movie); // 영화 정보 저장
            ArrayList <Movie> recommendMovieList = new ArrayList<Movie>();
            String movieTitle = movie.getTitle();
            System.out.println("movieTitle=" + movieTitle);

            // 호출할 Flask URL 설정 (http://본인EC2아이피:5000/movie_recommend)
            HttpPost httpPost = new HttpPost("http://{yourAwsEc2IpAddress}:5000/movie_recommend");

            List<BasicNameValuePair> nvps = new ArrayList<>(); // Flask로 전송할 영화 제목을 저장할 객체
            nvps.add(new BasicNameValuePair("title", movieTitle)); // Flask로 전송할 영화의 제목 설정 (파라미터 이름, 파라미터 값)
            
            // Flask로 전송할 문자 타입 설정: 한국어이기 때문에 UTF-8로 지정
            httpPost.setEntity(
                new UrlEncodedFormEntity(nvps, Charset.forName("UTF-8")));
            CloseableHttpClient httpClient = HttpClients.createDefault(); // Flask에 접속해 실행 결과를 가져올 객체
            CloseableHttpResponse response2 = httpClient.execute(httpPost); // Flask에 접속해 실행 결과 가져오기
            String flaskResult =
                EntityUtils.toString(response2.getEntity(),
                    Charset.forName("UTF-8"));
            System.out.println("flaskResult =" + flaskResult);

            httpClient.close();

            try {
                JSONArray jsonArray = new JSONArray(flaskResult); // Flask 서버에서 가져온 문자열을 JSON 형태 객체로 변환
                for (int j=0; j < jsonArray.length(); j++) {
                    // JSONArray recommend = jsonArray.getJSONArray(j);
                    // String recommendTitle = recommend.getString(0);
                    String recommendTitle = jsonArray.getString(0); // 추천 영화 정보 리턴
                    Movie recommendMovie = repository.findTitle(recommendTitle); // 추천 영화 제목의 영화 정보를 DB에서 조회
                    recommendMovieList.add(recommendMovie); // recommendMovieList에 추천 영화 정보 추가
                }
                // allMovie에 추천 영화 추가
                oneMovie.put("recommend", recommendMovieList);
                allMovie.add(oneMovie);
            } catch (Exception e) {
                System.out.println("e =" + e);
            }
            
        }
        // 박스오피스와 추천 영화가 모두 저장된 allMovie를 JSON 문자열로 변환해서 리턴
        return new JSONArray(allMovie).toString();
    }
}
