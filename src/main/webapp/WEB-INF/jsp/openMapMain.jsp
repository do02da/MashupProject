<%@ page language="java" contentType="text/html; charset=UTF-8"
    pageEncoding="UTF-8"%>
<!DOCTYPE html>
<html>
<head>
<meta charset="UTF-8">
<meta name="viewport" content="width=device-width, initial-scale=1">
<title>OpenMap + OpenData MashUp Project</title>
<%@ include file="/WEB-INF/include/include-header.jspf" %>
<style>
section {
	height:85vh;
}
header{
	height:10vh;
}
#map{
	height:85vh;
}

#data_list {
	height:85vh;
    overflow-y: auto;
}

.wrap-loading { /*화면 전체를 어둡게 합니다.*/
	z-index:3;
    position: fixed;
    left:0;
    right:0;
    top:0;
    bottom:0;
    background: rgba(0,0,0,0.2); /*not in ie */
    filter: progid:DXImageTransform.Microsoft.Gradient(startColorstr='#20000000', endColorstr='#20000000');    /* ie */
}

.wrap-loading div { /*로딩 이미지*/
    position: fixed;
    top:50%;
    left:50%;
    margin-left: -21px;
    margin-top: -21px;
}

.display-none { /*감추기*/
    display:none;
}
</style>
</head>
<body>


	<!-- header start -->
	<header class="navbar navbar-default">
		<div class="container-fluid">
			<div class="col-xs-4"><img class="img-responsive" src="<c:url value='/pic/logo.png'/>" alt="로고"/></div>
			<div class="col-xs-8">
				<!-- search start-->
				<form id="searchForm" name="searchForm">
					<div class="text-center input-group" id="searchDiv">
						<input type="text" class="form-control" id="searchWord" name="searchWord">
						
						<span class="input-group-btn">
							<button class="btn btn-default" type="button" id="search">검색</button>
						</span>
					</div>
				</form>
				<!-- section end -->
			</div>
		</div>
	</header>

	<!-- contents -->
	<section class="container-fluid">
		<div class="col-xs-12 col-sm-8 col-sm-push-4" id="map"></div>
		<div class="col-xs-6 col-sm-2 col-sm-pull-8" id="LayerField"></div>
		<div class="col-xs-6 col-sm-2 col-sm-pull-8">
			<div class="list-group" id="data_list">
			</div>
		</div>
	</section>
	
	<!-- loading div -->
	<div class="wrap-loading display-none">
	    <div><img src="<c:url value='/pic/loadingImg.gif'/>" alt="로딩" /></div>
	</div>
	
	<!-- map -->
	<script type="text/javascript" src="//dapi.kakao.com/v2/maps/sdk.js?appkey=91b19305259c40284745eee37f88b8eb"></script>
	<script>
		var container = document.getElementById('map');				// 지도 표시 div
		var options = {
			center: new kakao.maps.LatLng(36.357994, 127.361817),	// 지도 중심좌표
			level: 3												// 지도 확대 레벨
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
		
		var markers = [];
	</script>

	<script type="text/javascript">
		// html onload
		$(document).ready(function(){
			fn_getDataList();
		});
	
		function fn_getDataList() {
			$.ajax({
				type: 'POST',
				url: "<c:url value='/map/getDataList.do'/>",
				success: function(data){
					fn_setDataListToLayer(data);
				},
				error: function(error){
					alert("Error : " + error);
				}
			});
		}
		
		function fn_setDataListToLayer(data) {
			var chkBoxHtml = "";
			
			for (i=0; i<data.length; i++) {
				chkBoxHtml += "<label>";
				chkBoxHtml += "<input type='hidden' id='checkName_" + i + "' value='" + data[i] + "'>";
				chkBoxHtml += "<input type='checkbox' id='checkID_" + i + "'>" + data[i];
				chkBoxHtml += "</label>";
			}
			
			$("#LayerField").html(chkBoxHtml);
			
			for (i=0; i<data.length; i++) {
				fn_setChkBoxEvent(i);
			}
		}
		
		var html = "";
		
		function fn_setChkBoxEvent(ChkBoxId) {
			// checkBox 클릭 이벤트
			$("#checkID_" + ChkBoxId).change(function(e) {
				
				fn_removeMarker();
				html = "";
				$("#data_list").html("");
				
				// 체크했을 때
				if($("#checkID_" + ChkBoxId).prop('checked') == true) {
					var checkName = $("#checkName_"+ChkBoxId).val();

					$.ajax({
						type: 'POST',
						data:{name:checkName},
						url: "<c:url value='/map/getData.do'/>",
						success: function(data){
							if(data.length > 0){
				                for(i=0; i<data.length; i++){
				                		fn_addMarker(data[i]);
				                		fn_addList(data[i], i);
				               	 }
				                
				                $("#data_list").html(html);
				                
				                for(i=0; i<data.length; i++) {
				        			$("#listID_" + i).on("click", function(e) {
				        				fn_moveToMarker($(this));
				        			});	
				                }
							}
						},
						// 로딩 출처: https://skylhs3.tistory.com/4 [다루이의 생활일기]
						beforeSend:function(){
					        // 이미지 보여주기 처리
					        $('.wrap-loading').removeClass('display-none');
					    },
					    complete:function(){
					        // 이미지 감추기 처리
					        $('.wrap-loading').addClass('display-none');
					    },
						error:function(request,status,error){
						    alert("code:"+request.status+"\n"+"message:"+request.responseText+"\n"+"error:"+error);
						}
					}) // ajax end
				} // if end
			}); // $("#check_1").change end
		}

		// 지도에 올려진 마커 삭제
		function fn_removeMarker() {
			for (i=0; i<markers.length; i++) {
				markers[i].setMap(null);
			}
			markers = [];
		}
		
		// https://devtalk.kakao.com/t/topic/61302/5 중복 마커 처리 참조!!
		// 마커 생성하고 지도 위에 표시
		function fn_addMarker(data){
			var marker = new kakao.maps.Marker({
                map: map, // 마커를 표시할 지도
                position: new kakao.maps.LatLng(data.위도, data.경도), // 마커를 표시할 위치
                title : data.설치장소 // 마커의 타이틀, 마커에 마우스를 올리면 타이틀이 표시됩니다
            });
			
			marker.setMap(map);
			markers.push(marker) // 배열에 생선된 마커를 추가합니다
			
			// 마커에 커서가 오버됐을 때 마커 위에 표시할 인포윈도우를 생성합니다
			// 인포윈도우에 표출될 내용으로 HTML 문자열이나 document element가 가능합니다
			var iwContent = "";
			iwContent += "<div class='list-group-item'>";
			iwContent += fn_addDataDetail(data);
			iwContent += "</div>";

			// 인포윈도우를 생성합니다
			var infowindow = new kakao.maps.InfoWindow({
			    content : iwContent
			});
			
			// 마커에 마우스오버 이벤트를 등록합니다
			kakao.maps.event.addListener(marker, 'mouseover', function() {
				// 마커에 마우스오버 이벤트가 발생하면 인포윈도우를 마커위에 표시합니다
				infowindow.open(map, marker);
			});

			// 마커에 마우스아웃 이벤트를 등록합니다
			kakao.maps.event.addListener(marker, 'mouseout', function() {
				// 마커에 마우스아웃 이벤트가 발생하면 인포윈도우를 제거합니다
				infowindow.close();
			});
		}
		
		// 목록 생성
		function fn_addList(data, i) {
			html += "<a href='#this' class='list-group-item' id='listID_" + i + "'>";
			html += fn_addDataDetail(data);
			html += "</a>";
		}
	
		// 인포윈도우와 리스트에 들어갈 정보 생성
		function fn_addDataDetail(data) {
			var tmpHtml = "";
			
			// 리스트 아이팀 헤더 부분
			if (data.설치장소  !== undefined) {				
				tmpHtml += "<h4 class='list-group-item-heading'>" + data.설치장소 + "</h4>";
			} else if (data.대상시설명 !== undefined) {		// 대상시설명이 있으면 (ex. 어린이보호구역표준데이터)
				tmpHtml += "<h4 class='list-group-item-heading'>" + data.대상시설명 + "</h4>";
			} else if (data.촬영방면정보 !== undefined) {	// 촬영방면정보가 있으면 (ex. 전국CCTV표준데이터)
				tmpHtml += "<h4 class='list-group-item-heading'>" + data.촬영방면정보 + "</h4>";
			}
			
			if (data.설치위치  !== undefined) {
				tmpHtml += "<p class='list-group-item-text'>설치위치 : " + data.설치위치 + "</p>";
			}
			
			if (data.소재지도로명주소 !== undefined) {		// 소재지도로명주소가 있으면
				tmpHtml += "<p class='list-group-item-text'>소재지도로명주소 : " + data.소재지도로명주소 + "</p>";
			}
			
			if (data.소재지지번주소 !== undefined) {		// 소재지도로명주소가 있으면
				tmpHtml += "<p class='list-group-item-text'>지번 : " + data.소재지지번주소 + "</p>";
			}
			tmpHtml += "<input type='hidden' id='latitude' name='latitude' value='" + data.위도 + "'>"
			tmpHtml += "<input type='hidden' id='longitude' name='longitude' value='" + data.경도 + "'>"
			
			return tmpHtml;
		}
		
		// 선택한 목록 아이템을 지도의 중심으로 보여줍니다.
		function fn_moveToMarker(obj){
			var latitude = obj.find("#latitude").val();
			var longitude = obj.find("#longitude").val();
			
			// 이동할 위도 경도 위치를 생성합니다 
		    var moveLatLon = new kakao.maps.LatLng(latitude, longitude);
		    
		    // 지도 중심을 부드럽게 이동시킵니다
		    // 만약 이동할 거리가 지도 화면보다 크면 부드러운 효과 없이 이동합니다
		    map.panTo(moveLatLon);
		}
	</script>
</body>
</html>