//React import
import React from "react";
import { useEffect } from "react";
import {useState} from "react";

import axios from 'axios'

const MovieList = () =>{ //MovieList 객체 생성

    //const [allMovie, setAllMovie]  : 변수와 set 함수 선언
    //변수명 allMovie
    //변수 값을 수정 하는 set 함수 setAllMovie

    //useState([]) : allMovie 변수의 초기값을 [] 로 설정 
   const [allMovie, setAllMovie] = useState([])


    //const [loading, setLoading] : 변수와 set 함수 선언
    //변수명 loading
    //변수 값을 수정 하는 set 함수 setLoading 
   

    //useState(false) : loading 변수의 초기값을 false로 설정 
    const [loading, setLoading] = useState(false);


    const takeMovieList = async() =>{
        
        //백엔드에서 데이터를 가져오기 시작 할때 load변수에 true 대입
        setLoading(true);        

        //백엔드 스프링부트의 http://localhost:8080/box_office를 호출하고
        //리턴값을 response에 저장 
        //await axios.post('/box_office')  : 백엔드에서 데이터 가져오기를 완료 할때 까지 
        //다음줄을 실행하지 말고 프로그램은 여기서 멈춰 있음
        const response = await axios.post('/box_office');
        //response에 저장된 값을 콘솔창에 출력
        console.log("response=",response); 
 
        //allMovie 변수에 값 대입
        setAllMovie(response.data);
 
        //백엔드에서 데이터 가져오기 완료 했을때 loading 변수에 false 대입
        setLoading(false);
      
        
   };

    useEffect(() => {

        takeMovieList(); //페이지 맨처음 나타 날때 takeMovieList() 함수 실행
      },
      []  //페이지 실행 맨처음 나타날때 1번 실행
      );


    //백엔드 스프링부트의 http://localhost:8080/box_office를 호출하고
    //리턴값을 response에 저장 
    //const response = axios.post('/box_office');
    //response에 저장된 값을 콘솔창에 출력
    //console.log("response=",response);

    return (
                <>
                    <h1>MovieList.js 실행</h1>
                    {/* 
                      react에서 변수를 사용하기 위해서는 {} 안에 변수를 사용
                      {loading} : loading 변수 사용 또는 출력
                      
                      && : and 연산자
                      
                      loading 변수가 true 이면 출력 메시지 출력
                      {loading && 출력 메시지 }

                    */}
                    {loading && <h1>현재 백엔드에서 데이터를 가져 오는 중 입니다</h1> }

                    {/* allMovie변수의 값 출력 */}

                    {/* allMovie.map((data,index) : allMovie의 데이터를 1개씩 순서대로 data에 대입해서 반복문 실행*/}
                    {allMovie.map((data,index)=>(
                        
                        //반복문 내용
                        <>
                        <p>Box Office {index+1} 위</p>  {/* index + 1 출력 */}
                        {/* 영화 정보는 data.movie에 저장되 있음 */}
                        <p>제목 : {data.movie.title}</p> {/* 영화 제목 출력 */}
                        <p>장르: {data.movie.genre}</p>  {/* 영화 장르 출력 */}
                        <p><img src={data.movie.poster}></img></p> {/* 영화 포스터 출력 */}
                        <p>{data.movie.synopsys}</p> {/* 영화 줄거리 출력 */}
                        <hr/>
                        
                        <h1>{data.movie.title} 과 비슷한 줄거리의 영화</h1>{/* 영화 제목 출력 */}
                        <table align="center" width="100%" border="1">
                            <tr>
                                {/* data.recommend.map((rec) :data의 recommend를 1개씩 rec에 대입*/}
                                {data.recommend.map((rec)=>(
                                    <td width="33%">
                                        <p>제목:{rec.title}</p> {/* 추천 영화 제목 출력 */}
                                        <p>장르:{rec.genre}</p> {/* 추천 영화 장르 출력 */}
                                        <p><img src={rec.poster}></img></p> {/* 추천 영화 포스터 출력 */}
                                        <p>{rec.synopsys}</p> {/* 추천 영화 줄거리 출력 */}
                                    </td>
                                ))}
                            </tr>
                        </table>
                        
                        </>
                    ))}
                </>
           ); //MovieList 리턴값 설정 리턴한 값이 화면에 출력됨
}

export default MovieList; //export default : MovieList.js에는 1개의 객체만 존재함
                          //export default MovieList; 1개의 객체 MovieList를 
                         //다른 JS에서 사용 가능 하도록 내보냄
