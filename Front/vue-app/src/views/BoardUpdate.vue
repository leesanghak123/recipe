<template>
  <div class="container mt-5"> <!-- 중앙정렬, 상단 5마진 -->
    <div class="card shadow-sm">
      <div class="card-body">
        <!-- submit: 이벤트가 발생했을 때, prevent 비동기(새로고침X)로 실행 -->
        <form @submit.prevent="submitForm">
          <div class="form-group mb-4">
            <label for="title" class="form-label fw-bold">제목</label>
            <input
              id="title"
              type="text"
              class="form-control"
              placeholder="Enter title"
              v-model="board.title"
              required
            />
          </div>

          <!-- Summernote가 적용될 div -->
          <div id="summernote"></div>

          <div class="text-end mt-4">
            <button class="btn btn-beige btn-sm px-3" type="submit">저장</button>
          </div>
        </form>
      </div>
    </div>
  </div>
</template>

<script>
import axios from "axios";
import $ from "jquery";

export default {
  name: "BoardUpdate",
  data() {
    return {
      board: {
        title: "",
        content: "",
      },
    };
  },
  mounted() {
    this.fetchBoard(); // 게시글 정보 가져오기
  },
  methods: {
    async fetchBoard() {
      try {
        const boardId = this.$route.params.id; // URL에서 게시글 ID 가져오기
        const token = localStorage.getItem("jwt");

        // 게시글 정보 가져오기
        const response = await axios.get(`http://localhost:8002/api/boards/${boardId}`, {
          headers: {
            Authorization: `Bearer ${token}`,
          },
        });

        // 가져온 게시글 데이터 설정
        this.board.title = response.data.title;
        this.board.content = response.data.content;

        // Summernote 에디터에 기존 게시글 내용 설정
        $("#summernote").summernote("code", this.board.content);
        
      } catch (error) {
        console.error("게시글 불러오기 실패:", error);
      }
    },
    async submitForm() {
      try {
        // Summernote 에디터 내용 HTML로 변환
        this.board.content = $("#summernote").summernote("code");

        // JWT 토큰 가져오기
        const token = localStorage.getItem("jwt");

        if (!token) {
          console.error("JWT 토큰이 없습니다. 로그인이 필요합니다.");
          this.$router.push("/auth/loginForm");
          return;
        }

        // 게시글 수정 요청
        const boardId = this.$route.params.id;
        const response = await axios.patch(
          `http://localhost:8002/api/boards/update/${boardId}`, // 수정 API
          this.board,
          {
            headers: {
              Authorization: `Bearer ${token}`,
            },
          }
        );

        // 성공 시 알림 및 이동
        alert("글이 성공적으로 수정되었습니다!"); // 알림 표시
        console.log("글 수정 성공:", response);
        this.$router.push(`/board/detail/${boardId}`); // 수정된 게시글로 이동
      } catch (error) {
        console.error("글 수정 실패:", error);
        alert("글 수정에 실패했습니다. 다시 시도해주세요."); // 실패 알림
      }
    },
  },
};
</script>

<style scoped>
.container {
  max-width: 800px;
}

.card {
  border-radius: 10px;
}

.form-control {
  border-radius: 8px;
  border: 1px solid #ced4da;
}

.form-control:focus {
  border-color: #007bff;
  box-shadow: 0 0 5px rgba(0, 123, 255, 0.5);
}

.btn-beige {
  background-color: #f5f5dc;
  color: #333;
  border: none;
}

.btn-beige:hover {
  background-color: #eae0c8;
  color: #000;
}
</style>