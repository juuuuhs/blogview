 	<%@ page language="java" contentType="text/html; charset=UTF-8"
    pageEncoding="UTF-8"%>
    
	<%@ taglib uri="jakarta.tags.core" prefix="c" %> 
	<%@ taglib uri="jakarta.tags.functions" prefix="fn" %>
 <html>

 	<head>
 		<title>블로그 프로젝트 !! </title>
 		<meta charset="utf-8">
 		<link href="https://cdn.jsdelivr.net/npm/bootstrap@5.3.8/dist/css/bootstrap.min.css" rel="stylesheet" integrity="sha384-sRIl4kxILFvY47J16cr9ZwB07vP4J8+LH7qKQnuqkuIAvNWLzeN8tE5YBujZqJLB" crossorigin="anonymous">
 		<link href="${pageContext.request.contextPath}/css/common.css" rel="styLesheet">
 		<link href="${pageContext.request.contextPath}/css/mystyle.css" rel="styLesheet">
 
 </head>
 	<body>
 	

 	<header class="site-header"> <!-- class = "별칭" 클래스이름이 사이트 헤더라는 태그 --> 
 	<!-- 사이트의 검색창 하는 거, 본문 머리글  -->
 		<div class="header-inline">
 		<h1 class="Logo"><a href="${pageContext.request.contextPath}/main/index">MY BLOG</a></h1>	
 	<nav class="gnb"><!-- 메뉴 상단바 (바로가기 아이콘 마냥 -->
 		<ul>
 		<!-- ul,li 순서가 없는 목록 ol,li는 순서가  있는 목록으로 생성-->
 		<li><a href="${pageContext.request.contextPath}/blog/list">글 목록</a></li>
 		<li><a href="${pageContext.request.contextPath}/blog/popular">인기글</a></li>
 		</ul>
 	</nav>
	<div class="member-menu">
	
	
 	<!-- 여러 요소를 묶음으로 만들 때 사용 , 정리하기 편리  --> 
 	<c:choose> 
 	<c:when test="${empty sessionScope.loginMember}">
 		<a href="${pageContext.request.contextPath}/member/login">로그인</a>
 		<a href="${pageContext.request.contextPath}/member/join">회원가입</a>
 		</c:when>
 		<c:otherwise> 
 		<span>${sessionScope.loginMember.nickname}아가님</span>
 		<a href="${pageContext.request.contextPath}/member/logout">로그아웃</a>
 		<a href="${pageContext.request.contextPath}/member/mypage">마이페이지</a>
 		</c:otherwise>
 	</c:choose>
 	</div>
 	</div>
 	 <script src="https://ajax.googleapis.com/ajax/libs/jquery/3.7.1/jquery.min.js"></script>
 	</header>