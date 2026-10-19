<%@ page language="java" contentType="text/html; charset=UTF-8"
    pageEncoding="UTF-8"%>
 <html>
 <body>
 <%@ include file="/include/header.jsp" %>
 	<!-- 컨텐츠 영역: 최신글 인기글 들어갈 자리 -->
 	<main class="list-wrap">
 	<!-- 검색창,글쓰기 버튼 만드는 법  -->
 	<div class = "list-top>">
 		<form class="search-form" action="${pageContext.request.contextPath}/blog/list" method="get">
 			<select name="searchType" class="search-select">
 			<option value="">선택</option>
 			<option value="title">${searchType == "title" ? 'selected' : '' }제목</option>
 			<option value="content">${searchType == "content" ? 'selected' : '' }내용</option>
 			<option value="writer">${searchType == "writer" ? 'selected' : '' }글쓴이</option>
 			</select>
 			<input type="text" name="keyword" value="${keyword}" }placeholder="검색어를 입력하시오" class="search-input">
 			<button type="submit" class="btn btn-outline">검색</button>
 			<a href="${pageContext.request.contextPath}/blog/wirte" class="btn btn-primary">글쓰기</a>
 		</form>
 		
 	
 	</div>
	<!-- 게시글 카드 목록 -->
	<ul class="board-grid">
	<c:forEach var="b" items="${boardList}">
         <li class="board-card">
            <c:choose>
               <c:when test="${not empty b.imgPath}">
                  <img src="/Ss/${b.imgPath}" alt="게시글 썸네일" class="board-thumb">
               </c:when>
               <c:otherwise>
                  <img src="${pageContext.request.contextPath}/imgaes/noimg.jpg" alt="게시글 썸네일" class="board-thumb">
               </c:otherwise>
            </c:choose>
               <div class="board-info">
                  <p class="board-title"><a href="${pageContext.request.contextPath}/blog/view?bno=${b.bno}">${b.title}</a></p>
                  <p class="board-meta">작성자 ${b.writerNickname} | ${b.reg_date}</p>
                  <p class="board-stat">조회수 ${b.viewCnt} | 좋아요 ${b.likeCnt}</p>
               </div>
         </li>
      </c:forEach>

		</ul>
	<div class="pageination">
		<c:choose>
			<c:when test="${starPage > 1}">
				<c:url var="preUrl" value="/blog/list">
				<c:param name="page" value="${startPage -1}"/>
				<c:param name="searchType" value="${serchType}"/>
				<c:param name="keyword" value="${keyword}"/>
			 </c:url>
			 	<a href="${preUrl}" class="page-nav">이전</a>
			</c:when>		
			<c:otherwise>
				<span class="page-nav disabled">이전</span>
			</c:otherwise>
		</c:choose>
		<!-- 페이지 번호 -->
      <c:forEach var="p" begin="${startPage}" end="${endPage}">
         <c:url var="pageUrl" value="/blog/list">
            <c:param name="page" value="${p}"/>
            <c:param name="searchType" value="${searchType}"/>
            <c:param name="keyword" value="${keyword}"/>
         </c:url>
         <a href="${pageUrl}" class="page-num ${p==currentPage?'active':''}">${p}</a>
      </c:forEach>
      <!-- 다음버튼 -->
      <c:choose>
         <c:when test="${endPage < totalPage}">
            <c:url var="nextUrl" value="/blog/list">
               <c:param name="page" value="${endPage +1}"/>
               <c:param name="searchType" value="${searchType}"/>
               <c:param name="keyword" value="${keyword}"/>
            </c:url>
            <a href="${nextUrl}" class="page-nav">다음</a>
         </c:when>
         <c:otherwise>
            <span class="page-nav disabled">다음</span>
         </c:otherwise>
      </c:choose>
		</div>
 	</main>
 	<%@ include file="/include/footer.jsp" %>
 	
 	</body>
 	
 </html>
