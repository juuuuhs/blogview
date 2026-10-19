<%@ page language="java" contentType="text/html; charset=UTF-8"
	pageEncoding="UTF-8"%>
<html>
<body>
	<!-- header.jsp -->
	<%@ include file="/include/header.jsp"%>



	<!-- 컨텐츠 영역: 최신글 인기글 들어갈 자리 -->
	<main class="view-wrap">
		<div class="view-header">
			<!-- 여백 조정 등을 위해 모두 그룹으로 나눔. 나누지 않아도 상관은 없음 -->
			<h2 class="view-title">${view.title}</h2>
			<p class="view-meta">${view.writerNickname}|${view.reg_date} |
				${view.viewCnt}</p>
		</div>
		<div class="view-img">
			<img src="/Ss/${view.imgPath}" alt="" class="view-thumb">
		</div>
		<div class="view-content">${view.content}</div>

		<div class="view-actions">
			<button type="button" id="likeBtn" class="btn btn-outline like-btn ${liked ? 'liked' : '' }">
				♥ 좋아요 <span id="likeCount">${view.likeCnt}</span>
			</button>
			<div class="view-actions-right">
				<a
					href="${pageContext.request.contextPath}/blog/update?bno=${view.bno}"
					class="btn btn-outline">수정</a> <a
					href="${pageContext.request.contextPath}/blog/delete?bno=${view.bno}"
					class="btn btn-outline" onclick="return confirm('삭제 하겠습니까?')">삭제</a>
				<a href="${pageContext.request.contextPath}/blog/list"
					class="btn btn-primary">목록</a>
			</div>
		</div>
		<!-- 댓글 영역 -->
		<div class="reply-section">
			<h3 class="reply-title">
				댓글 <span id="replyCount">${fn:length(replylist)}</span>
			</h3>
			<ul class="reply-list" id="replylist">
				<c:forEach var="r" items="${replylist}">
					<li class="reply-item" data-rno="${r.rno}">
						<div class="replt-info">
							<span class="reply-writer">${r.nickname}</span> <span
								class="reply-date">${r.reg_date}</span>
						</div>
						<p class="reply-content">${r.content}</p>
						<div class="reply-btns">
							<button type="button" class="reply-edit-btn">수정</button>
							<button type="button" class="reply-delete-btn">삭제</button>
						</div>
					</li>
				</c:forEach>
			</ul>


			<div class="reply-form">
				<input type="text" id="replyContent" placeholder="댓글을 입력하세요"
					class="reply-input">
				<!-- placeholder:입력창에 설명글 삽입 -->
				<button type="button" id="replySubmitBtn" class="btn btn-primary">등록</button>
			</div>
		</div>
	<div class="modal fade" id="replyModal" tabindex="-1" aria-labelledby="exampleModalLabel" aria-hidden="true">
  <div class="modal-dialog">
    <div class="modal-content">
      <div class="modal-header">
        <h1 class="modal-title fs-5" id="exampleModalLabel">댓글 수정 </h1>
        <button type="button" class="btn-close" data-bs-dismiss="modal" aria-label="Close"></button>
      </div>
      <div class="modal-body">
        <input type = "hidden" id="modalRno">
        <textarea id = "modalContent" class ="from-control" row="4"></textarea>
      </div>
      <div class="modal-footer">
        <button type="button" class="btn btn-secondary" data-bs-dismiss="modal">Close</button>
        <button type="button" class="btn btn-primary" id="modalSavebtn">Save changes</button>
      </div>
    </div>
  </div>
