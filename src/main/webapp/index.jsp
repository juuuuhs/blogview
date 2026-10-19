<%@ page language="java" contentType="text/html; charset=UTF-8"
    pageEncoding="UTF-8"%>
 <html>
 <body>
 	
<%@ include file="/include/header.jsp" %>
 	
 	<section class="visual">
 	
		<div id="carouselExample" class="carousel slide">
	<div class="carousel-inner">
	  <div class="carousel-item active">
	    <img src="${pageContext.request.contextPath}/imgaes/paniroom.jpg" class="d-block w-100" alt="...">
	  </div>
	  <div class="carousel-item">
	    <img src="${pageContext.request.contextPath}/imgaes/visual2.jpg" class="d-block w-100" alt="...">
	  </div>
	  <div class="carousel-item">
	    <img src="${pageContext.request.contextPath}/imgaes/wow.jpg" class="d-block w-100" alt="...">
	  </div>
	</div>
	<button class="carousel-control-prev" type="button" data-bs-target="#carouselExample" data-bs-slide="prev">
	  <span class="carousel-control-prev-icon" aria-hidden="true"></span>
	  <span class="visually-hidden">Previous</span>
	</button>
	<button class="carousel-control-next" type="button" data-bs-target="#carouselExample" data-bs-slide="next">
	  <span class="carousel-control-next-icon" aria-hidden="true"></span>
	  <span class="visually-hidden">Next</span>
	</button>
	</div>
 	
 	<!-- 하나의 주제나 기능을 가진 영역을 만들 떄 -->
 	<!--<img src="./imgaes.visual2.jpg" alt="메인비쥬얼" class="visual-img">-->
	<!--  ./현재디렉토리 밑에 omages 폴더 밑에 있는  --> 	
	<span class="visual-txt">Blog</span>	
 	</section>
 	<!-- 컨텐츠 영역: 최신글 인기글 들어갈 자리 -->
 	<main class="content">
 		<section class="content-box">
 		<h2 class="section-title">최신 작성글 </h2>
 		<ul class="post-card-list">
 		<c:forEach var="n" items="${newlist}" begin="0" end="2">
 		<li class="post-card">
		<c:choose>	 			
		<c:when test="${not empty n.imgPath}">	
 		<img src="/Ss/${n.imgPath}" alt="샘플이미지" class="post-thumb">
 		</c:when>
 		<c:otherwise>
 		 <img src="${pageContext.request.contextPath}/imgaes/noimg.jpg" alt="게시글 썸네일" class="popular-thumb">
 		</c:otherwise>
 		</c:choose>
 		<p class="post-title">${n.title}</p>
 		<p class="post-date">${n.reg_date} 너무너무 졸리다</p>
 		</li>
 		</c:forEach>
 	</ul>
</section>
	<section class="content-box">
 		<h2 class="section-title">인기글</h2>
 		<ul class="post-card-list">
 		<c:forEach var="b" items="${indexpop}" begin="0" end="2">
 		<li class="post-card">
		<c:choose>	 			
		<c:when test="${not empty b.imgPath}">	
 		<img src="/Ss/${b.imgPath}" alt="샘플이미지" class="post-thumb">
 		</c:when>
 		<c:otherwise>
 		 <img src="${pageContext.request.contextPath}/imgaes/noimg.jpg" alt="게시글 썸네일" class="popular-thumb">
 		</c:otherwise>
 		</c:choose>
 		<p class="post-title">${b.title}</p>
 		<p class="post-date">${b.reg_date} 너무너무 졸리다</p>
 		</li>
 		
 		</c:forEach>
 	</ul>
</section>
	<script src="https://ajax.googleapis.com/ajax/libs/jquery/3.7.1/jquery.min.js"></script>
   <script src="https://cdn.jsdelivr.net/npm/bootstrap@5.3.8/dist/js/bootstrap.bundle.min.js"></script>
 	</main>
 	<%@ include file="/include/footer.jsp" %>

 	
 	
 	</body>
 	
 </html>
