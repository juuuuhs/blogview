<%@ page language="java" contentType="text/html; charset=UTF-8"
    pageEncoding="UTF-8"%>
<%@ include file="/include/header.jsp"%>
<main class="write-wrap">
	
	<h2 class="write-page-title">글 수정</h2>
	<form name="my" id="modifyform" class="write-form" 
		  method="post"
		  enctype="multipart/form-data"
		  action="${pageContext.request.contextPath}/blog/update">
		  <input type = "hidden" name="bno" value="${board.bno}" >
		  
	<div class="form-row">
		<label>작성자</label>
		<p class="write-author">"${board.writerNickname}"</p>
	</div>
	<div>
		<label for="title">제목</label>
		<input type="text" id="title" name="title" 
			placeholder="제목을 입력하세요" class="form-input" value="${board.title}">
	</div>
	<div class="form-row">
		<label> 대표 이미지 <span class="form-optional">(선택)</span></label>
		<div class="image-upload-box">
			<img id="imgPreview" src="${not empty board.imgPath ?'/Ss/'.concat(board.imgPath) :
			 pageContext.request.contextPath.concat('/imgaes/noimg.jpg')}"
			alt="이미지 미리보기" class="img-preview">
	<div class="image-upload-btns">
		<label for="imgfile" class="btn btn-outline file-label">이미지 선택</label>
		<input type="file"
			   id="imgfile" name="imgfile" accept="image/*" class="file-input">
		<button type="button" id="imgremoveBtn"
				class="btn btn-outline">이미지 제거</button>
	</div>
	</div>
	</div>
	
	<div class="form-row">
		<label for="content">내용</label>
		<textarea rows="14"
		name="content" placeholder="내용을 입력하세요" 
		class="form-textarea">"${board.content}"</textarea>	  
	</div>
	
	<div class="write-btns">
		<a href="${pageContext.request.contextPath}/blog/view?bno=${board.bno}"
			class="btn btn-outline">취소</a>
		<button type="submit" class="btn btn-primary">수정완료</button>
	</div>
	</form>
	
</main>
	 <script src="https://ajax.googleapis.com/ajax/libs/jquery/3.7.1/jquery.min.js"></script>
	 <script src="${pageContext.request.contextPath}/js/wirte.js"></script>












<%@ include file="/include/footer.jsp"%>