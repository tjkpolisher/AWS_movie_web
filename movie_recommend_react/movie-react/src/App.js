//App.js 는 React의 main 문으로 여기서 부터 React가 실행됨
//React 라우터 관련 객체들 import
import { BrowserRouter as Router, Route, Routes } from "react-router-dom";
//박스 오피스 영화정보와 추천 영화 정보는 front 디렉토리 MovieList.js에 구현 할 것임
///front/MovieList.js 의 MovieList 객체를 import
import MovieList from "./front/MovieList";
//Recact 객체 import
import React from "react";

function App() {  
  //여기가 메인문  
  return (
    <Router> 
    <Routes>
      {/*  입력한 url이 http://localhost:3000 일때 MovieList 객체를 실행 할 것임 */ }
      <Route path="/" element={<MovieList/>}/> 
    </Routes>
    </Router>
  );
}

export default App;
