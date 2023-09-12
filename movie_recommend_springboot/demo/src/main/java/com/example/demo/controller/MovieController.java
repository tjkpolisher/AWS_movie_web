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
    private MovieRepository repository;

    @RequestMapping(value="/box_office")
    @ResponseBody
    public String boxOffice() throws Exception {
        List<Movie> boxOfficeList = repository.findBoxOffice();
        List<Map> allMovie = new ArrayList<Map>();
        
        for (int i=0;i < boxOfficeList.size(); i++) {
            Map oneMovie = new HashMap();
            Movie movie = boxOfficeList.get(i);
            oneMovie.put("movie", movie);
            ArrayList <Movie> recommendMovieList = new ArrayList<Movie>();
            String movieTitle = movie.getTitle();
            System.out.println("movieTitle=" + movieTitle);

            HttpPost httpPost = new HttpPost("http://{yourAWSEC2IPAddress}:5000/movie_recommend");

            List<BasicNameValuePair> nvps = new ArrayList<>();
            nvps.add(new BasicNameValuePair("title", movieTitle));
            httpPost.setEntity(
                new UrlEncodedFormEntity(nvps, Charset.forName(("UTF-8"))));
            CloseableHttpClient httpClient = HttpClients.createDefault();
            CloseableHttpResponse response2 = httpClient.execute(httpPost);
            String flaskResult =
                EntityUtils.toString(response2.getEntity(),
                    Charset.forName("UTF-8"));
            System.out.println("flaskResult =" + flaskResult);

            httpClient.close();

            try {
                JSONArray jsonArray = new JSONArray(flaskResult);
                for (int j=0; j < jsonArray.length(); j++) {
                    JSONArray recommend = jsonArray.getJSONArray(j);
                    String recommendTitle = recommend.getString(0);
                    Movie recommendMovie = repository.findTitle(recommendTitle);
                    recommendMovieList.add(recommendMovie);
                }
                oneMovie.put("recommend", recommendMovieList);
                allMovie.add(oneMovie);
            } catch (Exception e) {
                System.out.println("e =" + e);
            }
            
        }
        return new JSONArray(allMovie).toString();
    }
}
