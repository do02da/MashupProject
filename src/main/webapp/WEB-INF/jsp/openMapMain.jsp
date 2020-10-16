<%@ page language="java" contentType="text/html; charset=UTF-8"
    pageEncoding="UTF-8"%>
<!DOCTYPE html>
<html>
<head>
<meta charset="UTF-8">
<meta name="viewport" content="width=device-width, initial-scale=1">
<title>OpenMap + OpenData MashUp Project</title>
<%@ include file="/WEB-INF/include/include-header.jspf" %>
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
		
	</script>

	<script type="text/javascript">
		var isAddMarker = [];	// 마커를 추가했는지 여부	false : 미추가, true : 추가
		var markers_list = [];	// 각 데이터의 마커 목록이 들어갈 배열
		var markers = [];		// 각 데이터의 마커가 들어갈 목록
		
		// html onload되면 서버에 데이터리스트 이름을 가져와서 체크박스로 뿌려줌
		$(document).ready(function(){
			fn_getDataList();
		});
		
		function fn_isImage(DataListName) {
			$.ajax({
				type: 'POST',
				data: {DataListName:DataListName},
				url: "<c:url value='/map/isImage.do'/>",
				success: function(data){
					alert("success : " + data + " data length : " + data.length );
				},
				error: function(error){
					alert("Error : " + error);
				}
			});
		}
	
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
				isAddMarker.push(false);
				fn_setChkBoxEvent(i);
			}
		}
		
		var html = "";
		var DataListHtml = [];
		function fn_setChkBoxEvent(ChkBoxId) {
			// checkBox 클릭 이벤트
			$("#checkID_" + ChkBoxId).change(function(e) {
				// 체크했을 때
				if($("#checkID_" + ChkBoxId).prop('checked') == true) {
					var checkName = $("#checkName_"+ChkBoxId).val();

					$.ajax({
						type: 'POST',
						data:{name:checkName},
						url: "<c:url value='/map/getData.do'/>",
						success: function(data){
							if(data.length > 0){
								if (isAddMarker[ChkBoxId] == false) {	// if : 마커가 생선된 적 없으면
									html += "<div id='ListDiv_" + ChkBoxId + "'>";
					                for(i=0; i<data.length; i++){
					                		fn_addMarker(data[i], checkName);		// 마커 생성
					                		fn_addList(data[i], ChkBoxId, i);
					               	}
					                html += "</div>";
					                markers_list[ChkBoxId] = markers;	// 마커 목록
					                
					                DataListHtml[ChkBoxId] = html;
					                
					                html = "";
					                markers = [];
					                isAddMarker[ChkBoxId] = true;
								} else {								// else : 마커가 생선된 적 있으면
									for(i=0; i<markers_list[ChkBoxId].length; i++) {
										markers_list[ChkBoxId][i].setMap(map);
									}
								}
				                
								$("#data_list").append(DataListHtml[ChkBoxId]);
								
				                for(i=0; i<data.length; i++) {
				        			$("#listID_" + ChkBoxId + "_" + i).on("click", function(e) {
				        				fn_moveToMarker($(this));
				        			});	
				                }
							}
						},
						// 로딩 - 출처: https://skylhs3.tistory.com/4 [다루이의 생활일기]
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
				} else {	// check if end, Uncheck start
					fn_removeMarker(ChkBoxId);
					$("#ListDiv_"+ChkBoxId).remove();

				}
			}); // $("#check_1").change end
		}

		// 지도에 올려진 마커 삭제
		function fn_removeMarker(ChkBoxId) {
			for (i=0; i<markers_list[ChkBoxId].length; i++) {
				markers_list[ChkBoxId][i].setMap(null);
			}
		}
		
		// https://devtalk.kakao.com/t/topic/61302/5 중복 마커 처리 참조!!
		// 마커 생성하고 지도 위에 표시
		function fn_addMarker(data, DataListName){

			var marker = new kakao.maps.Marker({
                map: map, // 마커를 표시할 지도
                position: new kakao.maps.LatLng(data.위도, data.경도), // 마커를 표시할 위치
                title : data.설치장소, // 마커의 타이틀, 마커에 마우스를 올리면 타이틀이 표시됩니다
            });
	
			// 마커이미지 리소스가 있으면 추가한다.
			var image = new Image();
			image.src = "<c:url value='/MarkerImg/" + DataListName + ".png'/>"
			
			image.onload = function() {
				fn_setMarkerImage(marker, DataListName);
			}
			
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
		
		// 마커 이미지 등록
		function fn_setMarkerImage(marker, DataListName) {
			var imageSrc = "<c:url value='/MarkerImg/" + DataListName + ".png'/>", // 마커이미지의 주소입니다
		    imageSize = new kakao.maps.Size(30, 33) // 마커이미지의 크기입니다
		      
			var markerImage = new kakao.maps.MarkerImage(imageSrc, imageSize);
		    
			marker.setImage(markerImage);
		}
		
		// 목록 생성
		function fn_addList(data, ChkBoxId, i) {
			html += "<a href='#this' class='list-group-item' id='listID_" + ChkBoxId + "_" + i + "'>";
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