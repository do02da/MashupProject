<%@ page language="java" contentType="text/html; charset=UTF-8"
    pageEncoding="UTF-8"%>
<!DOCTYPE html>
<html>
<head>
<meta charset="UTF-8">
<title>OpenMap + OpenData MashUp Project</title>
<%@ include file="/WEB-INF/include/include-header.jspf" %>
<style>
html, body{
	height:100%;
}

</style>
</head>
<body>

	<!-- 헤더 시작 -->
	<header style="height:10%">
		<div class="col-xs-4">로고영역</div>
		<div class="col-xs-8">
			<!-- 검색 -->
			<form id="searchForm" name="searchForm">
				<div class="text-center input-group" id="searchDiv">
					<input type="text" class="form-control" id="searchWord" name="searchWord">
					
					<span class="input-group-btn">
						<button class="btn btn-default" type="button" id="search">검색</button>
					</span>
				</div>
			</form>
			<!-- 검색 끝 -->
			<a href="#this" id="TEST">TEST</a>
		</div>
	</header>

	<!-- 컨텐츠 부분 -->
	<section style="height:90%">
		<div style="height:100%" class="col-xs-2">레이어영역</div>
		<div style="height:100%" class="col-xs-2"></div>
		<div style="height:100%" class="col-xs-8" id="map"></div>
	</section>
	
	<!-- 지도 부분 -->
	<!-- https://apis.map.kakao.com/web/sample/categoryMarker/ 참조-->
	<script type="text/javascript" src="//dapi.kakao.com/v2/maps/sdk.js?appkey=91b19305259c40284745eee37f88b8eb"></script>
	<script>
		var container = document.getElementById('map');
		var options = {
			center: new kakao.maps.LatLng(33.450701, 126.570667),
			level: 3
		};

		var map = new kakao.maps.Map(container, options);
		
		// 일반 지도와 스카이뷰로 지도 타입을 전환할 수 있는 지도타입 컨트롤을 생성합니다
		var mapTypeControl = new kakao.maps.MapTypeControl();

		// 지도에 컨트롤을 추가해야 지도위에 표시됩니다
		// kakao.maps.ControlPosition은 컨트롤이 표시될 위치를 정의하는데 TOPRIGHT는 오른쪽 위를 의미합니다
		map.addControl(mapTypeControl, kakao.maps.ControlPosition.TOPRIGHT);

		// 지도 확대 축소를 제어할 수 있는  줌 컨트롤을 생성합니다
		var zoomControl = new kakao.maps.ZoomControl();
		map.addControl(zoomControl, kakao.maps.ControlPosition.RIGHT);
	</script>

	<script type="text/javascript">
		$("a[id='TEST']").on("click", function(e) {
			$.ajax({
				type: 'POST',
				url: "<c:url value='/test/test.do'/>",
				success: function(){
					alert("TEST");
				},
				error: function(error) {
					alert("실패");
				}
			})
		});
	</script>
</body>
</html>