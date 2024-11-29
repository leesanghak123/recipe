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
  name: "BoardWrite",
  data() {
    return {
      board: {
        title: "",
        content: "",
      },
    };
  },
  mounted() {
    // Summernote 초기화
    $("#summernote").summernote({
      placeholder: "Write something here...", // 표시되는 Text
      tabsize: 2, // 탭 크기(tab을 눌렀을 때 2칸 띄어짐)
      height: 300, // 높이
      // toolbar: [ // 이건 기본값으로 할래
      //   ["style", ["style"]],
      //   ["font", ["bold", "italic", "underline", "clear"]],
      //   ["fontname", ["fontname"]],
      //   ["fontsize", ["fontsize"]],
      //   ["color", ["color"]],
      //   ["para", ["ul", "ol", "paragraph"]],
      //   ["table", ["table"]],
      //   ["insert", ["link", "picture", "video"]],
      //   ["view", ["fullscreen", "codeview", "help"]],
      // ],
    });
  },
  methods: {
    async submitForm() {
      try {
        // Summernote 에디터 내용 HTML로 변환
        this.board.content = $("#summernote").summernote("code");

        // JWT 토큰 가져오기 (로컬 스토리지에서 가져온다고 가정)
        const token = localStorage.getItem("jwt");

        // 토큰이 없을 경우 처리
        if (!token) {
          console.error("JWT 토큰이 없습니다. 로그인이 필요합니다.");
          this.$router.push("/auth/loginForm");
          return;
        }

        // Axios 요청 보내기
        const response = await axios.post(
          "http://localhost:8002/api/boards/write", // API URL
          this.board, // 게시글 데이터 (title, content)
          {
            headers: {
              Authorization: `Bearer ${token}`, // JWT 토큰 추가
            },
          }
        );

        // 성공 시 로그 출력 및 메인 화면으로 이동
        console.log("글 작성 성공:", response);
        this.$router.push("/");
      } catch (error) {
        // 에러 처리
        console.error("글 작성 실패:", error);
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