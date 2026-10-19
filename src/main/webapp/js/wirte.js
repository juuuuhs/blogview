$(function(){ //jquery 시작
	//이미지 선택시 미리보기 기능 만들어보기
	const defaultImg = $("#imgPreview").attr('src');
	$("#imgfile").on("change",function(){
		const file = this.files[0];
		if(!file) return;
		//브라우저 메모리 안에서 임시 url을 만들어 미리보기에 반영
		const previewUrl = URL.createObjectURL(file);
		$("#imgPreview").attr("src",previewUrl);
		$("#removeImg").val("false");  // 제거 눌렀다가 다시 새 파일 선택하면 제거 요청 취소
	})
	//이미지 제거
	$("#imgremoveBtn").on("click", function(){
		$("#imgfile").val('');
		$("#imgPreview").attr("src",defaultImg)
		$("#removeImg").val('true'); //moddify.jsp에만 있는 hidden,weiter.jsp엔 없다
	})
	$("#writeform").on('submit',function(e){
		const title = $("#title").val().trim();
		const content = $("#content").val().trim();
		if(title===''){
			alert("제목을 입력해주세요!");
			e.preventDefault();
			$("#title").focus();
			return;
	}
	if(content==''){
		alert("내용을 입력해주세요!");
		e.preventDefault();
		$("#content").focus();
		return;
	}
})
})