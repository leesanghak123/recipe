<template>
  <div class="container mt-3">
    <!-- 검색창과 버튼 -->
    <!-- v-model : 데이터를 바인딩 하는 부분 -->
    <!-- class : style -->
    <div class="input-group mb-3">
      <input
        type="text"
        v-model="searchQuery"
        class="form-control"
        placeholder="검색어를 입력하세요"
        aria-label="Search Input"
        aria-describedby="search-button"
      />
      <button
        class="btn btn-primary"
        type="button"
        id="search-button"
        @click="searchPlaces"
      >
        검색
      </button>
    </div>

    <!-- 지도를 표시할 영역 -->
    <div id="map" style="width: 100%; height: 500px; margin-top: 10px;"></div>
  </div>
</template>

<script>
export default {
  name: "KakaoMap",
  data() {
    return {
      map: null, // Kakao Map 객체
      placesService: null, // Kakao Places 검색 서비스 객체
      infowindow: null, // 인포윈도우 객체 (마커를 클릭했을 때)
      searchQuery: "", // 검색어
      markers: [],  // marker를 임시로 저장할 배열(배열에 넣고 이전 기록들을 순회하면서 삭제할 예정)
    };
  },
  mounted() {
    this.loadKakaoMap(); // 컴포넌트 마운트 시 지도 초기화
  },
  methods: {
    loadKakaoMap() {
      // 카카오 지도 API 로드를 위한 스크립트 동적 생성
      const script = document.createElement("script"); // document.createElement: DOM에 새로운 HTML요소를 동적으로 생성하는 JS메서드
      script.src = `https://dapi.kakao.com/v2/maps/sdk.js?appkey=${process.env.VUE_APP_KAKAO_API_KEY}&libraries=services&autoload=false`;
      document.head.appendChild(script);  // document.head: 문서 내의 <head> 요소, appendChild: 자식 요소 하나 추가
      // 추가 설명: script는 head에 넣어서 미리 문서를 로드하기 전에 제공되어야한다.(외부라이브러리)
      // 추가 설명: head에 script를 추가하는 것이기 때문에 부모요소인 head에 자식 요소인 script 태그를 추가하는 개념

      // API 로드 후 카카오 지도를 생성하여 초기화
      script.onload = () => { // script가 실행 된 후 실행
        kakao.maps.load(() => { // kakao에서 제공해주는 함수, 지도 초기화에 사용
          // 지도 설정
          const container = document.getElementById("map"); // html에서 map이라는 id를 가진 요소를 가져옴
          const options = {
            center: new kakao.maps.LatLng(33.450701, 126.570667), // 기본 위치
            level: 3, // 기본 확대 레벨
          };
          // 위에서 만든 data에 관련 객체 초기화
          this.map = new kakao.maps.Map(container, options); // 지도 생성
          this.placesService = new kakao.maps.services.Places(); // 장소 검색 객체 생성
          this.infowindow = new kakao.maps.InfoWindow({ zIndex: 1 }); // 인포윈도우 생성 (zIndex : 인포 윈도우의 우선순위)
        });
      };
    },
    searchPlaces() {
      if (!this.searchQuery.trim()) {
        alert("검색어를 입력하세요!");
        return;
      }

      // 장소 검색 수행
      this.placesService.keywordSearch(this.searchQuery, (data, status) => { // keywordSearch: 검색어 검색 수행 메서드, searchQuery를 기반으로 검색, data와 status를 반환하라는 뜻
        if (status === kakao.maps.services.Status.OK) { // 응답이 200 인 경우
          this.displayPlaces(data); // 검색 결과 표시
        } else if (status === kakao.maps.services.Status.ZERO_RESULT) {
          alert("검색 결과가 없습니다.");
        } else {
          alert("검색 중 오류가 발생했습니다.");
        }
      });
    },
    displayPlaces(places) { // places : 결과 data값
      const bounds = new kakao.maps.LatLngBounds(); // 지도 범위 객체 (검색 기반으로 범위를 동적 조정)

      // 기존 마커 제거
      if(this.markers.length > 0) {
        this.markers.forEach((marker) => marker.setMap(null)); // 지도에서 제거
      }
      this.markers = []; // 만약 이전 배열을 참조하는게 있다면 완전히 제거하기 위함

      // 이전 마커와 인포윈도우 제거
      this.map.setLevel(3); // 기본 확대 수준으로 복구 (초기 지도 범위)
      this.infowindow.close(); // 기존 인포윈도우 닫기

      places.forEach((place) => {  // 장소 데이터를 순회하면서
        const position = new kakao.maps.LatLng(place.y, place.x); // 장소 좌표 (위도, 경도)
        bounds.extend(position); // 좌표를 지도 범위에 추가

        // 마커 생성 및 지도에 추가
        const marker = new kakao.maps.Marker({
          map: this.map,  // 지도 객체
          position,  // 좌표 객체
        });

        // 마커를 markers 배열에 추가
        this.markers.push(marker);

        // 마커 클릭 이벤트
        kakao.maps.event.addListener(marker, "click", () => {
          this.infowindow.setContent(
            `<div style="padding:5px; font-size:12px;">${place.place_name}</div>`
          );
          this.infowindow.open(this.map, marker); // 인포윈도우(정보 창)를 마커 위에 표시
        });
      });

      // 지도 범위를 검색 결과에 맞게 조정
      this.map.setBounds(bounds);
    },
  },
};
</script>

<style scoped>
</style>