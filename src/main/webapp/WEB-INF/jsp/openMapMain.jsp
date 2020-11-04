<%@ page language="java" contentType="text/html; charset=UTF-8"
    pageEncoding="UTF-8"%>
<!DOCTYPE html>
<html lang="ko">
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
			<div class="col-xs-4">
				<a href="<c:url value='/map/openMapMain.do'/>"><img class="img-responsive" src="<c:url value='/pic/logo.png'/>" alt="로고"/></a>
			</div>
			<div class="col-xs-8">
				<!-- search start-->
				<form id="searchForm" name="searchForm">
					<div class="text-center input-group" id="searchDiv">
						<input type="text" class="form-control" id="search_keyWord" name="search_keyWord">
						
						<span class="input-group-btn">
							<button class="btn btn-default" type="button" id="search_btn">검색</button>
						</span>
					</div>
				</form>
				<!-- search end -->
			</div>	
		</div>
	</header>

	<!-- contents -->
	<section class="container-fluid" role="main">
		<div class="col-xs-12 col-sm-8 col-sm-push-4" id="map"></div>
		<div class="col-xs-6 col-sm-2 col-sm-pull-8" id="LayerField">
			<select name="SiDo" id="SiDo"></select>
			<select name="SiGuGun" id="SiGuGun"></select>
		</div>
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
			level: 3,												// 지도 확대 레벨
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
		
		// copyright의 위치를 오른쪽 아래로 설정하고, 로고와의 위치를 반전시킨다
		map.setCopyrightPosition(kakao.maps.CopyrightPosition.BOTTOMRIGHT, true);
		
	</script>

	<script type="text/javascript">
		var markers_list = [];	// 각 데이터의 마커 목록이 들어갈 배열
		var markers = [];		// 각 데이터의 마커가 들어갈 목록
		var DataListLength;		// 데이터 리스트 개수
		
		$(document).ready(function(){
			// 셀렉트 생성
			fn_setSelect();
			
			// html onload되면 서버에 데이터리스트 이름을 가져와서 체크박스로 뿌려줌
			fn_getDataList();
			
			$('#search_btn').on('click', function(e) {
				e.preventDefault();
				fn_search();
			});
			
			
			$("#search_keyWord").keypress(function (e) {
		        if (e.which === 13) { // Enter키 눌렸을 때
		        	e.preventDefault();
		        	fn_search();
		        }
		    });
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
			DataListLength = data.length;
			
			for (i=0; i<data.length; i++) {
				chkBoxHtml += "<label>";
				chkBoxHtml += "<input type='checkbox' id='checkID_" + i + "' name='DataListName' value='" + data[i] + "'>" + data[i];
				chkBoxHtml += "</label>";
			}
			
			$("#LayerField").append(chkBoxHtml);
			
			for (i=0; i<data.length; i++) {
				fn_setChkBoxEvent(i);
			}
		}
		
		// 출처 : https://codepen.io/eond/pen/oMBRpG
		function fn_setSelect() {
			var area0 = ["시/도 선택","서울특별시","인천광역시","대전광역시","광주광역시","대구광역시","울산광역시","부산광역시","경기도","강원도","충청북도","충청남도","전라북도","전라남도","경상북도","경상남도","제주도"];
			var area1 = ["강남구","강동구","강북구","강서구","관악구","광진구","구로구","금천구","노원구","도봉구","동대문구","동작구","마포구","서대문구","서초구","성동구","성북구","송파구","양천구","영등포구","용산구","은평구","종로구","중구","중랑구"];
			var area2 = ["계양구","남구","남동구","동구","부평구","서구","연수구","중구","강화군","옹진군"];
			var area3 = ["대덕구","동구","서구","유성구","중구"];
			var area4 = ["광산구","남구","동구",     "북구","서구"];
			var area5 = ["남구","달서구","동구","북구","서구","수성구","중구","달성군"];
			var area6 = ["남구","동구","북구","중구","울주군"];
			var area7 = ["강서구","금정구","남구","동구","동래구","부산진구","북구","사상구","사하구","서구","수영구","연제구","영도구","중구","해운대구","기장군"];
			var area8 = ["고양시","과천시","광명시","광주시","구리시","군포시","김포시","남양주시","동두천시","부천시","성남시","수원시","시흥시","안산시","안성시","안양시","양주시","오산시","용인시","의왕시","의정부시","이천시","파주시","평택시","포천시","하남시","화성시","가평군","양평군","여주군","연천군"];
			var area9 = ["강릉시","동해시","삼척시","속초시","원주시","춘천시","태백시","고성군","양구군","양양군","영월군","인제군","정선군","철원군","평창군","홍천군","화천군","횡성군"];
			var area10 = ["제천시","청주시","충주시","괴산군","단양군","보은군","영동군","옥천군","음성군","증평군","진천군","청원군"];
			var area11 = ["계룡시","공주시","논산시","보령시","서산시","아산시","천안시","금산군","당진군","부여군","서천군","연기군","예산군","청양군","태안군","홍성군"];
			var area12 = ["군산시","김제시","남원시","익산시","전주시","정읍시","고창군","무주군","부안군","순창군","완주군","임실군","장수군","진안군"];
			var area13 = ["광양시","나주시","목포시","순천시","여수시","강진군","고흥군","곡성군","구례군","담양군","무안군","보성군","신안군","영광군","영암군","완도군","장성군","장흥군","진도군","함평군","해남군","화순군"];
			var area14 = ["경산시","경주시","구미시","김천시","문경시","상주시","안동시","영주시","영천시","포항시","고령군","군위군","봉화군","성주군","영덕군","영양군","예천군","울릉군","울진군","의성군","청도군","청송군","칠곡군"];
			var area15 = ["거제시","김해시","마산시","밀양시","사천시","양산시","진주시","진해시","창원시","통영시","거창군","고성군","남해군","산청군","의령군","창녕군","하동군","함안군","함양군","합천군"];
			var area16 = ["서귀포시","제주시","남제주군","북제주군"];
			
			// 시/도 선택 박스 초기화
			$("select[name^=SiDo]").each(function() {
				$selsido = $(this);
				$.each(eval(area0), function() {
					$selsido.append("<option value='" + this + "'>" + this + "</option>");
				});
				$selsido.next().append("<option value='isNotSelect'>시/구/군 선택</option>");
			});
			
			// 시/도 선택시 시/구/군 설정
			$("select[name^=SiDo]").change(function() {
				var area = "area"+$("option", $(this)).index($("option:selected", $(this))); // 선택지역의 시구군 Array
				var $sigugun = $(this).next();  // 선택영역 시/구/군 객체
				$("option", $sigugun).remove(); // 시/구/군 초기화
				fn_listMarker_init();			// 리스트, 마커 초기화
				
				if(area == "area0")
					$sigugun.append("<option value='isNotSelect'>시/구/군 선택</option>");
				else {
					$.each(eval(area), function() {
						$sigugun.append("<option value='" + this + "'>" + this + "</option>");
					});
				}
			});
			
			$("select[name^=SiGuGun]").change(function() {
				fn_listMarker_init();
			});
		}
		
		var html = "";
		var DataListHtml = [];
		var bounds;
		
		function fn_setChkBoxEvent(ChkBoxId) {
			// checkBox 클릭 이벤트
			$("#checkID_" + ChkBoxId).change(function(e) {
				if($(this).prop('checked') == true) {
					var checkName = $(this).val();
					
					var SiDoName = $("select[name^=SiDo]").val(),
						SiGuGunName = $("select[name^=SiGuGun]").val();
					
					if (SiGuGunName == "isNotSelect") {
						alert("지역을 선택해주세요");
						
						$('input:checkbox[name="DataListName"]').each(function() {
				            this.checked = false; // 체크 해제 처리
				 		});
						
						return false;
					} else {
						$.ajax({
							type: 'POST',
							data:{
								name:checkName,
								SiDoName:SiDoName,
								SiGuGunName:SiGuGunName
								
							},
							url: "<c:url value='/data/getData.do'/>",
							success: function(data){
								if(data.length > 0){
									// 검색된 마커와 리스트 초기화
									for(i=0; i<SearchMarker.length; i++) {
										SearchMarker[i].setMap(null);
									}
									$("#ListDiv_Search").remove();
									//
									markers_list[ChkBoxId] = [];
									bounds = new kakao.maps.LatLngBounds();
									
									html += "<div id='ListDiv_" + ChkBoxId + "'>";
					                for(i=0; i<data.length; i++){
					                		fn_addMarker(data[i], checkName);		// 마커 생성
					                		fn_addList(data[i]);
					               	}
					                
					                html += "</div>";
					                
					            	// 검색된 장소 위치를 기준으로 지도 범위를 재설정합니다
					                map.setBounds(bounds);
					                
					                markers_list[ChkBoxId] = markers;	// 마커 목록
					                
					                DataListHtml[ChkBoxId] = html;
					                
					                html = "";
					                markers = [];
									
									$("#data_list").append(DataListHtml[ChkBoxId]);
									
									$(".ListData").on("click", function(e) {
										fn_moveToMarker($(this));
									});
								} else {	// Data.length가 0보다 작을 때
									alert("해당 지역에 데이터가 없습니다.");
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
							error:function(request, status, error){
							    alert("code:" + request.status+"\n" + "message:" + request.responseText + "\n" + "error:" + error);
							}
						}) // ajax end
					}
				} else {	// check if end, 체크해제일 경우
					fn_removeMarker(ChkBoxId);			// 마커들 제거
					$("#ListDiv_"+ChkBoxId).remove();	// 리스트 제거

				}
			}); // $("#checkID_").change end
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

			var position = new kakao.maps.LatLng(data.lat, data.lng),
				marker = new kakao.maps.Marker({
	                map: map, // 마커를 표시할 지도
	                position: position // 마커를 표시할 위치
            });
			
			// 검색된 장소 위치를 기준으로 지도 범위를 재설정하기위해
	        // LatLngBounds 객체에 좌표를 추가합니다
			bounds.extend(position);
	
			// 마커이미지 리소스가 있으면 추가한다.
			var image = new Image();
			image.src = "<c:url value='/MarkerImg/" + DataListName + ".png'/>"
			
			image.onload = function() {
				fn_setMarkerImage(marker, DataListName);
			}

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
			var imageSrc = "<c:url value='/MarkerImg/" + DataListName + ".png'/>", 	// 마커이미지의 주소입니다
		    imageSize = new kakao.maps.Size(30, 33)									// 마커이미지의 크기입니다
		      
			var markerImage = new kakao.maps.MarkerImage(imageSrc, imageSize);
		    
			marker.setImage(markerImage);
		}
		
		// 목록 생성
		function fn_addList(data) {
			html += "<a href='#this' class='list-group-item ListData'>";
			html += fn_addDataDetail(data);
			html += "</a>";
		}
	
		// 인포윈도우와 리스트에 들어갈 정보 생성
		function fn_addDataDetail(data) {
			var tmpHtml = "";
			
			// 리스트 아이템 헤더 부분
			tmpHtml += "<h4 class='list-group-item-heading'>";
			if (data.Place) {				
				tmpHtml += data.Place
			} else if (data.Facilities) {		// 대상시설명이 있으면 (ex. 어린이보호구역표준데이터)
				tmpHtml += data.Facilities
			} else if (data.Direction) {	// 촬영방면정보가 있으면 (ex. 전국CCTV표준데이터)
				tmpHtml += data.Direction
			}
			tmpHtml += "</h4>";
			
			if (data.Location) {
				tmpHtml += "<p class='list-group-item-text'>설치위치 : " + data.Location + "</p>";
			}
			
			if (data.RoadNameAddr) {		// 소재지도로명주소가 있으면
				tmpHtml += "<p class='list-group-item-text'>소재지도로명주소 : " + data.RoadNameAddr + "</p>";
			}
			
			if (data.LotNumAddr) {		// 소재지도로명주소가 있으면
				tmpHtml += "<p class='list-group-item-text'>지번 : " + data.LotNumAddr + "</p>";
			}
			
			tmpHtml += "<input type='hidden' id='latitude' name='latitude' value='" + data.lat + "'>"
			tmpHtml += "<input type='hidden' id='longitude' name='longitude' value='" + data.lng + "'>"

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
		
		var SearchMarker = [];
		
		// 검색
		function fn_search() {
			var search_keyword = $("#search_keyWord").val();
			
			// 검색어가 비어있지않으면
			if (search_keyword != "") {
				$.ajax({
					type: 'POST',
					data:{search_keyword:search_keyword},
					url: "<c:url value='/data/search.do'/>",
					success: function(DataList){
						if (DataList.length > 0) {
							// 초기화
							fn_listMarker_init()

							bounds = new kakao.maps.LatLngBounds();
							//
							
							html += "<div id='ListDiv_Search'>";
			                for(i=0; i < DataList.length; i++){
			                	for (j=0; j < DataList[i].data.length; j++) {
			                		if (DataList[i].data.length > 0) {
				                		fn_addMarker(DataList[i].data[j], DataList[i].DataListName);		// 마커 생성
				                		fn_addList(DataList[i].data[j]);
			                		}
			                	}
			               	}
			                html += "</div>";
			                
			                SearchMarker = markers;
			                var SearchDataListHtml = html;
			                
			                html = "";
			                markers = [];

			             	// 검색된 장소 위치를 기준으로 지도 범위를 재설정합니다
			                map.setBounds(bounds);
			             	
			                $("#data_list").append(SearchDataListHtml);
							
							$(".ListData").on("click", function(e) {
								fn_moveToMarker($(this));
							});
						
						} else {	// DataList.length < 0
							alert("검색 결과가 없습니다.");
						}
					}
				});
			} else {
				alert("검색어를 입력후 검색해주세요.")
				return false;
			}	
			
		}
		
		function fn_listMarker_init() {
			$("#search_keyWord").val("");
			
			for(i=0; i<SearchMarker.length; i++) {		// 검색된 마커들 초기화
				SearchMarker[i].setMap(null);
			}
			SearchMarker = [];
			
			$("#data_list").children().remove();
			
			for(i=0; i<DataListLength; i++) {	// 마커 초기화
				if ($("#checkID_" + i).prop('checked') == true) {	// 체크되어있으면
					fn_removeMarker(i);
				}
			}
			
			 $('input:checkbox[name="DataListName"]').each(function() {
			 	this.checked = false; // 체크 해제 처리
			 });
		}
	</script>
</body>
</html>