</div>
	</main>
	<script src="https://ajax.googleapis.com/ajax/libs/jquery/3.7.1/jquery.min.js"></script>
   <script src="https://cdn.jsdelivr.net/npm/bootstrap@5.3.8/dist/js/bootstrap.bundle.min.js"></script>
   
	<script>
	//좋아요 버튼 처리 기능 
	$("#likeBtn").on("click",function(){
		const bno = ${view.bno}
		$.ajax({
			url: "${pageContext.request.contextPath}/blog/like",
			type:'post',
			data:{bno:bno},
			dayaType:'json',
			success:function(result){
				if(!result.success){
					alert(result.message);
					return;
				}
			$("#likeCount").text(result.likeCount);
			$("#likeBtn").toggleclass("liked",result.liked);
			},error:function(){
				alert("접속오류");
			}
		})
	})
	//--댓글 등록---
	$("#replySubmitBtn").on("click",function(){
		const content= $("#replyContent").val().trim();
		if(content ==''){
			alert("댓글 내용을 입력하시오");
			return;
		}
		$.ajax({
			url: "${pageContext.request.contextPath}/reply/write",
			type: "post",
			data: {
			    bno: ${view.bno},
			    content: content
			},
			dataType: "json",
			success:function(result){
				if(!result.success){
						alert(result.message || '댓글 등록에 실패하였습니다')
						return
				}
		//'혼따옴표'가아니라 '백틱'이다 ,jsp와 자바스크립트 언어를 동시에 쓸 수 있는 
		const newItem=`
			<li class="reply-item" data-rno="\${result.rno}">
				<div class="reply-info">
				<span class="reply-write">\${result.writerNickName}안건호</span>
				<span class="reply-date">방금전</span>
			</div>
			<p class="reply-content">\${result.content}</p>
			<div class="reply-btns">
				<button type="button" class="reply-edit-btn">수정</button>
				<button type="button" class="reply-delete-btn">삭제</button>
			</div>
		</li>
			`;
		$("#replylist").append(newItem);
		$("#replyContent").val('');
			},error:function(){
				alert("댓글 등록중 오류가 발생");
			}
		});
	});
	
	//---댓글 삭제 방법--
	$("#replylist").on("click",'.reply-delete-btn',function(){
		if(confirm('댓글을 삭제하시겠어요??')){ //alert는 그냥 경고창이고 confim은 확인 버튼이 달린 거 
			$(this).closest('.reply-item').remove();
			//실제 연동시 rno값을 data-rno에서 꺼내서 비동기식으로 처리
			const $item = $(this).closest('reply-item');
			const rno = $ithem.data('rno'); //li 태그의 data-rno 속성 값
			
			$.ajax({
				url:"${pageContext.request.contextPath}/reply/delete",
				type: 'post',
				data:{rno : rno},
				dataType:json, //서버에서 보내는 데이터는 반드시 json으로 보내줘 
				success:function(result){
					if(result.success){
						$item.remove(); //서버에서 실제로 지워졌으면 화면에서 제거 
					}else{
						alert(result.message);
					}
				},error:function(){
					alert("통신오류!");
				}
			})
			
		}
	})
	
	//---댓글 수정방법---
	const replyModal = new bootstrap.Modal(document.getElementById('replyModal'));
   document.getElementById('replyModal').addEventListener('show.bs.modal',function(){
      $('#modalContent').focus();
   });
   
   $("#replylist").on("click", '.reply-edit-btn', function() {
      const $item=$(this).closest('.reply-item');
      $('#modalRno').val($item.data('rno'));
      $('#modalContent').val($item.find('.reply-content').text());
      replyModal.show();
   });

$('#modalSaveBTn').on('click',function(){
	const rno = $('#modlaRno').val();
	const newContent = $('#madalContent').val().trim();
	if(newContent ==''){
		alert('댓글 내용을 입력하세요');
		return;
	}
$.ajax({
	url:"${pageContext.request.contextPath}/reply/modify",
	type:'post',
	data:{rno:rno,content:newContent},
	dataType:'json',
	success:function(result){
		if(result.success){
			$('#replylist .reply-item[data-rno="'+rno+'"]')
			.find(".reply-content").text(result.content);
			replyModal.hide();
		}else{
			alert(result.message);
		}
 	 },error:function(){
 		 alert("접속오류");
 	 }
  })
})
	//자바스크립트
	 /*	document.getElementById("likeBtn").addEventListener=(.으로 표시)("click",function(){
		//1.likeBtn id 버튼을 눌러라 (let) 2.자기자신에게 저장하라 (this)
		let likeBtn=this;
		//좋아요 버튼
		likeBtn.classList.toggle("liked");
		//3.liked 클래스가 있으면 제거하고,없으면 추가된다(toggle)
		let count = parseInt(document.getElementById("likeCount").textContent);
		// =likeCount의 요소를 가져와라 pasreInt = 숫자를 문자로 바꿔서 가져오는 것
		//text-content:likecount에 있는 3를 가져오는 것
		if(likeBtn.classList.contains("liked")){
			count = count + 1;
			document.getElementById("likeCount").textContent = count;
		}else{
			document.getElementById("likeCount").textContent = count-1;
		}
		}) */
		
		//'혼따옴표'가 아니라 `백틱이다`
	
	
		/*실제 연습 시 비동기식으로 데이터 처리 할 거다
		$.jax({
			
		})
	*/
		
	</script>


	<!-- footer.jsp -->
	<%@ include file="/include/footer.jsp"%>