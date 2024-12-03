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
      <button @click="goToList" class="btn btn-list">목록으로</button>
    </div>

    <!-- 댓글 섹션 -->
    <div class="comment-section">
      <h3>댓글</h3>

      <!-- 댓글 입력 -->
      <div class="comment-input">
        <textarea v-model="newReply" placeholder="댓글을 입력하세요"></textarea>
        <button @click="postComment">등록</button>
      </div>

      <!-- 댓글 목록 -->
      <div class="comment-list">
        <div v-for="comment in board.reply" :key="comment.id" class="comment-item">
          <p class="comment-content">{{ comment.content }}</p>
          <p class="comment-meta">
            {{ comment.user.username }} · {{ formatDate(comment.createDate) }}
            <span v-if="isCommentAuthor(comment)" @click="deleteComment(comment.id)" class="delete-btn">삭제</span>
          </p>
        </div>
      </div>
    </div>
  </div>
</template>

<script>
import axios from "axios";

export default {
  data() {
    return {
      board: {}, // 게시글 데이터
      newReply: "", // 새 댓글 입력 필드
      isAuthor: false, // 게시글 작성자인지 여부
    };
  },
  methods: {
    // 게시글 데이터 가져오기 (댓글 포함)
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
        const parts = token.split("."); // const: 변하지 않는 값(상수)를 넣는 자료형, 여러 값 넣기 가능(배열)
        if (parts.length === 3) { // parts의 길이가 3인지 확인, ===: 값과 타입까지 비교, ==: 타입은 달라도 값만 비교, "3"인 경우를 방지 
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

    // 댓글 작성
    async postComment() {
      if (!this.newReply.trim()) { // null이면, trim 공백을 제거(띄어쓰기만 한 경우)
        alert("댓글을 입력하세요.");
        return;
      }

      try {
        const token = localStorage.getItem("jwt");
        await axios.post(
          `http://localhost:8002/api/reply/write/${this.$route.params.id}`,
          {
            content: this.newReply,
          },
          {
            headers: { Authorization: `Bearer ${token}` },
          }
        );

        this.newReply = ""; // 입력 초기화
        this.fetchBoard(); // 댓글 포함된 게시글 데이터 다시 불러오기, 데이터 갱신
      } catch (error) {
        console.error("댓글 작성 실패:", error);
      }
    },

    // 댓글 삭제
    async deleteComment(commentId) {
      if (confirm("댓글을 삭제하시겠습니까?")) {
        try {
          const token = localStorage.getItem("jwt");
          await axios.delete(`http://localhost:8002/api/reply/delete/${commentId}`, {
            headers: { Authorization: `Bearer ${token}` },
          });

          this.fetchBoard(); // 댓글 포함된 게시글 데이터 다시 불러오기
        } catch (error) {
          console.error("댓글 삭제 실패:", error);
        }
      }
    },

    // 댓글 작성자인지 확인
    isCommentAuthor(comment) {
      const token = localStorage.getItem("jwt");
      const parts = token.split(".");
      if (parts.length === 3) {
        const payload = JSON.parse(atob(parts[1]));
        return comment.user.username === payload.username;
      }
      return false;
    },

    // 날짜 포맷
    formatDate(dateString) {
      const options = { year: "numeric", month: "2-digit", day: "2-digit" };
      return new Date(dateString).toLocaleDateString("ko-KR", options);
    },

    // 페이지 이동
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
    this.fetchBoard(); // 게시글 데이터와 댓글 포함된 게시글 데이터 불러오기
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
  background-color: #007bff;
  color: white;
  border-radius: 4px;
  cursor: pointer;
}

.badge:hover {
  background-color: #0056b3;
}

.badge-delete {
  background-color: #dc3545;
}

.badge-delete:hover {
  background-color: #c82333;
}

/* 댓글 섹션 스타일 */
.comment-section {
  margin-top: 30px;
}

.comment-section h3 {
  font-size: 1.2rem;
  margin-bottom: 10px;
}

.comment-input {
  display: flex;
  flex-direction: column;
  margin-bottom: 20px;
}

.comment-input textarea {
  width: 100%;
  height: 80px;
  margin-bottom: 10px;
  padding: 10px;
  border: 1px solid #ddd;
  border-radius: 4px;
  resize: none;
}

.comment-input button {
  align-self: flex-end;
  padding: 8px 16px;
  background-color: #007bff;
  color: white;
  border: none;
  border-radius: 4px;
  cursor: pointer;
}

.comment-input button:hover {
  background-color: #0056b3;
}

.comment-list {
  margin-top: 20px;
}

.comment-item {
  padding: 10px;
  border-bottom: 1px solid #eee;
}

.comment-content {
  margin: 0;
}

.comment-meta {
  font-size: 0.8rem;
  color: #777;
}

.delete-btn {
  margin-left: 10px;
  color: #dc3545;
  cursor: pointer;
}

.delete-btn:hover {
  text-decoration: underline;
}
</style>