<template>
  <div class="board-detail-container">
    <h2 class="board-detail-title">{{ board.title }}</h2>

    <div class="board-detail-meta">
      <p>작성자: {{ board.username }}</p>
      <p>작성일: {{ formatDate(board.createDate) }}</p>
      <p>조회수: {{ board.count }}</p>
      <p>추천수: {{ board.likes }}</p>

      <div v-if="isAuthor" class="author-badges">
        <span @click="editPost" class="badge badge-edit">수정</span>
        <span @click="deletePost" class="badge badge-delete">삭제</span>
      </div>
    </div>

    <div class="board-detail-content">
      <div v-html="board.content"></div> <!-- HTML을 실제로 렌더링 -->
    </div>

    <div class="button-group">
      <button @click="goToList" class="btn btn-list">목록</button>
    </div>
  </div>
</template>

<script>
import axios from "axios";

export default {
  data() {
    return {
      board: {}, // 게시글 데이터
      isAuthor: false, // 작성자인지 여부
    };
  },
  methods: {
    async fetchBoard() {
      try {
        const token = localStorage.getItem("jwt");
        const response = await axios.get(
          `http://localhost:8002/api/boards/${this.$route.params.id}`, // 백틱, $route.params.id: 현재 Detail의 url에 있는 id를 넣고 서버로 호출
          {
            headers: { Authorization: `Bearer ${token}` },
          }
        );

        this.board = response.data;

        // JWT 디코딩하여 작성자인지 확인
        const parts = token.split("."); // const: 변하지않는 값(상수)를 넣는 자료형, 여러 값 넣기 가능(배열)
        if (parts.length === 3) { // parts의 길이가 3인지 확인, ===: 값과 타입까지 비교, == 타입은 달라도 값만 비교, "3"인 경우를 방지
          // JWT 페이로드 디코딩
          const payload = JSON.parse(atob(parts[1])); // atob: base64 디코딩, JSON.parse: 문자열을 json 객체로 변환
          const currentUser = payload.username; // 'username' 필드 추출
          
          // 작성자 확인
          this.isAuthor = this.board.username === currentUser;
        } else {
          console.error("JWT 형식이 잘못되었습니다.");
        }
      } catch (error) {
        console.error("게시글 불러오기 실패:", error);
      }
    },

    formatDate(dateString) {
      const options = { year: "numeric", month: "2-digit", day: "2-digit" };
      return new Date(dateString).toLocaleDateString("ko-KR", options);
    },

    goToList() {
      this.$router.push("/"); // 목록 페이지로 이동
    },

    editPost() {
      this.$router.push(`/board/update/${this.board.id}`);
    },

    async deletePost() {
      if (confirm("정말 삭제하시겠습니까?")) {
        try {
          const token = localStorage.getItem("jwt");
          await axios.delete(`http://localhost:8002/api/boards/delete/${this.board.id}`, {
            headers: { Authorization: `Bearer ${token}` },
          });

          alert("게시글이 삭제되었습니다.");
          this.$router.push("/"); // 목록 페이지로 리다이렉트
        } catch (error) {
          console.error("게시글 삭제 실패:", error);
        }
      }
    },
  },
  mounted() {
    this.fetchBoard(); // 게시글 데이터와 작성자 정보를 불러옴
  },
};
</script>

<style scoped>
/* 전체 컨테이너 고정 */
.board-detail-container {
  width: 800px; /* 고정된 너비 */
  margin: 0 auto; /* 화면 중앙 정렬 */
  padding: 20px;
  background-color: #f9f9f9;
  border-radius: 8px;
  box-shadow: 0 4px 8px rgba(0, 0, 0, 0.1);
}

/* 제목 스타일 */
.board-detail-title {
  font-size: 1.5rem;
  font-weight: bold;
  text-align: center;
  margin-bottom: 20px;
  color: #333;
}

/* 메타 정보 */
.board-detail-meta {
  font-size: 0.9rem;
  color: #555;
  margin-bottom: 20px;
  border-bottom: 1px solid #ddd;
  padding-bottom: 10px;
  display: flex;
  justify-content: space-between;
  align-items: center; /* 수평 정렬 */
}

.board-detail-meta p {
  margin: 0;
}

/* 본문 내용 고정 */
.board-detail-content {
  width: 100%; /* 부모의 너비에 맞춤 */
  height: 300px; /* 고정된 높이 */
  padding: 15px;
  background-color: #fff;
  border: 1px solid #ddd;
  border-radius: 4px;
  overflow-y: auto; /* 넘칠 경우 세로 스크롤 */
  overflow-x: hidden; /* 가로 스크롤 숨기기 */
  box-sizing: border-box; /* 패딩 포함 */
}

/* 버튼 그룹 */
.button-group {
  display: flex;
  flex-direction: column; /* 목록 버튼과 수정/삭제 버튼을 세로 정렬 */
  align-items: center; /* 중앙 정렬 */
  margin-top: 20px;
}

button {
  padding: 10px 20px;
  border: none;
  border-radius: 4px;
  cursor: pointer;
  font-size: 14px;
  transition: background-color 0.3s ease;
}

button.btn-list {
  background-color: #ccc; /* 회색 */
  color: black;
  margin-bottom: 10px; /* 수정/삭제 버튼과 간격 */
}

button.btn-list:hover {
  background-color: #bbb;
}

/* 배지 스타일 */
.author-badges {
  display: flex;
  gap: 10px; /* 배지 간 간격 */
  align-items: center;
}

.badge {
  padding: 6px 12px;
  font-size: 12px;
  font-weight: bold;
  border-radius: 12px;
  cursor: pointer;
  display: inline-block;
  transition: background-color 0.3s ease;
}

.badge:hover {
  opacity: 0.8;
}

.badge-edit {
  background-color: #6c757d; /* 중립적인 회색 */
  color: white;
}

.badge-edit:hover {
  background-color: #5a6268; /* 어두운 회색 */
}

.badge-delete {
  background-color: #dc3545; /* 경고 빨간색 */
  color: white;
}

.badge-delete:hover {
  background-color: #c82333; /* 어두운 빨간색 */
}
</style>